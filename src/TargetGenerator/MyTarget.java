package TargetGenerator;

import TargetRecognize.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MyTarget extends JPanel {
	float dHeight = 1;
	private boolean drawCircles = false;
	private boolean drawDot = false;
	ArrayList<Circle> circlesForTarget = new ArrayList<Circle>() ;
	private boolean saveFile = false;
	Color backColor = Color.white;	
	Color circleColor = Color.BLACK;	
	int circleRadius = 10;
	int thickness = 10;
	private MyPoint redPoint;
	private int curWidth = 0;
	private int curHeight = 0;
	private String targetName = "";
	
	public MyTarget() {
		super();
	}
	
	public MyTarget(String name) {
		super();
		targetName = name;
	}

	public void drawTarget(ArrayList<Circle> circles) {
		this.circlesForTarget = circles;
		drawCircles = true;
		repaint();
	}
	
	public void drawRedDot(int x, int y) {
		drawDot = true;
		redPoint = new MyPoint(x, y);
		repaint();
	}
	
	public void setBackColor(int grayValue) {
		backColor = new Color(grayValue, grayValue, grayValue);
//		backColor = 
	}
	
	public void setCircleColor(int grayValue) {
		circleColor	= new Color(grayValue, grayValue, grayValue);
	}
	
	public void setThickness(int thick) {
		thickness = thick;
	}

	public void setCircleRadius(int circleIndex , int value) {
		circleRadius = value;
		circlesForTarget.get(circleIndex).setRadius(circleIndex);
	}
	
	public void saveToFile() {
//		drawCircles = false;
		saveFile = true;
		repaint();
	}
	
	public int getCurrentWidth() {
		return curWidth;
	}

	public int getCurrentHeight() {
		return curHeight;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D gr2D = (Graphics2D)g;
		BasicStroke pen;
//		gr2D.setColor(Color.WHITE);
		gr2D.setBackground(backColor);
//		gr2D.drawRect(0, 0, this.getWidth(), this.getHeight());
		curWidth = getSize().width;
		curHeight = getSize().height;
		System.out.println(targetName+" size = " + getCurrentWidth() +" x "+ getCurrentHeight() +" ");
		gr2D.clearRect(0, 0, getSize().width, getSize().height);
		gr2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (drawCircles ) {
			pen = new BasicStroke(thickness);
			gr2D.setStroke(pen);
			gr2D.setColor(circleColor);
			System.out.println("circles size = "+circlesForTarget.size());
			for (Circle circle : circlesForTarget) {
				gr2D.setStroke(new BasicStroke(circle.getThickness()));
				int d = (int) (circle.getRadius()*2*dHeight);
				int R = (int) (circle.getRadius()*dHeight);
				int X = (int) (circle.getX()*dHeight);
				int Y = (int) (circle.getY()*dHeight);
				System.out.println("x = " + X + " y = " + Y + " r = " + R);
				if (circle.getIsClicked()) gr2D.setColor(Color.yellow);
				else gr2D.setColor(circleColor);
				gr2D.drawOval(X-R, Y-R, d, d);				
			}
		}
		if (drawDot) {
			pen = new BasicStroke(2);
			gr2D.setStroke(pen);
			gr2D.setColor(Color.red);
			gr2D.fillOval(redPoint.getX()-3, redPoint.getY()-3, 6, 6);
		}
//		if (saveFile ) {
//			System.out.println("in paint saving size = "+getWidth()+" x "+ getHeight());
//			BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
//			Graphics2D g2 = (Graphics2D)image.getGraphics();
//			saveFile = false;
//	        paint(g2);
//	        
//	        try {
//				ImageIO.write(image, "png", new File("canvas.png"));
//			} catch (Exception e) {
//				 e.printStackTrace();
//			}
//	        saveFile = false;
//		}
	}
}
