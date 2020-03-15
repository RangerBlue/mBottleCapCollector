package com.km.mbottlecapcollector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

public class SquareOverlay extends View {
    private Paint paint = new Paint();
    private double ratio = 0.3;
    private Rect rect;
    private int radius;
    private int deviceWidth;
    private Point circleCenter;


    public SquareOverlay(Context context, int deviceWidth) {
        super(context);
        this.deviceWidth = deviceWidth;
        rect = new Rect(220, 200, 520, 500);
        radius = (int)(ratio*deviceWidth)/2;
        circleCenter = new Point(deviceWidth/2,deviceWidth/2);
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        for(int i=0; i<deviceWidth ; i++){
            canvas.drawCircle(circleCenter.x, circleCenter.y, radius+i, paint);
        }
    }

    public Rect getRect() {
        return rect;
    }

    public double getRatio() {
        return ratio;
    }
}
