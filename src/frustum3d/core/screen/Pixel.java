package frustum3d.core.screen;

public record Pixel(int x, int y, double depth) {

    @Override
    public String toString() {
        return "Pixel(" + this.x() + "," + this.y() + ")";
    }

}
