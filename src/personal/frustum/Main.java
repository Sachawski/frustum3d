package personal.frustum;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.Timer;
import personal.frustum.core.Mesh;
import personal.frustum.core.Point3D;
import personal.frustum.renderer.RendererPanel;
import personal.frustum.scene.Camera;
import personal.frustum.scene.CameraMovementManager;
import personal.frustum.utils.CubeFactory;
import personal.frustum.utils.Referentiel;


public class Main extends JFrame{

    public static void main(String[] args) {

        Rectangle rectangle = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int width = rectangle.width;
        int height = rectangle.height;

        JFrame frame = new JFrame("Lines");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width,height);
        frame.setLocation(0,0);

        JPanel configPanel = new JPanel();
        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS)); // vertical layout for controls
        String[] choices = {"Wireframe","Full"};
        final JComboBox<String> cb = new JComboBox<>(choices);
        configPanel.add(cb);

        Camera camera = new Camera(0, 500, -500, 180, 0, width, height);
        CameraMovementManager movementListener = new CameraMovementManager(camera);
        movementListener.driveCamera();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Referentiel ref = new Referentiel(width/2,height/2);
        double cubeSize = 1000;
        CubeFactory cubeFactory = new CubeFactory(cubeSize);

        List<Mesh> forms = new ArrayList<>();
        for (int i = -50; i < 50; i++) {
            for (int j = -50; j < 50; j++) {
                forms.add((Mesh) cubeFactory.buildAt(new Point3D(2*i*cubeSize,0,2*j*cubeSize)));
            }
        }
        // forms.add(cubeFactory.buildAt(new Point3D(0,0,0)));
        

        RendererPanel renderer = new RendererPanel(ref, forms, camera);
        renderer.addKeyListener(movementListener);
        renderer.addMouseMotionListener(movementListener);
        renderer.addMouseListener(movementListener);
        renderer.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                renderer.requestFocusInWindow();
            }
        });

        // Use JSplitPane for resizable panels side by side
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, configPanel, renderer);
        splitPane.setDividerLocation(300); // initial width of the config panel
        frame.getContentPane().add(splitPane);

        frame.setVisible(true);

        new Timer(16, e -> renderer.repaint()).start(); // ~60 FPS

    }
}
