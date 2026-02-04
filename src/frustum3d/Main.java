package frustum3d;

import frustum3d.core.meshes.Mesh;
import frustum3d.core.view.Vertex;
import frustum3d.renderer.RendererPanel;
import frustum3d.renderer.RenderingType;
import frustum3d.scene.Camera;
import frustum3d.scene.CameraMovementManager;
import frustum3d.utils.CubeFactory;
import frustum3d.utils.Referentiel;

import javax.swing.*;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;



public class Main extends JFrame {

    public static void main(String[] args) {

        Rectangle rectangle = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int width = rectangle.width;
        int height = rectangle.height;

        JFrame frame = new JFrame("Lines");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(width,height);
        frame.setLocation(0,0);

        JPanel configPanel = new JPanel();
        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS)); // vertical layout for controls
        String[] choices = { 
            RenderingType.WIREFRAME.name(),
            RenderingType.FULLMESH.name()
        };
        final JComboBox<String> cb = new JComboBox<>(choices);
        
        configPanel.add(cb);

        Camera camera = new Camera(0, 500, -500, 180, 0, width, height);
        CameraMovementManager movementListener = new CameraMovementManager(camera);
        movementListener.driveCamera();

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        Referentiel ref = new Referentiel((double) width /2, (double) height /2);
        double cubeSize = 1000;
        CubeFactory cubeFactory = new CubeFactory(cubeSize);

        List<Mesh> forms = new ArrayList<>();
        for (int i = -10; i < 10; i++) {
            for (int j = -10; j < 10; j++) {
                for(int k = -10; k < 10; k++) {
                    forms.add(cubeFactory.buildAt(new Vertex(i * cubeSize, j * cubeSize, k * cubeSize)));
                }
//                forms.add(cubeFactory.buildAt(new Vertex(i * cubeSize, 0, j * cubeSize)));
            }
        }
         // forms.add(cubeFactory.buildAt(new Vertex(0,0,0)));
        

        RendererPanel renderer = new RendererPanel(ref, forms, camera, width, height);
        renderer.setBackground(Color.LIGHT_GRAY);
        renderer.addKeyListener(movementListener);
        renderer.addMouseMotionListener(movementListener);
        renderer.addMouseListener(movementListener);
        renderer.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                renderer.requestFocusInWindow();
            }
        });

        cb.addActionListener(e -> {
            RenderingType selected = RenderingType.valueOf((String) cb.getSelectedItem()); // get the selected item
            switch (selected) {
                case RenderingType.WIREFRAME -> renderer.setRenderingType(RenderingType.WIREFRAME);
                case RenderingType.FULLMESH -> renderer.setRenderingType(RenderingType.FULLMESH);

            }
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, configPanel, renderer);
        splitPane.setDividerLocation(300); // initial width of the config panel
        splitPane.setBackground(Color.GRAY);
        frame.getContentPane().add(splitPane);

        frame.setVisible(true);

        new Timer(16, e -> renderer.repaint()).start(); // ~60 FPS

    }

}
