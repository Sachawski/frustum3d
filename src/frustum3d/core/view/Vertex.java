package frustum3d.core.view;

public class Vertex {

    private double x;
    private double y;
    private double z;

    public Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public Vertex add(Vertex point) {
        return new Vertex(this.getX() + point.getX(), this.getY() + point.getY(), this.getZ() + point.getZ());
    }

    public Vertex substract(Vertex point) {
        return new Vertex(this.getX() - point.getX(), this.getY() - point.getY(), this.getZ() - point.getZ());
    }

    public double dot(Vertex point) {
        return this.getX()*point.getX() + this.getY()*point.getY() + this.getZ()*point.getZ();
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
        return "Point3D(" + this.x + "," + this.y + "," + this.z + ")";
    }
}