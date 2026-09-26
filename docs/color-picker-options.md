# Color Picker Options

All options can be changed at runtime.

| Option                      | Default | Description                                         |
|-----------------------------|---------|-----------------------------------------------------|
| `colorPaletteEnabled`       | `true`  | Show the color palette                              |
| `colorPipettePickerEnabled` | `true`  | Show the pipette button to pick a color from screen |
| `colorAlphaEnabled`         | `true`  | Show the alpha slider and alpha in the color field  |
| `colorPreviewEnabled`       | `true`  | Show the selected color preview                     |

``` java
colorPicker.setColorPaletteEnabled(false);
colorPicker.setColorPipettePickerEnabled(false);
colorPicker.setColorAlphaEnabled(false);
colorPicker.setColorPreviewEnabled(false);
```

## Color Palette

Choose one of the built-in palettes

``` java
colorPicker.applyColorPaletteType(ColorPaletteType.DEFAULT);
colorPicker.applyColorPaletteType(ColorPaletteType.TAILWIND);
colorPicker.applyColorPaletteType(ColorPaletteType.MATERIAL);
```

Or use your own colors with `ColorPaletteData`, and your own item painter with `ColorPaletteItemPainter`

``` java
RecentColorPaletteData recent = new RecentColorPaletteData();
recent.add(Color.RED);
recent.add(Color.BLUE);

colorPicker.getColorPalette().setColorData(recent);
colorPicker.getColorPalette().setItemPainter(new DefaultColorPaletteItemPainter());
```

## Pipette

The pipette button picks a color from anywhere on the screen. It is only shown when screen capture is available on the
system.

## Alpha

When alpha is disabled, a selected color with alpha is changed to the same color without alpha.

## Layout

`ColorPicker` uses a `ColorPickerLayout` to place its elements. The default is `DefaultColorPickerLayout`.
To use your own layout, extend `ColorPickerLayout`

``` java
colorPicker.setColorPickerLayout(new MyColorPickerLayout());
```

`ColorPicker.setLayout()` only accepts a `ColorPickerLayout`.

## Style

`ColorPicker` supports FlatLaf style properties

``` java
colorPicker.putClientProperty(FlatClientProperties.STYLE, "" +
        "border:10,10,10,10,$Component.borderColor,1,15;" +
        "[light]background:#E6E6E6;" +
        "[dark]background:#363636;");
```
