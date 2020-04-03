package hr.fer.zemris.irg;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import hr.fer.zemris.irg.listener.GLEventListenerImpl;
import hr.fer.zemris.irg.listener.ScreenModelListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Zad2 extends JFrame implements ScreenModelListener {

    static {
        GLProfile.initSingleton();
    }

    private GLCanvas canvas;
    private ScreenModel model;
    private int frameWidth;
    private int frameHeight;

    public Zad2() {
        initGLCanvas();
        initModel();
        initListeners();
        initGUI();
    }

    private void initGLCanvas() {
        GLProfile profile = GLProfile.getDefault();
        GLCapabilities capabilities = new GLCapabilities(profile);
        canvas = new GLCanvas(capabilities);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameWidth = (int) screenSize.getWidth() / 2;
        frameHeight = (int) screenSize.getHeight() / 2;
    }

    private void initModel() {
        model = new ScreenModel();
        model.addListener(this);
    }

    private void initListeners() {
        canvas.addGLEventListener(new GLEventListenerImpl(model));
        initKeyListener();
        initMouseListeners();
    }

    private void initKeyListener() {
        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                e.consume();
                if (e.getKeyCode() == KeyEvent.VK_N) {
                    model.prepareNextColor();
                }
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    model.preparePreviousColor();
                }
            }
        });
    }

    private void initMouseListeners() {
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.mouseClicked(e.getPoint());
            }
        });
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                model.mouseMoved(e.getPoint());
            }
        });
    }

    private void initGUI() {
        setSize(frameWidth, frameHeight);
        setLocationRelativeTo(null);
        setTitle("Zad2 - Filled triangles");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().add(canvas, BorderLayout.CENTER);
        setVisible(true);
        canvas.requestFocusInWindow();
    }

    @Override
    public void colorChanged() {
        canvas.display();
    }

    @Override
    public void pointMoved() {
        canvas.display();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Zad2::new);
    }

}
