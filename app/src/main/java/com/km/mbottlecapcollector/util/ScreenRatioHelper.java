package com.km.mbottlecapcollector.util;

import android.content.res.Resources;
import android.os.Build;

public class ScreenRatioHelper {
    private static double capStandaloneRatio = 0.8;
    public static int capsInRowAmount = 4;
    private static double capInRowRatio = (1. / capsInRowAmount) * (0.96);
    public static int capsInRowAmountValidate = 3;
    private static double capInRowValidateRatio = (1. / capsInRowAmountValidate) * (0.95);
    private static String SAMSUNG_MODEL = "SM-A505FN";
    public static int FULL_HD_WIDTH = 1920;
    public static int FULL_HD_HEIGHT = 1080;
    public static double IMAGE_RATIO = FULL_HD_WIDTH / (double) FULL_HD_HEIGHT;

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

    /**
     * Workaround for certain models of Samsung where images are rotated after being taken, list of
     * model could be updated. Exif could not be used as we are not storing image bitmap to device
     * memory. If given model is in the list image is rotated.
     *
     * @return
     */
    public static boolean isDeviceSMA50() {
        return Build.MODEL.equals(ScreenRatioHelper.SAMSUNG_MODEL);
    }

    public static boolean compareDouble(double x, double y) {
        return (Math.abs(x - y) < 0.01);
    }
}
