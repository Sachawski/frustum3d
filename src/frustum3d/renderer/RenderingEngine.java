package frustum3d.renderer;

import frustum3d.core.ClipLine;
import frustum3d.core.ClipVertex;
import frustum3d.core.CubeMesh;
import frustum3d.core.Line;
import frustum3d.core.Mesh;
import frustum3d.core.Vertex;
import frustum3d.scene.Camera;
import frustum3d.utils.Matrix;
import frustum3d.utils.Referentiel;
import java.util.ArrayList;
import java.util.List;

public class RenderingEngine {
    
    private final Referentiel ref;
    private final double width;
    private final double height;
    private final Camera camera;

    public RenderingEngine(Referentiel ref,
                            double width,
                            double height,
                            Camera camera ){
        this.ref = ref;
        this.width = width;
        this.height = height;
        this.camera = camera;
    }

    public List<ScreenLine> wireFrameRendering(List<Mesh> forms) {
        List<Line> vertexLines = new ArrayList<>();
        List<ClipLine> clipLines = new ArrayList<>();
        Matrix viewMatrix = camera.createViewMatrix();
        Matrix projectionMatrix = camera.createProjectionMatrix();

        for (Mesh form : forms) {
            if (form instanceof CubeMesh cube) {
                List<Vertex> view = new ArrayList<>();
                List<Vertex> vertices = form.getVertices();

                for (int i = 0; i < vertices.size(); i++) {
                    Vertex point = vertices.get(i);
                    Vertex transformed = viewMatrix.multiply(point);
                    view.add(transformed);
                }                

                int[][] edges = form.getEdges();
                for (int edgeIndex = 0; edgeIndex < edges.length; edgeIndex++) {
                    int edgeFirstPointIndex = edges[edgeIndex][0];
                    int edgeSecondPointIndex = edges[edgeIndex][1];
                    int[] facesForEdge = cube.getEdgesFacesMap().get(edgeIndex);
                    
                    boolean edgeVisible = false;
                    for (int faceIndex : facesForEdge) {
                        int[] faceVertices = cube.getFaceVertices(faceIndex);
                        if (camera.isFaceFrontFacing(faceVertices, view.toArray(new Vertex[0]))) {
                            edgeVisible = true;
                            break; // At least one face is front-facing, render edge
                        }
                    }  
                    if (!edgeVisible) {
                        continue; // Skip rendering back-face-only edges
                    } 

                    ClipVertex p1 = projectionMatrix.multiplyClip(view.get(edgeFirstPointIndex));
                    ClipVertex p2 = projectionMatrix.multiplyClip(view.get(edgeSecondPointIndex));

                    clipLines.add(new ClipLine(p1, p2));       
                }

            }
        }
        clipLines = camera.getClippedLineOptimized(clipLines);

        vertexLines = camera.toNDCSpace(clipLines);

        return vertexLines.stream().map( vLine -> getScreenLine(vLine.from(), vLine.to())).toList();
    }

    // public List<Line> fullMeshRendering(List<Mesh> forms) {
    //     List<Line> lines = new ArrayList<>();
    //     List<Triangle> triangles = new ArrayList<>();
    //     Matrix viewMatrix = camera.createViewMatrix();
    //     Matrix projectionMatrix = camera.createProjectionMatrix();
    //     Matrix mvpMatrix = camera.createMVPMatrix();

    //     for (Mesh form : forms) {
    //         if (form instanceof CubeMesh cube) {
                
    //             int[][] faces = cube.getFacesDef();

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

    //                 lines.add(getClippedLine(p1, p2));
    //                 lines.add(getClippedLine(p2, p3));
    //                 lines.add(getClippedLine(p3, p1));
    //             }
    //         }
    //     }
    //     return lines;
    // }

    // public List<Line> fullMeshRenderingOptimized(List<Mesh> forms) {
    //     List<Line> lines = new ArrayList<>();
    //     List<Triangle> triangles = new ArrayList<>();
    //     Matrix mvpMatrix = camera.createMVPMatrix();

    //     for (Mesh form : forms) {
    //         if (form instanceof CubeMesh cube) {
                
    //             int[][] faces = cube.getFacesDef();

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

    //                 lines.add(getClippedLine(p1, p2));
    //                 lines.add(getClippedLine(p2, p3));
    //                 lines.add(getClippedLine(p3, p1));
    //             }
    //         }
    //     }
    //     return lines;
    // }

        
    private ScreenLine getScreenLine(Vertex p1, Vertex p2) {
        // Vertex[] clipped = camera.clipLine(p1, p2);
        // if (clipped != null && clipped.length == 2) { 
        //     int screenX1 = (int) ref.x() + (int)(clipped[0].getX() * (width / 2));
        //     int screenY1 = (int) ref.y() - (int)(clipped[0].getY() * (height / 2));
        //     int screenX2 = (int) ref.x() + (int)(clipped[1].getX() * (width / 2));
        //     int screenY2 = (int) ref.y() - (int)(clipped[1].getY() * (height / 2));
        //     return new ScreenLine(
        //         new Pixel(screenX1, screenY1),
        //         new Pixel(screenX2, screenY2)
        //     );
        // }
        // return new ScreenLine(
        //     new Pixel(0, 0),
        //     new Pixel(0, 0)
        // );
        // Vertex[] clipped = camera.clipLine(p1, p2);
        // if (clipped != null && clipped.length == 2) { 
        int screenX1 = (int) ref.x() + (int)(p1.getX() * (width / 2));
        int screenY1 = (int) ref.y() - (int)(p1.getY() * (height / 2));
        int screenX2 = (int) ref.x() + (int)(p2.getX() * (width / 2));
        int screenY2 = (int) ref.y() - (int)(p2.getY() * (height / 2));
        return new ScreenLine(
            new Pixel(screenX1, screenY1),
            new Pixel(screenX2, screenY2)
        );
    }

    // private List<Triangle> triangulate4VertexPolygon(Polygon polygon) {
    //     List<Triangle> result = new ArrayList<>();
    //     result.add(new Triangle(polygon.getVertices().get(0), polygon.getVertices().get(2), polygon.getVertices().get(3)));
    //     result.add(new Triangle(polygon.getVertices().get(0), polygon.getVertices().get(1), polygon.getVertices().get(2)));
    //     return result;
    // }
}
