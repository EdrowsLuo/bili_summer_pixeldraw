package com.edplan.bilipixeldraw.graphics;

public class PointF
{
	public float x;
	public float y;

	public PointF(){
		this(0,0);
	}

	public PointF(float x,float y){
		this.x=x;
		this.y=y;
	}
	
	public float distance(){
		return x*x+y*y;
	}
	
	public void translation(float dx,float dy){
		this.x+=dx;
		this.y+=dy;
	}
	
	public void reflect(float cx,float cy,float zoomX,float zoomY){
		this.x=cx+(this.x-cx)*zoomX;
		this.y=cy+(this.y-cy)*zoomY;
	}

	@Override
	public String toString()
	{
		// TODO: Implement this method
		return super.toString()+" ["+x+","+y+"]";
	}
	
	
}
