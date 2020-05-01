package hr.fer.zemris.irg.linearna;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Matrix extends AbstractMatrix {

    private double[][] elements;
    private int rows;
    private int cols;

    public Matrix(int rows, int cols) {
        this(rows, cols, new double[rows][cols], false);
    }

    public Matrix(int rows, int cols, double[][] elements, boolean copyElements) {
        this.rows = rows;
        this.cols = cols;
        this.elements = copyElements
                ? Arrays.stream(elements).map(double[]::clone).toArray(double[][]::new)
                : elements;
    }

    @Override
    public int getRowsCount() {
        return rows;
    }

    @Override
    public int getColsCount() {
        return cols;
    }

    @Override
    public double get(int row, int column) {
        return elements[row][column];
    }

    @Override
    public IMatrix set(int row, int column, double value) {
        elements[row][column] = value;
        return this;
    }

    @Override
    public IMatrix copy() {
        return new Matrix(rows, cols, elements, true);
    }

    @Override
    public IMatrix newInstance(int rows, int columns) {
        return new Matrix(rows, columns);
    }

    public static IMatrix parseSimple(String s) {
        List<double[]> elements = new ArrayList<>();
        String[] rows = s.trim().split("\\|");
        for (String row : rows) {
            elements.add(Arrays.stream(row.trim().split("\\s+"))
                    .mapToDouble(Double::parseDouble)
                    .toArray()
            );
        }
        return new Matrix(
                elements.size(),
                elements.get(0).length,
                elements.toArray(new double[0][]),
                false);
    }

}
