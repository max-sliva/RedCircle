import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class RedMain {
	private static JFrame mainFrame;
	static BufferedImage myPicture = null;

	public static void main(String[] args) {
//		consoleTest();
		guiTest();
	}

	private static void guiTest() {
		mainFrame = new JFrame("RedTargetTest");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MyLabel imageLabel = new MyLabel();
		String fileName = "target.png";
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
		imageLabel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				resizeImage(imageLabel, myPicture, imgIcon);
			}
		});
	}

	private static void resizeImage(MyLabel imageLabel, BufferedImage myPicture, ImageIcon imgIcon) {
		float dHeight = imageLabel.getHeight() / (float) myPicture.getHeight();
		int newWidth = (int) (myPicture.getWidth() * dHeight);
		Image dimg = myPicture.getScaledInstance(newWidth, imageLabel.getHeight(), Image.SCALE_SMOOTH);
		imgIcon.setImage(dimg);
		RedSearch redSearch = new RedSearch(myPicture);
		Circle circle = redSearch.getCircle();
		imageLabel.drawCircle(circle.getX(), circle.getY(), circle.getRadius(), dHeight);
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
