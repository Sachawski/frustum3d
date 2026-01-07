package personal.frustum;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CameraManager3D implements KeyListener {

    Integer posX = 0;
    Integer posY = 500;
    Integer posZ = 0;
    double yaw = 0;
    double pitch = 0;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if ( KeyEvent.VK_D == keyCode) {
            posX += 50;
        }
        if ( KeyEvent.VK_Q == keyCode) {
            posX -= 50;
        }
        if ( KeyEvent.VK_Z == keyCode) {
            posZ -= 50;
        }
        if ( KeyEvent.VK_S == keyCode) {
            posZ += 50;
        }
        if ( KeyEvent.VK_SPACE == keyCode) {
            posY += 50;
        }
        if ( KeyEvent.VK_T == keyCode) {
            posY -= 50;
        }
        if ( KeyEvent.VK_RIGHT == keyCode) {
            yaw += 5;
        }
        if ( KeyEvent.VK_LEFT == keyCode) {
            yaw -= 5;
        }
        if ( KeyEvent.VK_UP == keyCode) {
            pitch += 0;
        }
        if ( KeyEvent.VK_DOWN == keyCode) {
            pitch -= 5;
        }

        System.out.println("Position:");
        System.out.println("Pos X:" + posX);
        System.out.println("Pos Y:" + posY);
        System.out.println("Pos Z:" + posZ);
    }

    @Override
    public void keyReleased(KeyEvent e){ 
    }
    
}