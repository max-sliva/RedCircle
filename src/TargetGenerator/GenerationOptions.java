package TargetGenerator;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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

import TargetRecognize.Circle;

public class GenerationOptions extends JFrame{

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
	int filesNum = (circlesNto - circlesNfrom + 1)*((thickTo - thickFrom + 1) / 2)*((betweenTo - betweenFrom + 1)/5) ;
	JLabel filesNumLabel = new JLabel("filesNum = "+filesNum);
	int progressI = 0;
	JLabel progressLabel = new JLabel("progressI = "+progressI);
	BasicStroke pen;

	
	public GenerationOptions(String title) {
		super(title);
		Box circlesCountBox = create2ValueSliderBox("Circles number: ", 1, 10, circlesNfrom, circlesNto);
		Box circlesThickBox = create2ValueSliderBox("Circles thickness: ", 1, 70, thickFrom, thickTo);
		Box circlesSpaceBox = create2ValueSliderBox("Circles between: ", 10, 100, betweenFrom, betweenTo);

		JButton genBtn = new JButton("Generate targets");
		genBtn.addActionListener(e->{
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
	            generateTargetImages(1400, 1000, dirPath);
	            System.out.println("---!!targets created!!---");
	        } else {
	            System.out.println("No Selection");
	        }
		});
		Box genBtnBox = new Box(BoxLayout.X_AXIS);
		JCheckBox showImagesChBox = new JCheckBox("Show preview");
		showImagesChBox.addActionListener(e->{
			if (showImagesChBox.isSelected()) {
				imageFrom.repaint();
				imagesPreviewBox.setVisible(true);
				imagesPreviewBox.setVisible(false);
				imagesPreviewBox.setVisible(true);
				System.out.println("imageFrom width = "+imageFrom.getCurrentWidth());
				imageFrom.drawTarget(circlesFrom);
				if (circlesFrom.getFirst().getRadius()==0) { //если еще нет кругов
					circlesFrom.clear();
					int w = imageFrom.getCurrentWidth(); //ширина
					int h = imageFrom.getCurrentHeight(); //высота
					int radius = (w < h) ? (w/2): (h/2);  //радиус будет половиной от меньшего
					circlesFrom.add(new Circle(w/2, h/2, (radius==0)? radius : radius-10-thickFrom, thickFrom));
					imageFrom.drawTarget(circlesFrom);
					circlesTo.clear();
					w = imageTo.getCurrentWidth();
					h = imageTo.getCurrentHeight();
					radius = (radius==0)? radius : radius-10-thickTo;
					//для второго превью
					System.out.println("betweenTo = "+betweenTo);
					for (int i=0; i<circlesNto; i++) {
						System.out.println("radius"+i+"= " + radius);
						circlesTo.add(new Circle(w/2, h/2, radius, thickTo));
						radius = radius - betweenTo;
					}
					imageTo.drawTarget(circlesTo);
				}
				imagesPreviewBox.setVisible(false);
				imagesPreviewBox.setVisible(true);
			}
			else imagesPreviewBox.setVisible(false);
		});
		genBtnBox.add(showImagesChBox);
		genBtnBox.add(Box.createHorizontalGlue());
		genBtnBox.add(filesNumLabel);
		genBtnBox.add(Box.createHorizontalGlue());
		genBtnBox.add(genBtn);
		Box northBox = new Box(BoxLayout.Y_AXIS);
		northBox.add(circlesCountBox);
		northBox.add(circlesThickBox);
		northBox.add(circlesSpaceBox);
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
		int radius = (w < h) ? w / 2 : h / 2 ;
		circlesFrom.add(new Circle(w/2, h/2, radius));
		imageFrom.drawTarget(circlesFrom);
		w = imageTo.getCurrentWidth();
		h = imageTo.getCurrentHeight();
		radius = (w < h) ? w / 2 : h / 2 ;
		circlesTo.add(new Circle(w/2, h/2, radius));
		imageTo.drawTarget(circlesTo);
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
	
