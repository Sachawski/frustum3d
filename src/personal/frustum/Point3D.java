package personal.frustum;

public record Point3D(double x, double y, double z) {

    public Point3D add(Point3D point) {
        return new Point3D(this.x() + point.x(), this.y() + point.y(), this.z() + point.z());
    }

    public Point3D substract(Point3D point) {
        return new Point3D(this.x() - point.x(), this.y() - point.y(), this.z() - point.z());
    }

    public Point3D scale(Integer scale) {
        return new Point3D(scale*this.x(), scale*this.y(), scale*this.z());
    }

    // public Point3D projection(Point3D point) {
    //     Integer scalarProduct = this.x()*point.x() + this.y()*point.y() + this.z()*point.z() ; 
    //     return new Point3D(scalarProduct*point.x(), scalarProduct*point.y(), scalarProduct*point.z());
    // }

    public Point3D vectorialProduct(Point3D point) {
        return new Point3D(
            this.y()*point.z() - this.z()*point.y(),
            this.z()*point.x() - this.x()*point.z(),
            this.x()*point.y() - this.y()*point.x()
        );
    }
}