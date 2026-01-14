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

        Matrix mvpMatrix = camera.createMVPMatrix();

        for (Point3D point : form.getPoints()) {
            Point3D transformed = mvpMatrix.multiply(point);
            projection.add(transformed);
        }

        Integer[][] visibilityGraph = form.getGraph();

        for (int i = 0; i < form.getNumberOfPoints(); i++) {
            for (int j = i+1; j< form.getNumberOfPoints(); j++ ) {
                if (visibilityGraph[i][j] == 1) {
                    int width = getWidth();
                    int height = getHeight();
                    
                    double scale = Math.min(width, height) / 4.0;
                        
                    int screenX1 = (int) ref.x() + (int)(projection.get(i).getPosX() * scale);
                    int screenY1 = (int) ref.y() + (int)(projection.get(i).getPosY() * scale);
                    int screenX2 = (int) ref.x() + (int)(projection.get(j).getPosX() * scale);
                    int screenY2 = (int) ref.y() + (int)(projection.get(j).getPosY() * scale);
                        
                    g.drawLine(screenX1, screenY1, screenX2, screenY2);
                    }
                }
            }
        }

}