# Custom Model

Create your own model by implementing `ColorPickerModel`. The easiest way is to extend `AbstractColorPickerModel`, it
handles the selected color, value, location and listeners. You can also extend one of the built-in models and override
only what you need.

## How a Model Works

A model has two inputs:

- **Location**: the point selected in the color area, `x` and `y` from `0` to `1` (top left is `0, 0`).
- **Value**: the value of the value component (the slider under the color area), from `0` to `1`.
  Each model decides what it means, for example hue in `DinoColorPickerModel` or brightness in `DiskColorPickerModel`.

The model converts between them and a color, and paints the images of the color area and the value component.

## Methods

| Method                                    | Description                                                                         |
|-------------------------------------------|-------------------------------------------------------------------------------------|
| `getColorImage(width, height, arc)`       | Image of the color area. `arc` is the corner radius                                 |
| `getValueImage(width, height, arc)`       | Image of the value component, return `null` when not used                          |
| `getDimensionImage(width, height)`        | Size of the color image in the available size. Override to keep a square or ratio   |
| `locationToColor(location, value)`        | Color at the location with the value                                                |
| `colorToLocation(color)`                  | Location of the color                                                               |
| `colorToValue(color)`                     | Value of the color                                                                  |
| `valueToColor(location, value)`           | Color when the value changes, usually the same as `locationToColor()`               |
| `locationValue(location, event)`          | Called when the user presses or drags. Change `location` to snap or clamp it, or call `event.consume()` to handle it yourself |
| `showValueComponent()`                    | Show the value component, default `true`                                            |
| `notifySelectedLocationOnValueChanged()`  | Move the selected location when the value changes, default `false`                  |
| `paintSelection(g2, x, y, bounds)`        | Paint your own selection marker and return `true`, default `false` paints the round marker |

`AbstractColorPickerModel` has fields to cache images: `colorImage`, `valueImage` and `oldValue`.

## Example

A model where `x` is the hue and `y` is the brightness, and the value component is the saturation

``` java
public class HueBrightnessColorPickerModel extends AbstractColorPickerModel {

    public HueBrightnessColorPickerModel() {
        this(Color.RED);
    }

    public HueBrightnessColorPickerModel(Color color) {
        setSelectedColor(color);
    }

    @Override
    public Image getColorImage(int width, int height, int arc) {
        if (width <= 0 || height <= 0) {
            return null;
        }
        // create the image only when the size or value changed
        if (colorImage == null || colorImage.getWidth() != width || colorImage.getHeight() != height || oldValue != getValue()) {
            colorImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    float hue = (x + 0.5f) / width;
                    float brightness = 1f - (y + 0.5f) / height;
                    colorImage.setRGB(x, y, Color.HSBtoRGB(hue, getValue(), brightness));
                }
            }
            oldValue = getValue();
        }
        return colorImage;
    }

    @Override
    public Image getValueImage(int width, int height, int arc) {
        if (width <= 0 || height <= 0) {
            return null;
        }
        // gray to the selected hue
        Color color = locationToColor(getLocation(), 1f);
        valueImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = valueImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(new GradientPaint(0, 0, locationToColor(getLocation(), 0f), width, 0, color));
        arc = clampArc(width, height, arc);
        g2.fill(new RoundRectangle2D.Float(0, 0, width, height, arc, arc));
        g2.dispose();
        return valueImage;
    }

    @Override
    public Color locationToColor(ColorLocation location, float value) {
        return Color.getHSBColor(location.getX(), value, 1f - location.getY());
    }

    @Override
    public ColorLocation colorToLocation(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        return new ColorLocation(hsb[0], 1f - hsb[2]);
    }

    @Override
    protected Color valueToColor(ColorLocation location, float value) {
        return locationToColor(location, value);
    }

    @Override
    protected float colorToValue(Color color) {
        return Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[1];
    }
}
```

``` java
colorPicker.setModel(new HueBrightnessColorPickerModel());
```

## Custom Selection Marker

Override `paintSelection()` to paint your own marker. `x` and `y` are the center of the selected location in the
component, and `bounds` is the area of the color image. Return `true` when painted, or `false` to paint the default
round marker.

``` java
public class MyColorPickerModel extends DinoColorPickerModel {

    @Override
    public boolean paintSelection(Graphics2D g2, int x, int y, Rectangle bounds) {
        g2.setColor(Color.WHITE);
        g2.drawRect(x - 5, y - 5, 10, 10);
        return true;
    }
}
```

## Tips

- Cache images and create them again only when the size or value changes, painting happens often.
- Use `locationValue()` to snap the location, like `HexagonColorPickerModel` snaps to the nearest cell.
- Keep `locationToColor()` and `colorToLocation()` consistent, so setting a color moves the selection to the right place.
