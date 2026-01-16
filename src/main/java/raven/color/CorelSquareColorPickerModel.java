package raven.color;

import raven.color.component.LocationChangeEvent;
import raven.color.utils.ColorLocation;
import raven.color.utils.ColorPickerUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class CorelSquareColorPickerModel extends DiskColorPickerModel {

    protected static final float wheelSize = 0.08f;
    protected ColorLocation wheelPressed = new ColorLocation();
    protected boolean isWheelPressed;

    protected BufferedImage selectionImage;
    protected BufferedImage hueWheelImage;

    protected BufferedImage bothImage;

    public CorelSquareColorPickerModel() {
    }

    public CorelSquareColorPickerModel(Color color) {
        super(color);
    }

    protected float getSpace() {
        return 0.14f;
    }

    protected float getSelectionRotate() {
        return 0;
    }

    @Override
    public BufferedImage getColorImage(int width, int height, int arc) {
        super.getColorImage(width, height, arc);
        return bothImage;
    }

    @Override
    public void locationValue(ColorLocation loc, LocationChangeEvent event) {
        if (isWheelPressed(event.getPressedLocation())) {
            float angle = locationToAngle(loc.getX(), loc.getY());
            float v = angle / 360f;
            setValue(v);
            event.consume();
        } else {
            loc.set(clampLocation(loc));
            setLocation(loc);
        }
    }

    @Override
    public Color locationToColor(ColorLocation location, float value) {
        float rotate = getSelectionRotate();
        if (rotate > 0) {
            // undo rotation
            location = rotate(location.getX(), location.getY(), -rotate);
        }

        float padding = (wheelSize + getSpace()) * 2f;
        float usable = Math.max(0f, 1f - padding);

        float x = (location.getX() - padding * 0.5f) / usable;
        float y = (location.getY() - padding * 0.5f) / usable;

        x = clamp(x);
        y = clamp(y);

        return Color.getHSBColor(value, x, 1f - y);
    }

    @Override
    public ColorLocation colorToLocation(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float s = hsb[1];
        float v = hsb[2];

        float padding = (wheelSize + getSpace()) * 2f;
        float usable = Math.max(0f, 1f - padding);
        float halfPad = padding * 0.5f;

        // invert mapping
        float x = halfPad + s * usable;
        float y = halfPad + (1f - v) * usable;
        ColorLocation loc = new ColorLocation(clamp(x), clamp(y));

        float rotate = getSelectionRotate();
        if (rotate > 0) {
            return rotate(loc.getX(), loc.getY(), rotate);
        } else {
            return loc;
        }
    }

    @Override
    public ColorLocation getLocation() {
        float rotate = getSelectionRotate();
        if (rotate > 0) {
            ColorLocation location = super.getLocation();
            return rotate(location.getX(), location.getY(), rotate);
        }
        return super.getLocation();
    }

    @Override
    public void setLocation(ColorLocation location) {
        float rotate = getSelectionRotate();
        if (rotate > 0) {
            super.setLocation(rotate(location.getX(), location.getY(), -rotate));
        } else {
            super.setLocation(location);
        }
    }

    @Override
    protected Color valueToColor(ColorLocation location, float value) {
        return locationToColor(getLocation(), value);
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
    public boolean showValueComponent() {
        return false;
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
        int spaceSize = (int) (size * getSpace());
        int slSize = size - (hueWheelBorderSize + spaceSize) * 2;
        boolean colorCreated = false;
        if (selectionImage == null || (selectionImage.getWidth() != slSize || selectionImage.getHeight() != slSize) || oldValue != getValue()) {
            Color color = Color.getHSBColor(getValue(), 1f, 1f);
            selectionImage = createSelectionImage(color, slSize, arc);
            oldValue = getValue();
            colorCreated = true;
        }

        if (hueWheelImage == null || hueWheelImage.getWidth() != size || hueWheelImage.getHeight() != size) {
            hueWheelImage = createHueWheelImage(size);
            colorCreated = true;
        }
        if (colorCreated) {
            bothImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2 = bothImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.drawImage(hueWheelImage, 0, 0, null);

            // paint selection color
            float angle = getValue() * 360;
            float selectionRotate = getSelectionRotate();
            int l = hueWheelBorderSize + spaceSize;
            AffineTransform tran = g2.getTransform();

            float cx = selectionImage.getWidth() / 2f;
            float cy = selectionImage.getHeight() / 2f;
            if (selectionRotate > 0) {
                tran.rotate(Math.toRadians(selectionRotate), cx, cy);
            }
            g2.translate(l, l);
            g2.drawImage(selectionImage, tran, null);

            // paint selected
            g2.rotate(Math.toRadians(angle), cx, cy);
            float s = size / 2f;
            float lx = (s - hueWheelBorderSize / 2f);
            float ly = 0;
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

    protected BufferedImage createSelectionImage(Color color, int size, int arc) {
        Shape shape = createSelectionImageShape(size, arc);
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint primary = new GradientPaint(0f, 0f, Color.WHITE, size, 0f, color);
        GradientPaint shade = new GradientPaint(0f, 0f, new Color(0, 0, 0, 0), 0f, size, new Color(0, 0, 0, 255));
        g2.setPaint(primary);
        g2.fill(shape);
        g2.setPaint(shade);
        g2.fill(shape);
        return image;
    }

    protected Shape createSelectionImageShape(int size, int arc) {
        arc = clampArc(size, (int) (arc * 0.5f));
        return new RoundRectangle2D.Float(0, 0, size, size, arc, arc);
    }

    protected BufferedImage createHueWheelImage(int size) {
        super.createColorImage(size, size, 0);
        return colorImage;
    }

    @Override
    protected Shape createColorMaskShape(int width, int height) {
        int size = Math.min(width, height);
        int border = (int) (size * wheelSize);
        Area area = new Area(super.createColorMaskShape(width, height));
        area.subtract(new Area(new Ellipse2D.Float(border, border, size - border * 2, size - border * 2)));
        return area;
    }

    protected float locationToAngle(float x, float y) {
        float dx = x - 0.5f;
        float dy = 0.5f - y;
        float angle = (float) Math.toDegrees(Math.atan2(dy, dx));
        angle = angle < 0 ? angle + 360f : angle;
        return (360f - angle) % 360f;
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

    protected int clampArc(int size, int arc) {
        if (arc == 0) {
            return arc;
        }
        if (arc > size || arc == 999) {
            arc = size;
        }
        return arc;
    }

    protected ColorLocation clampLocation(ColorLocation loc) {
        float rotate = getSelectionRotate();
        if (rotate > 0) {
            // undo rotation
            loc = rotate(loc.getX(), loc.getY(), -rotate);
        }

        float nx = loc.getX() * 2f - 1f;
        float ny = loc.getY() * 2f - 1f;

        // effective half-size after padding
        float padding = (wheelSize + getSpace()) * 2f;
        float limit = Math.max(0f, 1f - padding);

        // clamp to square
        nx = Math.max(-limit, Math.min(limit, nx));
        ny = Math.max(-limit, Math.min(limit, ny));

        loc.set((nx + 1f) * 0.5f, (ny + 1f) * 0.5f);

        if (rotate > 0) {
            return rotate(loc.getX(), loc.getY(), rotate);
        }
        return loc;
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
