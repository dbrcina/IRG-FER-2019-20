package hr.fer.zemris.irg.vjezba9.linearna;

import hr.fer.zemris.irg.vjezba9.linearna.exception.MethodNotSupportedException;
import hr.fer.zemris.irg.vjezba9.linearna.exception.ParseException;

import java.util.Arrays;
import java.util.Objects;

/**
 * An implementation of {@link AbstractVector}.
 */
public class Vector extends AbstractVector {

    private double[] elements;
    private int dimension;
    private boolean readOnly;

    /**
     * Constructor. Expects at least one element.
     *
     * @param elements elements.
     */
    public Vector(double... elements) {
        this(false, false, elements);
    }

    /**
     * Constructor. Expects at least one element. <i>copyElements</i> flag indicates whether provided
     * <i>elements</i> need to be copied or can be used with given reference.
     *
     * @param readOnly     read only flag.
     * @param copyElements copy elements flag.
     * @param elements     elements.
     */
    public Vector(boolean readOnly, boolean copyElements, double... elements) {
        if (elements == null || elements.length < 1) {
            throw new IllegalArgumentException("Vector needs to be at least 1-dimensional.");
        }
        this.elements = copyElements ? Arrays.copyOf(elements, elements.length) : elements;
        this.dimension = elements.length;
        this.readOnly = readOnly;
    }

    @Override
    public double get(int index) {
        Objects.checkIndex(index, dimension);
        return elements[index];
    }

    @Override
    public IVector set(int index, double value) {
        if (readOnly) {
            throw new MethodNotSupportedException("Vector is read-only.");
        }
        Objects.checkIndex(index, dimension);
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
        if (n < 1) {
            throw new IllegalArgumentException("Dimension must be >= 1");
        }
        return new Vector(new double[n]);
    }

    /**
     * Parses provided vector definition <i>s</i> into a real vector object. Method expects some text
     * where numbers are separated by at least one space.
     *
     * @param s vector definition.
     * @return new vector as a result.
     * @throws ParseException if some error occurs while parsing provided string.
     */
    public static Vector parseSimple(String s) {
        try {
            double[] elements = Arrays.stream(s.trim().split("\\s+"))
                    .mapToDouble(Double::parseDouble)
                    .toArray();
            return new Vector(elements);
        } catch (Exception e) {
            throw new ParseException("Parsing failed");
        }
    }

}
