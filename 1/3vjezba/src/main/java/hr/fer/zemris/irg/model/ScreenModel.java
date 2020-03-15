package hr.fer.zemris.irg.model;

import hr.fer.zemris.irg.listener.ScreenModelListener;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ScreenModel {

    private Point currentPoint1;
    private Point currentPoint2;
    private int numberOfClicks;
    private List<Line> lines;
    private boolean control;
    private boolean clip;
    private List<ScreenModelListener> listeners;

    public boolean isControlSet() {
        return control;
    }

    public boolean isClipSet() {
        return clip;
    }

    public void toggleControl() {
        control = !control;
        notifyListeners(ScreenModelListener::keyPressed);
    }

    public void toggleClip() {
        clip = !clip;
        notifyListeners(ScreenModelListener::keyPressed);
    }

    public void mouseClicked(Point p) {
        numberOfClicks++;
        if (numberOfClicks == 1) setCurrentPoint1(p);
        else {
            setCurrentPoint2(p);
            addLine(new Line(currentPoint1, currentPoint2));
            notifyListeners(ScreenModelListener::lineDefined);
            currentPoint1 = currentPoint2 = null;
        }
        if (numberOfClicks == 2) numberOfClicks = 0;
    }

    private void setCurrentPoint1(Point currentPoint1) {
        this.currentPoint1 = currentPoint1;
    }

    private void setCurrentPoint2(Point currentPoint2) {
        this.currentPoint2 = currentPoint2;
    }

    public List<Line> getLines() {
        if (lines == null) lines = new ArrayList<>();
        return lines;
    }

    private boolean addLine(Line line) {
        if (line == null) lines = new ArrayList<>();
        return lines.add(line);
    }

    public boolean addListener(ScreenModelListener l) {
        if (listeners == null) listeners = new ArrayList<>();
        return listeners.add(l);
    }

    public boolean removeListener(ScreenModelListener l) {
        return listeners != null && listeners.remove(l);
    }

    private void notifyListeners(Consumer<ScreenModelListener> function) {
        if (listeners != null) listeners.forEach(function);
    }

}
