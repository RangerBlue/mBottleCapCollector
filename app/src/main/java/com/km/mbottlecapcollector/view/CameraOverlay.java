package com.km.mbottlecapcollector.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

import com.km.mbottlecapcollector.R;

public abstract class CameraOverlay extends View {
    private Paint paint = new Paint();
    private final double CIRCLE_SCREEN_RATIO = 1;
    private final double RING_SCREEN_RATIO = 0.05;
    private final double SIDE_BAR_SCREEN_RATIO = 0.1;
    private final double BOTTOM_BAR_SCREEN_RATIO = 0.2;
    private int radius;
    private int deviceWidth;
    private int deviceHeight;
    private Point circleCenter;


    public CameraOverlay(Context context, int deviceWidth, int deviceHeight) {
        super(context);
        this.deviceWidth = deviceWidth;
        this.deviceHeight = deviceHeight;
        this.radius = (int) (getCircleScreenRatio() * deviceWidth) / 2;
        this.circleCenter = new Point(deviceWidth / 2, deviceWidth / 2);
    }

    @Override
    public void onDraw(Canvas canvas) {
        setRingStyleData();
        drawRing(canvas);
        setBordersStyleData();
        drawBorders(canvas);
    }

    public void setRingStyleData() {
        paint.setColor(getResources().getColor(R.color.colorMainLayout));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
    }

    public void drawRing(Canvas canvas) {
        for (int i = 0; i < (deviceWidth * getRingScreenRatio()); i++) {
            canvas.drawCircle(circleCenter.x, circleCenter.y, radius + i, paint);
        }
    }

    public void setBordersStyleData() {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.colorBackground));
    }

    public void drawBorders(Canvas canvas) {
        float barWidth = (float) (getSideBarScreenRatio() * deviceWidth);
        drawLeftBar(canvas, barWidth);
        drawRightBar(canvas, barWidth);
        drawBottomBar(canvas, barWidth);
    }

    public void drawLeftBar(Canvas canvas, float barWidth) {
        canvas.drawRect(0, 0, barWidth, deviceHeight, paint);
    }

    public void drawRightBar(Canvas canvas, float barWidth) {
        canvas.drawRect((deviceWidth - barWidth), 0, deviceWidth, deviceHeight, paint);
    }

    public void drawBottomBar(Canvas canvas, float barWidth) {
        canvas.drawRect(barWidth, (float) (deviceHeight - (deviceHeight * getBottomBarScreenRatio())),
                deviceWidth - barWidth, deviceHeight, paint);
    }


    public double getRingScreenRatio() {
        return RING_SCREEN_RATIO;
    }

    public double getSideBarScreenRatio() {
        return SIDE_BAR_SCREEN_RATIO;
    }

    public double getBottomBarScreenRatio() {
        return BOTTOM_BAR_SCREEN_RATIO;
    }

    public double getCircleScreenRatio() {
        return CIRCLE_SCREEN_RATIO;
    }
}
