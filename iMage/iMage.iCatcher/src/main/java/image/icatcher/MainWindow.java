package image.icatcher;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class MainWindow extends JFrame {
    private JPanel sourcepics;

    public MainWindow() throws URISyntaxException {
        var file = getClass().getClassLoader().getResource("2019-06-05-200418.jpg");
        var layout = new GridLayout(2, 2);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("iMage.iCatcher");
        setResizable(false);
        setSize(800, 700);
        setPreferredSize(new Dimension(800, 700));
        setLayout(layout);
        sourcepics = new JPanel(new FlowLayout());
        var scrollpane = new JScrollPane(sourcepics);
        add(scrollpane);
        var pic = new ScaledImageLabel(file, false);
        pic.setPreferredSize(new Dimension(350, 250));
        sourcepics.add(pic);
        sourcepics.add(new ScaledImageLabel(file, false));
        sourcepics.add(new ScaledImageLabel(file, false));
        var sc = new ScaledImageLabel(file, true);
        add(sc);
        var box = new JComboBox<>(new String[]{ "Standard Curve", "Calculated Curve" });
        add(box);
        // add(new JButton("Test"));
        add(new JButton("Test2"));
        pack();
        // sourcepics.setSize((int)(sourcepics.getWidth() * ((double)sourcepics.getHeight() / scrollpane.getHeight())), scrollpane.getHeight());
        setVisible(true);
    }
}