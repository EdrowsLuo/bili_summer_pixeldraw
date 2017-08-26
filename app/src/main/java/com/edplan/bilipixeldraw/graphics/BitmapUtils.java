package com.edplan.bilipixeldraw.graphics;
import android.graphics.Bitmap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

public class BitmapUtils
{
	public static void compressBitmap(Bitmap bmp,File out) throws FileNotFoundException{
		bmp.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(out));
	}
}
