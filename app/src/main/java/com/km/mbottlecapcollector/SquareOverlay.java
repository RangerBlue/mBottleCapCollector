package com.km.mbottlecapcollector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

public class SquareOverlay extends View {
    private Paint paint = new Paint();
    private double circleScreeRatio = 0.3;
    private double ringScreenRatio = 0.05;
    private double sideBarScreenRatio = 0.1;
    private double bottomBarScreenRatio = 0.2;
    private Rect rect;
    private int radius;
    private int deviceWidth;
    private int deviceHeight;
    private Point circleCenter;


    public SquareOverlay(Context context, int deviceWidth, int deviceHeight) {
        super(context);
        this.deviceWidth = deviceWidth;
        this.deviceHeight = deviceHeight;
        rect = new Rect(220, 200, 520, 500);
        radius = (int) (circleScreeRatio * deviceWidth) / 2;
        circleCenter = new Point(deviceWidth / 2, deviceWidth / 2);
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(getResources().getColor(R.color.colorMainLayout));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        //Ring
        for (int i = 0; i < (deviceWidth * ringScreenRatio); i++) {
            canvas.drawCircle(circleCenter.x, circleCenter.y, radius + i, paint);
        }
        //Left bar
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.colorBackground));
        float barWidth = (float) (sideBarScreenRatio * deviceWidth);
        canvas.drawRect(0, 0, barWidth, deviceHeight, paint);
        //Right bar
        canvas.drawRect((deviceWidth - barWidth), 0, deviceWidth, deviceHeight, paint);
        //Bottom part
        canvas.drawRect(barWidth, (float) (deviceHeight - (deviceHeight * bottomBarScreenRatio)), deviceWidth - barWidth, deviceHeight, paint);
    }

    public Rect getRect() {
        return rect;
    }

    public double getCircleScreenRatio() {
        return circleScreeRatio;
    }
}
