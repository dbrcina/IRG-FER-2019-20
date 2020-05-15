package hr.fer.zemris.irg.vjezba11;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;

public class Zad11_2_2 extends JFrame {

    static {
        GLProfile.initSingleton();
    }

    private final GLCanvas canvas = new GLCanvas(new GLCapabilities(GLProfile.getDefault()));

    private int frameWidth = 720;
    private int frameHeight = 720;

    private final int pointsNumber;
    private final int limit;
    private final double[] xEtas;
    private final double[] yEtas;
    private final List<IFSFunction> ifsFunctions;
    private final double[] probabilities;
    private final Random rand = new Random();

    public Zad11_2_2(int pointsNumber, int limit, double[] xEtas, double[] yEtas,
                     List<IFSFunction> ifsFunctions) {
        this.pointsNumber = pointsNumber;
        this.limit = limit;
        this.xEtas = xEtas;
        this.yEtas = yEtas;
        this.ifsFunctions = ifsFunctions;
        probabilities = ifsFunctions.stream()
                .mapToDouble(IFSFunction::getProbability)
                .toArray();
        for (int i = 1; i < probabilities.length; i++) {
            probabilities[i] += probabilities[i - 1];
        }
        initGUI();
        initGLEventListener();
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
                gl2.glColor3f(0, 0.7f, 0.3f);
                gl2.glBegin(GL2.GL_POINTS);
                for (int i = 0; i < pointsNumber; i++) {
                    double x0 = 0;
                    double y0 = 0;
                    for (int j = 0; j < limit; j++) {
                        IFSFunction fun = ifsFunctions.get(rouletteWheel());
                        double x = fun.getA() * x0 + fun.getB() * y0 + fun.getE();
                        double y = fun.getC() * x0 + fun.getD() * y0 + fun.getF();
                        x0 = x;
                        y0 = y;
                    }
                    gl2.glVertex2i(round(x0 * xEtas[0] + xEtas[1]), round(y0 * yEtas[0] + yEtas[1]));
                }
                gl2.glEnd();
            }

            private int rouletteWheel() {
                double p = rand.nextDouble();
                for (int i = 0; i < probabilities.length - 1; i++) {
                    if (p < probabilities[i]) {
                        return i;
                    }
                }
                return probabilities.length - 1;
            }

            private int round(double d) {
                if (d >= 0) {
                    return (int) (d + 0.5);
                } else {
                    return (int) (d - 0.5);
                }
            }

            public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
                frameWidth = width;
                frameHeight = height;
                GL2 gl2 = glAutoDrawable.getGL().getGL2();

                gl2.glMatrixMode(GL2.GL_PROJECTION);
                gl2.glLoadIdentity();
                GLU glu = GLU.createGLU(gl2);
                glu.gluOrtho2D(0, width, 0, height);

                gl2.glViewport(0, 0, width, height);
            }
        });
    }

    private void initGUI() {
        setSize(frameWidth, frameHeight);
        setLocationRelativeTo(null);
        setTitle("Vježba 11 - Zad 11.2.2");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().add(canvas, BorderLayout.CENTER);
        setVisible(true);
        canvas.requestFocusInWindow();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            return;
        }
        Path file = Paths.get(args[0]);
        int pointsNumber;
        int limit;
        double[] xEtas;
        double[] yEtas;
        List<IFSFunction> ifsFunctions = new LinkedList<>();
        try (Scanner sc = new Scanner(Files.newInputStream(file))) {
            pointsNumber = Integer.parseInt(sc.nextLine().split("\\s+#\\s+")[0]);
            limit = Integer.parseInt(sc.nextLine().split("\\s+#\\s+")[0]);
            xEtas = Arrays.stream(sc.nextLine().split("\\s+#\\s+")[0].split("\\s+"))
                    .mapToDouble(Double::parseDouble)
                    .toArray();
            yEtas = Arrays.stream(sc.nextLine().split("\\s+#\\s+")[0].split("\\s+"))
                    .mapToDouble(Double::parseDouble)
                    .toArray();
            while (sc.hasNext()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                double[] params = Arrays.stream(line.split("\\s+"))
                        .mapToDouble(Double::parseDouble)
                        .toArray();
                ifsFunctions.add(new IFSFunction(
                        params[0], params[1], params[2], params[3], params[4], params[5], params[6]));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        ifsFunctions.sort(Comparator.comparingDouble(IFSFunction::getProbability));
        SwingUtilities.invokeLater(() -> new Zad11_2_2(pointsNumber, limit, xEtas, yEtas, ifsFunctions));
    }

}
