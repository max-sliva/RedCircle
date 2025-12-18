package TargetRecognize;
import javax.imageio.ImageIO;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ClosedLineDetector {

    // Threshold for binarization: 0–255 (lower = more black)
    private static final int BINARY_THRESHOLD = 128;
    private static final int MIN_PERIMETER = 20; // ignore tiny loops

    public static void main(String[] args) throws IOException {
        String inputPath = "target.png"; // ← change to your image
        String outputPath = "output_closed.png";

        BufferedImage img = ImageIO.read(new File(inputPath));
        if (img == null) {
            System.err.println("❌ Could not load image: " + inputPath);
            return;
        }

        // Step 1: Convert to binary (black = 0, white = 1)
        boolean[][] binary = toBinary(img);

        // Step 2: Find all black connected components
        boolean[][] visited = new boolean[img.getHeight()][img.getWidth()];
        List<List<Point>> closedContours = new ArrayList<>();

        for (int y = 0; y < binary.length; y++) {
            for (int x = 0; x < binary[y].length; x++) {
                if (binary[y][x] && !visited[y][x]) {
                    // Start tracing from first black pixel
                	List<Point> contour = traceBoundary(binary, visited, x, y);
                    if (isClosedContour(contour)) {
                        closedContours.add(contour);
                    }
                }
            }
        }

        // Step 3: Draw detected closed contours on copy
        BufferedImage result = copyImage(img);
        Graphics2D g2d = result.createGraphics();
        g2d.setColor(Color.GREEN);
        g2d.setStroke(new BasicStroke(2));

        for (List<Point> contour : closedContours) {
            if (contour.size() >= 3) {
                Polygon poly = new Polygon();
                for (Point p : contour) poly.addPoint(p.x, p.y);
                g2d.draw(poly);
            }
        }
        g2d.dispose();

        // Save result
        ImageIO.write(result, "png", new File(outputPath));
        System.out.println("✅ Found " + closedContours.size() + " closed contour(s).");
        System.out.println("💾 Output saved: " + outputPath);
    }

    // Convert image to binary: true = black (line), false = white (background)
    private static boolean[][] toBinary(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        boolean[][] out = new boolean[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgb = img.getRGB(x, y);
                int gray = (int) (0.299 * ((rgb >> 16) & 0xFF) +
                                 0.587 * ((rgb >> 8) & 0xFF) +
                                 0.114 * (rgb & 0xFF));
                out[y][x] = gray < BINARY_THRESHOLD; // black if dark
            }
        }
        return out;
    }

    // Moore-Neighbor (Square) Boundary Tracing — returns ordered boundary points
    private static List<Point> traceBoundary(boolean[][] binary, boolean[][] visited, int startX, int startY) {
        int h = binary.length;
        int w = binary[0].length;
        List<Point> contour = new ArrayList<>();

        // Find first black neighbor with white on left (standard start condition)
        int x = startX, y = startY;

        // Directions: East, SouthEast, South, SouthWest, West, NorthWest, North, NorthEast
        int[] dx = {1, 1, 0, -1, -1, -1, 0, 1};
        int[] dy = {0, 1, 1, 1, 0, -1, -1, -1};

        // Find initial direction (first white neighbor in clockwise order)
        int dir = 0;
        while (dir < 8) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if (nx >= 0 && nx < w && ny >= 0 && ny < h && !binary[ny][nx]) {
                break;
            }
            dir++;
        }
        dir = (dir + 6) % 8; // turn back 90° CCW (start moving with white on left)

        Point start = new Point(x, y);
        contour.add(start);
        visited[y][x] = true;

        int steps = 0;
        final int MAX_STEPS = w * h;

        while (steps++ < MAX_STEPS) {
            // Try next direction (turn right until hitting black)
            int nextDir = (dir + 1) % 8;
            int nx = x + dx[nextDir];
            int ny = y + dy[nextDir];

            if (nx >= 0 && nx < w && ny >= 0 && ny < h && binary[ny][nx]) {
                // Move to next boundary pixel
                x = nx;
                y = ny;
                Point p = new Point(x, y);
                contour.add(p);
                visited[y][x] = true;
                dir = nextDir;
            } else {
                // Turn left (keep direction)
                dir = nextDir;
            }

            // Stop if back to start with same direction (full loop)
            if (x == start.x && y == start.y && contour.size() > 5) {
                break;
            }
        }

        return contour;
    }

    // Heuristic: Is contour closed? (looped back, sufficient size, no degenerate line)
    private static boolean isClosedContour(List<Point> contour) {
        if (contour.size() < 5) return false;

        Point first = contour.get(0);
        Point last = contour.get(contour.size() - 1);

        // Check proximity to start (within 2 pixels)
        double dist = Math.hypot(first.x - last.x, first.y - last.y);
        if (dist > 2.0) return false;

        // Check area > 0 (simple shoelace formula)
        double area = computeArea(contour);
        return Math.abs(area) > 10.0; // non-zero area ⇒ closed
    }

    // Shoelace formula for polygon area
    private static double computeArea(List<Point> points) {
        int n = points.size();
        if (n < 3) return 0;
        double area = 0.0;
        for (int i = 0; i < n; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get((i + 1) % n);
            area += p1.x * p2.y - p2.x * p1.y;
        }
        return Math.abs(area) / 2.0;
    }

    // Utility: deep copy image
    private static BufferedImage copyImage(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics2D g = b.createGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }
}