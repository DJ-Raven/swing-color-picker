package raven.color.event;

import java.util.EventObject;

public class ColorChangeEvent extends EventObject {

    private final boolean valueChanged;

    public ColorChangeEvent(Object source, boolean valueChanged) {
        super(source);
        this.valueChanged = valueChanged;
    }

    public boolean isValueChanged() {
        return valueChanged;
    }
}
