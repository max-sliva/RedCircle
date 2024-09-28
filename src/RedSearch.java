import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RedSearch {
	private String path;
	private BufferedImage image;
	
	public RedSearch(BufferedImage img) {
		image = img;
	}

	public RedSearch(String path) {
		this.path = path;
//		BufferedImage img = null;
		File f = null;

		// read image
		try {
			f = new File(path);
			image = ImageIO.read(f);
			
		} catch (IOException e) {
			System.out.println(e);
		}

		// get image width and height
//		int width = image.getWidth();
//		int height = image.getHeight();
	}

	public void findRedPoints() {
		//https://www.geeksforgeeks.org/image-processing-in-java-get-and-set-pixels/ 
		int width = image.getWidth();
		int height = image.getHeight();
		System.out.println("img width = "+width+" height = "+height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int p = image.getRGB(i, j);
		        int r = (p >> 16) & 0xff; // get red
		        int g = (p >> 8) & 0xff; // get green 
		        int b = p & 0xff; // get blue 
		        if (r > 200 && g < 50 && b < 50) {
		        	System.out.println("red detected at x = "+i+", y = "+j);
		        }
			}
		}
	}

}
