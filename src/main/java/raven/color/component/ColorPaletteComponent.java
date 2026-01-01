package raven.color.component;

import com.formdev.flatlaf.util.ScaledEmptyBorder;
import com.formdev.flatlaf.util.UIScale;
import raven.color.utils.ColorPickerUtils;
import raven.color.component.palette.ColorPaletteData;
import raven.color.component.palette.ColorPaletteItemPainter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ColorPaletteComponent extends JComponent {

    private final List<Item> items = new ArrayList<>();
    private ColorPaletteData colorData;
    private ColorPaletteItemPainter itemPainter;

    private int hoverIndex = -1;
    private int selectedIndex = -1;

    public ColorPaletteComponent(ColorPaletteData colorData, ColorPaletteItemPainter itemPainter) {
        this.colorData = colorData;
        this.itemPainter = itemPainter;
        init();
    }

    private void init() {
        setBorder(new ScaledEmptyBorder(10, 10, 5, 10));
        MouseAdapter mouseListener = new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    int index = getIndexOf(e.getPoint());
                    if (hoverIndex == index) {
                        setSelectedIndex(index);
                    }
                    if (index != hoverIndex) {
                        int oldHover = hoverIndex;
                        hoverIndex = index;
                        repaintAt(oldHover);
                        repaintAt(hoverIndex);
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int index = getIndexOf(e.getPoint());
                if (hoverIndex != index) {
                    int oldIndex = hoverIndex;
                    hoverIndex = index;
                    repaintAt(hoverIndex);
                    repaintAt(oldIndex);
                }
            }
        };

        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
    }

    public ColorPaletteData getColorData() {
        return colorData;
    }

    public void setColorData(ColorPaletteData colorData) {
        this.colorData = colorData;
        repaint();
        revalidate();
    }

    public ColorPaletteItemPainter getItemPainter() {
        return itemPainter;
    }

    public void setItemPainter(ColorPaletteItemPainter itemPainter) {
        this.itemPainter = itemPainter;
        repaint();
        revalidate();
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        int oldIndex = this.selectedIndex;
        this.selectedIndex = selectedIndex;
        if (oldIndex != selectedIndex) {
            repaintAt(oldIndex);
        }
        repaintAt(selectedIndex);
        selectedIndex(selectedIndex);
    }

    public Color getColorAt(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index).getColor();
        }
        return null;
    }

    public void addChangeListener(ChangeListener listener) {
        listenerList.add(ChangeListener.class, listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        listenerList.remove(ChangeListener.class, listener);
    }

    private int getIndexOf(Point point) {
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item != null) {
                if (item.getSize().contains(point)) {
                    return item.getIndex();
                }
            }
        }
        return -1;
    }

    private void repaintAt(int index) {
        if (index >= 0 && index < items.size()) {
            int border = scale(itemPainter.getItemBorderSize());
            Rectangle itemRec = items.get(index).getSize();
            int x = itemRec.x - border;
            int y = itemRec.y - border;
            int width = itemRec.width + border * 2;
            int height = itemRec.height + border * 2;
            repaint(x, y, width, height);
        }
    }

    private void selectedIndex(int index) {
        if (index >= 0 && index < items.size()) {
            fireSelectionChanged(new ChangeEvent(this));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        prepareItemSize();
        Item selectedItem = null;
        Item focusedItem = null;
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item != null) {
                if (item.isSelected()) {
                    selectedItem = item;
                } else if (item.isHasFocus()) {
                    focusedItem = item;
                } else {
                    item.paint(g);
                }
            }
        }
        // paint selected and focused item
        if (selectedItem != null) {
            selectedItem.paint(g);
        }
        if (focusedItem != null) {
            focusedItem.paint(g);
        }
        super.paintComponent(g);
    }

    private void prepareItemSize() {
        Insets insets = getInsets();
        int x = insets.left;
        int y = insets.top;
        int width = getWidth() - (insets.left + insets.right);
        int gap = scale(itemPainter.getItemGap());
        Dimension itemSize = ColorPickerUtils.scale(itemPainter.getItemSize());
        int itemCount = colorData.size();
        int column = (width + gap) / (itemSize.width + gap);

        int targetWidth = (column * itemSize.width) + ((column - 1) * gap);
        x += (width - targetWidth) / 2;

        items.clear();
        for (int i = 0; i < itemCount; i++) {
            int row = i / column;
            int col = i % column;

            Color color = colorData.get(i);

            int lx = x + col * (itemSize.width + gap);
            int ly = y + row * (itemSize.height + gap);
            items.add(new Item(color, i, lx, ly, itemSize.width, itemSize.height));
        }
    }

    @Override
    public Dimension getPreferredSize() {
        int maxRow = itemPainter.getMaxRow();
        int gap = scale(itemPainter.getItemGap());
        Insets insets = getInsets();
        Dimension itemSize = ColorPickerUtils.scale(itemPainter.getItemSize());
        int height = (maxRow * itemSize.height) + ((maxRow - 1) * gap) + (insets.top + insets.bottom);
        return new Dimension(50, height);
    }

    public void fireSelectionChanged(ChangeEvent event) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                ((ChangeListener) listeners[i + 1]).stateChanged(event);
            }
        }
    }

    private class Item {

        public int getIndex() {
            return index;
        }

        public Color getColor() {
            return color;
        }

        public Rectangle getSize() {
            return size;
        }

        private final Color color;
        private final int index;
        private final Rectangle size;

        public Item(Color color, int index, Rectangle size) {
            this.color = color;
            this.index = index;
            this.size = size;
        }

        public Item(Color color, int index, int x, int y, int width, int height) {
            this(color, index, new Rectangle(x, y, width, height));
        }

        public void paint(Graphics g) {
            Shape clip = g.getClip();
            if (clip == null || clip.intersects(size)) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setClip(g.getClip());
                g2.translate(size.x, size.y);
                boolean isSelected = isSelected();
                boolean hasFocus = isHasFocus();
                itemPainter.paintItem(g2, color, size.width, size.height, isSelected, hasFocus);
                g2.dispose();
            }
        }

        public boolean isSelected() {
            return selectedIndex == index;
        }

        public boolean isHasFocus() {
            return hoverIndex == index;
        }
    }

    public int scale(int v) {
        return UIScale.scale(v);
    }
}
