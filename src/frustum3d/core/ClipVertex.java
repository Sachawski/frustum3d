package frustum3d.core;

public class ClipVertex {

    private double x;
    private double y;
    private double z;
    private double w;

    public ClipVertex(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getW() {
        return w;
    }

    public void setX(double newX) {
        this.x = newX;
    }

    public void setY(double newY) {
        this.y = newY;
    }

    public void setZ(double newZ) {
        this.z = newZ;
    }

    public void setW(double newW) {
        this.z = newW;
    }

    public Vertex toPoint3D() {
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

    public ClipVertex substract(ClipVertex other) {
        return new ClipVertex(
            this.getX() - other.getX(),
            this.getY() - other.getY(),
            this.getZ() - other.getZ(),
            this.getW() - other.getW());
    }

    public double dot(ClipVertex other) {
        return this.getX()*other.getX() + this.getY()*other.getY() + this.getZ()*other.getZ();
    }

    public Vertex scale(Integer scale) {
        return new Vertex(scale*this.getX(), scale*this.getY(), scale*this.getZ());
    }

    public Vertex normalize() {
        double magnitude = Math.sqrt(
            Math.pow(this.getX(), 2) + 
            Math.pow(this.getY(), 2) + 
            Math.pow(this.getZ(), 2)
        );
        
        if (magnitude == 0) {
            return new Vertex(0, 0, 0);
        }
        
        return new Vertex(
            this.getX() / magnitude,
            this.getY() / magnitude,
            this.getZ() / magnitude
        );
    }

    public Vertex vectorialProduct(Vertex point) {
        return new Vertex(
            this.getY()*point.getZ() - this.getZ()*point.getY(),
            this.getZ()*point.getX() - this.getX()*point.getZ(),
            this.getX()*point.getY() - this.getY()*point.getX()
        );
    }

    @Override
    public String toString(){
        return "Vertex(" + this.x + "," + this.y + "," + this.z + ")";
    }
}