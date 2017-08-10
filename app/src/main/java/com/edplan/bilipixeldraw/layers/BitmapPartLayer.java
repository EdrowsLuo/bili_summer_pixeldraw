package com.edplan.bilipixeldraw.layers;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import com.edplan.bilipixeldraw.Layer;
import com.edplan.bilipixeldraw.graphics.Point;
import com.edplan.bilipixeldraw.graphics.PointF;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff;
import com.edplan.bilipixeldraw.StaticM;
import android.util.Log;
import android.view.MotionEvent;
import android.graphics.drawable.Drawable;
import android.graphics.BitmapFactory;

public class BitmapPartLayer extends Layer
{
	
	private Drawable d;
	
	private boolean enableDrag=true;
	private boolean enableZoom=true;
	private boolean enableSelect=true;
	private boolean enablePositionText=true;
	
	
	private Bitmap background;
	private Paint backgroundPaint;
	
	
	private PointF downPoint;
	private PointF downBitmapCenter;
	private boolean hasMove=false;
	
	//private PointF downPoint2;
	//private PointF nowDownPoint;
	//private PointF nowDownPoint2;
	//private boolean has2finger=false;
	private float downDistance;
	private boolean ifZoom=false;
	private boolean hasZoom=false;
	private float rawZoomTimes;
	
	public PointF centerPoint;
	public float zoomTimes=1;
	
	private int backgroundColor=0x00222222;
	
	private Matrix drawMatrix;
	
	private Rect saveCanvasRect;
	public Bitmap bitmap;
	public Bitmap bitmapOverlay;
	
	private boolean ifDrawNet=true;
	private Paint netPaint;
	private float minZoomTimesToShowNet;
	
	private Point selectedPoint;
	private Paint selectedPointPaint;
	private Paint selectedRectBackGroundPaint;
	
	private Paint textPaint;
	private Paint textFieldPaint;
	
	public BitmapPartLayer(){
		saveCanvasRect=new Rect();
		drawMatrix=new Matrix();
		centerPoint=new PointF();
		
		getDefaultPaint().setAntiAlias(false);
		
		
		netPaint=new Paint();
		netPaint.setAntiAlias(false);
		netPaint.setStyle(Paint.Style.FILL);
		netPaint.setStrokeWidth(StaticM.dip2px(0.6f));
		netPaint.setARGB(150,180,229,249);
		netPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
		minZoomTimesToShowNet=Math.min(StaticM.getScreenRect().x,StaticM.getScreenRect().y)/120f;
		
		backgroundPaint=new Paint();
		backgroundPaint.setAlpha(140);
		backgroundPaint.setAntiAlias(true);
		
		
		selectedPointPaint=new Paint();
		selectedPointPaint.setARGB(200,200,50,50);
		selectedPointPaint.setStyle(Paint.Style.STROKE);
		selectedPointPaint.setStrokeWidth(StaticM.dip2px(2.4f));
		selectedPointPaint.setStrokeMiter(30f);
		selectedPointPaint.setStrokeCap(Paint.Cap.ROUND);
		selectedPointPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
		
		selectedRectBackGroundPaint=new Paint();
		selectedRectBackGroundPaint.setARGB(250,200,200,200);
		selectedRectBackGroundPaint.setStyle(Paint.Style.STROKE);
		selectedRectBackGroundPaint.setStrokeWidth(StaticM.dip2px(3.7f));
		selectedRectBackGroundPaint.setStrokeMiter(30f);
		selectedRectBackGroundPaint.setStrokeCap(Paint.Cap.ROUND);
		//selectedRectBackGroundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));

		
		
		
		textPaint=new Paint();
		textPaint.setARGB(255,255,255,255);
		textPaint.setTextSize(StaticM.dip2px(18f));
		textPaint.setFakeBoldText(true);
		textPaint.setShadowLayer(StaticM.dip2px(2f),StaticM.dip2px(0.5f),StaticM.dip2px(0.5f),0xDD111111);
		
		textFieldPaint=new Paint();
		textFieldPaint.setARGB(210,85,85,85);
		textFieldPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		textFieldPaint.setStrokeWidth(5f);
		
		
		//Log.v("data test",minZoomTimesToShowNet+"");
	}

