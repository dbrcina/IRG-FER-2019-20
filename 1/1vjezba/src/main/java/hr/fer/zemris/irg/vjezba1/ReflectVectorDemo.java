package hr.fer.zemris.irg.vjezba1;

import hr.fer.zemris.linearna.IVector;
import hr.fer.zemris.linearna.Vector;

import java.util.Scanner;

public class ReflectVectorDemo {

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            IVector a = null;
            IVector b = null;
            String vector = "a";
            System.out.println("Reflect vector \033[1ma\033[0m over vector " + "\033[1mb\033[0m:");
            do {
                System.out.print("Enter " + "\033[1m" + vector + "\033[0m = ");
                IVector v = parseVectorFromInput(sc);
                if (v != null) {
                    if (vector.equals("a")) {
                        a = v;
                        vector = "b";
                    } else {
                        b = v;
                        vector = "";
                    }
                }
            } while (!vector.isEmpty());
            if (a.getDimension() != b.getDimension()) {
                System.out.println("Dimensions are not equal");
                return;
            }

            IVector k = b.nNormalize().scalarMultiply(a.scalarProduct(b) / b.norm());
            IVector r = k.nScalarMultiply(2).sub(a);
            System.out.println("\033[1mr\033[0m = " + r);
        }
    }

    private static IVector parseVectorFromInput(Scanner sc) {
        IVector v = Vector.parseSimple(sc.nextLine());
        if (v.getDimension() != 2 && v.getDimension() != 3) {
            System.out.println("Valid dimensions are 2 and 3");
            return null;
        }
        return v;
    }

}
