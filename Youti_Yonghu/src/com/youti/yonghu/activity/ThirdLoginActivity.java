package com.youti.yonghu.activity;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.YoutiApplication;
import com.youti.fragment.ThirdLoginTwoActivity;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
import com.youti.yonghu.bean.InfoBean;

public class ThirdLoginActivity extends Activity implements OnClickListener{
	
	
	TextView tv_yonghuxieyi;
	EditText telNum,password;
	boolean isChecked;
	ImageView iv_checked;
	Button btn_next;
	private String phone;
	
	String status;
	String uid;
	String head_img;
	String nickname;
	String sex;
	
	public final String mPageName ="ThirdLoginActivity";
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
		setContentView(R.layout.layout_signfirst);
		
		if(getIntent()!=null&&getIntent().getExtras()!=null){
			status =getIntent().getExtras().getString("status");
			uid=getIntent().getExtras().getString("uid");
			head_img=getIntent().getExtras().getString("head_img");
			nickname=getIntent().getExtras().getString("nickname");
			sex=getIntent().getExtras().getString("sex");
		}
		
		tv_yonghuxieyi = (TextView) findViewById(R.id.tv_yonghuxieyi);
		iv_checked = (ImageView) findViewById(R.id.iv_checked);
		btn_next=(Button) findViewById(R.id.btn_next);
		telNum=(EditText) findViewById(R.id.tel_num);
		btn_next.setOnClickListener(this);
		password=(EditText) findViewById(R.id.password);
		password.setVisibility(View.GONE);
		
		//tv_yonghuxieyi.setOnClickListener(this);
		isChecked=true;
		iv_checked.setImageResource(R.drawable.login_right_h);
		iv_checked.setOnClickListener(this);
		 tv_yonghuxieyi.setText(getClickableSpan(),BufferType.SPANNABLE);
		tv_yonghuxieyi.setMovementMethod(LinkMovementMethod.getInstance());
		tv_yonghuxieyi.setFocusable(false);
		tv_yonghuxieyi.setLongClickable(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_checked:
			if (isChecked) {
				isChecked = false;
				iv_checked.setImageResource(R.drawable.login_right);
			} else {
				isChecked = true;
				iv_checked.setImageResource(R.drawable.login_right_h);
			}
			break;
		case R.id.btn_next:
			
			submit();
			break;
		default:
			break;
		}
	}
	//获取验证码的submitUrl;
	String submitUrl;
	/**
	 *
	 */
	private void submit() {
		String str="http://112.126.72.250/ut_app/index.php?m=User&a=check_phone";
		
		phone =telNum.getText().toString().replace(" ", "");
		if(TextUtils.isEmpty(phone)||phone.length()!=11){
			Utils.showToast(ThirdLoginActivity.this, "请检查手机号码，重新输入");
			return;
		}
		if(!isChecked){
			Utils.showToast(ThirdLoginActivity.this, "请同意用户协议");
			return;
		}
		RequestParams params =new RequestParams();
		params.put("status", status);
		params.put("phone", phone);
		HttpUtils.post(str, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				// 登陆成功，保存用户名密码
				YoutiApplication.getInstance().setUserName(phone);
				YoutiApplication.getInstance().setPassword("111111");
				Gson gson =new Gson();
				InfoBean infoBean = gson.fromJson(arg2, InfoBean.class);
				if(infoBean!=null){
					
					if(infoBean.code.equals("403")){
						Utils.showToast(ThirdLoginActivity.this, "该手机已绑定其他账号");
					}else if(infoBean.code.equals("0")){
						Utils.showToast(ThirdLoginActivity.this, "短信发送失败");
					}else if(infoBean.code.equals("1")){
						Utils.showToast(ThirdLoginActivity.this, "绑定的账号注册过");
						//该手机号可能绑定过其他的第三方登录，但是没有注册过当前的第三方登录
						Intent intent =new Intent(ThirdLoginActivity.this,ThirdLoginTwoActivity.class);
						intent.putExtra("status", status);
						intent.putExtra("phone", phone);
						intent.putExtra("uid", uid);
						startActivity(intent);
						finish();
						
					}else if(infoBean.code.equals("2")){
						Utils.showToast(ThirdLoginActivity.this, "绑定的账号未注册过");
						//该手机号未绑定任何第三方，也没有注册过
						Intent intent =new Intent(ThirdLoginActivity.this,FillDataActivity.class);
						intent.putExtra("status", status);
						intent.putExtra("phone", phone);
						intent.putExtra("uid", uid);
						intent.putExtra("head_img", head_img);
						intent.putExtra("nickname", nickname);
						intent.putExtra("sex", sex);
						startActivity(intent);
						finish();
						
					}else if(infoBean.code.equals("0")){
						Utils.showToast(ThirdLoginActivity.this, "短信发送失败");
					}else{
						Utils.showToast(ThirdLoginActivity.this, "绑定失败");
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Utils.showToast(ThirdLoginActivity.this, "网络连接异常");
			}
		});
	}
	
	private SpannableString getClickableSpan() {
		
		
		SpannableString spanableInfo = new SpannableString(
				"用户协议");
		
		
		int start = 0;
		int end = spanableInfo.length();
		spanableInfo.setSpan(new ClickableSpan() {
			
			@Override
			public void onClick(View widget) {
				Toast.makeText(ThirdLoginActivity.this, "用户协议",
						Toast.LENGTH_SHORT).show();
				
			}
			
			 @Override
             public void updateDrawState(TextPaint ds) {
                 super.updateDrawState(ds);
                 ds.setColor(Color.WHITE); // 设置文本颜色
                 // 去掉下划线
               //  ds.setUnderlineText(false);
             }
		}, start, end,0);
		return spanableInfo;
	}

	
	
	
	
	class Clickable extends ClickableSpan implements OnClickListener {
		private final View.OnClickListener mListener;

		public Clickable(View.OnClickListener l) {
			mListener = l;
		}

		@Override
		public void onClick(View v) {
			mListener.onClick(v);
		}
	}
	
	
	
	


	
}
