package TargetRecognize;

import javax.imageio.ImageIO;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ContourFinder {
    
    // Direction vectors for 8-connectivity
    private static final int[][] DIRECTIONS = {
        {-1, -1}, {-1, 0}, {-1, 1},
        {0, -1},          {0, 1},
        {1, -1},  {1, 0},  {1, 1}
    };
    
    public static List<List<Point>> findClosedRegions(BufferedImage image, int threshold) {
        int width = image.getWidth();
        int height = image.getHeight();
        
        // Convert to binary (black/white)
        boolean[][] visited = new boolean[width][height];
        boolean[][] isForeground = new boolean[width][height];
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = new Color(image.getRGB(x, y));
                int gray = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                isForeground[x][y] = gray < threshold;  // Dark pixels as foreground
            }
        }
        
        List<List<Point>> regions = new ArrayList<>();
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!visited[x][y] && isForeground[x][y]) {
                    List<Point> region = floodFill(x, y, visited, isForeground, width, height);
                    
                    if (isClosedRegion(region, width, height)) {
                        regions.add(region);
                    }
                }
            }
        }
        
        return regions;
    }
    
    private static List<Point> floodFill(int startX, int startY, 
                                         boolean[][] visited, 
                                         boolean[][] isForeground,
                                         int width, int height) {
        List<Point> region = new ArrayList<>();
        Stack<Point> stack = new Stack<>();
        
        stack.push(new Point(startX, startY));
        visited[startX][startY] = true;
        
        while (!stack.isEmpty()) {
            Point p = stack.pop();
            region.add(p);
            
            for (int[] dir : DIRECTIONS) {
                int newX = p.x + dir[0];
                int newY = p.y + dir[1];
                
                if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                    if (!visited[newX][newY] && isForeground[newX][newY]) {
                        visited[newX][newY] = true;
                        stack.push(new Point(newX, newY));
                    }
                }
            }
        }
        
        return region;
    }
    
    private static boolean isClosedRegion(List<Point> region, int width, int height) {
        if (region.isEmpty()) return false;
        
        // Check if region touches image boundary (not closed within image)
        for (Point p : region) {
            if (p.x == 0 || p.x == width - 1 || p.y == 0 || p.y == height - 1) {
                return false;
            }
        }
        
        // Calculate bounding box
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
        
        for (Point p : region) {
            minX = Math.min(minX, p.x);
            maxX = Math.max(maxX, p.x);
            minY = Math.min(minY, p.y);
            maxY = Math.max(maxY, p.y);
        }
        
        // Check if region has significant size
        int widthRegion = maxX - minX + 1;
        int heightRegion = maxY - minY + 1;
        
        return widthRegion > 5 && heightRegion > 5 && region.size() > 50;
    }
    
    public static void main(String[] args) throws Exception {
        // Load image
        BufferedImage image = ImageIO.read(new File("111.png"));
        
        // Find closed regions
        List<List<Point>> closedRegions = findClosedRegions(image, 128);
        
        System.out.println("Found " + closedRegions.size() + " closed regions");
        
        // Draw bounding boxes around found regions
        Graphics2D g = image.createGraphics();
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(2));
        
        for (List<Point> region : closedRegions) {
            // Calculate bounding box
            int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
            int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
            
            for (Point p : region) {
                minX = Math.min(minX, p.x);
                maxX = Math.max(maxX, p.x);
                minY = Math.min(minY, p.y);
                maxY = Math.max(maxY, p.y);
            }
            
            // Draw rectangle
            g.drawRect(minX, minY, maxX - minX, maxY - minY);
        }
        
        g.dispose();
        
        // Save result
        ImageIO.write(image, "jpg", new File("output_detected2.jpg"));
        
        // Print region details
        for (int i = 0; i < closedRegions.size(); i++) {
            List<Point> region = closedRegions.get(i);
            System.out.println("Region " + (i + 1) + ": " + region.size() + " points");
        }
    }
}