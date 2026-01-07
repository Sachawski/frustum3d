package personal;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class Panel extends JPanel {

    Referentiel ref;
    Form form;
    Point3D position;

    public Panel(Referentiel ref, Form form, Point3D position) {
        this.ref = ref;
        this.form = form; 
        this.position = position;
    }

    public Panel(Form form, Point3D position) {
        this.ref = new Referentiel(0, 0);
        this.form = form; 
        this.position = position;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);

        List<Point3D> projection = new ArrayList<>();

        List<Point3D> points = form.getPoints();

        for (Point3D point : points) {
            Point3D newPoint3D = position.add(point);
            projection.add(newPoint3D.perspective());
        }

        Integer[][] visibilityGraph = form.getGraph();



        for (int i = 0; i < form.getNumberOfPoints(); i++) {
            for (int j = i+1; j< form.getNumberOfPoints(); j++ ) {
                if (visibilityGraph[i][j] == 1) {
                    g.drawLine(
                        projection.get(i).x() + ref.x(),
                        projection.get(i).y() + ref.y(),
                        projection.get(j).x() + ref.x(),
                        projection.get(j).y() + ref.y()
                        );
                }
            }
        }

        for (int i = 0; i < form.getNumberOfPoints()/2; i++) {
            g.setColor(Color.PINK);
            if (i == 1) {
                g.setColor(Color.RED);
            }
            if (i == 2) {
                g.setColor(Color.GREEN);
            }
            if (i == 3) {
                g.setColor(Color.BLUE);
            }
            g.drawLine(
                projection.get(i).x() + ref.x(),
                projection.get(i).y() + ref.y(),
                position.x(),
                position.y()
            );
        
        }
    }
}