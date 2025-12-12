package raven.color.component;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.util.ColorFunctions;
import com.formdev.flatlaf.util.HiDPIUtils;
import com.formdev.flatlaf.util.UIScale;
import raven.color.ColorPickerUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

public abstract class SliderColor extends JComponent {

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
    }

    public void uninstall() {
        if (mouseListener != null) {
            removeMouseListener(mouseListener);
            removeMouseMotionListener(mouseListener);
            mouseListener = null;
        }
    }

    private void mouseChange(Point point) {
        Insets insets = getInsets();
        int x = insets.left;
        int y = insets.top;
        int width = getWidth() - (insets.left + insets.right);
        int height = getHeight() - (insets.top + insets.bottom);
        int px = point.x - x;
        int py = point.y - y;
        float vx = ((float) px) / (float) width;
        float vy = ((float) py) / (float) height;
        vx = Math.max(0f, Math.min(1f, vx));
        vy = Math.max(0f, Math.min(1f, vy));
        valueChanged(new Location(vx, vy));
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

        HiDPIUtils.paintAtScale1x(g2, x, y, width, height, this::paint);

        int size = UIScale.scale(selectedSize);
        Location location = getValue();
        int selectionX = (int) (x + width * location.x);
        int selectionY = (int) (y + height * location.y);

        paintSelection(g2, selectionX, selectionY, size);

        g2.dispose();
        super.paintComponent(g);
    }

    protected abstract void valueChanged(Location location);

    protected abstract Location getValue();

    protected abstract void paint(Graphics2D g, int x, int y, int width, int height, double scaleFactor);

    protected void paintSelection(Graphics2D g2, int x, int y, int size) {
        g2.translate(x - size / 2f, y - size / 2f);

        g2.setColor(UIManager.getColor("Component.borderColor"));
        g2.fill(ColorPickerUtils.createShape(size, 0.6f, 0f));

        g2.setColor(getTrackColor());
        g2.fill(ColorPickerUtils.createShape(size, 0.6f, UIScale.scale(1f)));

        Color selectedColor = getSelectedColor();
        if (selectedColor != null) {
            float s = size * 0.5f;
            float sx = (size - s) / 2f;
            g2.setColor(selectedColor);
            g2.fill(new Ellipse2D.Double(sx, sx, s, s));
        }
    }

    protected int scale(int value, double scaleFactor) {
        return (int) Math.ceil(UIScale.scale(value) * scaleFactor);
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

    public static class Location {
        public final float x;
        public final float y;

        public Location(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
