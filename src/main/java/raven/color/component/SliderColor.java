package raven.color.component;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.util.ColorFunctions;
import com.formdev.flatlaf.util.HiDPIUtils;
import raven.color.utils.ColorLocation;
import raven.color.utils.ColorPickerUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

public abstract class SliderColor extends ColorElement {

    private final LocationChangeEvent event = new LocationChangeEvent(this);
    private int selectedSize;
    private Insets sliderInsets = new Insets(4, 10, 4, 10);
    private MouseAdapter mouseListener;

    public SliderColor() {
        this(18);
    }

    public SliderColor(int selectedSize) {
        this.selectedSize = selectedSize;
    }

    public void install() {
        mouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    mouseChange(e.getPoint(), true);
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    mouseChange(e.getPoint(), false);
                }
            }
        };
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
    }

    public void uninstall() {
        if (mouseListener != null) {
            removeMouseListener(mouseListener);
            removeMouseMotionListener(mouseListener);
            mouseListener = null;
        }
    }

    private void mouseChange(Point point, boolean isPressed) {
        Rectangle rec = getSlideRectangle();
        int px = point.x - rec.x;
        int py = point.y - rec.y;
        float vx = ((float) px) / (float) rec.width;
        float vy = ((float) py) / (float) rec.height;
        vx = Math.max(0f, Math.min(1f, vx));
        vy = Math.max(0f, Math.min(1f, vy));
        ColorLocation loc = new ColorLocation(vx, vy);
        event.reset();
        if (isPressed) {
            event.setPressedLocation(loc);
        }
        valueChanged(loc, event);
    }

    @Override
    public Dimension getMinimumSize() {
        if (isMinimumSizeSet()) {
            return super.getMinimumSize();
        }
        int size = ColorPickerUtils.scale(selectedSize);
        return new Dimension(size, size);
    }

    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {
            return super.getPreferredSize();
        }
        int size = ColorPickerUtils.scale(selectedSize);
        return new Dimension(size, size);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Rectangle rec = getSlideRectangle();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (rec.width > 0 && rec.height > 0) {
            paintSliderColor(g2, rec.x, rec.y, rec.width, rec.height);
        }

        if (selectedSize > 0) {
            int size = ColorPickerUtils.scale(selectedSize);
            ColorLocation value = getValue();
            if (value != null) {
                int selectionX = (int) (rec.x + rec.width * value.getX());
                int selectionY = (int) (rec.y + rec.height * value.getY());

                paintSelection(g2, selectionX, selectionY, size);
            }
        }
        g2.dispose();
        super.paintComponent(g);
    }

    protected void paintSliderColor(Graphics2D g2, int x, int y, int width, int height) {
        HiDPIUtils.paintAtScale1x(g2, x, y, width, height, this::paint);
    }

    protected abstract void valueChanged(ColorLocation value, LocationChangeEvent event);

    protected abstract ColorLocation getValue();

    protected abstract void paint(Graphics2D g, int x, int y, int width, int height, double scaleFactor);

    protected Rectangle getSlideRectangle() {
        Insets insets = ColorPickerUtils.scale(getSliderInsets());
        int x = insets.left;
        int y = insets.top;
        int width = getWidth() - (insets.left + insets.right);
        int height = getHeight() - (insets.top + insets.bottom);
        return new Rectangle(x, y, width, height);
    }

    protected void paintSelection(Graphics2D g2, int x, int y, int size) {
        double tranX = x - size / 2f;
        double tranY = y - size / 2f;
        g2.translate(tranX, tranY);

        g2.setColor(UIManager.getColor("Component.borderColor"));
        g2.fill(ColorPickerUtils.createShape(size, 0.6f, 0f));

        g2.setColor(getTrackColor());
        g2.fill(ColorPickerUtils.createShape(size, 0.6f, ColorPickerUtils.scale(1f)));

        Color selectedColor = getSelectedColor();
        if (selectedColor != null) {
            float s = size * 0.5f;
            float sx = (size - s) / 2f;
            g2.setColor(selectedColor);
            g2.fill(new Ellipse2D.Double(sx, sx, s, s));
        }
        g2.translate(-tranX, -tranY);
    }

    protected int scale(int value, double scaleFactor) {
        return (int) Math.ceil(ColorPickerUtils.scale(value) * scaleFactor);
    }

    protected Color getTrackColor() {
        if (FlatLaf.isLafDark()) {
            return ColorFunctions.tint(getBackground(), 0.8f);
        }
        return ColorFunctions.tint(getBackground(), 0.5f);
    }

    protected Color getSelectedColor() {
        return null;
    }

    public int getSelectedSize() {
        return selectedSize;
    }

    public void setSelectedSize(int selectedSize) {
        if (this.selectedSize != selectedSize) {
            this.selectedSize = selectedSize;
            repaint();
        }
    }

    public Insets getSliderInsets() {
        return sliderInsets;
    }

    public void setSliderInsets(Insets sliderInsets) {
        this.sliderInsets = sliderInsets;
        repaint();
    }
}
