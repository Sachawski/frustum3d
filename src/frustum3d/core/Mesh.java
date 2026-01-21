package frustum3d.core;

import java.util.List;
import java.util.Map;

public interface Mesh {

    public int[][] getFacesDef();

    public int[][] getEdges();

    public abstract List<Vertex> getVertices();

    public Map<Integer, int[]> getEdgesFacesMap();

}