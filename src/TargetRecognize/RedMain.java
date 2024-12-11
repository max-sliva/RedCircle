package TargetRecognize;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Timer;

public class RedMain {
	private static JFrame mainFrame;
	static BufferedImage myPicture = null;
	 private static Timer timer;

	public static void main(String[] args) {
//		consoleTest();
		guiTest();
	}

	private static void guiTest() {

		mainFrame = new JFrame("RedTargetTest");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MyLabel imageLabel = new MyLabel();
		String fileName = "imageTarget4.png";
		try {
			myPicture = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ImageIcon imgIcon = new ImageIcon(myPicture);
		imageLabel.setIcon(imgIcon);
		mainFrame.add(imageLabel, BorderLayout.CENTER);
		mainFrame.setSize(800, 600);
		mainFrame.setVisible(true);
		resizeImage(imageLabel, myPicture, imgIcon);
		mainFrame.setLocationRelativeTo(null);
		timer = new Timer(50, e -> {
			System.out.println("Resize action performed!");
			resizeImage(imageLabel, myPicture, imgIcon);
		});
		timer.setRepeats(false); // Only execute once after resizing stops

//		mainFrame.addComponentListener(new ComponentAdapter() {
//			@Override
//			public void componentResized(ComponentEvent e) {
//				timer.restart();
////				System.out.println("--!!resized!!--");
//			}
//		});
		imageLabel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				timer.restart();
//				resizeImage(imageLabel, myPicture, imgIcon);
			}
		});
	}

	private static void resizeImage(MyLabel imageLabel, BufferedImage myPicture, ImageIcon imgIcon) {
		float dHeight = imageLabel.getHeight() / (float) myPicture.getHeight();
		int newWidth = (int) (myPicture.getWidth() * dHeight);
		Image dimg = myPicture.getScaledInstance(newWidth, imageLabel.getHeight(), Image.SCALE_SMOOTH);
		imgIcon.setImage(dimg);
		RedSearch redSearch = new RedSearch(myPicture);
		Circle circle = redSearch.getCircle(); //находим внешний круг
		imageLabel.drawCircle(circle.getX(), circle.getY(), circle.getRadius(), dHeight);
		System.out.println("--- inner circles search ---");
		ArrayList<Circle> circlesList = redSearch.getCircles(circle); //находим все внутренние круги
		imageLabel.drawCircles(circlesList);
		//circlesList.add(circle); //если нужен список со всеми кругами
	}

	private static void consoleTest() {
		RedSearch redSearch = new RedSearch("img.png");
		redSearch.findRedPoints();
		redSearch = new RedSearch("img2.png");
		redSearch.findRedPoints();
		redSearch = new RedSearch("target.png");
		redSearch.boundCircleSearch();
	}

}
