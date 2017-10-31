package com.example.eslong.jsketch;

import android.graphics.*;
import android.os.Parcel;
import android.os.Parcelable;

abstract class Shapes
{
	public PointF origin;
	public PointF endPoint;
	public int lineColor;
	public int lineThickness;
	public int fillColor;
	public RectF shapeToDraw;

	abstract void translate(float tx, float ty);
}