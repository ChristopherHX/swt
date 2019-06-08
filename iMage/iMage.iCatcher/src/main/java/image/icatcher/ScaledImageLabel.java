package image.icatcher;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * ScaledImageIcon
 */
public class ScaledImageLabel extends JLabel {
    Image orig;
    ImageIcon ico;
    double scale = 1;
    boolean fit;

    public ScaledImageLabel(ImageIcon ico) {
        super(ico);
        this.ico = ico;
    }

    public ScaledImageLabel(URL pic, boolean fit) {
        this(new ImageIcon(pic));
        orig = ico.getImage();
        if(fit) {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
        this.fit = fit;
    }

    // @Override
    // public void setSize(int w, int h) {
    private void scale(int w, int h) {
        double scaledh = (double)h / orig.getHeight(null);
        if(scale != scaledh) {
            var width = (int)(orig.getWidth(null) * scaledh);
            var height = h;
            if(fit && width > w) {
                width = w;
                double scaledw = (double)w / orig.getWidth(null);
                height = (int)(orig.getHeight(null) * scaledw);                
            }
            ico.setImage(orig.getScaledInstance(width, height, 0));
            scale = scaledh;
            // super.setSize(w, h);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        scale(getWidth(), getHeight());
        // setSize(getWidth(), getHeight());
    }
}