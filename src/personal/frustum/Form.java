package personal.frustum;

import java.util.List;

public interface Form {

    public int[][] getFaces();

    public int[][] getEdges();

    public Integer[][] getGraph();

    public abstract int getNumberOfPoints();

    public abstract List<Point3D> getPoints();

}