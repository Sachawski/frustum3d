package personal;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class Panel extends JPanel {

    List<Point3D> points;
    Point3D position;

    public Panel(List<Point3D> points, Point3D position) {
        this.points = points; 
        this.position = position;
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);


        List<Point3D> projection = new ArrayList<>();

        for (Point3D point : points) {
            Point3D newPoint3D = position.add(point);
            projection.add(newPoint3D.perspective());
        }
        
        for (int i = 0; i < points.size()-1; i++) {
            for (int j = i+1; j< points.size(); j++ ) {
                g.drawLine(
                    projection.get(i).x() + position.x(),
                    projection.get(i).y() + position.y(),
                    projection.get(j).x() + position.x(),
                    projection.get(j).y() + position.y()
                    );
            }
        }
    }
}