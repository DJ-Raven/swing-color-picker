package raven.color.component.palette;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DefaultColorPaletteData implements ColorPaletteData {

    protected List<Color> listColors = new ArrayList<>();

    public DefaultColorPaletteData() {
        init();
    }

    protected void init() {

        listColors.add(new Color(239, 68, 68));   // red-500
        listColors.add(new Color(249, 115, 22));  // orange-500
        listColors.add(new Color(234, 179, 8));   // yellow-500
        listColors.add(new Color(132, 204, 22));  // lime-500
        listColors.add(new Color(34, 197, 94));   // green-500
        listColors.add(new Color(16, 185, 129));  // emerald-500
        listColors.add(new Color(20, 184, 166));  // teal-500
        listColors.add(new Color(6, 182, 212));   // cyan-500
        listColors.add(new Color(14, 165, 233));  // sky-500
        listColors.add(new Color(59, 130, 246));  // blue-500
        listColors.add(new Color(99, 102, 241));  // indigo-500
        listColors.add(new Color(139, 92, 246));  // violet-500
        listColors.add(new Color(168, 85, 247));  // purple-500
        listColors.add(new Color(217, 70, 239));  // fuchsia-500
        listColors.add(new Color(236, 72, 153));  // pink-500
        listColors.add(new Color(244, 63, 94));   // rose-500
        listColors.add(new Color(251, 146, 60));  // amber-500
        listColors.add(new Color(202, 138, 4));   // yellow-600
    }

    @Override
    public void add(Color color) {
    }

    @Override
    public void remove(Color color) {
    }

    @Override
    public void clear() {
    }

    @Override
    public int size() {
        return listColors.size();
    }

    @Override
    public Color get(int index) {
        return listColors.get(index);
    }
}
