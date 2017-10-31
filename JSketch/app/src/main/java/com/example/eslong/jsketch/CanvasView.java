package com.example.eslong.jsketch;

import android.view.*;
import android.graphics.*;
import android.content.Context;
import android.util.AttributeSet;

public class CanvasView extends View
{
    Model model;
    Paint paint;

    public CanvasView(Context context)
    {
        super(context);
        paint = new Paint();
        invalidate();
    }

    public CanvasView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        paint = new Paint();
        invalidate();
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        paint = new Paint();
        invalidate();
    }

    // Handle all touch events on canvas (for drawing, selecting, erasing, and moving
    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        int action = e.getAction();
        PointF p = new PointF(e.getX(), e.getY());

        if (action == MotionEvent.ACTION_DOWN)
        {
            if (model.currTool == Tool.SELECT)
            {
                model.checkSelect(p);
                if (model.currShape != null)
                {
                    model.dragStart = p; // if shape selected, set drag starting point
                    model.changeColor(model.currColor); // change line color of selected shape to currently selected color
                }
            }
            else if (model.currTool == Tool.LINE || model.currTool == Tool.RECT || model.currTool == Tool.OVAL)
            {
                model.dragStart = p;

                model.createShape(p);
            }
            else if (model.currTool == Tool.ERASE)
            {
                model.checkErase(p);
            }
            else if (model.currTool == Tool.FILL)
            {
                model.checkFill(p);
            }
        }
        else if (action == MotionEvent.ACTION_MOVE)
        {
            if (model.currTool == Tool.SELECT && model.currShape != null && model.dragStart != null)
            {
                model.moveShape(p);
            }
            else if (model.currTool == Tool.LINE || model.currTool == Tool.RECT || model.currTool == Tool.OVAL)
            {
                model.dragShape(p);
            }
        }
        else if (action == MotionEvent.ACTION_UP)
        {
            if (model.currTool == Tool.SELECT && model.currShape != null && model.dragStart != null)
            {
                model.setTranslatedPoints();
            }
            else if (model.currTool == Tool.LINE || model.currTool == Tool.RECT || model.currTool == Tool.OVAL)
            {
                model.addDrawnShape();
            }

            model.dragStart = null;
        }

        invalidate();

        return true;
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        paint.setStyle(Paint.Style.STROKE); // set unfilled drawing style

        // Draw all stored shapes
        for (int i = 0; i < model.shapes.size(); i++)
        {
            Shapes shape = model.shapes.get(i);
            paint.setStrokeWidth(shape.lineThickness); // set line thickness
            paint.setColor(shape.lineColor); // set line color
            drawShape(canvas, shape);
            paint.setPathEffect(null);
        }

        // Draw current shape
        if (model.currShape != null)
        {
            paint.setStrokeWidth(model.currShape.lineThickness); // set line thickness
            paint.setColor(model.currShape.lineColor); // set line color
            drawShape(canvas, model.currShape);
            paint.setPathEffect(null);
        }
    }

    // handle drawing of stored shape
    public void drawShape(Canvas canvas, Shapes shape)
    {
        if (shape instanceof Line) // Line
        {
            Line line = (Line)shape;

            if (model.currShape != null && model.currShape.equals(line) && !model.isDrawing)
            {
                // set stroked border
                paint.setPathEffect(new DashPathEffect(new float[] {10,20}, 0));
            }

            canvas.drawLine(line.origin.x, line.origin.y, line.endPoint.x, line.endPoint.y, paint);
        }
        else if (shape instanceof Rectangle) // Rectangle
        {
            Rectangle rect = (Rectangle)shape;

            if (rect.fillColor != 0)
            {
                paint.setColor(rect.fillColor);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawRect(rect.shapeToDraw, paint);
                paint.setColor(rect.lineColor);
                paint.setStyle(Paint.Style.STROKE);
            }

            if (model.currShape != null && model.currShape.equals(rect) && !model.isDrawing)
            {
                // Set stroked border
                paint.setPathEffect(new DashPathEffect(new float[] {10,20}, 0));
            }

            canvas.drawRect(rect.shapeToDraw, paint);
        }
        else if (shape instanceof Oval) // Oval
        {
            Oval oval = (Oval)shape;

            if (oval.fillColor != 0)
            {
                paint.setColor(oval.fillColor);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawOval(oval.shapeToDraw, paint);
                paint.setColor(oval.lineColor);
                paint.setStyle(Paint.Style.STROKE);
            }

            if (model.currShape != null && model.currShape.equals(oval) && !model.isDrawing)
            {
                // set stroked border
                paint.setPathEffect(new DashPathEffect(new float[] {10,20}, 0));
            }

            canvas.drawOval(oval.shapeToDraw, paint);
        }
    }

    // Set reference to model
    public void setModel(Model m)
    {
        this.model = m;
    }
}
