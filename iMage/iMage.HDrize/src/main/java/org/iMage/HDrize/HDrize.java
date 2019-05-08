package org.iMage.HDrize;

import java.awt.image.BufferedImage;
import java.util.Objects;

import org.iMage.HDrize.base.ICameraCurve;
import org.iMage.HDrize.base.IHDrize;
import org.iMage.HDrize.base.images.EnhancedImage;
import org.iMage.HDrize.base.images.HDRImage;
import org.iMage.HDrize.base.images.HDRImageIO;
import org.iMage.HDrize.base.images.HDRImageIO.ToneMapping;
import org.iMage.HDrize.base.matrix.IMatrixCalculator;
import org.iMage.HDrize.matrix.Matrix;

/**
 * Implementation of {@link IHDrize}.
 */
public class HDrize implements IHDrize<Matrix> {
  private final HDRCombine combine;

  /**
   * Create {@link HDrize} .
   */
  public HDrize() {
    this.combine = new HDRCombine();
  }

  @Override
  public HDRImage createHDR(EnhancedImage[] images, ICameraCurve curve) {
    return this.combine.createHDR(//
        Objects.requireNonNull(curve, "curve cannot be null"),
        Objects.requireNonNull(images, "images cannot be null") //
    );
  }

  @Override
  public HDRImage createHDR(EnhancedImage[] images, int samples, double lambda,
      IMatrixCalculator<Matrix> mtxCalc) {

    var curve = this.createCurve( //
        Objects.requireNonNull(images, "images cannot be null"), //
        samples, lambda, //
        Objects.requireNonNull(mtxCalc, "mtxCalc cannot be null") //
    );

    if (curve == null)
      return null;

    return this.combine.createHDR(curve, images);
  }

  @Override
  public BufferedImage createRGB(EnhancedImage[] enhancedImages, ICameraCurve curve,
      ToneMapping mapping) {
    return HDRImageIO.createRGB(//
        this.createHDR(enhancedImages, curve),
        Objects.requireNonNull(mapping, "mapping cannot be null")//
    );
  }

  @Override
  public BufferedImage createRGB(EnhancedImage[] enhancedImages, int samples, double lambda,
      IMatrixCalculator<Matrix> mtxCalc, ToneMapping mapping) {

    return HDRImageIO.createRGB(//
        this.createHDR(enhancedImages, samples, lambda, mtxCalc), //
        Objects.requireNonNull(mapping, "mapping cannot be null") //
    );
  }

  private ICameraCurve createCurve(EnhancedImage[] images, int samples, double lambda,
      IMatrixCalculator<Matrix> mtxCalc) {
    if (lambda <= 0 || lambda > 100)
      throw new IllegalArgumentException("Lambda has to be in (0,100]");
    if (samples < 1 || samples > 1000)
      throw new IllegalArgumentException("samples has to be in [1,1000]");

    CameraCurve cc = new CameraCurve(images, samples, lambda, mtxCalc);
    cc.calculate();
    return cc.isCalculated() ? cc : null;
  }
}
