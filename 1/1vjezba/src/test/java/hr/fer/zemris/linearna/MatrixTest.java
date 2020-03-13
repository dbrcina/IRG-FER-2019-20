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

}
