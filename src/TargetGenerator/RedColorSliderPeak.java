package TargetGenerator;

import javax.swing.*;
import java.awt.*;
import java.util.Dictionary;
import java.util.Hashtable;

public class RedColorSliderPeak extends JFrame {
    private JSlider slider;
    private JPanel colorPanel;
    private JLabel rgbLabel; // Optional: show RGB values

    public RedColorSliderPeak() {
        setTitle("Red Slider: Dark → Bright → Light (0–255)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Color panel
        colorPanel = new JPanel();
        colorPanel.setPreferredSize(new Dimension(400, 100));
        rgbLabel = new JLabel("RGB(128, 0, 0)", JLabel.CENTER);
        updateColor(0); // Start at dark red

        // RGB label (optional, for clarity)
        rgbLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        rgbLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Slider 0–255
        slider = new JSlider(0, 255, 0);
        slider.setMajorTickSpacing(64); // ~0, 64, 128, 192, 255
        slider.setMinorTickSpacing(16);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        Dictionary<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(0, new JLabel("Dark"));
        labelTable.put(127, new JLabel("Bright"));
        labelTable.put(255, new JLabel("Light"));
        slider.setLabelTable(labelTable);
        slider.addChangeListener(e -> {
            int value = slider.getValue();
            updateColor(value);
        });

        add(colorPanel, BorderLayout.CENTER);
        add(rgbLabel, BorderLayout.NORTH);
        add(slider, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void updateColor(int value) {
        int r, g, b;

        if (value <= 127) {
            // Phase 1: 0 → 127: dark red (128,0,0) → bright red (255,0,0)
            // Red increases from 128 to 255 over 128 steps (0 to 127 inclusive)
            double ratio = value / 127.0;
            r = 128 + (int) ((255 - 128) * ratio); // 128 → 255
            g = 0;
            b = 0;
        } else {
            // Phase 2: 128 → 255: bright red (255,0,0) → light red (255,220,220)
            // Green & blue rise from 0 → 220 over 128 steps (128 to 255 inclusive = 128 values)
            double ratio = (value - 128) / 127.0; // normalize to [0,1]
            r = 255;
            g = (int) (220 * ratio); // 0 → 220
            b = (int) (220 * ratio); // 0 → 220
        }

        Color color = new Color(r, g, b);
        colorPanel.setBackground(color);
        rgbLabel.setText(String.format("RGB(%d, %d, %d)", r, g, b));
        colorPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RedColorSliderPeak().setVisible(true);
        });
    }
}