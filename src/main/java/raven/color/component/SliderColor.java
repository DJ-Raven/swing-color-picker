package raven.color.component;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.util.ColorFunctions;
import com.formdev.flatlaf.util.HiDPIUtils;
import raven.color.utils.ColorPickerUtils;
import raven.color.utils.ColorLocation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

public abstract class SliderColor extends JComponent {

    private final LocationChangeEvent event = new LocationChangeEvent(this);
    private final int selectedSize;
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
    protected void paintComponent(Graphics g) {
        Rectangle rec = getSlideRectangle();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        HiDPIUtils.paintAtScale1x(g2, rec.x, rec.y, rec.width, rec.height, this::paint);

        int size = ColorPickerUtils.scale(selectedSize);
        ColorLocation value = getValue();
        if (value != null) {
            int selectionX = (int) (rec.x + rec.width * value.getX());
            int selectionY = (int) (rec.y + rec.height * value.getY());

            paintSelection(g2, selectionX, selectionY, size);
        }
        g2.dispose();
        super.paintComponent(g);
    }

    protected abstract void valueChanged(ColorLocation value, LocationChangeEvent event);

    protected abstract ColorLocation getValue();

    protected abstract void paint(Graphics2D g, int x, int y, int width, int height, double scaleFactor);

    protected Rectangle getSlideRectangle() {
        Insets insets = getInsets();
        int x = insets.left;
        int y = insets.top;
        int width = getWidth() - (insets.left + insets.right);
        int height = getHeight() - (insets.top + insets.bottom);
        return new Rectangle(x, y, width, height);
    }

    protected void paintSelection(Graphics2D g2, int x, int y, int size) {
        g2.translate(x - size / 2f, y - size / 2f);

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
}
