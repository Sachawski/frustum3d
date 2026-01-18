package personal.frustum;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;


public class Main extends JFrame{

    public static void main(String[] args) {

        JFrame frame = new JFrame("Lines");
        Rectangle rectangle = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int width = rectangle.width;
        int height = rectangle.height;
        Camera camera = new Camera(0, 500, -500, 180, 0, width, height);
        CameraMovementManager movementListener = new CameraMovementManager(camera);
        movementListener.driveCamera();
        frame.setSize(width,height);
        frame.setLocation(0,0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(movementListener);
        frame.addMouseMotionListener(movementListener);
        frame.addMouseListener(movementListener);
        
        Referentiel ref = new Referentiel(width/2,height/2);
        double cubeSize = 1000;
        CubeFactory cubeFactory = new CubeFactory(cubeSize);

        List<Mesh> forms = new ArrayList<>();
        for (int i = -50; i < 50; i++) {
            for (int j = -50; j < 50; j++) {
                forms.add(cubeFactory.buildAt(new Point3D(2*i*cubeSize,0,2*j*cubeSize)));
            }
        }
        // forms.add(cubeFactory.buildAt(new Point3D(0,0,0)));

        while (true) {
            
            Renderer renderer = new Renderer(ref, forms, camera);

            frame.setContentPane(renderer);
            frame.setVisible(true);
        }   
    }
}
