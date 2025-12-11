package TargetGenerator;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import TargetRecognize.Circle;

public class GenerationOptions extends JFrame {

	private static final long serialVersionUID = 1L;
	MyTarget target1;
	MyTarget target2;
	ArrayList<Circle> circlesFrom = new ArrayList<Circle>();
	ArrayList<Circle> circlesTo = new ArrayList<Circle>();
	Box centralBox = new Box(BoxLayout.Y_AXIS);
	Box imagesPreviewBox = new Box(BoxLayout.X_AXIS);
	MyTarget imageFrom = new MyTarget("from");
	MyTarget imageTo = new MyTarget("to");
	int thickFrom = 3;
	int thickTo = 20;
	int circlesNfrom = 1;
	int circlesNto = 2;
	int betweenFrom = 20;
	int betweenTo = 60;
	int bgColorFrom = 255;
	int bgColorTo = 155;
	int circleColorFrom = 0;
	int circleColorTo = 155;
	int filesNum = (circlesNto - circlesNfrom + 1) * ((thickTo - thickFrom + 1) / 2)
			* ((betweenTo - betweenFrom + 1) / 5);
	int redDotNumber = 5;
	int redDotColorFrom = 155;
	int redDotColorTo = 255;
	//TODO сделать изменение размера красной точки
	int redDotSizeFrom = 5;
	int redDotSizeTo = 20;
	JLabel filesNumLabel = new JLabel("filesNum = " + filesNum);
	int progressI = 0;
	JLabel progressLabel = new JLabel("progressI = " + progressI);
	BasicStroke pen;
	JCheckBox rebDotChBox = new JCheckBox("Red dot");

