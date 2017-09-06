package com.edplan.bilipixeldraw;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.CheckBox;

public class FinalActivity extends AppCompatActivity
{
	public static final String text="      这次绘版活动，，我已经不知道该说什么了。\n      作为最先开始使用b站api画点的人之一，我一直坚持不做多开和自动化，但还是搞到现在这样子了。。。→_→\n      这个是真▪最终版本了，我把上次画板的和前几天的画板截图加了进来，各位自己感受感受吧，捣乱的随他们去，主界面往下拖就可以看到完整的图了（第二个画板是8月19号的图），这里是他们不可能侵犯的地方。\n\n      ✺◟(∗❛ัᴗ❛ั∗)◞✺";
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.final_layout);
		
		TextView t=(TextView) findViewById(R.id.finallayoutTextView1);
		t.setText(text);
		Button b=(Button) findViewById(R.id.finallayoutButton1);
		b.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					
					CheckBox cb=(CheckBox) findViewById(R.id.finallayoutCheckBox1);
					
					if(cb.isChecked()){
						StaticM.putString(MainActivity.final_key,MainActivity.final_key);
					}
					
					finish();
				}
			});
	}
}
