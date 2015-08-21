package com.youti.yonghu.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ab.activity.AbActivity;
import com.example.youti_geren.R;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.Constants;
import com.youti.appConfig.YoutiApplication;
import com.youti.chat.activity.ChatActivity;
import com.youti.utils.IntentJumpUtils;

//首頁 快速预定
public class FastBookActivity extends AbActivity implements OnClickListener{

	
	private Button btBook, btPhone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.fast_book);
		// 图片的下载
		iniView();
	}
	public final String mPageName="FastBookActivity";
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

	private void iniView() {
		
		btPhone = (Button) findViewById(R.id.bt_phone400);
		btBook = (Button) findViewById(R.id.bt_online);
		
		btPhone.setOnClickListener(this);
		btBook.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.bt_phone400:
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel://400-696-8080"));    
            startActivity(intent); 
			break;
		case R.id.bt_online:
			if(YoutiApplication.getInstance().myPreference.getHasLogin()){
				Bundle b = new Bundle();
				b.putString(Constants.KEY_CHAT_USERNAME, Constants.CHAT_KEFU_NAME);
				b.putString(Constants.KEY_CHAT_TEL, Constants.CHAT_KEFU_NAME1);
				b.putInt("chatType", 1);
				IntentJumpUtils.nextActivity(ChatActivity.class,this, b);
			}else {
				IntentJumpUtils.nextActivity(LoginActivity.class, this, null);
			}
			break;

		default:
			break;
		}
	}

}
