package hr.fer.zemris.irg.ciklus2.structure;

import java.util.Objects;

/**
 * Represents one polygon's element. <br> It consists of:
 * <ul>
 *     <li>one {@link IPoint2D},</li>
 *     <li>one {@link IEdge2D} and</li>
 *     <li>boolean flag which separates the left from the right edge.</li>
 * </ul>
 */
public class IPolyElem {

    private IPoint2D point;
    private IEdge2D edge;
    private boolean isEdgeLeft;

    public IPoint2D getPoint() {
        return point;
    }

    public void setPoint(IPoint2D point) {
        this.point = point;
    }

    public IEdge2D getEdge() {
        return edge;
    }

    public void setEdge(IEdge2D edge) {
        this.edge = edge;
    }

    public boolean isEdgeLeft() {
        return isEdgeLeft;
    }

    public void setEdgeLeft(boolean edgeLeft) {
        isEdgeLeft = edgeLeft;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IPolyElem)) return false;
        IPolyElem iPolyElem = (IPolyElem) o;
        return isEdgeLeft == iPolyElem.isEdgeLeft &&
                Objects.equals(point, iPolyElem.point) &&
                Objects.equals(edge, iPolyElem.edge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(point, edge, isEdgeLeft);
    }

}
