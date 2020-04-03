package hr.fer.zemris.irg.ciklus2.algorithm;

import com.jogamp.opengl.GL2;
import hr.fer.zemris.irg.ciklus2.structure.IEdge2D;
import hr.fer.zemris.irg.ciklus2.structure.ILine2D;
import hr.fer.zemris.irg.ciklus2.structure.IPoint2D;
import hr.fer.zemris.irg.ciklus2.structure.IPolyElem;

import java.util.List;

/**
 * Utility class which provides some methods for polygon manipulation.
 */
public class PolygonUtil {

    /**
     * Draws a convex polygon.
     *
     * @param gl2       instance of {@link GL2}.
     * @param polyElems list of polygon elements.
     */
    public static void drawConvex(GL2 gl2, List<IPolyElem> polyElems) {
        int n = polyElems.size();
        int i0 = n - 1;
        for (int i = 0; i < n; i0 = i++) {
            IPoint2D start = polyElems.get(i0).getPoint();
            IPoint2D end = polyElems.get(i).getPoint();
            LineDrawingAlg.bresenhamDrawLineI(gl2, new ILine2D(start, end));
        }
    }

    /**
     * Calculates polygon's coefficients.
     *
     * @param polyElems list of polygon elements.
     */
    public static void calculateCoeffConvex(List<IPolyElem> polyElems) {
        int n = polyElems.size();
        int i0 = n - 1;
        for (int i = 0; i < n; i0 = i++) {
            IPolyElem element = polyElems.get(i0);
            IPoint2D start = element.getPoint();
            IPoint2D end = polyElems.get(i).getPoint();
            int a = start.getY() - end.getY();
            int b = -(start.getX() - end.getX());
            int c = start.getX() * end.getY() - start.getY() * end.getX();
            element.setEdge(new IEdge2D(a, b, c));
            element.setEdgeLeft(start.getY() < end.getY());
        }
    }

    /**
     * Fills convex polygon using horizontal rays that intersects(or not) with polygon.
     *
     * @param gl2       instance of {@link GL2}.
     * @param polyElems list of polygon elements.
     */
    public static void fillConvex(GL2 gl2, List<IPolyElem> polyElems) {
        int n = polyElems.size();
        int xmin, xmax, ymin, ymax;

        // find min and max coordinates
        xmin = xmax = polyElems.get(0).getPoint().getX();
        ymin = ymax = polyElems.get(0).getPoint().getY();
        for (int i = 1; i < n; i++) {
            IPoint2D p = polyElems.get(i).getPoint();
            if (xmin > p.getX()) xmin = p.getX();
            if (xmax < p.getX()) xmax = p.getX();
            if (ymin > p.getY()) ymin = p.getY();
            if (ymax < p.getY()) ymax = p.getY();
        }

        // fill polygon from ymin to ymax...
        for (int y = ymin; y <= ymax; y++) {
            // find max left and right intersect
            double L = xmin;
            double R = xmax;
            int i0 = n - 1;
            for (int i = 0; i < n; i0 = i++) {
                IPoint2D i0point = polyElems.get(i0).getPoint();
                IEdge2D i0edge = polyElems.get(i0).getEdge();
                // if i0edge is horizontal
                if (i0edge.getA() == 0) {
                    if (i0point.getY() == y) {
                        IPoint2D ipoint = polyElems.get(i).getPoint();
                        if (i0point.getX() < ipoint.getX()) {
                            L = i0point.getX();
                            R = ipoint.getX();
                        } else {
                            L = ipoint.getX();
                            R = i0point.getX();
                        }
                        break;
                    }
                } else { // i0edge is regular, find intersection
                    double x = -(i0edge.getB() * y + i0edge.getC()) / (double) i0edge.getA();
                    if (polyElems.get(i0).isEdgeLeft()) {
                        if (L < x) L = x;
                    } else {
                        if (R > x) R = x;
                    }
                }
            }
            IPoint2D start = new IPoint2D((int) Math.round(L), y);
            IPoint2D end = new IPoint2D((int) Math.round(R), y);
            LineDrawingAlg.bresenhamDrawLineI(gl2, new ILine2D(start, end));
        }
    }

    /**
     * Checks if polynom is convex. Result is returned through an array <i>results</i> which will contain
     * information about whether polynom is convex as first element and vertices orientation as second.
     * If polynom isn't convex, orientation is ignored, otherwise if orientation is set to false,
     * orientation is counter clock-wise. <br> Initialized array <i>results</i> will be provided as an
     * argument.
     *
     * @param polyElems list of polygon elements.
     * @param results   an array where results will be stored.
     */
    public static void checkIfConvex(List<IPolyElem> polyElems, boolean[] results) {
        int n = polyElems.size();
        int above = 0, under = 0, on = 0;
        int i0 = n - 2;
        for (int i = 0; i < n; i++, i0++) {
            if (i0 >= n) i0 = 0;
            IEdge2D i0edge = polyElems.get(i0).getEdge();
            IPoint2D ipoint = polyElems.get(i).getPoint();
            int r = i0edge.getA() * ipoint.getX() + i0edge.getB() * ipoint.getY() + i0edge.getC();
            if (r == 0) on++;
            else if (r > 0) above++;
            else under++;
        }
        if (under == 0) {
            results[0] = true;
            results[1] = false;
        } else if (above == 0) {
            results[0] = results[1] = true;
        } else {
            results[0] = results[1] = false;
        }
    }

}
