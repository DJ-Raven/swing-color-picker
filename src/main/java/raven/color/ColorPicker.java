package raven.color;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.SystemInfo;
import raven.color.component.*;
import raven.color.component.palette.DefaultColorPaletteData;
import raven.color.component.palette.DefaultColorPaletteItemPainter;
import raven.color.component.piptte.ColorPaletteType;
import raven.color.component.piptte.ColorPipette;
import raven.color.component.piptte.DefaultColorPipette;
import raven.color.event.ColorChangeEvent;
import raven.color.event.ColorChangedListener;
import raven.color.utils.ColorPickerLayout;
import raven.color.utils.ColorPickerModel;
import raven.color.utils.DefaultColorPickerLayout;
import raven.color.utils.OtherComponentLayout;

import javax.swing.*;
import java.awt.*;

public class ColorPicker extends JPanel implements ColorChangedListener {

    private ColorPickerModel model;
    private ColorComponent colorComponent;
    private ColorValueComponent colorValueComponent;
    private ColorAlphaComponent colorAlphaComponent;

    private ColorElement colorOtherComponent;
    private ColorPreview colorPreview;
    private ColorField colorField;
    private ColorPaletteComponent colorPalette;
    private ColorPipette colorPipette;
    private ColorPickerLayout colorPickerLayout;

    private boolean colorPaletteEnabled = true;
    private boolean colorPipettePickerEnabled = true;

    public ColorPicker() {
        this(new DinoColorPickerModel());
    }

    public ColorPicker(Color initialColor) {
        this(new DinoColorPickerModel(initialColor));
    }

    public ColorPicker(ColorPickerModel model) {
        super(new DefaultColorPickerLayout());
        init(model);
    }

    private void init(ColorPickerModel model) {
        colorComponent = new ColorComponent(model, false);
        colorValueComponent = new ColorValueComponent(model, false);
        colorAlphaComponent = new ColorAlphaComponent(model, false);
        colorField = new ColorField(model);

        add(colorComponent);
        add(createOtherComponent());
        add(colorValueComponent);
        add(colorAlphaComponent);
        add(colorField);

        if (isColorPaletteEnabled()) {
            add(createColorPalette());
        }
        if (isColorPipettePickerEnabled()) {
            installColorPipettePicker();
        }
        setModel(model);
        installLayoutComponent();
    }

    private Component createOtherComponent() {
        colorOtherComponent = new ColorElement(new OtherComponentLayout());
        colorPreview = new ColorPreview();
        colorOtherComponent.add(colorPreview);
        return colorOtherComponent;
    }

    private void installColorPipettePicker() {
        ColorPipette pipette = createColorPipette();
        if (pipette != null && pipette.isAvailable()) {
            JButton cmdPipette = new JButton(new FlatSVGIcon("raven/color/icon/pipette.svg", 0.4f));
            cmdPipette.addActionListener(e -> {
                pipette.setInitialColor(getSelectedColor());
                pipette.show();
            });
            cmdPipette.putClientProperty(FlatClientProperties.STYLE, "border:2,2,2,2,$Component.borderColor,1,10;background:null;");
            colorOtherComponent.add(cmdPipette, 0);
            colorPipette = pipette;
            repaint();
            revalidate();
        }
    }

    private void uninstallColorPipettePicker() {
        if (colorPipette != null) {
            colorOtherComponent.remove(0);
            colorPipette.dispose();
            colorPipette = null;
            repaint();
            revalidate();
        }
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
            colorField.setModel(model);
        }
        if (colorPreview != null) {
            colorPreview.setColor(model.getSelectedColor());
        }
        colorComponent.setModel(model);
        colorValueComponent.setModel(model);
        colorAlphaComponent.setModel(model);

        if (model.showValueComponent()) {
            add(colorValueComponent);
            getColorPickerLayout().setColorValue(colorValueComponent);
        } else {
            remove(colorValueComponent);
            getColorPickerLayout().setColorValue(null);
        }
        revalidate();
    }

    private ColorPipette createColorPipette() {
        if (!isColorPipettePickerEnabled()) {
            return null;
        }
        if (SystemInfo.isWindows) {
            return new DefaultColorPipette(this, (color, evt) -> setSelectedColor(color));
        }
        return null;
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
                model.setSelectedColor(old.getSelectedColor());
            }
            this.model = model;
            this.model.addChangeListener(this);

            updateColorComponent();
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
                    getColorPickerLayout().setColorPalette(null);
                    revalidate();
                }
            } else {
                add(createColorPalette());
                getColorPickerLayout().setColorPalette(colorPalette);
                revalidate();
            }
        }
    }

    public boolean isColorPipettePickerEnabled() {
        return colorPipettePickerEnabled;
    }

    public void setColorPipettePickerEnabled(boolean colorPipettePickerEnabled) {
        if (this.colorPipettePickerEnabled != colorPipettePickerEnabled) {
            this.colorPipettePickerEnabled = colorPipettePickerEnabled;
            if (colorPipettePickerEnabled) {
                installColorPipettePicker();
            } else {
                uninstallColorPipettePicker();
            }
        }
    }

    public ColorPaletteComponent getColorPalette() {
        return colorPalette;
    }

    public ColorPickerLayout getColorPickerLayout() {
        return colorPickerLayout;
    }

    public void setColorPickerLayout(ColorPickerLayout colorPickerLayout) {
        if (colorPickerLayout == null) {
            throw new IllegalArgumentException("color layout can't be null");
        }
        if (this.colorPickerLayout != colorPickerLayout) {
            if (this.colorPickerLayout != null) {
                uninstallLayoutComponent();
            }
            this.colorPickerLayout = colorPickerLayout;
            installLayoutComponent();
            super.setLayout(this.colorPickerLayout);
            repaint();
            revalidate();
        }
    }

    @Override
    public void setLayout(LayoutManager layout) {
        if (!(layout instanceof ColorPickerLayout)) {
            throw new ClassCastException("layout of ColorPicker must be a ColorPickerLayout");
        }
        setColorPickerLayout((ColorPickerLayout) layout);
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
            colorComponent.notifyColorChanged(color, event);
        }
        if (colorValueComponent != null) {
            colorValueComponent.notifyColorChanged(color, event);
        }
        if (colorAlphaComponent != null) {
            colorAlphaComponent.notifyColorChanged(color, event);
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

    private void installLayoutComponent() {
        colorPickerLayout.setColorElement(colorComponent, getColorComponentForLayout(), colorAlphaComponent, colorOtherComponent, colorField, colorPalette);
    }

    private void uninstallLayoutComponent() {
        colorPickerLayout.setColorElement(null, null, null, null, null, null);
    }

    private ColorElement getColorComponentForLayout() {
        if (getModel() == null || getModel().showValueComponent()) {
            return colorValueComponent;
        }
        return null;
    }
}
