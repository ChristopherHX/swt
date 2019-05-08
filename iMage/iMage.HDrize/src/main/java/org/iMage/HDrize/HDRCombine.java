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
    throw new UnsupportedOperationException("TODO Implement me!");
  }

}
