package raven.color.component.piptte;

import raven.color.utils.SwingRequest;
import raven.color.event.ColorChangeEvent;
import raven.color.event.ColorChangedListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public abstract class AbstractColorPipette implements ColorPipette {

    private final SwingRequest request = new SwingRequest();
    protected final JComponent parent;
    private final ColorChangedListener listener;
    private JDialog pickerDialog;

    protected final Robot robot;

    private Color currentColor;
    private Color initialColor;

    public AbstractColorPipette(JComponent parent, ColorChangedListener listener) {
        this.parent = parent;
        this.listener = listener;
        this.robot = createRobot();
    }

    public Color getInitialColor() {
        return initialColor;
    }

    @Override
    public void setInitialColor(Color initialColor) {
        this.initialColor = initialColor;
        setColor(initialColor);
    }

    @Override
    public Color getColor() {
        return currentColor;
    }

    public void setColor(Color color) {
        this.currentColor = color;
    }

    @Override
    public Dialog show() {
        Dialog picker = getOrCreatePickerDialog();
        updateLocation();
        picker.setVisible(true);
        return picker;
    }

    @Override
    public void pickAndClose() {
        PointerInfo pointerInfo = MouseInfo.getPointerInfo();
        Color pixelColor = getPixelColor(pointerInfo.getLocation());
        cancelPipette();
        notifyListener(pixelColor, 0);
        setInitialColor(pixelColor);
    }

    @Override
    public void cancelPipette() {
        Dialog picker = getPickerDialog();
        if (picker != null) {
            picker.setVisible(false);
        }
        Color initialColor = getInitialColor();
        if (initialColor != null) {
            notifyListener(initialColor, 0);
        }
    }

    @Override
    public void dispose() {
        // remove event
        if (pickerDialog != null) {
            MouseListener[] mouseListeners = pickerDialog.getMouseListeners();
            for (MouseListener l : mouseListeners) {
                pickerDialog.removeMouseListener(l);
            }
            KeyListener[] keyListeners = pickerDialog.getKeyListeners();
            for (KeyListener l : keyListeners) {
                pickerDialog.removeKeyListener(l);
            }
            MouseMotionListener[] mouseMotionListeners = pickerDialog.getMouseMotionListeners();
            for (MouseMotionListener l : mouseMotionListeners) {
                pickerDialog.removeMouseMotionListener(l);
            }
            FocusListener[] focusListeners = pickerDialog.getFocusListeners();
            for (FocusListener l : focusListeners) {
                pickerDialog.removeFocusListener(l);
            }
            pickerDialog = null;
        }
        request.dispose();
        setInitialColor(null);
        setColor(null);
    }

    @Override
    public boolean imageUpdate(Image img, int info, int x, int y, int width, int height) {
        return false;
    }

    protected Color getPixelColor(Point location) {
        return robot.getPixelColor(location.x, location.y);
    }

    protected Point updateLocation() {
        PointerInfo pointerInfo = MouseInfo.getPointerInfo();
        if (pointerInfo == null) return null;

        Point mouseLocation = pointerInfo.getLocation();
        Dialog picker = getPickerDialog();
        if (picker != null && mouseLocation != null) {
            picker.setLocation(mouseLocation.x - picker.getWidth() / 2, mouseLocation.y - picker.getHeight() / 2);
        }

        return mouseLocation;
    }

    protected Dialog getOrCreatePickerDialog() {
        if (pickerDialog == null) {
            Window owner = SwingUtilities.getWindowAncestor(parent);
            if (owner instanceof Dialog) {
                pickerDialog = new JDialog((Dialog) owner);
            } else if (owner instanceof Frame) {
                pickerDialog = new JDialog((Frame) owner);
            } else {
                pickerDialog = new JDialog(new JFrame());
            }
            pickerDialog.setTitle("colorPickerDialog");
        }
        pickerDialog.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                e.consume();
                if (SwingUtilities.isLeftMouseButton(e)) {
                    pickAndClose();
                } else {
                    cancelPipette();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                e.consume();
            }
        });
        pickerDialog.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        cancelPipette();
                        break;
                    case KeyEvent.VK_ENTER:
                        pickAndClose();
                        break;
                }
            }
        });

        pickerDialog.setUndecorated(true);
        pickerDialog.setAlwaysOnTop(true);

        JRootPane rootPane = pickerDialog.getRootPane();
        rootPane.putClientProperty("Window.shadow", Boolean.FALSE);

        return pickerDialog;
    }

    public JDialog getPickerDialog() {
        return pickerDialog;
    }

    protected void notifyListener(Color color, int delayMillis) {
        request.setRequest(() -> listener.colorChanged(color, new ColorChangeEvent(this, true)), delayMillis);
    }

    private static Robot createRobot() {
        try {
            return new Robot();
        } catch (AWTException e) {
            return null;
        }
    }
}
