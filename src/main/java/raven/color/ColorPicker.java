package raven.color;

import net.miginfocom.swing.MigLayout;
import raven.color.component.*;
import raven.color.component.utils.DefaultColorPaletteData;
import raven.color.component.utils.DefaultColorPaletteItemPainter;
import raven.color.event.ColorChangeEvent;
import raven.color.event.ColorChangedListener;

import javax.swing.*;
import java.awt.*;

public class ColorPicker extends JPanel implements ColorChangedListener {

    private ColorPickerModel model;
    private ColorComponent colorComponent;
    private ColorValueComponent colorValueComponent;
    private ColorAlphaComponent colorAlphaComponent;
    private ColorPreview colorPreview;
    private ColorField colorField;
    private ColorPaletteComponent colorPalette;

    private boolean colorPaletteEnabled = true;

    public ColorPicker() {
        this(new DinoColorPickerModel());
    }

    public ColorPicker(Color initialColor) {
        init(new DinoColorPickerModel(), initialColor);
    }

    public ColorPicker(ColorPickerModel model) {
        init(model, model.getSelectedColor());
    }

    private void init(ColorPickerModel model, Color initialColor) {
        setLayout(new MigLayout("wrap,fillx,gap 0,insets 0 0 5 0", "[fill,280]"));

        colorComponent = new ColorComponent(this);
        colorValueComponent = new ColorValueComponent(this);
        colorAlphaComponent = new ColorAlphaComponent(this);
        colorField = new ColorField(this);

        JPanel panel = new JPanel(new MigLayout("wrap 2,fillx,insets 0,gap 3", "7[grow 0,fill][fill]"));

        panel.setOpaque(false);
        add(colorComponent, "height 50:180:");
        panel.add(createLeftComponent(), "span 1 2");
        panel.add(colorValueComponent, "height 20!");
        panel.add(colorAlphaComponent, "height 20!");
        add(panel);

        add(colorField);
        if (isColorPaletteEnabled()) {
            add(createColorPalette());
        }
        model.setSelectedColor(initialColor);

        setModel(model);
    }

    private Component createLeftComponent() {
        JPanel panel = new JPanel(new MigLayout("insets 3"));
        panel.setOpaque(false);

        colorPreview = new ColorPreview();

        panel.add(colorPreview, "width 33,height 33");

        return panel;
    }

    private Component createColorPalette() {
        if (colorPalette == null) {
            colorPalette = new ColorPaletteComponent(new DefaultColorPaletteData(), new DefaultColorPaletteItemPainter());
            colorPalette.addChangeListener(e -> {
                Color color = colorPalette.getColorAt(colorPalette.getSelectedIndex());
                if (color != null) {
                    getModel().setSelectedColor(color);
                }
            });
        }
        return colorPalette;
    }

    private void updateColorComponent() {
        if (colorField != null) {
            colorField.colorChanged(model.getSelectedColor());
        }
        if (colorPreview != null) {
            colorPreview.setColor(model.getSelectedColor());
        }
        colorComponent.changeSelectedPoint(model.getSelectedColor());
    }

    public ColorPickerModel getModel() {
        return model;
    }

    public void setModel(ColorPickerModel model) {
        if (model == null) {
            throw new IllegalArgumentException("color model can't be null");
        }
        if (this.model != model) {
            ColorPickerModel old = this.model;
            if (old != null) {
                old.removeChangeListener(this);
            }
            this.model = model;
            this.model.addChangeListener(this);

            updateColorComponent();
            repaint();
        }
    }

    public Color getSelectedColor() {
        return getModel().getSelectedColor();
    }

    public void setSelectedColor(Color color) {
        getModel().setSelectedColor(color);
    }

    public boolean isColorPaletteEnabled() {
        return colorPaletteEnabled;
    }

    public void setColorPaletteEnabled(boolean enabled) {
        if (this.colorPaletteEnabled != enabled) {
            this.colorPaletteEnabled = enabled;
            if (!enabled) {
                if (colorPalette != null) {
                    remove(colorPalette);
                    revalidate();
                }
            } else {
                add(createColorPalette());
                revalidate();
            }
        }
    }

    public ColorPaletteComponent getColorPalette() {
        return colorPalette;
    }

    public void applyColorPaletteType(ColorPaletteType type) {
        if (type != null) {
            type.apply(this);
        }
    }

    public void addColorChangedListener(ColorChangedListener listener) {
        listenerList.add(ColorChangedListener.class, listener);
    }

    public void removeColorChangedListener(ColorChangedListener listener) {
        listenerList.remove(ColorChangedListener.class, listener);
    }

    public static Color showDialog(Component component, String title, Color initialColor) {
        ColorPicker colorPicker = new ColorPicker(initialColor != null ? initialColor : Color.WHITE);
        return showDialog(component, title, colorPicker);
    }

    public static Color showDialog(Component component, String title, ColorPicker colorPicker) {
        int option = JOptionPane.showConfirmDialog(component, colorPicker,
                title,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            return colorPicker.getSelectedColor();
        }
        return null;
    }

    @Override
    public void colorChanged(Color color, ColorChangeEvent event) {
        if (colorComponent != null) {
            if (colorComponent.isNotifyRepaint()) {
                if (!event.isValueChanged()) {
                    // selected color invoked
                    // so coverts color to selected point
                    colorComponent.changeSelectedPoint(color);
                }
                colorComponent.repaint();
            }
        }
        if (colorValueComponent != null) {
            colorValueComponent.repaint();
        }
        if (colorAlphaComponent != null) {
            colorAlphaComponent.repaint();
        }
        if (colorPreview != null) {
            colorPreview.setColor(color);
        }
        if (colorField != null) {
            colorField.colorChanged(color);
        }
        fireColorChanged(event);
    }

    public void fireColorChanged(ColorChangeEvent event) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ColorChangedListener.class) {
                ((ColorChangedListener) listeners[i + 1]).colorChanged(getSelectedColor(), event);
            }
        }
    }
}
