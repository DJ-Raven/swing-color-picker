package raven.color.component;

import raven.color.event.ColorChangeEvent;
import raven.color.utils.ColorLocation;
import raven.color.utils.ColorPickerModel;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class ColorValueComponent extends OrientationSliderColorModel {

    public ColorValueComponent(ColorPickerModel model) {
        super(model);
    }

    public ColorValueComponent(ColorPickerModel model, boolean installModelListener) {
        super(model, installModelListener);
    }

    public ColorValueComponent(ColorPickerModel model, Orientation orientation) {
        super(model, orientation);
    }

    @Override
    protected void valueChanged(ColorLocation value, LocationChangeEvent event) {
        value = orientationValue(value);
        getModel().setValue(value.getX());
    }

    @Override
    protected ColorLocation getValue() {
        return orientationValue(new ColorLocation(getModel().getValue(), 0.5f));
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
        Image image = getModel().getValueImage(width, height, 999);
        if (image != null) {
            AffineTransform transform = null;
            if (!isHorizontal) {
                transform = g.getTransform();
                float centerX = x + height / 2f;
                float centerY = y + height / 2f;
                g.rotate(Math.toRadians(90), centerX, centerY);
            }
            g.drawImage(image, x, y, null);
            if (transform != null) {
                g.setTransform(transform);
            }
        }
    }
}
