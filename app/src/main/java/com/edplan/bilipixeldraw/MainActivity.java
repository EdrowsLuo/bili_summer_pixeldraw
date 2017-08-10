package com.edplan.bilipixeldraw;

import android.content.Intent;
import android.graphics.BitmapFactory;
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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.edplan.bilipixeldraw.layers.BitmapPartLayer;
import com.edplan.bilipixeldraw.layers.ColorFieldLayer;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
	LayerDrawView ldw;
	LayerDrawView colorSelecter;
	BiliBitmapParser bbp;
	CoordinatorLayout n;
	BiliDrawer biliDrawer;

	private BitmapPartLayer defLayer;

	private ColorFieldLayer cfl;
	
	
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
						StaticM.makeSnakeBar("时间还没到喵(^･ｪ･^)",null);
						return;
					}
					
					if(defLayer.getSelectedPoint()==null){
						StaticM.makeSnakeBar("(σ′▽‵)′▽‵)σ 你还没告诉我画哪啊",null);
						return;
					}
					
					if(cfl.getSelectedPoint()==null){
						StaticM.makeSnakeBar("| ू•ૅω•́)ᵎᵎᵎ 所以到底画什么颜色",null);
						showColorSelectBar();
						return;
					}
					
					//if(lastClickTime==0)lastClickTime=System.c
					
					if(System.currentTimeMillis()-lastClickTime>2000){
						StaticM.makeSnakeBar("你确定要画吗？ 确定请再点一次(ง •̀_•́)ง",null);
						lastClickTime=System.currentTimeMillis();
						return;
					}
					drawPixel();
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
		
		cfl=new ColorFieldLayer();
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
		
		
		
		defLayer=new BitmapPartLayer();
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
		defLayer.setBitmap(bbp.bitmap);
		defLayer.setBitmapCenter(10, 10);
		defLayer.setZoomTimes(1080f/50);
		defLayer.setBackgroundColor(0xff000000);
		defLayer.setBackground(BitmapFactory.decodeResource(getResources(),R.drawable.page_light_wide_big));
		
		//WebUtil.startLoginActivity(this);
		
		biliDrawer=new BiliDrawer(BiliStateGetter.getBiliCookie());
		
		
		timer.start();
		
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
						StaticM.makeSnakeBar("出。。。出错了(●—●)",null);
					}else if(r.code==BiliDrawer.CODE_DRAW_JAVAERR){
						StaticM.makeSnakeBar("出错了(ノ=Д=)ノ┻━┻ : "+r.message,null);
					}else if(r.code==BiliDrawer.CODE_DRAW_OK){
						StaticM.toast("成功画点 (｢･ω･)｢嘿");
						bbp.update();
					}else if(r.code==BiliDrawer.CODE_DRAW_OVERTIME){
						StaticM.makeSnakeBar("时间还没到喵(^･ｪ･^)",null);
					}else if(r.code==BiliDrawer.CODE_DRAW_NOT_LOGIN){
						StaticM.makeSnakeBar("（｡ò ∀ ó｡）还没登录，点→登录", new View.OnClickListener(){
								@Override
								public void onClick(View p1)
								{
									// TODO: Implement this method
									WebUtil.startLoginActivity(MainActivity.this);
								}
							});
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
	
	
}
