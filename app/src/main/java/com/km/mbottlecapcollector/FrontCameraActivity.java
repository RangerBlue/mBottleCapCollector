package com.km.mbottlecapcollector;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.camera2.CameraAccessException;
import android.os.Bundle;

import com.km.mbottlecapcollector.util.FileHelper;
import com.km.mbottlecapcollector.util.ScreenRatioHelper;
import com.km.mbottlecapcollector.view.CameraOverlay;
import com.km.mbottlecapcollector.view.FrontCameraOverlay;

public class FrontCameraActivity extends CameraActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public CameraOverlay initializeOverlay() {
        return new FrontCameraOverlay(getApplicationContext(), deviceWidth, ScreenRatioHelper.getScreenHeight());
    }

    @Override
    public String getCameraNumber() throws CameraAccessException {
        String[] cameraList = manager.getCameraIdList();
        if (cameraList.length == 1) {
            return cameraList[0];
        } else {
            return cameraList[1];
        }
    }

    @Override
    public Bitmap rotateImage(Bitmap originalBitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(180);
        originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0,
                originalBitmap.getWidth(), originalBitmap.getHeight(), matrix,
                true);
        //mirror
        matrix.preScale(1.0f, -1.0f);
        matrix.postRotate(0);
        originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0,
                originalBitmap.getWidth(), originalBitmap.getHeight(), matrix,
                true);
        return originalBitmap;
    }

    @Override
    public double getCircleScreenRatio() {
        return screenSquare.getCircleScreenRatio();
    }

    @Override
    public String getFileSuffix() {
        return FileHelper.SELFIE_PREFIX;
    }

}