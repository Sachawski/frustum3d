package personal.frustum;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Camera implements KeyListener, MouseMotionListener {

    private Point3D position;
    private double yaw;
    private double pitch;
    private double mouseX = 0;
    private double mouseY = 0;

    private static final Point3D worldUp = new Point3D(0,1,0);

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

    public void setMouseX(double newMouseX) {
        this.mouseX = newMouseX;
    }

    public void setMouseY(double newMouseY) {
        this.mouseY = newMouseY;
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

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        double radianYaw = Math.toRadians(yaw);
        double radianPitch = Math.toRadians(pitch);
        
        Point3D forward = new Point3D(
            Math.cos(radianPitch) * Math.sin(radianYaw),
            Math.sin(radianPitch),
            Math.cos(radianPitch) * Math.cos(radianYaw)
        ).normalize();

        Point3D right = forward.vectorialProduct(worldUp).normalize();

        Point3D up = right.vectorialProduct(forward).normalize();

        if ( KeyEvent.VK_D == keyCode) { 
            this.position = this.position.add(right.scale(50)); 
        }
        if ( KeyEvent.VK_Q == keyCode) {
            this.position = this.position.add(right.scale(-50));        
        }
        if ( KeyEvent.VK_Z == keyCode) {
            this.position = this.position.add(forward.scale(50));        
        }
        if ( KeyEvent.VK_S == keyCode) {
            this.position = this.position.add(forward.scale(-50));        
        }
        if ( KeyEvent.VK_SPACE == keyCode) {
            this.position = this.position.add(up.scale(50));  
        }
        if ( KeyEvent.VK_T == keyCode) {
            this.position = this.position.add(up.scale(-50));         
        }
        System.out.println(this.position.toString());
    }

    @Override
    public void keyReleased(KeyEvent e){ 
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        double newMouseX = e.getX();
        double newMouseY = e.getY();
        if (pitch < 89) {
            if (newMouseY > this.mouseY) {
                this.setPitch(this.getPitch() + 1);
            }
        }
        if (pitch > -89) {
            if (newMouseY < this.mouseY) {
                this.setPitch(this.getPitch() - 1);
            }
        }

        if (newMouseX < this.mouseX) {
            this.setYaw(this.getYaw() + 1);
        }
        if (newMouseX > this.mouseX) {
            this.setYaw(this.getYaw() - 1);
        }
        this.mouseX = newMouseX;
        this.mouseY = newMouseY;

        System.out.println("Yaw :" + yaw);        


    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
    }
    
}