package hr.fer.zemris.irg.ciklus2.structure;

public class Vertex3D {

    private double x;
    private double y;
    private double z;

    public Vertex3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public Vertex3D translate(Vertex3D delta) {
        x -= delta.x;
        y -= delta.y;
        z -= delta.z;
        return this;
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

    @Override
    public String toString() {
        return String.format("v %.5f, %.5f, %.5f", x, y, z);
    }

}
