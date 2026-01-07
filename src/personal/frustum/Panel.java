package personal.frustum;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class Panel extends JPanel {

    Referentiel ref;
    Form form;
    Point3D cameraPosition;

    public Panel(Referentiel ref, Form form, Point3D cameraPosition) {
        this.ref = ref;
        this.form = form; 
        this.cameraPosition = cameraPosition;
    }

    public Panel(Form form, Point3D cameraPosition) {
        this.ref = new Referentiel(0, 0);
        this.form = form; 
        this.cameraPosition = cameraPosition;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);

        List<Point3D> projection = new ArrayList<>();

        List<Point3D> points = form.getPoints();

        for (Point3D point : points) {
            if (isPointVisible(point)) {
                Point3D newPoint3D = point.substract(cameraPosition);
                projection.add(newPoint3D.perspective());
            }
        }

        Integer[][] visibilityGraph = form.getGraph();

        for (int i = 0; i < form.getNumberOfPoints(); i++) {
            for (int j = i+1; j< form.getNumberOfPoints(); j++ ) {
                if (visibilityGraph[i][j] == 1) {
                    g.drawLine(
                        ref.x() - projection.get(i).x(),
                        ref.y() - projection.get(i).y(),
                        ref.x() - projection.get(j).x(),
                        ref.y() - projection.get(j).y()
                        );
                }
            }
        }
    }

    private boolean isPointVisible(Point3D point) {
        return (cameraPosition.z() > point.z());
    }
}