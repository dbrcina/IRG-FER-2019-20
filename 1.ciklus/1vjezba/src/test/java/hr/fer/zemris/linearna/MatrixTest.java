package hr.fer.zemris.linearna;

import org.junit.Assert;
import org.junit.Test;

public class MatrixTest {

    @Test
    public void parseSimpleTest() {
        IMatrix m1 = Matrix.parseSimple("1 2    3| 4 5 6");
        Assert.assertEquals("[1.0, 2.0, 3.0]\n[4.0, 5.0, 6.0]\n", m1.toString());
    }

    @Test
    public void subMatrixLiveViewTest() {
        IMatrix m1 = Matrix.parseSimple("1 2    3| 4 5 6");
        Assert.assertEquals("[4.0, 5.0]\n", m1.subMatrix(0, 2, true).toString());

        IMatrix m2 = Matrix.parseSimple("1 2 3 | 4 5 6 | 7 8 9");
        Assert.assertEquals("[4.0, 6.0]\n[7.0, 9.0]\n", m2.subMatrix(0, 1, true).toString());
        Assert.assertEquals("[1.0, 2.0]\n[4.0, 5.0]\n", m2.subMatrix(2, 2, true).toString());

        IMatrix m3 = m2.subMatrix(2, 2, true);
        m2.set(0, 0, 2);
        m3.set(0, 1, 5);
        Assert.assertEquals("[2.0, 5.0, 3.0]\n[4.0, 5.0, 6.0]\n[7.0, 8.0, 9.0]\n", m2.toString());
        Assert.assertEquals("[2.0, 5.0]\n[4.0, 5.0]\n", m3.toString());
    }

    @Test
    public void subMatrixTest() {
        IMatrix m = Matrix.parseSimple("1 2 3 | 4 5 6 | 7 8 9");
        Assert.assertEquals("[4.0, 6.0]\n[7.0, 9.0]\n", m.subMatrix(0, 1, false).toString());
        Assert.assertEquals("[1.0, 2.0]\n[4.0, 5.0]\n", m.subMatrix(2, 2, false).toString());

        IMatrix m2 = m.subMatrix(2, 2, false);
        m2.set(0, 0, 2);
        Assert.assertEquals("[1.0, 2.0, 3.0]\n[4.0, 5.0, 6.0]\n[7.0, 8.0, 9.0]\n", m.toString());
        Assert.assertEquals("[2.0, 2.0]\n[4.0, 5.0]\n", m2.toString());
    }

    @Test
    public void nMultiplyTest() {
        IMatrix m1 = Matrix.parseSimple("1 2 3 | 4 5 6");
        IMatrix m2 = Matrix.parseSimple("7 8 | 9 10 | 11 12");
        IMatrix m1m2 = m1.nMultiply(m2);
        Assert.assertEquals("[58.0, 64.0]\n[139.0, 154.0]\n", m1m2.toString());
    }

    @Test
    public void determinantTest() {
        IMatrix m1 = Matrix.parseSimple("3 8 | 4 6");
        Assert.assertEquals(-14, m1.determinant(), 1e-9);

        IMatrix m2 = Matrix.parseSimple("6 1 1 | 4 -2 5 | 2 8 7");
        Assert.assertEquals(-306, m2.determinant(), 1e-9);

        IMatrix m3 = Matrix.parseSimple("1 3 5 9 | 1 3 1 7 | 4 3 9 7 | 5 2 0 9");
        Assert.assertEquals(-376, m3.determinant(), 1e-9);
    }

    @Test
    public void nInvertTest() {
        IMatrix m1 = Matrix.parseSimple("4 7 | 2 6");
        Assert.assertEquals("[0.6, -0.7]\n[-0.2, 0.4]\n", m1.nInvert().toString());

        IMatrix m2 = Matrix.parseSimple("1 2 3 | 4 5 6 | 7 2 9");
        Assert.assertEquals(
                "[-0.917, 0.333, 0.083]\n[-0.167, 0.333, -0.167]\n[0.75, -0.333, 0.083]\n",
                m2.nInvert().toString()
        );

        IMatrix m3 = Matrix.parseSimple("1 3 5 9 | 1 3 1 7 | 4 3   9 7 | 5 2 0 9");
        Assert.assertEquals("[-0.277, 0.043, 0.149, 0.128]\n[-0.625, 0.875, 0.25, -0.25]\n" +
                        "[0.104, -0.141, 0.069, -0.048]\n[0.293, -0.218, -0.138, 0.096]\n",
                m3.nInvert().toString()
        );
    }

    @Test
    public void toVectorTest() {
        IMatrix m1 = Matrix.parseSimple("1 2 3");
        IVector v1 = m1.toVector(false);
        Assert.assertEquals("(1.0, 2.0, 3.0)", v1.toString());

        IMatrix m2 = Matrix.parseSimple("1|2|3");
        IVector v2 = m2.toVector(false);
        Assert.assertEquals("(1.0, 2.0, 3.0)", v2.toString());
    }

}
