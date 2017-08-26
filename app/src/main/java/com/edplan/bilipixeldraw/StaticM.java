package com.edplan.bilipixeldraw;
import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.edplan.bilipixeldraw.graphics.Point;
import android.view.View.OnClickListener;
import java.io.File;
import android.os.Environment;
import android.content.SharedPreferences;

public class StaticM
{
	private static Activity context;
	private static View view;
	
	public static void ini(Activity con,View viewr){
		context=con;
		view=viewr;
	}
	
	public static void putString(String key,String s){
		getMainSharedPreferences().edit().putString(key,s).apply();
	}
	
	public static void putInt(String key,int value){
		getMainSharedPreferences().edit().putInt(key,value).apply();
	}
	
	public static void putFloat(String key,float v){
		getMainSharedPreferences().edit().putFloat(key,v).apply();
	}
	
	public static float getFloat(String key){
		return getMainSharedPreferences().getFloat(key,-1);
	}
	
	public static int getInt(String key){
		return getMainSharedPreferences().getInt(key,-1);
	}
	
	public static String getString(String key){
		return getMainSharedPreferences().getString(key,null);
	}
	
	public static SharedPreferences getMainSharedPreferences(){
		return context.getSharedPreferences("main",0);
	}
	
	
	
	public static File getMainDir(){
		File d= new File(Environment.getExternalStorageDirectory(),"夏日绘版");
		if(!d.exists())d.mkdirs();
		return d;
	}
	
	public static File getMainPictureDir(){
		File d=new File(getMainDir(),"Pictures");
		if(!d.exists())d.mkdirs();
		return d;
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
	
	public static void  makeSnakeBar(final String msg){
		makeSnakeBar(msg,null,null);
	}
	
	
	
	
	public static void makeSnakeBar(final String msg,final View.OnClickListener click,final String action){
		
		context.runOnUiThread(new Runnable(){
				@Override
				public void run()
				{
					// TODO: Implement this method
					Snackbar.make(view,msg, Snackbar.LENGTH_SHORT)
						.setAction(action, click).show();
				}
			});
	}
	
}
