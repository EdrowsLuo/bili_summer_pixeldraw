package com.edplan.bilipixeldraw;

import android.graphics.*;
import android.view.*;

public abstract class Layer
{
	private boolean visible=true;
	private Paint defaultPaint=new Paint();

	
	
	public void draw(Canvas canvas){
		
	}
	
	public void onTouch(MotionEvent event){
		
	}
	
	
	
	public boolean isVisible(){
		return visible;
	}
	
	public void setVisible(boolean b){
		visible=b;
	}
	
	public void setDefaultPaint(Paint defaultPaint)
	{
		this.defaultPaint = defaultPaint;
	}

	public Paint getDefaultPaint()
	{
		return defaultPaint;
	}
	
}
