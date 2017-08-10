package com.edplan.bilipixeldraw;

import android.graphics.*;

public class MathUtil
{
	
	public static RectF moveRectToCenter(Rect raw,RectF rect){
		RectF newRect=new RectF();
		newRect.top=raw.centerY()-rect.height()/2;
		newRect.bottom=raw.centerY()+rect.height()/2;
		newRect.left=raw.centerX()-rect.width()/2;
		newRect.right=raw.centerX()+rect.width()/2;
		return newRect;
	}
	
	public static RectF zoomRectIntoCenter(Rect raw,RectF rect){
		RectF cRect=moveRectToCenter(raw,rect);
		float zoomTimes=Math.min(raw.width()/cRect.width(),raw.height()/cRect.height());
		float w=cRect.width();
		float h=cRect.height();
		float cx=cRect.centerX();
		float cy=cRect.centerY();
		return new RectF(cx-w/2*zoomTimes,cy-h/2*zoomTimes,cx+w/2*zoomTimes,cy+h/2*zoomTimes);
	}
	
}
