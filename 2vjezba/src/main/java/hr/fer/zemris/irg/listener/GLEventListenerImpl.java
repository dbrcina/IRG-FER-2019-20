package hr.fer.zemris.irg.listener;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import hr.fer.zemris.irg.ScreenModel;
import hr.fer.zemris.irg.Triangle;

import java.awt.*;
import java.util.List;


/**
 * An implementation of {@link GLEventListener} interface.
 */
public class GLEventListenerImpl implements GLEventListener {

    private final int quadDimension = 10;

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
        displayFilledQuad(gl2, glAutoDrawable.getSurfaceWidth(), glAutoDrawable.getSurfaceHeight());
        displayFilledTriangles(gl2);
    }

    private void displayFilledQuad(GL2 gl2, int surfaceWidth, int surfaceHeight) {
        Color color = model.getCurrentColor();
        gl2.glColor3f(color.getRed() / 255, color.getGreen() / 255, color.getBlue() / 255);
        gl2.glBegin(GL2.GL_QUADS);
        gl2.glVertex2f(surfaceWidth - quadDimension, quadDimension);
        gl2.glVertex2f(surfaceWidth - quadDimension, 0);
        gl2.glVertex2f(surfaceWidth, 0);
        gl2.glVertex2f(surfaceWidth, quadDimension);
        gl2.glEnd();
    }

    private void displayFilledTriangles(GL2 gl2) {
        List<Triangle> triangles = model.getTriangles();
        triangles.forEach(triangle -> {
            Color color = triangle.getFill();
            gl2.glColor3f(color.getRed() / 255, color.getGreen() / 255, color.getBlue() / 255);
            drawTriangle(gl2, triangle);
        });

        Color color = model.getCurrentColor();
        gl2.glColor3f(color.getRed() / 255, color.getGreen() / 255, color.getBlue() / 255);

        Point p1 = model.getCurrentPoint1();
        Point p2 = model.getCurrentPoint2();
        Point p3 = model.getCurrentPoint3();
        if (p1 == null) return;
        if (p2 != null && p3 == null) {
            drawLine(gl2, p1, p2);
        } else if (p2 != null) {
            drawTriangle(gl2, new Triangle(p1, p2, p3, color));
        }
    }

    private void drawLine(GL2 gl2, Point p1, Point p2) {
        gl2.glBegin(GL.GL_LINES);
        gl2.glVertex2f(p1.x, p1.y);
        gl2.glVertex2f(p2.x, p2.y);
        gl2.glEnd();
    }

    private void drawTriangle(GL2 gl2, Triangle triangle) {
        Point p1 = triangle.getP1();
        Point p2 = triangle.getP2();
        Point p3 = triangle.getP3();
        gl2.glBegin(GL.GL_TRIANGLES);
        gl2.glVertex2f(p1.x, p1.y);
        gl2.glVertex2f(p2.x, p2.y);
        gl2.glVertex2f(p3.x, p3.y);
        gl2.glEnd();
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();

        // coordinate system origin at lower left with width and
        // height same as the window
        GLU glu = new GLU();
        glu.gluOrtho2D(0.0f, width, height, 0.0f);

        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glLoadIdentity();

        gl2.glViewport(0, 0, width, height);
    }

}
