package hr.fer.zemris.irg.linearna;

import hr.fer.zemris.irg.linearna.exception.IncompatibleOperandException;
import hr.fer.zemris.irg.linearna.exception.MethodNotSupportedException;

/**
 * Provides some generic methods for working with vectors.
 */
public interface IVector {

    /**
     * @param index position.
     * @return vector's value at position <i>index</i>.
     * @throws IndexOutOfBoundsException if provided <i>index</i> is out of bounds.
     */
    double get(int index);

    /**
     * Sets provided <i>value</i> at provided <i>index</i>.
     *
     * @param index position.
     * @param value value.
     * @return modified vector.
     * @throws MethodNotSupportedException if <b>this</b> vector is read-only.
     * @throws IndexOutOfBoundsException   if provided <i>index</i> is out of bounds.
     */
    IVector set(int index, double value);

    /**
     * @return vector's dimension.
     */
    int getDimension();

    /**
     * Copies elements of <b>this</b> vector into a new one.
     *
     * @return copied vector as a result.
     */
    IVector copy();

    /**
     * Copies <i>n</i> number of components from <b>this</b> vector. If <i>n</i> is higher than vector's
     * dimension, then last <i>n-dimension</i> elements will be set to 0.
     *
     * @param n number of components.
     * @return copied vector as a result.
     * @throws IllegalArgumentException if provided <i>n</i> is less than 1.
     */
    IVector copyPart(int n);

    /**
     * Creates a new <i>n</i> dimensional null-vector.
     *
     * @param n number of components.
     * @return new null-vector.
     * @throws IllegalArgumentException if provided <i>n</i> is less than 1.
     */
    IVector newInstance(int n);

    /**
     * Adds <b>this</b> vector to <i>other</i> vector.
     *
     * @param other vector.
     * @return modified <b>this</b> vector.
     * @throws IncompatibleOperandException if <i>other</i> vector has not the same dimension as
     *                                      <b>this</b> vector.
     */
    IVector add(IVector other);

    /**
     * Adds <b>this</b> vector to <i>other</i> vector.
     *
     * @param other vector.
     * @return new vector as a result.
     * @throws IncompatibleOperandException if <i>other</i> vector has not the same dimension as
     *                                      <b>this</b> vector.
     */
    IVector nAdd(IVector other);

    /**
     * Subtracts <b>this</b> vector from <i>other</i> vector.
     *
     * @param other vector.
     * @return modified <b>this</b> vector.
     * @throws IncompatibleOperandException if <i>other</i> vector has not the same dimension as
     *                                      <b>this</b> vector.
     */
    IVector sub(IVector other);

    /**
     * Subtracts <b>this</b> vector from <i>other</i> vector.
     *
     * @param other vector.
     * @return new vector as a result.
     * @throws IncompatibleOperandException if <i>other</i> vector has not the same dimension as
     *                                      <b>this</b> vector.
     */
    IVector nSub(IVector other);

    /**
     * Performs scalar multiplication of <b>this</b> vector by provided <i>scalar</i>.
     *
     * @param scalar scalar.
     * @return modified <b>this</b> vector.
     */
    IVector scalarMultiply(double scalar);

    /**
     * Performs scalar multiplication of <b>this</b> vector by provided <i>scalar</i>.
     *
     * @param scalar scalar.
     * @return new vector as a result.
     */
    IVector nScalarMultiply(double scalar);

    /**
     * @return <b>this</b> vector's norm.
     */
    double norm();

    /**
     * Normalizes <b>this</b> vector.
     *
     * @return modified <b>this</b> vector.
     */
    IVector normalize();

    /**
     * Normalizes <b>this</b> vector.
     *
     * @return new vector as a result.
     */
    IVector nNormalize();

    /**
     * Calculates cosine angle between <b>this</b> vector and <i>other</i> vector.
     *
     * @param other vector.
     * @return cosine angle.
     * @throws IncompatibleOperandException if <i>other</i> vector has not the same dimension as
     *                                      <b>this</b> vector.
     */
    double cosine(IVector other);

    /**
     * Performs scalar product between <b>this</b> vector and <i>other</i> vector.
     *
     * @param other vector.
     * @return scalar product.
     * @throws IncompatibleOperandException if <i>other</i> vector has not the same dimension as
     *                                      <b>this</b> vector.
     */
    double scalarProduct(IVector other);

    /**
     * Performs vector product between <b>this</b> vector and <i>other</i> vector.
     *
     * @param other vector.
     * @return new vector as a result.
     * @throws IncompatibleOperandException if <i>other</i> vector has not the same dimension as
     *                                      <b>this</b> vector and if dimension is not equal to 3.
     */
    IVector nVectorProduct(IVector other);

    /**
     * Creates a new vector from homogeneous space into real space. If a vector from homogeneous space is
     * a d-dimensional vector, then its vector from a real space will be (d-1)-dimensional vector whose
     * components will be divided by d.th component.
     *
     * @return new vector as a result.
     * @throws MethodNotSupportedException if dimension of <b>this</b> vector is less than 2.
     */
    IVector nFromHomogeneous();

    /**
     * If <i>liveView</i> is true, new instance of {@link MatrixVectorView} is created.
     *
     * @param liveView live view.
     * @return representation of <b>this</b> vector as a row matrix.
     */
    IMatrix toRowMatrix(boolean liveView);

    /**
     * If <i>liveView</i> is true, new instance of {@link MatrixVectorView} is created.
     *
     * @param liveView live view.
     * @return representation of <b>this</b> vector as a column matrix.
     */
    IMatrix toColumnMatrix(boolean liveView);

    /**
     * @return <b>this</b> vector as an array of doubles.
     */
    double[] toArray();

    /**
     * Performs {@link #scalarMultiply(double)} method with -1 as an argument.
     *
     * @return modified <b>this</b> vector.
     */
    IVector invert();

    /**
     * Performs {@link #scalarMultiply(double)} method with -1 as an argument.
     *
     * @return new vector as a result.
     */
    IVector nInvert();

    /**
     * @param decimals number of decimals.
     * @return formatted string representation of <b>this</b> vector.
     */
    String toString(int decimals);

}
