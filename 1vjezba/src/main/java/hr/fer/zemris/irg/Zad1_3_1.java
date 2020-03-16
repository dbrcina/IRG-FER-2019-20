package hr.fer.zemris.irg;

import hr.fer.zemris.linearna.IMatrix;
import hr.fer.zemris.linearna.IVector;
import hr.fer.zemris.linearna.Matrix;
import hr.fer.zemris.linearna.Vector;

public class Zad1_3_1 {

    public static void main(String[] args) {
        IVector a = new Vector(2, 3, -4);
        IVector b = new Vector(-1, 4, -3);
        IVector v1 = a.nAdd(b);
        System.out.println("\033[1mv1\033[0m = " + a + " + " + b + " = " + v1);
        double s = v1.scalarProduct(b);
        System.out.println("s = " + "\033[1mv1\033[0m * " + b + " = " + s);
        IVector c = Vector.parseSimple("2 2 4");
        IVector v2 = v1.nVectorProduct(c);
        System.out.println("\033[1mv2\033[0m = " + "\033[1mv1\033[0m x " + c + " = " + v2);
        IVector v3 = v2.nNormalize();
        System.out.println("\033[1mv3\033[0m = normalize(" + "\033[1mv2\033[0m) = " + v3);
        IVector v4 = v2.nInverse();
        System.out.println("\033[1mv4\033[0m = -" + "\033[1mv2\033[0m = " + v4);
        System.out.println();

        IMatrix t1 = Matrix.parseSimple("1 2 3 | 2 1 3 | 4 5 1");
        IMatrix t2 = Matrix.parseSimple("-1 2 -3 | 5 -2 7 | -4 -1 3");
        IMatrix m1 = t1.nAdd(t2);
        System.out.println("\033[1mM1\033[0m:");
        System.out.println(m1);
        IMatrix m2 = t1.nMultiply(t2.nTranspose(false));
        System.out.println("\033[1mM2\033[0m:");
        System.out.println(m2);
        IMatrix t3 = Matrix.parseSimple("-24 18 5 | 20 -15 -4 | -5 4 1");
        IMatrix t4 = Matrix.parseSimple("1 2 3 | 0 1 4 | 5 6 0");
        IMatrix m3 = t3.nInvert().nMultiply(t4.nInvert());
        System.out.println("\033[1mM3\033[0m:");
        System.out.println(m3);

        IVector k = Vector.parseSimple("1 2 3 1");
        IMatrix t5 = Matrix.parseSimple("1 0 0 0 | 0 2 0 0 | 0 0 1 0 | 2 3 3 1");
        IVector v = k.toRowMatrix(false).nMultiply(t5).toVector(false);
        System.out.println("\033[1mV\033[0m = " + v);
    }

}
