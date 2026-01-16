package raven.color.utils;

import raven.color.component.ColorElement;

import java.awt.*;

public abstract class ColorPickerLayout implements LayoutManager {

    protected ColorElement colorComponent;
    protected ColorElement colorValue;
    protected ColorElement colorAlpha;
    protected ColorElement colorOtherComponent;
    protected ColorElement colorField;
    protected ColorElement colorPalette;

    public void setColorElement(ColorElement colorComponent, ColorElement colorValue, ColorElement colorAlpha, ColorElement colorOtherComponent, ColorElement colorField, ColorElement colorPalette) {
        this.colorComponent = colorComponent;
        this.colorValue = colorValue;
        this.colorAlpha = colorAlpha;
        this.colorOtherComponent = colorOtherComponent;
        this.colorField = colorField;
        this.colorPalette = colorPalette;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    public ColorElement getColorComponent() {
        return colorComponent;
    }

    public void setColorComponent(ColorElement colorComponent) {
        this.colorComponent = colorComponent;
    }

    public ColorElement getColorValue() {
        return colorValue;
    }

    public void setColorValue(ColorElement colorValue) {
        this.colorValue = colorValue;
    }

    public ColorElement getColorAlpha() {
        return colorAlpha;
    }

    public void setColorAlpha(ColorElement colorAlpha) {
        this.colorAlpha = colorAlpha;
    }

    public ColorElement getColorOtherComponent() {
        return colorOtherComponent;
    }

    public void setColorOtherComponent(ColorElement colorOtherComponent) {
        this.colorOtherComponent = colorOtherComponent;
    }

    public ColorElement getColorField() {
        return colorField;
    }

    public void setColorField(ColorElement colorField) {
        this.colorField = colorField;
    }

    public ColorElement getColorPalette() {
        return colorPalette;
    }

    public void setColorPalette(ColorElement colorPalette) {
        this.colorPalette = colorPalette;
    }
}
