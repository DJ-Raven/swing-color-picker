package raven.color.component;

import com.formdev.flatlaf.util.HiDPIUtils;
import com.formdev.flatlaf.util.ScaledEmptyBorder;
import raven.color.ColorPicker;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ColorHueComponent extends SliderColor {

    private final ColorPicker colorPicker;


    public ColorHueComponent(ColorPicker colorPicker) {
        this.colorPicker = colorPicker;
        init();
    }

    private void init() {
        install();
    }

    @Override
    public void install() {
        super.install();
        setBorder(new ScaledEmptyBorder(5, 10, 5, 10));
    }

    @Override
    protected void valueChanged(float v) {
        colorPicker.getSelectionModel().setHue(v);
    }

    @Override
    protected float getValue() {
        return colorPicker.getSelectionModel().getHue();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Insets insets = getInsets();
        int x = insets.left;
        int y = insets.top;
        int width = getWidth() - (insets.left + insets.right);
        int height = getHeight() - (insets.top + insets.bottom);

        // draw image
        HiDPIUtils.paintAtScale1x((Graphics2D) g, x, y, width, height, this::paintImpl);
        super.paintComponent(g);
    }

    private void paintImpl(Graphics2D g, int x, int y, int width, int height, double scaleFactor) {
        BufferedImage image = colorPicker.getSelectionModel().getHueImage(width, height, height);
        if (image != null) {
            g.drawImage(image, x, y, null);
        }
    }
}
