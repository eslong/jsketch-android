package com.example.eslong.jsketch;

import android.graphics.*;
import android.os.Parcel;
import android.os.Parcelable;

public class Line extends Shapes
{
	Line(PointF p1, PointF p2, int color, int thickness)
	{
		this.origin = p1;
		this.endPoint = p2;
		this.lineColor = color;
		this.lineThickness = thickness;
		this.fillColor = 0;
		this.shapeToDraw = new RectF(origin.x, origin.y, endPoint.x, endPoint.y);
	}

	public void translate(float tx, float ty) {}

	public boolean intersects(PointF p) { return false; }
}