package com.edplan.bilipixeldraw.widget;
import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.edplan.bilipixeldraw.R;

public class ColorSelectDialog extends Dialog
{
	public ColorSelectDialog(Context context,int theme){
		super(context,theme);
		setContentView(R.layout.main_colorselecter);
		
		getRootView().setOnTouchListener(new View.OnTouchListener(){
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getX() < getMainView().getLeft() 
						|| event.getX() >getMainView().getRight()
						|| event.getY() > getMainView().getBottom() 
						|| event.getY() < getMainView().getTop()) {
						dismiss();
					}
					return false;
				}
			});
	}
	
	
	public View getRootView(){
		return findViewById(R.id.dialog_rootView);
	}
	
	public View getMainView(){
		return findViewById(R.id.dialog_mainView);
	}
	

	@Override
	public void dismiss()
	{
		// TODO: Implement this method
		Animation anim=AnimationUtils.loadAnimation(getContext(),R.anim.dialog_main_hide_amination);
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
					getMainView().post(new Runnable() {
							@Override
							public void run() {
								ColorSelectDialog.super.dismiss();
							}
						});
				}

				@Override
				public void onAnimationRepeat(Animation p1)
				{
					// TODO: Implement this method
				}


			});

		getRootView().startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.dialog_root_hide_amin));
		getMainView().startAnimation(anim);

		//super.dismiss();
	}
	
	@Override
	public void show()
	{
		// TODO: Implement this method
		super.show();
		getRootView().startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.dialog_root_show_amin));
		getMainView().startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.dialog_main_show_amination));
	}
	
}
