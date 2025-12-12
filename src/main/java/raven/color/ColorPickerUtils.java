package raven.color;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class ColorPickerUtils {

    public static Shape createShape(float size, float border, float margin) {
        Area area = new Area(new Ellipse2D.Float(margin, margin, size - margin * 2f, size - margin * 2f));
        float borderSize = (size / 2f * border);
        float centerSize = (size - borderSize * 2f) + margin * 2f;
        area.subtract(new Area(new Ellipse2D.Float(borderSize - margin, borderSize - margin, centerSize, centerSize)));
        return area;
    }

    public static BufferedImage createTransparentImage(Color color, int size, int width, int height, int round) {
        int row = (int) Math.ceil((double) height / size);
        if (row <= 0) {
            return null;
        }
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fill(new RoundRectangle2D.Float(0, 0, width, height, round, round));
        if (round > 0) {
            g2.setComposite(AlphaComposite.SrcIn.derive(0.4f));
        } else {
            g2.setComposite(AlphaComposite.SrcOver.derive(0.4f));
        }

        // draw transparent background
        int column = (int) Math.ceil((double) width / size);
        Color gray = Color.GRAY;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if ((i + j) % 2 == 0) {
                    g2.setColor(color);
                } else {
                    g2.setColor(gray);
                }
                g2.fill(new Rectangle2D.Float(j * size, size * i, size, size));
            }
        }
        return image;
    }
}
