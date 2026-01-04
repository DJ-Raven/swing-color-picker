package raven.color.component;

import raven.color.utils.ColorLocation;
import raven.color.utils.ColorPickerModel;
import raven.color.utils.ColorPickerUtils;

import java.awt.*;

public abstract class OrientationSliderColorModel extends SliderColorModel {

    private Orientation orientation = Orientation.HORIZONTAL;

    public OrientationSliderColorModel(ColorPickerModel model) {
        super(model);
    }

    public OrientationSliderColorModel(ColorPickerModel model, boolean installModelListener) {
        super(model, installModelListener);
    }

    public OrientationSliderColorModel(ColorPickerModel model, Orientation orientation) {
        super(model);
        this.orientation = orientation;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public ColorLocation orientationValue(ColorLocation value) {
        if (isHorizontal()) {
            return value;
        }
        return new ColorLocation(value.getY(), value.getX());
    }

    public boolean isHorizontal() {
        return orientation == Orientation.HORIZONTAL;
    }

    @Override
    protected Rectangle getSlideRectangle() {
        Rectangle rec = super.getSlideRectangle();
        if (!isHorizontal()) {
            Insets insets = ColorPickerUtils.scale(getSliderInsets());
            int sx = insets.top;
            int sy = insets.left;
            int sw = getWidth() - (insets.top + insets.bottom);
            int sh = getHeight() - (insets.left + insets.right);
            rec.setBounds(sx, sy, sw, sh);
        }
        return rec;
    }

    @Override
    protected void paintSliderColor(Graphics2D g2, int x, int y, int width, int height) {
        int sw = width;
        int sh = height;
        if (!isHorizontal()) {
            sw = height;
            sh = width;
        }
        super.paintSliderColor(g2, x, y, sw, sh);
    }

    public enum Orientation {
        HORIZONTAL, VERTICAL
    }
}
