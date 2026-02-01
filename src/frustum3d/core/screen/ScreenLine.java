package frustum3d.core.screen;

import java.awt.*;

public record ScreenLine(Pixel pixel1, Pixel pixel2, Color color) {

    @Override
    public String toString() {
        return "Line(" + this.pixel1() + "," + this.pixel2() + ")";
    }

    public int getMaxX() {
        return Math.max(pixel1.x(),pixel2.x());
    }

    public int getMaxY() {
        return Math.max(pixel1.y(),pixel2.y());
    }
}
