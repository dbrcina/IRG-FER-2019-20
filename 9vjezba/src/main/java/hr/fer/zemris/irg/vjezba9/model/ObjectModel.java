package hr.fer.zemris.irg.vjezba9.model;

import hr.fer.zemris.irg.vjezba9.linearna.IVector;
import hr.fer.zemris.irg.vjezba9.linearna.Vector;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiFunction;

public class ObjectModel {

    private final Vertex3D[] vertices;
    private final Face3D[] faces;

    public ObjectModel(Vertex3D[] vertices, Face3D[] faces) {
        this.vertices = vertices;
        this.faces = faces;
        calculateFacePlanes();
    }

    private void calculateFacePlanes() {
        for (Face3D face : faces) {
            Vertex3D[] fVertices = findVerticesForFace(face);
            Vertex3D v1 = fVertices[1].nSub(fVertices[0]);
            Vertex3D v2 = fVertices[2].nSub(fVertices[0]);
            double a = A.apply(v1, v2);
            double b = B.apply(v1, v2);
            double c = C.apply(v1, v2);
            double d = D.apply(new double[]{a, b, c}, fVertices[0]);
            face.setPlaneCoefficients(a, b, c, d);
        }
    }

    public Face3D[] getFaces() {
        return faces;
    }

    public Vertex3D[] findVerticesForFace(Face3D face) {
        int[] indexes = face.getVIndexes();
        return new Vertex3D[]{vertices[indexes[0]], vertices[indexes[1]], vertices[indexes[2]]};
    }

    public void determineFaceVisibilities1(IVector eye) {
        for (Face3D face : faces) {
            double[] coefficients = face.getPlaneCoefficients();
            double r = coefficients[0] * eye.get(0) + coefficients[1] * eye.get(1)
                    + coefficients[2] * eye.get(2) + coefficients[3];
            face.setVisible(r > 0);
        }
    }

    public void determineFaceVisibilities2(IVector eye) {
        for (Face3D face : faces) {
            IVector[] vertices = Arrays.stream(findVerticesForFace(face))
                    .map(v -> new Vector(v.getX(), v.getY(), v.getZ()))
                    .toArray(IVector[]::new);
            IVector c = (vertices[0].nAdd(vertices[1]).add(vertices[2])).scalarMultiply(1.0 / 3);
            IVector e = eye.nSub(c);
            IVector n = (vertices[1].nSub(vertices[0])).nVectorProduct(vertices[2].sub(vertices[0]));
            face.setVisible(n.scalarProduct(e) > 0);
        }
    }

    public void determineNormalsForVertices() {
        for (Vertex3D vertex : vertices) {
            IVector normal = new Vector(0, 0, 0);
            int n = 0;
            for (Face3D face : faces) {
                Vertex3D[] fVertices = findVerticesForFace(face);
                if (Arrays.asList(fVertices).contains(vertex)) {
                    normal.add(face.normal().normalize());
                    n++;
                }
            }
            vertex.setNormal(normal.scalarMultiply(1.0 / n).normalize());
        }
    }

    public void checkPointPosition(Vertex3D point) {
        int on = 0;
        for (Face3D face : faces) {
            double[] planeCoefficients = face.getPlaneCoefficients();
            double r = planeCoefficients[0] * point.getX() + planeCoefficients[1] * point.getY()
                    + planeCoefficients[2] * point.getZ() + planeCoefficients[3];
            if (r > 0) {
                System.out.println("Točka se nalazi izvan tijela.");
                return;
            }
            if (Math.abs(r) < 1e-12) on++;
        }
        if (on > 0) System.out.println("Točka se nalazi na obodu tijela.");
        else System.out.println("Točka se nalazi unutar tijela.");
    }

    public String dumpToOBJ() {
        StringBuilder sb = new StringBuilder();
        for (Vertex3D v : vertices) sb.append(v).append("\n");
        for (Face3D f : faces) sb.append(f).append("\n");
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public ObjectModel normalize() {
        double[] minMax = findMinMaxCoordinates();
        Vertex3D center = findObjectCenter(minMax);
        double M = findMaxSpan(minMax);
        double alpha = 2 / M;
        for (Vertex3D v : vertices) v.sub(center).scale(alpha);
        calculateFacePlanes();
        determineNormalsForVertices();
        return this;
    }

    // [xMin, xMax, yMin, yMax, zMin, zMax]
    private double[] findMinMaxCoordinates() {
        Vertex3D v0 = vertices[0];
        double[] minMax = {v0.getX(), v0.getX(), v0.getY(), v0.getY(), v0.getZ(), v0.getZ()};
        for (int i = 1; i < vertices.length; i++) {
            Vertex3D vi = vertices[i];
            minMax[0] = Math.min(minMax[0], vi.getX());
            minMax[1] = Math.max(minMax[1], vi.getX());
            minMax[2] = Math.min(minMax[2], vi.getY());
            minMax[3] = Math.max(minMax[3], vi.getY());
            minMax[4] = Math.min(minMax[4], vi.getZ());
            minMax[5] = Math.max(minMax[5], vi.getZ());
        }
        return minMax;
    }

    // ((xMin + xMax)/2, (yMin + yMax)/2, (zMin + zMax)/2)
    private Vertex3D findObjectCenter(double[] minMax) {
        double xCenter = (minMax[0] + minMax[1]) / 2;
        double yCenter = (minMax[2] + minMax[3]) / 2;
        double zCenter = (minMax[4] + minMax[5]) / 2;
        return new Vertex3D(xCenter, yCenter, zCenter);
    }

    // M = max(xMax - xMin, yMax - yMin, zMax - zMin)
    private double findMaxSpan(double[] minMax) {
        double xSpan = minMax[1] - minMax[0];
        double ySpan = minMax[3] - minMax[2];
        double zSpan = minMax[5] - minMax[4];
        return Math.max(xSpan, Math.max(ySpan, zSpan));
    }

    // A = y1 * z2 - z1 * y2
    private static final BiFunction<Vertex3D, Vertex3D, Double> A =
            (v1, v2) -> v1.getY() * v2.getZ() - v1.getZ() * v2.getY();

    // B = -x1 * z2 + z1 * x2
    private static final BiFunction<Vertex3D, Vertex3D, Double> B =
            (v1, v2) -> -v1.getX() * v2.getZ() + v1.getZ() * v2.getX();

    // C = x1 * y2 - y1 * x2
    private static final BiFunction<Vertex3D, Vertex3D, Double> C =
            (v1, v2) -> v1.getX() * v2.getY() - v1.getY() * v2.getX();

    // D = -x * A - y * B - z * C
    private static final BiFunction<double[], Vertex3D, Double> D =
            (coeffs, v) -> -v.getX() * coeffs[0] - v.getY() * coeffs[1] - v.getZ() * coeffs[2];

    public static ObjectModel readFromOBJ(Path file) {
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
