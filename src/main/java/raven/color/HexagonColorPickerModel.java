package raven.color;

import raven.color.component.LocationChangeEvent;
import raven.color.event.ColorChangeEvent;
import raven.color.utils.AbstractColorPickerModel;
import raven.color.utils.ColorDimension;
import raven.color.utils.ColorLocation;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

/**
 * Honeycomb color picker: a large hexagon made of small hexagon cells, with an optional gray row below.
 * White at the center, hue by angle and lightness decreasing toward the outer ring.
 * Selection always snaps to the nearest cell.
 */
public class HexagonColorPickerModel extends AbstractColorPickerModel {

    private static final float SQRT3 = (float) Math.sqrt(3);

    // number of rings around the center cell
    protected static final int RINGS = 6;

    // number of cells in the gray row, same width as the honeycomb
    protected static final int GRAY_COUNT = RINGS * 2 + 1;

    // space between the honeycomb and the gray row, in cell radius units
    protected static final float GRAY_GAP = 0.5f;

    // cell radius in normalized (0..1) location units, fit the content into the square image
    // width: (RINGS * 2 + 1) cells, each cell sqrt(3) * radius wide
    // height: honeycomb (RINGS * 3 + 2) + gap + gray row (2)
    protected static final float CELL_RADIUS = 1f / Math.max((RINGS * 2 + 1) * SQRT3, RINGS * 3 + 4 + GRAY_GAP);

    private boolean grayRowEnabled;
    protected Cell[] cells;

    public HexagonColorPickerModel() {
        this(Color.WHITE);
    }

    public HexagonColorPickerModel(Color color) {
        this(color, true);
    }

    public HexagonColorPickerModel(Color color, boolean grayRowEnabled) {
        this.grayRowEnabled = grayRowEnabled;
        this.cells = createCells();
        setSelectedColor(color);
    }

    public boolean isGrayRowEnabled() {
        return grayRowEnabled;
    }

    public void setGrayRowEnabled(boolean grayRowEnabled) {
        if (this.grayRowEnabled != grayRowEnabled) {
            this.grayRowEnabled = grayRowEnabled;
            cells = createCells();
            colorImage = null;
            setLocation(colorToLocation(getSelectedColor()));
            // selected color not changed, notify to update the selected location and repaint
            fireColorChanged(new ColorChangeEvent(this, false));
        }
    }

    @Override
    public Image getValueImage(int width, int height, int arc) {
        return null;
    }

    @Override
    public BufferedImage getColorImage(int width, int height, int arc) {
        ColorDimension size = getDimensionImage(width, height);
        createColorImage(size.width, size.height);
        return colorImage;
    }

    @Override
    public ColorDimension getDimensionImage(int width, int height) {
        ColorDimension size = super.getDimensionImage(width, height);
        int s = Math.min(size.width, size.height);
        size.width = s;
        size.height = s;
        return size;
    }

    @Override
    public void locationValue(ColorLocation loc, LocationChangeEvent event) {
        Cell cell = locationToCell(loc.getX(), loc.getY());
        loc.set(cell.x, cell.y);
        super.locationValue(loc, event);
    }

    @Override
    public Color locationToColor(ColorLocation location, float value) {
        return locationToCell(location.getX(), location.getY()).color;
    }