	public void setSelectedPoint(Point selectedPoint)
	{
		this.selectedPoint = selectedPoint;
	}

	public Point getSelectedPoint()
	{
		return selectedPoint;
	}
	
	
	public boolean onSelectPoint(int x,int y){
		return inBitmap(x,y);
	}

	public void setSaveCanvasRect(Rect saveCanvasRect)
	{
		this.saveCanvasRect = saveCanvasRect;
	}

	public Rect getSaveCanvasRect()
	{
		return saveCanvasRect;
	}

	public void setSelectedRectBackGroundPaint(Paint selectedRectBackGroundPaint)
	{
		this.selectedRectBackGroundPaint = selectedRectBackGroundPaint;
	}

	public Paint getSelectedRectBackGroundPaint()
	{
		return selectedRectBackGroundPaint;
	}

	public void setSelectedPointPaint(Paint selectedPointPaint)
	{
		this.selectedPointPaint = selectedPointPaint;
	}

	public Paint getSelectedPointPaint()
	{
		return selectedPointPaint;
	}

	public void setEnablePositionText(boolean enablePositionText)
	{
		this.enablePositionText = enablePositionText;
	}

	public boolean isEnablePositionText()
	{
		return enablePositionText;
	}

	public void setEnableSelect(boolean enableSelect)
	{
		this.enableSelect = enableSelect;
	}

	public boolean isEnableSelect()
	{
		return enableSelect;
	}

	public void setEnableZoom(boolean enableZoom)
	{
		this.enableZoom = enableZoom;
	}

	public boolean isEnableZoom()
	{
		return enableZoom;
	}

	public void setEnableDrag(boolean enableDrag)
	{
		this.enableDrag = enableDrag;
	}

	public boolean isEnableDrag()
	{
		return enableDrag;
	}

	public void setBackground(Bitmap background)
	{
		this.background = background;
	}

	public Bitmap getBackground()
	{
		return background;
	}

	
	public void setBitmapCenter(float x,float y){
		
		//if(x<0||y<0||x>bitmap.getWidth()||y>bitmap.getHeight()){
		//	return;
		//}
		
		if(x<0)x=0;
		if(x>bitmap.getWidth())x=bitmap.getWidth();
		if(y<0)y=0;
		if(y>bitmap.getHeight())y=bitmap.getHeight();
		
		
		centerPoint.x=x;
		centerPoint.y=y;
	}
	
	public void setZoomTimes(float zoom){
		if(zoom<StaticM.dip2px(0.05f))zoom=StaticM.dip2px(0.05f);
		if(zoom>StaticM.dip2px(20f))zoom=StaticM.dip2px(20f);
		zoomTimes=zoom;
	}
	
	public float getZoomTimes(){
		return zoomTimes;
	}
	
	public void setBitmap(Bitmap bmp){
		bitmap=bmp;
		centerPoint.x=bmp.getWidth()/2f;
		centerPoint.y=bmp.getHeight()/2f;
		zoomTimes=1;
	}
	
	public Bitmap getBitmap(){
		return bitmap;
	}
	
	public PointF bitmapPointToCanvas(PointF p){
		PointF pn=new PointF(p.x,p.y);
		pn.translation(saveCanvasRect.centerX(),saveCanvasRect.centerY());
		pn.translation(-centerPoint.x,-centerPoint.y);
		pn.reflect(saveCanvasRect.centerX(),saveCanvasRect.centerY(),zoomTimes,zoomTimes);
		return pn;
	}
	
	public PointF canvasPointToBitmap(PointF p){
		PointF pn=new PointF(p.x,p.y);
		pn.reflect(saveCanvasRect.centerX(),saveCanvasRect.centerY(),1/zoomTimes,1/zoomTimes);
		pn.translation(centerPoint.x,centerPoint.y);
		pn.translation(-saveCanvasRect.centerX(),-saveCanvasRect.centerY());
		return pn;
	}
	
