package raven.color.component;

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
}
