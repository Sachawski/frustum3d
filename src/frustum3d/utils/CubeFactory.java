package frustum3d.utils;

import frustum3d.core.meshes.CubeMesh;
import frustum3d.core.view.Vertex;

import java.awt.Color;
import java.util.List;
import java.util.Random;

public class CubeFactory {

    private double cubeSize = 1000;
    Random randomColor = new Random();

    public CubeFactory(double cubeSize) {
        this.cubeSize = cubeSize;
    }

    public CubeMesh buildAt(Vertex point) {
        return new CubeMesh(List.of(
                new Vertex(point.getX(), point.getY(), point.getZ() - cubeSize),
                new Vertex(point.getX() + cubeSize, point.getY(), point.getZ() - cubeSize),
                new Vertex(point.getX() + cubeSize, point.getY() + cubeSize, point.getZ() - cubeSize),
                new Vertex(point.getX(), point.getY() + cubeSize, point.getZ() - cubeSize),
                new Vertex(point.getX(), point.getY(), point.getZ()),
                new Vertex(point.getX() + cubeSize, point.getY(), point.getZ()),
                new Vertex(point.getX() + cubeSize, point.getY() + cubeSize, point.getZ()),
                new Vertex(point.getX(), point.getY() + cubeSize, point.getZ())
            ),
            new Color(randomColor.nextInt(255), randomColor.nextInt(255), randomColor.nextInt(255))
        );
    }
}
