package frustum3d.core.clip;

import frustum3d.core.view.Vertex;

public class ClipVertex extends Vertex {

    private double w;

    public ClipVertex(double x, double y, double z, double w) {
        super(x, y, z);
        this.w = w;
    }

    public double getW() {
        return w;
    }

    public Vertex toVertex() {
        double divider = this.getW();
        return new Vertex(
            this.getX()/divider,
            this.getY()/divider,
            this.getZ()/divider);
    }

    public ClipVertex lerp(ClipVertex other, double t) {
        return new ClipVertex(
            this.getX() + (other.getX() - this.getX()) * t,
            this.getY() + (other.getY() - this.getY()) * t,
            this.getZ() + (other.getZ() - this.getZ()) * t,
            this.getW() + (other.getW() - this.getW()) * t

        );
    }

    public ClipVertex add(ClipVertex other) {
        return new ClipVertex(
            this.getX() + other.getX(),
            this.getY() + other.getY(),
            this.getZ() + other.getZ(),
            this.getW() + other.getW());
    }

    @Override
    public String toString(){
        return "ClipVertex(" + this.getX() + "," + this.getY() + "," + this.getZ() + " " + this.getW() + ")";
    }
}