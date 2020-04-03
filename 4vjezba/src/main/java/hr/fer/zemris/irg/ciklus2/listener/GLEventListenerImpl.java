package hr.fer.zemris.irg.ciklus2.listener;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import hr.fer.zemris.irg.ciklus2.Zad4;
import hr.fer.zemris.irg.ciklus2.algorithm.PolygonUtil;
import hr.fer.zemris.irg.ciklus2.model.ScreenModel;
import hr.fer.zemris.irg.ciklus2.structure.IPolyElem;

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
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glEnable(GL2.GL_POINT_SMOOTH);
        gl2.glHint(GL.GL_NICEST, GL.GL_DONT_CARE);
        gl2.glEnable(GL.GL_BLEND);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        System.out.println("Disposed");
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        if (model.isConvex()) {
            glAutoDrawable.getGL().getGL2().glClearColor(0, 1, 0, 1);
        } else {
            glAutoDrawable.getGL().getGL2().glClearColor(1, 1, 1, 1);
        }
        gl2.glClear(GL.GL_COLOR_BUFFER_BIT);

        List<IPolyElem> polyElems = model.getPolyElems();
        if (polyElems.isEmpty()) return;
        if (model.isStateDrawPolygon()) drawPolygonState(gl2, polyElems);
    }

    private void drawPolygonState(GL2 gl2, List<IPolyElem> polyElems) {
        if (model.isFill()) {
            PolygonUtil.calculateCoeffConvex(polyElems);
            PolygonUtil.fillConvex(gl2, polyElems);
        } else {
            PolygonUtil.drawConvex(gl2, polyElems);
        }
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();
        Zad4.setFrameWidth(width);
        Zad4.setFrameHeight(height);

        GLU glu = new GLU();
        glu.gluOrtho2D(0.0f, width, height, 0.0f);

        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glLoadIdentity();

        gl2.glViewport(0, 0, width, height);
    }

}
