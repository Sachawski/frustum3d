package personal.frustum.core;

public class Triangle {

    private Point3D p1;
    private Point3D p2;
    private Point3D p3;

    public Triangle(Point3D p1, Point3D p2, Point3D p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public Point3D getP1() {
        return this.p1;
    }

    public Point3D getP2() {
        return this.p2;
    }
    
    public Point3D getP3() {
        return this.p3;
    }

    public void setP1(Point3D newP1) {
        this.p1 = newP1;
    }

    public void setP2(Point3D newP2) {
        this.p2 = newP2;
    }

    public void setP3(Point3D newP3) {
        this.p3 = newP3;
    }

    public String toString() {
        return "Triangle(" + this.p1 + " " + this.p2 + " " + this.p3 + ")";
    }
}
