package com.youti.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.youti_geren.R;
import com.youti.appConfig.YoutiApplication;
import com.youti.utils.Utils;
import com.youti.yonghu.activity.EditDataActivity;
import com.youti.yonghu.activity.LoginActivity;
import com.youti.yonghu.activity.MoreActivity;
import com.youti.yonghu.activity.MyAccountActivity;
import com.youti.yonghu.activity.MyCoachActivity;
import com.youti.yonghu.activity.MyCourseActivity;
import com.youti.yonghu.activity.MyFavoriateActivity;
import com.youti.yonghu.activity.MyMessageActivity;
import com.youti.yonghu.activity.PersonCenterActivity;
import com.youti.yonghu.download.DownloadManagerActivity;

public class RightMenuView extends LinearLayout implements OnClickListener {

	RelativeLayout rl_user_icon,rl_edit,rl_account,rl_course,rl_coarch,rl_message,rl_download,rl_favoriate,rl_more;
	Context context;
	Intent intent;
	SharedPreferences sp;
	private boolean isLogin;
	public RightMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		inflate(context,R.layout.main_menu_right , this);
		context.getSharedPreferences("config",context.MODE_PRIVATE);
		rl_user_icon=(RelativeLayout) findViewById(R.id.rl_user_icon);
		rl_edit=(RelativeLayout) findViewById(R.id.rl_edit);
		rl_account=(RelativeLayout) findViewById(R.id.rl_account);
		rl_course=(RelativeLayout) findViewById(R.id.rl_course);
		rl_coarch=(RelativeLayout) findViewById(R.id.rl_coarch);
		rl_message=(RelativeLayout) findViewById(R.id.rl_message);
		rl_download=(RelativeLayout) findViewById(R.id.rl_download);
		rl_favoriate=(RelativeLayout) findViewById(R.id.rl_favorite);
		rl_more=(RelativeLayout) findViewById(R.id.rl_more);
		
		rl_user_icon.setOnClickListener(this);
		rl_edit.setOnClickListener(this);
		rl_account.setOnClickListener(this);
		rl_course.setOnClickListener(this);
		rl_coarch.setOnClickListener(this);
		rl_message.setOnClickListener(this);
		rl_download.setOnClickListener(this);
		rl_favoriate.setOnClickListener(this);
		rl_more.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.rl_user_icon:
			isLogin=((YoutiApplication)(context.getApplicationContext())).myPreference.getHasLogin();
			if(isLogin){
				intent =new Intent(context,PersonCenterActivity.class);				
			}else{
				intent = new Intent(context,LoginActivity.class);
			}
			context.startActivity(intent);
			break;
		case R.id.rl_edit:
			Utils.showToast(context, "编辑个人资料");
			isLogin=((YoutiApplication)(context.getApplicationContext())).myPreference.getHasLogin();
			if(isLogin){
				intent=new Intent(context,EditDataActivity.class);
			}else{
				intent = new Intent(context,LoginActivity.class);
			}
			context.startActivity(intent);
			break;
		case R.id.rl_account: 
			Utils.showToast(context, "个人账户");
			isLogin=((YoutiApplication)(context.getApplicationContext())).myPreference.getHasLogin();
			if(isLogin){
				intent=new Intent(context,MyAccountActivity.class);
			}else{
				intent = new Intent(context,LoginActivity.class);
			}
			context.startActivity(intent);
			break;
		case R.id.rl_course:
			Utils.showToast(context, "个人课程");
			isLogin=((YoutiApplication)(context.getApplicationContext())).myPreference.getHasLogin();
			if(isLogin){
				intent=new Intent(context,MyCourseActivity.class);
			}else{
				intent = new Intent(context,LoginActivity.class);
			}
			context.startActivity(intent);
			break;
		case R.id.rl_coarch:
			/*Utils.showToast(context, "个人教练");
			isLogin=((YoutiApplication)(context.getApplicationContext())).myPreference.getHasLogin();
			if(isLogin){
				intent=new Intent(context,MyCoachActivity.class);
			}else{
				intent = new Intent(context,LoginActivity.class);
			}*/
			intent=new Intent(context,MyCoachActivity.class);
			context.startActivity(intent);
			break;
		case R.id.rl_message:
			Utils.showToast(context, "个人消息");
			isLogin=((YoutiApplication)(context.getApplicationContext())).myPreference.getHasLogin();
			if(isLogin){
				intent=new Intent(context,MyMessageActivity.class);
			}else{
				intent = new Intent(context,LoginActivity.class);
			}
			context.startActivity(intent);
			break;
		case R.id.rl_download:
			Utils.showToast(context, "个人下载");
			intent =new Intent(context,DownloadManagerActivity.class);
			context.startActivity(intent);
			break;
		case R.id.rl_favorite:
			Utils.showToast(context, "个人最爱");
			isLogin=((YoutiApplication)(context.getApplicationContext())).myPreference.getHasLogin();
			if(isLogin){
				intent=new Intent(context,MyFavoriateActivity.class);
			}else{
				intent = new Intent(context,LoginActivity.class);
			}
			context.startActivity(intent);
			break;
		case R.id.rl_more:
			Utils.showToast(context, "更多");
			intent =new Intent(context,MoreActivity.class);
			context.startActivity(intent);
			break;
		}
	}
	
	
}
