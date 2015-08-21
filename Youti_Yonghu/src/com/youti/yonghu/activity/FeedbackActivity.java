package com.youti.yonghu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.youti_geren.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.YoutiApplication;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
import com.youti.view.TitleBar;

/**
 * http://112.126.72.250/ut_app/index.php?m=User&a=user_feedback
	传入参数:user_id:用户id
         content:用户反馈提交
	返回参数:
         code:400用户id为空
 		 code:401反馈内容为空
 		 code：0失败
 		 code：1成功 
 * @author xiaguangcheng
 *
 */

public class FeedbackActivity extends Activity implements OnClickListener {
	String url="http://112.126.72.250/ut_app/index.php?m=User&a=user_feedback";
	TitleBar titleBar;
	EditText et_feedback;
	Button btn_submit,btn_cancle;
	String userId;
	public final String mPageName = "FeedbackActivity";
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_feedback);
		titleBar =(TitleBar) findViewById(R.id.index_titlebar);
		titleBar.setTitleBarTitle("问题反馈");
		titleBar.setShareGone(false);
		titleBar.setSearchGone();
		et_feedback=(EditText) findViewById(R.id.et_feedback);
		btn_submit=(Button) findViewById(R.id.btn_submit);
		btn_cancle=(Button) findViewById(R.id.btn_cancle);
		btn_submit.setOnClickListener(this);
		btn_cancle.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			String text =et_feedback.getText().toString().replace(" ", "");
			if(TextUtils.isEmpty(text)){
				Utils.showToast(this, "提交的内容，最好不要为空");
				return;
			}
			boolean hasLogin = ((YoutiApplication)getApplication()).myPreference.getHasLogin();
			if(!hasLogin){
				Intent intent =new Intent(this,LoginActivity.class);
				startActivity(intent);
				finish();
				return;
			}
			userId=((YoutiApplication)getApplication()).myPreference.getUserId();
			submit(text,userId);
			break;
		case R.id.btn_cancle:
			finish();
			break;

		default:
			break;
		}
	}
	private void submit(String text,String userID) {
		RequestParams params =new RequestParams();
		params.put("user_id", userID);
		params.put("content", text);
		HttpUtils.post(url, params, new JsonHttpResponseHandler(){
			public void onStart() {  
                super.onStart();  
            } 
            public void onFailure(int statusCode,
                    org.apache.http.Header[] headers,
                    java.lang.Throwable throwable,
                    org.json.JSONObject errorResponse) {
            	Utils.showToast(FeedbackActivity.this, "无法连接服务器，请稍后重试");
            };
            public void onFinish() {
            };
            public void onSuccess(int statusCode,
                    org.apache.http.Header[] headers,
                    org.json.JSONObject response){ 
                try {
                	//接口返回
                	response.toString();
                	//为了重新发送验证码  需要把参数都传给下个activity
                	String state =  response.getString("code");
                	if(state.equals("1")){
                		Utils.showToast(FeedbackActivity.this, "提交成功");
            			finish();
                	}else{
                		Utils.showToast(FeedbackActivity.this, "提交失败");
                	}
                } catch (Exception e) {
                	
                }
            };
        
		});
	}
	
	
}
