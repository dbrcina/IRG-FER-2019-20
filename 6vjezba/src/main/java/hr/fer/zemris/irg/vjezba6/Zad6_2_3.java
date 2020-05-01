package hr.fer.zemris.irg.vjezba6;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import hr.fer.zemris.irg.linearna.IMatrix;
import hr.fer.zemris.irg.linearna.IRG;
import hr.fer.zemris.irg.linearna.IVector;
import hr.fer.zemris.irg.linearna.Vector;
import hr.fer.zemris.irg.vjezba6.model.Face3D;
import hr.fer.zemris.irg.vjezba6.model.ObjectModel;
import hr.fer.zemris.irg.vjezba6.model.Vertex3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Zad6_2_3 extends JFrame {

    static {
        GLProfile.initSingleton();
    }

    private static int frameWidth = 640;
    private static int frameHeight = 480;

    private final GLCanvas canvas = new GLCanvas(new GLCapabilities(GLProfile.getDefault()));
    private final ObjectModel model;
    private final EyeController controller = new EyeController(3, 4, 1);

    public Zad6_2_3(ObjectModel model) {
        this.model = model;
        initGUI();
        initGLEventListener();
        initKeyListener();
    }

    private void initGLEventListener() {
        canvas.addGLEventListener(new GLEventListener() {
            public void init(GLAutoDrawable glAutoDrawable) {
                System.out.println("Initialized.");
                glAutoDrawable.getGL().getGL2().glClearColor(0, 1, 0, 0);
            }

            public void dispose(GLAutoDrawable glAutoDrawable) {
                System.out.println("Disposed.");
            }

            public void display(GLAutoDrawable glAutoDrawable) {
                GL2 gl2 = glAutoDrawable.getGL().getGL2();
                gl2.glClear(GL.GL_COLOR_BUFFER_BIT);

                gl2.glMatrixMode(GL2.GL_MODELVIEW);
                gl2.glLoadIdentity();
                IVector eye = controller.eye();
                IMatrix lookAtMatrix = IRG.lookAtMatrix(eye, new Vector(0, 0, 0), new Vector(0, 1, 0));
                IMatrix frustum = IRG.buildFrustumMatrix(-0.5, 0.5, -0.5, 0.5, 1, 100);

                renderScene(gl2, lookAtMatrix.nMultiply(frustum));
            }

            private void renderScene(GL2 gl2, IMatrix transformMatrix) {
                gl2.glColor3f(1, 0, 0);
                Face3D[] faces = model.getFaces();
                for (Face3D face : faces) {
                    Vertex3D[] vertices = model.findVerticesForFace(face);
                    gl2.glBegin(GL.GL_LINE_LOOP);
                    for (Vertex3D vertex : vertices) {
                        IVector v = new Vector(vertex.getX(), vertex.getY(), vertex.getZ(), 1);
                        IVector tv = v.toRowMatrix(false).nMultiply(transformMatrix).toVector(false)
                                .nFromHomogeneous();
                        gl2.glVertex2d(tv.get(0), tv.get(1));
                    }
                    gl2.glEnd();
                }
            }

            public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
                frameWidth = width;
                frameHeight = height;
                GL2 gl2 = glAutoDrawable.getGL().getGL2();

                gl2.glMatrixMode(GL2.GL_PROJECTION);
                gl2.glLoadIdentity();

                gl2.glViewport(0, 0, width, height);
            }
        });
    }

    private void initKeyListener() {
        canvas.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_R || code == KeyEvent.VK_L) {
                    if (code == KeyEvent.VK_ESCAPE) {
                        controller.reset();
                    } else if (code == KeyEvent.VK_R) {
                        controller.increment();
                    } else {
                        controller.decrement();
                    }
                    canvas.display();
                }
            }
        });
    }

    private void initGUI() {
        setSize(frameWidth, frameHeight);
        setLocationRelativeTo(null);
        setTitle("Vježba 6 - Zad 6.2.3");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().add(canvas, BorderLayout.CENTER);
        setVisible(true);
        canvas.requestFocusInWindow();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Program expects one argument; a path to .obj file.");
            return;
        }
        if (!args[0].endsWith(".obj")) {
            System.out.println("Program accepts only .obj files.");
            return;
        }
        Path file = Paths.get(args[0]);
        ObjectModel model = ObjectModel.readFromOBJ(file).normalize();
        SwingUtilities.invokeLater(() -> new Zad6_2_3(model));
    }

}
