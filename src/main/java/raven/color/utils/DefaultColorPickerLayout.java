package raven.color.utils;

import java.awt.*;

public class DefaultColorPickerLayout extends ColorPickerLayout {

    private static final int GAP = 3;
    private static final int COLOR_WIDTH = 280;
    private static final int COLOR_HEIGHT = 180;

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            return size(parent);
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            return size(parent);
        }
    }

    private Dimension size(Container parent) {
        Insets insets = parent.getInsets();
        int w = scale(COLOR_WIDTH) + insets.left + insets.right;
        int h = insets.top + insets.bottom;
        int gap = scale(GAP);
        int gapCount = 0;
        if (colorComponent != null) {
            h += scale(COLOR_HEIGHT);
            gapCount++;
        }

        int sliderHeight = getSliderHeight(gap);
        int otherHeight = 0;

        if (colorOtherComponent != null) {
            otherHeight = colorOtherComponent.getPreferredSize().height;
        }

        if (sliderHeight > 0 || otherHeight > 0) {
            h += Math.max(otherHeight, sliderHeight);
            gapCount++;
        }

        if (colorField != null) {
            h += colorField.getPreferredSize().height;
            gapCount++;
        }
        if (colorPalette != null) {
            h += colorPalette.getPreferredSize().height;
            gapCount++;
        }
        if (gapCount > 1) {
            h += (gapCount - 1) * gap;
        }
        return new Dimension(w, h);
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int width = parent.getWidth() - (insets.left + insets.right);
            int x = insets.left;
            int y = insets.top;
            int gap = scale(GAP);

            // color component
            if (colorComponent != null) {
                int ch = scale(COLOR_HEIGHT);
                colorComponent.setBounds(x, y, width, ch);
                y += ch + gap;
            }

            // other component
            int sliderHeight = getSliderHeight(gap);
            int maxOtherHeight = sliderHeight;
            int otherWidth = 0;
            int otherHeight = 0;
            if (colorOtherComponent != null) {
                Dimension otherSize = colorOtherComponent.getPreferredSize();
                int cw = Math.min(otherSize.width, width);
                int add = scale(7);
                int otherY = y;
                otherWidth = cw + gap + add;
                otherHeight = otherSize.height;
                if (otherHeight <= sliderHeight) {
                    otherY += (sliderHeight - otherHeight) / 2;
                } else {
                    maxOtherHeight = otherHeight;
                }
                colorOtherComponent.setBounds(x + add, otherY, cw, otherSize.height);
            }

            // color value component
            int sliderY = y + (maxOtherHeight - sliderHeight) / 2;

            if (colorValue != null) {
                int cw = Math.max(width - otherWidth, 0);
                int ch = colorValue.getPreferredSize().height;
                colorValue.setBounds(x + otherWidth, sliderY, cw, ch);
                sliderY += ch + gap;
            }

            // color alpha component
            if (colorAlpha != null) {
                int cw = Math.max(width - otherWidth, 0);
                int ch = colorAlpha.getPreferredSize().height;
                colorAlpha.setBounds(x + otherWidth, sliderY, cw, ch);
            }
            if (otherHeight > 0 || sliderHeight > 0) {
                y += Math.max(otherHeight, sliderHeight) + gap;
            }

            // color field component
            if (colorField != null) {
                int ch = colorField.getPreferredSize().height;
                colorField.setBounds(x, y, width, ch);
                y += ch + gap;
            }

            // color palette
            if (colorPalette != null) {
                int ch = colorPalette.getPreferredSize().height;
                colorPalette.setBounds(x, y, width, ch);
            }
        }
    }

    private int getSliderHeight(int gap) {
        int height = 0;
        if (colorValue != null) {
            height += colorValue.getPreferredSize().height;
        }
        if (colorAlpha != null) {
            if (height > 0) {
                height += gap;
            }
            height += colorAlpha.getPreferredSize().height;
        }
        return height;
    }

    private int scale(int v) {
        return ColorPickerUtils.scale(v);
    }
}
