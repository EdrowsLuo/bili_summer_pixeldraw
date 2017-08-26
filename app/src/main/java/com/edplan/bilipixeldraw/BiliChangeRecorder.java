package com.edplan.bilipixeldraw;
import android.webkit.JavascriptInterface;

public interface BiliChangeRecorder
{
	@JavascriptInterface
	public void onChange(String msg)
	
	@JavascriptInterface
	public void onConnect()
	
	@JavascriptInterface
	public void onBreak()
}
