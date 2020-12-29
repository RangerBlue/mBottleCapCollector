package com.km.mbottlecapcollector.view;

import android.content.Context;

public class BackCameraOverlay extends CameraOverlay {
    private static double circleScreenRatio = 0.3;

    public BackCameraOverlay(Context context, int deviceWidth, int deviceHeight) {
        super(context, deviceWidth, deviceHeight);
    }

    @Override
    public double getCircleScreenRatio() {
        return circleScreenRatio;
    }
}
