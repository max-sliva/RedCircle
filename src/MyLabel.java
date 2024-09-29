import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;

public class MyLabel extends JLabel{
	int circleR;
	int circleX;
	int circleY;
	boolean paintCircle = false;
	
	public void drawCircle(int x, int y, int r, float dHeight) {
		circleR = (int) (r*dHeight);
		circleX = (int) (x*dHeight);
		circleY = (int) (y*dHeight);
		paintCircle = true;
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D gr2D = (Graphics2D)g;
		BasicStroke pen;
		if (paintCircle) {
			float[] dash = {20, 20};
			gr2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			pen=new BasicStroke(10,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND, 10, dash, 1);
			gr2D.setStroke(pen);
			gr2D.setColor(Color.GREEN);
			gr2D.drawOval(circleX-circleR, circleY-circleR, 2*circleR, 2*circleR);
		}
	}
	
	

}
