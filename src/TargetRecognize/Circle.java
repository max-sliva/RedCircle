package TargetRecognize;
public class Circle extends MyPoint{
	private int radius;
	private int thickness = 10;

	public Circle(int x, int y, int radius) {
		super(x, y);
		this.radius = radius;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getThickness() {
		return thickness;
	}
	
	public void setThickness(int thick) {
		thickness = thick;
	}
}
