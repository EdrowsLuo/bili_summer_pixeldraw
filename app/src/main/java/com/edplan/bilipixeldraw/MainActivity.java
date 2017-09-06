package com.edplan.bilipixeldraw;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.edplan.bilipixeldraw.graphics.BitmapUtils;
import com.edplan.bilipixeldraw.graphics.Point;
import com.edplan.bilipixeldraw.graphics.PointF;
import com.edplan.bilipixeldraw.layers.BitmapPartLayer;
import com.edplan.bilipixeldraw.layers.ColorFieldLayer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import com.edplan.bilipixeldraw.layers.AnimaLayer;
import com.edplan.bilipixeldraw.layers.BiliCanvasLayer;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
	LayerDrawView ldw;
	LayerDrawView colorSelecter;
	BiliBitmapParser bbp;
	CoordinatorLayout n;
	BiliDrawer biliDrawer;
	
	
	BiliChangeRecorder changeRecorder;

	private BiliCanvasLayer defLayer;

	private ColorFieldLayer cfl;

	private AnimaLayer animaLayer;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		//if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
		//	WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
		//	localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
		//}
		
		try
		{
			File d=new File(Environment.getExternalStorageDirectory(), "edplan");
			if(!d.exists())d.mkdirs();
			File f=new File(d,"log");
			if(!f.exists())f.createNewFile();
			Runtime.getRuntime().exec("logcat -f " + (f.getAbsolutePath()));
		}
		catch (IOException e)
		{}

        setContentView(R.layout.main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
		
		LinearLayout fabLayout=(LinearLayout) findViewById(R.id.fabLayout);
		
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
			this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
		
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					//Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    //    .setAction("Action", null).show();
					bbp.update();
				}
			});
			
		final FloatingActionButton fab_draw=(FloatingActionButton) findViewById(R.id.fab_draw);
		fab_draw.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					if(getColorSelectBar().getVisibility()==View.GONE){
						showColorSelectBar();
					}else{
						hideColorSelectBar();
					}
				}
			});
			
		FloatingActionButton fab_drawPixel=(FloatingActionButton) findViewById(R.id.fab_drawPixel);
		
		fab_drawPixel.setOnClickListener(new View.OnClickListener(){
			long lastClickTime=0;
				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					
					if(biliDrawer.getTimeRemind()!=0){
						StaticM.makeSnakeBar("时间还没到喵(^･ｪ･^)");
						return;
					}
					
					if(defLayer.getSelectedPoint()==null){
						StaticM.makeSnakeBar("(σ′▽‵)′▽‵)σ 你还没告诉我画哪啊");
						return;
					}
					
					if(cfl.getSelectedPoint()==null){
						StaticM.makeSnakeBar("| ू•ૅω•́)ᵎᵎᵎ 所以到底画什么颜色");
						showColorSelectBar();
						return;
					}
					
					//if(lastClickTime==0)lastClickTime=System.c
					
					if(System.currentTimeMillis()-lastClickTime>2000){
						StaticM.makeSnakeBar("你确定要画吗？ 确定请再点一次(ง •̀_•́)ง");
						lastClickTime=System.currentTimeMillis();
						return;
					}
					drawPixel();
				}
		});
		
		FloatingActionButton fab_showModel=(FloatingActionButton) findViewById(R.id.fab_show_model);
		fab_showModel.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View fab)
				{
					// TODO: Implement this method
					if(defLayer.bitmapOverlay==null){
						StaticM.makeSnakeBar("你还没有设置模板哦_(:з」∠)_", new View.OnClickListener(){
								@Override
								public void onClick(View p1)
								{
									// TODO: Implement this method
									startAddModel();
								}
							}, "去设置");
					}else{
						if(defLayer.isBitmapOverlayVisible()){
							defLayer.setBitmapOverlayVisible(false);
							((FloatingActionButton)fab).setImageResource(R.drawable.ic_visibility_off_black_48dp);
						}else{
							defLayer.setBitmapOverlayVisible(true);
							((FloatingActionButton)fab).setImageResource(R.drawable.ic_visibility_white_48dp);
						}
					}
				}
			});
		
		
		final TextView timerText=(TextView) findViewById(R.id.timerText);
		
		Thread timer=new Thread(){
			public int deltaTime=200;
			@Override
			public void run(){
				while(true){
					runOnUiThread(new Runnable(){
							@Override
							public void run()
							{
								// TODO: Implement this method
								timerText.setText(biliDrawer.getTimeRemind()+"s");
							}
						});
					try
					{
						sleep(deltaTime);
					}
					catch (InterruptedException e)
					{}
				}
			}
		};
		
		
		
		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
		
		StaticM.ini(this,fabLayout);
		
		//Log.v("has test",BiliStateGetter.hasLogin()+"");
		
		ldw=(LayerDrawView) findViewById(R.id.mainLayerDrawView1);
		
		colorSelecter=(LayerDrawView) findViewById(R.id.colorSelecter);
		
		cfl=new ColorFieldLayer(6,6);
		colorSelecter.addLayer(cfl);
		for(int i=0;i<BiliBitmapParser.colors.length;i++){
			cfl.addColor(BiliBitmapParser.colors[i]);
		}
		cfl.setOnColorSelectListener(new ColorFieldLayer.OnColorSelectListener(){
				@Override
				public void onSelect(int color)
				{
					// TODO: Implement this method
					fab_draw.setBackgroundColor(color);
				}
			});
		
		
		
		defLayer=new BiliCanvasLayer();
		//new BitmapPartLayer();
		ldw.addLayer(defLayer);
		
		
		
		bbp=new BiliBitmapParser();
		bbp.update();
		
		/*try
		{
			defLayer.setBitmap(BitmapFactory.decodeStream(getAssets().open("id_372.png")));
		}
		catch (IOException e)
		{
			Log.e("decode",e.getMessage(),e);
		}*/
		//defLayer.setBitmap(bbp.bitmap);
		defLayer.setBiliBitmapParser(bbp);
		defLayer.setBitmapCenter(10, 10);
		defLayer.setZoomTimes(1080f/50);
		defLayer.setBackgroundColor(0xff000000);
		defLayer.setBackground(BitmapFactory.decodeResource(getResources(),R.drawable.page_light_wide_big));
		
		if(StaticM.getFloat("def_x")!=-1){
			defLayer.setCenterPoint(new PointF(StaticM.getFloat("def_x"),StaticM.getFloat("def_y")));
			defLayer.setZoomTimes(StaticM.getFloat("def_zoom"));
		}
		
		if(getModelX()!=-1){
			postModel();
		}
		
		
		animaLayer=new AnimaLayer(defLayer);
		
		ldw.addLayer(animaLayer);
		
		//WebUtil.startLoginActivity(this);
		
		biliDrawer=new BiliDrawer(BiliStateGetter.getBiliCookie());
		
		timer.start();
		
		setUpChangeRecorder();
		
		
		
		checkFinal();
		
    }
	static final String final_key="final_done";
	public void checkFinal(){
		if(StaticM.getString(final_key)!=final_key){
			Intent i=new Intent();
			i.setAction("com.edplan.summerpixeldraw.final");
			startActivity(i);
		}else{
			
		}
	}
	
	
	WebView web;
	
	public void setUpChangeRecorder(){
		
		changeRecorder = new BiliChangeRecorder(){
			@Override
			@JavascriptInterface
			public void onChange(String msg)
			{
				// TODO: Implement this method
				String[] s=msg.split(" ");
				if(s.length!=3)return;
				int x=Integer.parseInt(s[0]);
				int y=Integer.parseInt(s[1]);
				bbp.setPixel(x,y,s[2].charAt(0));
				
				AnimaLayer.AnimaEntry e=new AnimaLayer.AnimaEntry(
					x+0.5f,
					y+0.5f,
					BiliBitmapParser.parseColor(s[2].charAt(0))
				);
				
				animaLayer.add(e);
				
				//Log.v("js","change ("+s[2]+","+s[1]+") to "+s[2]);
			}

			@Override
			@JavascriptInterface
			public void onConnect()
			{
				// TODO: Implement this method
				Log.v("js","connect");
			}

			@Override
			@JavascriptInterface
			public void onBreak()
			{
				// TODO: Implement this method
				bbp.update();
				Log.v("js","break");
			}
		};
		/*
		
		WebView web=new WebView(this);
		
		web.getSettings().setJavaScriptEnabled(true);
		web.addJavascriptInterface(changeRecorder,"changeRecorder");
		web.loadUrl("file:///android_asset/test.html");
		*/
		
		
		web=new WebView(this); //(WebView) findViewById(R.id.mainWebView1);

		//JavascriptInterface jsinterface;

		JSCallback jscallback=new JSCallback();


		web.getSettings().setJavaScriptEnabled(true);
		web.addJavascriptInterface(jscallback,"jscallback");


		web.setWebChromeClient(new WebChromeClient() { 



				@Override 
				public boolean onJsAlert(WebView view, String url, String message, 
										 final JsResult result) { 
					//AlertDialog.Builder builder = new AlertDialog.Builder(mContext); 
					//builder.setMessage(message) 
					//   .setNeutralButton("确定", new OnClickListener() { 
					//	   @Override 
					//	   public void onClick(DialogInterface arg0, int arg1) { 
					//		   arg0.dismiss(); 
					//	   } 
					//   }).show(); 
					//result.cancel();
					Log.e("js",message);
					Log.e("js",result.toString());
					return true; 
				} 

				@Override 
				public boolean onJsConfirm(WebView view, String url, 
										   String message, final JsResult result) { 
					// TODO Auto-generated method stub 
					Log.i("js", "onJsConfirm" + "," + "url: " + url); 

					/*DialogUtils.dialogBuilder(mContext, "温馨提示", message, 
					 new DialogCallBack() { 

					 @Override 
					 public void onCompate() { 
					 Log.i(TAG, "onJsConfirm,onCompate"); 
					 result.confirm(); 
					 } 

					 @Override 
					 public void onCancel() { 
					 Log.i(TAG, "onJsConfirm,onCancel"); 
					 result.cancel(); 
					 } 
					 }); */
					return true; 
				} 
			}); 

		web.loadUrl("file:///android_asset/test2.html");
		
		
	}
	
	public class JSCallback{

		@JavascriptInterface
		public void callBack(String s){
			Log.v("call back main",s);
			changeRecorder.onChange(s);
		}


	}

	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		//WUYHGYCN37
		//GLYH8YCN7F
		//GVUFHUUWMP
		super.onResume();
		StaticM.ini(this,findViewById(R.id.fabLayout));
	}
	
	
	

	@Override
	protected void onStop()
	{
		// TODO: Implement this method
		super.onStop();
		
		StaticM.putFloat("def_x",defLayer.getCenterPoint().x);
		StaticM.putFloat("def_y",defLayer.getCenterPoint().y);
		StaticM.putFloat("def_zoom",defLayer.getZoomTimes());
		
		
	}
	
	public void drawPixel(){
		biliDrawer.setCookie(BiliStateGetter.getBiliCookie());
		biliDrawer.drawSynced(defLayer.getSelectedPoint().x,defLayer.getSelectedPoint().y,String.valueOf(BiliBitmapParser.getChar(cfl.getSelectedColor())),
			new BiliDrawer.OnResultListener(){
				@Override
				public void onResult(BiliDrawer.Result r)
				{
					// TODO: Implement this method
					if(r==null){
						StaticM.makeSnakeBar("出。。。出错了(●—●)");
					}else if(r.code==BiliDrawer.CODE_DRAW_JAVAERR){
						StaticM.makeSnakeBar("出错了(ノ=Д=)ノ┻━┻ : "+r.message);
					}else if(r.code==BiliDrawer.CODE_DRAW_OK){
						StaticM.toast("成功画点 (｢･ω･)｢嘿");
						bbp.update();
					}else if(r.code==BiliDrawer.CODE_DRAW_OVERTIME){
						StaticM.makeSnakeBar("时间还没到喵(^･ｪ･^)");
					}else if(r.code==BiliDrawer.CODE_DRAW_NOT_LOGIN){
						StaticM.makeSnakeBar("（｡ò ∀ ó｡）还没登录，点→登录", new View.OnClickListener(){
								@Override
								public void onClick(View p1)
								{
									// TODO: Implement this method
									WebUtil.startLoginActivity(MainActivity.this);
								}
							},
							"登录"
						);
					}
				}
			});
	}
	
	public View getColorSelectBar(){
		return findViewById(R.id.mainColorSelectView);
	}
	
	public void hideColorSelectBar(){
		final View mainColorSelectView=findViewById(R.id.mainColorSelectView);
		//if(mainColorSelectView.getVisibility()==View.VISIBLE){
		//	mainColorSelectView.setVisibility(View.GONE);
		//}else{
			
			
		Animation anim=AnimationUtils.loadAnimation(this,android.R.anim.fade_out);
		anim.setDuration(100);
		anim.setAnimationListener(new Animation.AnimationListener(){
				@Override
				public void onAnimationStart(Animation p1)
				{
					// TODO: Implement this method
				}

				@Override
				public void onAnimationEnd(Animation p1)
				{
					// TODO: Implement this method
					mainColorSelectView.setVisibility(View.GONE);
				}

				@Override
				public void onAnimationRepeat(Animation p1)
				{
					// TODO: Implement this method
				}
			});
			
			
		mainColorSelectView.startAnimation(anim);
		//}
	}
	
	
	public void showColorSelectBar(){
		
		//LayerDrawView colorSelecter=(LayerDrawView) findViewById(R.id.colorSelecter);
		View mainColorSelectView=findViewById(R.id.mainColorSelectView);
		//if(mainColorSelectView.getVisibility()==View.VISIBLE){
		//	mainColorSelectView.setVisibility(View.GONE);
		//}else{
			mainColorSelectView.setVisibility(View.VISIBLE);
		//
		
		Animation anim=AnimationUtils.loadAnimation(this,android.R.anim.fade_in);
		anim.setDuration(100);
		mainColorSelectView.startAnimation(anim);
		
		//ColorSelectDialog dlog=new ColorSelectDialog(this,R.style.DialogTheme);
		//dlog.show();
		
		
		//ColorSelectPopupWindow pwd=new ColorSelectPopupWindow(this);
		//PopupWindowCompat.showAsDropDown(pwd,findViewById(R.id.mainLayout),0,0,Gravity.BOTTOM);
		
		
		
		//pwd.showAtLocation(findViewById(R.id.mainLayout),Gravity.BOTTOM,0,0);
		
		
		
		//PopupMenu pm=new PopupMenu(this,findViewById(R.id.fabLayout));
		//pm.inflate(R.menu.activity_main_drawer);
		//pm.show();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
	
	@Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
	}


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
		
		//StaticM.toast("select "+id);
		
        if (id == R.id.nav_login) {
            WebUtil.startLoginActivity(this);
        } else if (id == R.id.nav_share) {
			//教程
			Intent i=new Intent();
			i.setAction("com.edplan.jc");
			startActivity(i);
        } else if (id == R.id.nav_send) {
			//ppy 
			Intent i=new Intent(Intent.ACTION_VIEW,Uri.parse("http://osu.ppy.sh/"));
			startActivity(i);
        }else if(id == R.id.nav_save){
			try
			{
				final File f=compressCanvas();
				StaticM.makeSnakeBar("已保存至 " + f.getAbsolutePath() + " →查看", new View.OnClickListener(){
						@Override
						public void onClick(View p1)
						{
							// TODO: Implement this method
							Intent intent=new Intent();
							intent.setAction(Intent.ACTION_VIEW);
							intent.setDataAndType(Uri.fromFile(f),"image/*");
							startActivity(intent);
						}
					},
					"打开/分享"
				);
			}
			catch (IOException e)
			{
				
			}
		}else if(id==R.id.nav_addmodel){
			startAddModel();
		}else if(id==R.id.nav_clip){
			startClip();
		}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
	
	public void startClip(){
		final AppCompatDialog dialog=new AppCompatDialog(this,R.style.DialogTheme);
		dialog.setContentView(R.layout.dialog_cilp_bitmap);
		dialog.setTitle("选择区域");
		
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener(){
				@Override
				public void onCancel(DialogInterface dialog)
				{
					// TODO: Implement this method
					dialog.dismiss();
				}
			});
		
		((Button)dialog.findViewById(R.id.dialog_clipbitmap_sureButton)).setOnClickListener(
			new View.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					try{
						int x1=Integer.parseInt(((EditText)dialog.findViewById(R.id.dialog_x1)).getText().toString());
						int x2=Integer.parseInt(((EditText)dialog.findViewById(R.id.dialog_x2)).getText().toString());
						int y1=Integer.parseInt(((EditText)dialog.findViewById(R.id.dialog_y1)).getText().toString());
						int y2=Integer.parseInt(((EditText)dialog.findViewById(R.id.dialog_y2)).getText().toString());
						
						try
						{
							final File f=compressCanvas(new Rect(x1, y1, x2, y2));
							StaticM.makeSnakeBar("已保存至 " + f.getAbsolutePath() + " →查看", new View.OnClickListener(){
									@Override
									public void onClick(View p1)
									{
										// TODO: Implement this method
										Intent intent=new Intent();
										intent.setAction(Intent.ACTION_VIEW);
										intent.setDataAndType(Uri.fromFile(f),"image/*");
										startActivity(intent);
									}
								},
								"打开/分享"
							);
						}
						catch (FileNotFoundException e)
						{
							
						}
						catch(IOException e){
							
						}
					}
					catch(NumberFormatException e){
						StaticM.toast("错误的参数");
					}
				}
			});
		
		dialog.show();
	}
	
	public void startAddModel(){
		final AppCompatDialog dialog=new AppCompatDialog(this,R.style.DialogTheme);
		dialog.setContentView(R.layout.dialog_add_model);
		dialog.setTitle("设置模板");
		
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener(){
				@Override
				public void onCancel(DialogInterface dialog)
				{
					// TODO: Implement this method
					dialog.dismiss();
				}
			});
			
		final EditText edit_x=(EditText) dialog.findViewById(R.id.dialog_addmodel_x);
		final EditText edit_y=(EditText) dialog.findViewById(R.id.dialog_addmodel_y);
		final EditText edit_path=(EditText) dialog.findViewById(R.id.dialog_addmodel_path);
		
		if(getModelX()!=-1){
			edit_x.setText(getModelX()+"");
			edit_y.setText(getModelY()+"");
			edit_path.setText(getModelFilePath());
		}
		
		
		
		Button sureButton=(Button) dialog.findViewById(R.id.dialog_addmodel_sureButton);
		
		sureButton.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					if(((edit_x.getText().toString().length()<1||
					   edit_y.getText().toString().length()<1))){
						StaticM.toast("没填数据喵(^･ｪ･^)");
					}else{
						String msg=setModel(
							Integer.parseInt(edit_x.getText().toString()),
							Integer.parseInt(edit_y.getText().toString()),
							edit_path.getText().toString()
						);
						if(msg!="true"){
							StaticM.toast(msg+" (●—●)");
						}else{

							dialog.dismiss();
							StaticM.toast("设置模板成功");
						}
					}
				}
			});
		
		
		dialog.show();
	}
	
	public void postModel(){
		defLayer.setBitmapOverlay(BitmapFactory.decodeFile(getModelFilePath()),new Point(getModelX(),getModelY()));
	}
	
	
	public String setModel(int x,int y,String path){
		
		Bitmap bmp=BitmapFactory.decodeFile(path);
		if(bmp==null){
			return "文件不是图片或无法解析";
		}else{
			if(bmp.getWidth()+x<=defLayer.getBitmap().getWidth()&&bmp.getHeight()+y<=defLayer.getBitmap().getHeight()){
				setModelX(x);
				setModelY(y);
				setModelFilePath(path);
				postModel();
				return "true";
			}else{
				return "贴图超过画板边缘";
			}
		}
	}
	
	public int getModelX(){
		return StaticM.getInt("model_x");
	}
	
	public int getModelY(){
		return StaticM.getInt("model_y");
	}
	
	public String getModelFilePath(){
		return StaticM.getString("model_path");
	}
	
	public void setModelX(int x){
		StaticM.putInt("model_x",x);
	}
	
	public void setModelY(int y){
		StaticM.putInt("model_y",y);
	}
	
	public void setModelFilePath(String path){
		StaticM.putString("model_path",path);
	}
	
	public File compressCanvas(Rect rect) throws FileNotFoundException, IOException{
		File outPutFile=new File(StaticM.getMainPictureDir(),"part["+rect.left+","+rect.top+","+rect.right+","+rect.bottom+"]_"+(new Date()).toLocaleString()+".png");
		if(!outPutFile.exists()){
			outPutFile.createNewFile();
		}
		Bitmap bmp=Bitmap.createBitmap(rect.width(),rect.height(),Bitmap.Config.ARGB_8888);
		Canvas canvas=new Canvas(bmp);
		Paint paint=new Paint();
		paint.setAntiAlias(false);
		canvas.drawBitmap(defLayer.bitmap,rect,new Rect(0,0,canvas.getWidth(),canvas.getHeight()),paint);
		
		BitmapUtils.compressBitmap(bmp,outPutFile);
		
		return outPutFile;
	}
	
	public File compressCanvas() throws IOException{
		File outPutFile=new File(StaticM.getMainPictureDir(),"canvas_"+(new Date()).toLocaleString()+".png");
		if(!outPutFile.exists()){
			outPutFile.createNewFile();
		}
		bbp.bitmap.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(outPutFile));
		return outPutFile;
	}
	
	
}
