package personal.frustum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cube implements Form {

    public static final int POINT_NUMBER = 8;
    public List<Point3D> points;

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

    @Override
    public int[][] getFaces() {
        return FACES;
    }

    @Override
    public int[][] getEdges() {
        return EDGES;
    }

    @Override
    public Map<Integer, int[]> getEdgesFacesMap() {
        return EDGES_FACES_MAP;
    }

     // Check if both endpoints of edge are in the same face
    public boolean edgeBelongsToFace(int edgeV1, int edgeV2, int faceIndex) {
        int[] faceVertices = FACES[faceIndex];
        boolean v1InFace = false;
        boolean v2InFace = false;
        
        for (int v : faceVertices) {
            if (v == edgeV1) v1InFace = true;
            if (v == edgeV2) v2InFace = true;
        }
        
        return (v1InFace && v2InFace);
    }
    
    // Get all faces that contain this edge
    public int[] getFacesForEdge(int edgeV1, int edgeV2) {
        List<Integer> facesList = new ArrayList<>();
        
        for (int faceIndex = 0; faceIndex < FACES.length; faceIndex++) {
            if (edgeBelongsToFace(edgeV1, edgeV2, faceIndex)) {
                facesList.add(faceIndex);
            }
        }
        
        return facesList.stream().mapToInt(i -> i).toArray();
    }

    public int[] getFaceVertices(int faceIndex) {
        return FACES[faceIndex];
    }

    @Override
    public Integer[][] getGraph() {
        return new Integer[][] {{}};
    }

    @Override 
    public int getNumberOfPoints() {
        return POINT_NUMBER;
    }

    @Override
    public List<Point3D> getPoints() {
        return points;
    }

    public Cube(List<Point3D> points) throws Exception {
        if (points.size() > POINT_NUMBER) {
            this.points = points;
        }
        else {
            throw new Exception("Wrong polygon");
        }
    }

    public Cube(Point3D firstPoint,
                Point3D secondPoint,
                Point3D thirdPoint,
                Point3D fourthPoint,
                Point3D fifthPoint,
                Point3D sixthPoint,
                Point3D seventhPoint,
                Point3D eigthPoint) {
        List<Point3D> newPoints = new ArrayList<>();
        newPoints.add(firstPoint);
        newPoints.add(secondPoint);
        newPoints.add(thirdPoint);
        newPoints.add(fourthPoint);
        newPoints.add(fifthPoint);
        newPoints.add(sixthPoint);
        newPoints.add(seventhPoint);
        newPoints.add(eigthPoint);
        this.points = newPoints;
    }


}