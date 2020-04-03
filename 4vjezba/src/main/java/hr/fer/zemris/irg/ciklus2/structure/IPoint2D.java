package hr.fer.zemris.irg.ciklus2.structure;

import java.util.Objects;

/**
 * Represents a 2D point where x and y coordinates are integers.
 */
public class IPoint2D {

    private int x;
    private int y;

    public IPoint2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IPoint2D)) return false;
        IPoint2D iPoint2D = (IPoint2D) o;
        return x == iPoint2D.x &&
                y == iPoint2D.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}
