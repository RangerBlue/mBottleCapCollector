package com.km.mbottlecapcollector.util;

import android.content.res.Resources;

public class ScreenRatioHelper {
    private static double capStandaloneRatio = 0.8;
    public static int capsInRowAmount = 4;
    private static double capInRowRatio = (1. / 4) * (0.96);

    public static int getScreenWidth() {
        System.out.println("Width" + Resources.getSystem().getDisplayMetrics().widthPixels);
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        System.out.println("Height" + Resources.getSystem().getDisplayMetrics().heightPixels);
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static int getStandaloneCapWidth() {
        return (int) (getScreenWidth() * capStandaloneRatio);
    }

    public static int getCapInRowWidth() {
        return (int) (getScreenWidth() * capInRowRatio);
    }
}
