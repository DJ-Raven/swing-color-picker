package raven.color.utils;

import raven.color.component.LocationChangeEvent;
import raven.color.event.ColorChangedListener;

import java.awt.*;

public interface ColorPickerModel {

    Image getValueImage(int width, int height, int arc);

    Image getColorImage(int width, int height, int arc);

    ColorDimension getDimensionImage(int width, int height);

    Color getSelectedColor();

    void setSelectedColor(Color color);

    void setSelectedColor(Color color, boolean valueChange);

    float getValue();

    void setValue(float value);

    void locationValue(ColorLocation location, LocationChangeEvent event);

    Color locationToColor(ColorLocation location, float value);

    ColorLocation colorToLocation(Color color);

    boolean notifySelectedLocationOnValueChanged();

    boolean showValueComponent();

    /**
     * Paint the marker of the selected location.
     *
     * @param x      marker center x
     * @param y      marker center y
     * @param bounds bounds of the color image
     * @return {@code false} to paint the default marker
     */
    default boolean paintSelection(Graphics2D g2, int x, int y, Rectangle bounds) {
        return false;
    }

    void addChangeListener(ColorChangedListener listener);

    void removeChangeListener(ColorChangedListener listener);
}
