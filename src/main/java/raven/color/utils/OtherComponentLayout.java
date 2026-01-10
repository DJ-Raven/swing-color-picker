package raven.color.utils;

import java.awt.*;

public class OtherComponentLayout implements LayoutManager {

    private static final int INSETS = 3;
    private static final int SIZE = 33;
    private static final int GAP = 7;

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

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
        int insets = ColorPickerUtils.scale(INSETS);
        int size = ColorPickerUtils.scale(SIZE);
        int width = insets * 2;
        int height = size + insets * 2;
        int count = parent.getComponentCount();
        width += size * count;
        if (count > 1) {
            width += ColorPickerUtils.scale(GAP) * (count - 1);
        }
        return new Dimension(width, height);
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            int insets = ColorPickerUtils.scale(INSETS);
            int size = ColorPickerUtils.scale(SIZE);
            int gap = ColorPickerUtils.scale(GAP);
            int x = insets;
            int count = parent.getComponentCount();
            for (int i = 0; i < count; i++) {
                Component com = parent.getComponent(i);
                com.setBounds(x, insets, size, size);
                x += size + gap;
            }
        }
    }
}
