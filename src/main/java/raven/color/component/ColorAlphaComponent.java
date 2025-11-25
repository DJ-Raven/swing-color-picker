package raven.color.component;

import com.formdev.flatlaf.util.HiDPIUtils;
import com.formdev.flatlaf.util.ScaledEmptyBorder;
import raven.color.ColorPicker;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class ColorAlphaComponent extends SliderColor {

    private final ColorPicker colorPicker;
    private BufferedImage image;

    public ColorAlphaComponent(ColorPicker colorPicker) {
        this.colorPicker = colorPicker;
        init();
    }

    private void init() {
        install();
    }

    @Override
    public void install() {
        super.install();
        setBorder(new ScaledEmptyBorder(5, 10, 5, 10));
    }

    @Override
    protected void valueChanged(float v) {
        Color color = colorPicker.getSelectionModel().getSelectedColor();
        if (color != null) {
            colorPicker.getSelectionModel().setSelectedColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (v * 255f)), false);
        }
    }

    @Override
    protected float getValue() {
        Color color = colorPicker.getSelectionModel().getSelectedColor();
        if (color == null) {
            return 1f;
        }
        return color.getAlpha() / 255f;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Insets insets = getInsets();
        int x = insets.left;
        int y = insets.top;
        int width = getWidth() - (insets.left + insets.right);
        int height = getHeight() - (insets.top + insets.bottom);

        HiDPIUtils.paintAtScale1x((Graphics2D) g, x, y, width, height, this::paintImpl);
        super.paintComponent(g);
    }

    private void paintImpl(Graphics2D g, int x, int y, int width, int height, double scaleFactor) {
        BufferedImage img = createTransparentImage(width, height);
        if (img != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawImage(img, x, y, null);
            g2.setPaint(new GradientPaint(x, y, new Color(255, 255, 255, 0), x + width, y, Color.getHSBColor(colorPicker.getSelectionModel().getHue(), 1f, 1f)));
            g2.fill(new RoundRectangle2D.Float(x, y, width, height, height, height));
            g2.dispose();
        }
    }

    private BufferedImage createTransparentImage(int width, int height) {
        if (image == null || image.getWidth() != width || image.getHeight() != height) {
            int row = 2;
            float size = height / (float) row;
            if (size <= 0) {
                return null;
            }
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = image.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.fill(new RoundRectangle2D.Float(0, 0, width, height, height, height));
            g2.setComposite(AlphaComposite.SrcIn.derive(0.5f));

            // draw transparent background
            int count = (int) Math.ceil(width / size);
            Color color1 = getBackground();
            Color color2 = Color.GRAY;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < count; j++) {
                    if ((i + j) % 2 == 0) {
                        g2.setColor(color1);
                    } else {
                        g2.setColor(color2);
                    }
                    g2.fill(new Rectangle2D.Float(j * size, size * i, size, size));
                }
            }
        }
        return image;
    }
}
