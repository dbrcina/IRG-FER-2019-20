package hr.fer.zemris.linearna;

public class Vector extends AbstractVector {

    private double[] elements;
    private int dimension;
    private boolean readOnly;

    public Vector(double[] elements) {
        this.elements = elements;
    }

    public Vector(double[] elements, boolean readOnly) {
        this.elements = elements;
        this.readOnly = readOnly;
    }
}
