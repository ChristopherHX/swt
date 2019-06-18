package image.icatcher;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.iMage.HDrize.base.ICameraCurve;

/**
 * CurvePanel
 * A Panel which painst the color curves
 */
public class CurvePanel extends JPanel {
    private static final long serialVersionUID = -3203895422559382440L;
    private float[][] responsecurve;
    private float min;
    private float max;

    /**
     * Creates an Panel to show the Curve
     * @param curve curve for graph no reference saved
     */
    public CurvePanel(ICameraCurve curve) {
        responsecurve = new float[3][255];
        min = Float.MAX_VALUE;
        max = Float.MIN_VALUE;
        for (int i = 0; i < 255; i++) {
            var response = curve.getResponse(new int[] { i, i, i });
            for (int j = 0; j < 3; j++) {
                float c = response[j];
                responsecurve[j][i] = c;
                if (c > max) {
                    max = c;
                } else if (c < min) {
                    min = c;
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        var scalex = (float) getWidth() / responsecurve[0].length;
        var scaley = (float) getHeight() / (max - min);
        int[] xcord = new int[responsecurve[0].length];
        int[][] ycord = new int[3][responsecurve[0].length];
        for (int i = 0; i < xcord.length; i++) {
            xcord[i] = (int) Math.round(i * scalex);
            for (int j = 0; j < 3; j++) {
                ycord[j][i] = (int) Math.round(responsecurve[j][i] * scaley);
            }
        }
        g.setColor(new Color(1f, 0f, 0f, 0.5f));
        g.drawPolyline(xcord, ycord[0], xcord.length);
        g.setColor(new Color(0f, 1f, 0f, 0.5f));
        g.drawPolyline(xcord, ycord[1], xcord.length);
        g.setColor(new Color(0f, 0f, 1f, 0.5f));
        g.drawPolyline(xcord, ycord[2], xcord.length);
    }
}