package hr.fer.zemris.irg.linearna;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.StringJoiner;

public abstract class AbstractMatrix implements IMatrix {

    @Override
    public IMatrix nTranspose(boolean liveView) {
        if (liveView) return new MatrixTransposeView(this);
        IMatrix transposed = this.newInstance(this.getColsCount(), this.getRowsCount());
        for (int i = 0; i < transposed.getRowsCount(); i++) {
            for (int j = 0; j < transposed.getColsCount(); j++) {
                transposed.set(i, j, this.get(j, i));
            }
        }
        return transposed;
    }

    @Override
    public IMatrix add(IMatrix other) {
        for (int i = 0; i < this.getRowsCount(); i++) {
            for (int j = 0; j < this.getColsCount(); j++) {
                this.set(i, j, this.get(i, j) + other.get(i, j));
            }
        }
        return this;
    }

    @Override
    public IMatrix nAdd(IMatrix other) {
        return this.copy().add(other);
    }

    @Override
    public IMatrix sub(IMatrix other) {
        for (int i = 0; i < this.getRowsCount(); i++) {
            for (int j = 0; j < this.getColsCount(); j++) {
                this.set(i, j, this.get(i, j) - other.get(i, j));
            }
        }
        return this;
    }

    @Override
    public IMatrix nSub(IMatrix other) {
        return this.copy().sub(other);
    }

    @Override
    public IMatrix nMultiply(IMatrix other) {
        IMatrix result = this.newInstance(this.getRowsCount(), other.getColsCount());
        for (int i = 0; i < result.getRowsCount(); i++) {
            for (int j = 0; j < result.getColsCount(); j++) {
                double sum = 0.0;
                for (int k = 0; k < other.getRowsCount(); k++) {
                    sum += this.get(i, k) * other.get(k, j);
                }
                result.set(i, j, sum);
            }
        }
        return result;
    }

    @Override
    public double determinant() {
        if (getRowsCount() != getColsCount()) {
            throw new RuntimeException("Matrix is not square matrix");
        }
        if (getRowsCount() == 1) return get(0, 0);
        int sign = 1;
        double result = 0.0;
        for (int i = 0; i < getRowsCount(); i++) {
            double minor = subMatrix(0, i, false).determinant();
            double cofactor = sign * minor;
            result += get(0, i) * cofactor;
            sign *= -1;
        }
        return result;
    }

    @Override
    public IMatrix subMatrix(int row, int column, boolean liveView) {
        if (liveView) return new MatrixSubMatrixView(this, row, column);
        double[][] elements = new double[getRowsCount() - 1][getColsCount() - 1];
        for (int i = 0, r = 0; i < getRowsCount(); i++) {
            if (i == row) continue;
            for (int j = 0, c = 0; j < getColsCount(); j++) {
                if (j == column) continue;
                elements[r][c++] = get(i, j);
            }
            r++;
        }
        return new Matrix(elements.length, elements[0].length, elements, false);
    }

    @Override
    public IMatrix nInvert() {
        double determinant = determinant();
        if (determinant == 0) throw new RuntimeException("Inverse doesn't exist");
        IMatrix adJoint = findAdJointMatrix();
        return adJoint.scalarMultiply(1 / determinant);
    }

    private IMatrix findAdJointMatrix() {
        if (getRowsCount() == 1) {
            return this;
        }
        IMatrix result = newInstance(getRowsCount(), getColsCount());
        for (int i = 0; i < getRowsCount(); i++) {
            for (int j = 0; j < getColsCount(); j++) {
                double minor = subMatrix(i, j, false).determinant();
                int sign = (i + j) % 2 == 0 ? 1 : -1;
                double cofactor = sign * minor;
                result.set(i, j, cofactor);
            }
        }
        return result.nTranspose(false);
    }

    @Override
    public IMatrix scalarMultiply(double scalar) {
        for (int i = 0; i < getRowsCount(); i++) {
            for (int j = 0; j < getColsCount(); j++) {
                set(i, j, get(i, j) * scalar);
            }
        }
        return this;
    }

    @Override
    public IMatrix nScalarMultiply(double scalar) {
        return copy().scalarMultiply(scalar);
    }

    @Override
    public double[][] toArray() {
        double[][] elements = new double[getRowsCount()][getColsCount()];
        for (int i = 0; i < getRowsCount(); i++) {
            for (int j = 0; j < getColsCount(); j++) {
                elements[i][j] = get(i, j);
            }
        }
        return elements;
    }

    @Override
    public IVector toVector(boolean liveView) {
        if (getRowsCount() != 1 && getColsCount() != 1) {
            throw new RuntimeException("Cannot convert to vector because of dimensions");
        }
        if (liveView) return new VectorMatrixView(this);
        boolean rowMatrix = getRowsCount() == 1;
        double[][] elements = toArray();
        double[] results = new double[rowMatrix ? getColsCount() : getRowsCount()];
        for (int i = 0, index = 0; i < getRowsCount(); i++) {
            for (int j = 0; j < getColsCount(); j++) {
                results[index++] = elements[i][j];
            }
        }
        return new Vector(results);
    }

    @Override
    public String toString() {
        return toString(3);
    }

    private String toString(int decimals) {
        StringBuilder result = new StringBuilder();
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("0.0" + "#".repeat(decimals - 1), otherSymbols);
        for (int i = 0; i < getRowsCount(); i++) {
            StringJoiner sj = new StringJoiner(", ", "[", "]\n");
            for (int j = 0; j < getColsCount(); j++) {
                sj.add(String.format("%1$4s", format.format(get(i, j))));
            }
            result.append(sj.toString());
        }
        return result.toString();
    }

}
