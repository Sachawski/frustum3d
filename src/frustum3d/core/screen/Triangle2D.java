package frustum3d.core.screen;

public class Triangle2D {

    private Pixel p0;
    private Pixel p1;
    private Pixel p2;

    public Triangle2D(Pixel p1, Pixel p2, Pixel p3) {
        this.p0 = p1;
        this.p1 = p2;
        this.p2 = p3;
    }

    public Pixel getP0() {
        return this.p0;
    }

    public Pixel getP1() {
        return this.p1;
    }
    
    public Pixel getP2() {
        return this.p2;
    }

    public void setP0(Pixel newP1) {
        this.p0 = newP1;
    }

    public void setP1(Pixel newP2) {
        this.p1 = newP2;
    }

    public void setP2(Pixel newP3) {
        this.p2 = newP3;
    }


    public String toString() {
        return "Triangle(" + this.p0 + " " + this.p1 + " " + this.p2 + ")";
    }
}
