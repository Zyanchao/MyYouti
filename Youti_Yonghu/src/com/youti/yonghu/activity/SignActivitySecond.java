package com.youti.yonghu.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.youti_geren.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;

public class SignActivitySecond extends Activity implements OnClickListener {

	
	String yanzhengma,recomcode;
	String phone="";
	String paw="";
	String scode="";
	EditText et_yanzhengma,et_recomcode;
	Button sendAgain,btn_finish;
	int count=60;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_signsecond);
		initView();
		initData();
		
		
	}
	public final String mPageName ="SignActivitySecond";
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
	private void initData() {
		//
		if(getIntent()!=null){
			phone=getIntent().getStringExtra("phone");
			paw=getIntent().getStringExtra("password");
			scode=getIntent().getStringExtra("scode");
		}
		//开始倒计时
		sendAginCount();
	}

	private void initView() {
		et_yanzhengma=(EditText) findViewById(R.id.et_yanzhengma);
		et_recomcode=(EditText)findViewById(R.id.recomcode);
		
		btn_finish=(Button) findViewById(R.id.btn_finish);		
		btn_finish.setOnClickListener(this);
		
		
		sendAgain =(Button) findViewById(R.id.sendagain);
		sendAgain.setOnClickListener(this);
		sendAgain.setClickable(false);
		
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sendagain:
			//重新发送验证码
			sendAginCount();
			reQuestScode();
			break;
		case R.id.btn_finish:
			//点击完成发送
			submit();
			break;
		default:
			break;
		}
	}
	/**
	 * 重新获取验证码
	 */
	private void reQuestScode() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(); // 绑定参数
		//params.put("key", ((YoutiApplication)getApplication()).getUuid());
		//params.put("utype", ((YoutiApplication)getApplication()).getUtype());
		params.put("tel_phone", phone);
	//	params.put("scode", scode);
		//params.put("op", 0);
		params.put("login_pwd", paw);
		params.put("invite_code", recomcode);
        HttpUtils.post("http://112.126.72.250/ut_app/index.php?m=User&a=reg_sendsms", params, new JsonHttpResponseHandler() {
            public void onStart() {  
                super.onStart();  
            } 
            public void onFailure(int statusCode,
                    org.apache.http.Header[] headers,
                    java.lang.Throwable throwable,
                    org.json.JSONObject errorResponse) {    
            };
            public void onFinish() {
            };
            public void onSuccess(int statusCode,
                    org.apache.http.Header[] headers,
                    org.json.JSONObject response){ 
                try {
                	//接口返回
                	String state =  response.getString("code");
                	if(state.equals("1")){
                		Utils.showToast(SignActivitySecond.this, "重新获取验证码成功");
                	}else{
                		Utils.showToast(SignActivitySecond.this, response.getString("msg"));
                	}
                } catch (Exception e) {
                	
                }
            };
        });
	}
	
	/**
	 * 通过handler延迟一秒修改页面
	 */
	Handler mHandler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			sendAgain.setText(getResources().getString(R.string.register2_send_again, ""+(count--)));
			if(count > 0){
				mHandler.sendEmptyMessageDelayed(0, 1000);
			}else{
				sendAgain.setText(getResources().getString(R.string.register2_send_again, ""));
				sendAgain.setTextColor(Color.parseColor("#666666"));
				sendAgain.setClickable(true);
			}
		};
	};
	/**
	 * 发送验证码倒计时
	 */
	private void sendAginCount(){
		sendAgain.setTextColor(Color.parseColor("#999999"));
		sendAgain.setClickable(false);
		count = 60;
		mHandler.sendEmptyMessage(0);
	}
	/**
	 * 请求网络
	 */
	private void submit() {
		yanzhengma=et_yanzhengma.getText().toString().replace(" ", "");
		recomcode=et_recomcode.getText().toString().replace(" ", "");
		if(TextUtils.isEmpty(yanzhengma)){
			Utils.showToast(SignActivitySecond.this, "验证码不能为空");
			return;
		}
		
		RequestParams params = new RequestParams(); // 绑定参数
/*		params.put("key", ((YoutiApplication)getApplication()).getUuid());
		params.put("utype", ((YoutiApplication)getApplication()).getUtype());
		params.put("recomcode", recomcode);
		params.put("phone", phone);*/
		
		params.put("tel_phone", phone);
		params.put("login_pwd", paw);
		params.put("invite_code", recomcode);
		params.put("code", yanzhengma);
        HttpUtils.post("http://112.126.72.250/ut_app/index.php?m=User&a=reg_sendsms", params, new JsonHttpResponseHandler() {
            public void onStart() {  
                super.onStart();  
            } 
            public void onFailure(int statusCode,
                    org.apache.http.Header[] headers,
                    java.lang.Throwable throwable,
                    org.json.JSONObject errorResponse) {
            	Utils.showToast(SignActivitySecond.this, "无法连接服务器，请稍后重试");
            };
            public void onFinish() {
            };
            public void onSuccess(int statusCode,
                    org.apache.http.Header[] headers,
                    org.json.JSONObject response){ 
                try {
                	//接口返回
                	String state =  response.getString("code");
                	if(state.equals("1")){
                		Bundle b = new Bundle();
                		b.putString("phone", phone);
                		/**
                		 * 从服务器获取到token
                		 */
                	//	b.putString("token", response.getString("info"));
                		/**
                		 * 注册成功后，跳转到个人中心，设置头像
                		 */
                		nextActivity(LoginActivity.class, b);
                		finish();
                		//((YoutiApplication) getApplication()).myPreference.setHasLogin(true);
                	}else{
                		Utils.showToast(SignActivitySecond.this, response.getString("info"));
                	}
                } catch (Exception e) {
                	
                }
            };
        });
	}

	private void nextActivity(Class<?> next, Bundle b) {
		Intent intent = new Intent();
		intent.setClass(this, next);
		if (b != null) {
			intent.putExtras(b);
		}
		startActivity(intent);
	}
}
