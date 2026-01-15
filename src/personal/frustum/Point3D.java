package personal.frustum;

public class Point3D {

    private double posX;
    private double posY;
    private double posZ;

    public Point3D(double posX, double posY, double posZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getPosZ() {
        return posZ;
    }

    public void setPosX(double newPosX) {
        this.posX = newPosX;
    }

    public void setPosY(double newPosY) {
        this.posY = newPosY;
    }

    public void setPosZ(double newPosZ) {
        this.posZ = newPosZ;
    }

    public Point3D add(Point3D point) {
        return new Point3D(this.getPosX() + point.getPosX(), this.getPosY() + point.getPosY(), this.getPosZ() + point.getPosZ());
    }

    public Point3D substract(Point3D point) {
        return new Point3D(this.getPosX() - point.getPosX(), this.getPosY() - point.getPosY(), this.getPosZ() - point.getPosZ());
    }

    public Point3D scale(Integer scale) {
        return new Point3D(scale*this.getPosX(), scale*this.getPosY(), scale*this.getPosZ());
    }

    // public Point3D projection(Point3D point) {
    //     Integer scalarProduct = this.getPosX()*point.getPosX() + this.getPosY()*point.getPosY() + this.getPosZ()*point.getPosZ() ; 
    //     return new Point3D(scalarProduct*point.getPosX(), scalarProduct*point.getPosY(), scalarProduct*point.getPosZ());
    // }

    public Point3D normalize() {
        double magnitude = Math.sqrt(
            Math.pow(this.getPosX(), 2) + 
            Math.pow(this.getPosY(), 2) + 
            Math.pow(this.getPosZ(), 2)
        );
        
        if (magnitude == 0) {
            return new Point3D(0, 0, 0);
        }
        
        return new Point3D(
            this.getPosX() / magnitude,
            this.getPosY() / magnitude,
            this.getPosZ() / magnitude
        );
    }

    public Point3D vectorialProduct(Point3D point) {
        return new Point3D(
            this.getPosY()*point.getPosZ() - this.getPosZ()*point.getPosY(),
            this.getPosZ()*point.getPosX() - this.getPosX()*point.getPosZ(),
            this.getPosX()*point.getPosY() - this.getPosY()*point.getPosX()
        );
    }

    @Override
    public String toString(){
        return "Point3D(" + this.posX + "," + this.posY + "," + this.posZ + ")";
    }
}