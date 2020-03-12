package hr.fer.zemris.linearna;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.StringJoiner;

/**
 * An implementation of {@link IVector} interface.
 */
public abstract class AbstractVector implements IVector {

    private void checkDimension(IVector other) {
        if (this.getDimension() != other.getDimension()) {
            throw new IllegalArgumentException("Dimensions are not equal");
        }
    }

    @Override
    public IVector add(IVector other) {
        checkDimension(other);
        for (int i = 0; i < this.getDimension(); i++) {
            this.set(i, this.get(i) + other.get(i));
        }
        return this;
    }

    @Override
    public IVector nAdd(IVector other) {
        return this.copy().add(other);
    }

    @Override
    public IVector sub(IVector other) {
        checkDimension(other);
        for (int i = 0; i < this.getDimension(); i++) {
            this.set(i, this.get(i) - other.get(i));
        }
        return this;
    }

    @Override
    public IVector nSub(IVector other) {
        return this.copy().sub(other);
    }

    @Override
    public IVector scalarMultiply(double scalar) {
        for (int i = 0; i < this.getDimension(); i++) {
            this.set(i, this.get(i) * scalar);
        }
        return this;
    }

    @Override
    public IVector nScalarMultiply(double scalar) {
        return this.copy().scalarMultiply(scalar);
    }

    @Override
    public double norm() {
        double norm = 0.0;
        for (int i = 0; i < this.getDimension(); i++) {
            norm += Math.pow(this.get(i), 2);
        }
        return Math.sqrt(norm);
    }

    @Override
    public IVector normalize() {
        double norm = norm();
        for (int i = 0; i < this.getDimension(); i++) {
            this.set(i, this.get(i) / norm);
        }
        return this;
    }

    @Override
    public IVector nNormalize() {
        return this.copy().normalize();
    }

    @Override
    public double cosine(IVector other) {
        checkDimension(other);
        return this.scalarProduct(other) / (this.norm() * other.norm());
    }

    @Override
    public double scalarProduct(IVector other) {
        checkDimension(other);
        double product = 0.0;
        for (int i = 0; i < this.getDimension(); i++) {
            product += this.get(i) * other.get(i);
        }
        return product;
    }

    @Override
    public IVector nVectorProduct(IVector other) {
        checkDimension(other);
        if (this.getDimension() != 3) {
            throw new IllegalArgumentException("This operation only works with 3-dimensional vectors");
        }
        double[] components = new double[]{
                this.get(1) * other.get(2) - other.get(1) * this.get(2),        // y1*z2 - y2*z1
                -1 * this.get(0) * other.get(2) + other.get(0) * this.get(2),   // -x1*z2 + x2*z1
                this.get(0) * other.get(1) - other.get(0) * this.get(1)         // x1*y2 - x2*y1
        };
        IVector product = this.newInstance(3);
        for (int i = 0; i < this.getDimension(); i++) {
            product.set(i, components[i]);
        }
        return product;
    }

    @Override
    public IVector nFromHomogeneous() {
        double dValue = this.get(this.getDimension() - 1);
        IVector result = this.newInstance(this.getDimension() - 1);
        for (int i = 0; i < result.getDimension(); i++) {
            result.set(i, this.get(i) / dValue);
        }
        return result;
    }

    @Override
    public double[] toArray() {
        double[] result = new double[this.getDimension()];
        for (int i = 0; i < result.length; i++) {
            result[i] = this.get(i);
        }
        return result;
    }

    @Override
    public IVector copyPart(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n needs to be >= 1");
        }
        IVector result = this.newInstance(n);
        for (int i = 0; i < n && i < this.getDimension(); i++) {
            result.set(i, this.get(i));
        }
        if (n > this.getDimension()) {
            for (int i = this.getDimension(); i < n; i++) {
                result.set(i, 0);
            }
        }
        return result;
    }

    @Override
    public IVector inverse() {
        return this.scalarMultiply(-1);
    }

    @Override
    public IVector nInverse() {
        return this.copy().inverse();
    }

    @Override
    public String toString() {
        return toString(3);
    }

    private String toString(int dimension) {
        StringJoiner sj = new StringJoiner(", ");
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("0.0##", otherSymbols);
        for (int i = 0; i < this.getDimension(); i++) {
            sj.add(format.format(this.get(i)));
        }
        return "(" + sj.toString() + ")";
    }

}
