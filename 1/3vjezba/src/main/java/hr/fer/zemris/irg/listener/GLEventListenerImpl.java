package hr.fer.zemris.irg.listener;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import hr.fer.zemris.irg.Zad3;
import hr.fer.zemris.irg.model.Line;
import hr.fer.zemris.irg.model.ScreenModel;

import java.awt.*;
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

        List<Line> lines = model.getLines();
        lines.forEach(line -> {
            line.draw(gl2, true);
            if (model.isControlSet()) line.parallelLine(5, Color.RED).draw(gl2, false);
        });
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
