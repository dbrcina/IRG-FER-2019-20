package hr.fer.zemris.irg.vjezba8;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import hr.fer.zemris.irg.linearna.IMatrix;
import hr.fer.zemris.irg.linearna.Matrix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Zad8 extends JFrame {

    static {
        GLProfile.initSingleton();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Zad8::new);
    }

    private final GLCanvas canvas = new GLCanvas(new GLCapabilities(GLProfile.getDefault()));
    private final List<Point> points = new ArrayList<>();
    private int indexOfClickedPoint = -1;
    private final int divs = 200;

    public Zad8() {
        initGUI();
        initGLEventListener();
        initKeyListener();
        initMouseListener();
    }

    private void initGUI() {
        setSize(640, 480);
        setLocationRelativeTo(null);
        setTitle("Vježba 8 - Bezierova krivulja");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().add(canvas, BorderLayout.CENTER);
        setVisible(true);
        canvas.requestFocusInWindow();
    }

    private void initKeyListener() {
        canvas.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_ESCAPE) {
                    System.out.println("Reset");
                    points.clear();
                    canvas.display();
                }
            }
        });
    }

    private void initMouseListener() {
        canvas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    points.add(e.getPoint());
                    canvas.display();
                }
            }

            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    Point pressed = e.getPoint();
                    for (int i = 0; i < points.size(); i++) {
                        Point p = points.get(i);
                        if (Math.abs(p.x - pressed.x) <= 20 && Math.abs(p.y - pressed.y) <= 20) {
                            indexOfClickedPoint = i;
                            break;
                        }
                    }
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    indexOfClickedPoint = -1;
                }
            }
        });
        canvas.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (indexOfClickedPoint > -1) {
                        points.set(indexOfClickedPoint, e.getPoint());
                        canvas.display();
                    }
                }
            }
        });
    }

    private void initGLEventListener() {
        canvas.addGLEventListener(new GLEventListener() {
            public void init(GLAutoDrawable glAutoDrawable) {
                System.out.println("Initialized");
            }

            public void dispose(GLAutoDrawable glAutoDrawable) {
                System.out.println("Disposed");
            }

            public void display(GLAutoDrawable glAutoDrawable) {
                GL2 gl2 = glAutoDrawable.getGL().getGL2();

                gl2.glClearColor(0, 1, 0, 0);
                gl2.glClear(GL2.GL_COLOR_BUFFER_BIT);

                gl2.glMatrixMode(GL2.GL_MODELVIEW);
                gl2.glLoadIdentity();

                if (points.isEmpty()) {
                    return;
                }

                renderScene(gl2);
            }

            public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
                GL2 gl2 = glAutoDrawable.getGL().getGL2();

                gl2.glMatrixMode(GL2.GL_PROJECTION);
                gl2.glLoadIdentity();
                GLU glu = GLU.createGLU(gl2);
                glu.gluOrtho2D(0, width, height, 0);

                gl2.glViewport(0, 0, width, height);
            }

            private void renderScene(GL2 gl2) {
                drawControlPolygon(gl2);
                drawBezier(gl2, Color.BLUE, points);
                drawBezier(gl2, Color.BLACK, interpolate());
            }

            private void drawControlPolygon(GL2 gl2) {
                gl2.glColor3f(1, 0, 0);
                gl2.glBegin(GL2.GL_LINE_STRIP);
                for (Point point : points) {
                    gl2.glVertex2d(point.x, point.y);
                }
                gl2.glEnd();
            }

            private void drawBezier(GL2 gl2, Color color, List<Point> points) {
                int n = points.size() - 1;
                int[] factors = computeFactors(n);
                gl2.glColor3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
                gl2.glBegin(GL2.GL_LINE_STRIP);
                for (int i = 0; i <= divs; i++) {
                    double t = 1.0 / divs * i;
                    Point p = new Point();
                    for (int j = 0; j <= n; j++) {
                        double b;
                        if (j == 0) {
                            b = factors[j] * Math.pow(1 - t, n);
                        } else if (j == n) {
                            b = factors[j] * Math.pow(t, n);
                        } else {
                            b = factors[j] * Math.pow(t, j) * Math.pow(1 - t, n - j);
                        }
                        p.x += b * points.get(j).x;
                        p.y += b * points.get(j).y;
                    }
                    gl2.glVertex2f(p.x, p.y);
                }
                gl2.glEnd();
            }

            private int[] computeFactors(int n) {
                int[] factors = new int[n + 1];
                int a = 1;
                for (int i = 1; i <= n + 1; i++) {
                    factors[i - 1] = a;
                    a = a * (n - i + 1) / i;
                }
                return factors;
            }

            private List<Point> interpolate() {
                int n = points.size() - 1;
                IMatrix P = new Matrix(n + 1, 2);
                for (int i = 0; i < P.getRowsCount(); i++) {
                    Point p = points.get(i);
                    P.set(i, 0, p.x);
                    P.set(i, 1, p.y);
                }
                IMatrix B = new Matrix(n + 1, n + 1);
                int[] factors = computeFactors(n);
                for (int i = 0; i < B.getRowsCount(); i++) {
                    double t = 1.0 / n * i;
                    for (int j = 0; j < B.getColsCount(); j++) {
                        if (j == 0) {
                            B.set(i, j, factors[j] * Math.pow(1 - t, n));
                        } else if (j == n) {
                            B.set(i, j, factors[j] * Math.pow(t, n));
                        } else {
                            B.set(i, j, factors[j] * Math.pow(t, j) * Math.pow(1 - t, n - j));
                        }
                    }
                }
                IMatrix R = B.nInvert().nMultiply(P);
                return Arrays.stream(R.toArray())
                        .map(ar -> new Point((int) ar[0], (int) ar[1]))
                        .collect(Collectors.toList());
            }
        });
    }

}
