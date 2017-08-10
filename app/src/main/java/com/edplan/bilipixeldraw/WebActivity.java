package com.edplan.bilipixeldraw;

import android.app.*;
import android.os.*;
import android.util.*;
import android.webkit.*;
import android.widget.*;
import com.just.library.*;
import android.support.v7.app.AppCompatActivity;

public class WebActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.web);
		
		LinearLayout lin=(LinearLayout) findViewById(R.id.webLayout);
		
		AgentWeb agentWeb=AgentWeb.with(this)
			.setAgentWebParent(lin,new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams
			.useDefaultIndicator()// 使用默认进度条
			.defaultProgressBarColor() // 使用默认进度条颜色
			//.setReceivedTitleCallback(mCallback) //设置 Web 页面的 title 回调
			.setWebViewClient(new MyWebViewClient())
			.createAgentWeb()//
			.ready()
			.go("https://passport.bilibili.com/login");
		//.go("https://api.live.bilibili.com/activity/v1/SummerDraw/draw?x_max=1&y_max=1&x_min=1&y_min=1&color=1");
		//.go("https://www.bilibili.com/");

		//Log.v("cookie",AgentWebConfig.getCookiesByUrl("https://www.bilibili.com/")+"");
		
		
	}
	
	public class MyWebViewClient extends WebViewClient
	{



		//@Override
		//public boolean shouldOverrideUrlLoading(WebView view, String url)
		//{
		// TODO: Implement this method
		//	view.loadUrl(url);
		//	return true; //super.shouldOverrideUrlLoading(view, url);
		//}

		@Override
		public void onPageFinished(WebView view, String url)
		{
			// TODO: Implement this method
			super.onPageFinished(view, url);
			
			
			
			runOnUiThread(new Runnable(){

					@Override
					public void run()
					{
						// TODO: Implement this method
						if(BiliStateGetter.hasLogin()){
							StaticM.toast("已登录(⑉°з°)-♡ 摁返回键返回夏日绘版");
						}
					}
				});
			
			
			//Log.v("new cookie","finish page : "+url);
			//Log.v("new cookie","cookie : "+ BiliStateGetter.getBiliCookie());
			//parseCookies(AgentWebConfig.getCookiesByUrl("https://www.bilibili.com/"));

		}




	}
	
}
