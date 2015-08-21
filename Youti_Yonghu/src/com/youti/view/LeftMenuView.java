package com.youti.view;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.youti_geren.R;
import com.youti.yonghu.activity.CoachListActivity;
import com.youti.yonghu.activity.CommunityListActivity;
import com.youti.yonghu.activity.CourseListActivity;
import com.youti.yonghu.activity.VideoWaiteActivity;

public  class LeftMenuView extends RelativeLayout implements View.OnClickListener {

	
	
	protected Context context;
	private RelativeLayout coachBtn;
	private RelativeLayout courseBtn;
	private RelativeLayout vedioBtn;
	private RelativeLayout communityBtn;
	
	public LeftMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		inflate(context, R.layout.mian_menu_left, this);
		courseBtn = (RelativeLayout) findViewById(R.id.ll_menu_kc);//课程
		coachBtn = (RelativeLayout) findViewById(R.id.ll_menu_jl);//教练
		vedioBtn = (RelativeLayout) findViewById(R.id.ll_menu_sp);//视频
		communityBtn = (RelativeLayout) findViewById(R.id.ll_menu_sq);//社区
		
		coachBtn.setOnClickListener(this);
		courseBtn.setOnClickListener(this);
		vedioBtn.setOnClickListener(this);
		communityBtn.setOnClickListener(this);
		
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_menu_kc:
			if(selectIndex==0)return;
			nextActivity(CourseListActivity.class,null);
			break;
		case R.id.ll_menu_jl:
			if(selectIndex==1)return;
			nextActivity(CoachListActivity.class,null);
			break;
		case R.id.ll_menu_sp:
			if(selectIndex==2)return;
			nextActivity(VideoWaiteActivity.class,null);
			break;
		case R.id.ll_menu_sq:
			if(selectIndex==3)return;
			nextActivity(CommunityListActivity.class,null);
			break;
		default:
			break;
		}
		
	}
	
	private int selectIndex = -1;
	public void setSelectButton(int index){
		coachBtn.setSelected(false);
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
		}
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
