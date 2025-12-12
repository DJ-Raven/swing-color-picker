package raven.color.component;

import com.formdev.flatlaf.util.ScaledEmptyBorder;
import raven.color.ColorPicker;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ColorHueComponent extends SliderColor {

    private final ColorPicker colorPicker;

    public ColorHueComponent(ColorPicker colorPicker) {
        this.colorPicker = colorPicker;
        install();
    }

    @Override
    public void install() {
        super.install();
        setBorder(new ScaledEmptyBorder(5, 10, 5, 10));
    }

    @Override
    protected void valueChanged(Location location) {
        colorPicker.getSelectionModel().setHue(location.x);
    }

    @Override
    protected Location getValue() {
        return new Location(colorPicker.getSelectionModel().getHue(), 0.5f);
    }

    @Override
    protected void paint(Graphics2D g, int x, int y, int width, int height, double scaleFactor) {
        BufferedImage image = colorPicker.getSelectionModel().getHueImage(width, height, height);
        if (image != null) {
            g.drawImage(image, x, y, null);
        }
    }
}
