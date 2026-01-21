package frustum3d.utils;

import frustum3d.core.ClipVertex;
import frustum3d.core.Vertex;

// MADE WITH AI FOR SIMPLICITY

public class Matrix {
    private final double[][] elements;
    private static final int SIZE = 4;

    public Matrix() {
        this.elements = new double[SIZE][SIZE];
    }

    private Matrix(double[][] elements) {
        this.elements = new double[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(elements[i], 0, this.elements[i], 0, SIZE);
        }
    }

    public double get(int row, int col) {
        return elements[row][col];
    }

    public void set(int row, int col, double value) {
        elements[row][col] = value;
    }   
     
    public Matrix copyAndset(int row, int col, double value) {
        double[][] newElements = deepCopy(elements);
        newElements[row][col] = value;
        return new Matrix(newElements);
    }

    public Matrix extract(int raw, int col, int height, int width) {
        Matrix result = new Matrix();
        if (raw+height > SIZE || col+width > SIZE) {
            return Matrix.identity();
        }
        for (int i = raw; i < height; i++) {
            for (int j = col ; j < width; j++) {
                result.set(i-raw, j-col, this.get(i, j));
            }
        }
        return result;
    }

    private double[][] deepCopy(double[][] matrix) {
        return java.util.Arrays.stream(matrix).map(el -> el.clone()).toArray($ -> matrix.clone());
    }

    public static Matrix identity() {
        Matrix matrix = new Matrix();
        for (int i = 0; i < SIZE; i++) {
            matrix.set(i, i, 1.0);
        }
        return matrix;
    }

