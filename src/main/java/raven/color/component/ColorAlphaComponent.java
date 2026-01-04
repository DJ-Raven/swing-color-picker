package raven.color.component;

import raven.color.event.ColorChangeEvent;
import raven.color.utils.ColorLocation;
import raven.color.utils.ColorPickerModel;
import raven.color.utils.ColorPickerUtils;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class ColorAlphaComponent extends OrientationSliderColorModel {

    private BufferedImage image;

    public ColorAlphaComponent(ColorPickerModel model) {
        super(model);
    }

    public ColorAlphaComponent(ColorPickerModel model, boolean installModelListener) {
        super(model, installModelListener);
    }

    public ColorAlphaComponent(ColorPickerModel model, Orientation orientation) {
        super(model, orientation);
    }

    @Override
    public void uninstall() {
        super.uninstall();
        image = null;
    }

    @Override
    protected void valueChanged(ColorLocation value, LocationChangeEvent event) {
        value = orientationValue(value);
        Color color = getModel().getSelectedColor();
        if (color != null) {
            getModel().setSelectedColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (value.getX() * 255f)), false);
        }
    }

    @Override
    protected ColorLocation getValue() {
        Color color = getModel().getSelectedColor();
        return orientationValue(new ColorLocation(color.getAlpha() / 255f, 0.5f));
    }

    @Override
    public void notifyColorChanged(Color color, ColorChangeEvent event) {
        repaint();
    }

    @Override
    public void notifyModelChanged(ColorPickerModel model) {
        repaint();
    }

    @Override
    protected void paint(Graphics2D g, int x, int y, int width, int height, double scaleFactor) {
        boolean isHorizontal = isHorizontal();
        BufferedImage img = createTransparentImage(width, height, scaleFactor);
        if (img != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (!isHorizontal) {
                float centerX = x + height / 2f;
                float centerY = y + height / 2f;
                g2.rotate(Math.toRadians(90), centerX, centerY);
            }
            g2.drawImage(img, x, y, null);
            Color color = new Color(getModel().getSelectedColor().getRGB());
            g2.setPaint(new GradientPaint(x, y, new Color(255, 255, 255, 0), x + width, y, color));
            g2.fill(new RoundRectangle2D.Float(x, y, width, height, height, height));
            g2.dispose();
        }
    }

    private BufferedImage createTransparentImage(int width, int height, double scaleFactor) {
        if (image == null || image.getWidth() != width || image.getHeight() != height) {
            image = ColorPickerUtils.createTransparentImage(getBackground(), scale(5, scaleFactor), width, height, height);
        }
        return image;
    }
}
