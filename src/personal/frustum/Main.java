package personal.frustum;

import javax.swing.JFrame;

public class Main extends JFrame{

    public static void main(String[] args) {

        CameraManager3D movement = new CameraManager3D();
        int size = 1000;
        JFrame frame = new JFrame("Lines");
        frame.setSize(size,size);
        frame.setLocation(0,0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(movement);
        
        Referentiel ref = new Referentiel(size/2,size/2);

        while (true) {
            Cube cube = new Cube(
                new Point3D(500, 1000, -500),
                new Point3D(-500, 1000, -500),
                new Point3D(-500, 0, -500),
                new Point3D(500, 0, -500),
                new Point3D(500, 1000, -1500),
                new Point3D(-500, 1000, -1500),
                new Point3D(-500, 0, -1500),
                new Point3D(500, 0, -1500)
            );


            Point3D position = new Point3D(movement.posX, movement.posY, movement.posZ);

            Panel panel = new Panel(ref, cube, position);

            frame.setContentPane(panel);

            frame.setVisible(true);
            frame.repaint(); 
        }
    }

}
