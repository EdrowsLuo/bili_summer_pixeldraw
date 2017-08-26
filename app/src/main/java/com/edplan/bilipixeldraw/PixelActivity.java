package com.edplan.bilipixeldraw;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.edplan.bilipixeldraw.layers.BitmapPartLayer;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

public class PixelActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_pixel_view);
		
		StaticM.ini(this,findViewById(R.id.fab_exit));
		
		
		Intent i=getIntent();
		
		Uri uri=i.getData();
		//Log.v("intent", uri.getPath());
		
		LayerDrawView layerDrawView=(LayerDrawView) findViewById(R.id.mainLayerDrawView1);
		
		BitmapPartLayer bpl=new BitmapPartLayer();
		
		bpl.setBackgroundColor(0xff000000);
		bpl.setBackground(BitmapFactory.decodeResource(getResources(),R.drawable.page_light_wide_big));
		bpl.setBitmap(BitmapFactory.decodeFile(uri.getPath()));
		
		layerDrawView.addLayer(bpl);
	}
}
