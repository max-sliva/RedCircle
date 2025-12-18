package TargetRecognize;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SimpleEdgeDetector {
    
    public static BufferedImage detectEdges(BufferedImage image, int threshold) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage edges = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        // Sobel operator kernels
        int[][] sobelX = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] sobelY = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
        
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                int gx = 0, gy = 0;
                
                // Apply Sobel operator
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        Color color = new Color(image.getRGB(x + i, y + j));
                        int gray = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                        
                        gx += gray * sobelX[i + 1][j + 1];
                        gy += gray * sobelY[i + 1][j + 1];
                    }
                }
                
                int magnitude = (int) Math.sqrt(gx * gx + gy * gy);
                
                if (magnitude > threshold) {
                    edges.setRGB(x, y, Color.WHITE.getRGB()); //рисуем контур
                } else {
                    edges.setRGB(x, y, Color.BLACK.getRGB()); //или фон
                }
            }
        }
        
        return edges;
    }
    
    public static void main(String[] args) throws Exception {
        BufferedImage image = javax.imageio.ImageIO.read(new java.io.File("target.png"));
        BufferedImage edges = detectEdges(image, 100);
        javax.imageio.ImageIO.write(edges, "jpg", new java.io.File("edges_detected2.jpg"));
    }
}