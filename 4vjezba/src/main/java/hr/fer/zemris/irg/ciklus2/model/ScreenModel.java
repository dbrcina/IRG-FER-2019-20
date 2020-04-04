package hr.fer.zemris.irg.ciklus2.model;

import hr.fer.zemris.irg.ciklus2.algorithm.PolygonUtil;
import hr.fer.zemris.irg.ciklus2.listener.ScreenModelListener;
import hr.fer.zemris.irg.ciklus2.structure.IEdge2D;
import hr.fer.zemris.irg.ciklus2.structure.IPoint2D;
import hr.fer.zemris.irg.ciklus2.structure.IPolyElem;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class ScreenModel implements MouseListener, MouseMotionListener, KeyListener {

    private IPoint2D currentPoint;
    private int numberOfClicks;
    private List<IPolyElem> polyElems = new LinkedList<>();
    private boolean convex;
    private boolean fill;
    private boolean stateDrawPolygon = true;
    private boolean stopAddingPoints;
    private List<ScreenModelListener> listeners = new ArrayList<>();

    public boolean isConvex() {
        return convex;
    }

    public boolean isFill() {
        return fill;
    }

    public boolean isStateDrawPolygon() {
        return stateDrawPolygon;
    }

    public List<IPolyElem> getPolyElems() {
        return polyElems;
    }

    public void addListener(ScreenModelListener l) {
        listeners.add(l);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        boolean update = false;
        switch (code) {
            case KeyEvent.VK_ESCAPE:
                update = handleStopKey();
                break;
            case KeyEvent.VK_K:
                update = handleConvexKey();
                break;
            case KeyEvent.VK_P:
                update = handleFillKey();
                break;
            case KeyEvent.VK_N:
                update = handleStateKey();
                break;
            default:  // do nothing
        }
        if (update) {
            if (code == KeyEvent.VK_N || code == KeyEvent.VK_ESCAPE) {
                notifyListeners(ScreenModelListener::modelUpdated);
            } else {
                addNewPolyElement(currentPoint, false);
            }
        }
    }

    private boolean handleStopKey() {
        System.out.println("Zaustavljeno je crtanje u stanju 1.");
        numberOfClicks = 0;
        stopAddingPoints = true;
        return true;
    }

    private boolean handleConvexKey() {
        if (!stateDrawPolygon) {
            System.out.println("Promjena zastavice 'konveksnost' je moguća samo u stanju 1");
            return false;
        }
        boolean update = polyElems.isEmpty();
        if (!update) {
            boolean[] convexOrientation = new boolean[2];
            PolygonUtil.checkIfConvex(polyElems, convexOrientation);
            if (!convexOrientation[0]) {
                System.out.println("Trenutni poligon nije konveksan pa se promjena zastavice " +
                        "'konveksnost' neće registrirati.");
            }
            update = convexOrientation[0];
        }
        if (update) convex = !convex;
        return update;
    }

    private boolean handleFillKey() {
        if (!stateDrawPolygon) {
            System.out.println("Promjena zastavice 'popunjavanje' je moguća samo u stanju 1");
            return false;
        }
        fill = !fill;
        return true;
    }

    private boolean handleStateKey() {
        stateDrawPolygon = !stateDrawPolygon;
        if (stateDrawPolygon) {
            polyElems.clear();
            currentPoint = null;
            numberOfClicks = 0;
            stopAddingPoints = fill = convex = false;
        }
        return true;
    }

    private void addNewPolyElement(IPoint2D point, boolean mouseClicked) {
        if (point == null) return;
        IPolyElem element = new IPolyElem();
        element.setPoint(point);
        polyElems.add(element);
        PolygonUtil.calculateCoeffConvex(polyElems);
        if (mouseClicked && convex && polyElems.size() > 1) {
            boolean[] convexOrientation = new boolean[2];
            PolygonUtil.checkIfConvex(polyElems, convexOrientation);
            if (!convexOrientation[0]) {
                System.out.println("Točka " + point + " se ne prihvaća jer narušava konveksnost");
                polyElems.remove(polyElems.size() - 1);
                return;
            }
        }
        notifyListeners(ScreenModelListener::modelUpdated);
        if (!mouseClicked) polyElems.remove(polyElems.size() - 1);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (stateDrawPolygon) {
            if (stopAddingPoints) return;
            numberOfClicks++;
            addNewPolyElement(new IPoint2D(e.getX(), e.getY()), true);
        } else {
            IPoint2D p = new IPoint2D(e.getX(), e.getY());
            if (polyElems.size() < 2) {
                System.out.println("Točka " + p + " se nalazi izvan poligona");
                return;
            }
            int above = 0, under = 0;
            for (IPolyElem polyElem : polyElems) {
                IEdge2D edge = polyElem.getEdge();
                int r = edge.getA() * p.getX() + edge.getB() * p.getY() + edge.getC();
                if (r == 0) {
                    System.out.println("Točka " + p + " se nalazi na poligonu");
                    return;
                } else if (r > 0) above++;
                else under++;
            }
            if (above == 0 || under == 0) System.out.println("Točka " + p + " se nalazi u poligonu");
            else System.out.println("Točka " + p + " se nalazi izvan poligona");
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!stateDrawPolygon || numberOfClicks == 0) return;
        currentPoint = new IPoint2D(e.getX(), e.getY());
        addNewPolyElement(currentPoint, false);
    }

    private void notifyListeners(Consumer<ScreenModelListener> action) {
        listeners.forEach(action);
    }

    // IGNORE OTHER METHODS!
    @Override
    public void keyTyped(KeyEvent e) {
        // DO NOTHING
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // DO NOTHING
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // DO NOTHING
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // DO NOTHING
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // DO NOTHING
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // DO NOTHING
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // DO NOTHING
    }

}
