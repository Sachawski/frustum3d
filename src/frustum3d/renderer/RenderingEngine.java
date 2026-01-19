package frustum3d.renderer;

import java.util.ArrayList;
import java.util.List;

import frustum3d.core.CubeMesh;
import frustum3d.core.Line;
import frustum3d.core.Mesh;
import frustum3d.core.Pixel;
import frustum3d.core.Triangle;
import frustum3d.core.Vertex;
import frustum3d.scene.Camera;
import frustum3d.utils.Matrix;
import frustum3d.utils.Referentiel;

public class RenderingEngine {
    
    private Referentiel ref;
    private double width;
    private double height;
    private Camera camera;

    public RenderingEngine(Referentiel ref,
                            double width,
                            double height,
                            Camera camera ){
        this.ref = ref;
        this.width = width;
        this.height = height;
        this.camera = camera;
    }

    public List<Line> wireFrameRendering(List<Mesh> forms) {
        List<Line> lines = new ArrayList<>();
        Matrix mvpMatrix = camera.createMVPMatrix();

        for (Mesh form : forms) {
            if (form instanceof CubeMesh cube) {
                List<Vertex> projection = new ArrayList<>();
                
                for (int i = 0; i < form.getVertices().size(); i++) {
                    Vertex point = form.getVertices().get(i);
                    Vertex transformed = mvpMatrix.multiply(point);
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
                        if (camera.isFaceFrontFacing(faceVertices, projection.toArray(new Vertex[0]))) {
                            edgeVisible = true;
                            break; // At least one face is front-facing, render edge
                        }
                        
                    }  

                    if (!edgeVisible) {
                        continue; // Skip rendering back-face-only edges
                    } 

                    Vertex p1 = projection.get(edgeFirstPointIndex);
                    Vertex p2 = projection.get(edgeSecondPointIndex);
                    
                    if (camera.isPointBehindCamera(p1) && camera.isPointBehindCamera(p2)) {
                        continue;
                    }
                     
                    lines.add(getClippedLine(p1, p2));       
                }

            }
        }
        return lines;
    }

    public List<Line> fullMeshRendering(List<Mesh> forms) {
        List<Line> lines = new ArrayList<>();
        List<Triangle> triangles = new ArrayList<>();
        Matrix mvpMatrix = camera.createMVPMatrix();

        for (Mesh form : forms) {
            if (form instanceof CubeMesh cube) {
                
                int[][] faces = cube.getFaces();

                for (int[] face : faces) {
                    triangles.add(new Triangle(cube.getVertices().get(face[0]),
                                                cube.getVertices().get(face[2]),
                                                cube.getVertices().get(face[3])));
                    triangles.add(new Triangle(cube.getVertices().get(face[0]),
                                                cube.getVertices().get(face[1]),
                                                cube.getVertices().get(face[2])));
                }

                triangles.stream().forEach(t -> {
                    t.setP1(mvpMatrix.multiply(t.getP1()));
                    t.setP2(mvpMatrix.multiply(t.getP2()));
                    t.setP3(mvpMatrix.multiply(t.getP3()));
                });

                for (Triangle triangle : triangles) {
                    Vertex[] trianglesPoint = new Vertex[] {
                        triangle.getP1(),
                        triangle.getP2(),
                        triangle.getP3()
                    };

                    if (!camera.isFaceFrontFacing(trianglesPoint)) {
                        continue;
                    }

                    Vertex p1 = triangle.getP1();
                    Vertex p2 = triangle.getP2();
                    Vertex p3 = triangle.getP3();

                    lines.add(getClippedLine(p1, p2));
                    lines.add(getClippedLine(p2, p3));
                    lines.add(getClippedLine(p3, p1));
                }
            }
        }
        return lines;
    }
        
    private Line getClippedLine(Vertex p1, Vertex p2) {
        Vertex[] clipped = camera.clipLine(p1, p2);
        if (clipped != null && clipped.length == 2) { 
            int screenX1 = (int) ref.x() + (int)(clipped[0].getX() * (width / 2));
            int screenY1 = (int) ref.y() - (int)(clipped[0].getY() * (height / 2));
            int screenX2 = (int) ref.x() + (int)(clipped[1].getX() * (width / 2));
            int screenY2 = (int) ref.y() - (int)(clipped[1].getY() * (height / 2));
            return new Line(
                new Pixel(screenX1, screenY1),
                new Pixel(screenX2, screenY2)
            );
        }
        return new Line(
            new Pixel(0, 0),
            new Pixel(0, 0)
        );
    }

}
