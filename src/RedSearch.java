import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class RedSearch {
	private String path;
	private BufferedImage image;

	public RedSearch(BufferedImage img) {
		image = img;
	}

	public RedSearch(String filePath) {
		path = filePath;
//		BufferedImage img = null;
		File f = null;

		// read image
		try {
			f = new File(path);
			image = ImageIO.read(f);

		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public MyPoint[] findRedPoints() {
		// https://www.geeksforgeeks.org/image-processing-in-java-get-and-set-pixels/
//		Point[] points;
		int width = image.getWidth();
		int height = image.getHeight();
		ArrayList<MyPoint> pointsList = new ArrayList<MyPoint>();
		System.out.println("img width = " + width + " height = " + height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int p = image.getRGB(i, j);
				int r = (p >> 16) & 0xff; // get red
				int g = (p >> 8) & 0xff; // get green
				int b = p & 0xff; // get blue
				if (r > 200 && g < 50 && b < 50) {
//		        	System.out.println("red detected at x = "+i+", y = "+j);
					pointsList.add(new MyPoint(i, j));
				}
			}
		}
		MyPoint[] points = new MyPoint[pointsList.size()];
		int i = 0;
		for (MyPoint point : pointsList) {
			points[i] = point;
			i++;
		}
//		return (MyPoint[]) pointsList.toArray();
		return points;
	}

	public MyPoint[] boundCircleSearch() {
		ArrayList<MyPoint> pointsList = new ArrayList<MyPoint>();
		int width = image.getWidth();
		int height = image.getHeight();
		MyPoint lowerPoint;
		MyPoint upperPoint;
		MyPoint leftPoint;
		for (int j = 0; j < height; j++) {
			int p = image.getRGB(width / 2, j);
			int r = (p >> 16) & 0xff; // get red
			int g = (p >> 8) & 0xff; // get green
			int b = p & 0xff; // get blue
			if (r < 2 && g < 2 && b < 2) {
				System.out.println("black at top detected at y = " + j);
				upperPoint = new MyPoint(width / 2, j);
				pointsList.add(upperPoint);
				break;
			}
		}
		for (int j = height - 1; j > 1; j--) {
			int p = image.getRGB(width / 2, j);
			int r = (p >> 16) & 0xff; // get red
			int g = (p >> 8) & 0xff; // get green
			int b = p & 0xff; // get blue
			if (r < 2 && g < 2 && b < 2) {
				System.out.println("black at buttom detected at y = " + j);
				lowerPoint = new MyPoint(width / 2, j);
				pointsList.add(lowerPoint);
				break;
			}
		}
		for (int i = 1; i < width; i++) {
			int p = image.getRGB(i, height / 2);
			int r = (p >> 16) & 0xff; // get red
			int g = (p >> 8) & 0xff; // get green
			int b = p & 0xff; // get blue
			if (r < 2 && g < 2 && b < 2) {
				System.out.println("black at left detected at x = " + i);
				leftPoint = new MyPoint(i, height / 2);
				pointsList.add(leftPoint);
				break;
			}
		}
		MyPoint[] points = new MyPoint[pointsList.size()];
		int i = 0;
		for (MyPoint point : pointsList) {
			points[i] = point;
			i++;
		}
//		return (MyPoint[]) pointsList.toArray();
		return points;
	}

	public Circle getCircle(/*Point[] z*/) {
		//https://shra.ru/2019/10/koordinaty-centra-okruzhnosti-po-trem-tochkam/ 
		MyPoint[] z = boundCircleSearch();
		int a = z[1].getX() - z[0].getX();
		int b = z[1].getY() - z[0].getY();
		int c = z[2].getX() - z[0].getX();
		int d = z[2].getY() - z[0].getY();
		int e = a * (z[0].getX() + z[1].getX()) + b * (z[0].getY() + z[1].getY());
		int f = c * (z[0].getX() + z[2].getX()) + d * (z[0].getY() + z[2].getY());
		int g = 2 * (a * (z[2].getY() - z[1].getY()) - b * (z[2].getX() - z[1].getX()));
		if (g == 0) {
			// если точки лежат на одной линии,
			// или их координаты совпадают,
			// то окружность вписать не получится
			return null;
		}
		// координаты центра
		int Cx = (int) ((d * e - b * f) / (float) g);
		int Cy = (int) ((a * f - c * e) / (float) g);
		// радиус
		int R = (int) Math.sqrt(Math.pow(z[0].getX() - Cx, 2) + Math.pow(z[0].getY() - Cy, 2));
		// вернем параметры круга
		return new Circle(Cx, Cy, R);
	}

}
