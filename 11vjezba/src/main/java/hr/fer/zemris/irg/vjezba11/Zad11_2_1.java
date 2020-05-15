package hr.fer.zemris.irg.vjezba11;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Set;
import java.util.Stack;

import static java.awt.event.KeyEvent.*;

public class Zad11_2_1 extends JFrame {

    static {
        GLProfile.initSingleton();
    }

    private final GLCanvas canvas = new GLCanvas(new GLCapabilities(GLProfile.getDefault()));
    private final Collection<Integer> keyCodes = Set.of(VK_1, VK_2, VK_B, VK_C, VK_X, VK_ESCAPE);

    private int frameWidth = 640;
    private int frameHeight = 480;

    private final Stack<Double> stack = new Stack<>();
    private double uMin = -2;
    private double uMax = 1;
    private double vMin = -1.2;
    private double vMax = 1.2;

    private int maxLimit = 128;

    private boolean quadratic = true;
    private boolean blackAndWhite = true;

    public Zad11_2_1() {
        initGUI();
        initGLEventListener();
        initMouseListener();
        initKeyListener();
    }

    private void initGLEventListener() {
        canvas.addGLEventListener(new GLEventListener() {
            public void init(GLAutoDrawable glAutoDrawable) {
            }

            public void dispose(GLAutoDrawable glAutoDrawable) {
            }

            public void display(GLAutoDrawable glAutoDrawable) {
                GL2 gl2 = glAutoDrawable.getGL().getGL2();
                gl2.glClearColor(1, 1, 1, 0);
                gl2.glClear(GL2.GL_COLOR_BUFFER_BIT);

                gl2.glMatrixMode(GL2.GL_MODELVIEW);
                gl2.glLoadIdentity();

                renderScene(gl2);
            }

            private void renderScene(GL2 gl2) {
                gl2.glPointSize(1);
                gl2.glBegin(GL2.GL_POINTS);
                for (int y = 0; y < frameHeight; y++) {
                    for (int x = 0; x < frameWidth; x++) {
                        double[] uv = transformCoordinates(x, y);
                        Complex c = new Complex(uv[0], uv[1]);
                        int n = quadratic ? divergenceTest(c, maxLimit) : divergenceTest2(c, maxLimit);
                        if (blackAndWhite) {
                            colorScheme(gl2, n);
                        } else {
                            colorScheme2(gl2, n);
                        }
                        gl2.glVertex2d(x, y);
                    }
                }
                gl2.glEnd();
            }

            private void colorScheme(GL2 gl2, int n) {
                if (n == -1) {
                    gl2.glColor3f(0, 0, 0);
                } else {
                    gl2.glColor3f(1, 1, 1);
                }
            }

            private void colorScheme2(GL2 gl2, int n) {
                if (n == -1) {
                    gl2.glColor3f(0, 0, 0);
                } else if (maxLimit < 16) {
                    int r = (int) ((n - 1) / (double) (maxLimit - 1) * 255 + 0.5);
                    int g = 255 - r;
                    int b = ((n - 1) % (maxLimit / 2)) * 255 / (maxLimit / 2);
                    gl2.glColor3f(r / 255f, g / 255f, b / 255f);
                } else {
                    int lim = Math.min(maxLimit, 32);
                    int r = (n - 1) * 255 / lim;
                    int g = ((n - 1) % (lim / 4)) * 255 / (lim / 4);
                    int b = ((n - 1) % (lim / 8)) * 255 / (lim / 8);
                    gl2.glColor3f(r / 255f, g / 255f, b / 255f);
                }
            }

            private int divergenceTest(Complex c, int limit) {
                Complex z = Complex.ZERO;
                for (int i = 1; i <= limit; i++) {
                    z = z.power(2).add(c);
                    double module = z.module();
                    if (module * module > 4) {
                        return i;
                    }
                }
                return -1;
            }

            private int divergenceTest2(Complex c, int limit) {
                Complex z = Complex.ZERO;
                for (int i = 1; i <= limit; i++) {
                    z = z.power(3).add(c);
                    double module = z.module();
                    if (module * module > 4) {
                        return i;
                    }
                }
                return -1;
            }

            public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
                frameWidth = width;
                frameHeight = height;
                GL2 gl2 = glAutoDrawable.getGL().getGL2();

                gl2.glMatrixMode(GL2.GL_PROJECTION);
                gl2.glLoadIdentity();
                GLU glu = GLU.createGLU(gl2);
                glu.gluOrtho2D(0, width, height, 0);

                gl2.glViewport(0, 0, width, height);
            }
        });
    }

    private void initMouseListener() {
        canvas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                stack.push(uMin);
                stack.push(uMax);
                stack.push(vMin);
                stack.push(vMax);
                double[] uv = transformCoordinates(e.getX(), e.getY());
                double uLen = uMax - uMin;
                double newULen = 1.0 / 16 * uLen;
                double vLen = vMax - vMin;
                double newVLen = 1.0 / 16 * vLen;
                uMin = uv[0] - 0.5 * newULen;
                uMax = uv[0] + 0.5 * newULen;
                vMin = uv[1] - 0.5 * newVLen;
                vMax = uv[1] + 0.5 * newVLen;
                canvas.display();
            }
        });
    }

    private void initKeyListener() {
        canvas.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (keyCodes.contains(code)) {
                    if (code == VK_1) {
                        quadratic = true;
                        System.out.println("Odabrana je kvadratna funkcija");
                    } else if (code == VK_2) {
                        quadratic = false;
                        System.out.println("Odabrana je kubna funkcija");
                    } else if (code == VK_B) {
                        blackAndWhite = true;
                        System.out.println("Odabran je crno-bijeli prikaz");
                    } else if (code == VK_C) {
                        blackAndWhite = false;
                        System.out.println("Odabran je prikaz u boji");
                    } else if (code == VK_X) {
                        if (stack.isEmpty()) {
                            return;
                        }
                        refreshState();
                    } else {
                        if (stack.isEmpty()) {
                            return;
                        }
                        while (!stack.isEmpty()) {
                            refreshState();
                        }
                    }
                    canvas.display();
                }
            }
        });
    }

    private void refreshState() {
        vMax = stack.pop();
        vMin = stack.pop();
        uMax = stack.pop();
        uMin = stack.pop();
    }

    private void initGUI() {
        setSize(frameWidth, frameHeight);
        setLocationRelativeTo(null);
        setTitle("Vježba 11 - Zad 11.2.1");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().add(canvas, BorderLayout.CENTER);
        setVisible(true);
        canvas.requestFocusInWindow();
    }

    private double[] transformCoordinates(int x, int y) {
        double xMin = 0.0;
        double xMax = frameWidth - 1;
        double yMin = 0.0;
        double yMax = frameHeight - 1;
        double u = (x - xMin) / (xMax - xMin) * (uMax - uMin) + uMin;
        double v = (y - yMin) / (yMax - yMin) * (vMax - vMin) + vMin;
        return new double[]{u, v};
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Zad11_2_1::new);
    }

}
