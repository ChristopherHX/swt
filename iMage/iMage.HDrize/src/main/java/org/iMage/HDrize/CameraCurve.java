package org.iMage.HDrize;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

import org.iMage.HDrize.base.ICameraCurve;
import org.iMage.HDrize.base.images.EnhancedImage;
import org.iMage.HDrize.base.matrix.IMatrix;
import org.iMage.HDrize.base.matrix.IMatrixCalculator;
import org.iMage.HDrize.matrix.Matrix;
import org.ojalgo.matrix.decomposition.SingularValue;
import org.ojalgo.matrix.store.PrimitiveDenseStore;

public class CameraCurve implements ICameraCurve {
  private static final int CHANNELS = 3;
  private static final int SIZE = 256;

  private final EnhancedImage[] images;
  private final int samples;
  private IMatrix[] respCurves;

  private final IMatrixCalculator<Matrix> matrixCalc;

  private final Random random = new Random(42);
  private final double lambda;

  /**
   * Create a camera curve based on a saved instance.
   *
   * @param is
   *          the saved instance
   * @throws IOException
   *           iff stream is invalid
   * @throws ClassNotFoundException
   *           iff stream is invalid
   * @see #save(OutputStream)
   */
  public CameraCurve(InputStream is) throws IOException, ClassNotFoundException {
    this.matrixCalc = null;
    this.lambda = -1;
    this.samples = -1;
    this.images = null;

    this.load(is);
  }

  /**
   * Create a general camera response curve.
   */
  public CameraCurve() {
    this.respCurves = new IMatrix[CameraCurve.CHANNELS];
    for (int channel = 0; channel < CameraCurve.CHANNELS; channel++) {
      this.respCurves[channel] = new Matrix(CameraCurve.SIZE, 1);
      for (int n = 0; n < CameraCurve.SIZE; n++)
        this.respCurves[channel].set(n, 0, n / ((double) CameraCurve.SIZE));
    }

    this.lambda = -1;
    this.samples = -1;
    this.images = null;
    this.matrixCalc = null;
  }

  /**
   * Create a camera response curve based on images.
   *
   * @param images
   *          the images
   * @param samples
   *          the amount of randomly chosen sample points.
   * @param lambda
   *          the lambda value
   * @param mtxCalc
   *          the calculator for matrices
   */
  public CameraCurve(EnhancedImage[] images, int samples, double lambda,
      IMatrixCalculator<Matrix> mtxCalc) {
    this.matrixCalc = Objects.requireNonNull(mtxCalc);
    this.images = Objects.requireNonNull(images);
    this.samples = samples;
    this.lambda = lambda;
  }

  @Override
  public void calculate() {
    this.calculate(3);
  }

  private void calculate(int remainingTries) {
    if (this.respCurves != null)
      return;

    if (remainingTries <= 0) {
      this.respCurves = null;
      return;
    }

    System.out.println("Try to calculate response curves .. remaining tries: " + remainingTries);

    int imageWidth = this.images[0].getWidth();
    int imageHeight = this.images[0].getHeight();

    int[] x = new int[this.samples];
    int[] y = new int[this.samples];

    boolean[][] taken = new boolean[imageHeight][imageWidth];

    for (int n = 0; n < this.samples; n++) {
      x[n] = this.random.nextInt(imageWidth);
      y[n] = this.random.nextInt(imageHeight);
      if (taken[y[n]][x[n]]) {
        n--;
        continue;
      }
      taken[y[n]][x[n]] = true;
    }

    this.respCurves = new IMatrix[CameraCurve.CHANNELS];

    for (int channel = 0; channel < CameraCurve.CHANNELS; channel++) {
      this.calculateChannel(channel, x, y);
    }
    for (var rc : this.respCurves)
      if (rc == null) {
        this.respCurves = null;
        this.calculate(remainingTries - 1);
      }
  }

  private boolean calculateChannel(int channel, final int[] x, final int[] y) {
    System.out.println("Calculation for channel " + channel);
    IMatrix[] AB = this.initAB(channel, x, y);

    var VSU = this.calcSingular(AB[0]);

    System.out.println("Starting inverse of M[" + VSU.S.rows() + "x" + VSU.S.cols() + "]");
    long t1 = System.currentTimeMillis();
    Matrix SInverse = this.matrixCalc.inverse(VSU.S);
    long t2 = System.currentTimeMillis();
    System.out.println("InverseOfMatrix has finished in " + ((t2 - t1) / 1000F) + " seconds");

    if (SInverse == null)
      return false;

    Matrix VSInverse = this.matrixCalc.multiply(VSU.V, SInverse);
    Matrix UTranspose = this.matrixCalc.transpose(VSU.U);
    Matrix result = this.matrixCalc.multiply(VSInverse, UTranspose);
    result = this.matrixCalc.multiply(result, new Matrix(AB[1]));

    this.respCurves[channel] = result;
    System.out.println("Finished channel " + channel);
    return true;

  }

