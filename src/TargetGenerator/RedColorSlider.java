package TargetGenerator;

import javax.swing.*;
import java.awt.*;

public class RedColorSlider extends JFrame {
    private JSlider slider;
    private JPanel colorPanel;

    public RedColorSlider() {
        setTitle("Red Color Slider (0–255)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Color display panel
        colorPanel = new JPanel();
        colorPanel.setPreferredSize(new Dimension(400, 100));
        updateColor(0); // Start at value = 0 → dark red

        // Slider from 0 to 255
        slider = new JSlider(0, 255, 0);
        slider.setMajorTickSpacing(50);
        slider.setMinorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(e -> {
            int value = slider.getValue();
            updateColor(value);
        });

        add(colorPanel, BorderLayout.CENTER);
        add(slider, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void updateColor(int value) {
        // Map slider value [0..255] → color from (128,0,0) to (255,220,220)
        // Linear interpolation: component = start + (end - start) * (value / 255.0)
        double ratio = value / 255.0;

        int r = (int) (128 + (255 - 128) * ratio); // 128 → 255
        int g = (int) (0   + 225  * ratio); // 0   → 220
        int b = (int) (0   + 225  * ratio); // 0   → 220

        colorPanel.setBackground(new Color(r, g, b));
        colorPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RedColorSlider().setVisible(true);
        });
    }
}