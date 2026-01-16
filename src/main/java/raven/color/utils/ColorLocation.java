package raven.color.utils;

public class ColorLocation {

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public ColorLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public ColorLocation(ColorLocation value) {
        this.x = value.getX();
        this.y = value.getY();
    }

    public ColorLocation() {
    }

    private float x;
    private float y;

    public void set(ColorLocation location) {
        this.x = location.x;
        this.y = location.y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "ColorLocation{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
