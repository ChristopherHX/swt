package image.icatcher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
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

/**
 * Mainframe of iCatcher which layout configuration
 */
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
    private JButton hDRPreviewbutton;
    private JPanel grid;
    private int calculatedsample;
    private double lambda;
    private double calculatedlambda;
    private JTextField textfield;
    private JComboBox<String> tonebox;
    private ImageIcon image;
    private JLabel samplesl;

    private class LambdaChangedListener implements DocumentListener {
        private void textchanged() {
            try {
                lambda = Double.parseDouble(textfield.getText());
                textfield.setForeground(lambda < 1 || lambda > 100 ? Color.RED : Color.BLACK);
            } catch (NumberFormatException format) {
                textfield.setForeground(Color.RED);
            }
            changed();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            textchanged();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            textchanged();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            textchanged();
        }
    }

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

    /**
     * Creates and show iCatchers MainWindow
     */
    public MainWindow() {
        createAndShowUI();
    }

    private void createAndShowUI() {
        curves[0] = new CameraCurve();
        curves[0].calculate();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("iMage.iCatcher");
        setResizable(false); // Unset for real Responsiveness
        setPreferredSize(new Dimension(800, 700));
        setLayout(new BorderLayout());
        grid = new JPanel(new GridBagLayout()); // RealGridlayout
        add(grid);
        var tpanel = new JPanel();
        tpanel.setPreferredSize(new Dimension(800, 10));
        add(tpanel, BorderLayout.PAGE_START);
        var lpanel = new JPanel();
        lpanel.setPreferredSize(new Dimension(10, 700));
        add(lpanel, BorderLayout.LINE_START);
        var rpanel = new JPanel();
        rpanel.setPreferredSize(new Dimension(10, 700));
        add(rpanel, BorderLayout.LINE_END);
        createImageContainer();
        createDropdowns();
        createButtons();
        textfield = new JTextField("20.0");
        var lambdachange = new LambdaChangedListener();
        textfield.getDocument().addDocumentListener(lambdachange);
        var c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 4;
        c.gridy = 4;
        c.gridwidth = 3;
        grid.add(textfield, c);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        samplesl = new JLabel();
        grid.add(samplesl, c);
        samplesslider = new JSlider(SwingConstants.HORIZONTAL, 1, 1000, 500);
        samplesslider.addChangeListener(e -> samplesliderchanged());
        Hashtable<Integer, JLabel> labeltable = new Hashtable<>();
        labeltable.put(1, new JLabel("1"));
        labeltable.put(1000, new JLabel("1000"));
        samplesslider.setLabelTable(labeltable);
        samplesslider.setPaintLabels(true);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 4;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        grid.add(samplesslider, c);
        c = new GridBagConstraints();
        c.gridx = 4;
        c.gridy = 3;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        grid.add(new JLabel("Lambda"), c);
        createWhiteSpace();
        pack();
        samplesliderchanged();
        lambdachange.changedUpdate(null);
        setVisible(true);
    }

    private void createButtons() {
        JButton loadDir = new JButton("LOAD DIR");
        loadDir.addActionListener(e -> loadSourceImages());
        var c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 7;
        c.gridwidth = 2;
        grid.add(loadDir, c);
        saveHDR = new JButton("Save HDR");
        saveHDR.addActionListener(e -> saveHDRImage());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 5;
        c.gridy = 1;
        c.gridwidth = 2;
        grid.add(saveHDR, c);
        runHDR = new JButton("RUN HDrize");
        runHDR.addActionListener(e -> runHDrize());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 5;
        c.gridy = 7;
        c.gridwidth = 2;
        grid.add(runHDR, c);
        saveCurve = new JButton("saveCurve");
        saveCurve.addActionListener(e -> saveCurve());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 1;
        c.gridwidth = 2;
        grid.add(saveCurve, c);
        JButton loadCurve = new JButton("loadCurve");
        loadCurve.addActionListener(e -> loadCurve());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 7;
        c.gridwidth = 2;
        grid.add(loadCurve, c);
        showCurve = new JButton("showCurve");
        showCurve.addActionListener(e -> showCurve());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 2;
        grid.add(showCurve, c);
    }

    private void createDropdowns() {
        var textpane = new JLabel("Camera Curve", SwingConstants.LEFT);
        var c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        grid.add(textpane, c);
        curveBox = new JComboBox<>(new String[] { "Standard Curve", "Calculated Curve", "Loaded Curve" });
        curveBox.addActionListener(e -> selectedCurveChanged());
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        grid.add(curveBox, c);
        var tonepane = new JLabel("Tone Mapping");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        grid.add(tonepane, c);
        tonebox = new JComboBox<>(new String[] { "Simple Map", "Standard Gamma", "SRGB Gamma" });
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        grid.add(tonebox, c);
    }

    private void createImageContainer() {
        var file = getClass().getClassLoader().getResource("default.png");
        sourcepics = new JPanel(new FlowLayout());
        var c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 2;
        c.fill = GridBagConstraints.BOTH;
        var sourcescrollpane = new JScrollPane(sourcepics);
        sourcescrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        sourcescrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        grid.add(sourcescrollpane, c);
        hDRPreviewbutton = new JButton();
        image = new ImageIcon(file);
        hDRPreviewbutton.setIcon(image);
        hDRPreviewbutton.addActionListener(e -> showHDRPreview());
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 6;
        c.fill = GridBagConstraints.HORIZONTAL;
        grid.add(hDRPreviewbutton, c);
    }

    private void createWhiteSpace() {
        for (int i = 0; i < 7; i++) {
            var c = new GridBagConstraints();
            c.gridx = i;
            c.gridy = 2;
            c.gridwidth = 1;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = i > 0 ? 1.0 / 256 : 1;
            c.weighty = 0.2;
            grid.add(new JLabel(), c);
        }
        var c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 7;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0.2;
        grid.add(new JLabel(), c);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 8;
        c.gridwidth = 7;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0.2;
        grid.add(new JLabel(), c);
    }

    private void selectedCurveChanged() {
        if (curveBox.getSelectedIndex() == 2 && curves[2] == null) {
            loadCurve();
        } else {
            changed();
        }
    }

    private void samplesliderchanged() {
        samplesl.setText(String.format("Samples (%d)", samplesslider.getValue()));
        changed();
    }

    private void runHDrize() {
        ICameraCurve curve = curves[curveBox.getSelectedIndex()];
        if (curve == null) {
            switch (curveBox.getSelectedIndex()) {
            case 1:
                curve = reCalculateCurvefromSource();
                break;
            case 2:
                curve = loadCurve();
                if (curve == null) {
                    JOptionPane.showMessageDialog(this, "You need a curve loaded");
                    return;
                }
                break;
            default:
                JOptionPane.showMessageDialog(this, "Curve not found (Internal Error)");
                return;
            }
        }
        hdrImage = hdr.createRGB(images, curve, ToneMapping.values()[tonebox.getSelectedIndex()]);
        image.setImage(generatePreview(hdrImage));
        changed();
    }

    private void saveHDRImage() {
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
                JOptionPane.showMessageDialog(this, "Failed to Save");
            }
        }
    }

    private void saveCurve() {
        var curve = reCalculateCurvefromSource();
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
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                curve.save(new FileOutputStream(chooser.getSelectedFile()));
            } catch (IOException r) {
                JOptionPane.showMessageDialog(this, "Failed to Save");
            }
        }
    }

    private void loadSourceImages() {
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
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File[] files = chooser.getSelectedFile().listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".jpe")
                            || name.endsWith(".jfif");
                }
            });
            if (files.length % 2 == 0 || files.length < 3) {
                JOptionPane.showMessageDialog(null, "jpeg Filecount must be odd and greater than 2");
                changed();
                return;
            }
            prefix = minPrefix(files, 3);
            if (prefix == null) {
                JOptionPane.showMessageDialog(null, "jpeg files have to a common prefix with min 3 chars");
                changed();
                return;
            }
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
                JOptionPane.showMessageDialog(this, "Image can't be read correctly from filesytem");
                sourcepics.removeAll();
                images = null;
            } catch (ImageReadException e1) {
                JOptionPane.showMessageDialog(this, "Image can't be read correctly");
                sourcepics.removeAll();
                images = null;
            }
            pack();
        }
        changed();
    }

    private void showCurve() {
        JDialog frame = new JDialog(this,
                String.format(Locale.ENGLISH, "Calculated Curve (%ds,%.1fl)", calculatedsample, calculatedlambda),
                Dialog.ModalityType.DOCUMENT_MODAL);
        frame.setPreferredSize(new Dimension(400, 350));
        frame.add(new CurvePanel(curves[1]));
        frame.pack();
        // frame.setResizable(false);
        frame.setVisible(true);
    }

    private void showHDRPreview() {
        JDialog frame = new JDialog(this, prefix + "_HDR", Dialog.ModalityType.DOCUMENT_MODAL);
        frame.setPreferredSize(new Dimension(400, 350));
        frame.add(new JScrollPane(new JLabel(new ImageIcon(hdrImage))));
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private ICameraCurve reCalculateCurvefromSource() {
        if (curves[1] == null || calculatedsample != samplesslider.getValue() || calculatedlambda != lambda) {
            calculatedsample = samplesslider.getValue();
            calculatedlambda = lambda;
            curves[1] = new CameraCurve(images, calculatedsample, calculatedlambda, new MatrixCalculator());
            curves[1].calculate();
        }
        return curves[1];
    }

    private ICameraCurve loadCurve() {
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
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                curves[2] = new CameraCurve(new FileInputStream(chooser.getSelectedFile()));
                curveBox.setSelectedIndex(2);
            } catch (ClassNotFoundException r) {
                JOptionPane.showMessageDialog(this, "Missing Dependency, please reinstall");
            } catch (IOException r) {
                JOptionPane.showMessageDialog(this, "Failed to read Curve");
            }
        }
        changed();
        return curves[2];
    }

    private Image generatePreview(Image read) {
		return read.getScaledInstance(350, 250, 0);
    }

    private void changed() {
        runHDR.setEnabled(images != null
        && (curveBox.getSelectedIndex() != 1 || textfield.getForeground() != Color.RED));
        saveHDR.setEnabled(hdrImage != null);
        hDRPreviewbutton.setEnabled(hdrImage != null);
        saveCurve.setEnabled(curves[1] != null && samplesslider.getValue() == calculatedsample
        && lambda == calculatedlambda && textfield.getForeground() != Color.RED);
        showCurve.setEnabled(curves[1] != null && samplesslider.getValue() == calculatedsample
        && lambda == calculatedlambda && textfield.getForeground() != Color.RED);
    }
}