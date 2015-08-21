package com.youti.yonghu.activity;

import java.util.ArrayList;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alipay.sdk.encrypt.MD5;
import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tencent.weibo.sdk.android.component.sso.tools.MD5Tools;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.YoutiApplication;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
import com.youti.view.TitleBar;
import com.youti.yonghu.bean.InfoBean;

public class ForgetPasswordthreeActivity extends FragmentActivity {
	TitleBar titleBar;
	ArrayList<Fragment> list =new ArrayList<Fragment>();
	FrameLayout fl ;
	int currentPosition;
	String user_id;
	
	Button btn_next;
	EditText yanzhengma;
	private String phone;
	TextView sendAgain,tv_tips;
	public final String mPageName = "ForgetPasswordthreeActivity";
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
		setContentView(R.layout.layout_forgetpassword3);
		titleBar =(TitleBar) findViewById(R.id.index_titlebar);
		titleBar.setTitleBarTitle("忘记密码");
		titleBar.setSearchGone();
		titleBar.setShareGone(false);
		if(getIntent()!=null){
			code = getIntent().getStringExtra("code");
		}
		newPaw =(EditText) findViewById(R.id.newpaw);
		confirmPaw=(EditText) findViewById(R.id.confirmpaw);
		finish=(Button) findViewById(R.id.finish);
		
		phoneNum = ((YoutiApplication)(getApplication())).myPreference.getTelNumber();
		tv_tips =(TextView) findViewById(R.id.tv_tips);
		if("0".equals(code)){
			tv_tips.setText("您可以设置您的支付密码");
		}
		finish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String pwd1=newPaw.getText().toString().replace(" ", "");
				String pwd2=confirmPaw.getText().toString().replace(" ", "");
				if(TextUtils.isEmpty(pwd1)||TextUtils.isEmpty(pwd2)){
					Utils.showToast(ForgetPasswordthreeActivity.this, "密码不能为空");
					return;
				}
				if(!pwd1.equals(pwd2)){
					Utils.showToast(ForgetPasswordthreeActivity.this, "两次密码不一致");
					return;
				}
				
				requestData(pwd2);
			}
		});
	}
	
	EditText newPaw,confirmPaw;
	Button finish;
	private String phoneNum;
	private String code;
	private String url;
	
	


	protected void requestData(String pwd) {
		RequestParams params =new RequestParams();
		//pwd=MD5Tools.toMD5(pwd);
		
		user_id=YoutiApplication.getInstance().myPreference.getUserId();
		
		if("0".equals(code)){
			//设置支付密码
			url = "http://112.126.72.250/ut_app/index.php?m=User&a=set_paypwd";
			pwd=MD5.a(pwd);
			
			params.put("paypwd",pwd);
			if(!TextUtils.isEmpty(user_id)){
				params.put("user_id", user_id);	
				}
		}else{
			//设置登录密码
			params.put("new_pwd",pwd);
			params.put("tel_phone",phoneNum);
			url ="http://112.126.72.250/ut_app/index.php?m=User&a=up_pwd";
		}
		Utils.showToast(ForgetPasswordthreeActivity.this, url+":"+pwd);
		HttpUtils.post(url, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson =new Gson();
				InfoBean fromJson = gson.fromJson(arg2, InfoBean.class);
				if(fromJson.code.equals("400")){
					Utils.showToast(ForgetPasswordthreeActivity.this, "手机号为空");
				}else if(fromJson.code.equals("401")){
					Utils.showToast(ForgetPasswordthreeActivity.this, "新密码为空");
				}else if(fromJson.code.equals("402")){
					Utils.showToast(ForgetPasswordthreeActivity.this, "账号不存在");
				}else if(fromJson.code.equals("1")){
					Utils.showToast(ForgetPasswordthreeActivity.this, "修改成功");
					finish();
				}else{
					Utils.showToast(ForgetPasswordthreeActivity.this, "修改失败");
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Utils.showToast(ForgetPasswordthreeActivity.this, "网络连接异常");
			}
		});
	}

}
