package raven.color.utils;

public class ColorDimension {

    public ColorDimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int width;
    public int height;

    @Override
    public String toString() {
        return "ColorDimension{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }
}
