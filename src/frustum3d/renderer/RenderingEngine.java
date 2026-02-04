package frustum3d.renderer;

import frustum3d.core.clip.ClipLine;
import frustum3d.core.clip.ClipTriangle;
import frustum3d.core.clip.ClipVertex;
import frustum3d.core.meshes.CubeMesh;
import frustum3d.core.meshes.Mesh;
import frustum3d.core.screen.Pixel;
import frustum3d.core.screen.ScreenLine;
import frustum3d.core.screen.Triangle2D;
import frustum3d.core.view.Line;
import frustum3d.core.view.Triangle3D;
import frustum3d.core.view.Vertex;
import frustum3d.scene.Camera;
import frustum3d.utils.Matrix;
import frustum3d.utils.Referentiel;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class RenderingEngine {
    
    private Referentiel ref;
    private double width;
    private double height;
    private final Camera camera;

    public RenderingEngine(Referentiel ref,
                            double width,
                            double height,
                            Camera camera ){
        this.ref = ref;
        this.height = height;
        this.width = width;
        this.camera = camera;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
    
    public void resize(int width, int height){
        this.width = width;
        this.height = height;
    }

    public void setRef(Referentiel ref) {
        this.ref = ref;
    }

    public BufferedImage wireFrameRendering(List<Mesh> meshes) {
        List<ClipLine> clipLines = new ArrayList<>();
        BufferedImage bufferedImage = new BufferedImage((int)width,(int)height,BufferedImage.TYPE_INT_ARGB);
        int[] pixels = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();
        int w = bufferedImage.getWidth();
        int h = bufferedImage.getHeight();
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

        List<ScreenLine> screenLines = vertexLines.stream().map(vLine -> getEdgeScreenLine(vLine.from(), vLine.to())).toList();

        for (ScreenLine line : screenLines) {
            drawLine(line, pixels, w, h ,Color.BLACK.getRGB());
        }
        return bufferedImage;
    }

    public BufferedImage fullMeshRenderingOptimized(List<Mesh> meshes) {
        BufferedImage bufferedImage = new BufferedImage((int)width,(int)height,BufferedImage.TYPE_INT_ARGB);
        int[] pixels = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();
        int w = bufferedImage.getWidth();
        int h = bufferedImage.getHeight();
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

                    Triangle2D screenTriangle = triangleToScreenSorted(triangle3D);

                    List<ScreenLine> filling = scanline(screenTriangle);

                    List<ScreenLine> edgeLines = List.of(
                        new ScreenLine(screenTriangle.getP0(), screenTriangle.getP1(), Color.BLACK),
                        new ScreenLine(screenTriangle.getP1(), screenTriangle.getP2(), Color.BLACK),
                        new ScreenLine(screenTriangle.getP2(), screenTriangle.getP0(), Color.BLACK)
                    );


                    for (ScreenLine line : filling) {
                        drawLine(line, pixels, w, h, line.color().getRGB());
                    }

                    for (ScreenLine line : edgeLines) {
                        drawLine(line, pixels, w, h, Color.BLACK.getRGB());
                    }
                }

            }
        }

        return bufferedImage;
    }

    private void drawLine(ScreenLine line, int[] pixels, int w, int h, int argb) {
        int x0 = line.pixel1().x();
        int y0 = line.pixel1().y();
        int x1 = line.pixel2().x();
        int y1 = line.pixel2().y();

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            if ((x0 | y0) >= 0 && x0 < w && y0 < h) { // bounds check rapide
                pixels[y0 * w + x0] = argb;
            }
            if (x0 == x1 && y0 == y1) break;

            int e2 = err << 1;
            if (e2 > -dy) { err -= dy; x0 += sx; }
            if (e2 <  dx) { err += dx; y0 += sy; }
        }
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

    private List<ScreenLine> scanline(Triangle2D triangle) {
        int x0 = triangle.getP0().x();
        int y0 = triangle.getP0().y();
        int x1 = triangle.getP1().x();
        int y1 = triangle.getP1().y();
        int x2 = triangle.getP2().x();
        int y2 = triangle.getP2().y();
        int mx = (int)((double)((x2 - x0) * (y1 - y0)) / (double)(y2 - y0)) + x0;
        int my = y1;
        List<ScreenLine> topSpans = topTriangle(x0, y0, x1, y1, mx, my);
        List<ScreenLine> bottomSpans = bottomTriangle(x1, y1, mx, my, x2, y2);
        topSpans.addAll(bottomSpans);
        return topSpans;
    }

    private List<ScreenLine> topTriangle(int x0, int y0, int x1, int y1, int x2, int y2) {
        List<ScreenLine> spans = new ArrayList<>();
        double invSlope1 = (double)(x1 - x0) / (double)(y1 - y0);
        double invSlope2 = (double)(x2 - x0) / (double)(y2 - y0);

        double xStart = x0;
        double xEnd = x0;

        for (int y = y0; y < y2; y++) {
            spans.add(new ScreenLine(
                new Pixel((int)xStart,y, 0),
                new Pixel((int)xEnd,y, 0),
                Color.RED
            ));

            xStart +=  invSlope1;
            xEnd += invSlope2;
        }
        return spans;
    }

    private List<ScreenLine> bottomTriangle(int x0, int y0, int x1, int y1, int x2, int y2) {
        List<ScreenLine> spans = new ArrayList<>();
        double invSlope1 = (double)(x2 - x0) / (double)(y2 - y0);
        double invSlope2 = (double)(x2 - x1) / (double)(y2 - y1);
        double xStart = x2;
        double xEnd = x2;

        for (int y = y2; y >= y1; y--) {
            spans.add(new ScreenLine(
                    new Pixel((int)xStart,y, 0),
                    new Pixel((int)xEnd,y, 0),
                    Color.RED
            ));

            xStart -=  invSlope1;
            xEnd -= invSlope2;
        }
        return spans;
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

    private Triangle2D triangleToScreenSorted(Triangle3D triangle) {
        List<Pixel> sortedPixels = Stream.of(triangle.getP1(), triangle.getP2(), triangle.getP3())
                .map(this::vertexToScreen)
                .sorted((a,b) -> (Integer.compare(a.y(), b.y())))
                .toList();
        return new Triangle2D(
                sortedPixels.getFirst(),
                sortedPixels.get(1),
                sortedPixels.getLast()
        );
    }

    private Pixel vertexToScreen(Vertex vertex) {
        int screenX1 = (int) ref.x() + (int)(vertex.getX() * (width / 2));
        int screenY1 = (int) ref.y() - (int)(vertex.getY() * (height / 2));
        return new Pixel(screenX1, screenY1, vertex.getZ());
    }
}
