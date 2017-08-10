package com.edplan.bilipixeldraw;
import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.edplan.bilipixeldraw.graphics.Point;
import android.view.View.OnClickListener;

public class StaticM
{
	private static Activity context;
	private static View view;
	
	public static void ini(Activity con,View viewr){
		context=con;
		view=viewr;
	}
	

	public static float px2dip(float pxValue){   
        final float scale = context.getResources().getDisplayMetrics().density;   
        return (pxValue / scale + 0.5f);   
	}   

	public static float dip2px(float dipValue){   
        final float scale = context.getResources().getDisplayMetrics().density;   
        return (dipValue * scale + 0.5f);   
	}   
	
	
	public static Point getScreenRect(){
		WindowManager wm = context.getWindowManager();

		return new Point(wm.getDefaultDisplay().getWidth(),wm.getDefaultDisplay().getHeight());
	}
	
	public static void toast(final String s){
		context.runOnUiThread(new Runnable(){
				@Override
				public void run()
				{
					// TODO: Implement this method
					Toast.makeText(context,s,Toast.LENGTH_LONG).show();
				}
			});
	}
	
	public static void makeSnakeBar(final String msg,final View.OnClickListener click){
		
		context.runOnUiThread(new Runnable(){
				@Override
				public void run()
				{
					// TODO: Implement this method
					Snackbar.make(view,msg, Snackbar.LENGTH_SHORT)
						.setAction("Action", click).show();
				}
			});
	}
	
}
