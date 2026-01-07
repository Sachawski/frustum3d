package personal;

import java.util.ArrayList;
import java.util.List;

public class Cube implements Form {

    public static final int POINT_NUMBER = 8;
    public List<Point3D> points;
    private static Integer[][] graph = new Integer[][] {{0, 1, 0, 1, 1, 0, 0, 0},
                                                       {0, 0, 1, 0, 0, 1, 0, 0},
                                                       {0, 0, 0, 1, 0, 0, 1, 0},
                                                       {0, 0, 0, 0, 0, 0, 0, 1},
                                                       {0, 0, 0, 0, 0, 1, 0, 1},
                                                       {0, 0, 0, 0, 0, 0, 1, 0},
                                                       {0, 0, 0, 0, 0, 0, 0, 1},
                                                       {0, 0, 0, 0, 0, 0, 0, 0},};

    @Override
    public Integer[][] getGraph() {
        return graph;
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