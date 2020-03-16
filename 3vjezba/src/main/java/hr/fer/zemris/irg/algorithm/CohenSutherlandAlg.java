package hr.fer.zemris.irg.algorithm;

import hr.fer.zemris.irg.model.Line;

import java.awt.*;

public class CohenSutherlandAlg {

    private static final int INSIDE = 0;    // 0000
    private static final int LEFT = 1;      // 0001
    private static final int RIGHT = 2;     // 0010
    private static final int BOTTOM = 4;    // 0100
    private static final int TOP = 8;       // 1000

    private int xMin;
    private int yMin;
    private int xMax;
    private int yMax;

    public CohenSutherlandAlg(int xMin, int yMin, int xMax, int yMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
    }

    public Line clip(Line line) {
        Point p1 = line.getStart();
        int x1 = (int) p1.getX();
        int y1 = (int) p1.getY();
        int code1 = computeCode(x1, y1);
        Point p2 = line.getEnd();
        int x2 = (int) p2.getX();
        int y2 = (int) p2.getY();
        int code2 = computeCode(x2, y2);
        boolean accept = false;

        while (true) {

            // if both endpoints lie within rectangle
            if ((code1 | code2) == 0) {
                accept = true;
                break;
            }

            // if both endpoints are outside rectangle
            else if ((code1 & code2) != 0) {
                break;
            }

            // some segment lies within the rectangle
            else {
                int codeOut;
                int x = 1;
                int y = 1;

                // line needs clipping
                // at least one of the points is outside, select it
                codeOut = code1 != 0 ? code1 : code2;

                // find intersection point;
                // using formulas y = y1 + slope * (x - x1),
                // x = x1 + (1 / slope) * (y - y1)
                if ((codeOut & TOP) != 0) {
                    // point is above the clip rectangle
                    x = x1 + (x2 - x1) * (yMax - y1) / (y2 - y1);
                    y = yMax;
                } else if ((codeOut & BOTTOM) != 0) {
                    // point is below the clip rectangle
                    x = x1 + (x2 - x1) * (yMin - y1) / (y2 - y1);
                    y = yMin;
                } else if ((codeOut & RIGHT) != 0) {
                    // point is to the right of the clip rectangle
                    y = y1 + (y2 - y1) * (xMax - x1) / (x2 - x1);
                    x = xMax;
                } else if ((codeOut & LEFT) != 0) {
                    // point is to the left of the clip rectangle
                    y = y1 + (y2 - y1) * (xMin - x1) / (x2 - x1);
                    x = xMin;
                }

                // now intersection point x,y is found
                // we replace point outside clipping rectangle
                // by intersection point
                if (codeOut == code1) {
                    x1 = x;
                    y1 = y;
                    code1 = computeCode(x1, y1);
                } else {
                    x2 = x;
                    y2 = y;
                    code2 = computeCode(x2, y2);
                }
            }
        }
        Line clipedLine = null;
        if (accept) clipedLine = new Line(new Point(x1, y1), new Point(x2, y2), line.getColor());
        return clipedLine;
    }

    private int computeCode(int x, int y) {
        int code = INSIDE;
        if (x < xMin) code |= LEFT;         // to the left of rectangle
        else if (x > xMax) code |= RIGHT;   // to the right of rectangle
        if (y < yMin) code |= BOTTOM;       // below the rectangle
        else if (y > yMax) code |= TOP;     // above the rectangle
        return code;
    }

}
