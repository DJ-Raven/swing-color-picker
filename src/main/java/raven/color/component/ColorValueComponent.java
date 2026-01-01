package raven.color.component;

import com.formdev.flatlaf.util.ScaledEmptyBorder;
import raven.color.utils.ColorLocation;
import raven.color.utils.ColorPickerModel;

import java.awt.*;

public class ColorValueComponent extends SliderColor {

    private ColorPickerModel model;

    public ColorValueComponent(ColorPickerModel model) {
        this.model = model;
        install();
    }

    @Override
    public void install() {
        super.install();
        setBorder(new ScaledEmptyBorder(5, 10, 5, 10));
    }

    @Override
    protected void valueChanged(ColorLocation value, LocationChangeEvent event) {
        getModel().setValue(value.getX());
    }

    @Override
    protected ColorLocation getValue() {
        return new ColorLocation(getModel().getValue(), 0.5f);
    }

    @Override
    protected void paint(Graphics2D g, int x, int y, int width, int height, double scaleFactor) {
        Image image = getModel().getValueImage(width, height, 999);
        if (image != null) {
            g.drawImage(image, x, y, null);
        }
    }

    public ColorPickerModel getModel() {
        return model;
    }

    public void setModel(ColorPickerModel model) {
        this.model = model;
    }
}
