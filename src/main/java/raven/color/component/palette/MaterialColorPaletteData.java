package raven.color.component.palette;

import java.awt.*;

public class MaterialColorPaletteData extends TailwindColorPaletteData {

    @Override
    protected void init() {
        // The Material Design color palette

        // Red
        listColors.add(new Color(255, 235, 238)); // red-50
        listColors.add(new Color(255, 205, 210)); // red-100
        listColors.add(new Color(239, 154, 154)); // red-200
        listColors.add(new Color(229, 115, 115)); // red-300
        listColors.add(new Color(239, 83, 80));   // red-400
        listColors.add(new Color(244, 67, 54));   // red-500
        listColors.add(new Color(229, 57, 53));   // red-600
        listColors.add(new Color(211, 47, 47));   // red-700
        listColors.add(new Color(198, 40, 40));   // red-800
        listColors.add(new Color(183, 28, 28));   // red-900

        // Pink
        listColors.add(new Color(252, 228, 236)); // pink-50
        listColors.add(new Color(248, 187, 208)); // pink-100
        listColors.add(new Color(244, 143, 177)); // pink-200
        listColors.add(new Color(240, 98, 146));  // pink-300
        listColors.add(new Color(236, 64, 122));  // pink-400
        listColors.add(new Color(233, 30, 99));   // pink-500
        listColors.add(new Color(216, 27, 96));   // pink-600
        listColors.add(new Color(194, 24, 91));   // pink-700
        listColors.add(new Color(173, 20, 87));   // pink-800
        listColors.add(new Color(136, 14, 79));   // pink-900

        // Purple
        listColors.add(new Color(243, 229, 245)); // purple-50
        listColors.add(new Color(225, 190, 231)); // purple-100
        listColors.add(new Color(206, 147, 216)); // purple-200
        listColors.add(new Color(186, 104, 200)); // purple-300
        listColors.add(new Color(171, 71, 188));  // purple-400
        listColors.add(new Color(156, 39, 176));  // purple-500
        listColors.add(new Color(142, 36, 170));  // purple-600
        listColors.add(new Color(123, 31, 162));  // purple-700
        listColors.add(new Color(106, 27, 154));  // purple-800
        listColors.add(new Color(74, 20, 140));   // purple-900

        // Deep Purple
        listColors.add(new Color(237, 231, 246)); // deep-purple-50
        listColors.add(new Color(209, 196, 233)); // deep-purple-100
        listColors.add(new Color(179, 157, 219)); // deep-purple-200
        listColors.add(new Color(149, 117, 205)); // deep-purple-300
        listColors.add(new Color(126, 87, 194));  // deep-purple-400
        listColors.add(new Color(103, 58, 183));  // deep-purple-500
        listColors.add(new Color(94, 53, 177));   // deep-purple-600
        listColors.add(new Color(81, 45, 168));   // deep-purple-700
        listColors.add(new Color(69, 39, 160));   // deep-purple-800
        listColors.add(new Color(49, 27, 146));   // deep-purple-900

        // Indigo
        listColors.add(new Color(232, 234, 246)); // indigo-50
        listColors.add(new Color(197, 202, 233)); // indigo-100
        listColors.add(new Color(159, 168, 218)); // indigo-200
        listColors.add(new Color(121, 134, 203)); // indigo-300
        listColors.add(new Color(92, 107, 192));  // indigo-400
        listColors.add(new Color(63, 81, 181));   // indigo-500
        listColors.add(new Color(57, 73, 171));   // indigo-600
        listColors.add(new Color(48, 63, 159));   // indigo-700
        listColors.add(new Color(40, 53, 147));   // indigo-800
        listColors.add(new Color(26, 35, 126));   // indigo-900

        // Blue
        listColors.add(new Color(227, 242, 253)); // blue-50
        listColors.add(new Color(187, 222, 251)); // blue-100
        listColors.add(new Color(144, 202, 249)); // blue-200
        listColors.add(new Color(100, 181, 246)); // blue-300
        listColors.add(new Color(66, 165, 245));  // blue-400
        listColors.add(new Color(33, 150, 243));  // blue-500
        listColors.add(new Color(30, 136, 229));  // blue-600
        listColors.add(new Color(25, 118, 210));  // blue-700
        listColors.add(new Color(21, 101, 192));  // blue-800
        listColors.add(new Color(13, 71, 161));   // blue-900

        // Light Blue
        listColors.add(new Color(225, 245, 254)); // light-blue-50
        listColors.add(new Color(179, 229, 252)); // light-blue-100
        listColors.add(new Color(129, 212, 250)); // light-blue-200
        listColors.add(new Color(79, 195, 247));  // light-blue-300
        listColors.add(new Color(41, 182, 246));  // light-blue-400
        listColors.add(new Color(3, 169, 244));   // light-blue-500
        listColors.add(new Color(3, 155, 229));   // light-blue-600
        listColors.add(new Color(2, 136, 209));   // light-blue-700
        listColors.add(new Color(2, 119, 189));   // light-blue-800
        listColors.add(new Color(1, 87, 155));    // light-blue-900

        // Cyan
        listColors.add(new Color(224, 247, 250)); // cyan-50
        listColors.add(new Color(178, 235, 242)); // cyan-100
        listColors.add(new Color(128, 222, 234)); // cyan-200
        listColors.add(new Color(77, 208, 225));  // cyan-300
        listColors.add(new Color(38, 198, 218));  // cyan-400
        listColors.add(new Color(0, 188, 212));   // cyan-500
        listColors.add(new Color(0, 172, 193));   // cyan-600
        listColors.add(new Color(0, 151, 167));   // cyan-700
        listColors.add(new Color(0, 131, 143));   // cyan-800
        listColors.add(new Color(0, 96, 100));    // cyan-900

        // Teal
        listColors.add(new Color(224, 242, 241)); // teal-50
        listColors.add(new Color(178, 223, 219)); // teal-100
        listColors.add(new Color(128, 203, 196)); // teal-200
        listColors.add(new Color(77, 182, 172));  // teal-300
        listColors.add(new Color(38, 166, 154));  // teal-400
        listColors.add(new Color(0, 150, 136));   // teal-500
        listColors.add(new Color(0, 137, 123));   // teal-600
        listColors.add(new Color(0, 121, 107));   // teal-700
        listColors.add(new Color(0, 105, 92));    // teal-800
        listColors.add(new Color(0, 77, 64));     // teal-900

        // Green
        listColors.add(new Color(232, 245, 233)); // green-50
        listColors.add(new Color(200, 230, 201)); // green-100
        listColors.add(new Color(165, 214, 167)); // green-200
        listColors.add(new Color(129, 199, 132)); // green-300
        listColors.add(new Color(102, 187, 106)); // green-400
        listColors.add(new Color(76, 175, 80));   // green-500
        listColors.add(new Color(67, 160, 71));   // green-600
        listColors.add(new Color(56, 142, 60));   // green-700
        listColors.add(new Color(46, 125, 50));   // green-800
        listColors.add(new Color(27, 94, 32));    // green-900

        // Light Green
        listColors.add(new Color(241, 248, 233)); // light-green-50
        listColors.add(new Color(220, 237, 200)); // light-green-100
        listColors.add(new Color(197, 225, 165)); // light-green-200
        listColors.add(new Color(174, 213, 129)); // light-green-300
        listColors.add(new Color(156, 204, 101)); // light-green-400
        listColors.add(new Color(139, 195, 74));  // light-green-500
        listColors.add(new Color(124, 179, 66));  // light-green-600
        listColors.add(new Color(104, 159, 56));  // light-green-700
        listColors.add(new Color(85, 139, 47));   // light-green-800
        listColors.add(new Color(51, 105, 30));   // light-green-900

        // Lime
        listColors.add(new Color(249, 251, 231)); // lime-50
        listColors.add(new Color(240, 244, 195)); // lime-100
        listColors.add(new Color(230, 238, 156)); // lime-200
        listColors.add(new Color(220, 231, 117)); // lime-300
        listColors.add(new Color(212, 225, 87));  // lime-400
        listColors.add(new Color(205, 220, 57));  // lime-500
        listColors.add(new Color(192, 202, 51));  // lime-600
        listColors.add(new Color(175, 180, 43));  // lime-700
        listColors.add(new Color(158, 157, 36));  // lime-800
        listColors.add(new Color(130, 119, 23));  // lime-900

        // Yellow
        listColors.add(new Color(255, 253, 231)); // yellow-50
        listColors.add(new Color(255, 249, 196)); // yellow-100
        listColors.add(new Color(255, 245, 157)); // yellow-200
        listColors.add(new Color(255, 241, 118)); // yellow-300
        listColors.add(new Color(255, 238, 88));  // yellow-400
        listColors.add(new Color(255, 235, 59));  // yellow-500
        listColors.add(new Color(253, 216, 53));  // yellow-600
        listColors.add(new Color(251, 192, 45));  // yellow-700
        listColors.add(new Color(249, 168, 37));  // yellow-800
        listColors.add(new Color(245, 127, 23));  // yellow-900

        // Amber
        listColors.add(new Color(255, 248, 225)); // amber-50
        listColors.add(new Color(255, 236, 179)); // amber-100
        listColors.add(new Color(255, 224, 130)); // amber-200
        listColors.add(new Color(255, 213, 79));  // amber-300
        listColors.add(new Color(255, 202, 40));  // amber-400
        listColors.add(new Color(255, 193, 7));   // amber-500
        listColors.add(new Color(255, 179, 0));   // amber-600
        listColors.add(new Color(255, 160, 0));   // amber-700
        listColors.add(new Color(255, 143, 0));   // amber-800
        listColors.add(new Color(255, 111, 0));   // amber-900

        // Orange
        listColors.add(new Color(255, 243, 224)); // orange-50
        listColors.add(new Color(255, 224, 178)); // orange-100
        listColors.add(new Color(255, 204, 128)); // orange-200
        listColors.add(new Color(255, 183, 77));  // orange-300
        listColors.add(new Color(255, 167, 38));  // orange-400
        listColors.add(new Color(255, 152, 0));   // orange-500
        listColors.add(new Color(251, 140, 0));   // orange-600
        listColors.add(new Color(245, 124, 0));   // orange-700
        listColors.add(new Color(239, 108, 0));   // orange-800
        listColors.add(new Color(230, 81, 0));    // orange-900

        // Deep Orange
        listColors.add(new Color(251, 233, 231)); // deep-orange-50
        listColors.add(new Color(255, 204, 188)); // deep-orange-100
        listColors.add(new Color(255, 171, 145)); // deep-orange-200
        listColors.add(new Color(255, 138, 101)); // deep-orange-300
        listColors.add(new Color(255, 112, 67));  // deep-orange-400
        listColors.add(new Color(255, 87, 34));   // deep-orange-500
        listColors.add(new Color(244, 81, 30));   // deep-orange-600
        listColors.add(new Color(230, 74, 25));   // deep-orange-700
        listColors.add(new Color(216, 67, 21));   // deep-orange-800
        listColors.add(new Color(191, 54, 12));   // deep-orange-900

        // Brown
        listColors.add(new Color(239, 235, 233)); // brown-50
        listColors.add(new Color(215, 204, 200)); // brown-100
        listColors.add(new Color(188, 170, 164)); // brown-200
        listColors.add(new Color(161, 136, 127)); // brown-300
        listColors.add(new Color(141, 110, 99));  // brown-400
        listColors.add(new Color(121, 85, 72));   // brown-500
        listColors.add(new Color(109, 76, 65));   // brown-600
        listColors.add(new Color(93, 64, 55));    // brown-700
        listColors.add(new Color(78, 52, 46));    // brown-800
        listColors.add(new Color(62, 39, 35));    // brown-900

        // Grey
        listColors.add(new Color(250, 250, 250)); // grey-50
        listColors.add(new Color(245, 245, 245)); // grey-100
        listColors.add(new Color(238, 238, 238)); // grey-200
        listColors.add(new Color(224, 224, 224)); // grey-300
        listColors.add(new Color(189, 189, 189)); // grey-400
        listColors.add(new Color(158, 158, 158)); // grey-500
        listColors.add(new Color(117, 117, 117)); // grey-600
        listColors.add(new Color(97, 97, 97));    // grey-700
        listColors.add(new Color(66, 66, 66));    // grey-800
        listColors.add(new Color(33, 33, 33));    // grey-900

        // Blue Grey
        listColors.add(new Color(236, 239, 241)); // blue-grey-50
        listColors.add(new Color(207, 216, 220)); // blue-grey-100
        listColors.add(new Color(176, 190, 197)); // blue-grey-200
        listColors.add(new Color(144, 164, 174)); // blue-grey-300
        listColors.add(new Color(120, 144, 156)); // blue-grey-400
        listColors.add(new Color(96, 125, 139));  // blue-grey-500
        listColors.add(new Color(84, 110, 122));  // blue-grey-600
        listColors.add(new Color(69, 90, 100));   // blue-grey-700
        listColors.add(new Color(55, 71, 79));    // blue-grey-800
        listColors.add(new Color(38, 50, 56));    // blue-grey-900
    }


    public ColorPaletteItemPainter getPainter() {

        return new DefaultColorPaletteItemPainter() {
            private final Dimension itemSize = new Dimension(26, 15);

            @Override
            public Dimension getItemSize() {
                return itemSize;
            }

            @Override
            public int getMaxRow() {
                return 19;
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
