package hr.fer.zemris.irg;

import hr.fer.zemris.linearna.IMatrix;
import hr.fer.zemris.linearna.Matrix;

public class Zad1_2_2 {

    public static void main(String[] args) {
        IMatrix a = Matrix.parseSimple("3 5 | 2 10");
        IMatrix r = Matrix.parseSimple("2 | 8");
        IMatrix v = a.nInvert().nMultiply(r);
        System.out.println("Rješenje sustava je:");
        System.out.println(v);
    }

}
