package raven.color.component;

import com.formdev.flatlaf.util.ScaledEmptyBorder;
import raven.color.utils.ColorPickerModel;
import raven.color.utils.ColorPickerUtils;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.Objects;

public class ColorField extends ColorElement implements PropertyChangeListener {

    private boolean colorAlphaEnabled = true;
    private ColorPickerModel model;
    private JFormattedTextField txtRed;
    private JFormattedTextField txtGreen;
    private JFormattedTextField txtBlue;
    private JFormattedTextField txtAlpha;
    private JFormattedTextField txtHex;

    private JLabel lbRed;
    private JLabel lbGreen;
    private JLabel lbBlue;
    private JLabel lbAlpha;
    private JLabel lbHex;

    private int red;
    private int green;
    private int blue;
    private int alpha;

    private String hex;

    public ColorField(ColorPickerModel model) {
        this.model = model;
        init();
    }

    private void init() {
        setBorder(new ScaledEmptyBorder(5, 10, 5, 10));
        txtRed = createTextField();
        txtGreen = createTextField();
        txtBlue = createTextField();
        txtAlpha = createTextField();
        txtHex = createHexTextField();

        lbRed = new JLabel("R");
        lbGreen = new JLabel("G");
        lbBlue = new JLabel("B");
        lbAlpha = new JLabel("A");
        lbHex = new JLabel("Hex");

        add(lbRed);
        add(lbGreen);
        add(lbBlue);
        add(lbAlpha);
        add(lbHex);

        add(txtRed);
        add(txtGreen);
        add(txtBlue);
        add(txtAlpha);
        add(txtHex);

        setLayout(new ColorFieldLayout());
    }

