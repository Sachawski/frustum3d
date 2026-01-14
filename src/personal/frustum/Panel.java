package personal.frustum;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class Panel extends JPanel {

    Referentiel ref;
    Form form;
    Camera camera;

    public Panel(Referentiel ref, Form form, Camera camera) {
        this.ref = ref;
        this.form = form; 
        this.camera = camera;
    }

    public Panel(Form form, Camera camera) {
        this.ref = new Referentiel(0, 0);
        this.form = form; 
        this.camera = camera;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);

        List<Point3D> projection = new ArrayList<>();

        Point3D cameraPosition = new Point3D(camera.getPosX(), camera.getPosY(), camera.getPosZ());

        // for (Point3D point : form.getPoints()) {
        //     if (isPointVisible(point)) {
        //         Point3D newPoint3D = point.substract(cameraPosition);
        //         projection.add(newPoint3D.perspective());
        //     }
        // }

        Matrix mvpMatrix = camera.createMVPMatrix();

        for (Point3D point : form.getPoints()) {
            Point3D transformed = mvpMatrix.multiply(point);
            System.out.println("Transformed: " + transformed.x() + ", " + transformed.y() + ", " + transformed.z());
            projection.add(transformed);
        }


        Integer[][] visibilityGraph = form.getGraph();

        for (int i = 0; i < form.getNumberOfPoints(); i++) {
            for (int j = i+1; j< form.getNumberOfPoints(); j++ ) {
                if (visibilityGraph[i][j] == 1) {
                    int width = getWidth();
                    int height = getHeight();
                    
                    // Apply viewport transformation with scaling factor
                    double scale = Math.min(width, height) / 4.0;
                    
                    int screenX1 = (int) ref.x() + (int)(projection.get(i).x() * scale);
                    int screenY1 = (int) ref.y() + (int)(projection.get(i).y() * scale);
                    int screenX2 = (int) ref.x() + (int)(projection.get(j).x() * scale);
                    int screenY2 = (int) ref.y() + (int)(projection.get(j).y() * scale);
                    
                    g.drawLine(screenX1, screenY1, screenX2, screenY2);
                }
            }
        }
    }

    private boolean isPointVisible(Point3D point) {
        return (camera.getPosZ() > point.z());
    }

    private boolean isPointInFrustum(Point3D point) {
    // NDC coordinates should be within [-1, +1] range
    return point.x() >= -1 && point.x() <= 1 &&
           point.y() >= -1 && point.y() <= 1 &&
           point.z() >= 0 && point.z() <= 1;
    }
    

}