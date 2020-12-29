package com.km.mbottlecapcollector;

import android.graphics.Bitmap;
import android.hardware.camera2.CameraAccessException;
import android.os.Bundle;

import com.km.mbottlecapcollector.util.ScreenRatioHelper;
import com.km.mbottlecapcollector.view.BackCameraOverlay;
import com.km.mbottlecapcollector.view.CameraOverlay;

public class BackCameraActivity extends CameraActivity{
    private static final String TAG = BackCameraActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public CameraOverlay initializeOverlay() {
        return new BackCameraOverlay(getApplicationContext(), deviceWidth, ScreenRatioHelper.getScreenHeight());
    }

    @Override
    public String getCameraNumber() throws CameraAccessException {
        return manager.getCameraIdList()[0];
    }

    @Override
    public Bitmap rotateImage(Bitmap originalBitmap) {
        return originalBitmap;
    }

    @Override
    public double getCircleScreenRatio() {
        return screenSquare.getCircleScreenRatio();
    }

    @Override
    public String getFileSuffix() {
        return "cap";
    }
}