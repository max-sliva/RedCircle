package TargetGenerator;

import TargetRecognize.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class MyTargetLabel extends JLabel {
	float dHeight = 1;
	private boolean drawCircles = false;
	ArrayList<Circle> circlesForTarget = new ArrayList<Circle>() ;
	private boolean saveFile = false;

	public MyTargetLabel() {
		super();
	}

	public void drawTarget(ArrayList<Circle> circles) {
		this.circlesForTarget = circles;
		drawCircles = true;
		repaint();
	}
	
	
	public void saveToFile() {
//		drawCircles = false;
		saveFile = true;
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D gr2D = (Graphics2D)g;
		BasicStroke pen;
//		gr2D.setColor(Color.WHITE);
		gr2D.setBackground(Color.WHITE);
//		gr2D.drawRect(0, 0, this.getWidth(), this.getHeight());
		gr2D.clearRect(0, 0, getSize().width, getSize().height);
		if (drawCircles ) {
			gr2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			pen = new BasicStroke(10);
			gr2D.setStroke(pen);
			gr2D.setColor(Color.BLACK);
			System.out.println("circles size = "+circlesForTarget.size());
			for (Circle circle : circlesForTarget) {
				int d = (int) (circle.getRadius()*2*dHeight);
				int R = (int) (circle.getRadius()*dHeight);
				int X = (int) (circle.getX()*dHeight);
				int Y = (int) (circle.getY()*dHeight);
				gr2D.drawOval(X-R, Y-R, d, d);
			}
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
