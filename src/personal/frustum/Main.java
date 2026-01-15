package personal.frustum;

import java.util.List;
import javax.swing.JFrame;

public class Main extends JFrame{

    public static void main(String[] args) {

        Camera camera = new Camera(0,500,-500,180,0);
        int size = 1000;
        JFrame frame = new JFrame("Lines");
        frame.setSize(size,size);
        frame.setLocation(0,0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(camera);
        frame.addMouseMotionListener(camera);
        
        Referentiel ref = new Referentiel(size/2,size/2);

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
                new Point3D(2000, 1000, -2000),
                new Point3D(1000, 1000, -2000),
                new Point3D(1000, 0, -2000),
                new Point3D(2000, 0, -2000),
                new Point3D(2000, 1000, -3000),
                new Point3D(1000, 1000, -3000),
                new Point3D(1000, 0, -3000),
                new Point3D(2000, 0, -3000)
            );

            List<Form> forms = List.of(cube1,cube2);

            Panel panel1 = new Panel(ref, forms, camera);


            frame.setContentPane(panel1);
            frame.setVisible(true);
            frame.repaint(); 
        }
    }

}
