package hr.fer.zemris.irg.vjezba6.demo;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    private static int frameWidth;
    private static int frameHeight;

    static {
        GLProfile.initSingleton();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frameWidth = (int) d.getWidth() / 2;
        frameHeight = (int) d.getHeight() / 2;
    }

    private final GLCanvas canvas = new GLCanvas(new GLCapabilities(GLProfile.getDefault()));

    public Main() {
        initGUI();
        canvas.addGLEventListener(new GLEventListener() {
            @Override
            public void init(GLAutoDrawable glAutoDrawable) {
                System.out.println("Initialized");
                glAutoDrawable.getGL().getGL2().glClearColor(1, 1, 1, 0);
            }

            @Override
            public void dispose(GLAutoDrawable glAutoDrawable) {
                System.out.println("Disposed");
            }

            @Override
            public void display(GLAutoDrawable glAutoDrawable) {
                GL2 gl2 = glAutoDrawable.getGL().getGL2();
                gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
                gl2.glMatrixMode(GL2.GL_MODELVIEW);
                gl2.glLoadIdentity();

                // Crtam vodoravnu liniju na blizoj ravnini, pri dnu...
                gl2.glColor3f(1, 0, 0);
                gl2.glBegin(GL.GL_LINE_STRIP);
                gl2.glVertex3f(-0.9f, -0.9f, -0.9f);
                gl2.glVertex3f(0.9f, -0.9f, -0.9f);
                gl2.glEnd();

                // Crtam vodoravnu liniju koja se udaljava, iznad prethodne...
                gl2.glColor3f(1, 0, 0);
                gl2.glBegin(GL.GL_LINE_STRIP);
                gl2.glVertex3f(-0.9f, -0.7f, -0.9f);
                gl2.glVertex3f(0.9f, -0.7f, 3.1f);
                gl2.glEnd();
            }

            @Override
            public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
                frameWidth = width;
                frameHeight = height;
                GL2 gl2 = glAutoDrawable.getGL().getGL2();
                gl2.glMatrixMode(GL2.GL_PROJECTION_MATRIX);
                gl2.glLoadIdentity();

                gl2.glViewport(width / 2, height / 2, width / 2, height / 2);
            }
        });
    }

    private void initGUI() {
        setSize(frameWidth, frameHeight);
        setLocationRelativeTo(null);
        setTitle("OpenGL volumen pogleda");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().add(canvas, BorderLayout.CENTER);
        setVisible(true);
        canvas.requestFocusInWindow();
    }

}
