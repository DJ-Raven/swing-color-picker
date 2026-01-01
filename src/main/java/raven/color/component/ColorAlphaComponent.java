package raven.color.component;

import com.formdev.flatlaf.util.ScaledEmptyBorder;
import raven.color.utils.ColorPickerUtils;
import raven.color.utils.ColorLocation;
import raven.color.utils.ColorPickerModel;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class ColorAlphaComponent extends SliderColor {

    private ColorPickerModel model;
    private BufferedImage image;

    public ColorAlphaComponent(ColorPickerModel model) {
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
        Color color = getModel().getSelectedColor();
        if (color != null) {
            getModel().setSelectedColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (value.getX() * 255f)), false);
        }
    }

    @Override
    protected ColorLocation getValue() {
        Color color = getModel().getSelectedColor();
        return new ColorLocation(color.getAlpha() / 255f, 0.5f);
    }

    @Override
    protected void paint(Graphics2D g, int x, int y, int width, int height, double scaleFactor) {
        BufferedImage img = createTransparentImage(width, height, scaleFactor);
        if (img != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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

    public ColorPickerModel getModel() {
        return model;
    }

    public void setModel(ColorPickerModel model) {
        this.model = model;
    }
}
