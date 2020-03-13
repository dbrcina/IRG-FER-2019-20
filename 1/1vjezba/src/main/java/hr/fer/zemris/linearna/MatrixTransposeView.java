package hr.fer.zemris.linearna;

public class MatrixTransposeView extends AbstractMatrix {

    private IMatrix realMatrix;

    public MatrixTransposeView(IMatrix realMatrix) {
        this.realMatrix = realMatrix;
    }

    @Override
    public int getRowsCount() {
        return realMatrix.getColsCount();
    }

    @Override
    public int getColsCount() {
        return realMatrix.getRowsCount();
    }

    @Override
    public double get(int row, int column) {
        return realMatrix.get(column, row);
    }

    @Override
    public IMatrix set(int row, int column, double value) {
        return realMatrix.set(column, row, value);
    }

    @Override
    public IMatrix copy() {
        return realMatrix.copy();
    }

    @Override
    public IMatrix newInstance(int rows, int columns) {
        return realMatrix.newInstance(rows, columns);
    }

    @Override
    public double[][] toArray() {
        double[][] result = new double[this.getRowsCount()][this.getColsCount()];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = this.get(i, j);
            }
        }
        return result;
    }

}
