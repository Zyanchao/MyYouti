package com.youti.yonghu.activity;

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
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;

public class SignActivityFirst extends Activity implements OnClickListener {

	TextView tv_yonghuxieyi;
	EditText telNum,password;
	boolean isChecked=true;
	ImageView iv_checked;
	Button btn_next;
	private String pw,phone;
	ImageView login_icon_back;
	
	public final String mPageName ="SignActivityFirst";
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
		tv_yonghuxieyi = (TextView) findViewById(R.id.tv_yonghuxieyi);
		iv_checked = (ImageView) findViewById(R.id.iv_checked);
		btn_next=(Button) findViewById(R.id.btn_next);
		telNum=(EditText) findViewById(R.id.tel_num);
		btn_next.setOnClickListener(this);
		password=(EditText) findViewById(R.id.password);
		login_icon_back=(ImageView) findViewById(R.id.login_icon_back);
		
		//tv_yonghuxieyi.setOnClickListener(this);
		iv_checked.setOnClickListener(this);
		iv_checked.setImageResource(R.drawable.login_right_h);
		 tv_yonghuxieyi.setText(getClickableSpan(),BufferType.SPANNABLE);
		tv_yonghuxieyi.setMovementMethod(LinkMovementMethod.getInstance());
		tv_yonghuxieyi.setFocusable(false);
		tv_yonghuxieyi.setLongClickable(false);
		login_icon_back.setOnClickListener(this);
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
		case R.id.login_icon_back:
			finish();
			break;
		default:
			break;
		}
	}
	//获取验证码的submitUrl;
	String submitUrl;
	/**
	 * 获得到用户输入的手机号和密码，然后将手机号和密码提交，提交成功就跳到下一个注册页面
	 */
	private void submit() {
		// TODO Auto-generated method stub
		submitUrl = "http://112.126.72.250/ut_app/index.php?m=User&a=register";
		pw =password.getText().toString().replace(" ", "");
		if(TextUtils.isEmpty(pw)||pw.length()<6){
			Utils.showToast(this, "密码长度最低为6位");
			return ;
		}
		phone =telNum.getText().toString().replace(" ", "");
		if(phone.length()!=11){
			Utils.showToast(this, "请核实手机号后，重新输入");
			return;
		}
		if(!isChecked){
			Utils.showToast(this, "请阅读并同意用户协议");
			return;
		}
		
		
		RequestParams params = new RequestParams(); // 绑定参数
		/*params.put("key", ((YoutiApplication)getApplication()).getUuid());
		params.put("utype", ((YoutiApplication)getApplication()).getUtype());
		params.put("phone", phone);
		params.put("scode", "yoti");
		params.put("op", 0);*/
		
		params.put("tel_phone", phone);
		//pw=MD5Tools.toMD5(pw);
		params.put("login_pwd", pw);
		
        HttpUtils.post(submitUrl, params, new JsonHttpResponseHandler() {
            public void onStart() {  
                super.onStart();  
            } 
            public void onFailure(int statusCode,
                    org.apache.http.Header[] headers,
                    java.lang.Throwable throwable,
                    org.json.JSONObject errorResponse) {
            	Utils.showToast(SignActivityFirst.this, "无法连接服务器，请稍后重试");
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
                		Bundle b = new Bundle();
                		b.putString("phone", phone);
                		b.putString("scode","yoti");
                		b.putString("password", pw);
                		Intent intent =new Intent(SignActivityFirst.this,SignActivitySecond.class);
                		intent.putExtras(b);
            			startActivity(intent);
            			finish();
            			
                	}else{
                		Utils.showToast(SignActivityFirst.this, response.getString("info"));
                	}
                } catch (Exception e) {
                	
                }
            };
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
				Toast.makeText(SignActivityFirst.this, "用户协议",
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
