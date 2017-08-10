package com.edplan.bilipixeldraw;

import com.just.library.*;
import java.util.*;

public class BiliStateGetter
{
	
	public static boolean hasLogin(){
		//Map<String,String> cookies=WebUtil.parseCookies(getBiliCookie());
		return BiliDrawer.getStatus(getBiliCookie()).isLogin; //(cookies!=null)?(cookies.containsKey("DedeUserID")):false;
	}
	
	public static String getBiliCookie(){
		return AgentWebConfig.getCookiesByUrl("https://www.bilibili.com/");
	}
	
}
