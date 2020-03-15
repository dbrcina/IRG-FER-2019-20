package hr.fer.zemris.irg.algorithm;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import hr.fer.zemris.irg.model.Line;

import java.awt.*;

/**
 * Represents Bresenham's algorithm for drawing lines.
 */
public class LineDrawingAlg {

    /**
     * Draws a line using normal algorithm given with <i>OpenGL API</i> and GL_LINES primitive.
     *
     * @param gl2  instance of {@link GL2}.
     * @param line model of {@link Line}.
     */
    public static void normalDrawLine(GL2 gl2, Line line) {
        prepareColor(gl2, line.getColor());
        Point start = line.getStart();
        Point end = line.getEnd();
        gl2.glBegin(GL.GL_LINES);
        gl2.glVertex2d(start.getX(), start.getY());
        gl2.glVertex2d(end.getX(), end.getY());
        gl2.glEnd();
    }

    private static void prepareColor(GL2 gl2, Color color) {
        gl2.glColor3f(color.getRed() / 255, color.getGreen() / 255, color.getBlue() / 255);
    }

    /**
     * Draws a line using Bresenham's algorithm with integers.
     *
     * @param gl2  instance of {@link GL2}.
     * @param line model of {@link Line}.
     */
    public static void bresenhamDrawLineI(GL2 gl2, Line line) {
        prepareColor(gl2, line.getColor());
        Point start = line.getStart();
        Point end = line.getEnd();
        if (start.getX() <= end.getX()) {
            if (start.getY() <= end.getY()) {
                drawFrom0To90(gl2, start.getX(), start.getY(), end.getX(), end.getY());
            } else {
                drawFrom0To90Inverse(gl2, start.getX(), start.getY(), end.getX(), end.getY());
            }
        } else {
            if (start.getY() >= end.getY()) {
                drawFrom0To90(gl2, end.getX(), end.getY(), start.getX(), start.getY());
            } else {
                drawFrom0To90Inverse(gl2, end.getX(), end.getY(), start.getX(), start.getY());
            }
        }
    }


    private static void drawFrom0To90(GL2 gl2, double xs, double ys, double xe, double ye) {
        // change coordinates if points are from right to left..
        if (ye - ys <= xe - xs) draw(gl2, xs, ys, xe, ye, false);
        else draw(gl2, ys, xs, ye, xe, true);
    }

    private static void drawFrom0To90Inverse(GL2 gl2, double xs, double ys, double xe, double ye) {
        if (-(ye - ys) <= xe - xs) drawInverse(gl2, xs, ys, xe, ye, false);
        else drawInverse(gl2, ye, xe, ys, xs, true);
    }

    // helper method used for drawing lines from 0 to 90
    private static void draw(GL2 gl2, double xs, double ys, double xe, double ye, boolean changeLines) {
        int a = (int) (2 * (ye - ys));
        int yc = (int) ys;
        int yf = (int) -(xe - xs);
        int korekcija = 2 * yf;
        gl2.glBegin(GL.GL_POINTS);
        for (int x = (int) xs; x <= xe; x++) {
            if (!changeLines) gl2.glVertex2f(x, yc);
            else gl2.glVertex2f(yc, x);
            yf += a;
            if (yf >= 0) {
                yf += korekcija;
                yc++;
            }
        }
        gl2.glEnd();
    }

    // helper method used for drawing lines from 0 to -90
    private static void drawInverse(
            GL2 gl2, double xs, double ys, double xe, double ye, boolean changeLines) {
        int a = (int) (2 * (ye - ys));
        int yc = (int) ys;
        int yf = (int) (xe - xs);
        int korekcija = 2 * yf;
        gl2.glBegin(GL.GL_POINTS);
        for (int x = (int) xs; x <= xe; x++) {
            if (!changeLines) gl2.glVertex2f(x, yc);
            else gl2.glVertex2f(yc, x);
            yf += a;
            if (yf <= 0) {
                yf += korekcija;
                yc--;
            }
        }
        gl2.glEnd();
    }

}
