package frustum3d.scene;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;

public class CameraMovementManager implements KeyListener, MouseListener, MouseMotionListener {

    private final Camera camera;
    private double mouseX = 0;
    private double mouseY = 0;

    public Map<String,Boolean> movementMap = new HashMap<>() {{
        put("forward",false);
        put("backward",false);
        put("up",false);
        put("down",false);
        put("right",false);
        put("left",false);
    }};

    public Map<String,Boolean> rotationMap = new HashMap<>() {{
        put("up",false);
        put("down",false);
        put("right",false);
        put("left",false);
    }};

    public CameraMovementManager(Camera camera) {
        this.camera = camera;
    }

    public void driveCamera() {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    if (this.movementMap.get("right")) {
                        camera.moveRight();
                    }
                    if (this.movementMap.get("left")) {
                        camera.moveLeft();
                    }
                    if (this.movementMap.get("forward")) {
                        camera.moveForward();
                    }
                    if (this.movementMap.get("backward")) {
                        camera.moveBackward();
                    }
                    if (this.movementMap.get("up")) {
                        camera.moveWorldUp();
                    }
                    if (this.movementMap.get("down")) {
                        camera.moveWorldDown();
                    }
                    if (this.rotationMap.get("right")) {
                        camera.rotateRight();
                    }
                    if (this.rotationMap.get("left")) {
                        camera.rotateLeft();
                    }
                    if (this.rotationMap.get("up")) {
                        camera.rotateUp();
                    }
                    if (this.rotationMap.get("down")) {
                        camera.rotateDown();
                    }
                    Thread.sleep(7);
                }
                
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(0);
            }
        });
        
        thread.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_D -> this.movementMap.put("right", true);
            case KeyEvent.VK_Q -> this.movementMap.put("left", true);
            case KeyEvent.VK_Z -> this.movementMap.put("forward", true); 
            case KeyEvent.VK_S -> this.movementMap.put("backward", true);      
            case KeyEvent.VK_SPACE -> this.movementMap.put("up", true);
            case KeyEvent.VK_T -> this.movementMap.put("down", true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e){ 
        switch (e.getKeyCode()) {
            case KeyEvent.VK_D -> this.movementMap.put("right", false);
            case KeyEvent.VK_Q -> this.movementMap.put("left", false);
            case KeyEvent.VK_Z -> this.movementMap.put("forward", false); 
            case KeyEvent.VK_S -> this.movementMap.put("backward", false);      
            case KeyEvent.VK_SPACE -> this.movementMap.put("up", false);
            case KeyEvent.VK_T -> this.movementMap.put("down", false);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        double newMouseX = e.getX();
        double newMouseY = e.getY();
        if (newMouseY > this.mouseY) {
            this.rotationMap.put("down", true);
        }
        if (newMouseY < this.mouseY) {
            this.rotationMap.put("up", true);
        }
        if (newMouseX < this.mouseX) {
            this.rotationMap.put("left", true);
        }
        if (newMouseX > this.mouseX) {
            this.rotationMap.put("right", true);
        }
        if (newMouseY == this.mouseY) {
            this.rotationMap.put("down", false);
        }
        if (newMouseY == this.mouseY) {
            this.rotationMap.put("up", false);
        }
        if (newMouseX == this.mouseX) {
            this.rotationMap.put("left", false);
        }
        if (newMouseX == this.mouseX) {
            this.rotationMap.put("right", false);
        }
        this.mouseX = newMouseX;
        this.mouseY = newMouseY;
    }


    @Override
    public void mousePressed(MouseEvent e) {
        double newMouseX = e.getX();
        double newMouseY = e.getY();
        if (newMouseY == this.mouseY) {
            this.rotationMap.put("down", false);
        }
        if (newMouseY == this.mouseY) {
            this.rotationMap.put("up", false);
        }
        if (newMouseX == this.mouseX) {
            this.rotationMap.put("left", false);
        }
        if (newMouseX == this.mouseX) {
            this.rotationMap.put("right", false);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.rotationMap.put("down", false);
        this.rotationMap.put("up", false);
        this.rotationMap.put("left", false);
        this.rotationMap.put("right", false);
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

}