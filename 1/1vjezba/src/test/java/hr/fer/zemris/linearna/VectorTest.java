package hr.fer.zemris.linearna;

import org.junit.Assert;
import org.junit.Test;

public class VectorTest {

    @Test
    public void testParseSimple() {
        IVector a = Vector.parseSimple("3 1 3");
        Assert.assertEquals("(3.0, 1.0, 3.0)", a.toString());
        IVector b = Vector.parseSimple("        32.5126              0.1    3");
        Assert.assertEquals("(32.513, 0.1, 3.0)", b.toString());
    }

    @Test
    public void testCopyPart() {
        IVector a = new Vector(-2, 4, 1);
        Assert.assertThrows(IllegalArgumentException.class, () -> a.copyPart(0));
        Assert.assertThrows(IllegalArgumentException.class, () -> a.copyPart(-1));
        IVector b = a.copyPart(1);
        Assert.assertEquals("(-2.0)", b.toString());
        IVector c = a.copyPart(2);
        Assert.assertEquals("(-2.0, 4.0)", c.toString());
        IVector d = a.copyPart(6);
        Assert.assertEquals("(-2.0, 4.0, 1.0, 0.0, 0.0, 0.0)", d.toString());
    }

    @Test
    public void testFromHomogeneous() {
        IVector a = Vector.parseSimple("1 3     7      2");
        Assert.assertEquals("(0.5, 1.5, 3.5)", a.nFromHomogeneous().toString());
    }
}
