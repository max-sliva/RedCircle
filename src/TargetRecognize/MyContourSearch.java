package TargetRecognize;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

class EdgeCoords{
	private int x;
	private int y;
	
	public EdgeCoords(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
}

class Contour{
	private ArrayList<EdgeCoords> edgeCoordsList = null;
	private EdgeCoords leftEdge;
	private EdgeCoords rightEdge;
	private EdgeCoords topEdge;
	private EdgeCoords buttomEdge;
	private int edgeWidth;

	public ArrayList<EdgeCoords> getEdgeCoordsList() {
		return edgeCoordsList;
	}

	public void setEdgeCoordsList(ArrayList<EdgeCoords> edgeCoordsList) {
		this.edgeCoordsList = edgeCoordsList;
	}
	
	public void addPointToEdgesArray(EdgeCoords point) {
		edgeCoordsList.add(point);
	}

	public EdgeCoords getLeftEdge() {
		return leftEdge;
	}

	public void setLeftEdge(EdgeCoords leftEdge) {
		this.leftEdge = leftEdge;
	}

	public EdgeCoords getRightEdge() {
		return rightEdge;
	}

	public void setRightEdge(EdgeCoords rightEdge) {
		this.rightEdge = rightEdge;
	}

	public EdgeCoords getTopEdge() {
		return topEdge;
	}

	public void setTopEdge(EdgeCoords topEdge) {
		this.topEdge = topEdge;
	}

	public EdgeCoords getButtomEdge() {
		return buttomEdge;
	}

	public void setButtomEdge(EdgeCoords buttomEdge) {
		this.buttomEdge = buttomEdge;
	}

	public int getEdgeWidth() {
		return edgeWidth;
	}

	public void setEdgeWidth(int edgeWidth) {
		this.edgeWidth = edgeWidth;
	}
}

public class MyContourSearch {
	private ArrayList<Contour> contoursList = null;
	
	public ArrayList<EdgeCoords> getContour(BufferedImage image, Color edgesColor, Color backColor) {
		ArrayList<EdgeCoords> coordsList = new ArrayList<>();
		Contour firstContour = new Contour();
		EdgeCoords topEdge = getTopEdge(image, edgesColor, backColor);
		firstContour.setTopEdge(topEdge);
		EdgeCoords buttomEdge = getButtomEdge(image, edgesColor, backColor);
		firstContour.setButtomEdge(buttomEdge);
		EdgeCoords leftEdge = getLeftEdge(image, edgesColor, backColor);
		firstContour.setLeftEdge(leftEdge);
		EdgeCoords rightEdge = getRightEdge(image, edgesColor, backColor);
		firstContour.setLeftEdge(rightEdge);
		
		drawRedCross(image, firstContour, "target_redcross.png");
		//TODO сделать поток для поиска контуров
		int contourWidth = getContourWidthFromTopEdge(image, topEdge, edgesColor, backColor); 
		System.out.println("contourWidth = "+contourWidth);
		return coordsList;
	}
	
