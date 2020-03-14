package hr.fer.zemris.irg;

import hr.fer.zemris.linearna.IMatrix;
import hr.fer.zemris.linearna.Matrix;
import hr.fer.zemris.linearna.Vector;

import java.util.Scanner;

public class Zad1_3_3 {

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Unesi 1. vrh trokuta formata x y z: ");
            String s1 = sc.nextLine();
            System.out.print("Unesi 2. vrh trokuta formata x y z: ");
            String s2 = sc.nextLine();
            System.out.print("Unesi 3. vrh trokuta formata x y z: ");
            String s3 = sc.nextLine();
            System.out.print("Unesi T formata x y z: ");
            String s4 = sc.nextLine();
            IMatrix a = Matrix.parseSimple(s1 + "|" + s2 + "|" + s3);
            IMatrix r = Vector.parseSimple(s4).toColumnMatrix(false);
            IMatrix v = a.nInvert().nMultiply(r);
            System.out.println("Baricentrične koordinate su: " + v.toVector(true));
        }
    }

}
