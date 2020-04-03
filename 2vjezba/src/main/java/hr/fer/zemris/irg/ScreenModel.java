package hr.fer.zemris.irg;

import hr.fer.zemris.irg.listener.ScreenModelListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static java.awt.Color.*;

public class ScreenModel {

    private static final Color[] COLORS = {RED, GREEN, BLUE, CYAN, YELLOW, MAGENTA};

    private int currentIndex;
    private Point currentPoint1;
    private Point currentPoint2;
    private Point currentPoint3;
    private int numberOfClicks;
    private List<Triangle> triangles;
    private List<ScreenModelListener> listeners;

    public void mouseClicked(Point p) {
        numberOfClicks++;
        switch (numberOfClicks) {
            case 1:
                setCurrentPoint1(p);
                break;
            case 2:
                setCurrentPoint2(p);
                break;
            case 3:
                setCurrentPoint3(p);
                addTriangle(new Triangle(
                        currentPoint1, currentPoint2, currentPoint3, getCurrentColor()));
                currentPoint1 = currentPoint2 = currentPoint3 = null;
        }
        if (numberOfClicks == 3) numberOfClicks = 0;
    }

    public void mouseMoved(Point p) {
        switch (numberOfClicks) {
            case 1:
                setCurrentPoint2(p);
                break;
            case 2:
                setCurrentPoint3(p);
        }
        notifyListeners(ScreenModelListener::pointMoved);
    }

    public Point getCurrentPoint1() {
        return currentPoint1;
    }

    private void setCurrentPoint1(Point currentPoint1) {
        this.currentPoint1 = currentPoint1;
    }

    public Point getCurrentPoint2() {
        return currentPoint2;
    }

    private void setCurrentPoint2(Point currentPoint2) {
        this.currentPoint2 = currentPoint2;
    }

    public Point getCurrentPoint3() {
        return currentPoint3;
    }

    private void setCurrentPoint3(Point currentPoint3) {
        this.currentPoint3 = currentPoint3;
    }

    public Color getCurrentColor() {
        return COLORS[currentIndex];
    }

    public void prepareNextColor() {
        updateIndex(true);
    }

    public void preparePreviousColor() {
        updateIndex(false);
    }

    private void updateIndex(boolean increment) {
        currentIndex = increment ? currentIndex + 1 : currentIndex - 1;
        int size = COLORS.length;
        if (currentIndex > size - 1) currentIndex = 0;
        if (currentIndex < 0) currentIndex = size - 1;
        notifyListeners(ScreenModelListener::colorChanged);
    }

    private boolean addTriangle(Triangle triangle) {
        if (triangle == null) triangles = new ArrayList<>();
        return triangles.add(triangle);
    }

    public List<Triangle> getTriangles() {
        if (triangles == null) triangles = new ArrayList<>();
        return triangles;
    }

    public boolean addListener(ScreenModelListener l) {
        if (listeners == null) listeners = new ArrayList<>();
        return listeners.add(l);
    }

    public boolean removeListener(ScreenModelListener l) {
        return listeners.remove(l);
    }

    private void notifyListeners(Consumer<ScreenModelListener> function) {
        if (listeners != null) listeners.forEach(function);
    }

}
