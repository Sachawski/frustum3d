package frustum3d.core.screen;

public class Triangle2D {

    private Pixel p1;
    private Pixel p2;
    private Pixel p3;

    public Triangle2D(Pixel p1, Pixel p2, Pixel p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public Pixel getP1() {
        return this.p1;
    }

    public Pixel getP2() {
        return this.p2;
    }
    
    public Pixel getP3() {
        return this.p3;
    }

    public void setP1(Pixel newP1) {
        this.p1 = newP1;
    }

    public void setP2(Pixel newP2) {
        this.p2 = newP2;
    }

    public void setP3(Pixel newP3) {
        this.p3 = newP3;
    }

    public String toString() {
        return "Triangle(" + this.p1 + " " + this.p2 + " " + this.p3 + ")";
    }
}
