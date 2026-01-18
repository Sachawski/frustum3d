package personal.frustum.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import personal.frustum.core.CubeMesh;
import personal.frustum.core.Mesh;
import personal.frustum.core.Point3D;
import personal.frustum.scene.Camera;
import personal.frustum.utils.Matrix;
import personal.frustum.utils.Referentiel;

public class RendererPanel extends JPanel {

    Referentiel ref;
    List<Mesh> forms;
    Camera camera;

    public RendererPanel(Referentiel ref, List<Mesh> forms, Camera camera) {
        setFocusable(true);
        this.ref = ref;
        this.forms = forms; 
        this.camera = camera;
    }

    public RendererPanel(List<Mesh> forms, Camera camera) {
        setFocusable(true);
        this.ref = new Referentiel(0, 0);
        this.forms = forms; 
        this.camera = camera;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        double width = getWidth();
        double height = getHeight();

        double scaleX = width / 2;
        double scaleY = height / 2;

        Matrix mvpMatrix = camera.createMVPMatrix();

        for (Mesh form : forms) {
            if (form instanceof CubeMesh cube) {
                List<Point3D> projection = new ArrayList<>();
                
                int[][] faces = cube.getFaces();

                // List<Triangle> triangles = new ArrayList<>();


                // for (int[] face : faces) {
                //     triangles.add(new Triangle(cube.getVertices().get(face[0]),
                //                                 cube.getVertices().get(face[1]),
                //                                 cube.getVertices().get(face[3])));
                //     triangles.add(new Triangle(cube.getVertices().get(face[1]),
                //                                 cube.getVertices().get(face[2]),
                //                                 cube.getVertices().get(face[3])));
                // }

                // triangles.stream().forEach(t -> {
                //     t.setP1(mvpMatrix.multiply(t.getP1()));
                //     t.setP2(mvpMatrix.multiply(t.getP2()));
                //     t.setP3(mvpMatrix.multiply(t.getP3()));
                // });

                // for (Triangle triangle : triangles) {
                //     Point3D p1 = triangle.getP1();
                //     Point3D p2 = triangle.getP2();
                //     Point3D p3 = triangle.getP3();

                //     int x1 = (int) (ref.x() + p1.getPosX() * scaleX);
                //     int y1 = (int) (ref.y() - p1.getPosY() * scaleY);
                //     int x2 = (int) (ref.x() + p2.getPosX() * scaleX);
                //     int y2 = (int) (ref.y() - p2.getPosY() * scaleY);
                //     int x3 = (int) (ref.x() + p3.getPosX() * scaleX);
                //     int y3 = (int) (ref.y() - p3.getPosY() * scaleY);

                //     // Ligne P1 -> P2
                //     g.setColor(Color.RED);
                //     g.drawLine(x1, y1, x2, y2);
                //     // Ligne P2 -> P3
                //     g.setColor(Color.GREEN);
                //     g.drawLine(x2, y2, x3, y3);
                //     // Ligne P3 -> P1
                //     g.setColor(Color.BLUE);
                //     g.drawLine(x3, y3, x1, y1);

                // }


                for (int i = 0; i < form.getVertices().size(); i++) {
                    Point3D point = form.getVertices().get(i);
                    Point3D transformed = mvpMatrix.multiply(point);
                    projection.add(transformed);
                }

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