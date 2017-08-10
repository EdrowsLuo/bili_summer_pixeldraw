package com.edplan.bilipixeldraw;

import android.content.*;
import android.util.*;
import java.util.*;
import java.net.*;

public class WebUtil
{
	
	public static void startLoginActivity(Context c){
		Intent intent=new Intent();
		intent.setAction("com.edplan.bililogin");
		c.startActivity(intent);
	}
	
	
	public static void addBiliCookie(HttpURLConnection httpConn){
		httpConn.addRequestProperty("Cookie",BiliStateGetter.getBiliCookie());
	}
	
	
	public static Map<String,String> parseCookies(String cookies){
		
		if(cookies==null)return null;
		
		Map<String,String> cookiesMap=new TreeMap<String,String>();

		String[] raws=cookies.split("; ");

		for(String s:raws){
			String name=s.substring(0,s.indexOf("="));
			String value=s.substring(s.indexOf("=")+1,s.length());
			//Log.v("new cookie",name+" = "+value);
			cookiesMap.put(name,value);
		}
		return cookiesMap;
	}
	
}
