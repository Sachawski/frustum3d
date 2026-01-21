package frustum3d.core;

import frustum3d.scene.Camera;
import java.util.ArrayList;
import java.util.List;

public class TriangleClipper {

    Camera camera;

    public TriangleClipper(Camera camera) {
        this.camera = camera;
    } 
    
    private List<ClipVertex[]> triangulate(List<ClipVertex> polygon) {
        List<ClipVertex[]> triangles = new ArrayList<>();
        if (polygon.size() < 3) return triangles; // Degenerate case
        
        ClipVertex first = polygon.get(0);
        for (int i = 1; i < polygon.size() - 1; i++) {
            triangles.add(new ClipVertex[] {
                first,
                polygon.get(i),
                polygon.get(i + 1)
            });
        }
        
        return triangles;
    }
    
    public List<Triangle> clipTriangle(ClipVertex v0, ClipVertex v1, ClipVertex v2) {
        List<ClipVertex> polygon = new ArrayList<>();
        polygon.add(v0);
        polygon.add(v1);
        polygon.add(v2);

        List<ClipVertex[]> triangles = triangulate(polygon);
    
        return triangles.stream().map(triangle -> new Triangle(triangle[0].toVertex(),
                                                                 triangle[1].toVertex(),
                                                                  triangle[2].toVertex())).toList();
    }

}
