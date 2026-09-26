# Swing Color Picker Documentation

A Java Swing color picker built on [FlatLaf](https://github.com/JFormDesigner/FlatLaf), with built-in and customizable
picker models, a color palette, and configurable UI elements.

## Contents

- [Getting Started](getting-started.md) - installation, basic usage and the color picker dialog
- [Color Picker Options](color-picker-options.md) - palette, pipette, alpha, preview and layout
- [Color Models](color-models.md) - all built-in models and their options
- [Custom Model](custom-model.md) - create your own color picker model

## Quick Example

``` java
ColorPicker colorPicker = new ColorPicker();
colorPicker.setModel(new CorelSquareColorPickerModel());
colorPicker.addColorChangedListener((color, event) -> {
    // color changed
});
```
