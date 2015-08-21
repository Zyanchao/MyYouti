package com.youti.yonghu.activity;

import java.util.ArrayList;

import org.apache.http.Header;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.YoutiApplication;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
import com.youti.view.TitleBar;
import com.youti.yonghu.bean.InfoBean;

public class ForgetPasswordtwoActivity extends FragmentActivity {
	TitleBar titleBar;
	ArrayList<Fragment> list =new ArrayList<Fragment>();
	FrameLayout fl ;
	int currentPosition;
	
	
	Button btn_next;
	EditText yanzhengma;
	private String phone;
	TextView sendAgain;
	
	public final String mPageName = "ForgetPasswordtwoActivity";
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
		setContentView(R.layout.layout_forgetpassword2);
		if(getIntent()!=null){
			code = getIntent().getStringExtra("code");
		}
		
		titleBar =(TitleBar) findViewById(R.id.index_titlebar);
	//	fl=(FrameLayout) findViewById(R.id.fl);
		titleBar.setTitleBarTitle("忘记密码");
		titleBar.setSearchGone();
		titleBar.setShareGone(false);
		
		btn_next=(Button) findViewById(R.id.btn_next);
		yanzhengma =(EditText) findViewById(R.id.yanzhengma);
		sendAgain=(TextView) findViewById(R.id.sendagain);
		
		//开始倒计时
		sendAginCount();
		sendAgain.setClickable(false);
		sendAgain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//重新发送验证码
				sendAginCount();
				reQuestScode();
			}
		});
		
		phone = ((YoutiApplication)getApplication()).myPreference.getTelNumber();
		btn_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String str=yanzhengma.getText().toString().replace(" ", "");
				if(TextUtils.isEmpty(str)){
					Utils.showToast(ForgetPasswordtwoActivity.this, "验证码不能为空");
					return;
				}
				requestData(str);
			}
		});
	}
	


	
	/**
	 * 重新获取验证码
	 */
	private void reQuestScode() {
		RequestParams params =new RequestParams();
		params.put("tel_phone", phone);
		HttpUtils.post("http://112.126.72.250/ut_app/index.php?m=User&a=find_pwd", params, new JsonHttpResponseHandler() {
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
                		Utils.showToast(ForgetPasswordtwoActivity.this, "重新获取验证码成功");
                	}else{
                		Utils.showToast(ForgetPasswordtwoActivity.this, response.getString("msg"));
                	}
                } catch (Exception e) {
                	
                }
            };
        });
	}
	
	
	int count=60;
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
	private String code;
	/**
	 * 发送验证码倒计时
	 */
	private void sendAginCount(){
		sendAgain.setTextColor(Color.parseColor("#999999"));
		sendAgain.setClickable(false);
		count = 60;
		mHandler.sendEmptyMessage(0);
	}
	
	protected void requestData(String str) {
		RequestParams params =new RequestParams();
		params.put("tel_phone", phone);
		params.put("code", str);
		HttpUtils.post("http://112.126.72.250/ut_app/index.php?m=User&a=check_code", params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson =new Gson();
				InfoBean fromJson = gson.fromJson(arg2, InfoBean.class);
				if(fromJson.code.equals("400")){
					Utils.showToast(ForgetPasswordtwoActivity.this, "手机号为空");
				}else if(fromJson.code.equals("401")){
					Utils.showToast(ForgetPasswordtwoActivity.this, "验证码为空");
				}else if(fromJson.code.equals("402")){
					Utils.showToast(ForgetPasswordtwoActivity.this, "验证码已经过期");
				}else if(fromJson.code.equals("0")){
					Utils.showToast(ForgetPasswordtwoActivity.this, "验证码错误");
				}else {
					Utils.showToast(ForgetPasswordtwoActivity.this, "验证码正确");
					//((ForgetPasswordActivity) getActivity()).switchPage(2);
					Intent intent =new Intent(ForgetPasswordtwoActivity.this,ForgetPasswordthreeActivity.class);
					if("0".equals(code)){
						intent.putExtra("code", "0");
					}
					startActivity(intent);
					finish();
				}
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Utils.showToast(ForgetPasswordtwoActivity.this, "网络连接异常");
			}
		});
	}
}