	private Box create2ValueSliderBox(String labelText, int minValue, int maxValue, int val1, int val2) {
		Box boxWith2Sliders = new Box(BoxLayout.X_AXIS);
		JLabel sliderLabel = new JLabel(labelText);
//		RangeSlider slider = new RangeSlider(minValue, maxValue, val1, val2);
		RangeSliderOld slider = new RangeSliderOld(minValue, maxValue);
		slider.setValue(val1);
		slider.setUpperValue(val2);
		if (labelText.contains("thick")) {
			thickFrom = val1;
			thickTo = val2;
		}
		slider.setPaintTrack(true);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMajorTickSpacing((maxValue - minValue) / 4 );
		slider.setMinorTickSpacing((maxValue - minValue+1) / 10);
		boxWith2Sliders.add(sliderLabel);
		JLabel val1Label = new JLabel(" "+val1+" ");
		val1Label.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
		JLabel val2Label = new JLabel(" "+val2+" ");
		val2Label.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
		boxWith2Sliders.add(val1Label);
		boxWith2Sliders.add(new JLabel("-"));
		boxWith2Sliders.add(val2Label);
		boxWith2Sliders.add(slider);
		slider.addChangeListener(e->{
			val1Label.setText(" "+slider.getValue()+" ");
			val2Label.setText(" "+slider.getUpperValue()+" ");
			if (labelText.contains("thick")) {
				thickFrom = slider.getValue();
				thickTo = slider.getUpperValue();
				circlesFrom.forEach(circle ->{
					circle.setThickness(thickFrom);
				});
				circlesTo.forEach(circle ->{
					circle.setThickness(thickTo);
				});
				imageFrom.drawTarget(circlesFrom);
				imageTo.drawTarget(circlesTo);
			}
			if (labelText.contains("number")) {
				circlesNfrom = slider.getValue();
				circlesNto = slider.getUpperValue();
				System.out.println("circlesNfrom = "+circlesNfrom);
				System.out.println("circlesNto = "+circlesNto);
				//TODO сделать изменения в массивах кругов circlesFrom
				imageFrom.drawTarget(circlesFrom);
				
				circlesTo.clear();
				int w = imageTo.getCurrentWidth();
				int h = imageTo.getCurrentHeight();
				int radius = (w < h) ? (w/2): (h/2); 
				radius = (radius==0)? radius : radius-10-thickTo;
				//для второго превью
				System.out.println("betweenTo = "+betweenTo);
				for (int i=0; i<circlesNto; i++) {
					System.out.println("radius"+i+"= " + radius);
					circlesTo.add(new Circle(w/2, h/2, radius, thickTo));
					radius = radius - betweenTo;
				}
				imageTo.drawTarget(circlesTo);
			}
			if (labelText.contains("between")) {
				betweenFrom = slider.getValue();
				betweenTo = slider.getUpperValue();
				System.out.println("betweenTo = "+betweenTo);
				//TODO сделать изменения в массивах кругов circlesFrom
				imageFrom.drawTarget(circlesFrom);
				circlesTo.clear();
				int w = imageTo.getCurrentWidth();
				int h = imageTo.getCurrentHeight();
				int radius = (w < h) ? (w/2): (h/2); 
				radius = (radius==0)? radius : radius-10-thickTo;
				//для второго превью
				System.out.println("betweenTo = "+betweenTo);
				for (int i=0; i<circlesNto; i++) {
					System.out.println("radius"+i+"= " + radius);
					circlesTo.add(new Circle(w/2, h/2, radius, thickTo));
					radius = radius - betweenTo;
				}
				imageTo.drawTarget(circlesTo);
			}
			filesNum = (circlesNto - circlesNfrom + 1)*((thickTo - thickFrom)/2+1)*((betweenTo - betweenFrom)/5+1) ;
			filesNumLabel.setText("filesNum = "+filesNum);
		});
		return boxWith2Sliders;
	}

	private void generateTargetImages(int w, int h, String dirPath) {
		int width = w;
        int height = h;
//        int between = betweenFrom;
        int radius = (width > height)? (height - 40) / 2 : (width - 40) / 2;  
//		BasicStroke pen;
		progressI = 0;
//		JDialog progressDialog = new JDialog(this);
////		JLabel progressLabel = new JLabel("progressI = "+progressI);
//		progressDialog.getContentPane().add(progressLabel);
//		progressDialog.setLocationRelativeTo(null);
//		progressDialog.setVisible(true);
		progressLabel.setText("progressI = "+progressI);
		setEnabled(false);
		// TODO добавить красную точку и открывать папку в конце
		Thread progressThread = new Thread(new Runnable() {
			@Override
			public void run() {
					for (int between = betweenFrom; between<=betweenTo; between+=5) {
						for (int thick = thickFrom; thick <= thickTo; thick+=2) {
							for (int i = circlesNfrom; i <= circlesNto; i++) {
								BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
								Graphics2D g2d = bufferedImage.createGraphics();
								g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
						        pen = new BasicStroke(thick);
						        BasicStroke penForRec = new BasicStroke(1); //для отладочного вывода обрамляющего прямоугольника
								g2d.setStroke(pen);
						        g2d.setColor(Color.WHITE);
						        g2d.fillRect(0, 0, width, height);
						        g2d.setColor(Color.BLACK);
						        ArrayList<MyLine> lineArr = new ArrayList<>();
						        
						        for (int j = 1; j<=i; j++) {
						        	g2d.setStroke(pen);
						        	int r = radius - between*j;
									int d = r*2;
									int X = width / 2;
									int Y = height / 2;
									g2d.drawOval(X-r, Y-r, d, d);
//									g2d.setStroke(penForRec);
//									g2d.drawRect(X-r-thick/2, Y-r-thick/2, d+thick, d+thick);
									lineArr.add(new MyLine(X-r-thick/2, Y-r-thick/2, X+r+thick/2, Y+r+thick/2));
						        }
						        g2d.dispose();
						        File outputPNGfile = new File(dirPath+"\\"+"out_n"+i+"th"+thick+"btw"+between+".png");
						        File outputCSVfile = new File(dirPath+"\\"+"out_n"+i+"th"+thick+"btw"+between+".csv");
						        FileWriter myWriter = null;
						        try {
									ImageIO.write(bufferedImage, "png", outputPNGfile);
									myWriter=new FileWriter(outputCSVfile);
									BufferedWriter myBWriter=new BufferedWriter(myWriter);
									//TODO записать параметры кругов и красной точки
									lineArr.forEach(line ->{
										try {
											myBWriter.write("1 "+line.getX()+" "+line.getY()+" "+line.getX2()+" "+line.getY2());
											myBWriter.newLine();
										} catch (IOException e) {
											e.printStackTrace();
										}
									});
									//myBWriter.write("");
									myBWriter.newLine();
									myBWriter.close();//закрываем все соединения
									myWriter.close();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								
								progressI++;
								progressLabel.setText("progressI = "+progressI);
//								System.out.println("progressI = "+progressI);
							}
						}
					}
					setEnabled(true);
			}
		});
		progressThread.start();
	}
}
