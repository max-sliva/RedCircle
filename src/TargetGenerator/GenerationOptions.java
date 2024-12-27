package TargetGenerator;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

public class GenerationOptions extends JFrame{

	private static final long serialVersionUID = 1L;

	public GenerationOptions(String title) {
		super(title);
		JButton genBtn = new JButton("Generate targets");
		genBtn.addActionListener(e->{
			generateTargetImages(5, 1400, 1000);
		});
		Box centralBox = new Box(BoxLayout.Y_AXIS);
		Box genBtnBox = new Box(BoxLayout.X_AXIS);
		genBtnBox.add(genBtn);
		genBtnBox.add(Box.createHorizontalGlue());
		centralBox.add(genBtnBox);
		add(centralBox, BorderLayout.CENTER);
		//TODO добавить генерацию нужного кол-ва мишеней с разными параметрами в отдельную папку

	}
	
	private void generateTargetImages(int n, int w, int h) {
		int width = w;
        int height = h;
        int between = 40;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int radius = (width > height)? (height - 40) / 2 : (width - 40) / 2;  
		BasicStroke pen;
		// TODO добавить зависимость кол-ва файлов от толщины линии и расстояния между кругами
		for (int i = 0; i < n; i++) {
	        // Get the graphics context
	        Graphics2D g2d = bufferedImage.createGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        // Draw something on the image
	        pen = new BasicStroke(5);
			g2d.setStroke(pen);
	        g2d.setColor(Color.WHITE);
	        g2d.fillRect(0, 0, width, height);
	        g2d.setColor(Color.BLACK);
	        for (int j = 0; j<=i; j++) {
	        	int r = radius - between*j;
				int d = r*2;
				int X = width / 2;
				int Y = height / 2;
				g2d.drawOval(X-r, Y-r, d, d);				
	        }
	        // Dispose of the graphics context
	        g2d.dispose();
	        File outputfile = new File("out"+i+".png");
			try {
				ImageIO.write(bufferedImage, "png", outputfile);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
}
