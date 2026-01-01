package raven.color.component.palette;

import raven.color.utils.ColorPickerUtils;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

public class DefaultColorPaletteItemPainter implements ColorPaletteItemPainter {

    private final Dimension itemSize = new Dimension(20, 20);

    @Override
    public Dimension getItemSize() {
        return itemSize;
    }

    @Override
    public int getMaxRow() {
        return 2;
    }

    @Override
    public int getItemGap() {
        return 10;
    }

    @Override
    public int getItemBorderSize() {
        return 3;
    }

    protected float getArc() {
        return 8;
    }

    protected Color getBorderColor(Color color) {
        return null;
    }

    @Override
    public void paintItem(Graphics g, Color color, int width, int height, boolean isSelected, boolean hasFocus) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        float arc = Math.min(ColorPickerUtils.scale(getArc()), Math.min(width, height));
        g2.fill(new RoundRectangle2D.Float(0, 0, width, height, arc, arc));

        int border = ColorPickerUtils.scale(getItemBorderSize());
        if (border > 0) {
            g2.translate(-border, -border);
            paintItemBorder(g2, color, width + border * 2, height + border * 2, isSelected, hasFocus);
        }
    }

    protected void paintItemBorder(Graphics2D g2, Color color, int width, int height, boolean isSelected, boolean hasFocus) {
        if (!(hasFocus)) {
            return;
        }

        Color borderColor = getBorderColor(color);
        if (borderColor != null) {
            g2.setColor(borderColor);
        }

        g2.setComposite(AlphaComposite.SrcOver.derive(0.35f));
        int border = ColorPickerUtils.scale(getItemBorderSize());
        float arc = ColorPickerUtils.scale(getArc() + getItemBorderSize() * 2);
        arc = Math.min(arc, Math.min(width, height));
        Area area = new Area(new RoundRectangle2D.Float(0, 0, width, height, arc, arc));
        int inWidth = width - border * 2;
        int inHeight = height - border * 2;
        float inArc = Math.min(ColorPickerUtils.scale(getArc()), Math.min(inWidth, inHeight));

        area.subtract(new Area(new RoundRectangle2D.Float(border, border, inWidth, inHeight, inArc, inArc)));
        g2.fill(area);
    }
}