	public GenerationOptions(String title) {
		super(title);
		Box circlesCountBox = create2ValueSliderBox("Circles number: ", 1, 10, circlesNfrom, circlesNto);
		Box circlesThickBox = create2ValueSliderBox("Circles thickness: ", 1, 70, thickFrom, thickTo);
		Box circlesSpaceBox = create2ValueSliderBox("Circles between: ", 10, 100, betweenFrom, betweenTo);
		Box bgColorBox = create2ValueSliderBox("Background color: ", 0, 255, 255, 155);
		Box circleColorBox = create2ValueSliderBox("Circle color: ", 0, 255, 0, 155);
		Box redDotColorBox = create2ValueSliderBox("red dot color: ", 0, 255, redDotColorFrom, redDotColorTo);

		imageTo.setBackColor(bgColorTo);
		JButton saveSettings = new JButton("save settings");
		saveSettings.addActionListener(e->{
			//TODO добавить сохранение параметров генерации
			System.out.println("Saving current params");
		});
		JButton loadSettings = new JButton("load settings");
		loadSettings.addActionListener(e->{
			//TODO добавить загрузку параметров генерации
			System.out.println("loading params");
		});
		JButton genBtn = new JButton("Generate targets");
		genBtn.addActionListener(e -> {
			JFileChooser chooser;
			chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File(".")); // Set default directory
			chooser.setDialogTitle("Select a Directory");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Allow only directories
			chooser.setAcceptAllFileFilterUsed(false); // Disable "All files" option

			int returnValue = chooser.showOpenDialog(this);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				String dirPath = chooser.getSelectedFile().getAbsolutePath();
				System.out.println("Selected Directory: " + dirPath);
//				generateTargetImages(1400, 1000, dirPath);
				generateTargetImages(640, 640, dirPath);
				System.out.println("---!!targets created!!---");
			} else {
				System.out.println("No Selection");
			}
		});
		Box genBtnBox = new Box(BoxLayout.X_AXIS);
		JSpinner redDotNspinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
		redDotNspinner.setEnabled(false);
		redDotNspinner.addChangeListener(e->{
			redDotNumber = (int) redDotNspinner.getValue();
			System.out.println("redDotNumber = "+redDotNumber);
		});
		rebDotChBox.addActionListener(e -> {
//			int w = imageTo.getCurrentWidth();
//			int h = imageTo.getCurrentHeight();
//			int radius = (w < h) ? (w/2): (h/2); 
//			radius = (radius==0)? radius : radius-10-thickTo;
//			if (rebDotChBox.isSelected()) {
//				imageFrom.drawRedDot(radius-30, radius-30);
//				imageTo.drawRedDot(radius-30, radius-30);
//			}
			redDotNspinner.setEnabled(rebDotChBox.isSelected());
			redrawPreviewImages();
		});
		JCheckBox showImagesChBox = new JCheckBox("Show preview");
		showImagesChBox.addActionListener(e -> {
			if (showImagesChBox.isSelected()) {
				imageFrom.repaint();
				imagesPreviewBox.setVisible(true);
				imagesPreviewBox.setVisible(false);
				imagesPreviewBox.setVisible(true);
				System.out.println("imageFrom width = " + imageFrom.getCurrentWidth());
//				imageFrom.drawTarget(circlesFrom);
				if (circlesFrom.getFirst().getRadius() == 0) { // если еще нет кругов
					circlesFrom.clear();
					int w = imageFrom.getCurrentWidth(); // ширина
					int h = imageFrom.getCurrentHeight(); // высота
					int radius = (w < h) ? (w / 2) : (h / 2); // радиус будет половиной от меньшего
					circlesFrom
							.add(new Circle(w / 2, h / 2, (radius == 0) ? radius : radius - 10 - thickFrom, thickFrom));
//					imageFrom.drawTarget(circlesFrom);
					circlesTo.clear();
					w = imageTo.getCurrentWidth();
					h = imageTo.getCurrentHeight();
					radius = (radius == 0) ? radius : radius - 10 - thickTo;
//					if (rebDotChBox.isSelected()) {
//						imageFrom.drawRedDot(radius-30, radius-30);
//						imageTo.drawRedDot(radius-30, radius-30);
//					}

					// для второго превью
					System.out.println("betweenTo = " + betweenTo);
					radius = updateCircles(w, h, radius);
//					imageTo.drawTarget(circlesTo);
					redrawPreviewImages();
				}
				imagesPreviewBox.setVisible(false);
				imagesPreviewBox.setVisible(true);
				redrawPreviewImages();
			} else
				imagesPreviewBox.setVisible(false);
		});
		
		genBtnBox.add(showImagesChBox);
		genBtnBox.add(Box.createHorizontalGlue());
		genBtnBox.add(rebDotChBox);
		genBtnBox.add(Box.createHorizontalStrut(10));
		genBtnBox.add(redDotNspinner);
		genBtnBox.add(Box.createHorizontalGlue());
		genBtnBox.add(filesNumLabel);
		genBtnBox.add(Box.createHorizontalGlue());
		genBtnBox.add(saveSettings);
		genBtnBox.add(Box.createHorizontalGlue());
		genBtnBox.add(loadSettings);
		genBtnBox.add(Box.createHorizontalGlue());
		genBtnBox.add(genBtn);
		Box northBox = new Box(BoxLayout.Y_AXIS);
		northBox.add(circlesCountBox);
		northBox.add(circlesThickBox);
		northBox.add(circlesSpaceBox);
		northBox.add(bgColorBox);
		northBox.add(circleColorBox);
		northBox.add(redDotColorBox);
		northBox.add(genBtnBox);

//		Box imagesPreviewBox = new Box(BoxLayout.X_AXIS);
//		MyTargetLabel imageFrom = new MyTargetLabel();
		add(northBox, BorderLayout.NORTH);
		add(imagesPreviewBox, BorderLayout.CENTER);
		add(progressLabel, BorderLayout.SOUTH);
		imagesPreviewBox.setVisible(false);
//		MyTarget imageFrom = new MyTarget("from");
//		MyTarget imageTo = new MyTarget("to");
		imagesPreviewBox.add(imageFrom);
		imagesPreviewBox.add(new JLabel("-->"));
		imagesPreviewBox.add(imageTo);
