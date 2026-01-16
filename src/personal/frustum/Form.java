package personal.frustum;

import java.util.List;
import java.util.Map;

public interface Form {

    public int[][] getFaces();

    public int[][] getEdges();

    public Map<Integer, int[]> getEdgesFacesMap();

    public abstract List<Point3D> getPoints();

}