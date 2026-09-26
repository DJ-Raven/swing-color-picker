package test;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.formdev.flatlaf.util.SystemFileChooser;
import raven.color.*;
import raven.color.component.palette.ColorPaletteType;
import test.utils.LineLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
        JPanel panelModel = new JPanel(new LineLayout(LineLayout.VERTICAL, true));
        panelModel.setBorder(new TitledBorder("Options Color Model"));

        ButtonGroup group = new ButtonGroup();
        JRadioButton jrDino = new JRadioButton("Dino", true);
        JRadioButton jrDisk = new JRadioButton("Disk");
        JRadioButton jrCorelTriangle = new JRadioButton("Corel Triangle");
        JRadioButton jrCorelSquare = new JRadioButton("Corel Square");
        JRadioButton jrCorelRhombus = new JRadioButton("Corel Rhombus");
        JRadioButton jrCorelCircle = new JRadioButton("Corel Circle");
        JRadioButton jrHexagon = new JRadioButton("Hexagon");
        JRadioButton jrImage = new JRadioButton("Image");
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
        jrCorelRhombus.addActionListener(e -> {
            if (jrCorelRhombus.isSelected()) {
                colorPicker.setModel(new CorelRhombusColorPickerModel());
            }
        });
        jrCorelCircle.addActionListener(e -> {
            if (jrCorelCircle.isSelected()) {
                colorPicker.setModel(new CorelCircleColorPickerModel());
            }
        });
        JCheckBox chHexagonGrayRow = new JCheckBox("Hexagon Gray Row", true);
        chHexagonGrayRow.setEnabled(false);
        jrHexagon.addItemListener(e -> chHexagonGrayRow.setEnabled(jrHexagon.isSelected()));
        jrHexagon.addActionListener(e -> {
            if (jrHexagon.isSelected()) {
                colorPicker.setModel(new HexagonColorPickerModel(Color.WHITE, chHexagonGrayRow.isSelected()));
            }
        });
        chHexagonGrayRow.addActionListener(e -> {
            if (colorPicker.getModel() instanceof HexagonColorPickerModel) {
                ((HexagonColorPickerModel) colorPicker.getModel()).setGrayRowEnabled(chHexagonGrayRow.isSelected());
            }
        });
        JButton cmdChooseImage = new JButton("Choose Image...");
        cmdChooseImage.setEnabled(false);
        jrImage.addItemListener(e -> cmdChooseImage.setEnabled(jrImage.isSelected()));
        jrImage.addActionListener(e -> {
            if (jrImage.isSelected()) {
                colorPicker.setModel(new ImageColorPickerModel(createSampleIcon()));
            }
        });
        cmdChooseImage.addActionListener(e -> {
            if (colorPicker.getModel() instanceof ImageColorPickerModel) {
                SystemFileChooser chooser = new SystemFileChooser();
                chooser.setDialogTitle("Choose Image");
                chooser.setFileFilter(new SystemFileChooser.FileNameExtensionFilter("Image", ImageIO.getReaderFileSuffixes()));
                if (chooser.showOpenDialog(this) == SystemFileChooser.APPROVE_OPTION) {
                    try {
                        BufferedImage image = ImageIO.read(chooser.getSelectedFile());
                        if (image != null) {
                            ((ImageColorPickerModel) colorPicker.getModel()).setIcon(new ImageIcon(image));
                        }
                    } catch (IOException err) {
                        System.err.println(err.getMessage());
                    }
                }
            }
        });

        group.add(jrDino);
        group.add(jrDisk);
        group.add(jrCorelTriangle);
        group.add(jrCorelSquare);
        group.add(jrCorelRhombus);
        group.add(jrCorelCircle);
        group.add(jrHexagon);
        group.add(jrImage);

        // first row: basic models, second row: corel models
        LineLayout l1 = new LineLayout();
        l1.setPadding(new Insets(0, 0, 0, 0));
        JPanel panelModelRow1 = new JPanel(l1);
        JPanel panelModelRow2 = new JPanel(l1);

        panelModelRow1.add(jrDino);
        panelModelRow1.add(jrDisk);
        panelModelRow1.add(jrHexagon);
        panelModelRow1.add(jrImage);

        panelModelRow2.add(jrCorelTriangle);
        panelModelRow2.add(jrCorelSquare);
        panelModelRow2.add(jrCorelRhombus);
        panelModelRow2.add(jrCorelCircle);

        panelModel.add(panelModelRow1);
        panelModel.add(panelModelRow2);

        panelOption.add(panelModel);

        // model specific option
        JPanel panelModelOption = new JPanel(new FlowLayout(FlowLayout.LEADING));
        panelModelOption.setBorder(new TitledBorder("Model Options"));

        panelModelOption.add(chHexagonGrayRow);
        panelModelOption.add(cmdChooseImage);

        panelOption.add(panelModelOption);

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

    /**
     * Sample landscape image for the image color picker model
     */
    private Icon createSampleIcon() {
        int width = 320;
        int height = 200;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // sky
        g2.setPaint(new LinearGradientPaint(0, 0, 0, height * 0.7f, new float[]{0f, 0.6f, 1f},
                new Color[]{new Color(0x2B3A8C), new Color(0xE0679A), new Color(0xFFB45A)}));
        g2.fillRect(0, 0, width, height);

        // sun
        g2.setColor(new Color(0xFFE38A));
        g2.fill(new Ellipse2D.Float(190, 70, 70, 70));

        // hills
        g2.setColor(new Color(0x3F7D4E));
        Path2D back = new Path2D.Float();
        back.moveTo(0, 140);
        back.curveTo(80, 90, 160, 150, 320, 110);
        back.lineTo(320, 200);
        back.lineTo(0, 200);
        back.closePath();
        g2.fill(back);

        g2.setColor(new Color(0x24513A));
        Path2D front = new Path2D.Float();
        front.moveTo(0, 170);
        front.curveTo(100, 130, 220, 190, 320, 150);
        front.lineTo(320, 200);
        front.lineTo(0, 200);
        front.closePath();
        g2.fill(front);

        g2.dispose();
        return new ImageIcon(image);
    }

    public static void main(String[] args) {
        // UIScale.setZoomFactor(1.5f);
        FlatMacLightLaf.setup();
        EventQueue.invokeLater(() -> new TestColor().setVisible(true));
    }
}
