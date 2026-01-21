package frustum3d.renderer;

public record Pixel(int x, int y) {

    @Override
    public String toString() {
        return "Pixel(" + this.x() + "," + this.y() + ")";
    }

}
