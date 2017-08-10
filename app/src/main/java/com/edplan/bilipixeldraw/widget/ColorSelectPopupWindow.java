package com.edplan.bilipixeldraw.widget;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.PopupWindow;
import com.edplan.bilipixeldraw.R;

public class ColorSelectPopupWindow extends PopupWindow
{
	
	public ColorSelectPopupWindow(Context cont){
		super(cont);
		setContentView(
			((LayoutInflater)cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.main_colorselecter,null)
			);
	}
	
}
