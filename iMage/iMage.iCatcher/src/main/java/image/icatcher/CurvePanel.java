package image.icatcher;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import org.iMage.HDrize.base.ICameraCurve;

/**
 * CurvePanel
 */
public class CurvePanel extends JPanel {
    private ICameraCurve curve;

    public CurvePanel(ICameraCurve curve) {
        this.curve = curve;
        setPreferredSize(new Dimension(280, 255));
    }

    @Override
    protected void paintComponent(Graphics g) {
        var width = getWidth();
        var height = getHeight();
        final int scale = 100;
        int ox0 = 0, oy0 = 0;
        int ox1 = 0, oy1 = 0;
        int ox2 = 0, oy2 = 0;
        // ((Graphics2D)g).setStroke(new BasicStroke(3));
        for (int i = 0; i < 255; i++) {
            var response = curve.getResponse(new int[] { i, i, i });
            g.setColor(Color.RED);
            if (i != 0) {
                g.drawLine(ox0, oy0, (int) (response[0] * scale), i);
            }
            //g.drawArc((int) (response[0] * scale), i, 10, 10, 0, 360);
            ox0 = (int) (response[0] * scale);
            oy0 = i;
            g.setColor(Color.GREEN);
            if (i != 0) {
                g.drawLine(ox1, oy1, (int) (response[1] * scale), i);
            }
            //g.drawArc((int) (response[1] * scale), i, 10, 10, 0, 360);
            ox1 = (int) (response[1] * scale);
            oy1 = i;
            g.setColor(Color.BLUE);
            if (i != 0) {
                g.drawLine(ox2, oy2, (int) (response[2] * scale), i);
            }
            //g.drawArc((int) (response[2] * scale), i, 10, 10, 0, 360);
            ox2 = (int) (response[2] * scale);
            oy2 = i;
        }
    }
}