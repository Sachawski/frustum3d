package frustum3d.core;

public record Line(Pixel pixel1, Pixel pixel2) {

    @Override
    public String toString() {
        return "Line(" + this.pixel1() + "," + this.pixel2() + ")";
    }

}
