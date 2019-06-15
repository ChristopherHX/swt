package image.icatcher;

import java.awt.Component;
import java.awt.Dialog;
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
import java.net.URISyntaxException;
import java.util.Hashtable;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.imaging.ImageReadException;
import org.iMage.HDrize.CameraCurve;
import org.iMage.HDrize.HDrize;
import org.iMage.HDrize.base.ICameraCurve;
import org.iMage.HDrize.base.images.EnhancedImage;
import org.iMage.HDrize.base.images.HDRImageIO.ToneMapping;
import org.iMage.HDrize.matrix.MatrixCalculator;

public class MainWindow extends JFrame {
    private static final long serialVersionUID = 1413007845669740256L;
    private JPanel sourcepics;
    private JSlider samplesslider;
    private EnhancedImage[] images;
    private BufferedImage hdrImage;
    private HDrize hdr = new HDrize();
    private ICameraCurve[] curves = new ICameraCurve[3];
    private String prefix;
    private JComboBox<String> curveBox;
    private JButton runHDR;
    private JButton saveHDR;
    private JButton saveCurve;
    private JButton showCurve;
    private JButton HDRPreviewbutton;

    private static String minPrefix(File[] strings, int min) {
        if (strings.length == 0 || strings[0].getName().length() <= min) {
            return null;
        }
        for (int prefixLen = 0; prefixLen < strings[0].getName().length(); prefixLen++) {
            char c = strings[0].getName().charAt(prefixLen);
            for (int i = 1; i < strings.length; i++) {
                if (prefixLen >= strings[i].getName().length() || strings[i].getName().charAt(prefixLen) != c) {
                    return prefixLen < min ? null : strings[0].getName().substring(0, prefixLen);
                }
            }
        }
        return strings[0].getName();
    }

