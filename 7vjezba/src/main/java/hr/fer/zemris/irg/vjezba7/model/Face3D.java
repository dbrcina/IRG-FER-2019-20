package hr.fer.zemris.irg.vjezba7.model;

import java.util.Arrays;

public class Face3D {

    private final int[] vIndexes;
    private double[] planeCoefficients;
    private boolean visible;

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

    public double[] getPlaneCoefficients() {
        return planeCoefficients;
    }

    public void setPlaneCoefficients(double ...planeCoefficients) {
        if (planeCoefficients.length != 4) {
            throw new IllegalArgumentException("Face3D plane expects four coefficients");
        }
        this.planeCoefficients = planeCoefficients;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return String.format("f %d %d %d", vIndexes[0] + 1, vIndexes[1] + 1, vIndexes[2] + 1);
    }

}
