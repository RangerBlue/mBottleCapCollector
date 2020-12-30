package com.km.mbottlecapcollector.util;

import android.content.res.Resources;
import android.os.Build;

public class ScreenRatioHelper {
    /**
     * Ratio of how much of screen width picture of cap would take
     */
    private static double CAP_STANDALONE_RATIO = 0.8;

    /**
     * Ratio of how much of screen width picture of cap would take in what cap you are screen
     */
    private static double CAP_WHAT_CAP_YOU_ARE_RATIO = 0.7;

    /**
     * Number of caps pictures in gallery row
     */
    public static int CAP_IN_ROW_AMOUNT = 4;

    /**
     * Width of cap in pixels in gallery row
     */
    private static double CAP_IN_ROW_RATIO = (1. / CAP_IN_ROW_AMOUNT) * (0.96);

    /**
     * Number of caps pictures in row in validate screen
     */
    public static int CAPS_IN_ROW_AMOUNT_VALIDATE = 3;

    /**
     * Width of cap in pixels in row of validate screen
     */
    private static double CAP_IN_ROW_VALIDATE_RATIO = (1. / CAPS_IN_ROW_AMOUNT_VALIDATE) * (0.95);

    /**
     * Ratio of caps gallery
     */
    private static double CAP_GALLERY_HEIGHT_RATIO = 0.90;

    /**
     * Width of default camera preview
     */
    public static int FULL_HD_WIDTH = 1920;

    /**
     * Height of default camera preview
     */
    public static int FULL_HD_HEIGHT = 1080;

    /**
     * Ratio of default camera preview
     */
    public static double IMAGE_RATIO = FULL_HD_WIDTH / (double) FULL_HD_HEIGHT;
    private static String SAMSUNG_MODEL = "SM-A505FN";


    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static int getStandaloneCapWidth() {
        return (int) (getScreenWidth() * CAP_STANDALONE_RATIO);
    }

    public static int getValidateCapWidth() {
        return (int) (getScreenWidth() * CAP_IN_ROW_VALIDATE_RATIO);
    }

    public static int getWhatCapAreYouCapWidth() {
        return (int) ((getScreenHeight() / 2) * CAP_WHAT_CAP_YOU_ARE_RATIO);
    }

    public static int getCapInRowWidth() {
        return (int) (getScreenWidth() * CAP_IN_ROW_RATIO);
    }

    public static int getNumberOfRows() {
        return (int) (getScreenHeight() * CAP_GALLERY_HEIGHT_RATIO / getCapInRowWidth());
    }

    /**
     * Workaround for certain models of Samsung where images are rotated after being taken, list of
     * model could be updated. Exif could not be used as we are not storing image bitmap to device
     * memory. If given model is in the list image is rotated.
     *
     * @return true if device is {@link #SAMSUNG_MODEL}, false otherwise
     */
    public static boolean isDeviceSMA50() {
        return Build.MODEL.equals(ScreenRatioHelper.SAMSUNG_MODEL);
    }

    public static boolean compareDouble(double x, double y) {
        return (Math.abs(x - y) < 0.01);
    }
}
