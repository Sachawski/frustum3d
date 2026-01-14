package personal.frustum;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class Panel extends JPanel {

    Referentiel ref;
    Form form;
    Camera camera;

    public Panel(Referentiel ref, Form form, Camera camera) {
        this.ref = ref;
        this.form = form; 
        this.camera = camera;
    }

    public Panel(Form form, Camera camera) {
        this.ref = new Referentiel(0, 0);
        this.form = form; 
        this.camera = camera;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);

        List<Point3D> projection = new ArrayList<>();

        Matrix mvpMatrix = camera.createMVPMatrix();

        for (int i = 0; i < form.getPoints().size(); i++) {
            Point3D point = form.getPoints().get(i);
            Point3D transformed = mvpMatrix.multiply(point);
            projection.add(transformed);
            // System.out.println("Point " + i + ": (" + point.getPosX() + "," + point.getPosY() + "," + point.getPosZ() + 
            //                  ") -> NDC: (" + transformed.getPosX() + "," + transformed.getPosY() + "," + transformed.getPosZ() + ")");
        }

        Integer[][] visibilityGraph = form.getGraph();
        
        int linesProcessed = 0;
        int linesClipped = 0;
        int linesBehindCamera = 0;

        for (int i = 0; i < form.getNumberOfPoints(); i++) {
            for (int j = i+1; j< form.getNumberOfPoints(); j++ ) {
                if (visibilityGraph[i][j] == 1) {
                    linesProcessed++;
                    Point3D p1 = projection.get(i);
                    Point3D p2 = projection.get(j);
                    
                    if (isPointBehindCamera(p1) && isPointBehindCamera(p2)) {
                        linesBehindCamera++;
                        continue;
                    }
                    
                    Point3D[] clipped = clipLine(p1, p2);
                    if (clipped != null && clipped.length == 2) {
                        linesClipped++;
                        int width = getWidth();
                        int height = getHeight();
                        
                        double scale = Math.min(width, height) / 4.0;
                        
                        int screenX1 = (int) ref.x() + (int)(clipped[0].getPosX() * scale);
                        int screenY1 = (int) ref.y() - (int)(clipped[0].getPosY() * scale);
                        int screenX2 = (int) ref.x() + (int)(clipped[1].getPosX() * scale);
                        int screenY2 = (int) ref.y() - (int)(clipped[1].getPosY() * scale);
                        
                        g.drawLine(screenX1, screenY1, screenX2, screenY2);
                    }
                }
            }
        }
    }

    private boolean isPointBehindCamera(Point3D point) {
        return Double.isNaN(point.getPosX()) || 
               Double.isNaN(point.getPosY()) || 
               Double.isNaN(point.getPosZ());
    }

    private Point3D[] clipLine(Point3D p1, Point3D p2) {
        if (isPointBehindCamera(p1) && isPointBehindCamera(p2)) {
            return null;
        }
        
        double x1 = p1.getPosX(), y1 = p1.getPosY(), z1 = p1.getPosZ();
        double x2 = p2.getPosX(), y2 = p2.getPosY(), z2 = p2.getPosZ();
        
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dz = z2 - z1;
        
        double tNear = 0.0;
        double tFar = 1.0;
        
        double[] p = {x1, y1, z1};
        double[] d = {dx, dy, dz};
        double[] bounds = {-1, -1, -1};
        double[] bounds2 = {1, 1, 1};
        
        for (int i = 0; i < 3; i++) {
            if (Math.abs(d[i]) < 1e-10) {
                if (p[i] < bounds[i] || p[i] > bounds2[i]) {
                    return null;
                }
            } else {
                double t1 = (bounds[i] - p[i]) / d[i];
                double t2 = (bounds2[i] - p[i]) / d[i];
                
                if (t1 > t2) {
                    double temp = t1;
                    t1 = t2;
                    t2 = temp;
                }
                
                tNear = Math.max(tNear, t1);
                tFar = Math.min(tFar, t2);
                
                if (tNear > tFar) {
                    return null;
                }
            }
        }
        
        if (tNear > 1.0 || tFar < 0.0) {
            return null;
        }
        
        tNear = Math.max(0.0, tNear);
        tFar = Math.min(1.0, tFar);
        
        if (tNear == tFar) {
            return null;
        }
        
        Point3D clippedP1 = new Point3D(
            x1 + tNear * dx,
            y1 + tNear * dy,
            z1 + tNear * dz
        );
        
        Point3D clippedP2 = new Point3D(
            x1 + tFar * dx,
            y1 + tFar * dy,
            z1 + tFar * dz
        );
        
        return new Point3D[] {clippedP1, clippedP2};
    }
}