package raven.color.component;

import com.formdev.flatlaf.util.ScaledEmptyBorder;
import raven.color.ColorPicker;

import java.awt.*;

public class ColorComponent extends SliderColor {

    private final ColorPicker colorPicker;
    private ColorLocation selectedPoint;
    private boolean notifyRepaint = true;

    public ColorComponent(ColorPicker colorPicker) {
        this.colorPicker = colorPicker;
        install();
    }

    @Override
    public void install() {
        super.install();
        setBorder(new ScaledEmptyBorder(10, 10, 10, 10));
        if (selectedPoint == null && (colorPicker.getModel() != null && colorPicker.getModel().getSelectedColor() != null)) {
            selectedPoint = colorPicker.getModel().colorToLocation(colorPicker.getModel().getSelectedColor());
        }
    }

    @Override
    public void uninstall() {
        super.uninstall();
        selectedPoint = null;
    }

    @Override
    protected void valueChanged(ColorLocation value) {
        selectedPoint = value;
        colorPicker.getModel().locationValue(selectedPoint);
        float v = colorPicker.getModel().getValue();
        Color color = colorPicker.getModel().locationToColor(selectedPoint, v);
        Color oldColor = colorPicker.getModel().getSelectedColor();
        if (oldColor != null) {
            int alpha = oldColor.getAlpha();
            if (alpha != 255) {
                color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            }
        }
        try {
            notifyRepaint = false;
            colorPicker.getModel().setSelectedColor(color, false);
        } finally {
            repaint();
            notifyRepaint = true;
        }
    }

    @Override
    protected ColorLocation getValue() {
        return selectedPoint;
    }

    @Override
    protected Rectangle getSlideRectangle() {
        Rectangle rec = super.getSlideRectangle();
        ColorDimension size = colorPicker.getModel().getDimensionImage(rec.width, rec.height);
        int x = (rec.width - size.width) / 2;
        int y = (rec.height - size.height) / 2;
        rec.x += x;
        rec.y += y;
        rec.width = size.width;
        rec.height = size.height;
        return rec;
    }

    public boolean isNotifyRepaint() {
        return notifyRepaint;
    }

    public void changeSelectedPoint(Color color) {
        if (color == null) {
            selectedPoint = null;
        } else {
            selectedPoint = colorPicker.getModel().colorToLocation(color);
        }
    }

    @Override
    protected void paint(Graphics2D g, int x, int y, int width, int height, double scaleFactor) {
        Image image = colorPicker.getModel().getColorImage(width, height, scale(10, scaleFactor));
        if (image != null) {
            int lx = (width - image.getWidth(null)) / 2;
            int ly = (height - image.getHeight(null)) / 2;
            g.drawImage(image, x + lx, y + ly, null);
        }
    }

    @Override
    protected Color getSelectedColor() {
        return new Color(colorPicker.getModel().getSelectedColor().getRGB());
    }
}
