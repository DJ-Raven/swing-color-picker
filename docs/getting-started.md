# Getting Started

## Installation

Add the dependency from [Maven Central](https://central.sonatype.com/artifact/io.github.dj-raven/swing-color-picker)

``` xml
<dependency>
    <groupId>io.github.dj-raven</groupId>
    <artifactId>swing-color-picker</artifactId>
    <version>2.0.0</version>
</dependency>
```

The library uses [FlatLaf](https://github.com/JFormDesigner/FlatLaf), so set up a FlatLaf look and feel before creating
the color picker

``` java
FlatLightLaf.setup();
```

## Create a Color Picker

`ColorPicker` is a `JPanel`, add it to any container

``` java
// default model with white color
ColorPicker colorPicker = new ColorPicker();

// with an initial color
ColorPicker colorPicker = new ColorPicker(Color.RED);

// with an initial model
ColorPicker colorPicker = new ColorPicker(new DiskColorPickerModel(Color.RED));

panel.add(colorPicker);
```

## Get and Set the Color

``` java
Color color = colorPicker.getSelectedColor();

colorPicker.setSelectedColor(new Color(90, 60, 200));
```

## Listen to Color Changes

``` java
colorPicker.addColorChangedListener((color, event) -> {
    // color changed
    System.out.println(color);
});
```

`event.isValueChanged()` returns `true` when the change comes from the value component (the slider under the color
area) or when only the alpha is changed.

## Show with Dialog

Show the color picker in a dialog with `OK` and `Cancel` buttons. Returns the selected color, or `null` when canceled.

``` java
Color color = ColorPicker.showDialog(this, "Pick Color", Color.WHITE);

if (color != null) {
    // color selected
}
```

Use your own `ColorPicker` to customize the dialog

``` java
ColorPicker colorPicker = new ColorPicker(new HexagonColorPickerModel());
colorPicker.setColorAlphaEnabled(false);

Color color = ColorPicker.showDialog(this, "Pick Color", colorPicker);
```

## Next

- [Color Picker Options](color-picker-options.md)
- [Color Models](color-models.md)
