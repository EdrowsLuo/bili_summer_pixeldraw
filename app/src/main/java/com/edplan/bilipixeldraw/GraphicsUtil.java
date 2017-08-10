package com.edplan.bilipixeldraw;

import android.graphics.*;

public class GraphicsUtil
{
	
	public static void drawRect(Canvas canvas,Rect rct,Paint paint){
		canvas.drawLine(rct.left,rct.top,rct.left,rct.bottom,paint);
		canvas.drawLine(rct.right,rct.top,rct.right,rct.bottom,paint);
		canvas.drawLine(rct.left,rct.top,rct.right,rct.top,paint);
		canvas.drawLine(rct.left,rct.bottom,rct.right,rct.bottom,paint);
	}
	
	public static void drawNet(Canvas canvas,int x,int y,Paint paint){
		float[] points=new float[((x-1)+(y-1))*4];

		float xDelta=canvas.getWidth()/(float)x;
		float yDelta=canvas.getHeight()/(float)y;

		for(int i=0;i<x-1;i++){
			points[4*i]=xDelta*(i+1);
			points[4*i+1]=0;
			points[4*i+2]=points[4*i];
			points[4*i+3]=canvas.getHeight();
		}

		for(int i=0;i<y-1;i++){
			points[4*(x-1)+4*i]=0;
			points[4*(x-1)+4*i+1]=yDelta*(i+1);
			points[4*(x-1)+4*i+2]=canvas.getWidth();
			points[4*(x-1)+4*i+3]=points[4*(x-1)+4*i+1];
		}

		canvas.drawLines(points,paint);
	}
	
}
