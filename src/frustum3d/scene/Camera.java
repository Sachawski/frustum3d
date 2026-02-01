package frustum3d.scene;

import frustum3d.core.clip.ClipLine;
import frustum3d.core.clip.ClipVertex;
import frustum3d.core.view.Line;
import frustum3d.core.view.Vertex;
import frustum3d.utils.Matrix;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Camera {

    public Vertex position;
    private double yaw;
    private double pitch;
    private final double aspectRatio; // square window
    private final double fovDegrees;
    private final double nearPlane;  // prevent division by zero
    private final double farPlane; // cull distant objects 
    private static final double[][] planes = {
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
        this.aspectRatio = width/heigth;
        this.fovDegrees = 60.0;
        this.nearPlane = 1;
        this.farPlane = 100000.0;
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
        yaw = yaw + 1;
    }

    public void rotateLeft(){
        yaw = yaw - 1;
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

    
    public boolean isFaceFrontFacing(int[] faceVertices, Vertex[] transformedPoints) {
        // Get 3 vertices of the face (first 3 define the plane)
        Vertex v0 = transformedPoints[faceVertices[0]];
        Vertex v1 = transformedPoints[faceVertices[1]];
        Vertex v2 = transformedPoints[faceVertices[2]];
        
        // Calculate face normal using cross product
        Vertex edge1 = v1.substract(v0);
        Vertex edge2 = v2.substract(v0);
        Vertex normal = edge1.vectorialProduct(edge2);
        
        // Vector from camera to face center
        Vertex viewVector = new Vertex(
                (v0.getX() + v1.getX() + v2.getX()) / 3.0,
                (v0.getY() + v1.getY() + v2.getY()) / 3.0,
                (v0.getZ() + v1.getZ() + v2.getZ()) / 3.0
        );
        
        // Dot product: if positive, face is back-facing (normal points away from camera)
        double dotProduct = normal.dot(viewVector);
        
        // If dot product > 0, normal points toward camera = front-facing
        return dotProduct > 0;
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

    public List<ClipLine> getClippedVertices(List<ClipVertex> vertices) {
        ClipVertex curr;
        ClipVertex prev;
        for (double[] plane : planes) {
            List<ClipVertex> output = new ArrayList<>();
            prev = vertices.getLast();
            boolean prevInside = inside(plane, prev);
            for (ClipVertex vertex : vertices) {
                curr = vertex;
                boolean currInside = inside(plane, curr);
                if (currInside && prevInside) {
                    output.add(curr);
                } else if (currInside) {
                    output.add(prev.lerp(curr, planIntersection(plane, prev, curr)));
                    output.add(curr);
                } else if (prevInside) {
                    output.add(prev.lerp(curr, planIntersection(plane, prev, curr)));
                }
                prev = curr;
                prevInside = currInside;
            }
            vertices = output;
            if (vertices.isEmpty()) {
                break;
            }
        }
        if (!vertices.isEmpty()) {
            vertices.add(vertices.getFirst());
        }
        List<ClipVertex> finalVertices = vertices;
        return IntStream.range(0, vertices.size()-1).mapToObj(i -> new ClipLine(finalVertices.get(i), finalVertices.get(i+1))).toList();
    }

    private boolean inside(double[] plane, ClipVertex vertex) {
        return (plane[0]*vertex.getX() +
                plane[1]*vertex.getY() +
                plane[2]*vertex.getZ() +
                plane[3]*vertex.getW() >= 0);
    }

    private double planIntersection(double[] plane, ClipVertex vertex1, ClipVertex vertex2 ) {
        double f0 = plane[0]*vertex1.getX() +
                plane[1]*vertex1.getY() +
                plane[2]*vertex1.getZ() +
                plane[3]*vertex1.getW();
        double f1 = plane[0]*vertex2.getX() +
                plane[1]*vertex2.getY() +
                plane[2]*vertex2.getZ() +
                plane[3]*vertex2.getW();
        return f0 / (f0 - f1);
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

}