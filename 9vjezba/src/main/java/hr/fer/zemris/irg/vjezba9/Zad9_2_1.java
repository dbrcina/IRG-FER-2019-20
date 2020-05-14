package hr.fer.zemris.irg.vjezba9;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import hr.fer.zemris.irg.vjezba9.linearna.IVector;
import hr.fer.zemris.irg.vjezba9.model.Face3D;
import hr.fer.zemris.irg.vjezba9.model.ObjectModel;
import hr.fer.zemris.irg.vjezba9.model.Vertex3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Set;

import static java.awt.event.KeyEvent.*;

public class Zad9_2_1 extends JFrame {

    static {
        GLProfile.initSingleton();
    }

    private final GLCanvas canvas = new GLCanvas(new GLCapabilities(GLProfile.getDefault()));
    private final ObjectModel model;
    private final EyeController controller = new EyeController(3, 4, 1);

    private boolean depthTest;

    private enum ShadingType {CONST, GOURAUD}

    private ShadingType type = ShadingType.CONST;

    public Zad9_2_1(ObjectModel model) {
        this.model = model;
        initGUI();
        initGLEventListener();
        initKeyListener();
    }

    private void initGLEventListener() {
        canvas.addGLEventListener(new GLEventListener() {
            public void init(GLAutoDrawable glAutoDrawable) {
                System.out.println("Initialized.");
            }

            public void dispose(GLAutoDrawable glAutoDrawable) {
                System.out.println("Disposed.");
            }

            public void display(GLAutoDrawable glAutoDrawable) {
                GL2 gl2 = glAutoDrawable.getGL().getGL2();
                gl2.glClearColor(0, 1, 0, 0);
                gl2.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

                if (depthTest) {
                    gl2.glEnable(GL2.GL_DEPTH_TEST);
                } else {
                    gl2.glDisable(GL2.GL_DEPTH_TEST);
                }

                gl2.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);
                gl2.glEnable(GL2.GL_CULL_FACE);
                gl2.glCullFace(GL2.GL_BACK);

                gl2.glMatrixMode(GL2.GL_MODELVIEW);
                gl2.glLoadIdentity();
                IVector eye = controller.eye();
                GLU glu = GLU.createGLU(gl2);
                glu.gluLookAt(eye.get(0), eye.get(1), eye.get(2), 0, 0, 0, 0, 1, 0);

                gl2.glEnable(GL2.GL_LIGHTING);
                gl2.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[]{0, 0, 0, 1}, 0);
                gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float[]{4f, 5f, 3f, 1f}, 0);
                gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, new float[]{0.2f, 0.2f, 0.2f, 1f}, 0);
                gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[]{0.8f, 0.8f, 0f, 1f}, 0);
                gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, new float[]{0f, 0f, 0f, 1f}, 0);
                gl2.glEnable(GL2.GL_LIGHT0);

                if (type == ShadingType.CONST) {
                    gl2.glShadeModel(GL2.GL_FLAT);
                } else {
                    gl2.glShadeModel(GL2.GL_SMOOTH);
                }

                renderScene(gl2);
            }

            private void renderScene(GL2 gl2) {
                gl2.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, new float[]{1f, 1f, 1f, 1f}, 0);
                gl2.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float[]{1f, 1f, 1f, 1f}, 0);
                gl2.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.01f, 0.01f, 0.01f, 1f}, 0);
                gl2.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 96f);
                Face3D[] faces = model.getFaces();
                for (Face3D face : faces) {
                    Vertex3D[] vertices = model.findVerticesForFace(face);
                    gl2.glBegin(GL2.GL_POLYGON);
                    for (Vertex3D vertex : vertices) {
                        IVector normal;
                        if (type == ShadingType.CONST) {
                            normal = face.normal().normalize();
                        } else {
                            normal = vertex.getNormal().normalize();
                        }
                        gl2.glNormal3d(normal.get(0), normal.get(1), normal.get(2));
                        gl2.glVertex3d(vertex.getX(), vertex.getY(), vertex.getZ());
                    }
                    gl2.glEnd();
                }
            }

            public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
                GL2 gl2 = glAutoDrawable.getGL().getGL2();

                gl2.glMatrixMode(GL2.GL_PROJECTION);
                gl2.glLoadIdentity();
                gl2.glFrustum(-0.5, 0.5, -0.5, 0.5, 1, 100);

                gl2.glViewport(0, 0, width, height);
            }
        });
    }

    private void initKeyListener() {
        Collection<Integer> keyCodes = Set.of(VK_ESCAPE, VK_R, VK_L, VK_Z, VK_K, VK_G);
        canvas.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (keyCodes.contains(code)) {
                    if (code == VK_R) {
                        controller.increment();
                    } else if (code == VK_L) {
                        controller.decrement();
                    } else if (code == VK_Z) {
                        depthTest = !depthTest;
                        System.out.println("Z-spremnik je " + (depthTest ? "uključen." : "iskljucen."));
                    } else if (code == VK_K) {
                        type = ShadingType.CONST;
                        System.out.println("Upaljeno je konstantno sjenčanje.");
                    } else if (code == VK_G) {
                        type = ShadingType.GOURAUD;
                        System.out.println("Upaljeno je gouraudovo sjenčanje.");
                    } else {
                        controller.reset();
                    }
                    canvas.display();
                }
            }
        });
    }

    private void initGUI() {
        setSize(640, 480);
        setLocationRelativeTo(null);
        setTitle("Vježba 9 - Zad 9.2.1");
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
        SwingUtilities.invokeLater(() -> new Zad9_2_1(model));
    }

}
