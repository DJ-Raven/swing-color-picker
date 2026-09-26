package raven.color;

import raven.color.utils.AbstractColorPickerModel;
import raven.color.utils.ColorLocation;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class DinoColorPickerModel extends AbstractColorPickerModel {

    public DinoColorPickerModel() {
        this(Color.WHITE);
    }

    public DinoColorPickerModel(Color color) {
        setSelectedColor(color);
    }

    @Override
    public Image getValueImage(int width, int height, int arc) {
        createValueImage(width, height, arc);
        return valueImage;
    }

    @Override
    public BufferedImage getColorImage(int width, int height, int arc) {
        createColorImage(width, height, arc);
        return colorImage;
    }

    @Override
    public Color locationToColor(ColorLocation location, float value) {
        return Color.getHSBColor(value, location.getX(), 1f - location.getY());
    }

    @Override
    public ColorLocation colorToLocation(Color color) {
        float[] hbs = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float x = hbs[1];
        float y = 1f - hbs[2];
        return new ColorLocation(x, y);
    }

    @Override
    protected Color valueToColor(ColorLocation location, float value) {
        return locationToColor(location, value);
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

    protected void createValueImage(int width, int height, int arc) {
        if (width <= 0 || height <= 0) {
            return;
        }
        if (valueImage == null || (valueImage.getWidth() != width || valueImage.getHeight() != height)) {
            valueImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = valueImage.createGraphics();
            arc = clampArc(width, height, arc);
            if (arc > 0) {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.fill(new RoundRectangle2D.Float(0, 0, width, height, arc, arc));
                g2.setComposite(AlphaComposite.SrcIn);
            }
            for (int i = 0; i < width; i++) {
                float v = (float) i / (float) width;
                g2.setColor(Color.getHSBColor(v, 1f, 1f));
                g2.drawLine(i, 0, i, height);
            }
            g2.dispose();
        }
    }

    protected void createColorImage(int width, int height, int arc) {
        if (width <= 0 || height <= 0) {
            return;
        }
        if (colorImage == null || (colorImage.getWidth() != width || colorImage.getHeight() != height) || oldValue != getValue()) {
            float hue = getValue();
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                float b = clamp(1f - (y + 0.5f) / height);
                for (int x = 0; x < width; x++) {
                    float s = clamp((x + 0.5f) / width);
                    pixels[y * width + x] = Color.HSBtoRGB(hue, s, b);
                }
            }
            image.setRGB(0, 0, width, height, pixels, 0, width);

            arc = clampArc(width, height, arc);

            // mask with rounded rectangle
            colorImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = colorImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.fill(new RoundRectangle2D.Float(0, 0, width, height, arc, arc));
            g2.setComposite(AlphaComposite.SrcIn);
            g2.drawImage(image, 0, 0, null);
            g2.dispose();
            oldValue = getValue();
        }
    }
}
