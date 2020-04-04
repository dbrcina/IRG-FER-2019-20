package hr.fer.zemris.irg.ciklus2.structure;

import java.util.Arrays;

public class Face3D {

    private int[] vIndexes;
    private double[] planeCoeff;

    public Face3D(int... vIndexes) {
        if (vIndexes == null
                || vIndexes.length != 3
                || Arrays.stream(vIndexes).anyMatch(i -> i < 0)
                || Arrays.stream(vIndexes).distinct().count() != 3) {
            throw new IllegalArgumentException(
                    "Face3D expects three different non-negative vertex indexes");
        }
        this.vIndexes = vIndexes;
    }

    public int[] getVIndexes() {
        return vIndexes;
    }

    public double[] getPlaneCoeff() {
        return planeCoeff;
    }

    public void setPlaneCoeff(double[] planeCoeff) {
        if (planeCoeff.length != 4) {
            throw new IllegalArgumentException("Face3D plane expects four coefficients");
        }
        this.planeCoeff = planeCoeff;
    }

    @Override
    public String toString() {
        return String.format("f %d %d %d", vIndexes[0] + 1, vIndexes[1] + 1, vIndexes[2] + 1);
    }

}
