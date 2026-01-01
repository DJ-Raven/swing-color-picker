package raven.color.component.piptte;

import raven.color.ColorPicker;
import raven.color.component.palette.DefaultColorPaletteData;
import raven.color.component.palette.DefaultColorPaletteItemPainter;
import raven.color.component.palette.MaterialColorPaletteData;
import raven.color.component.palette.TailwindColorPaletteData;

public enum ColorPaletteType {
    DEFAULT, TAILWIND, MATERIAL;

    public void apply(ColorPicker colorPicker) {
        if (this == DEFAULT) {
            colorPicker.getColorPalette().setColorData(new DefaultColorPaletteData());
            colorPicker.getColorPalette().setItemPainter(new DefaultColorPaletteItemPainter());
        } else if (this == TAILWIND) {
            TailwindColorPaletteData colorPaletteData = new TailwindColorPaletteData();
            colorPicker.getColorPalette().setColorData(colorPaletteData);
            colorPicker.getColorPalette().setItemPainter(colorPaletteData.getPainter());
        } else if (this == MATERIAL) {
            MaterialColorPaletteData colorPaletteData = new MaterialColorPaletteData();
            colorPicker.getColorPalette().setColorData(colorPaletteData);
            colorPicker.getColorPalette().setItemPainter(colorPaletteData.getPainter());
        }
    }
}