	private void updateMatrix(){
		drawMatrix.setTranslate(saveCanvasRect.centerX()-centerPoint.x,saveCanvasRect.centerY()-centerPoint.y);
		drawMatrix.postScale(zoomTimes,zoomTimes,saveCanvasRect.centerX(),saveCanvasRect.centerY());
	}
	
	public boolean inBitmap(float x,float y){
		return !(x<0||y<0||x>bitmap.getWidth()||y>bitmap.getHeight());
	}
	
	public void setSelectedPoint(int x,int y){
		if(onSelectPoint(x,y)){
			selectedPoint=new Point(x,y);
		}
	}
	
	
	
	public void onCanvasChange(Canvas canvas){
		saveCanvasRect.right=canvas.getWidth();
		saveCanvasRect.bottom=canvas.getHeight();
	}

	@Override
	public void onTouch(MotionEvent event)
	{
		// TODO: Implement this method
		super.onTouch(event);
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				downPoint=new PointF(event.getX(),event.getY());
				downBitmapCenter=new PointF(centerPoint.x,centerPoint.y);
				hasMove=false;
				
				//Log.v("on touch","set Point "+event.getX()+"|"+event.getY()+"|"+selectedPoint.x+"|"+selectedPoint.y);
				break;
			case MotionEvent.ACTION_MOVE:
				
				if(event.getPointerCount()>=2){
					float x=event.getX(1)-event.getX(0);
					float y=event.getY(1)-event.getY(0);
					float downDistance2=(float) Math.sqrt(x*x+y*y);
					
					if(downDistance<=0){
						downDistance=downDistance2;
					}
					
					
					if(Math.abs(downDistance2-downDistance)>StaticM.dip2px(0.5f)){
						if(enableZoom)if(ifZoom){
							setZoomTimes(rawZoomTimes*downDistance2/downDistance);
						}else{
							rawZoomTimes=zoomTimes;
							setZoomTimes(rawZoomTimes*downDistance2/downDistance);
							ifZoom=true;
							hasZoom=true;
						}
					}
					
				}
				
				PointF moveV=new PointF(downPoint.x-event.getX(),downPoint.y-event.getY());
				if((!ifZoom)&&(hasMove||(!hasMove&&moveV.distance()>StaticM.dip2px(1.5f)))){
					if(enableDrag)setBitmapCenter(downBitmapCenter.x+(downPoint.x-event.getX())/getWidthPerPixel(),downBitmapCenter.y+(downPoint.y-event.getY())/getWidthPerPixel());
					hasMove=true;
				}
				break;
			case MotionEvent.ACTION_UP:
				downPoint=null;
				downBitmapCenter=null;
				if(!hasMove&&!hasZoom){
					PointF p=canvasPointToBitmap(new PointF(event.getX(),event.getY()));
					if(enableSelect)setSelectedPoint((int)Math.floor(p.x),(int)Math.floor(p.y));
				}
				downDistance=0;
				if(ifZoom){
					ifZoom=false;
				}
				if(event.getPointerCount()==1){
					hasZoom=false;
				}
				break;
			
		}
	}
	
	
	@Override
	public void draw(Canvas canvas)
	{
		// TODO: Implement this method
		
		if(saveCanvasRect.width()!=canvas.getWidth()
			||saveCanvasRect.height()!=canvas.getHeight()){
			onCanvasChange(canvas);
		}
		
		canvas.drawColor(backgroundColor);
		
		drawBackground(canvas);
		
		
		if(bitmap==null){
			return;
		}
		
		//Log.v("draw net",getPixelOffsetX()+"|"+getPixelOffsetY()+"|"+getWidthPerPixel());
		//Log.v("test data",bitmapPointToCanvas(new PointF(0,0)).toString());
		
		updateMatrix();
		canvas.drawBitmap(bitmap,drawMatrix,getDefaultPaint());
		if(bitmapOverlay!=null)canvas.drawBitmap(bitmapOverlay,drawMatrix,getDefaultPaint());
		
		if(zoomTimes>=minZoomTimesToShowNet&&ifDrawNet){
			drawNet(canvas);
			if(selectedPoint!=null){
				PointF lt=bitmapPointToCanvas(new PointF(selectedPoint.x,selectedPoint.y));
				PointF rb=bitmapPointToCanvas(new PointF(selectedPoint.x+1,selectedPoint.y+1));
				canvas.drawRect(lt.x,lt.y,rb.x,rb.y,selectedRectBackGroundPaint);
				canvas.drawRect(lt.x,lt.y,rb.x,rb.y,selectedPointPaint);
				if(enablePositionText)canvas.drawText("("+selectedPoint.x+","+selectedPoint.y+")",rb.x+10,rb.y+10,textPaint);
			}
		}
		
	
		
	}
	
	/*public void setBitmapCenter(float x,float y){
		centerPoint.translation(dx,dy);
	}*/
	
	public void drawNet(Canvas canvas){
		float colum=getPixelOffsetX();
		float row=getPixelOffsetY();
		
		while(colum<canvas.getWidth()){
			canvas.drawLine(colum,0,colum,canvas.getHeight(),netPaint);
			colum+=getWidthPerPixel();
		}
		
		while(row<canvas.getHeight()){
			canvas.drawLine(0,row,canvas.getWidth(),row,netPaint);
			row+=getWidthPerPixel();
		}
		
	}
	
	Rect backgroundRect;
	public void drawBackground(Canvas canvas){
		
		if(background==null)return;
		
		if(backgroundRect==null)backgroundRect=new Rect();
		
		float backgroundZoomTimes=Math.min(((float)background.getWidth())/canvas.getWidth(),((float)background.getHeight())/canvas.getHeight());
		
		
		backgroundRect.left=0;
		backgroundRect.top=0;
		//backgroundRect.right=Math.min(background.getWidth(),background.getWidth()
		if(((float)background.getWidth())/background.getHeight()>((float)canvas.getWidth())/canvas.getHeight()){
			backgroundRect.bottom=background.getHeight();
			backgroundRect.right=(int)(((float)background.getHeight()*canvas.getWidth())/canvas.getHeight());
		}else{
			backgroundRect.right=background.getWidth();
			backgroundRect.bottom=(int)(((float)background.getWidth()*canvas.getHeight())/canvas.getWidth());
		}
		
		
		canvas.drawBitmap(background,backgroundRect,new Rect(0,0,canvas.getWidth(),canvas.getHeight()),backgroundPaint);
		
	}
	
	public float getPixelOffsetX(){
		float rcs=bitmapPointToCanvas(new PointF(0,0)).x;
		
		if(rcs<0){
			return getWidthPerPixel()-Math.abs(rcs)%getWidthPerPixel();
		}else{
			return rcs%getWidthPerPixel();
		}
	}
	
	public float getPixelOffsetY(){
		float rcs=bitmapPointToCanvas(new PointF(0,0)).y;
		if(rcs<0){
			return getWidthPerPixel()-Math.abs(rcs)%getWidthPerPixel();
		}else{
			return rcs%getWidthPerPixel();
		}
	}
	
	public float getWidthPerPixel(){
		return zoomTimes;
	}
	
	
	public void setBackgroundColor(int backgroundColor)
	{
		this.backgroundColor = backgroundColor;
	}

	public int getBackgroundColor()
	{
		return backgroundColor;
	}
	
	public void setNetPaint(Paint netPaint)
	{
		this.netPaint = netPaint;
	}

	public Paint getNetPaint()
	{
		return netPaint;
	}

	public void setIfDrawNet(boolean ifDrawNet)
	{
		this.ifDrawNet = ifDrawNet;
	}

	public boolean isIfDrawNet()
	{
		return ifDrawNet;
	}
	
	
}
