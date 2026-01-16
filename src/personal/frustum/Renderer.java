package personal.frustum;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class Renderer extends JPanel {

    Referentiel ref;
    List<Form> forms;
    Camera camera;

    public Renderer(Referentiel ref, List<Form> forms, Camera camera) {
        this.ref = ref;
        this.forms = forms; 
        this.camera = camera;
    }

    public Renderer(List<Form> forms, Camera camera) {
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

            if (form instanceof Cube cube) {
                int[][] edges = form.getEdges();
                for (int edgeIndex = 0; edgeIndex < edges.length; edgeIndex++) {
                    int edgeFirstPointIndex = edges[edgeIndex][0];
                    int edgeSecondPointIndex = edges[edgeIndex][1];
                    int[] facesForEdge = cube.getEdgesFacesMap().get(edgeIndex);
                    
                    boolean edgeVisible = false;
                    for (int faceIndex : facesForEdge) {
                        int[] faceVertices = cube.getFaceVertices(faceIndex);
                        if (camera.isFaceFrontFacing(faceVertices, projection.toArray(new Point3D[0]))) {
                            edgeVisible = true;
                            break; // At least one face is front-facing, render edge
                        }
                        
                    }  

                    if (!edgeVisible) {
                        continue; // Skip rendering back-face-only edges
                    } 

                    Point3D p1 = projection.get(edgeFirstPointIndex);
                    Point3D p2 = projection.get(edgeSecondPointIndex);
                    
                    if (camera.isPointBehindCamera(p1) && camera.isPointBehindCamera(p2)) {
                        continue;
                    }
                    
                    Point3D[] clipped = camera.clipLine(p1, p2);
                    if (clipped != null && clipped.length == 2) {
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