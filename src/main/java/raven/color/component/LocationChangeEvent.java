package raven.color.component;

import raven.color.utils.ColorLocation;

import java.util.EventObject;

public class LocationChangeEvent extends EventObject {

    private ColorLocation pressedLocation;
    private boolean consumed;

    public LocationChangeEvent(Object source) {
        super(source);
    }

    protected void setPressedLocation(ColorLocation pressedLocation) {
        this.pressedLocation = pressedLocation;
    }

    protected void reset() {
        this.consumed = false;
    }

    public ColorLocation getPressedLocation() {
        return pressedLocation;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void consume() {
        consumed = true;
    }
}
