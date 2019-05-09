package org.iMage.HDrize.matrix;

import org.iMage.HDrize.base.matrix.IMatrixCalculator;

/**
 * Common Matrix Operation
 */
public class MatrixCalculator implements IMatrixCalculator<Matrix> {

  @Override
  public Matrix inverse(Matrix mtx) {
    double[][] gaussian = mtx.copy();
    double[][] inverse = new double[mtx.rows()][mtx.cols()];
    for (int i = 0; i < inverse.length; i++) {
      inverse[i][i] = 1;
    }
    for (int l = 0; l < mtx.cols(); l++) {
      for (int i = l; i < mtx.rows(); i++) {
        if (gaussian[i][l] != 0) {
          var divisor = gaussian[i][l];
          for (int j = 0; j < mtx.cols(); j++) {
            gaussian[i][j] /= divisor;
            inverse[i][j] /= divisor;
          }
          for (int j = 0; j < mtx.rows(); j++) {
            if (j != i) {
              var factor = gaussian[j][l] / gaussian[i][l];
              for (int k = 0; k < mtx.cols(); k++) {
                gaussian[j][k] -= factor * gaussian[i][k];
                inverse[j][k] -= factor * inverse[i][k];
              }
            }
          }
          break;
        }
      }
    }
    return new Matrix(inverse);
  }

  @Override
  public Matrix multiply(Matrix a, Matrix b) {
    double[][] ret = new double[a.rows()][b.cols()];
    for (int j = 0; j < a.rows(); j++) {
      for (int i = 0; i < b.cols(); i++) {
        for (int l = 0; l < a.cols(); l++) {
          ret[j][i] += a.get(j, l) * b.get(l, i);
        }
      }
    }
    return new Matrix(ret);
  }

  @Override
  public Matrix transpose(Matrix mtx) {
    double[][] ret = new double[mtx.cols()][mtx.rows()];
    for (int j = 0; j < ret.length; j++) {
      for (int i = 0; i < ret[i].length; i++) {
        ret[i][j] = mtx.get(j, i);
      }
    }
    return new Matrix(ret);
  }

}
