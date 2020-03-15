package hr.fer.zemris.irg;

import java.awt.*;

public class Triangle {

    private Point p1;
    private Point p2;
    private Point p3;
    private Color fill;

    public Triangle(Point p1, Point p2, Point p3, Color fill) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.fill = fill;
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    public Point getP3() {
        return p3;
    }

    public Color getFill() {
        return fill;
    }

}
