package personal;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MovementManager2D implements KeyListener {

    Integer posX = 0;
    Integer posY = 0;
    Integer posZ = 0;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if ( KeyEvent.VK_D == keyCode) {
            System.out.print("Right");
            posX += 10;
        }
        if ( KeyEvent.VK_Q == keyCode) {
            System.out.print("Left");
            posX -= 10;
        }
        if ( KeyEvent.VK_Z == keyCode) {
            System.out.print("Up");
            posY -= 10;
        }
        if ( KeyEvent.VK_S == keyCode) {
            System.out.print("Down");
            posY += 10;
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e){ 
    }
    
}