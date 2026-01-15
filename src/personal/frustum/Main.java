package personal.frustum;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.JFrame;


public class Main extends JFrame{

    public static void main(String[] args) {

        Camera camera = new Camera(0,500,-500,180,0);
        JFrame frame = new JFrame("Lines");
        Rectangle rectangle = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int height = rectangle.height;
        int width = rectangle.width;
        frame.setSize(width,height);
        frame.setLocation(0,0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(camera);
        frame.addMouseMotionListener(camera);
        
        Referentiel ref = new Referentiel(width/2,height/2);

        while (true) {
            Cube cube1 = new Cube(
                new Point3D(500, 1000, -2000),
                new Point3D(-500, 1000, -2000),
                new Point3D(-500, 0, -2000),
                new Point3D(500, 0, -2000),
                new Point3D(500, 1000, -3000),
                new Point3D(-500, 1000, -3000),
                new Point3D(-500, 0, -3000),
                new Point3D(500, 0, -3000)
            );

            Cube cube2 = new Cube(
                new Point3D(500, 1000, -2000),
                new Point3D(1500, 1000, -2000),
                new Point3D(1500, 0, -2000),
                new Point3D(500, 0, -2000),
                new Point3D(500, 1000, -3000),
                new Point3D(1500, 1000, -3000),
                new Point3D(1500, 0, -3000),
                new Point3D(500, 0, -3000)
            );

            List<Form> forms = List.of(cube1,cube2);

            Panel panel1 = new Panel(ref, forms, camera);


            frame.setContentPane(panel1);
            frame.setVisible(true);
            frame.repaint(); 
        }
    }

}
