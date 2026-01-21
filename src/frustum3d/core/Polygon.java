package frustum3d.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Polygon {

    private List<Vertex> vertices;

    public Polygon(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public Polygon(Vertex... newVertices) {
        this.vertices = new ArrayList<>();
        vertices.addAll(Arrays.asList(newVertices));
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

}
