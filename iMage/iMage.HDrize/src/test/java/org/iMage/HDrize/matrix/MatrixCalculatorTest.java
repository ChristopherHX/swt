package org.iMage.HDrize.matrix;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * MatrixCalculatorTest
 */
public class MatrixCalculatorTest {
    private MatrixCalculator calc;

    /** 
     * Before every Test create a new Matrix Calculator instance to avoid inconsistency
     */
    @Before
    public void setUp() {
        calc = new MatrixCalculator();
    }

    /**
     * Test to multiply id with id that must be the id
     */
    @Test
    public void multiplyTest10x10() {
        double[][] id = new double[10][10];
        for (int i = 0; i < id.length; i++) {
            id[i][i] = 1;
        }
        var mtx = calc.multiply(new Matrix(id), new Matrix(id));
        assertTrue(mtx.equals(new Matrix(id), 0.125));
    }

    /**
     * Test self inversion of mirrored idendity
     */
    @Test
    public void inverseTest8x8() {
        double[][] ii = new double[8][8];
        for (int i = 0; i < ii.length; i++) {
            ii[i][7 - i] = 1;
        }
        var mtx = new Matrix(ii);
        var inv = calc.inverse(mtx);
        assertTrue("Invalid Inverse", mtx.equals(inv, 0.125));
    }

    /**
     * Test WikiPedia sample 4x4 Matrix Inversion
     */
    @Test
    public void inverseTest2x2() { 
        var mtx = new Matrix(new double[][] {
            { 2, 5 },
            { 1, 3 }
        });
        var inv = calc.inverse(mtx);
        assertTrue("Invalid Inverse", inv.equals(new Matrix(new double[][] {
            { 3, -5 },
            { -1, 2 }
        }), 0.125));
    }

    /**
     * Test if inverse of non Inversable Matrix fails
     */
    @Test(expected = IllegalArgumentException.class)
    public void inverseTest2x2NotInversable() { 
        var mtx = new Matrix(new double[][] {
            { 2, 1 },
            { 4, 2 }
        });
        var inv = calc.inverse(mtx);
    }
}