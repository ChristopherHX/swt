package org.iMage.iCatcher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.imaging.ImageReadException;
import org.iMage.HDrize.CameraCurve;
import org.iMage.HDrize.HDrize;
import org.iMage.HDrize.base.ICameraCurve;
import org.iMage.HDrize.base.images.EnhancedImage;
import org.iMage.HDrize.base.images.HDRImageIO.ToneMapping;
import org.iMage.HDrize.matrix.MatrixCalculator;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = -6727407640530197416L;
	private HDrize hdr = new HDrize();
	private JPanel panel;
	private EnhancedImage[] images;
	private String prefix;
	private ImageIcon hdrIcon = new ImageIcon();
	private BufferedImage hdrImage;
	private double lambda;
	private ICameraCurve[] curves = new ICameraCurve[3];
	private JSlider slider;
	private JButton runHDR;
	private JButton saveHDR;
	private JButton saveCurve;
	private JButton showCurve;
	private JButton buttonHDR;

	public MainFrame() {
		super("iCatcher");
		curves[0] = new CameraCurve();
		curves[0].calculate();
		setResizable(false);
		GridLayout layout = new GridLayout(2, 2);
		setLayout(layout);
		setPreferredSize(new Dimension(800, 700));
		panel = new JPanel(new FlowLayout());
		JScrollPane pane = new JScrollPane(panel);
		add(pane);

//		JPanel hdrPanel = new JPanel();
//		hdrPanel.setLayout(new BoxLayout(hdrPanel, BoxLayout.Y_AXIS));
		buttonHDR = new JButton();
		buttonHDR.setIcon(hdrIcon);
		buttonHDR.addActionListener(e -> {
			JFrame frame = new JFrame(prefix + "_HDR");
			JLabel label = new JLabel();
			label.setIcon(new ImageIcon(hdrImage));
			frame.add(new JScrollPane(label));
			frame.pack();
			frame.setVisible(true);
		});
//		hdrPanel.add(button);
//		JButton showCurve = new JButton("SHOW CURVE");
//		JButton saveCurve = new JButton("SAVE CURVE");
//		JButton saveHDR = new JButton("SAVE HDR");
//		Box box1 = Box.createHorizontalBox();
//		box1.add(showCurve);
//		box1.add(saveCurve);
//		box1.add(saveHDR);
//		hdrPanel.add(box1);
//		add(hdrPanel);
		add(buttonHDR);

		JPanel comboPanel = new JPanel();
		comboPanel.setLayout(new BoxLayout(comboPanel, BoxLayout.Y_AXIS));
		JLabel camCurve = new JLabel("Camera Curve");
		comboPanel.add(camCurve);
		JComboBox<String> curveBox = new JComboBox<String>(
				new String[] { "Standard Curve", "Calculated Curve", "Loaded Curve" });
		comboPanel.add(curveBox);
		JLabel toneMap = new JLabel("Tone Mapping");
		comboPanel.add(toneMap);
		JComboBox<String> toneBox = new JComboBox<>(new String[] { "Simple Map", "Standard Gamma", "SRGB Gamma" });
		comboPanel.add(toneBox);
		add(comboPanel);

		JPanel lambdaPanel = new JPanel();
		lambdaPanel.setLayout(new BoxLayout(lambdaPanel, BoxLayout.Y_AXIS));
		showCurve = new JButton("SHOW CURVE");
		showCurve.addActionListener(e -> {
			JFrame frame = new JFrame("Calculated Curve (" + slider.getValue() + "s, " + lambda + "L)");
			frame.add(new ShowCurve(curves[1]));
			frame.pack();
			frame.setVisible(true);
		});
		saveCurve = new JButton("SAVE CURVE");
		saveCurve.addActionListener(e -> {
			if (curves[1] == null) {
				curves[1] = new CameraCurve(images, slider.getValue(), lambda, new MatrixCalculator());
				curves[1].calculate();
			}
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Save Curve");
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setFileFilter(new FileFilter() {
				@Override
				public String getDescription() {
					return "Curve (BIN File)";
				}

				@Override
				public boolean accept(File f) {
					return f.getName().endsWith(".bin");
				}
			});
			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				try {
					curves[1].save(new FileOutputStream(chooser.getSelectedFile()));
				} catch (IOException e2) {
					controlHDR();
				}
			}
		});
		saveHDR = new JButton("SAVE HDR");
		saveHDR.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Save HDR Image");
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setFileFilter(new FileFilter() {
				@Override
				public String getDescription() {
					return "PNG Image";
				}

				@Override
				public boolean accept(File f) {
					return f.getName().endsWith(".png");
				}
			});
			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				try {
					ImageIO.write(hdrImage, "png", chooser.getSelectedFile());
				} catch (IOException e2) {
					controlHDR();
				}
			}
		});
		Box box1 = Box.createHorizontalBox();
		box1.add(showCurve);
		box1.add(saveCurve);
		box1.add(saveHDR);
		lambdaPanel.add(box1);
		JLabel lambdaLabel = new JLabel("Lambda");
		lambdaPanel.add(lambdaLabel);
		JTextField lambdaText = new JTextField("20.0", 1);
		lambda = 20;
		lambdaText.getDocument().addDocumentListener(new DocumentListener() {
			private void setText() {
				String lambdaString = lambdaText.getText();
				if (lambdaString.matches("(\\d)+(\\.(\\d)+)?")) {
					lambda = Double.parseDouble(lambdaString);
					if (lambda >= 1 && lambda <= 100) {
						lambdaText.setForeground(Color.BLACK);
						return;
					}
				}
				lambdaText.setForeground(Color.RED);
				controlHDR();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				setText();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				setText();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				setText();
			}
		});
		lambdaPanel.add(lambdaText);
		JLabel sliderLabel = new JLabel("Samples (500)");
		lambdaPanel.add(sliderLabel);
		slider = new JSlider(SwingConstants.HORIZONTAL, 1, 1000, 500);
		Hashtable<Integer, JLabel> sliderTable = new Hashtable<>();
		sliderTable.put(1, new JLabel("1"));
		sliderTable.put(1000, new JLabel("1000"));
		slider.setLabelTable(sliderTable);
		slider.setPaintLabels(true);
		slider.addChangeListener(e -> {
			sliderLabel.setText("Samples (" + slider.getValue() + ")");
			controlHDR();
		});
		lambdaPanel.add(slider);

		JButton loadDir = new JButton("LOAD DIR");
		loadDir.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Load Directory");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setFileFilter(new FileFilter() {
				@Override
				public String getDescription() {
					return "Folder";
				}

				@Override
				public boolean accept(File f) {
//					String name = f.getName();
//					return name.endsWith(".jpg") || name.endsWith(".jpeg");
					return true;
				}
			});
			chooser.showOpenDialog(this);
			File[] files = chooser.getSelectedFile().listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".tiff");
				}
			});
			if (files.length % 2 == 0 && files.length >= 3) {
				controlHDR();
				return;
			}
			for (File image : files) {
				String name = image.getName();
				name = name.substring(0, name.length() - ".jpg".length());
				if (name.length() < 3 || !files[0].getName().startsWith(name.substring(0, 3))) {
					controlHDR();
					return;
				}
				for (int i = 1; i < name.length(); i++) {
					if (name.substring(0, i).equals(files[0].getName().substring(0, i))) {
						prefix = name.substring(0, i);
					} else {
						break;
					}
				}
			}
			images = new EnhancedImage[files.length];
			try {
				for (int i = 0; i < files.length; i++) {
					images[i] = new EnhancedImage(new FileInputStream(files[i]));
					ImageIcon icon = new ImageIcon(generatePreview(ImageIO.read(files[i])));
					JLabel label = new JLabel(icon);
					panel.add(label);
				}
			} catch (IOException e1) {
			} catch (ImageReadException e1) {
			}
			pack();
			controlHDR();
		});
		JButton loadCurve = new JButton("LOAD CURVE");
		loadCurve.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Load Curve");
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setFileFilter(new FileFilter() {
				@Override
				public String getDescription() {
					return "Curve (BIN File)";
				}

				@Override
				public boolean accept(File f) {
					return f.getName().endsWith(".bin");
				}
			});
			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				try {
					curves[2] = new CameraCurve(new FileInputStream(chooser.getSelectedFile()));
					curves[2].calculate();
				} catch (IOException e2) {
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
				}
			}
			controlHDR();
		});
		runHDR = new JButton("RUN HDrize");
		runHDR.addActionListener(e -> {
			ICameraCurve curve = curves[curveBox.getSelectedIndex()];
			if (curve == null && curveBox.getSelectedIndex() == 1) {
				curve = curves[1] = new CameraCurve(images, slider.getValue(), lambda, new MatrixCalculator());
				curve.calculate();
			}
			hdrImage = hdr.createRGB(images, curve, ToneMapping.values()[toneBox.getSelectedIndex()]);
			hdrIcon.setImage(generatePreview(hdrImage));
			controlHDR();
		});
		Box box = Box.createHorizontalBox();
		box.add(loadDir);
		box.add(loadCurve);
		box.add(runHDR);
		lambdaPanel.add(box);
		add(lambdaPanel);
		pack();
		controlHDR();
		setVisible(true);

	}

	private Image generatePreview(Image hdrImage) {
		return hdrImage.getScaledInstance(350, 250, 0);
	}

	private void controlHDR() {
		runHDR.setEnabled(images != null);
		boolean b = hdrImage != null;
		saveHDR.setEnabled(b);
		saveCurve.setEnabled(curves[1] != null);
		showCurve.setEnabled(curves[1] != null);
		buttonHDR.setEnabled(b);
	}

}
