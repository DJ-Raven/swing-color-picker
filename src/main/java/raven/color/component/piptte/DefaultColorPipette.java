package raven.color.component.piptte;

import raven.color.event.ColorChangedListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;

public class DefaultColorPipette extends AbstractColorPipette {

    private static final int SIZE = 30;
    private static final int DIALOG_SIZE = SIZE - 4;
    private static final Point HOT_SPOT = new Point(DIALOG_SIZE / 2, DIALOG_SIZE / 2);

    private final Rectangle captureRect = new Rectangle(-4, -4, 9, 9);
    private final Rectangle zoomRect = new Rectangle(0, 0, SIZE, SIZE);
    private final Point previousLocation = new Point();

    private BufferedImage image;
    private Graphics2D graphics;
    private final Timer timer;

    public DefaultColorPipette(JComponent parent, ColorChangedListener listener) {
        super(parent, listener);
        timer = new Timer(5, e -> updatePipette());
    }

    @Override
    protected Color getPixelColor(Point location) {
        return super.getPixelColor(new Point(location.x - HOT_SPOT.x + SIZE / 2 + (captureRect.width / 2 - 2), location.y - HOT_SPOT.y + SIZE / 2 + (captureRect.height / 2 - 2)));
    }

    @Override
    public Dialog show() {
        Dialog picker = super.show();
        timer.start();

        picker.setOpacity(0.009f);

        Area area = new Area(new Rectangle(0, 0, DIALOG_SIZE, DIALOG_SIZE));
        area.subtract(new Area(new Rectangle(SIZE / 2 - 1, SIZE / 2 - 1, captureRect.width - 2, captureRect.height - 2)));
        picker.setShape(area);
        return picker;
    }

    @Override
    protected Dialog getOrCreatePickerDialog() {
        Dialog picker = getPickerDialog();
        if (picker == null) {
            picker = super.getOrCreatePickerDialog();
            picker.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    updatePipette();
                }
            });
            picker.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    updatePipette();
                }
            });
            picker.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    if (e.isTemporary()) {
                        pickAndClose();
                    } else {
                        cancelPipette();
                    }
                }
            });
            picker.setSize(DIALOG_SIZE, DIALOG_SIZE);

            image = parent.getGraphicsConfiguration().createCompatibleImage(SIZE, SIZE, Transparency.TRANSLUCENT);
            graphics = (Graphics2D) image.getGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        }
        return picker;
    }

    @Override
    public void cancelPipette() {
        timer.stop();
        super.cancelPipette();
    }

    @Override
    public void dispose() {
        timer.stop();
        super.dispose();
        if (graphics != null) {
            graphics.dispose();
        }
        image = null;
    }

    @Override
    public boolean isAvailable() {
        if (robot != null) {
            robot.createScreenCapture(new Rectangle(0, 0, 1, 1));
            return true;
        }
        return false;
    }

    private void updatePipette() {
        Dialog picker = getPickerDialog();
        if (picker != null && picker.isShowing()) {
            Point mouseLoc = updateLocation();
            if (mouseLoc == null) return;
            final Color c = getPixelColor(mouseLoc);
            if (!c.equals(getColor()) || !mouseLoc.equals(previousLocation)) {
                setColor(c);
                previousLocation.setLocation(mouseLoc);
                captureRect.setLocation(mouseLoc.x - HOT_SPOT.x + SIZE / 2 - 2, mouseLoc.y - HOT_SPOT.y + SIZE / 2 - 2);

                BufferedImage capture = robot.createScreenCapture(captureRect);

                // clear the cursor graphics
                graphics.setComposite(AlphaComposite.Src);
                graphics.setColor(new Color(0, 0, 0, 0));
                graphics.fillRect(0, 0, image.getWidth(), image.getHeight());

                graphics.drawImage(capture, zoomRect.x, zoomRect.y, zoomRect.width, zoomRect.height, this);

                // paint border
                graphics.setComposite(AlphaComposite.SrcOver);
                graphics.setColor(Color.GRAY);
                graphics.drawRect(0, 0, image.getWidth() - 1, image.getHeight() - 1);
                graphics.setColor(Color.RED);
                graphics.drawRect(image.getWidth() / 2 - 1, image.getHeight() / 2 - 1, 2, 2);

                picker.setCursor(parent.getToolkit().createCustomCursor(image, HOT_SPOT, "ColorPicker"));
                notifyListener(c, 300);
            }
        }
    }
}