    public Matrix add(Matrix other) {
        Matrix result = new Matrix();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                result.set(i, j, this.get(i, j) + other.get(i, j));
            }
        }
        return result;
    }

    public Matrix multiply(Matrix other) {
        Matrix result = new Matrix();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                double sum = 0.0;
                for (int k = 0; k < SIZE; k++) {
                    sum += this.get(i, k) * other.get(k, j);
                }
                result.set(i, j, sum);
            }
        }
        return result;
    }

    public Vertex multiply(Vertex point) {
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ();
        double w = 1.0;

        double newX = this.get(0, 0) * x + this.get(0, 1) * y + this.get(0, 2) * z + this.get(0, 3) * w;
        double newY = this.get(1, 0) * x + this.get(1, 1) * y + this.get(1, 2) * z + this.get(1, 3) * w;
        double newZ = this.get(2, 0) * x + this.get(2, 1) * y + this.get(2, 2) * z + this.get(2, 3) * w;

        return new Vertex(newX, newY, newZ);
    }

    public ClipVertex multiplyClip(Vertex point) {
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ();
        double w = 1.0;

        double newX = this.get(0, 0) * x + this.get(0, 1) * y + this.get(0, 2) * z + this.get(0, 3) * w;
        double newY = this.get(1, 0) * x + this.get(1, 1) * y + this.get(1, 2) * z + this.get(1, 3) * w;
        double newZ = this.get(2, 0) * x + this.get(2, 1) * y + this.get(2, 2) * z + this.get(2, 3) * w;
        double newW = this.get(3, 0) * x + this.get(3, 1) * y + this.get(3, 2) * z + this.get(3, 3) * w;

        return new ClipVertex(newX, newY, newZ, newW);
    }

    public static Matrix rotationX(double angleRadians) {
        Matrix matrix = Matrix.identity();
        double cos = Math.cos(angleRadians);
        double sin = Math.sin(angleRadians);
        
        matrix.set(1, 1, cos);
        matrix.set(1, 2, -sin);
        matrix.set(2, 1, sin);
        matrix.set(2, 2, cos);
        
        return matrix;
    }

    public static Matrix rotationY(double angleRadians) {
        Matrix matrix = Matrix.identity();
        double cos = Math.cos(angleRadians);
        double sin = Math.sin(angleRadians);
        
        matrix.set(0, 0, cos);
        matrix.set(0, 2, sin);
        matrix.set(2, 0, -sin);
        matrix.set(2, 2, cos);
        
        return matrix;
    }

    public static Matrix rotationZ(double angleRadians) {
        Matrix matrix = Matrix.identity();
        double cos = Math.cos(angleRadians);
        double sin = Math.sin(angleRadians);
        
        matrix.set(0, 0, cos);
        matrix.set(0, 1, -sin);
        matrix.set(1, 0, sin);
        matrix.set(1, 1, cos);
        
        return matrix;
    }

    public static Matrix perspective(double fovDegrees, double aspectRatio, double nearPlane, double farPlane) {
        Matrix matrix = new Matrix();
        
        double fovRadians = Math.toRadians(fovDegrees);
        double f = 1.0 / Math.tan(fovRadians / 2.0);
        
        matrix.set(0, 0, f / aspectRatio);
        matrix.set(1, 1, f);
        matrix.set(2, 2, (farPlane + nearPlane) / (nearPlane - farPlane));
        matrix.set(2, 3, (2.0 * farPlane * nearPlane) / (nearPlane - farPlane));
        matrix.set(3, 2, -1.0);
        matrix.set(3, 3, 0.0);
        
        return matrix;
    }

    public static Matrix lookAt(Vertex eye, Vertex target, Vertex up) {
        double eyeX = eye.getX();
        double eyeY = eye.getY();
        double eyeZ = eye.getZ();
        
        double targetX = target.getX();
        double targetY = target.getY();
        double targetZ = target.getZ();
        
        double upX = up.getX();
        double upY = up.getY();
        double upZ = up.getZ();

        double zAxisX = eyeX - targetX;
        double zAxisY = eyeY - targetY;
        double zAxisZ = eyeZ - targetZ;
        
        double zLength = Math.sqrt(zAxisX * zAxisX + zAxisY * zAxisY + zAxisZ * zAxisZ);
        zAxisX /= zLength;
        zAxisY /= zLength;
        zAxisZ /= zLength;

        double xAxisX = upY * zAxisZ - upZ * zAxisY;
        double xAxisY = upZ * zAxisX - upX * zAxisZ;
        double xAxisZ = upX * zAxisY - upY * zAxisX;
        
        double xLength = Math.sqrt(xAxisX * xAxisX + xAxisY * xAxisY + xAxisZ * xAxisZ);
        xAxisX /= xLength;
        xAxisY /= xLength;
        xAxisZ /= xLength;

        double yAxisX = zAxisY * xAxisZ - zAxisZ * xAxisY;
        double yAxisY = zAxisZ * xAxisX - zAxisX * xAxisZ;
        double yAxisZ = zAxisX * xAxisY - zAxisY * xAxisX;

        Matrix matrix = Matrix.identity();
        
        matrix.set(0, 0, xAxisX);
        matrix.set(0, 1, xAxisY);
        matrix.set(0, 2, xAxisZ);
        matrix.set(0, 3, -(xAxisX * eyeX + xAxisY * eyeY + xAxisZ * eyeZ));
        
        matrix.set(1, 0, yAxisX);
        matrix.set(1, 1, yAxisY);
        matrix.set(1, 2, yAxisZ);
        matrix.set(1, 3, -(yAxisX * eyeX + yAxisY * eyeY + yAxisZ * eyeZ));
        
        matrix.set(2, 0, zAxisX);
        matrix.set(2, 1, zAxisY);
        matrix.set(2, 2, zAxisZ);
        matrix.set(2, 3, -(zAxisX * eyeX + zAxisY * eyeY + zAxisZ * eyeZ));
        
        matrix.set(3, 3, 1.0);
        
        return matrix;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Matrix:\n");
        for (int i = 0; i < SIZE; i++) {
            sb.append("[");
            for (int j = 0; j < SIZE; j++) {
                sb.append(String.format("%8.3f", this.get(i, j)));
                if (j < SIZE - 1) sb.append(", ");
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
}