package personal;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MovementManager3D implements KeyListener {

    Integer posX = 0;
    Integer posY = 0;
    Integer posZ = 10;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if ( KeyEvent.VK_D == keyCode) {
            posX += 10;
        }
        if ( KeyEvent.VK_Q == keyCode) {
            posX -= 10;
        }
        if ( KeyEvent.VK_Z == keyCode) {
            posZ -= 1;
        }
        if ( KeyEvent.VK_S == keyCode) {
            posZ += 1;
        }
        if ( KeyEvent.VK_SPACE == keyCode) {
            posY += 10;
        }
        if ( KeyEvent.VK_T == keyCode) {
            posY -= 10;
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e){ 
    }
    
}