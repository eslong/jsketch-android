package com.example.eslong.jsketch;

import android.graphics.*;
import android.os.Parcel;
import android.os.Parcelable;

public class Oval extends Shapes
{
	public int fillColor;

	Oval(PointF p1, float diameter, int color, int thickness)
	{
		this.origin = p1;
		this.endPoint = new PointF(p1.x + diameter, p1.y + diameter);
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