package hr.fer.zemris.linearna;

public class MatrixVectorView extends AbstractMatrix {

    private IVector originalVector;
    private boolean asRowMatrix;

    public MatrixVectorView(IVector originalVector, boolean asRowMatrix) {
        this.originalVector = originalVector;
        this.asRowMatrix = asRowMatrix;
    }

    @Override
    public int getRowsCount() {
        return asRowMatrix ? 1 : originalVector.getDimension();
    }

    @Override
    public int getColsCount() {
        return asRowMatrix ? originalVector.getDimension() : 1;
    }

    @Override
    public double get(int row, int column) {
        return asRowMatrix ? originalVector.get(column) : originalVector.get(row);
    }

    @Override
    public IMatrix set(int row, int column, double value) {
        originalVector.set(asRowMatrix ? column : row, value);
        return this;
    }

    @Override
    public IMatrix copy() {
        return new MatrixVectorView(originalVector, asRowMatrix);
    }

    @Override
    public IMatrix newInstance(int rows, int columns) {
        IVector vector = new Vector(new double[originalVector.getDimension()]);
        return new MatrixVectorView(vector, asRowMatrix);
    }

}
