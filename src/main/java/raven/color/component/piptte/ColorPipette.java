package raven.color.component.piptte;

import java.awt.*;
import java.awt.image.ImageObserver;

public interface ColorPipette extends ImageObserver {

    void setInitialColor(Color color);

    Color getColor();

    Dialog show();

    void pickAndClose();

    void cancelPipette();

    void dispose();

    boolean isAvailable();
}
