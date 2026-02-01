package frustum3d.renderer;

import frustum3d.core.clip.ClipLine;
import frustum3d.core.clip.ClipTriangle;
import frustum3d.core.clip.ClipVertex;
import frustum3d.core.meshes.CubeMesh;
import frustum3d.core.meshes.Mesh;
import frustum3d.core.screen.*;
import frustum3d.core.view.Line;
import frustum3d.core.view.Triangle3D;
import frustum3d.core.view.Vertex;
import frustum3d.scene.Camera;
import frustum3d.utils.Matrix;
import frustum3d.utils.Referentiel;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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

    public List<ScreenLine> wireFrameRendering(List<Mesh> meshes) {
        List<ClipLine> clipLines = new ArrayList<>();
        Matrix viewMatrix = camera.createViewMatrix();
        Matrix projectionMatrix = camera.createProjectionMatrix();

        for (Mesh mesh : meshes) {
            if (mesh instanceof CubeMesh cube) {
                List<Vertex> vertices = mesh.getVertices();
                List<Vertex> view = computeViewCoordinates(viewMatrix, vertices);


                int[][] edges = mesh.getEdges();
                for (int edgeIndex = 0; edgeIndex < edges.length; edgeIndex++) {
                    int edgeFirstPointIndex = edges[edgeIndex][0];
                    int edgeSecondPointIndex = edges[edgeIndex][1];
                    int[] facesForEdge = cube.getEdgesFacesMap().get(edgeIndex);

                    boolean edgeVisible = false;
                    if (!isEdgeVisible(cube, edgeVisible, facesForEdge, view))
                        continue;

                    ClipVertex p1 = projectionMatrix.multiplyClip(view.get(edgeFirstPointIndex));
                    ClipVertex p2 = projectionMatrix.multiplyClip(view.get(edgeSecondPointIndex));

                    clipLines.add(new ClipLine(p1, p2));
                }

            }
        }
        clipLines = camera.getClippedLineOptimized(clipLines);

        List<Line> vertexLines = camera.toLineNDCSpace(clipLines);

        return vertexLines.stream().map( vLine -> getEdgeScreenLine(vLine.from(), vLine.to())).toList();
    }

    public List<ScreenLine> fullMeshRenderingOptimized(List<Mesh> meshes) {
        List<ScreenLine> drawLines = new ArrayList<>();
        Matrix viewMatrix = camera.createViewMatrix();
        Matrix projectionMatrix = camera.createProjectionMatrix();

        for (Mesh mesh : meshes) {
            if (mesh instanceof CubeMesh cube) {
                List<Triangle3D> triangle3DS = new ArrayList<>();
                List<ClipTriangle> clippedTriangles = new ArrayList<>();

                int[][] faces = cube.getFacesDef();

                for (int[] face : faces) {
                    triangle3DS.add(new Triangle3D(cube.getVertices().get(face[0]),
                            cube.getVertices().get(face[2]),
                            cube.getVertices().get(face[3])));
                    triangle3DS.add(new Triangle3D(cube.getVertices().get(face[0]),
                            cube.getVertices().get(face[1]),
                            cube.getVertices().get(face[2])));
                }

                triangle3DS.forEach(t -> {
                    t.setP1(viewMatrix.multiply(t.getP1()));
                    t.setP2(viewMatrix.multiply(t.getP2()));
                    t.setP3(viewMatrix.multiply(t.getP3()));
                });

                for (Triangle3D triangle3D : triangle3DS) {
                    Vertex[] trianglesPoint = new Vertex[] {
                            triangle3D.getP1(),
                            triangle3D.getP2(),
                            triangle3D.getP3()
                    };

                    if (!camera.isFaceFrontFacing(new int[] {0,1,2}, trianglesPoint)) {
                        continue;
                    }


                    List<ClipVertex> clippedVertices = camera.getClippedVertices(List.of(
                        projectionMatrix.multiplyClip(triangle3D.getP1()),
                        projectionMatrix.multiplyClip(triangle3D.getP2()),
                        projectionMatrix.multiplyClip(triangle3D.getP3())
                    ));

                    clippedTriangles.addAll(fanTriangulation(clippedVertices));
                }

                for (ClipTriangle triangle : clippedTriangles) {
                    Triangle3D triangle3D = camera.toVertexNDCSpace(triangle);

                    Triangle2D screenTriangle = triangleToScreen(triangle3D);

                    List<ScreenLine> edgeLines = List.of(
                        new ScreenLine(screenTriangle.getP1(), screenTriangle.getP2(), Color.BLACK),
                        new ScreenLine(screenTriangle.getP2(), screenTriangle.getP3(), Color.BLACK),
                        new ScreenLine(screenTriangle.getP3(), screenTriangle.getP1(), Color.BLACK)
                    );


                    drawLines.addAll(edgeLines);
                }

            }
        }
        return drawLines;
    }

    private List<ClipTriangle> fanTriangulation(List<ClipVertex> polygon) {
        List<ClipTriangle> triangles = new ArrayList<>();
        for (int ind = 1; ind < polygon.size()-2; ind++) {
            triangles.add(new ClipTriangle(polygon.getFirst(), polygon.get(ind), polygon.get(ind+1)));
        }
        return triangles;
    }

    private ScreenLine getEdgeScreenLine(Vertex p1, Vertex p2) {
        int screenX1 = (int) ref.x() + (int)(p1.getX() * (width / 2));
        int screenY1 = (int) ref.y() - (int)(p1.getY() * (height / 2));
        int screenX2 = (int) ref.x() + (int)(p2.getX() * (width / 2));
        int screenY2 = (int) ref.y() - (int)(p2.getY() * (height / 2));
        return new ScreenLine(
            new Pixel(screenX1, screenY1, p1.getZ()),
            new Pixel(screenX2, screenY2, p2.getZ()),
            Color.BLACK
        );
    }



    private boolean isEdgeVisible(CubeMesh cube, boolean edgeVisible, int[] facesForEdge, List<Vertex> view) {
        edgeVisible = backfaceCulling(cube, facesForEdge, view, edgeVisible);
        return edgeVisible;
    }

    private boolean backfaceCulling(CubeMesh cube, int[] facesForEdge, List<Vertex> view, boolean edgeVisible) {
        for (int faceIndex : facesForEdge) {
            int[] faceVertices = cube.getFaceVertices(faceIndex);
            if (camera.isFaceFrontFacing(faceVertices, view.toArray(new Vertex[0]))) {
                edgeVisible = true;
                break; // At least one face is front-facing, render edge
            }
        }
        return edgeVisible;
    }

    private List<Vertex> computeViewCoordinates(Matrix matrix, List<Vertex> vertices) {
        return vertices.stream().map(matrix::multiply).toList();
    }

    private Triangle2D triangleToScreen(Triangle3D triangle) {
        return new Triangle2D(
                vertexToScreen(triangle.getP1()),
                vertexToScreen(triangle.getP2()),
                vertexToScreen(triangle.getP3())
        );
    }

    private Pixel vertexToScreen(Vertex vertex) {
        int screenX1 = (int) ref.x() + (int)(vertex.getX() * (width / 2));
        int screenY1 = (int) ref.y() - (int)(vertex.getY() * (height / 2));
        return new Pixel(screenX1, screenY1, vertex.getZ());
    }
}
