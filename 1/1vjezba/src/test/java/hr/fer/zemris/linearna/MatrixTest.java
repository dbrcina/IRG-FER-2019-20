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
    public void subMatrixTest() {
        IMatrix m1 = Matrix.parseSimple("1 2    3| 4 5 6");
    }
}
