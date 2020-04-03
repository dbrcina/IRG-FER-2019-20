package hr.fer.zemris.irg.ciklus2.structure;

import com.jogamp.opengl.GL2;
import hr.fer.zemris.irg.ciklus2.algorithm.LineDrawingAlg;

import java.awt.Color;
import java.util.Objects;

/**
 * Represents a 2D line which consists of:
 * <ul>
 *     <li>{@link IPoint2D} start,</li>
 *     <li>{@link IPoint2D} end and</li>
 *     <li>some color.</li>
 * </ul>
 */
public class ILine2D {

    private IPoint2D start;
    private IPoint2D end;
    private Color color;

    public ILine2D(IPoint2D start, IPoint2D end, Color color) {
        this.start = start;
        this.end = end;
        this.color = color;
    }

    public ILine2D(IPoint2D start, IPoint2D end) {
        this(start, end, Color.BLACK);
    }

    public IPoint2D getStart() {
        return start;
    }

    public IPoint2D getEnd() {
        return end;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ILine2D)) return false;
        ILine2D iLine2D = (ILine2D) o;
        return start.equals(iLine2D.start) &&
                end.equals(iLine2D.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    /**
     * Draws a line on a screen. If <i>bresenhamAlg</i> is set to true, then {@link
     * LineDrawingAlg#bresenhamDrawLineI(GL2, ILine2D)} is used, otherwise {@link
     * LineDrawingAlg#normalDrawLine(GL2, ILine2D)} is used.
     *
     * @param gl2          an instance of {@link GL2}.
     * @param bresenhamAlg boolean flag.
     */
    public void draw(GL2 gl2, boolean bresenhamAlg) {
        if (bresenhamAlg) LineDrawingAlg.bresenhamDrawLineI(gl2, this);
        else LineDrawingAlg.normalDrawLine(gl2, this);
    }

}
