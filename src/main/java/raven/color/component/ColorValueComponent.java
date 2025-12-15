package raven.color.component;

import com.formdev.flatlaf.util.ScaledEmptyBorder;
import raven.color.ColorPicker;

import java.awt.*;

public class ColorValueComponent extends SliderColor {

    private final ColorPicker colorPicker;

    public ColorValueComponent(ColorPicker colorPicker) {
        this.colorPicker = colorPicker;
        install();
    }

    @Override
    public void install() {
        super.install();
        setBorder(new ScaledEmptyBorder(5, 10, 5, 10));
    }

    @Override
    protected void valueChanged(ColorLocation value) {
        colorPicker.getModel().setValue(value.getX());
    }

    @Override
    protected ColorLocation getValue() {
        return new ColorLocation(colorPicker.getModel().getValue(), 0.5f);
    }

    @Override
    protected void paint(Graphics2D g, int x, int y, int width, int height, double scaleFactor) {
        Image image = colorPicker.getModel().getValueImage(width, height, 999);
        if (image != null) {
            g.drawImage(image, x, y, null);
        }
    }
}
