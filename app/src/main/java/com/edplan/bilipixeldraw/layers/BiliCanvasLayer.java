package com.edplan.bilipixeldraw.layers;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.edplan.bilipixeldraw.BiliBitmapParser;
import com.edplan.bilipixeldraw.R;
import com.edplan.bilipixeldraw.StaticM;
import android.graphics.Canvas;
import android.graphics.Paint;
import java.io.IOException;

public class BiliCanvasLayer extends BitmapPartLayer
{
	BiliBitmapWrapped bbw;
	
	public void setBiliBitmapParser(BiliBitmapParser bnp){
		bbw=new BiliBitmapWrapped(bnp);
		setBitmap(bbw.bitmap);
	}
	
	
	@Override
	public void setBitmap(Bitmap bmp)
	{
		// TODO: Implement this method
		super.setBitmap(bmp);
	}
	
	
	public class BiliBitmapWrapped{
		public Bitmap bitmap;
		public int deltaHeight=15;
		public BiliBitmapWrapped(final BiliBitmapParser bbp){
			bitmap=Bitmap.createBitmap(1280,720*3+deltaHeight*2,Bitmap.Config.ARGB_8888);
			try
			{
			Bitmap raw=BitmapFactory.decodeStream(StaticM.getContext().getResources().getAssets().open("canvas_all.png"));
			
			final Canvas c=new Canvas(bitmap);
			final Paint paint=new Paint();
			paint.setAntiAlias(false);
			c.drawBitmap(raw,0,720+deltaHeight,paint);
			c.drawBitmap(bbp.bitmap,0,0,paint);
			
			bbp.l = new BiliBitmapParser.OnChangeListener(){
				@Override
				public void onChange(int x, int y, char ch, boolean isAll)
				{
					// TODO: Implement this method
					if(isAll){
						c.drawBitmap(bbp.bitmap,0,0,paint);
					}else{
						bitmap.setPixel(x,y,BiliBitmapParser.parseColor(ch));
					}
				}
			};
			}
			catch (IOException e)
			{}
		}
	}
	
}
