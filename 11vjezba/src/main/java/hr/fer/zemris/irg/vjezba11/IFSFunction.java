package hr.fer.zemris.irg.vjezba11;

public class IFSFunction {

    private final double a;
    private final double b;
    private final double c;
    private final double d;
    private final double e;
    private final double f;
    private final double probability;

    public IFSFunction(double a, double b, double c, double d, double e, double f, double probability) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.probability = probability;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getD() {
        return d;
    }

    public double getE() {
        return e;
    }

    public double getF() {
        return f;
    }

    public double getProbability() {
        return probability;
    }

}
