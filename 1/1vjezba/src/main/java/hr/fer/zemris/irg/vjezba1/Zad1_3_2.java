package hr.fer.zemris.irg.vjezba1;

import hr.fer.zemris.linearna.IMatrix;
import hr.fer.zemris.linearna.Matrix;

import java.util.Scanner;

public class Zad1_3_2 {

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Unesi za 1. jednadžbu s razmakom: ");
            String s1 = sc.nextLine();
            System.out.print("Unesi za 2. jednadžbu s razmakom: ");
            String s2 = sc.nextLine();
            System.out.print("Unesi za 3. jednadžbu s razmakom: ");
            String s3 = sc.nextLine();

            String[] args1 = splitByLastSpace(s1);
            String[] args2 = splitByLastSpace(s2);
            String[] args3 = splitByLastSpace(s3);

            IMatrix a = Matrix.parseSimple(args1[0] + "|" + args2[0] + "|" + args3[0]);
            IMatrix r = Matrix.parseSimple(args1[1] + "|" + args2[1] + "|" + args3[1]);
            IMatrix v = a.nInvert().nMultiply(r);
            System.out.println("[x, y, z] = " + v.nTranspose(true));
        }
    }

    private static String[] splitByLastSpace(String s) {
        int i = s.lastIndexOf(' ');
        return new String[]{s.substring(0, i), s.substring(i)};
    }

}
