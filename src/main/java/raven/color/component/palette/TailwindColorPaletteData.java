package raven.color.component.palette;

import java.awt.*;

public class TailwindColorPaletteData extends DefaultColorPaletteData {

    @Override
    protected void init() {
        // The Tailwind CSS v1.0 color palettes

        // Red
        listColors.add(new Color(254, 226, 226)); // red-100
        listColors.add(new Color(254, 202, 202)); // red-200
        listColors.add(new Color(252, 165, 165)); // red-300
        listColors.add(new Color(248, 113, 113)); // red-400
        listColors.add(new Color(239, 68, 68));   // red-500
        listColors.add(new Color(220, 38, 38));   // red-600
        listColors.add(new Color(185, 28, 28));   // red-700
        listColors.add(new Color(153, 27, 27));   // red-800
        listColors.add(new Color(127, 29, 29));   // red-900

        // Orange
        listColors.add(new Color(255, 237, 213)); // orange-100
        listColors.add(new Color(254, 215, 170)); // orange-200
        listColors.add(new Color(253, 186, 116)); // orange-300
        listColors.add(new Color(251, 146, 60));  // orange-400
        listColors.add(new Color(249, 115, 22));  // orange-500
        listColors.add(new Color(234, 88, 12));   // orange-600
        listColors.add(new Color(194, 65, 12));   // orange-700
        listColors.add(new Color(154, 52, 18));   // orange-800
        listColors.add(new Color(124, 45, 18));   // orange-900

        // Yellow
        listColors.add(new Color(254, 249, 195)); // yellow-100
        listColors.add(new Color(254, 240, 138)); // yellow-200
        listColors.add(new Color(253, 224, 71));  // yellow-300
        listColors.add(new Color(250, 204, 21));  // yellow-400
        listColors.add(new Color(234, 179, 8));   // yellow-500
        listColors.add(new Color(202, 138, 4));   // yellow-600
        listColors.add(new Color(161, 98, 7));    // yellow-700
        listColors.add(new Color(133, 77, 14));   // yellow-800
        listColors.add(new Color(113, 63, 18));   // yellow-900

        // Green
        listColors.add(new Color(220, 252, 231)); // green-100
        listColors.add(new Color(187, 247, 208)); // green-200
        listColors.add(new Color(134, 239, 172)); // green-300
        listColors.add(new Color(74, 222, 128));  // green-400
        listColors.add(new Color(34, 197, 94));   // green-500
        listColors.add(new Color(22, 163, 74));   // green-600
        listColors.add(new Color(21, 128, 61));   // green-700
        listColors.add(new Color(22, 101, 52));   // green-800
        listColors.add(new Color(20, 83, 45));    // green-900

        // Teal
        listColors.add(new Color(204, 251, 241)); // teal-100
        listColors.add(new Color(153, 246, 228)); // teal-200
        listColors.add(new Color(94, 234, 212));  // teal-300
        listColors.add(new Color(45, 212, 191));  // teal-400
        listColors.add(new Color(20, 184, 166));  // teal-500
        listColors.add(new Color(13, 148, 136));  // teal-600
        listColors.add(new Color(15, 118, 110));  // teal-700
        listColors.add(new Color(17, 94, 89));    // teal-800
        listColors.add(new Color(19, 78, 74));    // teal-900

        // Blue
        listColors.add(new Color(219, 234, 254)); // blue-100
        listColors.add(new Color(191, 219, 254)); // blue-200
        listColors.add(new Color(147, 197, 253)); // blue-300
        listColors.add(new Color(96, 165, 250));  // blue-400
        listColors.add(new Color(59, 130, 246));  // blue-500
        listColors.add(new Color(37, 99, 235));   // blue-600
        listColors.add(new Color(29, 78, 216));   // blue-700
        listColors.add(new Color(30, 64, 175));   // blue-800
        listColors.add(new Color(30, 58, 138));   // blue-900

        // Indigo
        listColors.add(new Color(224, 231, 255)); // indigo-100
        listColors.add(new Color(199, 210, 254)); // indigo-200
        listColors.add(new Color(165, 180, 252)); // indigo-300
        listColors.add(new Color(129, 140, 248)); // indigo-400
        listColors.add(new Color(99, 102, 241));  // indigo-500
        listColors.add(new Color(79, 70, 229));   // indigo-600
        listColors.add(new Color(67, 56, 202));   // indigo-700
        listColors.add(new Color(55, 48, 163));   // indigo-800
        listColors.add(new Color(49, 46, 129));   // indigo-900

        // Purple
        listColors.add(new Color(243, 232, 255)); // purple-100
        listColors.add(new Color(233, 213, 255)); // purple-200
        listColors.add(new Color(216, 180, 254)); // purple-300
        listColors.add(new Color(192, 132, 252)); // purple-400
        listColors.add(new Color(168, 85, 247));  // purple-500
        listColors.add(new Color(147, 51, 234));  // purple-600
        listColors.add(new Color(126, 34, 206));  // purple-700
        listColors.add(new Color(107, 33, 168));  // purple-800
        listColors.add(new Color(88, 28, 135));   // purple-900

        // Pink
        listColors.add(new Color(252, 231, 243)); // pink-100
        listColors.add(new Color(251, 207, 232)); // pink-200
        listColors.add(new Color(249, 168, 212)); // pink-300
        listColors.add(new Color(244, 114, 182)); // pink-400
        listColors.add(new Color(236, 72, 153));  // pink-500
        listColors.add(new Color(219, 39, 119));  // pink-600
        listColors.add(new Color(190, 24, 93));   // pink-700
        listColors.add(new Color(157, 23, 77));   // pink-800
        listColors.add(new Color(131, 24, 67));   // pink-900

        // Gray
        listColors.add(new Color(243, 244, 246)); // gray-100
        listColors.add(new Color(229, 231, 235)); // gray-200
        listColors.add(new Color(209, 213, 219)); // gray-300
        listColors.add(new Color(156, 163, 175)); // gray-400
        listColors.add(new Color(107, 114, 128)); // gray-500
        listColors.add(new Color(75, 85, 99));    // gray-600
        listColors.add(new Color(55, 65, 81));    // gray-700
        listColors.add(new Color(31, 41, 55));    // gray-800
        listColors.add(new Color(17, 24, 39));    // gray-900
    }

    public ColorPaletteItemPainter getPainter() {
        return new DefaultColorPaletteItemPainter() {
            private final Dimension itemSize = new Dimension(28, 15);

            @Override
            public Dimension getItemSize() {
                return itemSize;
            }

            @Override
            public int getMaxRow() {
                return 10;
            }

            @Override
            protected float getArc() {
                return 0;
            }

            @Override
            public int getItemGap() {
                return 0;
            }

            @Override
            protected Color getBorderColor(Color color) {
                float[] hub = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
                return Color.getHSBColor(hub[0], 1f, 1f);
            }
        };
    }
}
