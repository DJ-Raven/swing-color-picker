package test;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatIntelliJLaf;
import net.miginfocom.swing.MigLayout;
import raven.color.ColorPicker;
import raven.color.DinoColorPickerModel;
import raven.color.DiskColorPickerModel;
import raven.color.component.ColorPaletteType;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class TestColor extends JFrame {

    private final ColorPicker colorPicker;

    public TestColor() {
        super("Test Swing ColorPicker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(800, 800));
        setLocationRelativeTo(null);

        setLayout(new MigLayout("al center center", "[][300,fill]", "[fill]"));

        colorPicker = new ColorPicker();
        colorPicker.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:10,10,10,10,$Component.borderColor,1,15;" +
                "[light]background:#E6E6E6;" +
                "[dark]background:#363636;");

        add(colorPicker);

        colorPicker.addColorChangedListener((color, event) -> {
            // System.out.println("Color changed: " + color);
        });

        createOption();
    }

    // option
    private JCheckBox chEnablePalette;
    private JRadioButton rDefaultColor;
    private JRadioButton rTailwindColor;
    private JRadioButton rMaterialColor;

    private void createOption() {
        JPanel panelOption = new JPanel(new MigLayout("wrap,fillx", "[fill]"));
        JPanel panelPalette = new JPanel(new MigLayout());
        panelPalette.setBorder(new TitledBorder("Options Color Palette"));

        chEnablePalette = new JCheckBox("Enable Palette", true);
        rDefaultColor = new JRadioButton("Default Color", true);
        rTailwindColor = new JRadioButton("Tailwind Color");
        rMaterialColor = new JRadioButton("Material Color");

        ButtonGroup groupPalette = new ButtonGroup();

        groupPalette.add(rDefaultColor);
        groupPalette.add(rTailwindColor);
        groupPalette.add(rMaterialColor);

        chEnablePalette.addActionListener(e -> {
            boolean enable = chEnablePalette.isSelected();
            colorPicker.setColorPaletteEnabled(enable);
            rDefaultColor.setEnabled(enable);
            rTailwindColor.setEnabled(enable);
            rMaterialColor.setEnabled(enable);
        });
        rDefaultColor.addActionListener(e -> applyColorStyle(colorPicker));
        rTailwindColor.addActionListener(e -> applyColorStyle(colorPicker));
        rMaterialColor.addActionListener(e -> applyColorStyle(colorPicker));

        panelPalette.add(chEnablePalette, "wrap");

        panelPalette.add(rDefaultColor);
        panelPalette.add(rTailwindColor);
        panelPalette.add(rMaterialColor);

        panelOption.add(panelPalette);

        // model option
        JPanel panelModel = new JPanel(new MigLayout());
        panelModel.setBorder(new TitledBorder("Options Color Model"));

        ButtonGroup group = new ButtonGroup();
        JRadioButton jrDino = new JRadioButton("Dino Model", true);
        JRadioButton jrDisk = new JRadioButton("Disk Model");

        jrDino.addActionListener(e -> {
            if (jrDino.isSelected()) {
                colorPicker.setModel(new DinoColorPickerModel(colorPicker.getSelectedColor()));
            }
        });
        jrDisk.addActionListener(e -> {
            if (jrDisk.isSelected()) {
                colorPicker.setModel(new DiskColorPickerModel(colorPicker.getSelectedColor()));
            }
        });

        group.add(jrDino);
        group.add(jrDisk);

        panelModel.add(jrDino);
        panelModel.add(jrDisk);

        panelOption.add(panelModel);

        // button
        JButton cmdShowDialog = new JButton("show as dialog");
        cmdShowDialog.addActionListener(e -> {
            ColorPicker cp = new ColorPicker(colorPicker.getModel());
            cp.setColorPaletteEnabled(chEnablePalette.isSelected());
            applyColorStyle(cp);

            Color color = ColorPicker.showDialog(this, "Pick Color", cp);
            if (color != null) {
                System.out.println("--------------");
                System.out.println("Color selected: " + color);
            }
        });
        panelOption.add(cmdShowDialog, "grow 0");

        add(panelOption);
    }

    private void createModelOption() {

    }

    private void applyColorStyle(ColorPicker colorPicker) {
        if (rDefaultColor.isSelected()) {
            colorPicker.applyColorPaletteType(ColorPaletteType.DEFAULT);
        } else if (rTailwindColor.isSelected()) {
            colorPicker.applyColorPaletteType(ColorPaletteType.TAILWIND);
        } else if (rMaterialColor.isSelected()) {
            colorPicker.applyColorPaletteType(ColorPaletteType.MATERIAL);
        }
    }

    public static void main(String[] args) {
        FlatIntelliJLaf.setup();
        EventQueue.invokeLater(() -> new TestColor().setVisible(true));
    }
}
