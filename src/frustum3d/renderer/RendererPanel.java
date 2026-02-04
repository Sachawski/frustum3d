package frustum3d.renderer;

import frustum3d.core.meshes.Mesh;
import frustum3d.scene.Camera;
import frustum3d.utils.Referentiel;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.List;

public class RendererPanel extends JPanel {

    List<Mesh> forms;
    RenderingEngine renderingEngine;
    RenderingType renderingType;
    Referentiel referentiel;
    double targetAspect = 16.0 / 9.0;
    int viewWidth;
    int viewHeight;

    public RendererPanel(Referentiel ref, List<Mesh> forms, Camera camera, int width, int height) {
        setFocusable(true);
        setOpaque(true);
        this.referentiel = ref;
        this.forms = forms;
        this.renderingEngine = new RenderingEngine(
            ref,
            width,
            height,
            camera
        );
        this.renderingType = RenderingType.WIREFRAME;
        this.targetAspect = camera.getAspectRatio();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateRef();
            }
        });
    }

    public void setRenderingType(RenderingType newRenderingType) {
        this.renderingType = newRenderingType;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.drawLine(0,(int) this.referentiel.y()-viewHeight/2, viewWidth,(int) this.referentiel.y()-viewHeight/2);
        g.setColor(Color.CYAN);
        g.drawLine(0,(int) this.referentiel.y()+viewHeight/2, viewWidth,(int) this.referentiel.y()+viewHeight/2);
        g.setColor(Color.RED);
        g.drawLine((int) this.referentiel.x()-viewWidth/2, 0,(int) this.referentiel.x()-viewWidth/2, viewHeight);
        g.setColor(Color.GREEN);
        g.drawLine((int) this.referentiel.x()+viewWidth/2, 0,(int) this.referentiel.x()+viewWidth/2, viewHeight);
        switch (this.renderingType) {
            case RenderingType.FULLMESH -> fullMeshRendering(g);
            case RenderingType.WIREFRAME -> wireFrameRendering(g);
        }

    }

    private void wireFrameRendering(Graphics g) {
        BufferedImage lines = renderingEngine.wireFrameRendering(forms);
        g.drawImage(lines, 0, 0, this);
    }

     private void fullMeshRendering(Graphics g) {
         BufferedImage lines = renderingEngine.fullMeshRenderingOptimized(forms);
         g.drawImage(lines, 0,0, this);
     }


     private void updateRef() {
         int panelW = getWidth();
         int panelH = getHeight();
         int viewportW, viewportH;
         double panelAspect = (double) panelW / panelH;
         if (panelAspect > targetAspect) {
             viewportH = panelH;
             viewportW = (int) (panelH * targetAspect);
         } else {
             viewportW = panelW;
             viewportH = (int) (panelW / targetAspect);
         }
         this.referentiel = new Referentiel(
                 (double) panelW/2,
                 (double) panelH/2
         );
         this.viewHeight = viewportH;
         this.viewWidth = viewportW;
         renderingEngine.resize(viewportW, viewportH);
         renderingEngine.setRef(this.referentiel);
     }
}
