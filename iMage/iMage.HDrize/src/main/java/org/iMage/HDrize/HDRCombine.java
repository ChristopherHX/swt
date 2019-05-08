package org.iMage.HDrize;

import org.iMage.HDrize.base.ICameraCurve;
import org.iMage.HDrize.base.IHDRCombine;
import org.iMage.HDrize.base.images.EnhancedImage;
import org.iMage.HDrize.base.images.HDRImage;

/**
 * Implementation of {@link IHDRCombine}.
 */
public class HDRCombine implements IHDRCombine {
  @Override
  public HDRImage createHDR(ICameraCurve curve, EnhancedImage[] imageList) {
    var r = new float[imageList[0].getHeight()][imageList[0].getWidth()];
    var g = new float[imageList[0].getHeight()][imageList[0].getWidth()];
    var b = new float[imageList[0].getHeight()][imageList[0].getWidth()];
    var weights = calculateWeights();
    for (int j = 0; j < imageList[0].getHeight(); j++) {
      for (int i = 0; i < imageList[0].getWidth(); i++) {
        var wert = new float[3];
        var norm = new float[3];
        for (int k = 0; k < imageList.length; k++) {
          var rgb = imageList[k].getRGB(i, j);
          var exposuretime = imageList[k].getExposureTime();
          var ccurve = curve.getResponse(rgb);
          for (int channel = 0; channel < 3; channel++) {
            var weight = weights[rgb[channel]];
            wert[channel] += weight * ccurve[channel] / exposuretime;
            norm[channel] += weight;
          }
        }
        r[j][i] = wert[0] / norm[0];
        g[j][i] = wert[1] / norm[1];
        b[j][i] = wert[2] / norm[2];
      }
    }
    return new HDRImage(r, g, b);
  }

  @Override
  public float[] calculateWeights() {
    float[] ret = new float[256];
    for (int i = 0; i < 20; i++) {
      ret[i] = 1 + i / 20f;
      ret[255 - i] = ret[i];
    }
    for (int i = 20; i < ret.length - 20; i++) {
      ret[i] = 2;
    }
    return ret;
  }

}
