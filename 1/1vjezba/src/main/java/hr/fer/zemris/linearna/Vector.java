package hr.fer.zemris.linearna;

import java.util.Arrays;

/**
 * An implementation of {@link AbstractVector}.
 */
public class Vector extends AbstractVector {

    private double[] elements;
    private int dimension;
    private boolean readOnly;

    public Vector(double... elements) {
        this(false, false, elements);
    }

    public Vector(boolean readOnly, boolean copyElements, double... elements) {
        this.readOnly = readOnly;
        if (copyElements) {
            copyElements(elements);
        } else {
            this.elements = elements;
        }
        updateDimension();
    }

    private void updateDimension() {
        dimension = elements.length;
    }

    private void copyElements(double[] elements) {
        this.elements = Arrays.copyOf(elements, elements.length);
    }

    private void checkIndex(int index) {
        if (index < 0 || index > dimension - 1) {
            throw new IllegalArgumentException("Index is out of bounds.");
        }
    }

    @Override
    public double get(int index) {
        checkIndex(index);
        return elements[index];
    }

    @Override
    public IVector set(int index, double value) {
        if (readOnly) {
            throw new IllegalArgumentException("Vector is read-only.");
        }
        checkIndex(index);
        elements[index] = value;
        return this;
    }

    @Override
    public int getDimension() {
        return dimension;
    }

    @Override
    public IVector copy() {
        return new Vector(readOnly, true, elements);
    }

    @Override
    public IVector newInstance(int n) {
        return new Vector(new double[n]);
    }

    /**
     * Parses provided vector definition <i>s</i> into a real vector object.
     *
     * @param s vector definition.
     * @return new vector as a result.
     */
    public static Vector parseSimple(String s) {
        s = s.trim();
        return new Vector(Arrays.stream(s.split("\\s+")).mapToDouble(Double::parseDouble).toArray());
    }

}
