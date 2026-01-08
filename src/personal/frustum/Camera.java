package personal.frustum;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Camera implements KeyListener {

    private Integer posX;
    private Integer posY;
    private Integer posZ;
    private Integer yaw;
    private Integer pitch;

    public Camera(Integer posX, Integer posY, Integer posZ, Integer yaw, Integer pitch) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public void setPosX(Integer newPosX) {
        this.posX = newPosX;
    }

    public void setPosY(Integer newPosY) {
        this.posY = newPosY;
    }

    public void setPosZ(Integer newPosZ) {
        this.posZ = newPosZ;
    }

    public void setYaw(Integer newYaw) {
        this.yaw = newYaw;
    }

    public void setPitch(Integer newPitch) {
        this.pitch = newPitch;
    }

    public Integer getPosX() {
        return this.posX;
    }

    public Integer getPosY() {
        return this.posY;
    }

    public Integer getPosZ() {
        return this.posZ;
    }

    public Integer getYaw() {
        return this.yaw;
    }

    public Integer getPitch() {
        return this.pitch;
    }

    private Matrix createViewMatrix() {

        Matrix rotation = Matrix.rotationY(Math.toRadians(yaw))
                                .multiply(Matrix.rotationX(Math.toRadians(pitch)));

        Matrix translation = Matrix.translation(-posX, -posY, -posZ);

        return translation.multiply(rotation);
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
        Matrix view = createViewMatrix();
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
            this.setYaw(this.getYaw() + 5);
        }
        if ( KeyEvent.VK_LEFT == keyCode) {
            this.setYaw(this.getYaw() - 5);
        }
        if ( KeyEvent.VK_UP == keyCode) {
            this.setPitch(this.getPitch() + 5);
        }
        if ( KeyEvent.VK_DOWN == keyCode) {
            this.setPitch(this.getPitch() - 5);
        }

        System.out.println("Position:");
        System.out.println("Pos X:" + this.getPosX());
        System.out.println("Pos Y:" + this.getPosY());
        System.out.println("Pos Z:" + this.getPosZ());
    }

    @Override
    public void keyReleased(KeyEvent e){ 
    }
    
}