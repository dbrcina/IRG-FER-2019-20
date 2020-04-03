package hr.fer.zemris.irg.ciklus2;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import hr.fer.zemris.irg.ciklus2.listener.GLEventListenerImpl;
import hr.fer.zemris.irg.ciklus2.listener.ScreenModelListener;
import hr.fer.zemris.irg.ciklus2.model.ScreenModel;

import javax.swing.*;
import java.awt.*;
import java.util.StringJoiner;

public class Zad4 extends JFrame implements ScreenModelListener {

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
    private JLabel info = new JLabel("Stanje: 1, Konveksnost: false, Popunjavanje: false");

    public Zad4() {
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
        canvas.addKeyListener(model);
        canvas.addMouseListener(model);
        canvas.addMouseMotionListener(model);
    }

    private void initGUI() {
        setSize(frameWidth, frameHeight);
        setLocationRelativeTo(null);
        setTitle("Zad3 - Polygons");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        info.setHorizontalAlignment(SwingConstants.RIGHT);
        info.setBackground(Color.WHITE);
        info.setOpaque(true);
        getContentPane().add(info, BorderLayout.PAGE_START);
        getContentPane().add(canvas, BorderLayout.CENTER);
        setVisible(true);
        canvas.requestFocusInWindow();
    }

    @Override
    public void modelUpdated() {
        StringJoiner sj = new StringJoiner(", ");
        sj.add("Stanje: " + (model.isStateDrawPolygon() ? "1" : "2"));
        sj.add("Konveksnost: " + model.isConvex());
        sj.add("Popunjavanje: " + model.isFill());
        info.setText(sj.toString());
        canvas.display();
    }

    public static void setFrameWidth(int frameWidth) {
        Zad4.frameWidth = frameWidth;
    }

    public static void setFrameHeight(int frameHeight) {
        Zad4.frameHeight = frameHeight;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Zad4::new);
    }

}
