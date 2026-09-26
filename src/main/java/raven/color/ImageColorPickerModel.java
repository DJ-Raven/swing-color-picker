package raven.color;

import raven.color.utils.AbstractColorPickerModel;
import raven.color.utils.ColorDimension;
import raven.color.utils.ColorLocation;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * Pick color from an image.
 * The image is scaled to fit the component and keeps its aspect ratio,
 * the selected color is read from the original image pixel.
 */
public class ImageColorPickerModel extends AbstractColorPickerModel {

    // max number of pixels to check when searching the nearest pixel of a color
    private static final int MAX_SEARCH_PIXELS = 256 * 256;

    private Icon icon;
    protected BufferedImage sourceImage;
    private int oldArc = -1;

    public ImageColorPickerModel() {
        this(null, Color.WHITE);
    }

    public ImageColorPickerModel(Icon icon) {
        this(icon, Color.WHITE);
    }

    public ImageColorPickerModel(Icon icon, Color color) {
        this.icon = icon;
        this.sourceImage = createSourceImage(icon);
        setSelectedColor(color);
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        if (this.icon != icon) {
            this.icon = icon;
            sourceImage = createSourceImage(icon);
            colorImage = null;
            // reselect the same color to update the selected location and repaint
            Color color = getSelectedColor();
            selectedColor = null;
            setSelectedColor(color);
        }
    }

    @Override
    public Image getValueImage(int width, int height, int arc) {
        return null;
    }

    @Override
    public BufferedImage getColorImage(int width, int height, int arc) {
        ColorDimension size = getDimensionImage(width, height);
        createColorImage(size.width, size.height, arc);
        return colorImage;
    }

    @Override
    public ColorDimension getDimensionImage(int width, int height) {
        ColorDimension size = super.getDimensionImage(width, height);
        if (sourceImage != null) {
            // fit the image and keep aspect ratio
            float scale = Math.min(width / (float) sourceImage.getWidth(), height / (float) sourceImage.getHeight());
            size.width = Math.max(1, Math.round(sourceImage.getWidth() * scale));
            size.height = Math.max(1, Math.round(sourceImage.getHeight() * scale));
        }
        return size;
    }

    @Override
    public Color locationToColor(ColorLocation location, float value) {
        if (sourceImage == null) {
            return getSelectedColor();
        }
        int w = sourceImage.getWidth();
        int h = sourceImage.getHeight();
        int x = Math.max(0, Math.min(w - 1, (int) (location.getX() * w)));
        int y = Math.max(0, Math.min(h - 1, (int) (location.getY() * h)));
        return new Color(sourceImage.getRGB(x, y));
    }

    @Override
    public ColorLocation colorToLocation(Color color) {
        if (sourceImage == null) {
            return new ColorLocation(0.5f, 0.5f);
        }
        int rgb = color.getRGB() & 0xFFFFFF;

        // keep the current location when it already has the same color
        ColorLocation location = getLocation();
        if ((locationToColor(location, 1f).getRGB() & 0xFFFFFF) == rgb) {
            return new ColorLocation(location);
        }

        // search the nearest pixel, skip pixels on large image
        int w = sourceImage.getWidth();
        int h = sourceImage.getHeight();
        int step = Math.max(1, (int) Math.ceil(Math.sqrt((double) w * h / MAX_SEARCH_PIXELS)));
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int nearestX = 0;
        int nearestY = 0;
        int min = Integer.MAX_VALUE;
        for (int y = 0; y < h && min > 0; y += step) {
            for (int x = 0; x < w; x += step) {
                int p = sourceImage.getRGB(x, y);
                int dr = ((p >> 16) & 0xFF) - red;
                int dg = ((p >> 8) & 0xFF) - green;
                int db = (p & 0xFF) - blue;
                int dist = dr * dr + dg * dg + db * db;
                if (dist < min) {
                    min = dist;
                    nearestX = x;
                    nearestY = y;
                    if (dist == 0) {
                        break;
                    }
                }
            }
        }
        // center of the pixel
        return new ColorLocation((nearestX + 0.5f) / w, (nearestY + 0.5f) / h);
    }

    @Override
    protected Color valueToColor(ColorLocation location, float value) {
        return locationToColor(location, value);
    }

    @Override
    protected float colorToValue(Color color) {
        return 1f;
    }

    @Override
    public boolean showValueComponent() {
        return false;
    }

    protected void createColorImage(int width, int height, int arc) {
        if (width <= 0 || height <= 0 || sourceImage == null) {
            colorImage = null;
            return;
        }
        if (colorImage == null || colorImage.getWidth() != width || colorImage.getHeight() != height || oldArc != arc) {
            colorImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = colorImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            int a = clampArc(width, height, arc);
            if (a > 0) {
                g2.fill(new RoundRectangle2D.Float(0, 0, width, height, a, a));
                g2.setComposite(AlphaComposite.SrcIn);
            }
            g2.drawImage(sourceImage, 0, 0, width, height, null);
            g2.dispose();
            oldArc = arc;
        }
    }

    protected BufferedImage createSourceImage(Icon icon) {
        if (icon == null || icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
            return null;
        }
        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        icon.paintIcon(null, g2, 0, 0);
        g2.dispose();
        return image;
    }
}
