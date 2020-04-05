package hr.fer.zemris.irg.ciklus2.model;

import hr.fer.zemris.irg.ciklus2.structure.Face3D;
import hr.fer.zemris.irg.ciklus2.structure.Vertex3D;

import java.util.Arrays;
import java.util.function.BiFunction;

public class ObjectModel {

    private Vertex3D[] vertices;
    private Face3D[] faces;

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
            face.setPlaneCoeff(a, b, c, d);
        }
    }

    private Vertex3D[] findVerticesForFace(Face3D face) {
        int[] indexes = face.getVIndexes();
        return new Vertex3D[]{vertices[indexes[0]], vertices[indexes[1]], vertices[indexes[2]]};
    }

    public ObjectModel copy() {
        return new ObjectModel(Arrays.copyOf(vertices, vertices.length),
                Arrays.copyOf(faces, faces.length));
    }

    public void checkPointPosition(Vertex3D point) {
        int on = 0;
        for (Face3D face : faces) {
            double[] planeCoeff = face.getPlaneCoeff();
            double r = planeCoeff[0] * point.getX() + planeCoeff[1] * point.getY()
                    + planeCoeff[2] * point.getZ() + planeCoeff[3];
            if (r > 0) {
                System.out.println("Točka se nalazi izvan tijela");
                return;
            }
            if (Math.abs(r) < 1e-12) on++;
        }
        if (on > 0) System.out.println("Točka se nalazi na obodu tijela");
        else System.out.println("Točka se nalazi unutar tijela");
    }

    public String dumpToOBJ() {
        StringBuilder sb = new StringBuilder();
        for (Vertex3D v : vertices) sb.append(v).append("\n");
        for (Face3D f : faces) sb.append(f).append("\n");
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public void normalize() {
        double[] minsMaxs = findMinsMaxsCoordinates();
        Vertex3D center = findObjectCenter(minsMaxs);
        double M = findMaxSpan(minsMaxs);
        double alpha = 2 / M;
        for (Vertex3D v : vertices) v.sub(center).scale(alpha);
        calculateFacePlanes();
    }

    // [xmin, xmax, ymin, ymax, zmin, zmax]
    private double[] findMinsMaxsCoordinates() {
        Vertex3D v0 = vertices[0];
        double[] minsMaxs = {v0.getX(), v0.getX(), v0.getY(), v0.getY(), v0.getZ(), v0.getZ()};
        for (int i = 1; i < vertices.length; i++) {
            Vertex3D vi = vertices[i];
            minsMaxs[0] = Math.min(minsMaxs[0], vi.getX());
            minsMaxs[1] = Math.max(minsMaxs[1], vi.getX());
            minsMaxs[2] = Math.min(minsMaxs[2], vi.getY());
            minsMaxs[3] = Math.max(minsMaxs[3], vi.getY());
            minsMaxs[4] = Math.min(minsMaxs[4], vi.getZ());
            minsMaxs[5] = Math.max(minsMaxs[5], vi.getZ());
        }
        return minsMaxs;
    }

    // ((xmin + xmax)/2, (ymin + ymax)/2, (zmin + zmax)/2)
    private Vertex3D findObjectCenter(double[] minsMaxs) {
        double xcenter = (minsMaxs[0] + minsMaxs[1]) / 2;
        double ycenter = (minsMaxs[2] + minsMaxs[3]) / 2;
        double zcenter = (minsMaxs[4] + minsMaxs[5]) / 2;
        return new Vertex3D(xcenter, ycenter, zcenter);
    }

    // M = max(xmax - xmin, ymax - ymin, zmax - zmin)
    private double findMaxSpan(double[] minsMaxs) {
        double xspan = minsMaxs[1] - minsMaxs[0];
        double yspan = minsMaxs[3] - minsMaxs[2];
        double zspan = minsMaxs[5] - minsMaxs[4];
        return Math.max(xspan, Math.max(yspan, zspan));
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

}
