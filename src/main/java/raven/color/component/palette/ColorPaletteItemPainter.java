package raven.color.component.palette;

import java.awt.*;

public interface ColorPaletteItemPainter {

    Dimension getItemSize();

    int getMaxRow();

    int getItemGap();

    int getItemBorderSize();

    void paintItem(Graphics g, Color color, int width, int height, boolean isSelected, boolean hasFocus);
}
