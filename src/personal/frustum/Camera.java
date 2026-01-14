package personal.frustum;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Camera implements KeyListener {


    private final Point3D position;
    private double yaw;
    private double pitch;

    public Camera(double posX, double posY, double posZ, double yaw, double pitch) {
        this.position = new Point3D(posX, posY, posZ);
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public void setPosX(double newPosX) {
        this.position.setPosX(newPosX);
    }

    public void setPosY(double newPosY) {
        this.position.setPosY(newPosY);
    }

    public void setPosZ(double newPosZ) {
        this.position.setPosZ(newPosZ);
    }

    public void setYaw(double newYaw) {
        this.yaw = newYaw;
    }

    public void setPitch(double newPitch) {
        this.pitch = newPitch;
    }

    public double getPosX() {
        return this.position.getPosX();
    }

    public double getPosY() {
        return this.position.getPosY();
    }

    public double getPosZ() {
        return this.position.getPosZ();
    }

    public double getYaw() {
        return this.yaw;
    }

    public double getPitch() {
        return this.pitch;
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
    
    public Matrix createProjectionMatrix() {
        double aspectRatio = 1000.0 / 1000.0; // square window
        double fovDegrees = 60.0;
        double nearPlane = 1.0;  // prevent division by zero
        double farPlane = 10000.0; // cull distant objects
        
        return Matrix.perspective(fovDegrees, aspectRatio, nearPlane, farPlane);
    }

    public Matrix createMVPMatrix() {
        Matrix projection = createProjectionMatrix();
        // Matrix view = createViewMatrix();
        Matrix view = createViewMatrix(
            position.getPosX(),
            position.getPosY(),
            position.getPosZ(),
            yaw,
            pitch
        );
        Matrix model = Matrix.identity(); // objects don't move
        
        // MVP = Projection * View * Model
        return projection.multiply(view).multiply(model);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if ( KeyEvent.VK_D == keyCode) {
            this.setPosX(this.getPosX() + 50);
        }
        if ( KeyEvent.VK_Q == keyCode) {
            this.setPosX(this.getPosX() - 50);
        }
        if ( KeyEvent.VK_Z == keyCode) {
            this.setPosZ(this.getPosZ() - 50);
        }
        if ( KeyEvent.VK_S == keyCode) {
            this.setPosZ(this.getPosZ() + 50);
        }
        if ( KeyEvent.VK_SPACE == keyCode) {
            this.setPosY(this.getPosY() + 50);
        }
        if ( KeyEvent.VK_T == keyCode) {
            this.setPosY(this.getPosY() - 50);
        }
        if ( KeyEvent.VK_RIGHT == keyCode) {
            this.setYaw(this.getYaw() - 1);
        }
        if ( KeyEvent.VK_LEFT == keyCode) {
            this.setYaw(this.getYaw() + 1); 
        }
        if ( KeyEvent.VK_UP == keyCode) {
            this.setPitch(this.getPitch() - 1);
        }
        if ( KeyEvent.VK_DOWN == keyCode) {
            this.setPitch(this.getPitch() + 1);
        }

        // System.out.println("Position:");
        // System.out.println("Pos X:" + this.getPosX());
        // System.out.println("Pos Y:" + this.getPosY());
        // System.out.println("Pos Z:" + this.getPosZ());

        // System.out.println("Yaw:" + this.getYaw());
        // System.out.println("Pitch:" + this.getPitch());
    }

    @Override
    public void keyReleased(KeyEvent e){ 
    }
    
}