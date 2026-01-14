package personal.frustum;

import javax.swing.JFrame;

public class Main extends JFrame{

    public static void main(String[] args) {

        Camera camera = new Camera(0,500,-500,0,0);
        int size = 1000;
        JFrame frame = new JFrame("Lines");
        frame.setSize(size,size);
        frame.setLocation(0,0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(camera);
        frame.addMouseMotionListener(camera);
        
        Referentiel ref = new Referentiel(size/2,size/2);

        while (true) {
            Cube cube = new Cube(
                new Point3D(500, 1000, -2000),
                new Point3D(-500, 1000, -2000),
                new Point3D(-500, 0, -2000),
                new Point3D(500, 0, -2000),
                new Point3D(500, 1000, -3000),
                new Point3D(-500, 1000, -3000),
                new Point3D(-500, 0, -3000),
                new Point3D(500, 0, -3000)
            );

            Panel panel = new Panel(ref, cube, camera);

            frame.setContentPane(panel);

            frame.setVisible(true);
            frame.repaint(); 
        }
    }

}
