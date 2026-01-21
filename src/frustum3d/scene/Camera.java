package frustum3d.scene;

import frustum3d.core.ClipLine;
import frustum3d.core.ClipVertex;
import frustum3d.core.Line;
import frustum3d.core.Vertex;
import frustum3d.utils.Matrix;
import java.util.ArrayList;
import java.util.List;

public class Camera {

    public Vertex position;
    private double yaw;
    private double pitch;
    private final double width;
    private final double height;
    private final double aspectRatio; // square window
    private final double fovDegrees;
    private final double nearPlane;  // prevent division by zero
    private final double farPlane; // cull distant objects 
    private static double[][] planes = {
                                    {  1,  0,  0,  1 }, // left   :  x + w
                                    { -1,  0,  0,  1 }, // right  : -x + w
                                    {  0,  1,  0,  1 }, // bottom :  y + w
                                    {  0, -1,  0,  1 }, // top    : -y + w
                                    {  0,  0,  1,  1 }, // near   :  z + w
                                    {  0,  0, -1,  1 }  // far    : -z + w
                                };

    private static final Vertex worldUp = new Vertex(0,1,0);

    public Camera(double posX, double posY, double posZ, double yaw, double pitch, double width, double heigth) {
        this.position = new Vertex(posX, posY, posZ);
        this.yaw = yaw;
        this.pitch = pitch;
        this.width = width;
        this.height = heigth;
        this.aspectRatio = width/heigth;
        this.fovDegrees = 60.0;
        this.nearPlane = 1;
        this.farPlane = 100000.0;
    }

    public void moveUp() {
        Vertex forwardVector = forwardVector(Math.toRadians(yaw), Math.toRadians(pitch));
        Vertex rightVector = rightVector(forwardVector);
        this.position = this.position.add(upVector(rightVector, forwardVector).scale(50));
    }

    public void moveDown() {
        Vertex forwardVector = forwardVector(Math.toRadians(yaw), Math.toRadians(pitch));
        Vertex rightVector = rightVector(forwardVector);
        this.position = this.position.add(upVector(rightVector, forwardVector).scale(-50));
    }

    public void moveWorldUp() {
        this.position = this.position.add(worldUp.scale(50));
    }

    public void moveWorldDown() {
        this.position = this.position.add(worldUp.scale(-50));
    }

    public void moveForward() {        
        Vertex forwardVector = forwardVector(Math.toRadians(yaw), Math.toRadians(pitch));
        this.position = this.position.add(forwardVector.scale(50));
    }

    public void moveBackward() {
        Vertex forwardVector = forwardVector(Math.toRadians(yaw), Math.toRadians(pitch));
        this.position = this.position.add(forwardVector.scale(-50));
    }

    public void moveRight() {
        Vertex forwardVector = forwardVector(Math.toRadians(yaw), Math.toRadians(pitch));
        this.position = this.position.add(rightVector(forwardVector).scale(50));
    }

    public void moveLeft() {
        Vertex forwardVector = forwardVector(Math.toRadians(yaw), Math.toRadians(pitch));
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
            yaw = yaw + 1;
        }
    }

    public void rotateLeft(){
        if (pitch > -89) {
            yaw = yaw - 1;
        }
    }

    public Matrix createMVPMatrix() {
        Matrix projection = createProjectionMatrix();

        Matrix view = createViewMatrix();

        Matrix model = Matrix.identity(); // objects don't move
        
        return projection.multiply(view).multiply(model);
    }

