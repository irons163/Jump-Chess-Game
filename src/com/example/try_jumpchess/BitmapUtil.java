package com.example.try_jumpchess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class BitmapUtil {
	static Context context;
	
	public static Bitmap redPoint;
	public static Bitmap greenPoint;
	public static Bitmap blackPoint;
	public static Bitmap whitePoint;
	
	public static void initBitmap(Context context){
		BitmapUtil.context = context;
		initBitmap();
	}
	
	private static void initBitmap(){
		redPoint = BitmapFactory.decodeResource(context.getResources(), R.drawable.green_point, null);
		greenPoint = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.red_point);
		blackPoint = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.black_point);
		whitePoint = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.white_point);

	}
	
}
