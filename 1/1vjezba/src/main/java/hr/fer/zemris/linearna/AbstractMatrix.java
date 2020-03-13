package hr.fer.zemris.linearna;

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
        for (int k = 0; k < this.getRowsCount(); k++) {
            for (int i = 0; i < this.getRowsCount(); i++) {
                double sum = 0.0;
                for (int j = 0; j < other.getColsCount(); j++) {
                    sum += this.get(i, j) * other.get(j, i);
                }
                result.set(k, i, sum);
            }
        }
        return result;
    }

    @Override
    public double determinant() {
        return 0;
    }

    @Override
    public IMatrix subMatrix(int row, int column, boolean liveView) {
        return null;
    }

    @Override
    public IMatrix nInvert() {
        return null;
    }

    @Override
    public double[][] toArray() {
        return new double[0][];
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
                sj.add(format.format(get(i, j)));
            }
            result.append(sj.toString());
        }
        return result.toString();
    }
}
