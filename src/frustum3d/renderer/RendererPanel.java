package frustum3d.renderer;

import frustum3d.core.meshes.Mesh;
import frustum3d.core.screen.ScreenLine;
import frustum3d.scene.Camera;
import frustum3d.utils.Referentiel;
import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import javax.swing.JPanel;

public class RendererPanel extends JPanel {

    List<Mesh> forms;
    RenderingEngine renderingEngine;
    RenderingType renderingType;


    public RendererPanel(Referentiel ref, List<Mesh> forms, Camera camera, int width, int height) {
        setFocusable(true);
        this.forms = forms; 
        this.renderingEngine = new RenderingEngine(
            ref,
            width,
            height,
            camera
        );
        this.renderingType = RenderingType.WIREFRAME;
    }

    public void setRenderingType(RenderingType newRenderingType) {
        this.renderingType = newRenderingType;
    }

    @Override
    public void paintComponent(Graphics g) {
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

}