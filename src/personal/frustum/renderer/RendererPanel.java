package personal.frustum.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import javax.swing.JPanel;
import personal.frustum.core.Line;
import personal.frustum.core.Mesh;
import personal.frustum.scene.Camera;
import personal.frustum.utils.Referentiel;

public class RendererPanel extends JPanel {

    Referentiel ref;
    List<Mesh> forms;
    RenderingEngine renderingEngine;
    RenderingType renderingType;


    public RendererPanel(Referentiel ref, List<Mesh> forms, Camera camera, double width, double height) {
        this.forms = forms; 
        this.renderingEngine = new RenderingEngine(
            ref,
            width,
            height,
            camera
        );
        this.renderingType = RenderingType.WIREFRAME;
    }

    public RendererPanel(List<Mesh> forms, Camera camera, double width, double height) {
        this.forms = forms; 
        this.renderingEngine = new RenderingEngine(
            new Referentiel(0, 0),
            width,
            height,
            camera
        );
        this.renderingType = RenderingType.WIREFRAME;
    }

    public void setRenderingType(RenderingType newRenderingType) {
        this.renderingType = newRenderingType;
    }

    @Override
    public void paintComponent(Graphics g) {
        switch (this.renderingType) {
            case RenderingType.FULLMESH -> fullMeshRendering(g);
            case RenderingType.WIREFRAME -> wireFrameRendering(g);
        }
            
    }

    private void wireFrameRendering(Graphics g) {
        List<Line> lines = renderingEngine.wireFrameRendering(forms);
        g.setColor(Color.BLACK);
        for (Line line : lines) {
            g.drawLine(
                line.pixel1().x(), 
                line.pixel1().y(), 
                line.pixel2().x(),
                line.pixel2().y()
            );
        }
    }

    private void fullMeshRendering(Graphics g) {
        List<Line> lines = renderingEngine.fullMeshRendering(forms);
        g.setColor(Color.BLACK);
        for (Line line : lines) {
            g.drawLine(
                line.pixel1().x(), 
                line.pixel1().y(), 
                line.pixel2().x(),
                line.pixel2().y()
            );
        }
    }

    // private void wireFrameRendering(Graphics g) {
    //     g.setColor(Color.BLACK);
    //     double width = getWidth();
    //     double height = getHeight();

    //     double scaleX = width / 2;
    //     double scaleY = height / 2;
    //     Matrix mvpMatrix = camera.createMVPMatrix();

    //     for (Mesh form : forms) {
    //         if (form instanceof CubeMesh cube) {
    //             List<Vertex> projection = new ArrayList<>();
                
    //             for (int i = 0; i < form.getVertices().size(); i++) {
    //                 Vertex point = form.getVertices().get(i);
    //                 Vertex transformed = mvpMatrix.multiply(point);
    //                 projection.add(transformed);
    //             }

    //             int[][] edges = form.getEdges();
    //             for (int edgeIndex = 0; edgeIndex < edges.length; edgeIndex++) {
    //                 int edgeFirstPointIndex = edges[edgeIndex][0];
    //                 int edgeSecondPointIndex = edges[edgeIndex][1];
    //                 int[] facesForEdge = cube.getEdgesFacesMap().get(edgeIndex);
                    
    //                 boolean edgeVisible = false;
    //                 for (int faceIndex : facesForEdge) {
    //                     int[] faceVertices = cube.getFaceVertices(faceIndex);
    //                     if (camera.isFaceFrontFacing(faceVertices, projection.toArray(new Vertex[0]))) {
    //                         edgeVisible = true;
    //                         break; // At least one face is front-facing, render edge
    //                     }
                        
    //                 }  

    //                 if (!edgeVisible) {
    //                     continue; // Skip rendering back-face-only edges
    //                 } 

    //                 Vertex p1 = projection.get(edgeFirstPointIndex);
    //                 Vertex p2 = projection.get(edgeSecondPointIndex);
                    
    //                 if (camera.isPointBehindCamera(p1) && camera.isPointBehindCamera(p2)) {
    //                     continue;
    //                 }
                     
    //                 drawClippedLine(g, p1, p2, scaleX, scaleY);       
    //             }

    //         }
    //     }

    
    // }

    // private void drawClippedLine(Graphics g, Vertex p1, Vertex p2, double scaleX, double scaleY) {
    //     Vertex[] clipped = camera.clipLine(p1, p2);
    //     if (clipped != null && clipped.length == 2) {                        
    //         int screenX1 = (int) ref.x() + (int)(clipped[0].getX() * scaleX);
    //         int screenY1 = (int) ref.y() - (int)(clipped[0].getY() * scaleY);
    //         int screenX2 = (int) ref.x() + (int)(clipped[1].getX() * scaleX);
    //         int screenY2 = (int) ref.y() - (int)(clipped[1].getY() * scaleY);

    //         g.drawLine(screenX1, screenY1, screenX2, screenY2);
    //     }       
    // }

    // private void fullMeshRendering(Graphics g) {
    //     g.setColor(Color.BLACK);
    //     double width = getWidth();
    //     double height = getHeight();

    //     double scaleX = width / 2;
    //     double scaleY = height / 2;
    //     List<Triangle> triangles = new ArrayList<>();
    //     Matrix mvpMatrix = camera.createMVPMatrix();

    //     for (Mesh form : forms) {
    //         if (form instanceof CubeMesh cube) {
                
    //             int[][] faces = cube.getFaces();

    //             for (int[] face : faces) {
    //                 triangles.add(new Triangle(cube.getVertices().get(face[0]),
    //                                             cube.getVertices().get(face[2]),
    //                                             cube.getVertices().get(face[3])));
    //                 triangles.add(new Triangle(cube.getVertices().get(face[0]),
    //                                             cube.getVertices().get(face[1]),
    //                                             cube.getVertices().get(face[2])));
    //             }

    //             triangles.stream().forEach(t -> {
    //                 t.setP1(mvpMatrix.multiply(t.getP1()));
    //                 t.setP2(mvpMatrix.multiply(t.getP2()));
    //                 t.setP3(mvpMatrix.multiply(t.getP3()));
    //             });

    //             for (Triangle triangle : triangles) {
    //                 Vertex[] trianglesPoint = new Vertex[] {
    //                     triangle.getP1(),
    //                     triangle.getP2(),
    //                     triangle.getP3()
    //                 };

    //                 if (!camera.isFaceFrontFacing(trianglesPoint)) {
    //                     continue;
    //                 }

    //                 Vertex p1 = triangle.getP1();
    //                 Vertex p2 = triangle.getP2();
    //                 Vertex p3 = triangle.getP3();

    //                 drawClippedLine(g, p1, p2, scaleX, scaleY);
    //                 drawClippedLine(g, p2, p3, scaleX, scaleY);
    //                 drawClippedLine(g, p3, p1, scaleX, scaleY);
    //             }
    //         }
    //     }
    // }
        
}