package hr.fer.zemris.irg.model;

import com.jogamp.opengl.GL2;
import hr.fer.zemris.irg.algorithm.LineDrawingAlg;

import java.awt.Point;
import java.awt.Color;

public class Line {

    private Point start;
    private Point end;
    private Color color;

    public Line(Point start, Point end, Color color) {
        this.start = start;
        this.end = end;
        this.color = color;
    }

    public Line(Point start, Point end) {
        this(start, end, Color.BLACK);
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    public Color getColor() {
        return color;
    }

    public void draw(GL2 gl2, boolean bresenhamAlg) {
        if (bresenhamAlg) LineDrawingAlg.bresenhamDrawLineI(gl2, this);
        else LineDrawingAlg.normalDrawLine(gl2, this);
    }

    public Line parallelLine(int units, Color color) {
        int x1 = (int) (start.getX() + units);
        int y1 = (int) (start.getY() - units);
        int x2 = (int) (end.getX() + units);
        int y2 = (int) (end.getY() - units);
        return new Line(new Point(x1, y1), new Point(x2, y2), color);
    }

}
