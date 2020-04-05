package hr.fer.zemris.irg.ciklus2;

import hr.fer.zemris.irg.ciklus2.model.ObjectModel;
import hr.fer.zemris.irg.ciklus2.structure.Face3D;
import hr.fer.zemris.irg.ciklus2.structure.Vertex3D;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

public class Zad5 {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Program očekuje jedan argument; putanju do .obj datoteke");
            return;
        }
        if (!args[0].endsWith(".obj")) {
            System.out.println("Program očekuje .obj datoteku");
            return;
        }
        Path file = Paths.get(args[0]);
        ObjectModel model = readFromOBJ(file);
        startConsole(model, file.getFileName().toString());
    }

    private static void startConsole(ObjectModel model, String obj) {
        System.out.println("Učitan model: " + obj);
        System.out.println("Pregled naredbi:");
        System.out.println("(1)quit - izlazak iz programa,");
        System.out.println("(2)normiraj - normiraj koordinate tijela i ispiši rezultat,");
        System.out.println("(3)unos bilo koje 3D točke formata x y z.");
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                String line = sc.nextLine().toLowerCase().trim();
                if (line.isEmpty()) continue;
                if (line.equals("quit")) {
                    System.out.println("Izlaz iz programa...");
                    System.exit(0);
                }
                if (line.equals("normiraj")) {
                    model.normalize();
                    System.out.println(model.dumpToOBJ());
                    continue;
                }
                try {
                    double[] coordinates = Arrays.stream(line.split("\\s+"))
                            .mapToDouble(Double::parseDouble)
                            .toArray();
                    if (coordinates.length != 3) throw new RuntimeException();
                    Vertex3D v = new Vertex3D(coordinates[0], coordinates[1], coordinates[2]);
                    model.checkPointPosition(v);
                } catch (Exception e) {
                    System.out.println("Pogrešna naredba");
                }
            }
        }
    }

    // fills model with data from .obj file
    private static ObjectModel readFromOBJ(Path file) {
        ObjectModel model;
        try (BufferedReader br = Files.newBufferedReader(file)) {
            Collection<Vertex3D> vertices = new ArrayList<>();
            Collection<Face3D> faces = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.matches("^[^vf].*")) continue;
                String[] parts = line.split("\\s+");
                if (parts[0].equals("v")) {
                    Vertex3D v = new Vertex3D(Double.parseDouble(parts[1]),
                            Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
                    vertices.add(v);
                } else {
                    Face3D f = new Face3D(Integer.parseInt(parts[1]) - 1, Integer.parseInt(parts[2]) - 1,
                            Integer.parseInt(parts[3]) - 1);
                    faces.add(f);
                }
            }
            model = new ObjectModel(vertices.toArray(Vertex3D[]::new), faces.toArray(Face3D[]::new));
            return model;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }

}
