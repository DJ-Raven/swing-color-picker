package raven.color.component;

import com.formdev.flatlaf.util.ScaledEmptyBorder;
import raven.color.ColorPicker;
import raven.color.ColorPickerUtils;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class ColorAlphaComponent extends SliderColor {

    private final ColorPicker colorPicker;
    private BufferedImage image;

    public ColorAlphaComponent(ColorPicker colorPicker) {
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
        Color color = colorPicker.getSelectionModel().getSelectedColor();
        if (color != null) {
            colorPicker.getSelectionModel().setSelectedColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (location.x * 255f)), false);
        }
    }

    @Override
    protected Location getValue() {
        Color color = colorPicker.getSelectionModel().getSelectedColor();
        if (color == null) {
            return new Location(1f, 0.5f);
        }
        return new Location(color.getAlpha() / 255f, 0.5f);
    }

    @Override
    protected void paint(Graphics2D g, int x, int y, int width, int height, double scaleFactor) {
        BufferedImage img = createTransparentImage(width, height, scaleFactor);
        if (img != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawImage(img, x, y, null);
            g2.setPaint(new GradientPaint(x, y, new Color(255, 255, 255, 0), x + width, y, Color.getHSBColor(colorPicker.getSelectionModel().getHue(), 1f, 1f)));
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
}
