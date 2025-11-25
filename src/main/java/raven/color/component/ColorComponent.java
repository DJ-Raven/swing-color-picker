package raven.color.component;

import com.formdev.flatlaf.util.HiDPIUtils;
import com.formdev.flatlaf.util.ScaledEmptyBorder;
import com.formdev.flatlaf.util.UIScale;
import raven.color.ColorPicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class ColorComponent extends JComponent {

    private final ColorPicker colorPicker;
    private MouseAdapter mouseListener;
    private Point2D.Float selectedPoint;
    private boolean notifyRepaint = true;

    public ColorComponent(ColorPicker colorPicker) {
        this.colorPicker = colorPicker;
        install();
    }

    public void install() {
        setBorder(new ScaledEmptyBorder(10, 10, 10, 10));
        mouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    mouseChange(e.getPoint());
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    mouseChange(e.getPoint());
                }
            }
        };
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);

        if (selectedPoint == null && (colorPicker.getSelectionModel() != null && colorPicker.getSelectionModel().getSelectedColor() != null)) {
            selectedPoint = colorToPoint(colorPicker.getSelectionModel().getSelectedColor());
        }
    }

    public void uninstall() {
        if (mouseListener != null) {
            removeMouseListener(mouseListener);
            removeMouseMotionListener(mouseListener);
            mouseListener = null;
            selectedPoint = null;
        }
    }

    public boolean isNotifyRepaint() {
        return notifyRepaint;
    }

    public void changeSelectedPoint(Color color) {
        if (color == null) {
            selectedPoint = null;
        } else {
            selectedPoint = colorToPoint(color);
        }
    }

    private void mouseChange(Point point) {
        Insets insets = getInsets();
        point.x -= insets.left;
        point.y -= insets.top;
        int width = getWidth() - (insets.left + insets.right);
        int height = getHeight() - (insets.top + insets.bottom);
        selectedPoint = toPoint(point, width, height);
        Color color = pointToColor(selectedPoint, colorPicker.getSelectionModel().getHue());
        Color oldColor = colorPicker.getSelectionModel().getSelectedColor();
        if (oldColor != null) {
            int alpha = oldColor.getAlpha();
            if (alpha != 255) {
                color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            }
        }
        try {
            notifyRepaint = false;
            colorPicker.getSelectionModel().setSelectedColor(color, false);
        } finally {
            repaint();
            notifyRepaint = true;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Insets insets = getInsets();
        int x = insets.left;
        int y = insets.top;
        int width = getWidth() - (insets.left + insets.right);
        int height = getHeight() - (insets.top + insets.bottom);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        HiDPIUtils.paintAtScale1x(g2, x, y, width, height, this::paintImpl);

        paintSelection(g2, x, y, width, height);
        g2.dispose();
        super.paintComponent(g);
    }

    private void paintImpl(Graphics2D g, int x, int y, int width, int height, double scaleFactor) {
        BufferedImage image = colorPicker.getSelectionModel().getColorImage(width, height, scale(10, scaleFactor));
        if (image != null) {
            g.drawImage(image, x, y, null);
        }
    }

    private void paintSelection(Graphics2D g2, int x, int y, int width, int height) {
        if (selectedPoint == null) {
            return;
        }

        float size = UIScale.scale(18f);
        float lx = selectedPoint.x * width;
        float ly = selectedPoint.y * height;
        g2.translate(x + lx - size / 2f, y + ly - size / 2f);

        g2.setColor(UIManager.getColor("Component.borderColor"));
        g2.fill(new Ellipse2D.Float(0, 0, size, size));

        g2.setColor(Color.WHITE);
        float border = UIScale.scale(1f);
        g2.fill(new Ellipse2D.Float(border, border, size - border * 2, size - border * 2));

        g2.setColor(new Color(colorPicker.getSelectionModel().getSelectedColor().getRGB()));
        float borderIn = size * 0.25f;
        g2.fill(new Ellipse2D.Float(borderIn, borderIn, size - borderIn * 2, size - borderIn * 2));
    }

    private Point2D.Float colorToPoint(Color color) {
        float[] hbs = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float x = hbs[1];
        float y = 1f - hbs[2];
        return new Point2D.Float(x, y);
    }

    private Color pointToColor(Point2D.Float point, float hue) {
        float saturation = point.x;
        float brightness = 1f - point.y;
        return Color.getHSBColor(hue, saturation, brightness);
    }

    private Point2D.Float toPoint(Point point, int width, int height) {
        float x = clamp(point.x / (float) width);
        float y = clamp(point.y / (float) height);
        return new Point2D.Float(x, y);
    }

    private float clamp(float value) {
        return Math.max(0f, Math.min(1f, value));
    }

    private int scale(int value, double scaleFactor) {
        return (int) Math.ceil(UIScale.scale(value) * scaleFactor);
    }
}
