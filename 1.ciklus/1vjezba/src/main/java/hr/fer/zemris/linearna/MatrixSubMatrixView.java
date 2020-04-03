package hr.fer.zemris.linearna;

public class MatrixSubMatrixView extends AbstractMatrix {

    private IMatrix realMatrix;
    private int[] rowIndexes;
    private int[] colIndexes;

    public MatrixSubMatrixView(IMatrix realMatrix, int row, int column) {
        this(
                realMatrix,
                filterIndexes(row, realMatrix.getRowsCount()),
                filterIndexes(column, realMatrix.getColsCount())
        );
    }

    private static int[] filterIndexes(int index, int max) {
        int[] result = new int[max - 1];
        for (int i = 0, j = 0; i < max; i++) {
            if (i != index) result[j++] = i;
        }
        return result;
    }

    private MatrixSubMatrixView(IMatrix realMatrix, int[] rowIndexes, int[] colIndexes) {
        this.realMatrix = realMatrix;
        this.rowIndexes = rowIndexes;
        this.colIndexes = colIndexes;
    }

    @Override
    public int getRowsCount() {
        return rowIndexes.length;
    }

    @Override
    public int getColsCount() {
        return colIndexes.length;
    }

    @Override
    public double get(int row, int column) {
        return realMatrix.get(rowIndexes[row], colIndexes[column]);
    }

    @Override
    public IMatrix set(int row, int column, double value) {
        return realMatrix.set(rowIndexes[row], colIndexes[column], value);
    }

    @Override
    public IMatrix copy() {
        return new MatrixSubMatrixView(realMatrix, rowIndexes, colIndexes);
    }

    @Override
    public IMatrix newInstance(int rows, int columns) {
        IMatrix m = realMatrix.newInstance(rows + 1, columns + 1);
        return m.subMatrix(0, 0, true);
    }

}
