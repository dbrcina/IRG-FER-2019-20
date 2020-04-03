package hr.fer.zemris.linearna;

import hr.fer.zemris.linearna.exception.IncompatibleOperandException;
import hr.fer.zemris.linearna.exception.MethodNotSupportedException;
import hr.fer.zemris.linearna.exception.ParseException;
import org.junit.Test;

import static org.junit.Assert.*;

public class VectorTest {

    @Test
    public void parseSimpleTest() {
        IVector a = Vector.parseSimple("3 1 3");
        assertEquals("(3.0, 1.0, 3.0)", a.toString());
        IVector b = Vector.parseSimple("        32.5126              0.1    3   ");
        assertEquals("(32.513, 0.1, 3.0)", b.toString());
    }

    @Test
    public void parseSimpleTestFail() {
        assertThrows(ParseException.class, () -> Vector.parseSimple("bla"));
        assertThrows(ParseException.class, () -> Vector.parseSimple("123|"));
        assertThrows(ParseException.class, () -> Vector.parseSimple(""));
    }

    @Test
    public void vectorCreationTest() {
        // 1 argument constructor
        IVector a = new Vector(2, 3);
        assertEquals(2, a.get(0), 1e-9);
        double[] elements = {10, 20};
        // 3 argument constructor, elements are not copied
        IVector b = new Vector(false, false, elements);
        // 3 argument constructor, elements are copied
        IVector c = new Vector(false, true, elements);
        assertEquals(20, b.get(1), 1e-9);
        assertEquals(20, c.get(1), 1e-9);
        elements[1] = 13;
        assertEquals(13, b.get(1), 1e-9);
        assertEquals(20, c.get(1), 1e-9);
    }

    @Test
    public void vectorCreationTestFail() {
        assertThrows(IllegalArgumentException.class, () -> new Vector(null));
        assertThrows(IllegalArgumentException.class, () -> new Vector(new double[0]));
        assertThrows(IllegalArgumentException.class, () -> new Vector(false, false, new double[0]));
    }

    @Test
    public void getTest() {
        IVector a = new Vector(1, 2, 3);
        assertEquals(1, a.get(0), 1e-9);
        assertEquals(2, a.get(1), 1e-9);
        assertEquals(3, a.get(2), 1e-9);
    }

    @Test
    public void getTestFail() {
        IVector a = new Vector(1, 2, 3);
        assertThrows(IndexOutOfBoundsException.class, () -> a.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> a.get(3));
    }

    @Test
    public void setTest() {
        IVector a = new Vector(1, 2, 3);
        a.set(1, 20);
        assertEquals(20, a.get(1), 1e-9);
        a.set(2, 100);
        assertEquals(100, a.get(2), 1e-9);
    }

    @Test
    public void setTestFail() {
        IVector a = new Vector(true, false, 1, 2, 3);
        assertThrows(MethodNotSupportedException.class, () -> a.set(1, 100));
        IVector b = new Vector(1, 2, 3);
        assertThrows(IndexOutOfBoundsException.class, () -> b.set(-1, 100));
        assertThrows(IndexOutOfBoundsException.class, () -> b.set(3, 100));
    }

    @Test
    public void getDimensionTest() {
        IVector a = new Vector(1, 2, 3);
        assertEquals(3, a.getDimension());
    }

    @Test
    public void copyTest1() {
        IVector a = new Vector(true, false, 1, 2, 3);
        IVector b = a.copy();
        assertThrows(MethodNotSupportedException.class, () -> a.set(1, 100));
        assertThrows(MethodNotSupportedException.class, () -> b.set(1, 100));
    }

    @Test
    public void copyTest2() {
        IVector a = new Vector(1, 2, 3);
        IVector b = a.copy();
        assertEquals(a.get(1), b.get(1), 1e-9);
        a.set(0, 10);
        assertEquals(10, a.get(0), 1e-9);
        assertEquals(1, b.get(0), 1e-9);
    }

    @Test
    public void newInstanceTest() {
        IVector a = new Vector(1, 2, 3);
        IVector b = a.newInstance(3);
        assertArrayEquals(new double[3], b.toArray(), 1e-9);
        IVector c = b.newInstance(5);
        assertArrayEquals(new double[5], c.toArray(), 1e-9);
    }

