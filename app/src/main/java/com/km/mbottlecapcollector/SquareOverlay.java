package com.km.mbottlecapcollector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class SquareOverlay extends View {
    Paint paint = new Paint();

    public SquareOverlay(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6);
        Rect rect = new Rect(220, 200, 520, 500);
        canvas.drawRect(rect, paint );
    }
}
