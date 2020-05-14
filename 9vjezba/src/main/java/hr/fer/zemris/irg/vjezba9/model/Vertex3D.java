package hr.fer.zemris.irg.vjezba9.model;

import hr.fer.zemris.irg.vjezba9.linearna.IVector;
import hr.fer.zemris.irg.vjezba9.linearna.Vector;

import java.util.Objects;

public class Vertex3D {

    private double x;
    private double y;
    private double z;
    private IVector normal;

    public Vertex3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex3D add(Vertex3D other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
        return this;
    }

    public Vertex3D nAdd(Vertex3D other) {
        return copy().add(other);
    }

    public Vertex3D sub(Vertex3D other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
        return this;
    }

    public Vertex3D nSub(Vertex3D other) {
        return copy().sub(other);
    }

    public Vertex3D scale(double alpha) {
        x *= alpha;
        y *= alpha;
        z *= alpha;
        return this;
    }

    public Vertex3D copy() {
        return new Vertex3D(x, y, z);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public IVector getNormal() {
        return new Vector(normal.toArray());
    }

    public void setNormal(IVector normal) {
        this.normal = new Vector(normal.toArray());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertex3D)) return false;
        Vertex3D vertex3D = (Vertex3D) o;
        return Double.compare(vertex3D.x, x) == 0 &&
                Double.compare(vertex3D.y, y) == 0 &&
                Double.compare(vertex3D.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return String.format("v %.5f, %.5f, %.5f", x, y, z);
    }

    public static Vertex3D center(Vertex3D v1, Vertex3D v2, Vertex3D v3) {
        Vertex3D center = v1.nAdd(v2).add(v3);
        center.scale(1.0 / 3);
        return center;
    }

}
