package personal.frustum;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class Panel extends JPanel {

    Referentiel ref;
    List<Form> forms;
    Camera camera;

    public Panel(Referentiel ref, List<Form> forms, Camera camera) {
        this.ref = ref;
        this.forms = forms; 
        this.camera = camera;
    }

    public Panel(List<Form> forms, Camera camera) {
        this.ref = new Referentiel(0, 0);
        this.forms = forms; 
        this.camera = camera;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);

        Matrix mvpMatrix = camera.createMVPMatrix();

        for (Form form : forms) {
            List<Point3D> projection = new ArrayList<>();


            for (int i = 0; i < form.getPoints().size(); i++) {
                Point3D point = form.getPoints().get(i);
                Point3D transformed = mvpMatrix.multiply(point);
                projection.add(transformed);
            }

            Integer[][] visibilityGraph = form.getGraph();
            
            int linesProcessed = 0;
            int linesClipped = 0;
            int linesBehindCamera = 0;

            for (int i = 0; i < form.getNumberOfPoints(); i++) {
                for (int j = i+1; j< form.getNumberOfPoints(); j++ ) {
                    if (visibilityGraph[i][j] == 1) {
                        linesProcessed++;
                        Point3D p1 = projection.get(i);
                        Point3D p2 = projection.get(j);
                        
                        if (camera.isPointBehindCamera(p1) && camera.isPointBehindCamera(p2)) {
                            linesBehindCamera++;
                            continue;
                        }
                        
                        Point3D[] clipped = camera.clipLine(p1, p2);
                        if (clipped != null && clipped.length == 2) {
                            linesClipped++;
                            double width = getWidth();
                            double height = getHeight();

                            double scaleX = width / 2;
                            double scaleY = height / 2;
                            
                            int screenX1 = (int) ref.x() + (int)(clipped[0].getPosX() * scaleX);
                            int screenY1 = (int) ref.y() - (int)(clipped[0].getPosY() * scaleY);
                            int screenX2 = (int) ref.x() + (int)(clipped[1].getPosX() * scaleX);
                            int screenY2 = (int) ref.y() - (int)(clipped[1].getPosY() * scaleY);
                            
                            g.drawLine(screenX1, screenY1, screenX2, screenY2);
                        }
                    }
                }
            }
        }
    }

    
}