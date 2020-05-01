package hr.fer.zemris.irg.vjezba7;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import hr.fer.zemris.irg.linearna.IMatrix;
import hr.fer.zemris.irg.linearna.IRG;
import hr.fer.zemris.irg.linearna.IVector;
import hr.fer.zemris.irg.linearna.Vector;
import hr.fer.zemris.irg.vjezba7.model.Face3D;
import hr.fer.zemris.irg.vjezba7.model.ObjectModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;

public class Zad7_2_2 extends JFrame {

    static {
        GLProfile.initSingleton();
    }

    private static int frameWidth = 640;
    private static int frameHeight = 480;

    private final GLCanvas canvas = new GLCanvas(new GLCapabilities(GLProfile.getDefault()));
    private final ObjectModel model;
    private final EyeController controller = new EyeController(3, 4, 1);

    private enum Algorithm {
        NONE, FIRST, SECOND, THIRD
    }

    private Algorithm algorithm = Algorithm.NONE;

    public Zad7_2_2(ObjectModel model) {
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
                if (algorithm == Algorithm.FIRST) {
                    model.determineFaceVisibilities1(eye);
                }
                if (algorithm == Algorithm.SECOND) {
                    model.determineFaceVisibilities2(eye);
                }
                IMatrix lookAtMatrix = IRG.lookAtMatrix(eye, new Vector(0, 0, 0), new Vector(0, 1, 0));
                IMatrix frustum = IRG.buildFrustumMatrix(-0.5, 0.5, -0.5, 0.5, 1, 100);

                renderScene(gl2, lookAtMatrix.nMultiply(frustum));
            }

            private void renderScene(GL2 gl2, IMatrix transformMatrix) {
                gl2.glColor3f(1, 0, 0);
                Face3D[] faces = model.getFaces();
                for (Face3D face : faces) {
                    if (algorithm == Algorithm.FIRST || algorithm == Algorithm.SECOND) {
                        if (!face.isVisible()) {
                            continue;
                        }
                    }
                    // transform vertices using custom model-view matrix
                    IVector[] vertices3D = Arrays.stream(model.findVerticesForFace(face))
                            .map(v -> new Vector(v.getX(), v.getY(), v.getZ(), 1))
                            .map(v -> v.toRowMatrix(false).nMultiply(transformMatrix)
                                    .toVector(false).nFromHomogeneous())
                            .toArray(IVector[]::new);
                    if (algorithm == Algorithm.THIRD) {
                        if (!IRG.isAntiClockwise(vertices3D[0], vertices3D[1], vertices3D[2])) {
                            continue;
                        }
                    }
                    gl2.glBegin(GL.GL_LINE_LOOP);
                    for (IVector v : vertices3D) {
                        gl2.glVertex2d(v.get(0), v.get(1));
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
                Set<Integer> codes = Set.of(KeyEvent.VK_ESCAPE, KeyEvent.VK_R, KeyEvent.VK_L,
                        KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4);
                int code = e.getKeyCode();
                if (codes.contains(code)) {
                    if (code == KeyEvent.VK_R) {
                        controller.increment();
                    } else if (code == KeyEvent.VK_L) {
                        controller.decrement();
                    } else if (code == KeyEvent.VK_1) {
                        System.out.println("Bez odbacivanja");
                        algorithm = Algorithm.NONE;
                    } else if (code == KeyEvent.VK_2) {
                        System.out.println("Odbacivanje algoritmom 1");
                        algorithm = Algorithm.FIRST;
                    } else if (code == KeyEvent.VK_3) {
                        System.out.println("Odbacivanje algoritmom 2");
                        algorithm = Algorithm.SECOND;
                    } else if (code == KeyEvent.VK_4) {
                        System.out.println("Odbacivanje algoritmom 3");
                        algorithm = Algorithm.THIRD;
                    } else {
                        controller.reset();
                    }
                    canvas.display();
                }
            }
        });
    }

    private void initGUI() {
        setSize(frameWidth, frameHeight);
        setLocationRelativeTo(null);
        setTitle("Vježba 7 - Zad 7.2.2");
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
        SwingUtilities.invokeLater(() -> new Zad7_2_2(model));
    }

}