    @Override
    public ColorLocation colorToLocation(Color color) {
        // nearest cell by color
        Cell nearest = cells[0];
        int min = Integer.MAX_VALUE;
        for (Cell cell : cells) {
            int dr = cell.color.getRed() - color.getRed();
            int dg = cell.color.getGreen() - color.getGreen();
            int db = cell.color.getBlue() - color.getBlue();
            int dist = dr * dr + dg * dg + db * db;
            if (dist < min) {
                min = dist;
                nearest = cell;
            }
        }
        return new ColorLocation(nearest.x, nearest.y);
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

    @Override
    public boolean paintSelection(Graphics2D g2, int x, int y, Rectangle bounds) {
        float radius = CELL_RADIUS * Math.min(bounds.width, bounds.height);
        Shape shape = createHexagon(x, y, radius);
        Stroke oldStroke = g2.getStroke();

        // dark edge, keep the outline visible on light cells
        g2.setColor(new Color(0, 0, 0, 110));
        g2.setStroke(new BasicStroke(scale(3.5f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(shape);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(scale(2f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(shape);

        g2.setStroke(oldStroke);
        return true;
    }

    protected void createColorImage(int width, int height) {
        if (width <= 0 || height <= 0) {
            return;
        }
        if (colorImage == null || colorImage.getWidth() != width || colorImage.getHeight() != height) {
            colorImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = colorImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            float size = Math.min(width, height);
            // small gap between cells
            float radius = CELL_RADIUS * size * 0.96f;
            for (Cell cell : cells) {
                g2.setColor(cell.color);
                g2.fill(createHexagon(cell.x * size, cell.y * size, radius));
            }
            g2.dispose();
        }
    }

    protected Shape createHexagon(float cx, float cy, float radius) {
        Path2D path = new Path2D.Float();
        for (int i = 0; i < 6; i++) {
            // pointy-top hexagon
            double angle = Math.toRadians(60 * i - 90);
            float x = (float) (cx + radius * Math.cos(angle));
            float y = (float) (cy + radius * Math.sin(angle));
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.closePath();
        return path;
    }

    /**
     * Find the cell at location, or the nearest cell when the location is outside the cells
     */
    protected Cell locationToCell(float x, float y) {
        Cell nearest = cells[0];
        float min = Float.MAX_VALUE;
        for (Cell cell : cells) {
            float dx = cell.x - x;
            float dy = cell.y - y;
            float dist = dx * dx + dy * dy;
            if (dist < min) {
                min = dist;
                nearest = cell;
            }
        }
        return nearest;
    }

    /**
     * Vertical center of the honeycomb, moved up to make space for the gray row when enabled
     */
    protected float getCenterY() {
        if (!grayRowEnabled) {
            return 0.5f;
        }
        return 0.5f - (RINGS * 3 + 4 + GRAY_GAP) * CELL_RADIUS / 2f + (RINGS * 1.5f + 1f) * CELL_RADIUS;
    }

    protected Cell[] createCells() {
        int honeycombCount = 1 + 3 * RINGS * (RINGS + 1);
        Cell[] cells = new Cell[honeycombCount + (grayRowEnabled ? GRAY_COUNT : 0)];
        float centerY = getCenterY();

        // center cell first, so white prefers the center when matching colors
        cells[0] = new Cell(0.5f, centerY, Color.WHITE);
        int index = 1;
        for (int r = -RINGS; r <= RINGS; r++) {
            for (int q = -RINGS; q <= RINGS; q++) {
                int s = -q - r;
                int ring = Math.max(Math.abs(q), Math.max(Math.abs(r), Math.abs(s)));
                if (ring == 0 || ring > RINGS) {
                    continue;
                }
                float dx = SQRT3 * (q + r / 2f) * CELL_RADIUS;
                float dy = 1.5f * r * CELL_RADIUS;
                cells[index++] = new Cell(0.5f + dx, centerY + dy, createCellColor(dx, dy, ring));
            }
        }
        if (!grayRowEnabled) {
            return cells;
        }

        // gray row from white to black
        float grayY = centerY + (RINGS * 1.5f + 2f + GRAY_GAP) * CELL_RADIUS;
        float cellWidth = SQRT3 * CELL_RADIUS;
        for (int i = 0; i < GRAY_COUNT; i++) {
            float x = 0.5f + (i - (GRAY_COUNT - 1) / 2f) * cellWidth;
            int v = Math.round(255f * (1f - i / (float) (GRAY_COUNT - 1)));
            cells[index++] = new Cell(x, grayY, new Color(v, v, v));
        }
        return cells;
    }

    private static Color createCellColor(float dx, float dy, int ring) {
        // screen angle: 0 = right, 90 = bottom, 180 = left, 270 = top
        float angle = (float) Math.toDegrees(Math.atan2(dy, dx));
        if (angle < 0) {
            angle += 360f;
        }
        // red at the bottom, yellow and green on the left, blue at the top, magenta on the right
        float t = ((angle - 90f + 360f) % 360f) / 360f;
        float hue = t < 0.5f ? t * 4f / 3f : 2f / 3f + (t - 0.5f) * 2f / 3f;
        float lightness = 1f - ring * 0.13f;
        return hslToColor(hue, 1f, lightness);
    }

    private static Color hslToColor(float h, float s, float l) {
        float q = l < 0.5f ? l * (1f + s) : l + s - l * s;
        float p = 2f * l - q;
        float r = hueToRgb(p, q, h + 1f / 3f);
        float g = hueToRgb(p, q, h);
        float b = hueToRgb(p, q, h - 1f / 3f);
        return new Color(Math.round(r * 255), Math.round(g * 255), Math.round(b * 255));
    }

    private static float hueToRgb(float p, float q, float t) {
        if (t < 0f) t += 1f;
        if (t > 1f) t -= 1f;
        if (t < 1f / 6f) return p + (q - p) * 6f * t;
        if (t < 1f / 2f) return q;
        if (t < 2f / 3f) return p + (q - p) * (2f / 3f - t) * 6f;
        return p;
    }

    protected static class Cell {

        protected final float x;
        protected final float y;
        protected final Color color;

        protected Cell(float x, float y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }
}
