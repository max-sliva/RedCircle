
public class RedMain {

	public static void main(String[] args) {
		RedSearch redSearch = new RedSearch("img.png");
		redSearch.findRedPoints();
		redSearch = new RedSearch("img2.png");
		redSearch.findRedPoints();
		

	}

}
