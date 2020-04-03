package hr.fer.zemris.irg.ciklus2.structure;

import java.util.Objects;

/**
 * Represents a 2D edge of a polygon whose coefficients a, b and c are integers.
 */
public class IEdge2D {

    private int a;
    private int b;
    private int c;

    public IEdge2D(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public int getC() {
        return c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IEdge2D)) return false;
        IEdge2D iEdge2D = (IEdge2D) o;
        return a == iEdge2D.a &&
                b == iEdge2D.b &&
                c == iEdge2D.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }

}
