package frustum3d.core.meshes;

import frustum3d.core.view.Vertex;

import java.util.List;
import java.util.Map;

public interface Mesh {

    int[][] getFacesDef();

    int[][] getEdges();

    abstract List<Vertex> getVertices();

    Map<Integer, int[]> getEdgesFacesMap();

}