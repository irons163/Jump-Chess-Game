package com.example.try_jumpchess;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Bobble {
	private Point point;
	private int type;
	private Bitmap boobleBitmap;
	private boolean canBubblePop = false;
	private boolean isNeedClear = false;
	private boolean isCheckedForLeft = false;
	private ScatteredDirectionType scatteredDirectionType = ScatteredDirectionType.DIRECTION_NONE;

	enum ScatteredDirectionType {
		DIRECTION_NONE, DIRECTION_LEFT, DIRECTION_RIGHT, DIRECTION_DOWN
	}

	public Bobble(Bitmap boobleBitmap, int type, Point point) {
		// TODO Auto-generated constructor stub
		this.type = type;
		this.point = point;
		this.boobleBitmap = boobleBitmap;
	}

	@Override
	public String toString() {
		return "Target [point=" + point + ", type=" + type + ", bitmap_target="
				+ boobleBitmap + "]";
	}

	public void MyDraw(Canvas canvas, Paint paint) {
		canvas.drawBitmap(boobleBitmap, point.x, point.y, paint);
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setBobbleBitmap(Bitmap boobleBitmap) {
		this.boobleBitmap = boobleBitmap;
	}

	public void clear() {
		isNeedClear = true;
	}

	public boolean isNeedClear() {
		return isNeedClear;
	}

	public boolean isCheckedForLeft() {
		return isCheckedForLeft;
	}

	public void setIsCheckedForLeft(boolean isCheckedForLeft) {
		this.isCheckedForLeft = isCheckedForLeft;
	}

	public ScatteredDirectionType getScatteredDirectionType() {
		return scatteredDirectionType;
	}

	public void setScatteredDirectionType(
			ScatteredDirectionType scatteredDirectionType) {
		this.scatteredDirectionType = scatteredDirectionType;
	}

	int count = 0;

	public void scatteredMove() {
		int speed = BitmapUtil.greenPoint.getWidth() / 10 * 2;
		switch (scatteredDirectionType) {
		case DIRECTION_NONE:

			break;
		case DIRECTION_LEFT:
			if (count < 20)
				point.y -= speed;
			else
				point.y += speed;
			point.x -= speed / 10;
			count++;
			break;
		case DIRECTION_RIGHT:
			if (count < 20)
				point.y -= speed;
			else
				point.y += speed;

			point.x += speed / 10;
			count++;
			break;
		case DIRECTION_DOWN:
			point.y += speed / 2;
			break;
		}
	}
}
