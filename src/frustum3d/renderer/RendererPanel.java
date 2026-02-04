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
    private RenderingEngine renderingEngine;
    private RenderingType renderingType;
    private Referentiel referentiel;
    double targetAspect = 16.0 / 9.0;

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
                updateEngineRef();
            }
        });
    }

    public void setRenderingType(RenderingType newRenderingType) {
        this.renderingType = newRenderingType;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawLine((int)(this.referentiel.x()-renderingEngine.getWidth()/2),(int)(this.referentiel.y()-renderingEngine.getHeight()/2),(int)(this.referentiel.x()+renderingEngine.getWidth()/2),(int)(this.referentiel.y()-renderingEngine.getHeight()/2));
        g.drawLine((int)(this.referentiel.x()-renderingEngine.getWidth()/2),(int)(this.referentiel.y()-renderingEngine.getHeight()/2),(int)(this.referentiel.x()-renderingEngine.getWidth()/2),(int)(this.referentiel.y()+renderingEngine.getHeight()/2));
        g.drawLine((int)(this.referentiel.x()+renderingEngine.getWidth()/2),(int)(this.referentiel.y()-renderingEngine.getHeight()/2),(int)(this.referentiel.x()+renderingEngine.getWidth()/2),(int)(this.referentiel.y()+renderingEngine.getHeight()/2));
        g.drawLine((int)(this.referentiel.x()-renderingEngine.getWidth()/2),(int)(this.referentiel.y()+renderingEngine.getHeight()/2),(int)(this.referentiel.x()+renderingEngine.getWidth()/2),(int)(this.referentiel.y()+renderingEngine.getHeight()/2));
        switch (this.renderingType) {
            case RenderingType.FULLMESH -> fullMeshRendering(g);
            case RenderingType.WIREFRAME -> wireFrameRendering(g);
        }

    }

    private void wireFrameRendering(Graphics g) {
        BufferedImage lines = renderingEngine.wireFrameRendering(forms);
        g.drawImage(lines, (int)(referentiel.x() - (double)renderingEngine.getWidth()/2), (int)(referentiel.y() - (double)renderingEngine.getHeight()/2), this);
    }

     private void fullMeshRendering(Graphics g) {
         BufferedImage lines = renderingEngine.fullMeshRenderingOptimized(forms);
         g.drawImage(lines, (int)(referentiel.x() - (double)renderingEngine.getWidth()/2),(int)(referentiel.y() - (double)renderingEngine.getHeight()/2), this);
     }


     private void updateEngineRef() {
         int panelW = getWidth();
         int panelH = getHeight();
         int viewportW;
         int viewportH;
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
         renderingEngine.resize(viewportW, viewportH);
         renderingEngine.setRef(new Referentiel(
                 (double) viewportW/2,
                 (double) viewportH/2
         ));
     }
}
