# Color Models

A color model decides how the color area looks and how a location in it becomes a color.
Change the model at any time, the selected color is kept.

``` java
// create color picker with an initial model
ColorPicker colorPicker = new ColorPicker(new DiskColorPickerModel());

// change to a different model
colorPicker.setModel(new CorelTriangleColorPickerModel());
```

All models have a constructor with an initial color

``` java
new CorelSquareColorPickerModel(Color.RED);
```

## Available Models

| Model                           | Color area                                     | Value component        |
|---------------------------------|------------------------------------------------|------------------------|
| `DinoColorPickerModel`          | Saturation and brightness square (default)     | Hue                    |
| `DiskColorPickerModel`          | Color wheel, hue by angle, saturation by range | Brightness             |
| `CorelSquareColorPickerModel`   | Square selector with circular hue wheel        | Hidden, use hue wheel  |
| `CorelRhombusColorPickerModel`  | Rhombus selector with circular hue wheel       | Hidden, use hue wheel  |
| `CorelTriangleColorPickerModel` | Triangle selector with circular hue wheel      | Hidden, use hue wheel  |
| `CorelCircleColorPickerModel`   | Circle selector with circular hue wheel        | Hidden, use hue wheel  |
| `HexagonColorPickerModel`       | Honeycomb of hexagon color cells               | Brightness             |
| `ImageColorPickerModel`         | Image                                          | Hidden                 |

The value component is the slider under the color area.

## Corel Models

`CorelSquareColorPickerModel`, `CorelRhombusColorPickerModel`, `CorelTriangleColorPickerModel` and
`CorelCircleColorPickerModel` show a hue wheel around a selector. Drag on the wheel to change the hue, and drag in the
selector to change saturation and brightness.

- `CorelCircleColorPickerModel` maps the full saturation and brightness square into the circle, so every color is still
  reachable, including white and the pure hue.
- `CorelTriangleColorPickerModel` rotates the triangle with the hue, the pure hue corner always points at the hue handle.

## HexagonColorPickerModel

A honeycomb of hexagon color cells, like the color picker in Microsoft Office. White is at the center, the hue changes
by angle and colors get darker toward the outer ring. Selection always snaps to a cell and the selected cell is marked
with a white hexagon outline.

| Option           | Default | Description                                             |
|------------------|---------|---------------------------------------------------------|
| `grayRowEnabled` | `true`  | Show a row of gray cells from white to black            |

``` java
// gray row enabled by default
HexagonColorPickerModel model = new HexagonColorPickerModel();

// create with initial color and without the gray row
HexagonColorPickerModel model = new HexagonColorPickerModel(Color.WHITE, false);

// enable or disable the gray row at runtime
model.setGrayRowEnabled(true);
```

- The value component changes the brightness of all cells.
- When a color is set with `setSelectedColor()`, the model selects the cell and brightness that best match the color.
  If the color is not in the honeycomb, the nearest cell is marked and the color is kept until a cell is clicked.

## ImageColorPickerModel

Pick a color from an image. The image is scaled to fit and keeps its aspect ratio, the selected color is read from the
original image pixel. Any `Icon` is supported, such as `ImageIcon` or `FlatSVGIcon`.

``` java
// create with an image
ImageColorPickerModel model = new ImageColorPickerModel(new ImageIcon(image));
colorPicker.setModel(model);

// change the image at runtime
model.setIcon(new ImageIcon(ImageIO.read(file)));
```

- When a color is set with `setSelectedColor()`, the selection moves to the nearest matching pixel in the image.
  On a large image only part of the pixels are checked, so the match is fast but may not be exact.
- Picked colors are always opaque, a transparent pixel returns its stored color.
- Without an image, the color area is empty.

## Next

- [Custom Model](custom-model.md)
