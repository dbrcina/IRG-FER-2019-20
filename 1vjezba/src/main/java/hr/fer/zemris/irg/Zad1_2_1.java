package hr.fer.zemris.irg;

import hr.fer.zemris.linearna.IVector;
import hr.fer.zemris.linearna.Vector;

public class Zad1_2_1 {

    public static void main(String[] args) {
        IVector a = Vector.parseSimple("1 0 0");
        IVector b = Vector.parseSimple("5 0 0");
        IVector c = Vector.parseSimple("3 8 0");

        IVector t = Vector.parseSimple("3 4 0");

        double pov = b.nSub(a).nVectorProduct(c.nSub(a)).norm() / 2.0;
        double povA = b.nSub(t).nVectorProduct(c.nSub(t)).norm() / 2.0;
        double povB = a.nSub(t).nVectorProduct(c.nSub(t)).norm() / 2.0;
        double povC = a.nSub(t).nVectorProduct(b.nSub(t)).norm() / 2.0;

        double t1 = povA / pov;
        double t2 = povB / pov;
        double t3 = povC / pov;

        System.out.println("Baricentrične koordinate su: (" + t1 + ", " + t2 + ", " + t3 + ")");
    }

}
