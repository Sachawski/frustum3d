package personal.frustum.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CubeMesh implements Mesh {

    public static final int POINT_NUMBER = 8;
    private List<Vertex> vertices;
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
    private static final Map<Integer, int[]> EDGES_FACES_MAP = new HashMap<>() {{
        put(0,new int[] {0,2});
        put(1,new int[] {0,4});
        put(2,new int[] {0,3});
        put(3,new int[] {0,5});
        put(4,new int[] {1,5});
        put(5,new int[] {1,3});
        put(6,new int[] {1,4});
        put(7,new int[] {1,2});
        put(8,new int[] {2,5});
        put(9,new int[] {2,4});
        put(10,new int[] {3,5});
        put(11,new int[] {3,4});
    }};

    public CubeMesh(List<Vertex> points) throws Exception {
        if (points.size() > POINT_NUMBER) {
            this.vertices = points;
        }
        else {
            throw new Exception("Wrong polygon");
        }
    }

    public CubeMesh(Vertex firstPoint,
                Vertex secondPoint,
                Vertex thirdPoint,
                Vertex fourthPoint,
                Vertex fifthPoint,
                Vertex sixthPoint,
                Vertex seventhPoint,
                Vertex eigthPoint) {
        List<Vertex> newPoints = new ArrayList<>();
        newPoints.add(firstPoint);
        newPoints.add(secondPoint);
        newPoints.add(thirdPoint);
        newPoints.add(fourthPoint);
        newPoints.add(fifthPoint);
        newPoints.add(sixthPoint);
        newPoints.add(seventhPoint);
        newPoints.add(eigthPoint);
        this.vertices = newPoints;
    }

    @Override
    public int[][] getFaces() {
        return FACES;
    }

    @Override
    public int[][] getEdges() {
        return EDGES;
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