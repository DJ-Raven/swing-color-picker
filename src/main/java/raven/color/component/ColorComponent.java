package raven.color.component;

import com.formdev.flatlaf.util.ScaledEmptyBorder;
import raven.color.ColorPicker;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ColorComponent extends SliderColor {

    private final ColorPicker colorPicker;
    private Location selectedPoint;
    private boolean notifyRepaint = true;

    public ColorComponent(ColorPicker colorPicker) {
        this.colorPicker = colorPicker;
        install();
    }

    @Override
    public void install() {
        super.install();
        setBorder(new ScaledEmptyBorder(10, 10, 10, 10));
        if (selectedPoint == null && (colorPicker.getSelectionModel() != null && colorPicker.getSelectionModel().getSelectedColor() != null)) {
            selectedPoint = colorToPoint(colorPicker.getSelectionModel().getSelectedColor());
        }
    }

    @Override
    public void uninstall() {
        super.uninstall();
        selectedPoint = null;
    }

    @Override
    protected void valueChanged(Location location) {
        selectedPoint = location;
        Color color = pointToColor(selectedPoint, colorPicker.getSelectionModel().getHue());
        Color oldColor = colorPicker.getSelectionModel().getSelectedColor();
        if (oldColor != null) {
            int alpha = oldColor.getAlpha();
            if (alpha != 255) {
                color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            }
        }
        try {
            notifyRepaint = false;
            colorPicker.getSelectionModel().setSelectedColor(color, false);
        } finally {
            repaint();
            notifyRepaint = true;
        }
    }

    @Override
    protected Location getValue() {
        return selectedPoint;
    }

    public boolean isNotifyRepaint() {
        return notifyRepaint;
    }

    public void changeSelectedPoint(Color color) {
        if (color == null) {
            selectedPoint = null;
        } else {
            selectedPoint = colorToPoint(color);
        }
    }

    @Override
    protected void paint(Graphics2D g, int x, int y, int width, int height, double scaleFactor) {
        BufferedImage image = colorPicker.getSelectionModel().getColorImage(width, height, scale(10, scaleFactor));
        if (image != null) {
            g.drawImage(image, x, y, null);
        }
    }

    @Override
    protected Color getSelectedColor() {
        return new Color(colorPicker.getSelectionModel().getSelectedColor().getRGB());
    }

    private Location colorToPoint(Color color) {
        float[] hbs = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float x = hbs[1];
        float y = 1f - hbs[2];
        return new Location(x, y);
    }

    private Color pointToColor(Location location, float hue) {
        float saturation = location.x;
        float brightness = 1f - location.y;
        return Color.getHSBColor(hue, saturation, brightness);
    }
}
