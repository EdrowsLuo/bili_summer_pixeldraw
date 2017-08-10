package com.edplan.bilipixeldraw;

import android.content.*;
import android.graphics.*;
import android.view.*;
import java.util.*;
import android.util.*;

public class LayerDrawView extends View
{
	
	private List<Layer> layers;
	
	public LayerDrawView(Context con,AttributeSet att){
		super(con,att);
		layers=new ArrayList<Layer>();
		
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO: Implement this method
		//Log.v("on draw","draw"+System.currentTimeMillis());
		super.onDraw(canvas);
		for(Layer layer:layers){
			if(layer.isVisible())layer.draw(canvas);
		}
		invalidate();
	}
	

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// TODO: Implement this method
		//Log.v("on touch","send "+event.getPointerCount()+" | "+event.toString());
		for(Layer l:layers){
			l.onTouch(event);
		}
		return true;
		
		//return super.onTouchEvent(event);
	}
	
	
	
	
	public boolean addLayer(Layer l){
		return layers.add(l);
	}
	
	public boolean removeLayer(Layer l){
		return layers.remove(l);
	}
	
	public List<Layer> getLayers(){
		return layers;
	}
	
}
