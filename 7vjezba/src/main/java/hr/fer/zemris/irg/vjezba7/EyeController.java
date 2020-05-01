package hr.fer.zemris.irg.vjezba7;

import hr.fer.zemris.irg.linearna.IVector;
import hr.fer.zemris.irg.linearna.Vector;

public class EyeController {

    private static final double INCREMENT = 1.0;

    private final double initialX;
    private final double initialY;
    private final double initialZ;
    private final double initialAngle;
    private final double radius;

    private double angle;
    private double x;
    private double y;
    private double z;

    public EyeController(double initialX, double initialY, double initialZ) {
        this.initialX = x = initialX;
        this.initialY = y = initialY;
        this.initialZ = z = initialZ;
        this.initialAngle = angle = Math.atan(z / x);
        this.radius = z / Math.sin(angle);
        calculateEye();
    }

    public void reset() {
        x = initialX;
        y = initialY;
        z = initialZ;
        angle = initialAngle;
        calculateEye();
    }

    public void increment() {
        angle = Math.toRadians(Math.toDegrees(angle) + INCREMENT);
        calculateEye();
    }

    public void decrement() {
        angle = Math.toRadians(Math.toDegrees(angle) - INCREMENT);
        calculateEye();
    }

    private void calculateEye() {
        x = radius * Math.cos(angle);
        z = radius * Math.sin(angle);
    }

    public IVector eye() {
        return new Vector(x, y, z);
    }

}
