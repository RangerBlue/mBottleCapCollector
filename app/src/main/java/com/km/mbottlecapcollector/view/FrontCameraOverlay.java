package com.km.mbottlecapcollector.view;

import android.content.Context;

public class FrontCameraOverlay extends CameraOverlay {
    private static double CIRCLE_SCREEN_RATIO = 0.6;

    public FrontCameraOverlay(Context context, int deviceWidth, int deviceHeight) {
        super(context, deviceWidth, deviceHeight);
    }

    @Override
    public double getCircleScreenRatio() {
        return CIRCLE_SCREEN_RATIO;
    }
}
