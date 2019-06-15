package org.iMage.iCatcher;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.iMage.HDrize.base.ICameraCurve;

public class ShowCurve extends JPanel {
	private ICameraCurve curve;

	public ShowCurve(ICameraCurve curve) {
		if (curve == null) {
			throw new IllegalArgumentException("STOP");
		}
		this.curve = curve;
	}

	@Override
	protected void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		final int scaleFloat = 100;
		int[] old = { 0, 0, 0, 0, 0, 0 };
		for (int i = 0; i < 255; i++) {
			float[] response = curve.getResponse(new int[] { i, i, i });
			g.setColor(Color.RED);
			if (i != 0) {
				g.drawLine(old[0], old[1], (int) (response[0] * scaleFloat), i);
			}
			old[0] = (int) (response[0] * scaleFloat);
			old[1] = i;
			g.setColor(Color.GREEN);
			if (i != 0) {
				g.drawLine(old[2], old[3], (int) (response[1] * scaleFloat), i);
			}
			old[2] = (int) (response[1] * scaleFloat);
			old[3] = i;
			g.setColor(Color.BLUE);
			if (i != 0) {
				g.drawLine(old[4], old[5], (int) (response[2] * scaleFloat), i);
			}
			old[4] = (int) (response[2] * scaleFloat);
			old[5] = i;

		}
	}
}
