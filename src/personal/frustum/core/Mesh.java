package personal.frustum.core;

import java.util.List;
import java.util.Map;

public interface Mesh {

    public int[][] getFaces();

    public int[][] getEdges();

    public abstract List<Vertex> getVertices();

    public Map<Integer, int[]> getEdgesFacesMap();

}