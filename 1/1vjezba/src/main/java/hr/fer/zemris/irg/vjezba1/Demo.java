package hr.fer.zemris.irg.vjezba1;

import hr.fer.zemris.linearna.IVector;
import hr.fer.zemris.linearna.Vector;

public class Demo {

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
    }
}
