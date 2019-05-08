package org.iMage.HDrize.matrix;

import org.iMage.HDrize.base.matrix.IMatrix;

public final class Matrix implements IMatrix {

  private double[][] storage;
  
  /**
   * Create a new matrix.
   *
   * @param mtx
   *          the original matrix
   */
  public Matrix(IMatrix mtx) {
    storage = mtx.copy();
  }

  /**
   * Create a new matrix.
   *
   * @param mtx
   *          the original matrix mtx[Rows][Cols]
   */
  public Matrix(double[][] mtx) {
    storage = Copy(mtx);
  }

  private static double[][] Copy(double[][] mtx) {
    double[][] ret = new double[mtx.length][mtx[0].length];
    for (int j = 0; j < ret.length; j++) {
      for (int i = 0; i < ret[0].length; i++) {
        ret[j][i] = mtx[j][i];
      }
    }
    return ret;
  }

  /**
   * Create a matrix (only zeros).
   *
   * @param rows the amount of rows
   * @param cols the amount of columns
   */
  public Matrix(int rows, int cols) {
    throw new UnsupportedOperationException("TODO Implement me!");
  }

  @Override
  public double[][] copy() {
    return Copy(storage);
  }

  @Override
  public int rows() {
    return storage.length;
  }

  @Override
  public int cols() {
    return storage[0].length;
  }

  @Override
  public void set(int r, int c, double v) {
    storage[r][c] = v;
  }

  @Override
  public double get(int r, int c) {
    return storage[r][c];
  }

}
