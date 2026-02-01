package frustum3d.renderer;

import frustum3d.core.meshes.Mesh;
import frustum3d.core.screen.ScreenLine;
import frustum3d.scene.Camera;
import frustum3d.utils.Referentiel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import javax.swing.JPanel;

public class RendererPanel extends JPanel {

    List<Mesh> forms;
    RenderingEngine renderingEngine;
    RenderingType renderingType;
    Referentiel referentiel;
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
        g.drawLine(0,(int) this.referentiel.y()-viewHeight/2, getWidth(),(int) this.referentiel.y()-viewHeight/2);
        g.drawLine(0,(int) this.referentiel.y()+viewHeight/2, getWidth(),(int) this.referentiel.y()+viewHeight/2);
        switch (this.renderingType) {
            case RenderingType.FULLMESH -> fullMeshRendering(g);
            case RenderingType.WIREFRAME -> wireFrameRendering(g);
        }

    }

    private void wireFrameRendering(Graphics g) {
        List<ScreenLine> lines = renderingEngine.wireFrameRendering(forms);

        for (ScreenLine line : lines) {
            g.setColor(line.color());
            g.drawLine(
                line.pixel1().x(),
                line.pixel1().y(),
                line.pixel2().x(),
                line.pixel2().y()
            );
        }
    }

     private void fullMeshRendering(Graphics g) {
         List<ScreenLine> lines = renderingEngine.fullMeshRenderingOptimized(forms);
         g.setColor(Color.BLACK);
         for (ScreenLine line : lines) {
             g.drawLine(
                 line.pixel1().x(),
                 line.pixel1().y(),
                 line.pixel2().x(),
                 line.pixel2().y()
             );
         }
     }


     private void updateRef() {
         int panelW = getWidth();
         int panelH = getHeight();
         double targetAspect = 16.0 / 9.0;
         int viewportW, viewportH, offsetX, offsetY;
         double panelAspect = (double) panelW / panelH;
         if (panelAspect > targetAspect) {
             viewportH = panelH;
             viewportW = (int) (panelH * targetAspect);
             offsetX = (panelW - viewportW) / 2;
             offsetY = 0;
         } else {
             viewportW = panelW;
             viewportH = (int) (panelW / targetAspect);
             offsetX = 0;
             offsetY = (panelH - viewportH) / 2;
         }
         this.referentiel = new Referentiel(offsetX + viewportW/2.0,
                 offsetY + viewportH/2.0);
         this.viewHeight = viewportH;
         this.viewWidth = viewportW;
         renderingEngine.resize(viewportW, viewportH);
         renderingEngine.setRef(this.referentiel);
     }
}
