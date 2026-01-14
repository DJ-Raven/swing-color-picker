package raven.color;

import raven.color.utils.ColorLocation;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class CorelTriangleColorPickerModel extends CorelSquareColorPickerModel {

    private static final Point2D.Float PURE = new Point2D.Float(0.5f, 0.0f);
    private static final Point2D.Float WHITE = new Point2D.Float(0.9330127f, 0.75f);
    private static final Point2D.Float BLACK = new Point2D.Float(0.0669873f, 0.75f);

    public CorelTriangleColorPickerModel() {
    }

    public CorelTriangleColorPickerModel(Color color) {
        super(color);
    }

    @Override
    protected float getSpace() {
        return 0.03f;
    }

    @Override
    protected boolean isRotate() {
        return true;
    }

    @Override
    public Color locationToColor(ColorLocation location, float value) {
        float x = location.getX();
        float y = location.getY();

        // undo padding
        float padding = (wheelSize + getSpace()) * 2f;
        float cx = 0.5f;
        float cy = 0.5f;
        float k = 1f / (1f - padding);

        x = cx + (x - cx) * k;
        y = cy + (y - cy) * k;
        float angle = getValue() * 360 + 90;

        // undo rotation
        ColorLocation p = rotate(x, y, -angle);
        x = p.getX();
        y = p.getY();

        // barycentric coordinates
        float[] w = barycentric(x, y, PURE, WHITE, BLACK);

        float wPure = clamp(w[0]);
        float wBlack = clamp(w[2]);

        // barycentric to HSV
        float v = 1f - wBlack;
        float s = (v == 0f) ? 0f : (wPure / v);

        // HSV to Color
        return Color.getHSBColor(value, clamp(s), clamp(v));
    }

    @Override
    public ColorLocation colorToLocation(Color color) {
        float[] hsv = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float s = hsv[1];
        float v = hsv[2];

        float wPure = s * v;
        float wWhite = (1f - s) * v;
        float wBlack = 1f - v;

        float x = 0.5f * wPure + WHITE.x * wWhite + BLACK.x * wBlack;
        float y = 0.0f * wPure + WHITE.y * wWhite + BLACK.y * wBlack;

        // rotate
        float angle = getValue() * 360 + 90;
        ColorLocation p = rotate(x, y, angle);

        // apply padding
        float cx = 0.5f;
        float cy = 0.5f;
        float k = 1f - ((wheelSize + getSpace()) * 2f);

        float nx = cx + (p.getX() - cx) * k;
        float ny = cy + (p.getY() - cy) * k;
        return new ColorLocation(nx, ny);
    }

    @Override
    public ColorLocation getLocation() {
        ColorLocation location = super.getLocation();
        float angle = getValue() * 360;
        return rotate(location.getX(), location.getY(), angle);
    }

    @Override
    public void setLocation(ColorLocation location) {
        float ag = getValue() * 360;
        super.setLocation(rotate(location.getX(), location.getY(), -ag));
    }

    @Override
    public boolean notifySelectedLocationOnValueChanged() {
        return true;
    }

    @Override
    protected BufferedImage createSelectionImage(Color color, int size, int arc) {
        Shape triangle = createTriangle(size);
        Rectangle bounds = triangle.getBounds();
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        int w = bounds.width + bounds.x;
        int h = bounds.height + bounds.y;

        float ax = w / 2f, ay = bounds.y;
        float bx = bounds.x;

        Color cb = Color.BLACK;
        Color cc = Color.WHITE;
        float det = (ax - w) + (w - bx) * (ay - h);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                float l1 = ((w - bx) * (y - h)) / det;
                float l2 = ((h - ay) * (x - w) + (ax - w) * (y - h)) / det;
                float l3 = 1f - l1 - l2;

                int r = clamp((int) (l1 * color.getRed() + l2 * cb.getRed() + l3 * cc.getRed()));
                int gg = clamp((int) (l1 * color.getGreen() + l2 * cb.getGreen() + l3 * cc.getGreen()));
                int b = clamp((int) (l1 * color.getBlue() + l2 * cb.getBlue() + l3 * cc.getBlue()));

                image.setRGB(x, y, new Color(r, gg, b).getRGB());
            }
        }
        return maskImage(image, triangle);
    }

    protected Shape createTriangle(int size) {
        double c = size / 2f;
        double r = size / 2f;
        Path2D triangle = new Path2D.Double();
        for (int i = 0; i < 3; i++) {
            double angle = Math.toRadians(-90 + i * 120);
            double x = c + r * Math.cos(angle);
            double y = c + r * Math.sin(angle);
            if (i == 0) {
                triangle.moveTo(x, y);
            } else {
                triangle.lineTo(x, y);
            }
        }
        triangle.closePath();
        return triangle;
    }

    protected int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }

    @Override
    protected ColorLocation clampLocation(ColorLocation loc) {
        float angle = getValue() * 360 + 90;

        float cx = 0.5f;
        float cy = 0.5f;
        float padding = (wheelSize + getSpace()) * 2f;

        // undo rotation
        ColorLocation p = rotate(loc.getX(), loc.getY(), -angle);

        // undo padding (expand)
        float expand = 1f / (1f - padding);
        float x = cx + (p.getX() - cx) * expand;
        float y = cy + (p.getY() - cy) * expand;

        // barycentric coordinates
        float[] w = barycentric(x, y, PURE, WHITE, BLACK);

        float u = Math.max(0f, w[0]);
        float v = Math.max(0f, w[1]);
        float wgt = Math.max(0f, w[2]);

        float sum = u + v + wgt;
        if (sum == 0f) {
            u = 1f;
            sum = 1f;
        }

        u /= sum;
        v /= sum;
        wgt /= sum;

        // reconstruct clamped point
        float px = PURE.x * u + WHITE.x * v + BLACK.x * wgt;
        float py = PURE.y * u + WHITE.y * v + BLACK.y * wgt;

        // apply padding (shrink)
        float shrink = 1f - padding;
        px = cx + (px - cx) * shrink;
        py = cy + (py - cy) * shrink;

        // re-apply rotation
        return rotate(px, py, angle);
    }

    protected float[] barycentric(float px, float py, Point2D.Float a, Point2D.Float b, Point2D.Float c) {
        float v0x = b.x - a.x;
        float v0y = b.y - a.y;
        float v1x = c.x - a.x;
        float v1y = c.y - a.y;
        float v2x = px - a.x;
        float v2y = py - a.y;

        float d00 = v0x * v0x + v0y * v0y;
        float d01 = v0x * v1x + v0y * v1y;
        float d11 = v1x * v1x + v1y * v1y;
        float d20 = v2x * v0x + v2y * v0y;
        float d21 = v2x * v1x + v2y * v1y;

        float denom = d00 * d11 - d01 * d01;

        float v = (d11 * d20 - d01 * d21) / denom;
        float w = (d00 * d21 - d01 * d20) / denom;
        float u = 1f - v - w;

        return new float[]{u, v, w};
    }

    protected ColorLocation rotate(float x, float y, float angle) {
        float cx = 0.5f;
        float cy = 0.5f;
        double a = Math.toRadians(angle);
        double cos = Math.cos(a);
        double sin = Math.sin(a);
        float dx = x - cx;
        float dy = y - cy;
        float rx = (float) (cx + dx * cos - dy * sin);
        float ry = (float) (cy + dx * sin + dy * cos);
        return new ColorLocation(rx, ry);
    }
}
