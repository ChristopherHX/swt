package org.iMage.HDrize.matrix;

import org.iMage.HDrize.base.matrix.IMatrix;

/**
 * Matrix of double stored as array
 */
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
    storage = copy(mtx);
  }

  private static double[][] copy(double[][] mtx) {
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
    storage = new double[rows][cols];
  }

  @Override
  public double[][] copy() {
    return copy(storage);
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

  /**
   * Comoares equality of two Matrices
   * @param mtx matrix to compare with
   * @param error allowed inacuracy of double
   * @return if mtx is equal with this
   */
  public boolean equals(IMatrix mtx, double error) {
    if (mtx.rows() != storage.length || mtx.cols() != storage[0].length) {
      return false;
    }
    for (int j = 0; j < mtx.rows(); j++) {
      for (int i = 0; i < mtx.cols(); i++) {
        if (Math.abs(storage[j][i] - mtx.get(j, i)) > error) {
          return false;
        }
      }
    }
    return true;
  }

}
