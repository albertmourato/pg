import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.JTextComponent;

public class Points extends JPanel implements MouseListener {
	public static ArrayList<MyPoint> controlPoints = new ArrayList<MyPoint>();
	public static ArrayList<MyPoint> pointsOfCurve = new ArrayList<MyPoint>();
	static Graphics2D g2d;
	static Graphics2D g2;
	static final double radius = 10;
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g2d = (Graphics2D) g;

		g2d.setColor(Color.black);
		g2d.setStroke(new BasicStroke(1));
		
		for (int i = 0; i < controlPoints.size(); i++) {
			MyPoint.drawPoint(controlPoints.get(i));
		}
		
		for (int i = 0; i < controlPoints.size() - 1; i++) {
			//System.out.println("i = "+i+" size = " +(pointsOfCurve.size()));
			g2d.drawLine((int) controlPoints.get(i).getX(), (int) controlPoints.get(i).getY(), (int) controlPoints.get(i + 1).getX(),
					(int) controlPoints.get(i + 1).getY());
			 //System.out.println((int)controlPoints.get(i).getX()+" "+ (int)controlPoints.get(i).getY()+" "+ (int)controlPoints.get(i + 1).getX()+" "+ (int)controlPoints.get(i + 1).getY());
		}


		if (pointsOfCurve.size() > 2) {
			g2d.setColor(Color.RED);
			for (int i = 0; i < pointsOfCurve.size() - 1; i++) {
				//System.out.println("i = "+i+" size = " +(pointsOfCurve.size()));
				g2d.drawLine((int) pointsOfCurve.get(i).getX(), (int) pointsOfCurve.get(i).getY(), (int) pointsOfCurve.get(i + 1).getX(),
						(int) pointsOfCurve.get(i + 1).getY());
				 //System.out.println((int)controlPoints.get(i).getX()+" "+ (int)controlPoints.get(i).getY()+" "+ (int)controlPoints.get(i + 1).getX()+" "+ (int)controlPoints.get(i + 1).getY());
			}
		}
		pointsOfCurve.clear();
		//controlPoints.clear();
	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		MyPoint p1 = new MyPoint(x, y);
		if (e.getButton() == MouseEvent.BUTTON1) {
			controlPoints.add(p1);
			pointsOfCurve = castejour(100, controlPoints);
			repaint();
		}

	}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	
	//Returns t controlPoints that from curve
	public ArrayList<MyPoint> castejour(double numberOfPoints, ArrayList<MyPoint> controlPoints) {
		double t = (double) 1 / numberOfPoints;
		double counter = t;
		ArrayList<MyPoint> returnSetOfPoints = new ArrayList<MyPoint>();
		
		//add first point from original control controlPoints
		returnSetOfPoints.add(controlPoints.get(0));
	
		while (counter <=1) {
			returnSetOfPoints.add(bezierPointInCurve(counter, controlPoints));
			counter += t;
		}
		
//		I think it's wrong missing the last point of control
//		returnSetOfPoints.add(controlPoints.get(controlPoints.size()-1));
		
		
		return returnSetOfPoints;
	}

//		Returns a point in curve
	public MyPoint bezierPointInCurve(double t, ArrayList<MyPoint> cPoints) {
		int n = cPoints.size();
		// Point currentPoint = new Point();

		ArrayList<MyPoint> temp = new ArrayList<MyPoint>();
		ArrayList<MyPoint> controlPointsTemp = (ArrayList<MyPoint>) cPoints.clone();
		int k = n;
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; (j < k-1); j++) {
				// Linear interpolation
				MyPoint a = controlPointsTemp.get(j);
				MyPoint b = controlPointsTemp.get(j + 1);
				MyPoint auxPoint = new MyPoint(0, 0);
				if (a != null && b != null) {
					auxPoint = MyPoint.sumPoints(MyPoint.multiplyPointByConstant((1 - t), a), MyPoint.multiplyPointByConstant(t, b));
					temp.add(auxPoint);
				}
			}
			controlPointsTemp = (ArrayList<MyPoint>) temp.clone();
//			if (controlPointsTemp.size() == 1)
//				return controlPointsTemp.get(0);
			temp.clear();
			k = controlPointsTemp.size();
		}
		System.out.println("TAMANHO "+controlPointsTemp.size());
		//it has to return just one point
		return controlPointsTemp.get(0);
	}
	
	

	public static void main(String[] args) {
		Points controlPoints = new Points();
		JFrame frame = new JFrame("Points");
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Configurar");
		JMenuItem menuItem = new JMenuItem("Adicionar Pontos");
		JTextField textField = new JTextField();
		JPanel panelMenu = new JPanel();
		textField.setBackground(Color.WHITE);
		textField.setToolTipText("Digite a quantidade de pontos de controle:");
		textField.setSize(100, 30);
		panelMenu.setSize(100, 30);
		panelMenu.add(textField);
		menu.add(menuItem);

		menuBar.add(menu);
		menuBar.setBackground(Color.GRAY);
		menuBar.setPreferredSize(new Dimension(200, 30));

		// frame.setJMenuBar(menuBar);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(controlPoints);
		frame.setSize(1000, 700);
		frame.setLocationRelativeTo(controlPoints);

		frame.setVisible(true);
		frame.addMouseListener(controlPoints);
		frame.add(panelMenu);

		
	}
}
