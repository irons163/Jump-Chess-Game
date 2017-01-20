package com.example.try_jumpchess;

public class Point extends android.graphics.Point{
	private boolean isJumpableChecked = false;
	
	public Point(){
		super();
	}
	
	public Point(int x, int y){
		super(x, y);
	}

	public boolean isJumpableChecked() {
		return isJumpableChecked;
	}

	public void setJumpableChecked(boolean isJumpableChecked) {
		this.isJumpableChecked = isJumpableChecked;
	}
	
}
