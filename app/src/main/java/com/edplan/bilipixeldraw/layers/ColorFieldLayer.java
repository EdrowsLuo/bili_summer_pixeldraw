package com.edplan.bilipixeldraw.layers;
import android.graphics.Canvas;
import com.edplan.bilipixeldraw.Layer;
import java.util.List;
import java.util.ArrayList;
import android.graphics.Bitmap;
import com.edplan.bilipixeldraw.StaticM;
import android.graphics.RectF;
import com.edplan.bilipixeldraw.graphics.PointF;

public class ColorFieldLayer extends BitmapPartLayer
{
	
	public List<Integer> colorList;
	public int cCount=6;
	public OnColorSelectListener l; 
	
	public ColorFieldLayer(int x,int y){
		this.setEnableZoom(false);
		this.setEnablePositionText(false);
		colorList=new ArrayList<Integer>();
		cCount=x;
		Bitmap colorMap=Bitmap.createBitmap(x,y,Bitmap.Config.ARGB_8888);
		this.setBitmap(colorMap);
		this.getNetPaint().setStrokeWidth(StaticM.dip2px(10f));
		this.getNetPaint().setARGB(250,250,250,250);
		this.getSelectedPointPaint().setColor(0xFF3BE5DB);
	}
	
	public void setOnColorSelectListener(OnColorSelectListener l){
		this.l=l;
	}
	
	public int getSelectedColor(){
		return colorList.get(getIndex(getSelectedPoint().x,getSelectedPoint().y));
	}

	@Override
	public void onCanvasChange(Canvas canvas)
	{
		// TODO: Implement this method
		super.onCanvasChange(canvas);
		
		zoomTimes=(canvas.getWidth()-StaticM.dip2px(4.3f))/(cCount);
	}

	
	public void addColor(int color){
		colorList.add(color);
		getBitmap().setPixel(colorList.indexOf(color)%cCount,colorList.indexOf(color)/cCount,color);
	}
	
	
	@Override
	public void setBitmapCenter(float x, float y)
	{
		// TODO: Implement this method
		
		
		/*PointF canvasR=bitmapPointToCanvas(new PointF(0,0));
		PointF canvasR2=bitmapPointToCanvas(new PointF(0,getBitmap().getHeight()));
		if(canvasR.y>1){
			y=getSaveCanvasRect().bottom/(2*this.getZoomTimes());
		}else if(canvasR2.y<getSaveCanvasRect().bottom-1){
			y=getBitmap().getHeight()-getSaveCanvasRect().bottom/(2*this.getZoomTimes());
		}*/
		
		
		
		super.setBitmapCenter(x, y);
		centerPoint.x=((float)getBitmap().getWidth())/2;
	}

	@Override
	public boolean onSelectPoint(int x, int y)
	{
		// TODO: Implement this method
		if(!inBitmap(x,y)){
			return false;
		}else{
			//Integer i=colorList.get(getIndex(x,y));
			if(getIndex(x,y)<colorList.size()){
				//getSelectedPointPaint().setColor(colorList.get(getIndex(x,y)));
				if(l!=null)l.onSelect(colorList.get(getIndex(x,y)));
				return true;
			}else{
				return false;
			}
		}
	}
	
	public int getIndex(int x,int y){
		return x+y*cCount;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		// TODO: Implement this method
		super.draw(canvas);
	}
	
	public interface OnColorSelectListener{
		public void onSelect(int color);
	}
	
}
