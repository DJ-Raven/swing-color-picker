package raven.color.component;

import raven.color.event.ColorChangeEvent;
import raven.color.event.ColorChangedListener;
import raven.color.utils.ColorPickerModel;

import java.awt.*;

public abstract class SliderColorModel extends SliderColor {

    private final boolean installModelListener;
    private ColorChangedListener listener;
    private ColorPickerModel model;

    public SliderColorModel(ColorPickerModel model) {
        this(model, true);
    }

    public SliderColorModel(ColorPickerModel model, boolean installModelListener) {
        this.model = model;
        this.installModelListener = installModelListener;
        install();
    }

    public abstract void notifyColorChanged(Color color, ColorChangeEvent event);

    public abstract void notifyModelChanged(ColorPickerModel model);

    @Override
    public void install() {
        super.install();
        installModelListener();
        notifyModelChanged(model);
    }

    @Override
    public void uninstall() {
        super.uninstall();
        uninstallModelListener();
        listener = null;
    }

    private void installModelListener() {
        if (isInstallModelListener()) {
            model.addChangeListener(getOrCreateListener());
        }
    }

    private void uninstallModelListener() {
        if (model != null && listener != null) {
            model.removeChangeListener(listener);
        }
    }

    public ColorChangedListener getOrCreateListener() {
        if (listener == null) {
            listener = this::notifyColorChanged;
        }
        return listener;
    }

    public boolean isInstallModelListener() {
        return installModelListener;
    }

    public ColorPickerModel getModel() {
        return model;
    }

    public void setModel(ColorPickerModel model) {
        if (this.model != model) {
            uninstallModelListener();
            this.model = model;
            if (this.model != null) {
                installModelListener();
                notifyModelChanged(this.model);
            }
        }
    }
}
