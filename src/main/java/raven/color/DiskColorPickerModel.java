package raven.color;

import raven.color.component.LocationChangeEvent;
import raven.color.utils.AbstractColorPickerModel;
import raven.color.utils.ColorDimension;
import raven.color.utils.ColorLocation;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class DiskColorPickerModel extends AbstractColorPickerModel {

    private Color oldSelectedColor;

    public DiskColorPickerModel() {
        this(Color.WHITE);
    }

    public DiskColorPickerModel(Color color) {
        setSelectedColor(color);
    }

    @Override
    public Image getValueImage(int width, int height, int arc) {
        createValueImage(width, height, arc);
        return valueImage;
    }

    @Override
    public BufferedImage getColorImage(int width, int height, int arc) {
        ColorDimension size = getDimensionImage(width, height);
        createColorImage(size.width, size.height, arc);
        return colorImage;
    }

    @Override
    public ColorDimension getDimensionImage(int width, int height) {
        ColorDimension size = super.getDimensionImage(width, height);
        int s = Math.min(size.width, size.height);
        size.width = s;
        size.height = s;
        return size;
    }

    @Override
    public void locationValue(ColorLocation loc, LocationChangeEvent event) {
        float nx = loc.getX() * 2f - 1f;
        float ny = loc.getY() * 2f - 1f;

        float dist = (float) Math.sqrt(nx * nx + ny * ny);
        if (dist > 1f) {
            nx /= dist;
            ny /= dist;
        }
        loc.set((nx + 1f) / 2f, (ny + 1f) / 2f);
        super.locationValue(loc, event);
    }

    @Override
    public Color locationToColor(ColorLocation location, float value) {
        // round 3 decimal places
        float roundedX = ((int) (location.getX() * 1000)) / 1000f;
        float roundedY = ((int) (location.getY() * 1000)) / 1000f;
        float nx = (roundedX * 2f) - 1f;
        float ny = (roundedY * 2f) - 1f;

        float dist = (float) Math.sqrt(nx * nx + ny * ny);
        float sat = Math.min(dist, 1f);

        double angle = Math.atan2(ny, nx);
        float hue = (float) ((angle / (2 * Math.PI)));

        return Color.getHSBColor(hue, sat, value);
    }

    @Override
    public ColorLocation colorToLocation(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float hue = hsb[0];
        float sat = hsb[1];

        // convert hue to angle (radians)
        double angle = hue * 2 * Math.PI;

        // convert polar → cartesian (range -1 .. 1)
        float nx = (float) (Math.cos(angle) * sat);
        float ny = (float) (Math.sin(angle) * sat);

        // convert cartesian → normalized location (0 .. 1)
        float x = (nx + 1f) / 2f;
        float y = (ny + 1f) / 2f;

        return new ColorLocation(x, y);
    }

    @Override
    protected Color valueToColor(ColorLocation location, float value) {
        return locationToColor(location, value);
    }

    @Override
    protected float colorToValue(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        return hsb[2];
    }

    protected void createValueImage(int width, int height, int arc) {
        if (width <= 0 || height <= 0) {
            return;
        }
        Color color = locationToColor(getLocation(), 1f);
        if (valueImage == null || (valueImage.getWidth() != width || valueImage.getHeight() != height) || !Objects.equals(color, oldSelectedColor)) {
            valueImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = valueImage.createGraphics();
            arc = clampArc(width, height, arc);

            GradientPaint horizontal = new GradientPaint(
                    0, 0, Color.BLACK,
                    width, 0, color
            );
            g2.setPaint(horizontal);
            if (arc > 0) {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.fill(new RoundRectangle2D.Float(0, 0, width, height, arc, arc));
            } else {
                g2.fillRect(0, 0, width, height);
            }
            g2.dispose();
            oldSelectedColor = color;
        }
    }

    protected void createColorImage(int width, int height, int arc) {
        if (width <= 0 || height <= 0) {
            return;
        }
        if (colorImage == null || (colorImage.getWidth() != width || colorImage.getHeight() != height)) {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = image.createGraphics();
            int cx = width / 2;
            int cy = height / 2;
            int radius = Math.min(width, height) / 2;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int dx = x - cx;
                    int dy = y - cy;
                    double dist = Math.sqrt(dx * dx + dy * dy);

                    // normalize dist so that edge = 1.0
                    float sat = (float) Math.min(dist / radius, 1.0);

                    // angle -> hue
                    double angle = Math.atan2(dy, dx);
                    float hue = (float) ((angle / (2 * Math.PI)));
                    float bri = 1f;
                    int rgb = Color.HSBtoRGB(hue, sat, bri);
                    image.setRGB(x, y, rgb);
                }
            }
            g2.dispose();
            colorImage = maskImage(image, createColorMaskShape(image.getWidth(), image.getHeight()));
        }
    }

    protected Shape createColorMaskShape(int width, int height) {
        return new Ellipse2D.Float(0, 0, width, height);
    }

    protected BufferedImage maskImage(BufferedImage image, Shape shape) {
        BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fill(shape);
        g2.setComposite(AlphaComposite.SrcIn);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return img;
    }
}
