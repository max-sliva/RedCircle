package TargetRecognize;

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

	public ArrayList<EdgeCoords> getEdgeCoordsList() {
		return edgeCoordsList;
	}

	public void setEdgeCoordsList(ArrayList<EdgeCoords> edgeCoordsList) {
		this.edgeCoordsList = edgeCoordsList;
	}
	
	public void addPointToEdgesArray(EdgeCoords point) {
		edgeCoordsList.add(point);
	}
}

public class MyContourSearch {
	private ArrayList<Contour> contoursList = null;
	
	public ArrayList<EdgeCoords> getContour() {
		ArrayList<EdgeCoords> coordsList = new ArrayList<>();
		
		return coordsList;
	}
	
}
