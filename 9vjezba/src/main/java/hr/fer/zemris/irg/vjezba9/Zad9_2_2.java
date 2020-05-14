package hr.fer.zemris.irg.vjezba9;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import hr.fer.zemris.irg.vjezba9.linearna.IMatrix;
import hr.fer.zemris.irg.vjezba9.linearna.IRG;
import hr.fer.zemris.irg.vjezba9.linearna.IVector;
import hr.fer.zemris.irg.vjezba9.linearna.Vector;
import hr.fer.zemris.irg.vjezba9.model.Face3D;
import hr.fer.zemris.irg.vjezba9.model.ObjectModel;
import hr.fer.zemris.irg.vjezba9.model.Vertex3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.awt.event.KeyEvent.*;

public class Zad9_2_2 extends JFrame {

    static {
        GLProfile.initSingleton();
    }

    private final GLCanvas canvas = new GLCanvas(new GLCapabilities(GLProfile.getDefault()));
    private final ObjectModel model;
    private final EyeController controller = new EyeController(3, 4, 1);
    private final Collection<Integer> keyCodes = List.of(VK_ESCAPE, VK_L, VK_R, VK_1, VK_2, VK_3,
            VK_4, VK_K, VK_G, VK_Z);

    private enum Algorithm {NONE, FIRST, SECOND, THIRD}

    private enum ShadingType {CONST, GOURAUD}

    private Algorithm algorithm = Algorithm.FIRST;
    private ShadingType type = ShadingType.CONST;
    private boolean depthTest;

    private final IVector lightPosition = new Vector(4, 5, 3);
    private final IVector lightAmbient = new Vector(0.2, 0.2, 0.2);
    private final IVector lightDiffuse = new Vector(0.8, 0.8, 0);
    private final IVector lightSpecular = new Vector(0, 0, 0);

    private final IVector materialAmbient = new Vector(1, 1, 1);
    private final IVector materialDiffuse = new Vector(1, 1, 1);
    private final IVector materialSpecular = new Vector(0.01, 0.01, 0.01);
    private final float shininess = 96f;

    private final IVector ambientComponent = multiplyComponentWise(lightAmbient, materialAmbient);
    private final IVector diffuseComponent = multiplyComponentWise(lightDiffuse, materialDiffuse);
    private final IVector specularComponent = multiplyComponentWise(lightSpecular, materialSpecular);

    public Zad9_2_2(ObjectModel model) {
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
                gl2.glDisable(GL2.GL_CULL_FACE);

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
                Face3D[] faces = model.getFaces();
                for (Face3D face : faces) {
                    if (algorithm == Algorithm.FIRST || algorithm == Algorithm.SECOND) {
                        if (!face.isVisible()) {
                            continue;
                        }
                    }
                    Vertex3D[] vertices = model.findVerticesForFace(face);
                    // transform vertices using custom model-view matrix
                    IVector[] vertices3D = Arrays.stream(vertices)
                            .map(v -> new Vector(v.getX(), v.getY(), v.getZ(), 1))
                            .map(v -> v.toRowMatrix(false).nMultiply(transformMatrix)
                                    .toVector(false).nFromHomogeneous())
                            .toArray(IVector[]::new);
                    if (algorithm == Algorithm.THIRD) {
                        if (!IRG.isAntiClockwise(vertices3D[0], vertices3D[1], vertices3D[2])) {
                            continue;
                        }
                    }
                    if (type == ShadingType.CONST) {
                        Vertex3D position = Vertex3D.center(vertices[0], vertices[1], vertices[2]);
                        IVector vPos = new Vector(position.getX(), position.getY(), position.getZ());
                        float[] color = calculateShading(face.normal(), vPos);
                        gl2.glColor3f(color[0], color[1], color[2]);
                    }
                    gl2.glBegin(GL2.GL_POLYGON);
                    for (int i = 0; i < vertices3D.length; i++) {
                        Vertex3D vertex = vertices[i];
                        if (type == ShadingType.GOURAUD) {
                            IVector vPos = new Vector(vertex.getX(), vertex.getY(), vertex.getZ());
                            float[] color = calculateShading(vertex.getNormal(), vPos);
                            gl2.glColor3f(color[0], color[1], color[2]);
                        }
                        IVector v = vertices3D[i];
                        gl2.glVertex3d(v.get(0), v.get(1), v.get(2));
                    }
                    gl2.glEnd();
                }
            }

            private float[] calculateShading(IVector n, IVector position) {
                IVector Ig = ambientComponent.copy();

                IVector nNormalized = n.nNormalize();
                IVector l = lightPosition.nSub(position).normalize();
                double scalarProduct = l.scalarProduct(nNormalized);
                IVector Id = diffuseComponent.nScalarMultiply(Math.max(scalarProduct, 0));

                IVector eye = controller.eye();
                IVector v = eye.nSub(position).normalize();
                IVector r = n.nScalarMultiply(2 * scalarProduct / (l.norm() * l.norm()))
                        .sub(l)
                        .normalize();
                IVector Is = specularComponent.nScalarMultiply(Math.pow(r.scalarProduct(v), shininess));

                IVector I = Ig.add(Id).add(Is);
                return new float[]{(float) I.get(0), (float) I.get(1), (float) I.get(2)};
            }

            public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
                GL2 gl2 = glAutoDrawable.getGL().getGL2();

                gl2.glMatrixMode(GL2.GL_PROJECTION);
                gl2.glLoadIdentity();

                gl2.glViewport(0, 0, width, height);
            }
        });
    }

    private IVector multiplyComponentWise(IVector v1, IVector v2) {
        IVector result = v1.newInstance(v1.getDimension());
        for (int i = 0; i < result.getDimension(); i++) {
            result.set(i, v1.get(i) * v2.get(i));
        }
        return result;
    }

    private void initKeyListener() {
        canvas.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (keyCodes.contains(code)) {
                    if (code == VK_R) {
                        controller.increment();
                    } else if (code == VK_L) {
                        controller.decrement();
                    } else if (code == VK_1) {
                        System.out.println("Bez odbacivanja");
                        algorithm = Algorithm.NONE;
                    } else if (code == VK_2) {
                        System.out.println("Odbacivanje algoritmom 1");
                        algorithm = Algorithm.FIRST;
                    } else if (code == VK_3) {
                        System.out.println("Odbacivanje algoritmom 2");
                        algorithm = Algorithm.SECOND;
                    } else if (code == VK_4) {
                        System.out.println("Odbacivanje algoritmom 3");
                        algorithm = Algorithm.THIRD;
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
        setTitle("Vježba 9 - Zad 9.2.2");
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
        SwingUtilities.invokeLater(() -> new Zad9_2_2(model));
    }

}
