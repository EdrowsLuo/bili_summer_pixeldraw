package com.edplan.bilipixeldraw;
import android.util.Log;

public class LogUtil
{
	public static boolean ifLog=true;
	
	public static void logErr(Throwable t){
		Log.e("err",t.getMessage(),t);
	}
	
}