    @Test
    public void newInstanceTestFail() {
        IVector a = new Vector(1, 2, 3);
        assertThrows(IllegalArgumentException.class, () -> a.newInstance(0));
        assertThrows(IllegalArgumentException.class, () -> a.newInstance(-1));
    }

    @Test
    public void addTest() {
        IVector a = new Vector(2, 3, -4);
        IVector b = new Vector(-1, 4, -3);
        IVector c = a.add(b);
        assertArrayEquals(new double[]{1, 7, -7}, a.toArray(), 1e-9);
        assertArrayEquals(new double[]{1, 7, -7}, c.toArray(), 1e-9);
    }

    @Test
    public void nAddTest() {
        IVector a = new Vector(2, 3, -4);
        IVector b = new Vector(-1, 4, -3);
        IVector c = a.nAdd(b);
        assertArrayEquals(new double[]{2, 3, -4}, a.toArray(), 1e-9);
        assertArrayEquals(new double[]{1, 7, -7}, c.toArray(), 1e-9);
    }

    @Test
    public void addNAddTestFail() {
        IVector a = new Vector(2, 3, -4);
        IVector b = new Vector(-1, 4);
        assertThrows(IncompatibleOperandException.class, () -> a.add(b));
        assertThrows(IncompatibleOperandException.class, () -> a.nAdd(b));
    }

    @Test
    public void subTest() {
        IVector a = new Vector(2, 3, -4);
        IVector b = new Vector(-1, 4, -3);
        IVector c = a.sub(b);
        assertArrayEquals(new double[]{3, -1, -1}, a.toArray(), 1e-9);
        assertArrayEquals(new double[]{3, -1, -1}, c.toArray(), 1e-9);
    }

    @Test
    public void nSubTest() {
        IVector a = new Vector(2, 3, -4);
        IVector b = new Vector(-1, 4, -3);
        IVector c = a.nSub(b);
        assertArrayEquals(new double[]{2, 3, -4}, a.toArray(), 1e-9);
        assertArrayEquals(new double[]{3, -1, -1}, c.toArray(), 1e-9);
    }

    @Test
    public void subNSubTestFail() {
        IVector a = new Vector(2, 3, -4);
        IVector b = new Vector(-1, 4);
        assertThrows(IncompatibleOperandException.class, () -> a.sub(b));
        assertThrows(IncompatibleOperandException.class, () -> a.nSub(b));
    }

    @Test
    public void scalarMultiplyTest() {
        IVector a = new Vector(1, 2, 3);
        IVector b = a.scalarMultiply(0.5);
        assertArrayEquals(new double[]{0.5, 1, 1.5}, a.toArray(), 1e-9);
        assertArrayEquals(new double[]{0.5, 1, 1.5}, b.toArray(), 1e-9);
    }

    @Test
    public void nScalarMultiplyTest() {
        IVector a = new Vector(1, 2, 3);
        IVector b = a.nScalarMultiply(0.5);
        assertArrayEquals(new double[]{1, 2, 3}, a.toArray(), 1e-9);
        assertArrayEquals(new double[]{0.5, 1, 1.5}, b.toArray(), 1e-9);
    }

    @Test
    public void normTest() {
        IVector a = new Vector(1, 2, 3);
        assertEquals(Math.sqrt(14), a.norm(), 1e-9);
    }

    @Test
    public void normalizeTest() {
        IVector a = new Vector(1, 2, 3);
        double norm = Math.sqrt(14);
        IVector b = a.normalize();
        assertArrayEquals(new double[]{1 / norm, 2 / norm, 3 / norm}, a.toArray(), 1e-9);
        assertArrayEquals(new double[]{1 / norm, 2 / norm, 3 / norm}, b.toArray(), 1e-9);
    }

    @Test
    public void nNormalizeTest() {
        IVector a = new Vector(1, 2, 3);
        double norm = Math.sqrt(14);
        IVector b = a.nNormalize();
        assertArrayEquals(new double[]{1, 2, 3}, a.toArray(), 1e-9);
        assertArrayEquals(new double[]{1 / norm, 2 / norm, 3 / norm}, b.toArray(), 1e-9);
    }

