package raven.color.component;

import com.formdev.flatlaf.util.HiDPIUtils;
import raven.color.utils.ColorPickerUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class ColorPreview extends ColorElement {

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        repaint();
    }

    private Color color;
    private BufferedImage image;

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = getWidth();
        int height = getHeight();

        int arc = ColorPickerUtils.scale(10);
        int border = ColorPickerUtils.scale(1);

        int innerWidth = width - border * 2;
        int innerHeight = height - border * 2;
        int innerArc = arc - border;

        // paint transparent background
        HiDPIUtils.paintAtScale1x(g2, border, border, innerWidth, innerHeight, this::paintTransparent);

        g2.setColor(UIManager.getColor("Component.borderColor"));
        Area area = new Area(new RoundRectangle2D.Float(0, 0, width, height, arc, arc));
        area.subtract(new Area(new RoundRectangle2D.Float(border, border, innerWidth, innerHeight, innerArc, innerArc)));
        g2.fill(area);

        if (color != null) {
            g2.setColor(color);
            g2.setComposite(AlphaComposite.SrcOver);
            g2.fill(new RoundRectangle2D.Float(border, border, innerWidth, innerHeight, innerArc, innerArc));
        }
        g2.dispose();
        super.paintComponent(g);
    }

    protected void paintTransparent(Graphics2D g, int x, int y, int width, int height, double scaleFactor) {
        Image image = createTransparentImage(width, height, scaleFactor);
        if (image != null) {
            g.drawImage(image, x, y, null);
        }
    }

    private BufferedImage createTransparentImage(int width, int height, double scaleFactor) {
        if (image == null || image.getWidth() != width || image.getHeight() != height) {
            image = ColorPickerUtils.createTransparentImage(getBackground(), scale(5, scaleFactor), width, height, scale(9, scaleFactor));
        }
        return image;
    }

    protected int scale(int value, double scaleFactor) {
        return (int) Math.ceil(ColorPickerUtils.scale(value) * scaleFactor);
    }
}
