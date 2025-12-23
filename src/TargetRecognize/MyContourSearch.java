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
		
		
		return coordsList;
	}
	
	private EdgeCoords getTopEdge(BufferedImage image, Color edgesColor, Color backColor) {
		System.out.println("Started getTopEdge");
		EdgeCoords topEdge = null;
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
            	Color curDotColor = null;
            	int p = image.getRGB(x, y);
    			int r = (p >> 16) & 0xff; // get red
    			int g = (p >> 8) & 0xff; // get green
    			int b = p & 0xff; // get blue
    			if (r < 2 && g < 2 && b < 2) curDotColor = Color.BLACK;
            	if (dotsFound>0 && curDotColor != edgesColor) {
            		System.out.printf("cur edge x = %d, y = %d\n", x, y);
            		found = true;
            		break;
            	}
            	if (curDotColor == edgesColor) {
            		System.out.printf("---!! found edge in x = %d  y = %d!!\n", x, y);
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
