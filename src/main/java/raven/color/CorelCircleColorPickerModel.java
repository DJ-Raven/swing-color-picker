package raven.color;

import raven.color.utils.ColorLocation;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

/**
 * Circle selector with circular hue wheel.
 * The saturation/brightness square is mapped into the circle (elliptical grid mapping),
 * so every color of the square is still reachable, including the corners.
 */
public class CorelCircleColorPickerModel extends CorelSquareColorPickerModel {

    private static final float SQRT2 = (float) Math.sqrt(2);

    public CorelCircleColorPickerModel() {
    }

    public CorelCircleColorPickerModel(Color color) {
        super(color);
    }

    @Override
    protected float getSpace() {
        return 0.04f;
    }

    @Override
    public Color locationToColor(ColorLocation location, float value) {
        float radius = getSelectionRadius();
        if (radius <= 0) {
            return Color.getHSBColor(value, 0f, 1f);
        }
        float u = (location.getX() - 0.5f) / radius;
        float v = (location.getY() - 0.5f) / radius;
        float[] square = discToSquare(u, v);
        float s = clamp((square[0] + 1f) * 0.5f);
        float b = clamp(1f - (square[1] + 1f) * 0.5f);
        return Color.getHSBColor(value, s, b);
    }

    @Override
    public ColorLocation colorToLocation(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float sx = hsb[1] * 2f - 1f;
        float sy = (1f - hsb[2]) * 2f - 1f;
        float[] disc = squareToDisc(sx, sy);
        float radius = getSelectionRadius();
        return new ColorLocation(clamp(0.5f + disc[0] * radius), clamp(0.5f + disc[1] * radius));
    }

    @Override
    protected BufferedImage createSelectionImage(Color color, int size, int arc) {
        float hue = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[0];
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        int[] pixels = new int[size * size];
        float half = size / 2f;
        for (int y = 0; y < size; y++) {
            float v = (y + 0.5f - half) / half;
            for (int x = 0; x < size; x++) {
                float u = (x + 0.5f - half) / half;
                float[] square = discToSquare(u, v);
                float s = clamp((square[0] + 1f) * 0.5f);
                float b = clamp(1f - (square[1] + 1f) * 0.5f);
                pixels[y * size + x] = Color.HSBtoRGB(hue, s, b);
            }
        }
        image.setRGB(0, 0, size, size, pixels, 0, size);
        return maskImage(image, createSelectionImageShape(size, arc));
    }

    @Override
    protected Shape createSelectionImageShape(int size, int arc) {
        return new Ellipse2D.Float(0, 0, size, size);
    }

    @Override
    protected ColorLocation clampLocation(ColorLocation loc) {
        float radius = getSelectionRadius();
        float dx = loc.getX() - 0.5f;
        float dy = loc.getY() - 0.5f;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        if (dist > radius) {
            float ratio = dist == 0 ? 0 : radius / dist;
            dx *= ratio;
            dy *= ratio;
        }
        loc.set(0.5f + dx, 0.5f + dy);
        return loc;
    }

    /**
     * Radius of the selection circle, in normalized (0..1) location units
     */
    protected float getSelectionRadius() {
        return Math.max(0f, 0.5f - wheelSize - getSpace());
    }

    /**
     * Map a point in the unit disc to the square [-1, 1] (elliptical grid mapping)
     */
    protected float[] discToSquare(float u, float v) {
        float dist = u * u + v * v;
        if (dist > 1f) {
            float d = (float) Math.sqrt(dist);
            u /= d;
            v /= d;
        }
        float u2 = u * u;
        float v2 = v * v;
        float tu = 2f * SQRT2 * u;
        float tv = 2f * SQRT2 * v;
        float x = 0.5f * sqrt(2f + u2 - v2 + tu) - 0.5f * sqrt(2f + u2 - v2 - tu);
        float y = 0.5f * sqrt(2f - u2 + v2 + tv) - 0.5f * sqrt(2f - u2 + v2 - tv);
        return new float[]{Math.max(-1f, Math.min(1f, x)), Math.max(-1f, Math.min(1f, y))};
    }

    /**
     * Map a point in the square [-1, 1] to the unit disc (elliptical grid mapping)
     */
    protected float[] squareToDisc(float x, float y) {
        float u = x * sqrt(1f - y * y / 2f);
        float v = y * sqrt(1f - x * x / 2f);
        return new float[]{u, v};
    }

    private float sqrt(float v) {
        return (float) Math.sqrt(Math.max(0f, v));
    }
}
