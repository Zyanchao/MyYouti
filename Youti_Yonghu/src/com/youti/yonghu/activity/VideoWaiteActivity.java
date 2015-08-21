package com.youti.yonghu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.umeng.analytics.MobclickAgent;

public class VideoWaiteActivity extends Activity{
	TextView tv_time;
	int time=3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_videowaiteactivity);
		tv_time=(TextView) findViewById(R.id.tv_time);
		
		handler.sendEmptyMessageDelayed(0, 1000);
	}
	
	public final String mPageName = "VideoWaiteActivity";
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart( mPageName );
		MobclickAgent.onResume(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd( mPageName );
		MobclickAgent.onPause(this);
	}
	Handler handler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			tv_time.setText(--time+"");
			if(time==0){
				handler.removeMessages(0);
				
				finish();
			}
			handler.sendEmptyMessageDelayed(0, 1000);
		};
	};
}
