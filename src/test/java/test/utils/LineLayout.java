package test.utils;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;

import javax.swing.*;
import java.awt.*;

/**
 * Support horizontal and vertical layout
 */
public class LineLayout implements LayoutManager {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    public static final int START = 1;
    public static final int CENTER = 2;
    public static final int END = 3;

    private Insets padding = new Insets(5, 5, 5, 5);
    private int alignment;
    private int orientation;
    private int hGap;
    private int vGap;
    private boolean fill;

    public LineLayout() {
        this(HORIZONTAL);
    }

    public LineLayout(int orientation) {
        this(orientation, false);
    }

    public LineLayout(int orientation, boolean fill) {
        this(orientation, fill, START);
    }

    public LineLayout(int orientation, boolean fill, int alignment) {
        this(orientation, 7, 7, fill, alignment);
    }

    public LineLayout(int orientation, int hGap, int vGap, boolean fill, int alignment) {
        this.orientation = orientation;
        this.hGap = hGap;
        this.vGap = vGap;
        this.fill = fill;
        this.alignment = alignment;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            return size(parent, false);
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            return size(parent, true);
        }
    }

    private Dimension size(Container parent, boolean min) {
        Insets insets = getInsets(parent);
        int width = 0;
        int height = 0;
        int count = parent.getComponentCount();
        int countGap = 0;
        for (int i = 0; i < count; i++) {
            Component com = parent.getComponent(i);
            if (com.isVisible()) {
                Dimension size = min ? com.getMinimumSize() : com.getPreferredSize();
                Insets focus = getVisualPadding(com);
                if (focus != null) {
                    size.width -= (focus.left + focus.right);
                    size.height -= (focus.top + focus.bottom);
                }
                if (orientation == HORIZONTAL) {
                    width += size.width;
                    height = Math.max(height, size.height);
                } else {
                    height += size.height;
                    width = Math.max(width, size.width);
                }
                countGap++;
            }
        }
        width += insets.left + insets.right;
        height += insets.top + insets.bottom;

        if (countGap > 1) {
            if (orientation == HORIZONTAL) {
                width += scale(hGap) * (countGap - 1);
            } else {
                height += scale(vGap) * (countGap - 1);
            }
        }

        return new Dimension(width, height);
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = getInsets(parent);
            int width = parent.getWidth() - (insets.left + insets.right);
            int height = parent.getHeight() - (insets.top + insets.bottom);
            int x = insets.left;
            int y = insets.top;

            Dimension parentSize = size(parent, true);
            if (orientation == HORIZONTAL) {
                if (alignment == CENTER) {
                    x = Math.max(x, insets.left + insets.right + ((width - parentSize.width) / 2));
                } else if (alignment == END) {
                    x = insets.left + insets.right + insets.left + (width - parentSize.width);
                }
            } else {
                if (alignment == CENTER) {
                    y = Math.max(y, insets.top + insets.bottom + ((height - parentSize.height) / 2));
                } else if (alignment == END) {
                    y = insets.top + insets.bottom + insets.top + (height - parentSize.height);
                }
            }

            int gap = scale(orientation == HORIZONTAL ? hGap : vGap);
            int count = parent.getComponentCount();
            for (int i = 0; i < count; i++) {
                Component com = parent.getComponent(i);
                if (com.isVisible()) {
                    Dimension size = com.getPreferredSize();
                    int lx = x;
                    int ly = y;
                    int w;
                    int h;
                    if (orientation == HORIZONTAL) {
                        w = size.width;
                        h = fill ? height : size.height;
                        x += gap + size.width;
                    } else {
                        w = fill ? width : size.width;
                        h = size.height;
                        y += gap + size.height;
                    }
                    Insets focus = getVisualPadding(com);
                    if (focus != null) {
                        lx -= focus.left;
                        ly -= focus.top;
                    }
                    com.setBounds(lx, ly, w, h);
                }
            }
        }
    }

    protected Insets getInsets(Container parent) {
        Insets insets = parent.getInsets();
        Insets p = scale(padding);
        insets.top += p.top;
        insets.left += p.left;
        insets.bottom += p.bottom;
        insets.right += p.right;
        return insets;
    }

    protected Insets getVisualPadding(Component com) {
        if (com instanceof JComponent) {
            int w = (int) FlatUIUtils.getBorderFocusWidth((JComponent) com);
            return new Insets(w, w, w, w);
        }
        return null;
    }

    protected int scale(int v) {
        return UIScale.scale(v);
    }

    protected Insets scale(Insets v) {
        return UIScale.scale(v);
    }

    public Insets getPadding() {
        return padding;
    }

    public void setPadding(Insets padding) {
        this.padding = padding;
    }

    public int getAlignment() {
        return alignment;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getHGap() {
        return hGap;
    }

    public void setHGap(int hGap) {
        this.hGap = hGap;
    }

    public int getVGap() {
        return vGap;
    }

    public void setVGap(int vGap) {
        this.vGap = vGap;
    }

    public boolean isFill() {
        return fill;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }
}
