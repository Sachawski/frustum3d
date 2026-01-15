package personal.frustum;

public class CubeFactory {

    private double cubeSize = 1000;

    public CubeFactory(double cubeSize) {
        this.cubeSize = cubeSize;
    }

    public Cube buildAt(Point3D point) { 
        return new Cube(
                new Point3D(point.getPosX(), point.getPosY(), point.getPosZ()),
                new Point3D(point.getPosX() - cubeSize, point.getPosY(), point.getPosZ()),
                new Point3D(point.getPosX() - cubeSize, point.getPosY() - cubeSize, point.getPosZ()),
                new Point3D(point.getPosX(), point.getPosY() - cubeSize, point.getPosZ()),
                new Point3D(point.getPosX(), point.getPosY(), point.getPosZ() - cubeSize),
                new Point3D(point.getPosX() - cubeSize, point.getPosY(), point.getPosZ() - cubeSize),
                new Point3D(point.getPosX() - cubeSize, point.getPosY() - cubeSize, point.getPosZ() - cubeSize),
                new Point3D(point.getPosX(), point.getPosY() - cubeSize, point.getPosZ() - cubeSize)
        );
    }
}