//		imageTo.setBackColor(betweenFrom)
		imagesPreviewBox.setBorder(BorderFactory.createLineBorder(Color.blue, 3));
		System.out.println("gen window is shown");
////		System.out.println("imagesPreviewBox width = " + imagesPreviewBox.getWidth());
//		invalidate();
//		imageFrom.repaint();
//		imageTo.repaint();
//		System.out.println("imageFrom width = " + imageFrom.getCurrentWidth());

//		add(imageTo, BorderLayout.CENTER);
		showImagesChBox.setSelected(true);
		showImagesChBox.setSelected(false);
		int w = imageFrom.getCurrentWidth();
		int h = imageFrom.getCurrentHeight();
		int radius = (w < h) ? w / 2 : h / 2;
		circlesFrom.add(new Circle(w / 2, h / 2, radius));
//		imageFrom.drawTarget(circlesFrom);
		w = imageTo.getCurrentWidth();
		h = imageTo.getCurrentHeight();
		radius = (w < h) ? w / 2 : h / 2;
		circlesTo.add(new Circle(w / 2, h / 2, radius));
//		imageTo.drawTarget(circlesTo);
		redrawPreviewImages();
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				System.out.println("in resize");
				// TODO сделать изменение радиусов всех кругов
				System.out.println("window resize");
				redrawPreviewImages();
			}
		});
	}

	public void showPreviewBox() {
//		MyTarget imageFrom = new MyTarget("from");
//		MyTarget imageTo = new MyTarget("to");
//		imagesPreviewBox.add(imageFrom);
//		imagesPreviewBox.add(new JLabel("-->"));
//		imagesPreviewBox.add(imageTo);
//		imagesPreviewBox.setBorder(BorderFactory.createLineBorder(Color.blue, 3));
////		System.out.println("imagesPreviewBox width = " + imagesPreviewBox.getWidth());
//		invalidate();
//		imageFrom.repaint();
//		imageTo.repaint();
		System.out.println("imageFrom width = " + imageFrom.getCurrentWidth());

	}

	private void redrawPreviewImages() {
		int w = imageTo.getCurrentWidth();
		int h = imageTo.getCurrentHeight();
		int radius = (w < h) ? (w / 2) : (h / 2);
		radius = (radius == 0) ? radius : radius - 10 - thickTo;
		if (rebDotChBox.isSelected()) {
//			imageFrom.drawRedDot(radius - 30, radius - 30);
//			imageTo.drawRedDot(radius - 30, radius - 30);
			imageFrom.drawNRedDots(redDotNumber, radius - 30, radius - 30, redDotColorFrom);
			imageTo.drawNRedDots(redDotNumber, radius - 30, radius - 30, redDotColorTo);
		} else {
			imageFrom.setDrawDot(false);
			imageTo.setDrawDot(false);
		}
		imageFrom.drawTarget(circlesFrom);
		imageTo.drawTarget(circlesTo);

	}

	private Box create2ValueSliderBox(String labelText, int minValue, int maxValue, int val1, int val2) {
		Box boxWith2Sliders = new Box(BoxLayout.X_AXIS);
		JLabel sliderLabel = new JLabel(labelText);
//		RangeSlider slider = new RangeSlider(minValue, maxValue, val1, val2);
		RangeSliderOld slider = new RangeSliderOld(minValue, maxValue);
		if (labelText.contains("thick")) {
			thickFrom = val1;
			thickTo = val2;
		}
		JLabel val1Label = new JLabel(" " + val1 + " ");
		val1Label.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
		JLabel val2Label = new JLabel(" " + val2 + " ");
		val2Label.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
		if (labelText.contains("Background")) {
			slider.setInverted(true);
			int temp = val2;
			val2 = val1;
			val1 = temp;
			val1Label.setText(" " + val2 + " ");
			val2Label.setText(" " + val1 + " ");
		}
		slider.setValue(val1);
		slider.setUpperValue(val2);
		slider.setPaintTrack(true);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMajorTickSpacing((maxValue - minValue) / 4);
		slider.setMinorTickSpacing((maxValue - minValue + 1) / 10);
		boxWith2Sliders.add(sliderLabel);

		boxWith2Sliders.add(val1Label);
		boxWith2Sliders.add(new JLabel("-"));
		boxWith2Sliders.add(val2Label);
		boxWith2Sliders.add(slider);
		slider.addChangeListener(e -> {
			if (labelText.contains("Background")) {
				val2Label.setText(" " + slider.getValue() + " ");
				val1Label.setText(" " + slider.getUpperValue() + " ");
				bgColorFrom = slider.getUpperValue();
				bgColorTo = slider.getValue();
				imageFrom.setBackColor(slider.getUpperValue());
				imageTo.setBackColor(slider.getValue());
				System.out.println("Background color imageTo = " + slider.getValue());
			} else {
				val1Label.setText(" " + slider.getValue() + " ");
				val2Label.setText(" " + slider.getUpperValue() + " ");
			}
			if (labelText.contains("red")) {
				redDotColorFrom = slider.getValue();
				redDotColorTo = slider.getUpperValue();
				System.out.println("red dot from = "+redDotColorFrom);
				System.out.println("red dot to = "+redDotColorTo);
			}
			if (labelText.contains("Circle color")) {
				circleColorFrom = slider.getValue();
				circleColorTo = slider.getUpperValue();
				imageFrom.setCircleColor(slider.getValue());
				imageTo.setCircleColor(slider.getUpperValue());
			}
			int w = imageTo.getCurrentWidth();
			int h = imageTo.getCurrentHeight();
			int radius = (w < h) ? (w / 2) : (h / 2);
			radius = (radius == 0) ? radius : radius - 10 - thickTo;

			if (labelText.contains("thick")) {
				thickFrom = slider.getValue();
				thickTo = slider.getUpperValue();
				circlesFrom.forEach(circle -> {
					circle.setThickness(thickFrom);
				});
				circlesTo.forEach(circle -> {
					circle.setThickness(thickTo);
				});

//				imageFrom.drawTarget(circlesFrom);
//				imageTo.drawTarget(circlesTo);
			}
			if (labelText.contains("number")) {
				circlesNfrom = slider.getValue();
				circlesNto = slider.getUpperValue();
//				System.out.println("circlesNfrom = "+circlesNfrom);
//				System.out.println("circlesNto = "+circlesNto);
				// TODO проверить, почему в мишени circlesNfrom по умолчанию 2 круга
//				imageFrom.drawTarget(circlesFrom);
				circlesFrom.clear();
				circlesTo.clear();
//				int w = imageTo.getCurrentWidth();
//				int h = imageTo.getCurrentHeight();
//				int radius = (w < h) ? (w/2): (h/2); 
//				radius = (radius==0)? radius : radius-10-thickTo;
				// для второго превью
//				System.out.println("number = "+number);
				radius = updateCircles(w, h, radius);
//				imageTo.drawTarget(circlesTo);
			}
			if (labelText.contains("between")) {
				betweenFrom = slider.getValue();
				betweenTo = slider.getUpperValue();
//				System.out.println("betweenTo = "+betweenTo);
//				imageFrom.drawTarget(circlesFrom);
				circlesTo.clear();
				circlesFrom.clear();
//				int w = imageTo.getCurrentWidth();
//				int h = imageTo.getCurrentHeight();
//				int radius = (w < h) ? (w/2): (h/2); 
//				radius = (radius==0)? radius : radius-10-thickTo;
//				if (rebDotChBox.isSelected()) {
//					imageFrom.drawRedDot(radius-30, radius-30);
//					imageTo.drawRedDot(radius-30, radius-30);
//				}
				// для второго превью
//				System.out.println("betweenTo = "+betweenTo);
				radius = updateCircles(w, h, radius);
			}
//			if (rebDotChBox.isSelected()) {
//				imageFrom.drawRedDot(radius-30, radius-30);
//				imageTo.drawRedDot(radius-30, radius-30);
//			}
			redrawPreviewImages();
//			for (int bgColor = bgColorFrom; bgColor <= bgColorTo; bgColor += 30)
			filesNum = (circlesNto - circlesNfrom + 1) * ((thickTo - thickFrom) / 2 + 1)* ((betweenTo - betweenFrom) / 5 + 1)
					*((bgColorFrom - bgColorTo)/30 + 1)*((circleColorTo - circleColorFrom)/30 + 1)*((redDotColorTo - redDotColorFrom)/25 + 1);
			filesNumLabel.setText("filesNum = " + filesNum);
		});
		return boxWith2Sliders;
	}

	private int updateCircles(int w, int h, int radius) {
		int tempRadius = radius;
		for (int i = 0; i < circlesNto; i++) {
			System.out.println("radius" + i + "= " + radius);
			circlesTo.add(new Circle(w / 2, h / 2, radius, thickTo));
			radius = radius - betweenTo;
		}
		for (int i = 0; i < circlesNfrom; i++) {
			System.out.println("radius" + i + "= " + tempRadius);
			circlesFrom.add(new Circle(w / 2, h / 2, tempRadius, thickFrom));
			tempRadius = tempRadius - betweenFrom;
		}
		return radius;
	}

	private void generateTargetImages(int w, int h, String dirPath) {
		int width = w;
		int height = h;
//        int between = betweenFrom;
		int radius = (width > height) ? (height - 40) / 2 : (width - 40) / 2;
//		BasicStroke pen;
		progressI = 0;
//		JDialog progressDialog = new JDialog(this);
////		JLabel progressLabel = new JLabel("progressI = "+progressI);
//		progressDialog.getContentPane().add(progressLabel);
//		progressDialog.setLocationRelativeTo(null);
//		progressDialog.setVisible(true);
		progressLabel.setText("progressI = " + progressI);
		setEnabled(false);
		Thread progressThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Random rand = new Random();
				System.out.println("bgColorFrom = "+bgColorFrom);
				if (!rebDotChBox.isSelected()) redDotColorTo = redDotColorFrom;
			  for (int redColor = redDotColorFrom; redColor <=redDotColorTo; redColor += 25)
				for (int circleColor = circleColorFrom; circleColor <= circleColorTo; circleColor += 30)
					for (int bgColor = bgColorFrom; bgColor >= bgColorTo; bgColor -= 30)
						for (int between = betweenFrom; between <= betweenTo; between += 5) {
							for (int thick = thickFrom; thick <= thickTo; thick += 2) {
								for (int i = circlesNfrom; i <= circlesNto; i++) {
		//								BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); //это было для png
									BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // это сделал для jpg
									Graphics2D g2d = bufferedImage.createGraphics();
									g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
									pen = new BasicStroke(thick);
									BasicStroke penForRec = new BasicStroke(1); // для отладочного вывода обрамляющего
																				// прямоугольника
									g2d.setStroke(pen);
	//								g2d.setColor(Color.WHITE);
									System.out.println("bgColor = "+bgColor);
									g2d.setColor(new Color(bgColor, bgColor, bgColor));
									g2d.setBackground(new Color(bgColor, bgColor, bgColor));
									g2d.fillRect(0, 0, width, height);
	//								g2d.setColor(Color.BLACK);
									g2d.setColor(new Color(circleColor,circleColor,circleColor));
									ArrayList<MyLine> circlesLineArr = new ArrayList<>();
									MyLine redDotLine = null;
									ArrayList<MyLine> redDotdsLineArr = new ArrayList<>();
		
									for (int j = 1; j <= i; j++) {
										g2d.setStroke(pen);
										int r = radius - between * j;
										int d = r * 2;
										int X = width / 2;
										int Y = height / 2;
										g2d.drawOval(X - r, Y - r, d, d);
		//									g2d.setStroke(penForRec);
		//									g2d.drawRect(X-r-thick/2, Y-r-thick/2, d+thick, d+thick);
										circlesLineArr.add(new MyLine(X - r - thick / 2, Y - r - thick / 2, X + r + thick / 2,
												Y + r + thick / 2));
									}
									if (rebDotChBox.isSelected()) {
										BasicStroke pen2 = new BasicStroke(1);
										g2d.setStroke(pen2);
										int red, gr, b;
								        if (redColor<= 127) { //для преобразования красного от темного до светлого, в середине - яркий красный 
								            // Phase 1: 0 → 127: dark red (128,0,0) → bright red (255,0,0)
								            // Red increases from 128 to 255 over 128 steps (0 to 127 inclusive)
								            double ratio = redColor / 127.0;
								            red = 128 + (int) ((255 - 128) * ratio); // 128 → 255
								            gr = 0;
								            b = 0;
								        } else {
								            // Phase 2: 128 → 255: bright red (255,0,0) → light red (255,220,220)
								            // Green & blue rise from 0 → 220 over 128 steps (128 to 255 inclusive = 128 values)
								            double ratio = (redColor - 128) / 127.0; // normalize to [0,1]
								            red = 255;
								            gr = (int) (230 * ratio); // 0 → 220
								            b = (int) (230 * ratio); // 0 → 220
								        }
										g2d.setColor(new Color(red,gr,b));
//										g2d.setColor(Color.red);
		//									int X = width / 2;
										//цикл для генерации нужного кол-ва красных точек
										for (int curDot = 0; curDot<redDotNumber; curDot++) {
											int d = curDot*50; //для изменения промежутка генерации координат
											int X = rand.nextInt(circlesLineArr.getFirst().getX()+d, circlesLineArr.getFirst().getX2()-d);
			//									int Y = height / 2;
											int Y = rand.nextInt(circlesLineArr.getFirst().getY()+d, circlesLineArr.getFirst().getY2()-d);
											g2d.fillOval(X - 5, Y - 5, 10, 10);
			//									g2d.setColor(Color.green);
											// pen2 = new BasicStroke(1);
			//									g2d.drawRect(X-3, Y-3, 6, 6);
											redDotLine = new MyLine(X - 5, Y - 5, X + 5, Y + 5);
											redDotdsLineArr.add(redDotLine);
										}
									}
									g2d.dispose();
		//						        File outputPNGfile = new File(dirPath+"\\"+"out_n"+i+"th"+thick+"btw"+between+".png");
									File outputPNGfile = new File(
											dirPath + "\\" + "out_n" + i +"cC"+ circleColor+"bC"+ bgColor+ "th" + thick + "btw" + between +"rDC"+redColor +".jpg");
									File outputCSVfile = new File(
											dirPath + "\\" + "out_n" + i+"cC"+ circleColor+"bC"+ bgColor + "th" + thick + "btw" + between +"rDC"+redColor +".txt");
									FileWriter myWriter = null;
									try {
										ImageIO.write(bufferedImage, "jpg", outputPNGfile);
										myWriter = new FileWriter(outputCSVfile);
										BufferedWriter myBWriter = new BufferedWriter(myWriter);
										circlesLineArr.forEach(line -> { // записываем параметры кругов
											try {
												myBWriter.write("1 " + line.getX() + " " + line.getY() + " " + line.getX2()
														+ " " + line.getY2());
												myBWriter.newLine();
											} catch (IOException e) {
												e.printStackTrace();
											}
										});
										// пишем параметры красной точки
										redDotdsLineArr.forEach(line->{
											try {
												myBWriter.write("0 " + line.getX() + " " + line.getY() + " "
														+ line.getX2() + " " + line.getY2());
												myBWriter.newLine();
											} catch (IOException e) {
												e.printStackTrace();
											}
										});
										myBWriter.close();// закрываем все соединения
										myWriter.close();
									} catch (IOException e1) {
										e1.printStackTrace();
									}
		
									progressI++;
									progressLabel.setText("progressI = " + progressI);
		//								System.out.println("progressI = "+progressI);
								}
							}
						}
				setEnabled(true);
				File directory = new File(dirPath);
				try {
					Desktop.getDesktop().open(directory);
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("trouble with opening folder");
				}
			}
		});
		progressThread.start();
	}
}
