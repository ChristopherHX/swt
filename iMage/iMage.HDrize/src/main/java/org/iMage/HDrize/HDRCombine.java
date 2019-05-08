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
    throw new UnsupportedOperationException("TODO Implement me!");
  }

  @Override
  public float[] calculateWeights() {
    float[] ret = new float[256];
    for (int i = 0; i < 20; i++) {
      ret[i] = ret[255 - i] = 1 + i / 20f;
    }
    for (int i = 20; i < ret.length - 20; i++) {
      ret[i] = 2;
    }
    return ret;
  }

}
