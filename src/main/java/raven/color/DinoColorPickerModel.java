package raven.color;

import raven.color.component.AbstractColorPickerModel;
import raven.color.component.ColorLocation;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class DinoColorPickerModel extends AbstractColorPickerModel {

    public DinoColorPickerModel() {
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
        this.location.set(location);
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
            return 0f;
        }
        return Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[0];
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
            colorImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = colorImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint primary = new GradientPaint(0f, 0f, Color.WHITE, width, 0f, Color.getHSBColor(getValue(), 1f, 1f));
            GradientPaint shade = new GradientPaint(0f, 0f, new Color(0, 0, 0, 0), 0f, height, new Color(0, 0, 0, 255));
            g2.setPaint(primary);

            arc = clampArc(width, height, arc);

            g2.fill(new RoundRectangle2D.Float(0, 0, width, height, arc, arc));
            g2.setPaint(shade);
            g2.fill(new RoundRectangle2D.Float(0, 0, width, height, arc, arc));
            g2.dispose();
            oldValue = getValue();
        }
    }
}
