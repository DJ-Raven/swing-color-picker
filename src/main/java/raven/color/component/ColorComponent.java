package raven.color.component;

import com.formdev.flatlaf.util.ScaledEmptyBorder;
import raven.color.utils.ColorDimension;
import raven.color.utils.ColorLocation;
import raven.color.utils.ColorPickerModel;

import java.awt.*;

public class ColorComponent extends SliderColor {

    private ColorPickerModel model;
    private ColorLocation selectedPoint;
    private boolean notifyRepaint = true;

    public ColorComponent(ColorPickerModel model) {
        this.model = model;
        install();
    }

    @Override
    public void install() {
        super.install();
        setBorder(new ScaledEmptyBorder(10, 10, 10, 10));
        changeSelectedPoint(model.getSelectedColor());
    }

    @Override
    public void uninstall() {
        super.uninstall();
        selectedPoint = null;
    }

    @Override
    protected void valueChanged(ColorLocation value, LocationChangeEvent event) {
        getModel().locationValue(value, event);
        if (!event.isConsumed()) {
            selectedPoint = value;
            float v = getModel().getValue();
            Color color = getModel().locationToColor(selectedPoint, v);
            Color oldColor = getModel().getSelectedColor();
            if (oldColor != null) {
                int alpha = oldColor.getAlpha();
                if (alpha != 255) {
                    color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
                }
            }
            try {
                notifyRepaint = false;
                getModel().setSelectedColor(color, false);
            } finally {
                repaint();
                notifyRepaint = true;
            }
        }
    }

    @Override
    protected ColorLocation getValue() {
        return selectedPoint;
    }

    @Override
    protected Rectangle getSlideRectangle() {
        Rectangle rec = super.getSlideRectangle();
        ColorDimension size = getModel().getDimensionImage(rec.width, rec.height);
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
            selectedPoint = getModel().colorToLocation(color);
        }
    }

    public void changeSelectedPoint(ColorLocation location) {
        if (selectedPoint == null) {
            selectedPoint = new ColorLocation(location);
        } else {
            selectedPoint.set(location);
        }
    }

    @Override
    protected void paint(Graphics2D g, int x, int y, int width, int height, double scaleFactor) {
        Image image = getModel().getColorImage(width, height, scale(10, scaleFactor));
        if (image != null) {
            int lx = (width - image.getWidth(null)) / 2;
            int ly = (height - image.getHeight(null)) / 2;
            g.drawImage(image, x + lx, y + ly, null);
        }
    }

    @Override
    protected Color getSelectedColor() {
        return new Color(getModel().getSelectedColor().getRGB());
    }

    public ColorPickerModel getModel() {
        return model;
    }

    public void setModel(ColorPickerModel model) {
        this.model = model;
        if (model != null) {
            changeSelectedPoint(model.getSelectedColor());
        }
    }
}
