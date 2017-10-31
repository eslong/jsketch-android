package com.example.eslong.jsketch;

import java.util.*;
import android.graphics.*;

public class Model
{
    ArrayList<Shapes> shapes;
    int currColor;
    Tool currTool;
    int lineThickness;
    Shapes currShape;
    PointF dragStart;
    boolean isDrawing = false;
    float translateX, translateY;
    CanvasView canvas;

    Model()
    {
        shapes = new ArrayList<Shapes>();
        currColor = Color.BLACK;
        currTool = Tool.SELECT;
        lineThickness = 3;
    }

    // method that creates a shape once a click is detected and the proper tool is selected
    public void createShape(PointF p1)
    {
        // create new shape based on the current selected tool
        if (currTool == Tool.LINE)
        {
            currShape = (Shapes)(new Line(p1, p1, currColor, lineThickness));
        }
        else if (currTool == Tool.RECT)
        {
            currShape = (Shapes)(new Rectangle(p1, p1, currColor, lineThickness));
        }
        else if (currTool == Tool.OVAL)
        {
            currShape = (Shapes)(new Oval(p1, 1, currColor, lineThickness));
        }
        isDrawing = true;
    }

    // method that checks which shape, if any, was selected
    public void checkSelect(PointF p)
    {
        currShape = null;

        for (int i = 0; i < shapes.size(); i++)
        {
            if (shapes.get(i) instanceof Line)
            {
                Line line = (Line) shapes.get(i);
                if (line.intersects(p))
                {
                    currShape = shapes.get(i);
                }
            }
            else if (shapes.get(i).shapeToDraw.contains(p.x, p.y))
            {
                currShape = shapes.get(i);
            }
        }
    }

    // method that fills selected shape with current color
    public void checkFill(PointF p)
    {
        checkSelect(p);

        if (currShape == null) return;

        if (currShape instanceof Rectangle) 
        {
            Rectangle rect = (Rectangle)currShape;
            rect.setFillColor(currColor);
            currShape = rect;
        }
        else if (currShape instanceof Oval)
        {
            Oval oval = (Oval)currShape;
            oval.setFillColor(currColor);
            currShape = oval;
        }

        currShape = null;
    }

    // method that erases shape if it is clicked
    public void checkErase(PointF p)
    {
        checkSelect(p);

        if (currShape == null) return;
        else shapes.remove(currShape);

        currShape = null;
    }

    // method that draws shape as mouse is dragged
    public void dragShape(PointF p2)
    {
        // as mouse is dragged, the endPoint is updated to the current mouse position
        if (currTool == Tool.LINE)
        {
            currShape = new Line(currShape.origin, p2, currColor, lineThickness);
        }
        else if (currTool == Tool.RECT)
        {
            currShape = new Rectangle(getNewOriginRect(p2), getNewEndPointRect(p2), currColor, lineThickness);
        }
        else if (currTool == Tool.OVAL)
        {
            currShape = new Oval(getNewOriginOval(p2), Math.abs(dragStart.x - p2.x), currColor, lineThickness);
        }
    }

    // get new origin for rectangle being drawn
    public PointF getNewOriginRect(PointF p2)
    {
        PointF newPoint;

        if (p2.x < dragStart.x)
        {
            if (p2.y < dragStart.y)
            {
                return p2;
            }
            else
            {
                newPoint = new PointF(p2.x, dragStart.y);
                return newPoint;
            }
        }
        else if (p2.y < dragStart.y)
        {
            newPoint = new PointF(dragStart.x, p2.y);
            return newPoint;
        }
        else
        {
            return dragStart;
        }
    }

    // get endPoint for new rectangle being drawn
    public PointF getNewEndPointRect(PointF p2)
    {
        PointF newPoint;

        if (p2.x > dragStart.x)
        {
            if (p2.y > dragStart.y)
            {
                return p2;
            }
            else
            {
                newPoint = new PointF(p2.x, dragStart.y);
                return newPoint;
            }
        }
        else if (p2.y > dragStart.y)
        {
            newPoint = new PointF(dragStart.x, p2.y);
            return newPoint;
        }
        else
        {
            return dragStart;
        }
    }

    // get origin for new circle being drawn
    public PointF getNewOriginOval(PointF p2)
    {
        PointF newPoint;

        if (p2.x < dragStart.x)
        {
            if (p2.y < dragStart.y)
            {
                newPoint = new PointF(p2.x, dragStart.y - (dragStart.x - p2.x));
                return newPoint;
            }
            else
            {
                newPoint = new PointF(p2.x, dragStart.y);
                return newPoint;
            }
        }
        else if (p2.y < dragStart.y)
        {
            newPoint = new PointF(dragStart.x, dragStart.y - (p2.x - dragStart.x));
            return newPoint;
        }
        else
        {
            return dragStart;
        }
    }

    // get translation amount and translate shape
    public void moveShape(PointF p)
    {
        // when dragging a shape, change points by the value of the translation
        translateX = p.x - dragStart.x;
        translateY = p.y - dragStart.y;

        currShape.translate(translateX, translateY);
    }

    // add newly drawn shape to array of shapes
    public void addDrawnShape()
    {
        shapes.add(currShape);
        currShape = null;
        isDrawing = false;
    }

    // change current color
    public void changeColor(int color)
    {
        currColor = color;
        if (currShape != null) currShape.lineColor = currColor;
    }

    // change current line thickness
    public void changeLineThickness(int l)
    {
        lineThickness = l;
        if (currShape != null) currShape.lineThickness = lineThickness;
    }

    // set origin and endpoints now that the translation is completed
    public void setTranslatedPoints()
    {
        currShape.origin = new PointF(currShape.origin.x + translateX, currShape.origin.y + translateY);
        currShape.endPoint = new PointF(currShape.endPoint.x + translateX, currShape.endPoint.y + translateY);
    }

    // restore set tool after orientation change
    public void setSavedCurrTool(Tool tool)
    {
        this.currTool = tool;
    }

    // restore set color after orientation change
    public void setSavedCurrColor(int color)
    {
        this.currColor = color;
    }

    // restore line thickness after orientation change
    public void setSavedLineThickness(int lineThickness)
    {
        this.lineThickness = lineThickness;
    }
}