package hr.fer.zemris.irg.listener;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import hr.fer.zemris.irg.Zad3;
import hr.fer.zemris.irg.algorithm.CohenSutherlandAlg;
import hr.fer.zemris.irg.model.Line;
import hr.fer.zemris.irg.model.ScreenModel;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

/**
 * An implementation of {@link GLEventListener} interface.
 */
public class GLEventListenerImpl implements GLEventListener {

    private ScreenModel model;

    public GLEventListenerImpl(ScreenModel model) {
        this.model = model;
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        System.out.println("Initialized");
        glAutoDrawable.getGL().getGL2().glClearColor(1, 1, 1, 1);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        System.out.println("Disposed");
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl2.glLoadIdentity();

        int width = glAutoDrawable.getSurfaceWidth();
        int height = glAutoDrawable.getSurfaceHeight();
        int xMin = width / 4;
        int xMax = 3 * width / 4;
        int yMin = height / 4;
        int yMax = 3 * height / 4;
        if (model.isClipSet()) drawClip(gl2, xMin, yMin, xMax, yMax);
        CohenSutherlandAlg clipAlg = new CohenSutherlandAlg(xMin, yMin, xMax, yMax);
        List<Line> lines = model.getLines();
        Iterator<Line> it = lines.iterator();
        while (it.hasNext()) {
            Line line = it.next();
            if (model.isClipSet()) {
                Line clippedLine = clipAlg.clip(line);
                if (clippedLine == null) {
                    it.remove();
                    continue;
                }
                clippedLine.draw(gl2, true);
            } else {
                line.draw(gl2, true);
            }
            if (model.isControlSet()) {
                line.parallelLine(5, Color.RED).draw(gl2, false);
            }
        }
    }

    private void drawClip(GL2 gl2, int xMin, int yMin, int xMax, int yMax) {
        gl2.glColor3f(0, 1, 0);
        gl2.glBegin(GL.GL_LINE_LOOP);
        gl2.glVertex2f(xMin, yMin);
        gl2.glVertex2f(xMax, yMin);
        gl2.glVertex2f(xMax, yMax);
        gl2.glVertex2f(xMin, yMax);
        gl2.glEnd();
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();
        Zad3.setFrameWidth(width);
        Zad3.setFrameHeight(height);

        // coordinate system origin at lower left with width and
        // height same as the window
        GLU glu = new GLU();
        glu.gluOrtho2D(0.0f, width, 0.0f, height);

        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glLoadIdentity();

        gl2.glViewport(0, 0, width, height);
    }

}
