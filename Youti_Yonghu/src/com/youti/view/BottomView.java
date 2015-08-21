package com.youti.view;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.youti_geren.R;

public  class BottomView extends RelativeLayout implements View.OnClickListener {

	
	
	protected Context context;
	private LinearLayout one;
	
	public BottomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		inflate(context, R.layout.list_footer_item, this);
		one = (LinearLayout) findViewById(R.id.footer_xm);//课程
		
		
		one.setOnClickListener(this);
		
	}
	
	
	@Override
	public void onClick(View v) {
		
	}
	
	private int selectIndex = -1;
	public void setSelectButton(int index){
		/*coachBtn.setSelected(false);
		courseBtn.setSelected(false);
		vedioBtn.setSelected(false);
		communityBtn.setSelected(false);
		selectIndex = index;
		switch (index) {
		case 0:
			coachBtn.setSelected(true);
			break;
		case 1:
			courseBtn.setSelected(true);
			break;
		case 2:
			vedioBtn.setSelected(true);
			break;
		case 3:
			communityBtn.setSelected(true);
			break;
		default:
			break;
		}*/
	}
	
	private void nextActivity(Class<?> next, Bundle b) {
		Intent intent = new Intent();
		intent.setClass(this.context, next);
		if (b != null) {
			intent.putExtras(b);
		}
		context.startActivity(intent);
	}

}
