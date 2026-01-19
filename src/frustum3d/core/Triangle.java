package frustum3d.core;

public class Triangle {

    private Vertex p1;
    private Vertex p2;
    private Vertex p3;

    public Triangle(Vertex p1, Vertex p2, Vertex p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public Vertex getP1() {
        return this.p1;
    }

    public Vertex getP2() {
        return this.p2;
    }
    
    public Vertex getP3() {
        return this.p3;
    }

    public void setP1(Vertex newP1) {
        this.p1 = newP1;
    }

    public void setP2(Vertex newP2) {
        this.p2 = newP2;
    }

    public void setP3(Vertex newP3) {
        this.p3 = newP3;
    }

    public String toString() {
        return "Triangle(" + this.p1 + " " + this.p2 + " " + this.p3 + ")";
    }
}