	private void drawRedCross(BufferedImage image, Contour firstContour, String fileName) {
		int topX = firstContour.getTopEdge().getX();
		int leftY = firstContour.getLeftEdge().getY();
		for (int y = 0; y < image.getHeight(); y++) { 
			image.setRGB(topX, y, Color.RED.getRGB());
		}
		for (int x = 0; x < image.getWidth(); x++) { 
			image.setRGB(x, leftY, Color.RED.getRGB());
		}
		try {
			javax.imageio.ImageIO.write(image, "png", new java.io.File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Trouble with writing image");
		}
	}

	private int getContourWidthFromTopEdge(BufferedImage image, EdgeCoords topEdge, Color edgesColor, Color backColor) {
		int x = topEdge.getX();
		int y = topEdge.getY();
		Color curDotColor = getDotColor(image, x, y);
		int dotsFound = 0;
		while (curDotColor==edgesColor) {
			y++;
			curDotColor = getDotColor(image, x, y);
			dotsFound++;
		}

		return dotsFound;
	}

	private Color getDotColor(BufferedImage image, int x, int y) {
		Color curDotColor = null;
    	int p = image.getRGB(x, y);
		int r = (p >> 16) & 0xff; // get red
		int g = (p >> 8) & 0xff; // get green
		int b = p & 0xff; // get blue
		if (r < 2 && g < 2 && b < 2) curDotColor = Color.BLACK;
		return curDotColor;
	}

	private EdgeCoords getRightEdge(BufferedImage image, Color edgesColor, Color backColor) {
		System.out.println("Started getRightEdge");
		EdgeCoords rightEdge = new EdgeCoords(0, 0);
		int width = image.getWidth();
        int height = image.getHeight();
        boolean found = false;
        int dotsCount = 0; 
        int dotsFound = 0;
        int y = height;
        int x = width - 1;
        for (x = width - 1; x > width / 2; x--) {
        	for (y = 1; y < height-1; y++) {
            	dotsCount++;
            	Color curDotColor = getDotColor(image, x, y);
            	if (dotsFound>0 && curDotColor != edgesColor) {
            		System.out.printf("cur right edge x = %d, y = %d\n", x, y);
            		found = true;
            		break;
            	}
            	if (curDotColor == edgesColor) {
//            		System.out.printf("---!! found right edge in x = %d  y = %d!!\n", x, y);
            		dotsFound++;
            		curDotColor = null;
            	}
            }
            if (found) break;
        }
		System.out.println("ended getRightEdge, dotsCount = " + dotsCount + ", dotsFound = " + dotsFound);
		System.out.printf("right edge x = %d, y = %d\n", x, y-dotsFound/2);
		rightEdge.setX(x);
		rightEdge.setY(y-dotsFound/2);
		return rightEdge;	
	}
	
	private EdgeCoords getLeftEdge(BufferedImage image, Color edgesColor, Color backColor) {
		System.out.println("Started getLeftEdge");
		EdgeCoords leftEdge = new EdgeCoords(0, 0);
		int width = image.getWidth();
        int height = image.getHeight();
        boolean found = false;
        int dotsCount = 0; 
        int dotsFound = 0;
        int y = height;
        int x = 1;
        for (x = 1; x < width - 1; x++) {
        	for (y = 1; y < height-1; y++) {
            	dotsCount++;
            	Color curDotColor = getDotColor(image, x, y);
            	if (dotsFound>0 && curDotColor != edgesColor) {
            		System.out.printf("cur left edge x = %d, y = %d\n", x, y);
            		found = true;
            		break;
            	}
            	if (curDotColor == edgesColor) {
//            		System.out.printf("---!! found left edge in x = %d  y = %d!!\n", x, y);
            		dotsFound++;
            		curDotColor = null;
            	}
            }
            if (found) break;
        }
		System.out.println("ended getLeftEdge, dotsCount = " + dotsCount + ", dotsFound = " + dotsFound);
		System.out.printf("left edge x = %d, y = %d\n", x, y-dotsFound/2);
		leftEdge.setX(x);
		leftEdge.setY(y-dotsFound/2);
		return leftEdge;	
	}
	
	private EdgeCoords getButtomEdge(BufferedImage image, Color edgesColor, Color backColor) {
		System.out.println("Started getButtomEdge");
		EdgeCoords buttomEdge = new EdgeCoords(0, 0);
		int width = image.getWidth();
        int height = image.getHeight();
        boolean found = false;
        int dotsCount = 0; 
        int dotsFound = 0;
        int y = height;
        int x = 1;
        for (y = height-2; y > height/2; y--) {
        	for (x = 1; x < width - 1; x++) {
            	dotsCount++;
            	Color curDotColor = getDotColor(image, x, y);
            	if (dotsFound>0 && curDotColor != edgesColor) {
            		System.out.printf("cur buttom edge x = %d, y = %d\n", x, y);
            		found = true;
            		break;
            	}
            	if (curDotColor == edgesColor) {
//            		System.out.printf("---!! found buttom edge in x = %d  y = %d!!\n", x, y);
            		dotsFound++;
            		curDotColor = null;
            	}
            }
            if (found) break;
        }
		System.out.println("ended getButtomEdge, dotsCount = " + dotsCount + ", dotsFound = " + dotsFound);
		System.out.printf("buttom edge x = %d, y = %d\n", x-dotsFound/2, y);
		buttomEdge.setX(x-dotsFound/2);
		buttomEdge.setY(y);
		return buttomEdge;	
	}
	
	private EdgeCoords getTopEdge(BufferedImage image, Color edgesColor, Color backColor) {
		System.out.println("Started getTopEdge");
		EdgeCoords topEdge = new EdgeCoords(0, 0);
		int width = image.getWidth();
        int height = image.getHeight();
        boolean found = false;
        int dotsCount = 0; 
        int dotsFound = 0;
        int y = 1;
        int x = 1;
        for (y = 1; y < height - 1; y++) {
        	for (x = 1; x < width - 1; x++) {
            	dotsCount++;
            	Color curDotColor = getDotColor(image, x, y);
//            	int p = image.getRGB(x, y);
//    			int r = (p >> 16) & 0xff; // get red
//    			int g = (p >> 8) & 0xff; // get green
//    			int b = p & 0xff; // get blue
//    			if (r < 2 && g < 2 && b < 2) curDotColor = Color.BLACK;
            	if (dotsFound>0 && curDotColor != edgesColor) {
            		System.out.printf("cur edge x = %d, y = %d\n", x, y);
            		found = true;
            		break;
            	}
            	if (curDotColor == edgesColor) {
//            		System.out.printf("---!! found edge in x = %d  y = %d!!\n", x, y);
            		dotsFound++;
            		curDotColor = null;
            	}
            }
            if (found) break;
        }
		System.out.println("ended getTopEdge, dotsCount = " + dotsCount + ", dotsFound = " + dotsFound);
		System.out.printf("top edge x = %d, y = %d\n", x-dotsFound/2, y);
		topEdge.setX(x-dotsFound/2);
		topEdge.setY(y);
		return topEdge;	
	}
	
	public static void main(String[] args) throws Exception{
		System.out.println("Started MyContourSearch");
		MyContourSearch mySearch = new MyContourSearch();
		BufferedImage image = javax.imageio.ImageIO.read(new java.io.File("target.png"));
		mySearch.getContour(image, Color.BLACK, Color.WHITE);
	}
	
}