  protected SingularDecomposition calcSingular(IMatrix input) {
    var matrix = input.copy();
    var A = PrimitiveDenseStore.FACTORY.makeZero(matrix.length, matrix[0].length);
    for (int r = 0; r < A.countRows(); r++)
      for (int c = 0; c < A.countColumns(); c++)
        A.set(r, c, matrix[r][c]);

    long t1 = System.currentTimeMillis();

    SingularValue<Double> svd = SingularValue.PRIMITIVE.make(A);
    svd.compute(A);
    long t2 = System.currentTimeMillis();
    System.out.println("SingularDecomposition has finished in " + ((t2 - t1) / 1000F) + " seconds");

    var V = new Matrix(svd.getQ2().toRawCopy2D());
    var S = new Matrix(svd.getD().toRawCopy2D());
    var U = new Matrix(svd.getQ1().toRawCopy2D());

    return new SingularDecomposition(V, S, U);
  }

  private IMatrix[] initAB(int channel, final int[] x, final int[] y) {
    IMatrix A = new Matrix(//
        this.samples * this.images.length + CameraCurve.SIZE + 1, //
        CameraCurve.SIZE + this.samples //
    );
    IMatrix b = new Matrix(A.rows(), 1);

    int k = 0;
    for (int i = 0; i < this.samples; i++) {
      for (int j = 0; j < this.images.length; j++) {
        int[] clr = this.images[j].getRGB(x[i], y[i]);
        int intensity = clr[channel];

        double wij = this.getWeight(intensity);

        A.set(k, intensity, wij);
        A.set(k, CameraCurve.SIZE + i, -wij);
        b.set(k, 0, wij * Math.log(this.images[j].getExposureTime()));

        k++;
      }
    }

    A.set(k, CameraCurve.SIZE / 2, 1.0);
    k++;

    for (int i = 0; i < CameraCurve.SIZE - 2; i++) {
      A.set(k, i, this.lambda * this.getWeight(i + 1));
      A.set(k, i + 1, -2.f * this.lambda * this.getWeight(i + 1));
      A.set(k, i + 2, this.lambda * this.getWeight(i + 1));
      k++;
    }

    return new IMatrix[] { A, b };
  }

  @Override
  public boolean isCalculated() {
    return this.respCurves != null;
  }

  @Override
  public float[] getResponse(int[] color) {
    if (this.respCurves == null)
      return null;

    float[] response = new float[CameraCurve.CHANNELS];

    for (int n = 0; n < CameraCurve.CHANNELS; n++) {
      double index = this.respCurves[n].get(color[n], 0);
      response[n] = (float) Math.pow(2.0, (index));
    }
    return response;
  }

  private double getWeight(int index) {
    return (index <= 256 / 2 ? index : 256 - index) / 128.f;
  }

  @Override
  public void save(OutputStream os) throws IOException {
    if (this.respCurves == null)
      return;
    CurveWrapper[] data = new CurveWrapper[CameraCurve.CHANNELS];
    for (int i = 0; i < CameraCurve.CHANNELS; i++)
      data[i] = new CurveWrapper(this.respCurves[i]);
    ObjectOutputStream oos = new ObjectOutputStream(os);
    oos.writeObject(data);
    oos.flush();
  }

  private void load(InputStream is) throws ClassNotFoundException, IOException {
    if (this.respCurves != null)
      return;
    ObjectInputStream ois = new ObjectInputStream(is);
    CurveWrapper[] data = (CurveWrapper[]) ois.readObject();

    this.respCurves = new IMatrix[CameraCurve.CHANNELS];

    for (int i = 0; i < CameraCurve.CHANNELS; i++) {
      this.respCurves[i] = new Matrix(data[i].data);
    }

  }

  /**
   * The Singular Decomposition.
   *
   * @see https://de.wikipedia.org/wiki/Singul%C3%A4rwertzerlegung
   */
  protected static final class SingularDecomposition {

    private final Matrix V;
    private final Matrix S;
    private final Matrix U;

    private SingularDecomposition(Matrix v, Matrix s, Matrix u) {
      this.V = v;
      this.S = s;
      this.U = u;
    }

  }

  private static class CurveWrapper implements Serializable {
    private static final long serialVersionUID = 3600716880375465583L;
    private double[][] data;

    private CurveWrapper(IMatrix mtx) {
      this.data = mtx.copy();
    }
  }

}
