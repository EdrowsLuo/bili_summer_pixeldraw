package com.edplan.bilipixeldraw;
import android.util.*;
import java.net.*;
import org.json.*;
import java.io.*;

public class BiliDrawer
{
	public static final int CODE_DRAW_JAVAERR=-1;
	public static final int CODE_DRAW_OK=0;
	public static final int CODE_DRAW_OVERTIME=-400;
	public static final int CODE_DRAW_NOT_LOGIN=-101;
	public static final int DELTA_TIME=180;
	
	private static String drawApiBody="https://api.live.bilibili.com/activity/v1/SummerDraw/draw";
	private static String statusApiBody="http://api.live.bilibili.com/activity/v1/SummerDraw/status";
	
	private long latestUpdateTime=0;
	private String cookie;
	private Status status;

	
	public BiliDrawer(String cookie){
		this.cookie=cookie;
		status=new Status();
		updateSynced();
	}
	
	public BiliDrawer(){
		this(null);
	}
	
	public void updateSynced(){
		Thread t=new Thread(new Runnable(){
				@Override
				public void run()
				{
					// TODO: Implement this method
					BiliDrawer.this.updateStatus();
				}
			});
		t.start();
	}
	
	public void drawSynced(final int x,final int y,final String color,final OnResultListener l){
		Thread t=new Thread(new Runnable(){
				@Override
				public void run()
				{
					// TODO: Implement this method
					Result r=drawPixel(x,y,color);
					if(l!=null)l.onResult(r);
				}
			});
		
		t.start();
	}
	
	public static Status getStatus(String cookies){
		try
		{
			Status status_=new Status();
			URL url=new URL(statusApiBody);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10*1000);
			conn.setRequestProperty("Cookie",cookies);
			String s=JsonUtils.read(conn.getInputStream());
			status_.parse(new JSONObject(s));
			return status_;
		}
		catch(Exception e){
			Log.e("err",e.getMessage(),e);
			return new Status();
		}
	}
	
	public void updateStatus(){
		try
		{
			URL url=new URL(statusApiBody);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10*1000);
			conn.setRequestProperty("Cookie",cookie);
			String s=JsonUtils.read(conn.getInputStream());
			conn.disconnect();
			synchronized(status){
				status.parse(new JSONObject(s));
			}
			latestUpdateTime=System.currentTimeMillis();
		}
		catch(Exception e){
			Log.e("err",e.getMessage(),e);
		}
	}
	
	public Result drawPixel(int x,int y,String color){
		
		//if(getTimeRemind()!=0)return null;
		try
		{
			updateStatus();
			
			URL url=new URL(drawApiBody+"?x_min="+x+"&y_min="+y+"&x_max="+x+"&y_max="+y+"&color="+color);
			HttpURLConnection conn=(HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(10*1000);
			conn.setRequestProperty("Cookie",cookie);
			
			
			
			
			BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			
			String s;
			StringBuilder sb=new StringBuilder();
			while((s=br.readLine())!=null){
				sb.append(s);
			}
			br.close();
			conn.disconnect();
			
			updateStatus();
			
			Result r=(new Result()).parse(new JSONObject(sb.toString()));
			return r;
		}
		catch (Exception e)
		{
			Log.e("e",e.getMessage(),e);
			Result r=new Result();
			r.code=CODE_DRAW_JAVAERR;
			r.message=e.getMessage();
			r.msg=e.getMessage();
			r.time=DELTA_TIME;
			return r;
		}
	}
	
	
	public int getTimeRemind(){
		int time=status.time-(int)(System.currentTimeMillis()-latestUpdateTime)/1000;
		if(time<0)time=0;
		if(time>DELTA_TIME)time=DELTA_TIME;
		return time;
	}
	
	public void setCookie(String cookie)
	{
		this.cookie = cookie;
	}

	public String getCookie()
	{
		return cookie;
	}
	
	
	
	public class Result{
		public int code;
		public String msg;
		public String message;
		public int time;
		
		public Result parse(JSONObject json){
			code=json.optInt("code");
			msg=json.optString("msg");
			message=json.optString("message");
			if(code==CODE_DRAW_OK||code==CODE_DRAW_OVERTIME)time=json.optJSONObject("data").optInt("time");
			return this;
		}
	}
	
	public static class Status{
		public boolean isLogin=false;
		public boolean user_valid=false;
		public boolean draw_status=false;
		public boolean super_user=false;
		public int time=DELTA_TIME/1000;
		
		
		public void parse(JSONObject obj){
			int code=obj.optInt("code",0xff12345678);
			if(code==CODE_DRAW_NOT_LOGIN){
				isLogin=false;
				return;
			}else{
				JSONObject data=obj.optJSONObject("data");
				user_valid=data.optBoolean("user_valid");
				draw_status=data.optBoolean("draw_status");
				super_user=data.optBoolean("super_user");
				time=data.optInt("time");
			}
		}
		
	}
	
	
	public interface OnResultListener{
		public void onResult(Result r);
	}
	
}