    public MainWindow() throws URISyntaxException {
        (curves[0] = new CameraCurve()).calculate();
        var file = getClass().getClassLoader().getResource("2019-06-05-200418.jpg");
        var layout = new GridLayout(2, 2);
        // var layout = new BoxLayout(this, SwingConstants.HORIZONTAL);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("iMage.iCatcher");
        setResizable(false);
        setSize(800, 700);
        setPreferredSize(new Dimension(800, 700));
        setLayout(layout);
        sourcepics = new JPanel(new FlowLayout());
        var scrollpane = new JScrollPane(sourcepics);
        add(scrollpane);
        // for (int i = 0; i < 3; i++) {
        //     var pic = new ScaledImageLabel(file, false);
        //     pic.setPreferredSize(new Dimension(350, 250));
        //     sourcepics.add(pic);
        // }

        // sourcepics.add(new ScaledImageLabel(file, false));
        // sourcepics.add(new ScaledImageLabel(file, false));
        var sc = new ScaledImageLabel(file, true);
        HDRPreviewbutton = new JButton();
        var image = new ImageIcon(file);
        // image.setImage(image.getImage().getScaledInstance(350, 250, 0));
        // HDRPreviewbutton.setIcon(image);
        HDRPreviewbutton.addActionListener(e -> {
            JDialog frame = new JDialog(this);
            frame.setAlwaysOnTop(true);
            frame.setTitle(prefix + "_HDR");
            frame.add(new JScrollPane(new JLabel(new ImageIcon(hdrImage))));
            frame.setVisible(true);
            frame.pack();
        });
        add(HDRPreviewbutton);

        // JPanel leftJPanel = new JPanel();
        // leftJPanel.setLayout(new BoxLayout(leftJPanel, BoxLayout.Y_AXIS));
        Box leftJPanel = Box.createVerticalBox();
        leftJPanel.add(Box.createVerticalGlue());
        leftJPanel.add(Box.createVerticalGlue());
        leftJPanel.add(Box.createVerticalGlue());
        var textpane = new JLabel("Camera Curve", SwingConstants.LEFT);// new JTextPane();
        // textpane.setHorizontalAlignment(SwingConstants.LEFT);
        // textpane.setText("Camera Curve");
        // leftJPanel.add(new JTextPane());
        leftJPanel.add(textpane, Component.LEFT_ALIGNMENT);
        curveBox = new JComboBox<>(new String[] { "Standard Curve", "Calculated Curve", "Loaded Curve" });
        leftJPanel.add(curveBox);
        leftJPanel.add(Box.createVerticalGlue());
        leftJPanel.add(Box.createVerticalGlue());
        leftJPanel.add(Box.createVerticalGlue());
        var tonepane = new JLabel("Tone Mapping", SwingConstants.LEFT);
        // var tonepane = new JTextPane();
        // tonepane.setText("Tone Mapping");
        // leftJPanel.add(new JTextPane());
        leftJPanel.add(tonepane, Component.LEFT_ALIGNMENT);
        var tonebox = new JComboBox<>(new String[] { "Simple Map", "Standard Gamma", "SRGB Gamma" });
        // add(curveBox);
        leftJPanel.add(tonebox);
        leftJPanel.add(Box.createVerticalGlue());
        leftJPanel.add(Box.createVerticalGlue());
        leftJPanel.add(Box.createVerticalGlue());
        add(leftJPanel);
        JPanel rightJPanel = new JPanel();

        samplesslider = new JSlider(SwingConstants.HORIZONTAL, 1, 1000, 500);
        samplesslider.addChangeListener(e -> {

        });

        Hashtable labeltable = new Hashtable();
        labeltable.put(1, new JLabel("1"));
        labeltable.put(1000, new JLabel("1000"));
        samplesslider.setLabelTable(labeltable);
        samplesslider.setPaintLabels(true);
        rightJPanel.add(samplesslider);
        rightJPanel.setLayout(new BoxLayout(rightJPanel, BoxLayout.Y_AXIS));

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
                    return true;
                }
            });
            if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File[] files = chooser.getSelectedFile().listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".jfiff");
                    }
                });
                if (files.length % 2 == 0 && files.length >= 3) {
                    // controlHDR();
                }
                prefix = minPrefix(files, 3);
                images = new EnhancedImage[files.length];
                sourcepics.removeAll();
                try {
                    for (int i = 0; i < files.length; i++) {
                        images[i] = new EnhancedImage(new FileInputStream(files[i]));
                        ImageIcon icon = new ImageIcon(generatePreview(ImageIO.read(files[i])));
                        JLabel label = new JLabel(icon);
                        sourcepics.add(label);
                    }
                } catch (IOException e1) {
                    // controlHDR();
                } catch (ImageReadException e1) {
                    // controlHDR();
                }
                pack();
            }
            changed();
        });
        rightJPanel.add(loadDir);

        saveHDR = new JButton("Save HDR");
        saveHDR.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File(prefix + "_HDR.png"));
            chooser.setDialogTitle("Save HDR");
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
                var ifile = chooser.getSelectedFile();
                if (!ifile.getName().endsWith(".png")) {
                    ifile = new File(ifile.getPath() + ".png");
                }
                try {
                    ImageIO.write(hdrImage, "png", ifile);                    
                } catch (IOException r) {
                    //TODO: handle exception
                }
            }
        });
        rightJPanel.add(saveHDR);

        runHDR = new JButton("RUN HDrize");
		runHDR.addActionListener(e -> {
            ICameraCurve curve = curves[curveBox.getSelectedIndex()];
            if(curve == null && curveBox.getSelectedIndex() == 1) {
                curve = curves[1] = new CameraCurve(images, samplesslider.getValue(), 20, new MatrixCalculator());
                curve.calculate();
            }
			hdrImage = hdr.createRGB(images, curve, ToneMapping.values()[tonebox.getSelectedIndex()]);
            image.setImage(generatePreview(hdrImage));
            changed();            
        });
        
        rightJPanel.add(runHDR);

        saveCurve = new JButton("RUN saveCurve");
		saveCurve.addActionListener(e -> {
            if(curves[1] == null/*  && curveBox.getSelectedIndex() == 1 Diogo to patch */) {
                curves[1] = new CameraCurve(images, samplesslider.getValue(), 20, new MatrixCalculator());
                curves[1].calculate();
            }
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save Curve");
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setFileFilter(new FileFilter() {
                @Override
                public String getDescription() {
                    return "Curve (.bin)";
                }

                @Override
                public boolean accept(File f) {
                    return f.getName().endsWith(".bin");
                }
            });
            if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    curves[1].save(new FileOutputStream(chooser.getSelectedFile()));
                } catch (IOException r) {
                    System.err.println("invalidfile");
                    //TODO: handle exception
                }
            }
        });

        rightJPanel.add(saveCurve);

        JButton loadCurve = new JButton("RUN loadCurve");
		loadCurve.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Load Curve");
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setFileFilter(new FileFilter() {
                @Override
                public String getDescription() {
                    return "Curve (.bin)";
                }

                @Override
                public boolean accept(File f) {
                    return f.getName().endsWith(".bin");
                }
            });
            if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    curves[2] = new CameraCurve(new FileInputStream(chooser.getSelectedFile()));
                } catch (ClassNotFoundException r) {
                    //TODO: handle exception
                } catch (IOException r) {
                //TODO: handle exception
            }
            }
            changed();
        });
        
        rightJPanel.add(loadCurve);

        showCurve = new JButton("showCurve");
		showCurve.addActionListener(e -> {
            JDialog frame = new JDialog(this, String.format(Locale.ENGLISH, "Calculated Curve (%ds,%.1fl)", samplesslider.getValue(), 20.0), Dialog.ModalityType.DOCUMENT_MODAL);
            frame.add(new CurvePanel(curves[curveBox.getSelectedIndex()])/* new JLabel(new ImageIcon(drawCurve()) )*/);
            frame.pack();
            frame.setVisible(true);
        });
        
        rightJPanel.add(showCurve);
        add(rightJPanel);
        pack();
        // sourcepics.setSize((int)(sourcepics.getWidth() *
        // ((double)sourcepics.getHeight() / scrollpane.getHeight())),
        // scrollpane.getHeight());
        changed();
        setVisible(true);
    }

    private BufferedImage drawCurve() {
        BufferedImage image = new BufferedImage(256, 512, BufferedImage.TYPE_INT_ARGB);
        int index = curveBox.getSelectedIndex();
        for (int i = 0; i < image.getWidth(); i++) {
            image.setRGB(i, (int)(curves[index].getResponse(new int[]{i, 0, 0})[0] * 100), 0xffff0000 );
            image.setRGB(i, (int)(curves[index].getResponse(new int[]{0, i, 0})[1] * 100), 0xff00ff00 );
            image.setRGB(i, (int)(curves[index].getResponse(new int[]{0, 0, i})[2] * 100), 0xff0000ff );
            // image.setRGB(i, (int)(curves[curveBox.getSelectedIndex()].getResponse(new int[]{i, 0, 0})[1] * 100), 0xff0000ff );
            // image.setRGB(i, (int)(curves[curveBox.getSelectedIndex()].getResponse(new int[]{0, i, 0})[2] * 100), 0xffff0000 );
            // image.setRGB(i, (int)(curves[curveBox.getSelectedIndex()].getResponse(new int[]{0, 0, i})[0] * 100), 0xff00ff00 );
            // image.setRGB(i, (int)(curves[curveBox.getSelectedIndex()].getResponse(new int[]{i, 0, 0})[1] * 100), 0xff00ff00 );
            // image.setRGB(i, (int)(curves[curveBox.getSelectedIndex()].getResponse(new int[]{0, i, 0})[2] * 100), 0xff0000ff );
            // image.setRGB(i, (int)(curves[curveBox.getSelectedIndex()].getResponse(new int[]{0, 0, i})[0] * 100), 0xffff0000 );
        }
        return image;
    }

    private Image generatePreview(Image read) {
		return read.getScaledInstance(350, 250, 0);
    }

    private void changed() {
        runHDR.setEnabled(images != null);
        saveHDR.setEnabled(hdrImage != null);
        HDRPreviewbutton.setEnabled(hdrImage != null);
        saveCurve.setEnabled(curves[1] != null);
        showCurve.setEnabled(curves[1] != null);
    }
}