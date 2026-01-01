package raven.color.component;

import net.miginfocom.swing.MigLayout;
import raven.color.utils.ColorPickerModel;

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

public class ColorField extends JComponent implements PropertyChangeListener {

    private ColorPickerModel model;
    private JFormattedTextField txtRed;
    private JFormattedTextField txtGreen;
    private JFormattedTextField txtBlue;
    private JFormattedTextField txtAlpha;
    private JFormattedTextField txtHex;

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
        setLayout(new MigLayout("insets n 10 5 10,wrap 5,gapy 5,fillx", "[center,grow 0][center,grow 0][center,grow 0][center,grow 0][fill]"));

        txtRed = createTextField();
        txtGreen = createTextField();
        txtBlue = createTextField();
        txtAlpha = createTextField();
        txtHex = createHexTextField();

        add(new JLabel("R"));
        add(new JLabel("G"));
        add(new JLabel("B"));
        add(new JLabel("A"));
        add(new JLabel("Hex", SwingConstants.CENTER));

        add(txtRed);
        add(txtGreen);
        add(txtBlue);
        add(txtAlpha);
        add(txtHex);
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
        return String.format("%02X%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
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
}
