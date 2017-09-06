package com.edplan.bilipixeldraw.layers;
import com.edplan.bilipixeldraw.Layer;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.edplan.bilipixeldraw.StaticM;
import com.edplan.bilipixeldraw.graphics.PointF;
import java.util.Iterator;
import android.graphics.Color;

public class AnimaLayer extends Layer
{
	public List<AnimaEntry> animas;
	public List<AnimaEntry> addList;
	
	public boolean drawing=false;
	
	BitmapPartLayer layer;
	public AnimaLayer(BitmapPartLayer layer){
		this.layer=layer;
		animas=new ArrayList<AnimaEntry>();
		addList=new ArrayList<AnimaEntry>();
		animaPaint=new Paint();
		animaPaint.setStyle(Paint.Style.STROKE);
		
	}

	@Override
	public void draw(Canvas canvas)
	{
		// TODO: Implement this method
		super.draw(canvas);
		
		for(AnimaEntry e:addList){
			animas.add(e);
		}
		addList.clear();
		
		
		drawing=true;
		
		Iterator<AnimaEntry> itr=animas.iterator();
		AnimaEntry e;
		while(itr.hasNext()){
			e=itr.next();
			if(drawAnima(canvas,e)){
				itr.remove();
			}
		}
		
		drawing=false;
		
		
		
		
		
		/*
		for(AnimaEntry e:animas){
			if(drawAnima(canvas,e)){
				removeList.add(e);
			}
		}
		
		for(AnimaEntry e:removeList){
			animas.remove(e);
		}*/
	}
	
	
	public void add(AnimaEntry a){
		
		if(drawing){
			addList.add(a);
		}else{
			animas.add(a);
		}
		
		
	}
	
	
	Paint animaPaint;
	float r;
	float alpha;
	public boolean drawAnima(Canvas c,AnimaEntry e){
		if(e.startTime==-1){
			e.startTime=System.currentTimeMillis();
		}
		
		alpha=1-((float)(System.currentTimeMillis()-e.startTime))/e.duration;
		if(alpha<0)alpha=0;
		if(alpha>1)alpha=1;
		r=/*layer.getWidthPerPixel()*/ StaticM.dip2px(5) *(1f-alpha)*2;
		animaPaint.setARGB(255,80,80,80);
		//255,255-Color.red(e.color),255-Color.green(e.color),255-Color.blue(e.color));
		animaPaint.setAlpha((int)(alpha*255));
		animaPaint.setStrokeWidth(StaticM.dip2px(7)*alpha);
		
		PointF point=layer.bitmapPointToCanvas(e.p);
		
		c.drawCircle(point.x,point.y,r,animaPaint);
		animaPaint.setStrokeWidth(StaticM.dip2px(4)*alpha);
		//animaPaint.setColor(e.color);
		animaPaint.setARGB(255,245,40,40);
		c.drawCircle(point.x,point.y,r,animaPaint);
		
		
		return (System.currentTimeMillis()-e.startTime)>e.duration;
	}
	
	
	public static class AnimaEntry{
		public PointF p;
		public int color;
		public long startTime;
		public int duration;
		
		public AnimaEntry(float x,float y,int color){
			startTime=-1;
			duration=1000;
			this.p=new PointF(x,y);
			this.color=color;
		}
	}
	
	
}
