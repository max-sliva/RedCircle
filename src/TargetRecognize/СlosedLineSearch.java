package TargetRecognize;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class СlosedLineSearch {
	private String path;
	private BufferedImage image;
	
	public СlosedLineSearch(BufferedImage img) {
		image = img;
	}
	
	public MyPoint[] boundCircleSearch() { //ф-ия для нахождения внешнего круга
		ArrayList<MyPoint> pointsList = new ArrayList<MyPoint>();
		
		MyPoint[] points = new MyPoint[pointsList.size()];
		int i = 0;
		for (MyPoint point : pointsList) {
			points[i] = point;
			i++;
		}
//		return (MyPoint[]) pointsList.toArray();
		return points;
	}

}
