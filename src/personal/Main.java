package personal;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

public class Main extends JFrame{

    public static void main(String[] args) {

        MovementManager3D movement = new MovementManager3D();
        int size = 1000;
        JFrame frame = new JFrame("Lines");
        frame.setSize(size,size);
        frame.setLocation(0,0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(movement);

        while (true) {
            List<Point3D> points = new ArrayList();
            points.add(new Point3D(0, 0, 0));
            points.add(new Point3D(400, 0, 0));
            points.add(new Point3D(400, 400, 0));
            points.add(new Point3D(0, 400, 0));
            points.add(new Point3D(0, 0, 1));
            points.add(new Point3D(400, 0, 1));
            points.add(new Point3D(400, 400, 1));
            points.add(new Point3D(0, 400, 1));

            Point3D position = new Point3D(movement.posX + size/2 ,movement.posY + size/2 , movement.posZ);

            Panel panel = new Panel(points, position);

            frame.setContentPane(panel);

            frame.setVisible(true);
            frame.repaint(); 
        }
    }

}
