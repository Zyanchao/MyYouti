package com.youti.yonghu.activity;

import java.util.ArrayList;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.YoutiApplication;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
import com.youti.view.TitleBar;
import com.youti.yonghu.bean.InfoBean;

public class ForgetPasswordActivity extends FragmentActivity {
	TitleBar titleBar;
	ArrayList<Fragment> list =new ArrayList<Fragment>();
	FrameLayout fl ;
	int currentPosition;
	Button btn_next;
	EditText phonenumber;
	public final String mPageName = "ForgetPasswordActivity";
	private String code;
	private String url;
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
		setContentView(R.layout.layout_forgetpassword);
		if(getIntent()!=null){
			code = getIntent().getStringExtra("code");
		}
		titleBar =(TitleBar) findViewById(R.id.index_titlebar);
	//	fl=(FrameLayout) findViewById(R.id.fl);
		titleBar.setTitleBarTitle("忘记密码");
		titleBar.setSearchGone();
		titleBar.setShareGone(false);
		btn_next=(Button) findViewById(R.id.btn_next);
		phonenumber=(EditText) findViewById(R.id.phonenumber);
		
		btn_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String phone = phonenumber.getText().toString().replace(" ", "");
				if(TextUtils.isEmpty(phone)||phone.length()!=11){
					Utils.showToast(ForgetPasswordActivity.this, "请核实手机号码，重新发送");
					return;
				}
				requestData(phone);
				
				
			}
		});
	}
	
	
	
	
	
	
	
	protected void requestData(final String phone) {
		RequestParams params =new RequestParams();
		params.put("tel_phone", phone);
		
			url="http://112.126.72.250/ut_app/index.php?m=User&a=find_pwd";
		
		HttpUtils.post(url, params, new TextHttpResponseHandler() {
			
		
			

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Utils.showToast(ForgetPasswordActivity.this, "网络连接异常");
				
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson =new Gson();
				InfoBean fromJson = gson.fromJson(arg2, InfoBean.class);
				if(fromJson.code.equals("403")){
					Utils.showToast(ForgetPasswordActivity.this, "密码错误");
				}else if(fromJson.code.equals("402")){
					Utils.showToast(ForgetPasswordActivity.this, "账号不存在");
				}else if(fromJson.code.equals("0")){
					Utils.showToast(ForgetPasswordActivity.this, "验证码发送失败");
				}else if(fromJson.code.equals("401")){
					Utils.showToast(ForgetPasswordActivity.this, "手机格式错误");
				}else {
					Utils.showToast(ForgetPasswordActivity.this, "验证码发送成功");
					((YoutiApplication)(getApplication())).myPreference.setTelNumber(phone);
					Intent intent =new Intent(ForgetPasswordActivity.this,ForgetPasswordtwoActivity.class);
					if("0".equals(code)){
						intent.putExtra("code", "0");
					}
					startActivity(intent);
					finish();
				}
			}
		});
	}
}
