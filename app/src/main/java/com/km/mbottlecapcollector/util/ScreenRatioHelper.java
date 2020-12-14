package com.km.mbottlecapcollector.util;

import android.content.res.Resources;

public class ScreenRatioHelper {
    private static double capStandaloneRatio = 0.8;
    public static int capsInRowAmount = 4;
    private static double capInRowRatio = (1. / capsInRowAmount) * (0.96);
    public static int capsInRowAmountValidate = 3;
    private static double capInRowValidateRatio = (1. / capsInRowAmountValidate) * (0.95);

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static int getStandaloneCapWidth() {
        return (int) (getScreenWidth() * capStandaloneRatio);
    }

    public static int getValidateCapWidth() {
        return (int) (getScreenWidth() * capInRowValidateRatio);
    }

    public static int getCapInRowWidth() {
        return (int) (getScreenWidth() * capInRowRatio);
    }
}
