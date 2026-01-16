package raven.color;

import java.awt.*;

public class CorelRhombusColorPickerModel extends CorelSquareColorPickerModel {

    public CorelRhombusColorPickerModel() {
    }

    public CorelRhombusColorPickerModel(Color color) {
        super(color);
    }

    @Override
    protected float getSelectionRotate() {
        return 45;
    }
}
