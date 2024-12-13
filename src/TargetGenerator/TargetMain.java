package TargetGenerator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.server.Operation;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.metal.MetalBorders.Flush3DBorder;

import TargetRecognize.Circle;

public class TargetMain {
	static int rMax;
	static int rCurrent;
	static int x;
	static int y;
	static int betweenR = 40;
	static ArrayList<Circle> circles = new ArrayList<Circle>();
	static JSlider radiusSlider = new JSlider();
	static JTextField radiusValue = new JTextField("");
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createGui();
			}
		});
//		createGui();
	}

	private static BufferedImage createImage(Component panel) {
		int w = panel.getWidth();
		int h = panel.getHeight();
		System.out.println("in saving image size = " + w + " x " + h);
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		panel.paint(g);
		return bi;
	}

	private static void createGui() {
		JFrame targetWindow = new JFrame("Target creation");
		targetWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		MyTargetLabel targetLabel = new MyTargetLabel();

		
		targetWindow.setSize(900, 700);
		targetWindow.setLocationRelativeTo(null);
		targetWindow.add(targetLabel, BorderLayout.CENTER);
		targetWindow.setVisible(true);
		System.out.println("targetLabel width = " + targetLabel.getWidth());
		rMax = (targetLabel.getWidth() > targetLabel.getHeight()) ? targetLabel.getHeight() / 2
				: targetLabel.getWidth() / 2;
		System.out.println("rMax = " + rMax);
		rCurrent = rMax - 20;
		System.out.println("rCurrent = " + rCurrent);
		radiusSlider.getModel().setMaximum(rCurrent);
		radiusValue.setText(""+rCurrent);
		x = targetLabel.getWidth() / 2;
		y = targetLabel.getHeight() / 2;
		circles.add(new Circle(x, y, rCurrent));
		targetLabel.drawTarget(circles);
//		targetLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));

		targetWindow.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				System.out.println("targetLabel width = " + targetLabel.getWidth());
				rMax = (targetLabel.getWidth() > targetLabel.getHeight()) ? targetLabel.getHeight() / 2
						: targetLabel.getWidth() / 2;
				System.out.println("rMax = " + rMax);
				rCurrent = rMax - 20;
				radiusSlider.getModel().setMaximum(rCurrent);
				System.out.println("rCurrent in resize = " + rCurrent);
				radiusValue.setText(""+rCurrent);
				x = targetLabel.getWidth() / 2;
				y = targetLabel.getHeight() / 2;
				circles.clear();
				circles.add(new Circle(x, y, rCurrent));
				targetLabel.drawTarget(circles);
			}
		});

		JSlider thicknessSlider = new JSlider(JSlider.VERTICAL, 5, 20, 5);
		JSlider sizeSlider = new JSlider(10, rMax);
		JButton addCircleBtn = new JButton("circle +");
		addCircleBtn.addActionListener(e -> {
			circles.add(new Circle(x, y, circles.getLast().getRadius() - betweenR));
			System.out.println("circle added with parameters:  " + x + " " + y + " " + circles.getLast().getRadius());
			targetLabel.drawTarget(circles);
		});
		JButton delCircleBtn = new JButton("circle -");
		delCircleBtn.addActionListener(e -> {
			if (circles.size() > 1) {
				circles.removeLast();
				targetLabel.drawTarget(circles);
			}
		});
		JButton saveImageBtn = new JButton("Save image");
		saveImageBtn.addActionListener(e -> {
			String ext = "png";
			JFileChooser fileDialog = new JFileChooser();
			fileDialog.setAcceptAllFileFilterUsed(false);
	        FileNameExtensionFilter filter = new FileNameExtensionFilter(ext+" files", ext);
	        fileDialog.addChoosableFileFilter(filter);
			
			File workingDirectory = new File(System.getProperty("user.dir"));
			fileDialog.setCurrentDirectory(workingDirectory);
			int v = fileDialog.showSaveDialog(targetWindow);
			if (v == JFileChooser.APPROVE_OPTION) {
				BufferedImage bImage = createImage(targetLabel);
				System.out.println("image size = " + bImage.getWidth() + " x " + bImage.getHeight());
				File outputfile = fileDialog.getSelectedFile();
				if (!outputfile.getPath().contains(ext)) outputfile = new File(outputfile.getPath()+"."+ext);
				try {
					ImageIO.write(bImage, ext, outputfile);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		var upperBox = new Box(BoxLayout.Y_AXIS);
		upperBox.setBorder(BorderFactory.createLineBorder(Color.gray, 5));
		Box upperBoxForCircles = new Box(BoxLayout.X_AXIS);
		upperBoxForCircles.add(addCircleBtn);
		upperBoxForCircles.add(Box.createHorizontalGlue());
		upperBoxForCircles.add(saveImageBtn);
		upperBoxForCircles.add(Box.createHorizontalGlue());
		upperBoxForCircles.add(delCircleBtn);
		upperBox.add(upperBoxForCircles);
		
		Box upperBoxForBackColor = createSliderBox(targetLabel, "Back color in gray:", 0, 255, 255);
		
		upperBox.add(upperBoxForBackColor);
		Box upperBoxForCircleColor = createSliderBox(targetLabel, "Circle color in gray:", 0, 255, 0);
		upperBox.add(upperBoxForCircleColor);
		
		Box buttomBox = createSliderBox(targetLabel, "Radius:", 20, rCurrent, rCurrent);
		
		//TODO сделать добавление бокового слайдера через функцию 
		//TODO добавить генерацию нужного кол-ва мишеней с разными параметрами в отдельную папку
		
		targetWindow.add(upperBox, BorderLayout.NORTH);
		targetWindow.add(buttomBox, BorderLayout.SOUTH);
		targetWindow.add(thicknessSlider, BorderLayout.EAST);
		targetWindow.validate();
	}

	private static Box createSliderBox(MyTargetLabel targetLabel, String labelText, int min, int max, int curValue) {
		Box boxWithSlider = new Box(BoxLayout.X_AXIS);
		boxWithSlider.add(new JLabel(labelText));
		JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, curValue);
		slider.setPaintTrack(true);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMajorTickSpacing(50);
		slider.setMinorTickSpacing(5);
 
		JTextField sliderValue = new JTextField(""+curValue);
		slider.addChangeListener(e->{
			System.out.println(labelText+" = "+slider.getValue());
			if (labelText.contains("Back")) targetLabel.setBackColor(slider.getValue());
			if (labelText.contains("Circle")) targetLabel.setCircleColor(slider.getValue());
			if (labelText.contains("Radius")) {
				radiusSlider = slider;
				slider.getModel().setMaximum(rCurrent);
				radiusValue = sliderValue;
				radiusValue.setText(""+rCurrent);
				circles.get(0).setRadius(slider.getValue());
				targetLabel.drawTarget(circles);
			}
			
			sliderValue.setText(String.valueOf(slider.getValue()));
			targetLabel.repaint();
			
		});
		boxWithSlider.add(sliderValue);
		sliderValue.setMaximumSize(new Dimension(10, 20));
		boxWithSlider.add(slider);
		return boxWithSlider;
	}

}
