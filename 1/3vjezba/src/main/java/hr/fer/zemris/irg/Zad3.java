package hr.fer.zemris.irg;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import hr.fer.zemris.irg.model.ScreenModel;
import hr.fer.zemris.irg.listener.GLEventListenerImpl;
import hr.fer.zemris.irg.listener.ScreenModelListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Zad3 extends JFrame implements ScreenModelListener {

    private static int frameWidth;
    private static int frameHeight;

    static {
        GLProfile.initSingleton();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameWidth = (int) screenSize.getWidth() / 2;
        frameHeight = (int) screenSize.getHeight() / 2;
    }

    private GLCanvas canvas;
    private ScreenModel model;

    public Zad3() {
        initGLCanvas();
        initModel();
        initListeners();
        initGUI();
    }

    private void initGLCanvas() {
        GLProfile profile = GLProfile.getDefault();
        GLCapabilities capabilities = new GLCapabilities(profile);
        canvas = new GLCanvas(capabilities);
    }

    private void initModel() {
        model = new ScreenModel();
        model.addListener(this);
    }

    private void initListeners() {
        canvas.addGLEventListener(new GLEventListenerImpl(model));
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.mouseClicked(new Point(e.getX(), frameHeight - e.getY()));
            }
        });
        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                e.consume();
                if (e.getKeyCode() == KeyEvent.VK_K) model.toggleControl();
                if (e.getKeyCode() == KeyEvent.VK_O) model.toggleClip();
            }
        });
    }

    private void initGUI() {
        setSize(frameWidth, frameHeight);
        setLocationRelativeTo(null);
        setTitle("Zad3 - Bresenham's line algorithm ");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().add(canvas, BorderLayout.CENTER);
        setVisible(true);
        canvas.requestFocusInWindow();
    }

    @Override
    public void lineDefined() {
        canvas.display();
    }

    @Override
    public void keyPressed() {
        canvas.display();
    }

    public static void setFrameWidth(int frameWidth) {
        Zad3.frameWidth = frameWidth;
    }

    public static void setFrameHeight(int frameHeight) {
        Zad3.frameHeight = frameHeight;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Zad3::new);
    }

}