    @Test
    public void scalarProductTest() {
        IVector a = new Vector(1, 7, -7);
        IVector b = new Vector(-1, 4, -3);
        assertEquals(48, a.scalarProduct(b), 1e-9);
    }

    @Test
    public void scalarProductTestFail() {
        IVector a = new Vector(1, 7, -7);
        IVector b = new Vector(-1, 4);
        assertThrows(IncompatibleOperandException.class, () -> a.scalarProduct(b));
    }

    @Test
    public void cosineTest() {
        IVector a = new Vector(3, 4, 0);
        IVector b = new Vector(4, 4, 2);
        assertEquals(14.0 / 15, a.cosine(b), 1e-9);
    }

    @Test
    public void cosineTestFail() {
        IVector a = new Vector(3, 4, 0);
        IVector b = new Vector(4, 4);
        assertThrows(IncompatibleOperandException.class, () -> a.cosine(b));
    }

    @Test
    public void nVectorProduct() {
        IVector a = new Vector(2, 3, 4);
        IVector b = new Vector(2, 2, 4);
        assertArrayEquals(new double[]{4, 0, -2}, a.nVectorProduct(b).toArray(), 1e-9);
        IVector d = new Vector(1, 7, -7);
        assertArrayEquals(new double[]{42, -18, -12}, d.nVectorProduct(b).toArray(), 1e-9);
    }

    @Test
    public void nVectorProductFail() {
        IVector a = new Vector(2, 3, 4);
        IVector b = new Vector(2, 2);
        IVector c = new Vector(2, 2);
        assertThrows(IncompatibleOperandException.class, () -> a.nVectorProduct(b));
        assertThrows(IncompatibleOperandException.class, () -> c.nVectorProduct(b));
    }

    @Test
    public void fromHomogeneousTest() {
        IVector a = Vector.parseSimple("1 3     7      2");
        assertEquals("(0.5, 1.5, 3.5)", a.nFromHomogeneous().toString());
    }

    @Test
    public void fromHomogeneousTestFail() {
        IVector a = Vector.parseSimple("      2");
        assertThrows(MethodNotSupportedException.class, a::nFromHomogeneous);
    }

    @Test
    public void copyPartTest() {
        IVector a = new Vector(-2, 4, 1);
        IVector b = a.copyPart(1);
        assertEquals("(-2.0)", b.toString());
        IVector c = a.copyPart(2);
        assertEquals("(-2.0, 4.0)", c.toString());
        IVector d = a.copyPart(6);
        assertEquals("(-2.0, 4.0, 1.0, 0.0, 0.0, 0.0)", d.toString());
    }

    @Test
    public void copyPartTestFail() {
        IVector a = new Vector(-2, 4, 1);
        assertThrows(IllegalArgumentException.class, () -> a.copyPart(0));
        assertThrows(IllegalArgumentException.class, () -> a.copyPart(-1));
    }

    @Test
    public void invertTest() {
        IVector a = new Vector(1, 2, 3);
        IVector b = a.invert();
        assertArrayEquals(new double[]{-1, -2, -3}, a.toArray(), 1e-9);
        assertArrayEquals(new double[]{-1, -2, -3}, b.toArray(), 1e-9);
    }

    @Test
    public void nInvertTest() {
        IVector a = new Vector(1, 2, 3);
        IVector b = a.nInvert();
        assertArrayEquals(new double[]{1, 2, 3}, a.toArray(), 1e-9);
        assertArrayEquals(new double[]{-1, -2, -3}, b.toArray(), 1e-9);
    }

    @Test
    public void toStringTest() {
        IVector a = new Vector(1.12349, 2.458965, 5.43965734);
        assertEquals("(1.123, 2.459, 5.44)", a.toString());
        assertEquals("(1.1235, 2.459, 5.4397)", a.toString(4));
    }

    @Test
    public void toArrayTest() {
        IVector a = new Vector(1, 2, 3);
        assertArrayEquals(new double[]{1, 2, 3}, a.toArray(), 1e-9);
    }

}
