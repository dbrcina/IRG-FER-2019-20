package hr.fer.zemris.irg.vjezba9.linearna;

public class VectorMatrixView extends AbstractVector {

    private int dimension;
    private boolean rowMatrix;
    private IMatrix realMatrix;

    public VectorMatrixView(IMatrix realMatrix) {
        this.realMatrix = realMatrix;
        if (realMatrix.getRowsCount() == 1) {
            rowMatrix = true;
            dimension = realMatrix.getColsCount();
        } else {
            dimension = realMatrix.getRowsCount();
        }
    }

    @Override
    public double get(int index) {
        return rowMatrix ? realMatrix.get(0, index) : realMatrix.get(index, 0);
    }

    @Override
    public IVector set(int index, double value) {
        if (rowMatrix) realMatrix.set(0, index, value);
        else realMatrix.set(index, 0, value);
        return this;
    }

    @Override
    public int getDimension() {
        return dimension;
    }

    @Override
    public IVector copy() {
        return new VectorMatrixView(realMatrix);
    }

    @Override
    public IVector newInstance(int n) {
        IMatrix m = new Matrix(rowMatrix ? 1 : n, rowMatrix ? n : 1);
        return new VectorMatrixView(m);
    }

}
