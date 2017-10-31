package com.example.eslong.jsketch;

import android.graphics.*;
import android.os.Parcel;
import android.os.Parcelable;

public class Rectangle extends Shapes
{
	public int fillColor;

	Rectangle(PointF p1, PointF p2, int color, int thickness)
	{
		this.origin = p1;
		this.endPoint = p2;
		this.lineColor = color;
		this.lineThickness = thickness;
		this.fillColor = 0;
		this.shapeToDraw = new RectF(origin.x, origin.y, endPoint.x, endPoint.y);
	}

	public void setFillColor(int color)
	{
		fillColor = color;
	}

	public void translate(float tx, float ty)
	{
		shapeToDraw = new RectF(origin.x + tx, origin.y + ty, endPoint.x + tx, endPoint.y + ty);
	}
}