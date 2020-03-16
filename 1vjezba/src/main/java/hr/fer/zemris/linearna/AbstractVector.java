package hr.fer.zemris.linearna;

import hr.fer.zemris.linearna.exception.IncompatibleOperandException;
import hr.fer.zemris.linearna.exception.MethodNotSupportedException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.StringJoiner;

/**
 * An implementation of {@link IVector} interface. Represents any object that is considered as a vector.
 */
public abstract class AbstractVector implements IVector {

    // helper method which checks dimensions of this and other vector
    private void checkDimension(IVector other) {
        if (getDimension() != other.getDimension()) {
            throw new IncompatibleOperandException("Dimensions are not equal");
        }
    }

    @Override
    public IVector add(IVector other) {
        checkDimension(other);
        for (int i = 0; i < getDimension(); i++) {
            set(i, get(i) + other.get(i));
        }
        return this;
    }

    @Override
    public IVector nAdd(IVector other) {
        return copy().add(other);
    }

    @Override
    public IVector sub(IVector other) {
        checkDimension(other);
        for (int i = 0; i < getDimension(); i++) {
            set(i, get(i) - other.get(i));
        }
        return this;
    }

    @Override
    public IVector nSub(IVector other) {
        return copy().sub(other);
    }

    @Override
    public IVector scalarMultiply(double scalar) {
        for (int i = 0; i < getDimension(); i++) {
            set(i, get(i) * scalar);
        }
        return this;
    }

    @Override
    public IVector nScalarMultiply(double scalar) {
        return copy().scalarMultiply(scalar);
    }

    @Override
    public double norm() {
        double norm = 0.0;
        for (int i = 0; i < getDimension(); i++) {
            norm += Math.pow(get(i), 2);
        }
        return Math.sqrt(norm);
    }

    @Override
    public IVector normalize() {
        double norm = norm();
        for (int i = 0; i < getDimension(); i++) {
            set(i, get(i) / norm);
        }
        return this;
    }

    @Override
    public IVector nNormalize() {
        return copy().normalize();
    }

    @Override
    public double cosine(IVector other) {
        checkDimension(other);
        return scalarProduct(other) / (norm() * other.norm());
    }

    @Override
    public double scalarProduct(IVector other) {
        checkDimension(other);
        double product = 0.0;
        for (int i = 0; i < getDimension(); i++) {
            product += get(i) * other.get(i);
        }
        return product;
    }

    @Override
    public IVector nVectorProduct(IVector other) {
        checkDimension(other);
        if (getDimension() != 3) {
            throw new IncompatibleOperandException("This method works only with 3-dimensional vectors");
        }
        double[] components = new double[]{
                get(1) * other.get(2) - get(2) * other.get(1),  // a2*b3 - a3*b2
                get(2) * other.get(0) - get(0) * other.get(2),  // a3*b1 - a1*b3
                get(0) * other.get(1) - get(1) * other.get(0)   // a1*b2 - a2*b1
        };
        IVector product = newInstance(3);
        for (int i = 0; i < getDimension(); i++) {
            product.set(i, components[i]);
        }
        return product;
    }

    @Override
    public IVector nFromHomogeneous() {
        if (getDimension() < 2) {
            throw new MethodNotSupportedException("Dimension of this vector is less than 2");
        }
        double dValue = get(getDimension() - 1);
        IVector result = newInstance(getDimension() - 1);
        for (int i = 0; i < result.getDimension(); i++) {
            result.set(i, get(i) / dValue);
        }
        return result;
    }

    @Override
    public IMatrix toRowMatrix(boolean liveView) {
        if (liveView) return new MatrixVectorView(this, true);
        return new Matrix(1, getDimension(), new double[][]{toArray()}, false);
    }

    @Override
    public IMatrix toColumnMatrix(boolean liveView) {
        if (liveView) return new MatrixVectorView(this, false);
        double[][] elements = new double[getDimension()][1];
        for (int i = 0; i < getDimension(); i++) {
            elements[i][0] = get(i);
        }
        return new Matrix(getDimension(), 1, elements, false);
    }

    @Override
    public double[] toArray() {
        double[] result = new double[getDimension()];
        for (int i = 0; i < result.length; i++) {
            result[i] = get(i);
        }
        return result;
    }

    @Override
    public IVector copyPart(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n needs to be >= 1");
        }
        IVector result = newInstance(n);
        for (int i = 0; i < n && i < getDimension(); i++) {
            result.set(i, get(i));
        }
        if (n > getDimension()) {
            for (int i = getDimension(); i < n; i++) {
                result.set(i, 0);
            }
        }
        return result;
    }

    @Override
    public IVector invert() {
        return scalarMultiply(-1);
    }

    @Override
    public IVector nInvert() {
        return copy().invert();
    }

    @Override
    public String toString() {
        return toString(3);
    }

    @Override
    public String toString(int decimals) {
        StringJoiner sj = new StringJoiner(", ", "(", ")");
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("0.0" + "#".repeat(decimals - 1), otherSymbols);
        for (int i = 0; i < this.getDimension(); i++) {
            sj.add(format.format(get(i)));
        }
        return sj.toString();
    }

}
