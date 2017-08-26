package com.edplan.bilipixeldraw;
import java.io.*;
import java.net.*;
import org.json.*;
import android.util.*;

public class BiliJsonGetter
{
	
	public static Flag flag=Flag.Waiting;
	
	public static enum Flag{
		Getting,Waiting
	}
	
	public static String urlString="http://api.live.bilibili.com/activity/v1/SummerDraw/bitmap";
	
	public static String getJsonString() throws Exception{
		if(flag==Flag.Getting)return null;
		flag=Flag.Getting;
		try
		{
			URL url=new URL(urlString);
			HttpURLConnection con=(HttpURLConnection)url.openConnection();
			con.setConnectTimeout(10*1000);
			StringBuilder sb=new StringBuilder();
			BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
			String s;
			while((s=br.readLine())!=null){
				sb.append(s);
			}
			br.close();
			con.disconnect();
			flag=Flag.Waiting;
			return sb.toString();
		}
		catch (Exception e)
		{
			flag=Flag.Waiting;
			Log.e("parse",e.getMessage(),e);
			throw e;
		}
	}
	
	public static String  parseJson(String res) throws JSONException{
		if(res==null)return null;
		
		//Log.v("res",res);
		
		JSONObject obj=new JSONObject(res);
		if((obj.getString("msg")=="success"&&obj.getString("message")=="success")||true){
			String out=obj.getJSONObject("data").getString("bitmap");
			//Log.v("res","out :"+out);
			return out;
		}else{
			Log.e("format","err format");
			return null;
		}
	}
}
