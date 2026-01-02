package raven.color;

import raven.color.component.LocationChangeEvent;
import raven.color.utils.ColorLocation;
import raven.color.utils.ColorPickerUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class CorelColorPickerModel extends DiskColorPickerModel {

    private static final Point2D.Float PURE = new Point2D.Float(0.5f, 0.0f);
    private static final Point2D.Float WHITE = new Point2D.Float(0.9330127f, 0.75f);
    private static final Point2D.Float BLACK = new Point2D.Float(0.0669873f, 0.75f);

    private static final float wheelSize = 0.08f;
    private static final float space = 3;

    protected ColorLocation wheelPressed = new ColorLocation();
    protected boolean isWheelPressed;

    protected BufferedImage slImage;
    protected BufferedImage hueWheelImage;

    protected BufferedImage bothImage;

    public CorelColorPickerModel() {
    }

    public CorelColorPickerModel(Color color) {
        super(color);
    }

    @Override
    public BufferedImage getColorImage(int width, int height, int arc) {
        super.getColorImage(width, height, arc);
        return bothImage;
    }

    @Override
    public Color locationToColor(ColorLocation location, float value) {
        float x = location.getX();
        float y = location.getY();

        // undo padding
        float padding = (wheelSize * 2f) + scale(space) / 100f;
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
    public void locationValue(ColorLocation loc, LocationChangeEvent event) {
        if (isWheelPressed(event.getPressedLocation())) {
            float angle = locationToAngle(loc.getX(), loc.getY());
            float v = angle / 360f;
            setValue(v);
            event.consume();
        } else {
            float ag = getValue() * 360;
            loc.set(clampToTriangle(loc, ag + 90));
            setLocation(rotate(loc.getX(), loc.getY(), -ag));
        }
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
        float k = 1f - ((wheelSize * 2f) + scale(space) / 100f);

        float nx = cx + (p.getX() - cx) * k;
        float ny = cy + (p.getY() - cy) * k;
        return new ColorLocation(nx, ny);
    }

    @Override
    protected Color valueToColor(ColorLocation location, float value) {
        return locationToColor(getLocation(), value);
    }

    @Override
    public ColorLocation getLocation() {
        ColorLocation location = super.getLocation();
        float angle = getValue() * 360;
        return rotate(location.getX(), location.getY(), angle);
    }

    @Override
    protected float colorToValue(Color color) {
        if (color == null) {
            return 1f;
        }
        float v = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[0];
        if (v == 0) {
            v = 1f;
        }
        return v;
    }

    @Override
    public boolean notifySelectedLocationOnValueChanged() {
        return true;
    }

    @Override
    protected void createValueImage(int width, int height, int arc) {
    }

    @Override
    protected void createColorImage(int width, int height, int arc) {
        if (width <= 0 || height <= 0) {
            return;
        }
        int size = Math.min(width, height);
        int hueWheelBorderSize = (int) (size * wheelSize);
        int spaceSize = (int) scale(space);
        int slSize = size - (hueWheelBorderSize + spaceSize) * 2;
        boolean colorCreated = false;
        if (slImage == null || (slImage.getWidth() != slSize || slImage.getHeight() != slSize) || oldValue != getValue()) {
            Color color = Color.getHSBColor(getValue(), 1f, 1f);
            slImage = createSLImage(color, slSize);
            oldValue = getValue();
            colorCreated = true;
        }

        if (hueWheelImage == null || hueWheelImage.getWidth() != size || hueWheelImage.getHeight() != size) {
            hueWheelImage = createHueWheelImage(size, hueWheelBorderSize);
            colorCreated = true;
        }
        if (colorCreated) {
            bothImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2 = bothImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.drawImage(hueWheelImage, 0, 0, null);

            // paint triangle
            float angle = getValue() * 360;
            int l = hueWheelBorderSize + spaceSize;
            g2.translate(l, l);
            float cx = slImage.getWidth() / 2f;
            float cy = slImage.getHeight() / 2f;
            g2.rotate(Math.toRadians(angle + 90), cx, cy);
            g2.drawImage(slImage, 0, 0, null);

            // paint selected
            ColorLocation loc = angleToLocation(-90);
            float s = size / 2f;
            float lx = loc.getX() * (s - hueWheelBorderSize / 2f);
            float ly = loc.getY() * (s - hueWheelBorderSize / 2f);
            double tranX = s + lx - (hueWheelBorderSize / 2f);
            double tranY = s + ly - (hueWheelBorderSize / 2f);
            g2.translate(tranX - l, tranY - l);

            g2.setColor(UIManager.getColor("Component.borderColor"));
            g2.fill(ColorPickerUtils.createShape(hueWheelBorderSize, 0.6f, 0f));

            g2.setColor(new Color(255, 255, 255));
            g2.fill(ColorPickerUtils.createShape(hueWheelBorderSize, 0.6f, scale(1f)));

            g2.dispose();
        }
    }

    protected BufferedImage createSLImage(Color color, int size) {
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
        return cropImage(image, triangle);
    }

    protected BufferedImage createHueWheelImage(int size, int border) {
        Area circleShape = new Area(new Ellipse2D.Float(0, 0, size, size));
        circleShape.subtract(new Area(new Ellipse2D.Float(border, border, size - border * 2, size - border * 2)));
        super.createColorImage(size, size, 0);
        return cropImage(colorImage, circleShape);
    }

    protected BufferedImage cropImage(BufferedImage img, Shape shape) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fill(shape);
        g2.setComposite(AlphaComposite.SrcIn);
        g2.drawImage(img, 0, 0, null);
        g2.dispose();

        return image;
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

    protected float locationToAngle(float x, float y) {
        float dx = x - 0.5f;
        float dy = 0.5f - y;
        float angle = (float) Math.toDegrees(Math.atan2(dy, dx));
        angle = angle < 0 ? angle + 360f : angle;
        return (360f - angle) % 360f;
    }

    protected ColorLocation angleToLocation(float angle) {
        float rad = (float) Math.toRadians(angle);
        float x = (float) (Math.cos(rad));
        float y = (float) (Math.sin(rad));
        return new ColorLocation(x, y);
    }

    protected boolean isWheelPressed(ColorLocation loc) {
        if (wheelPressed.getX() == loc.getX() && wheelPressed.getY() == loc.getY()) {
            return isWheelPressed;
        }
        float cx = 0.5f;
        float cy = 0.5f;
        float dx = loc.getX() - cx;
        float dy = loc.getY() - cy;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        float inner = 0.5f - wheelSize;

        isWheelPressed = dist >= inner;
        wheelPressed.set(loc);
        return isWheelPressed;
    }

    protected int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }

    protected ColorLocation clampToTriangle(ColorLocation loc, float angle) {
        float cx = 0.5f;
        float cy = 0.5f;
        float padding = (wheelSize * 2f) + scale(space) / 100f;

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
