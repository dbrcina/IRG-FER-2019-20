package hr.fer.zemris.irg;

import hr.fer.zemris.linearna.IMatrix;
import hr.fer.zemris.linearna.Matrix;

public class Zad1_2_3 {

    public static void main(String[] args) {
        IMatrix a = Matrix.parseSimple("1 5 3 | 0 0 8 |1 1  1");
        IMatrix r = Matrix.parseSimple("3|4|1");
        IMatrix v = a.nInvert().nMultiply(r);
        System.out.println("Baricentrične koordinate su: " + v.toVector(false));
    }

}
