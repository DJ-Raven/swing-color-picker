package raven.color.utils;

import raven.color.component.LocationChangeEvent;
import raven.color.event.ColorChangeEvent;
import raven.color.event.ColorChangedListener;

import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public abstract class AbstractColorPickerModel implements ColorPickerModel {

    protected EventListenerList listenerList = new EventListenerList();

    protected BufferedImage valueImage;
    protected BufferedImage colorImage;
    protected Color selectedColor;

    private final ColorLocation location = new ColorLocation(0f, 0f);
    private float value = 1f;

    protected float oldValue = -1f;

    protected abstract Color valueToColor(ColorLocation location, float value);

    protected abstract float colorToValue(Color color);

    @Override
    public Color getSelectedColor() {
        if (selectedColor == null) {
            return Color.WHITE;
        }
        return selectedColor;
    }

    @Override
    public void setSelectedColor(Color selectedColor) {
        setSelectedColor(selectedColor, true);
    }

    @Override
    public void setSelectedColor(Color selectedColor, boolean valueChange) {
        if (selectedColor == null) {
            selectedColor = Color.WHITE;
        }
        if (notifyColor(this.selectedColor, selectedColor)) {
            boolean valueChanged = false;
            if (valueChange) {
                this.value = colorToValue(selectedColor);
                this.location.set(colorToLocation(selectedColor));
            } else {
                // check is only alpha changed
                if (isAlphaChangedOnly(this.selectedColor, selectedColor)) {
                    valueChanged = true;
                }
            }
            this.selectedColor = selectedColor;
            fireColorChanged(new ColorChangeEvent(this, valueChanged));
        }
    }

    public boolean notifyColor(Color oldColor, Color newColor) {
        return !Objects.equals(oldColor, newColor);
    }

    @Override
    public void locationValue(ColorLocation location, LocationChangeEvent event) {
        this.location.set(location);
    }

    public ColorLocation getLocation() {
        return location;
    }

    public void setLocation(ColorLocation location) {
        this.location.set(location);
    }

    @Override
    public float getValue() {
        return value;
    }

    @Override
    public void setValue(float value) {
        value = clamp(value);
        if (this.value != value) {
            this.value = value;
            Color oldColor = getSelectedColor();
            if (oldColor != null) {
                Color color = valueToColor(location, value);
                int alpha = oldColor.getAlpha();
                selectedColor = color;
                if (alpha != 255) {
                    selectedColor = new Color(selectedColor.getRed(), selectedColor.getGreen(), selectedColor.getBlue(), alpha);
                }
            }
            fireColorChanged(new ColorChangeEvent(this, true));
        }
    }

    @Override
    public ColorDimension getDimensionImage(int width, int height) {
        return new ColorDimension(width, height);
    }

    @Override
    public void addChangeListener(ColorChangedListener listener) {
        listenerList.add(ColorChangedListener.class, listener);
    }

    @Override
    public void removeChangeListener(ColorChangedListener listener) {
        listenerList.remove(ColorChangedListener.class, listener);
    }

    public void fireColorChanged(ColorChangeEvent event) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ColorChangedListener.class) {
                ((ColorChangedListener) listeners[i + 1]).colorChanged(getSelectedColor(), event);
            }
        }
    }

    protected boolean isAlphaChangedOnly(Color color1, Color color2) {
        if (color1 == null || color2 == null) {
            return false;
        }

        int r1 = color1.getRed();
        int g1 = color1.getGreen();
        int b1 = color1.getBlue();
        int a1 = color1.getAlpha();

        int r2 = color2.getRed();
        int g2 = color2.getGreen();
        int b2 = color2.getBlue();
        int a2 = color2.getAlpha();
        return r1 == r2 && g1 == g2 && b1 == b2 && a1 != a2;
    }

    protected int clampArc(int width, int height, int arc) {
        if (arc == 0) {
            return arc;
        }
        int maxArc = Math.min(width, height);
        if (arc > maxArc || arc == 999) {
            arc = maxArc;
        }
        return arc;
    }

    protected float clamp(float value) {
        return Math.max(0f, Math.min(1f, value));
    }

    protected float scale(float v) {
        return ColorPickerUtils.scale(v);
    }
}
