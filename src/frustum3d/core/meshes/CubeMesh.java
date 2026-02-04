package frustum3d.core.meshes;

import frustum3d.core.view.Vertex;

import java.awt.Color;
import java.util.List;
import java.util.Map;

public class CubeMesh implements Mesh {

    private final List<Vertex> vertices;
    private final Color color;
    public static final int POINT_NUMBER = 8;
    private static final int[][] FACES = {
        {0, 3, 2, 1},  // Back face
        {4, 5, 6 ,7},  // Front face
        {0, 4, 7, 3},  // Right face
        {1, 2, 6, 5},  // Left face
        {3, 7, 6 ,2},  // Top face
        {0, 1, 5, 4}   // Bottom face
    };
    private static final int[][] EDGES = {
        {0, 3},
        {3, 2},
        {2, 1},
        {1, 0},
        {4, 5},
        {5, 6},
        {6, 7},
        {7, 4},
        {4, 0},
        {3, 7},
        {1, 5},
        {6, 2}
    };
    private static final Map<Integer, int[]> EDGES_FACES_MAP = Map.ofEntries(
            Map.entry(0, new int[]{0, 2}),
            Map.entry(1, new int[]{0, 4}),
            Map.entry(2, new int[]{0, 3}),
            Map.entry(3, new int[]{0, 5}),
            Map.entry(4, new int[]{1, 5}),
            Map.entry(5, new int[]{1, 3}),
            Map.entry(6, new int[]{1, 4}),
            Map.entry(7, new int[]{1, 2}),
            Map.entry(8, new int[]{2, 5}),
            Map.entry(9, new int[]{2, 4}),
            Map.entry(10, new int[]{3, 5}),
            Map.entry(11, new int[]{3, 4})
    );

    public CubeMesh(List<Vertex> points, Color color) {
        this.vertices = points;
        this.color = color;
    }

    @Override
    public int[][] getFacesDef() {
        return FACES;
    }

    @Override
    public int[][] getEdges() {
        return EDGES;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public List<Vertex> getVertices() {
        return vertices;
    }

    @Override
    public Map<Integer, int[]> getEdgesFacesMap() {
        return EDGES_FACES_MAP;
    }

    public int[] getFaceVertices(int faceIndex) {
        return FACES[faceIndex];
    }

}