package org.iMage.HDrize.matrix;

import org.iMage.HDrize.base.matrix.IMatrix;

public final class Matrix implements IMatrix {

  /**
   * Create a new matrix.
   *
   * @param mtx
   *          the original matrix
   */
  public Matrix(IMatrix mtx) {
    throw new UnsupportedOperationException("TODO Implement me!");
  }

  /**
   * Create a new matrix.
   *
   * @param mtx
   *          the original matrix mtx[Rows][Cols]
   */
  public Matrix(double[][] mtx) {
    throw new UnsupportedOperationException("TODO Implement me!");
  }

  /**
   * Create a matrix (only zeros).
   *
   * @param rows
   *          the amount of rows
   * @param cols
   *          the amount of columns
   */
  public Matrix(int rows, int cols) {
    throw new UnsupportedOperationException("TODO Implement me!");
  }

  @Override
  public double[][] copy() {
    throw new UnsupportedOperationException("TODO Implement me!");
  }

  @Override
  public int rows() {
    throw new UnsupportedOperationException("TODO Implement me!");
  }

  @Override
  public int cols() {
    throw new UnsupportedOperationException("TODO Implement me!");
  }

  @Override
  public void set(int r, int c, double v) {
    throw new UnsupportedOperationException("TODO Implement me!");
  }

  @Override
  public double get(int r, int c) {
    throw new UnsupportedOperationException("TODO Implement me!");
  }

}