public Matrix createViewMatrix() {
        Vertex eye = this.position;
        double yawRadian = Math.toRadians(yaw);
        double pitchRadian = Math.toRadians(pitch);
        // 1️⃣ Direction caméra
        Vertex forward = forwardVector(yawRadian, pitchRadian);

        // 2️⃣ Base orthonormée caméra (repère droit)
        Vertex right = worldUp.vectorialProduct(forward).normalize();
        Vertex up    = forward.vectorialProduct(right).normalize();

        // 3️⃣ Construction de la view matrix
        Matrix view = Matrix.identity();

        // rotation
        view.set(0, 0, right.getX());
        view.set(0, 1, right.getY());
        view.set(0, 2, right.getZ());

        view.set(1, 0, up.getX());
        view.set(1, 1, up.getY());
        view.set(1, 2, up.getZ());

        view.set(2, 0, -forward.getX());
        view.set(2, 1, -forward.getY());
        view.set(2, 2, -forward.getZ());

        view.set(0, 3, -right.dot(eye));
        view.set(1, 3, -up.dot(eye));
        view.set(2, 3, forward.dot(eye));
        return view;
    }
    
    public Matrix createProjectionMatrix() {        
        return Matrix.perspective(fovDegrees, aspectRatio, nearPlane, farPlane);
    }

    public boolean isPointBehindCamera(ClipVertex point) {
        return Double.isNaN(point.getX()) || 
               Double.isNaN(point.getY()) || 
               Double.isNaN(point.getZ());
    }



    public Vertex[] clipLine(ClipVertex p1, ClipVertex p2) {
        if (isPointBehindCamera(p1) && isPointBehindCamera(p2)) {
            return null;
        }
        
        double x1 = p1.getX(), y1 = p1.getY(), z1 = p1.getZ();
        double x2 = p2.getX(), y2 = p2.getY(), z2 = p2.getZ();
        
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
        
        Vertex clippedP1 = new Vertex(
            x1 + tNear * dx,
            y1 + tNear * dy,
            z1 + tNear * dz
        );
        
        Vertex clippedP2 = new Vertex(
            x1 + tFar * dx,
            y1 + tFar * dy,
            z1 + tFar * dz
        );
        
        return new Vertex[] {clippedP1, clippedP2};
    }
    
    public boolean isFaceFrontFacing(int[] faceVertices, Vertex[] transformedPoints) {
        // Get 3 vertices of the face (first 3 define the plane)
        Vertex v0 = transformedPoints[faceVertices[0]];
        Vertex v1 = transformedPoints[faceVertices[1]];
        Vertex v2 = transformedPoints[faceVertices[2]];
        
        // Calculate face normal using cross product
        Vertex edge1 = v1.substract(v0);
        Vertex edge2 = v2.substract(v0);
        Vertex normal = edge1.vectorialProduct(edge2);
        
        // Calculate face center
        Vertex faceCenter = new Vertex(
            (v0.getX() + v1.getX() + v2.getX()) / 3.0,
            (v0.getY() + v1.getY() + v2.getY()) / 3.0,
            (v0.getZ() + v1.getZ() + v2.getZ()) / 3.0
        );
        
        // Vector from camera to face center
        Vertex viewVector = faceCenter;
        
        // Dot product: if positive, face is back-facing (normal points away from camera)
        double dotProduct = normal.dot(viewVector);
        
        // If dot product < 0, normal points toward camera = front-facing
        return dotProduct < 0;
    }

    public boolean isFaceFrontFacing(Vertex[] transformedPoints) {
        // Get 3 vertices of the face (first 3 define the plane)
        Vertex v0 = transformedPoints[0];
        Vertex v1 = transformedPoints[1];
        Vertex v2 = transformedPoints[2];
        
        // Calculate face normal using cross product
        Vertex edge1 = v1.substract(v0);
        Vertex edge2 = v2.substract(v0);
        Vertex normal = edge1.vectorialProduct(edge2);
        
        // Calculate face center
        Vertex faceCenter = new Vertex(
            (v0.getX() + v1.getX() + v2.getX()) / 3.0,
            (v0.getY() + v1.getY() + v2.getY()) / 3.0,
            (v0.getZ() + v1.getZ() + v2.getZ()) / 3.0
        );
        
        // Vector from camera to face center
        Vertex viewVector = faceCenter;
        
        // Dot product: if positive, face is back-facing (normal points away from camera)
        double dotProduct = normal.dot(viewVector);
        
        // If dot product < 0, normal points toward camera = front-facing
        return dotProduct < 0;
    }

    public List<ClipLine> getClippedLineOptimized(List<ClipLine> lines) {
        List<ClipLine> result = new ArrayList<>();
        double eps = 1e-10;
        for (ClipLine line : lines) {
            ClipVertex p0 = line.from();
            ClipVertex p1 = line.to();
            double tEnter = 0.0;
            double tExit = 1.0;
            boolean isLineRejected = false;
            double x0 = p0.getX();
            double y0 = p0.getY();
            double z0 = p0.getZ();
            double w0 = p0.getW();
            double x1 = p1.getX();
            double y1 = p1.getY();
            double z1 = p1.getZ();
            double w1 = p1.getW();

            for (double[] plane : planes) {
                double f0 = plane[0]*x0 + plane[1]*y0 + plane[2]*z0 + plane[3]*w0;
                double f1 = plane[0]*x1 + plane[1]*y1 + plane[2]*z1 + plane[3]*w1;
                double fd = f1 - f0;
                if ((Math.abs(fd) < eps)) {
                    if (f0 < 0) {
                        isLineRejected = true;
                        break;
                    }
                    continue;
                }
                double t = - f0/fd;
                if (fd > 0) {
                    tEnter = Math.max(tEnter, t);
                } else {
                    tExit = Math.min(tExit, t);
                }
                if (tEnter > tExit) {
                    isLineRejected = true;
                    break;
                }
            }

            ClipVertex c0 = p0.lerp(p1, tEnter);
            ClipVertex c1 = p0.lerp(p1, tExit);
            if (!isLineRejected) {
               result.add(new ClipLine(c0, c1));
            }
        }
        return result;
    }

    public List<Line> toNDCSpace(List<ClipLine> clippedLines) {
        return clippedLines.stream().map( clippedLine -> {
            double fromX = clippedLine.from().getX();
            double fromY = clippedLine.from().getY();
            double fromZ = clippedLine.from().getZ();            
            double fromW = clippedLine.from().getW();
            double toX = clippedLine.to().getX();
            double toY = clippedLine.to().getY();
            double toZ = clippedLine.to().getZ();
            double toW = clippedLine.to().getW();
            return new Line(new Vertex(fromX/fromW, fromY/fromW, fromZ/fromW), new Vertex(toX/toW, toY/toW, toZ/toW));
        }).toList();
    }
    
    private Vertex forwardVector(double radianYaw, double radianPitch) {
        return new Vertex(
            Math.cos(radianPitch) * Math.sin(radianYaw),
            Math.sin(radianPitch),
            Math.cos(radianPitch) * Math.cos(radianYaw)
        ).normalize();
    }

    private Vertex rightVector(Vertex forwardVector) {
        return worldUp.vectorialProduct(forwardVector).normalize();
    }

    private Vertex upVector(Vertex rightVector, Vertex forwardVector) {
        return forwardVector.vectorialProduct(rightVector).normalize();
    }

    

}