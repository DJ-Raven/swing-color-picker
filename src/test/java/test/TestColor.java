package test;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import raven.color.*;
import raven.color.component.palette.ColorPaletteType;
import test.utils.LineLayout;

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
        setLayout(new LineLayout(LineLayout.HORIZONTAL, false, LineLayout.CENTER));
        colorPicker = new ColorPicker();
        colorPicker.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:10,10,10,10,$Component.borderColor,1,15;" +
                "[light]background:#E6E6E6;" +
                "[dark]background:#363636;");

        add(colorPicker);

        colorPicker.addColorChangedListener((color, event) -> {
            System.out.println("Color changed: " + color);
        });
        createMenuBar();
        createOption();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenu menuThemes = new JMenu("Themes");
        JMenuItem menuExit = new JMenuItem("Exit");
        ButtonGroup group = new ButtonGroup();

        menuThemes.add(createThemeItem(FlatLightLaf.class, FlatLightLaf.NAME, group));
        menuThemes.add(createThemeItem(FlatDarkLaf.class, FlatDarkLaf.NAME, group));
        menuThemes.add(createThemeItem(FlatIntelliJLaf.class, FlatIntelliJLaf.NAME, group));
        menuThemes.add(createThemeItem(FlatDarculaLaf.class, FlatDarculaLaf.NAME, group));
        menuThemes.add(createThemeItem(FlatMacLightLaf.class, FlatMacLightLaf.NAME, group));
        menuThemes.add(createThemeItem(FlatMacDarkLaf.class, FlatMacDarkLaf.NAME, group));

        menuExit.addActionListener(e -> System.exit(0));

        menuFile.add(menuExit);

        menuBar.add(menuFile);
        menuBar.add(menuThemes);
        setJMenuBar(menuBar);
    }

    private JCheckBoxMenuItem createThemeItem(Class<? extends FlatLaf> clazz, String name, ButtonGroup group) {
        JCheckBoxMenuItem item = new JCheckBoxMenuItem(name);
        group.add(item);
        item.addActionListener(e -> {
            if (item.isSelected()) {
                EventQueue.invokeLater(() -> {
                    try {
                        UIManager.setLookAndFeel(clazz.getName());
                        FlatLaf.updateUI();
                    } catch (Exception err) {
                        System.err.println(err.getMessage());
                    }
                });
            }
        });
        if (UIManager.getLookAndFeel().getName().equals(name)) {
            item.setSelected(true);
        }
        return item;
    }

    // option
    private JRadioButton rNonePaletteColor;
    private JRadioButton rDefaultColor;
    private JRadioButton rTailwindColor;
    private JRadioButton rMaterialColor;

    private void createOption() {
        JPanel panelOption = new JPanel(new LineLayout(LineLayout.VERTICAL, true));
        JPanel panelPalette = new JPanel(new LineLayout());
        panelPalette.setBorder(new TitledBorder("Options Color Palette"));

        rNonePaletteColor = new JRadioButton("None Palette");
        rDefaultColor = new JRadioButton("Default Color", true);
        rTailwindColor = new JRadioButton("Tailwind Color");
        rMaterialColor = new JRadioButton("Material Color");

        ButtonGroup groupPalette = new ButtonGroup();

        groupPalette.add(rNonePaletteColor);
        groupPalette.add(rDefaultColor);
        groupPalette.add(rTailwindColor);
        groupPalette.add(rMaterialColor);

        rNonePaletteColor.addActionListener(e -> {
            colorPicker.setColorPaletteEnabled(false);
        });
        rDefaultColor.addActionListener(e -> applyColorStyle(colorPicker));
        rTailwindColor.addActionListener(e -> applyColorStyle(colorPicker));
        rMaterialColor.addActionListener(e -> applyColorStyle(colorPicker));

        panelPalette.add(rNonePaletteColor);

        panelPalette.add(rDefaultColor);
        panelPalette.add(rTailwindColor);
        panelPalette.add(rMaterialColor);

        panelOption.add(panelPalette);

        // model option
        JPanel panelModel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        panelModel.setBorder(new TitledBorder("Options Color Model"));

        ButtonGroup group = new ButtonGroup();
        JRadioButton jrDino = new JRadioButton("Dino", true);
        JRadioButton jrDisk = new JRadioButton("Disk");
        JRadioButton jrCorelTriangle = new JRadioButton("Corel Triangle");
        JRadioButton jrCorelSquare = new JRadioButton("Corel Square");
        jrDino.addActionListener(e -> {
            if (jrDino.isSelected()) {
                colorPicker.setModel(new DinoColorPickerModel());
            }
        });
        jrDisk.addActionListener(e -> {
            if (jrDisk.isSelected()) {
                colorPicker.setModel(new DiskColorPickerModel());
            }
        });

        jrCorelTriangle.addActionListener(e -> {
            if (jrCorelTriangle.isSelected()) {
                colorPicker.setModel(new CorelTriangleColorPickerModel());
            }
        });
        jrCorelSquare.addActionListener(e -> {
            if (jrCorelSquare.isSelected()) {
                colorPicker.setModel(new CorelSquareColorPickerModel());
            }
        });

        group.add(jrDino);
        group.add(jrDisk);
        group.add(jrCorelTriangle);
        group.add(jrCorelSquare);

        panelModel.add(jrDino);
        panelModel.add(jrDisk);
        panelModel.add(jrCorelTriangle);
        panelModel.add(jrCorelSquare);

        panelOption.add(panelModel);

        // other option
        JPanel panelOtherOption = new JPanel(new FlowLayout(FlowLayout.LEADING));
        panelOtherOption.setBorder(new TitledBorder("Other Options"));

        JCheckBox chPipettePicker = new JCheckBox("Pipette Picker Enabled", true);
        JCheckBox chPreview = new JCheckBox("Preview Enabled", true);
        JCheckBox chAlpha = new JCheckBox("Alpha Enabled", true);
        chPipettePicker.addActionListener(e -> colorPicker.setColorPipettePickerEnabled(chPipettePicker.isSelected()));
        chPreview.addActionListener(e -> colorPicker.setColorPreviewEnabled(chPreview.isSelected()));
        chAlpha.addActionListener(e -> colorPicker.setColorAlphaEnabled(chAlpha.isSelected()));
        panelOtherOption.add(chPipettePicker);
        panelOtherOption.add(chPreview);
        panelOtherOption.add(chAlpha);

        panelOption.add(panelOtherOption);

        // button
        JPanel panelTest = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JButton cmdShowDialog = new JButton("show as dialog");
        cmdShowDialog.addActionListener(e -> {
            ColorPicker cp = new ColorPicker(colorPicker.getModel());
            cp.setColorPaletteEnabled(rNonePaletteColor.isSelected());
            applyColorStyle(cp);

            Color color = ColorPicker.showDialog(this, "Pick Color", cp);
            if (color != null) {
                System.out.println("--------------");
                System.out.println("Color selected: " + color);
            }
        });
        panelTest.add(cmdShowDialog);
        panelOption.add(panelTest);
        add(panelOption);
    }

    private void applyColorStyle(ColorPicker colorPicker) {
        colorPicker.setColorPaletteEnabled(true);
        if (rDefaultColor.isSelected()) {
            colorPicker.applyColorPaletteType(ColorPaletteType.DEFAULT);
        } else if (rTailwindColor.isSelected()) {
            colorPicker.applyColorPaletteType(ColorPaletteType.TAILWIND);
        } else if (rMaterialColor.isSelected()) {
            colorPicker.applyColorPaletteType(ColorPaletteType.MATERIAL);
        }
    }

    public static void main(String[] args) {
        // UIScale.setZoomFactor(1.5f);
        FlatMacLightLaf.setup();
        EventQueue.invokeLater(() -> new TestColor().setVisible(true));
    }
}