    private JFormattedTextField createTextField() {
        JFormattedTextField txt = new JFormattedTextField(createFormatter());
        txt.setHorizontalAlignment(SwingConstants.CENTER);
        txt.setColumns(2);
        txt.addPropertyChangeListener("value", this);
        txt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (txt.getValue() == null) {
                    txt.setValue(0);
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(txt::selectAll);
            }
        });
        return txt;
    }

    private JFormattedTextField createHexTextField() {
        JFormattedTextField txt = new JFormattedTextField(createHexFormatter());
        txt.addPropertyChangeListener("value", this);
        txt.setHorizontalAlignment(SwingConstants.CENTER);
        txt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(txt::selectAll);
            }
        });
        return txt;
    }

    private NumberFormatter createFormatter() {
        NumberFormatter formatter = new NumberFormatter() {
            @Override
            public Object stringToValue(String text) throws ParseException {
                if (text == null || text.trim().isEmpty()) {
                    return null;
                }
                return super.stringToValue(text);
            }
        };
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(255);
        formatter.setCommitsOnValidEdit(true);
        return formatter;
    }

    private DefaultFormatter createHexFormatter() {
        DefaultFormatter formatter = new DefaultFormatter() {
            @Override
            public Object stringToValue(String string) throws ParseException {
                if (string != null) {
                    string = string.trim();
                    string = string.startsWith("#") ? string.substring(1) : string;
                    if (string.matches("^[0-9a-fA-F]{6,8}$")) {
                        return string.toUpperCase();
                    }
                }
                throw new ParseException("Invalid hex color", 0);
            }
        };
        formatter.setCommitsOnValidEdit(true);

        return formatter;
    }

    public void colorChanged(Color color) {
        if (color == null) {
            txtRed.setValue(0);
            txtGreen.setValue(0);
            txtBlue.setValue(0);
            txtAlpha.setValue(0);
            txtHex.setValue(null);
        } else {
            int red = color.getRed();
            int green = color.getGreen();
            int blue = color.getBlue();
            int alpha = color.getAlpha();
            this.red = red;
            txtRed.setValue(red);

            this.green = green;
            txtGreen.setValue(green);

            this.blue = blue;
            txtBlue.setValue(blue);

            this.alpha = alpha;
            txtAlpha.setValue(alpha);
            this.hex = colorToHex(color);
            txtHex.setValue(hex);
        }
    }

    private String colorToHex(Color color) {
        String format = isColorAlphaEnabled()
                ? "%02X%02X%02X%02X"
                : "%02X%02X%02X";

        return isColorAlphaEnabled()
                ? String.format(format,
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                color.getAlpha())
                : String.format(format,
                color.getRed(),
                color.getGreen(),
                color.getBlue());
    }

    private Color decodeRGBA(String hex) {
        if (hex == null) {
            throw new IllegalArgumentException("Invalid RGBA color format: null");
        }

        hex = hex.trim();
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        // Validate hex length and characters
        if (!hex.matches("^[0-9a-fA-F]{6,8}$")) {
            throw new IllegalArgumentException("Invalid RGBA color format");
        }

        // Normalize hex string to 8 digits by handling 6 or 7 digit cases
        if (hex.length() == 6) {
            hex += "FF"; // full opacity
        } else if (hex.length() == 7) {
            // last digit is half alpha, pad with 'F'
            hex = hex.substring(0, 6) + hex.charAt(6) + "F";
        }

        // Parse the 8-digit RGBA hex into int
        int rgba = (int) Long.parseLong(hex, 16);

        return new Color(
                (rgba >> 24) & 0xFF,  // Red
                (rgba >> 16) & 0xFF,  // Green
                (rgba >> 8) & 0xFF,   // Blue
                rgba & 0xFF           // Alpha
        );
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        onChanged(evt.getSource());
    }

    private void onChanged(Object source) {
        if (source == txtHex) {
            String colorHex = txtHex.getValue() == null ? null : txtHex.getValue().toString();
            if (!Objects.equals(this.hex, colorHex)) {
                if (colorHex != null) {
                    getModel().setSelectedColor(decodeRGBA(colorHex));
                }
                this.hex = colorHex;
            }
        } else {
            int red = txtRed.getValue() == null ? 0 : Integer.parseInt(txtRed.getValue().toString());
            int green = txtGreen.getValue() == null ? 0 : Integer.parseInt(txtGreen.getValue().toString());
            int blue = txtBlue.getValue() == null ? 0 : Integer.parseInt(txtBlue.getValue().toString());
            int alpha = txtAlpha.getValue() == null ? 0 : Integer.parseInt(txtAlpha.getValue().toString());
            if (this.red != red || this.green != green || this.blue != blue || this.alpha != alpha) {
                this.red = red;
                this.green = green;
                this.blue = blue;
                this.alpha = alpha;
                getModel().setSelectedColor(new Color(red, green, blue, alpha));
            }
        }
    }

    public ColorPickerModel getModel() {
        return model;
    }

    public void setModel(ColorPickerModel model) {
        this.model = model;
        if (model != null) {
            colorChanged(model.getSelectedColor());
        }
    }

    public boolean isColorAlphaEnabled() {
        return colorAlphaEnabled;
    }

    public void setColorAlphaEnabled(boolean colorAlphaEnabled) {
        if (this.colorAlphaEnabled != colorAlphaEnabled) {
            this.colorAlphaEnabled = colorAlphaEnabled;
            if (isColorAlphaEnabled()) {
                add(lbAlpha);
                add(txtAlpha);
            } else {
                remove(lbAlpha);
                remove(txtAlpha);
            }
            colorChanged(model.getSelectedColor());
        }
    }

    private class ColorFieldLayout implements LayoutManager {

        private static final int H_GAP = 7;
        private static final int V_GAP = 5;

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return size(parent);
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return size(parent);
            }
        }

        private Dimension size(Container parent) {
            Insets insets = parent.getInsets();
            int width = insets.left + insets.right;
            int height = insets.top + insets.bottom;
            int vGap = ColorPickerUtils.scale(V_GAP);
            Dimension labelSize;
            Dimension fieldSize;
            if (colorAlphaEnabled) {
                labelSize = calculate(lbRed, lbGreen, lbBlue, lbAlpha, lbHex);
                fieldSize = calculate(txtRed, txtGreen, txtBlue, txtAlpha, txtHex);
            } else {
                labelSize = calculate(lbRed, lbGreen, lbBlue, lbHex);
                fieldSize = calculate(txtRed, txtGreen, txtBlue, txtHex);
            }
            height += labelSize.height + fieldSize.height + vGap;
            width += Math.max(labelSize.width, fieldSize.width);
            return new Dimension(width, height);
        }

        private Dimension calculate(JComponent... components) {
            int width = 0;
            int height = 0;
            for (JComponent com : components) {
                Dimension size = com.getPreferredSize();
                Insets insets = ColorPickerUtils.getVisualPadding(com);
                if (insets != null) {
                    size.width -= (insets.left + insets.right);
                    size.height -= (insets.top + insets.bottom);
                }
                width += size.width;
                height = Math.max(height, size.height);
            }
            if (components.length > 1) {
                width += (ColorPickerUtils.scale(H_GAP) * components.length - 1);
            }
            return new Dimension(width, height);
        }

        @Override
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                Insets insets = parent.getInsets();
                int width = parent.getWidth() - (insets.left + insets.right);
                int hGap = ColorPickerUtils.scale(H_GAP);
                int vGap = ColorPickerUtils.scale(V_GAP);
                int x = insets.left;
                int y = insets.top;
                x += layoutComponent(lbRed, txtRed, x, y, -1, vGap) + hGap;
                x += layoutComponent(lbGreen, txtGreen, x, y, -1, vGap) + hGap;
                x += layoutComponent(lbBlue, txtBlue, x, y, -1, vGap) + hGap;
                if (colorAlphaEnabled) {
                    x += layoutComponent(lbAlpha, txtAlpha, x, y, -1, vGap) + hGap;
                }

                int w = width - (x - insets.left);
                layoutComponent(lbHex, txtHex, x, y, w, vGap);
            }
        }

        private int layoutComponent(JComponent label, JComponent field, int x, int y, int width, int vGap) {
            Dimension lbSize = label.getPreferredSize();
            Dimension fieldSize = field.getPreferredSize();
            Insets vsp = ColorPickerUtils.getVisualPadding(field);
            int lbWidth = lbSize.width;
            int fieldWidth;
            if (width == -1) {
                fieldWidth = fieldSize.width;
            } else {
                fieldWidth = width;
                if (vsp != null) {
                    fieldWidth += vsp.left + vsp.right;
                }
            }
            int max = Math.max(lbWidth, fieldWidth);
            int lbx = x + (max - lbWidth) / 2;
            if (vsp != null) {
                lbx -= vsp.left;
            }
            int fx = x + (max - fieldWidth) / 2;
            int fy = y + lbSize.height + vGap;

            if (vsp != null) {
                fx -= vsp.left;
                fy -= vsp.top;
                max -= vsp.left + vsp.right;
            }

            label.setBounds(lbx, y, lbWidth, lbSize.height);
            field.setBounds(fx, fy, fieldWidth, fieldSize.height);
            return max;
        }
    }
}
