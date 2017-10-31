package com.example.eslong.jsketch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class LinePreview extends View
{
    Model model;
    Paint paint;

    public LinePreview(Context context)
    {
        super(context);
        paint = new Paint();
        invalidate();
    }

    public LinePreview(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        paint = new Paint();
        invalidate();
    }

    public LinePreview(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        paint = new Paint();
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        paint.setStrokeWidth(model.lineThickness);
        canvas.drawLine(20 , this.getHeight() / 2, this.getWidth() - 20, this.getHeight() / 2, paint);
    }

    public void setModel(Model model)
    {
        this.model = model;
    }
}
