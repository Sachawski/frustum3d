package personal.frustum;

public class Camera {

    
    private Point3D position;
    private double yaw;
    private double pitch;
    private final double width;
    private final double height;


    private static final Point3D worldUp = new Point3D(0,1,0);

    public Camera(double posX, double posY, double posZ, double yaw, double pitch, double width, double heigth) {
        this.position = new Point3D(posX, posY, posZ);
        this.yaw = yaw;
        this.pitch = pitch;
        this.width = width;
        this.height = heigth;
    }

    public void moveUp() {
        Point3D forwardVector = forwardVector(Math.toRadians(yaw), Math.toRadians(pitch));
        Point3D rightVector = rightVector(forwardVector);
        this.position = this.position.add(upVector(rightVector, forwardVector).scale(50));
    }

    public void moveDown() {
        Point3D forwardVector = forwardVector(Math.toRadians(yaw), Math.toRadians(pitch));
        Point3D rightVector = rightVector(forwardVector);
        this.position = this.position.add(upVector(rightVector, forwardVector).scale(-50));
    }

    public void moveForward() {        
        Point3D forwardVector = forwardVector(Math.toRadians(yaw), Math.toRadians(pitch));
        this.position = this.position.add(forwardVector.scale(50));
    }

    public void moveBackward() {
        Point3D forwardVector = forwardVector(Math.toRadians(yaw), Math.toRadians(pitch));
        this.position = this.position.add(forwardVector.scale(-50));
    }

    public void moveRight() {
        Point3D forwardVector = forwardVector(Math.toRadians(yaw), Math.toRadians(pitch));
        this.position = this.position.add(rightVector(forwardVector).scale(50));
    }

    public void moveLeft() {
        Point3D forwardVector = forwardVector(Math.toRadians(yaw), Math.toRadians(pitch));
        this.position = this.position.add(rightVector(forwardVector).scale(-50));
    }

    public void rotateUp() {
        if (pitch < 89) {
            pitch = pitch + 1;
        }
    }

    public void rotateDown(){
        if (pitch > -89) {
            pitch = pitch - 1;
        }
    }

    public void rotateRight(){
        if (pitch < 89) {
            yaw = yaw - 1;
        }
    }

    public void rotateLeft(){
        if (pitch > -89) {
            yaw = yaw + 1;
        }
    }

    public Matrix createMVPMatrix() {
        Matrix projection = createProjectionMatrix();

        Matrix view = createViewMatrix(
            position.getPosX(),
            position.getPosY(),
            position.getPosZ(),
            yaw,
            pitch
        );
        Matrix model = Matrix.identity(); // objects don't move
        
        return projection.multiply(view).multiply(model);
    }

    public boolean isPointBehindCamera(Point3D point) {
        return Double.isNaN(point.getPosX()) || 
               Double.isNaN(point.getPosY()) || 
               Double.isNaN(point.getPosZ());
    }

    public Point3D[] clipLine(Point3D p1, Point3D p2) {
        if (isPointBehindCamera(p1) && isPointBehindCamera(p2)) {
            return null;
        }
        
        double x1 = p1.getPosX(), y1 = p1.getPosY(), z1 = p1.getPosZ();
        double x2 = p2.getPosX(), y2 = p2.getPosY(), z2 = p2.getPosZ();
        
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dz = z2 - z1;
        
        double tNear = 0.0;
        double tFar = 1.0;
        
        double[] p = {x1, y1, z1};
        double[] d = {dx, dy, dz};
        double[] bounds = {-1, -1, -1};
        double[] bounds2 = {1, 1, 1};
        
        for (int i = 0; i < 3; i++) {
            if (Math.abs(d[i]) < 1e-10) {
                if (p[i] < bounds[i] || p[i] > bounds2[i]) {
                    return null;
                }
            } else {
                double t1 = (bounds[i] - p[i]) / d[i];
                double t2 = (bounds2[i] - p[i]) / d[i];
                
                if (t1 > t2) {
                    double temp = t1;
                    t1 = t2;
                    t2 = temp;
                }
                
                tNear = Math.max(tNear, t1);
                tFar = Math.min(tFar, t2);
                
                if (tNear > tFar) {
                    return null;
                }
            }
        }
        
        if (tNear > 1.0 || tFar < 0.0) {
            return null;
        }
        
        tNear = Math.max(0.0, tNear);
        tFar = Math.min(1.0, tFar);
        
        if (tNear == tFar) {
            return null;
        }
        
        Point3D clippedP1 = new Point3D(
            x1 + tNear * dx,
            y1 + tNear * dy,
            z1 + tNear * dz
        );
        
        Point3D clippedP2 = new Point3D(
            x1 + tFar * dx,
            y1 + tFar * dy,
            z1 + tFar * dz
        );
        
        return new Point3D[] {clippedP1, clippedP2};
    }
    
    private Point3D forwardVector(double radianYaw, double radianPitch) {
        return new Point3D(
            Math.cos(radianPitch) * Math.sin(radianYaw),
            Math.sin(radianPitch),
            Math.cos(radianPitch) * Math.cos(radianYaw)
        ).normalize();
    }

    private Point3D rightVector(Point3D forwardVector) {
        return forwardVector.vectorialProduct(worldUp).normalize();
    }

    private Point3D upVector(Point3D rightVector, Point3D forwardVector) {
        return rightVector.vectorialProduct(forwardVector).normalize();
    }

     private Matrix createViewMatrix(double posX, double  posY, double  posZ, double yaw, double pitch) {
        Point3D eye = new Point3D(posX, posY, posZ);
        Point3D target = new Point3D(
            posX + Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)),
            posY + Math.sin(Math.toRadians(pitch)),
            posZ + Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch))
        );
        Point3D up = new Point3D(0, 1, 0);
        return Matrix.lookAt(eye, target, up);
    }
    
    private Matrix createProjectionMatrix() {
        double aspectRatio = this.width/this.height; // square window
        double fovDegrees = 60.0;
        double nearPlane = 1;  // prevent division by zero
        double farPlane = 100000.0; // cull distant objects
        
        return Matrix.perspective(fovDegrees, aspectRatio, nearPlane, farPlane);
    }

}