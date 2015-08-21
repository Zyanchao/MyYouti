package com.youti.yonghu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alipay.sdk.encrypt.MD5;
import com.example.youti_geren.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.weibo.sdk.android.component.sso.tools.MD5Tools;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.YoutiApplication;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
import com.youti.view.TitleBar;

public class ModifyPasswordActivity extends Activity implements OnClickListener {
	
	/**
	 * 传入参数:
		   user_id:用户id
		   old_pwd：旧密码
		   new_pwd：新密码
	       返回参数:
		  code:400用户id为空
		  code:401旧密码为空
		  code:402新密码为空
		  code:403旧密码错误
		  code:0修改失败
		  code:1修改成功
	 */
	
	EditText oldpassword,newpassword,confirmpassword;
	private String userId;
	Button submit,cancle,but_forget;
	TitleBar titleBar;
	
	public final String mPageName = "ModifyPasswordActivity";
	private String code;
	private String url;
	private String oldpwd;
	private String newpwd;
	private String confirmpwd;
	LinearLayout ll_old;
	
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
		setContentView(R.layout.layout_modifypassword);
		if(getIntent()!=null){
			code = getIntent().getStringExtra("code");
		}
		/**
		 * 设置支付密码 ，1表示设置支付密码。0表示修改支付密码
		 */
		
		titleBar=(TitleBar) findViewById(R.id.index_titlebar);
		titleBar.setTitleBarTitle("修改登录密码");
		titleBar.setSearchGone();
		titleBar.setShareGone(false);
		titleBar =(TitleBar) findViewById(R.id.index_titlebar);
		oldpassword=(EditText) findViewById(R.id.oldpassword);
		newpassword=(EditText) findViewById(R.id.newpassword);
		confirmpassword=(EditText) findViewById(R.id.confirmpassword);
		userId = ((YoutiApplication)getApplication()).myPreference.getUserId();
		submit =(Button) findViewById(R.id.submit);
		cancle=(Button) findViewById(R.id.cancle);
		but_forget=(Button) findViewById(R.id.but_forget);
		but_forget.setVisibility(View.GONE);
		ll_old=(LinearLayout) findViewById(R.id.ll_old);
		if("1".equals(code)){
			ll_old.setVisibility(View.GONE);
			titleBar.setTitleBarTitle("设置支付密码");
		
		//	Toast.makeText(ModifyPasswordActivity.this, code+"fadfa", 0).show();
		}else if("0".equals(code)){
			titleBar.setTitleBarTitle("修改支付密码");
			but_forget.setVisibility(View.VISIBLE);
			
		}
		submit.setOnClickListener(this);
		cancle.setOnClickListener(this);
		but_forget.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submit:
			oldpwd = oldpassword.getText().toString().replace(" ", "");
			newpwd = newpassword.getText().toString().replace(" ", "");
			confirmpwd = confirmpassword.getText().toString().replace(" ", "");
			
			if(!"1".equals(code)){
				
				if(TextUtils.isEmpty(oldpwd)){
					Utils.showToast(this, "旧密码不能为空");
					return;
				}
			}
			
			if(TextUtils.isEmpty(newpwd)){
				Utils.showToast(this, "新密码不能为空");
				return;
			}
			
			if(TextUtils.isEmpty(confirmpwd)){
				Utils.showToast(this, "新密码不能为空");
				return;
			}
			
			if(!newpwd.equals(confirmpwd)){
				Utils.showToast(this, "两次输入密码不一致");
				return;
			}
			submit();
			break;
		case R.id.cancle:
			finish();
			break;
		case R.id.but_forget:
			Intent intent =new Intent(ModifyPasswordActivity.this,ForgetPasswordActivity.class);
			intent.putExtra("code", "0");
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}
	private void submit() {
		RequestParams params = new RequestParams();
		
		if("0".equals(code)){
			//修改支付支付密码
			url="http://112.126.72.250/ut_app/index.php?m=User&a=up_paypwd";
			params.put("user_id", userId);
			oldpwd= MD5.a(oldpwd);
			params.put("paypwd", oldpwd);
		}else if("1".equals(code)){
			//设置支付密码
			url="http://112.126.72.250/ut_app/index.php?m=User&a=set_paypwd";
			params.put("user_id", userId);
			newpwd=MD5.a(newpwd);
			params.put("paypwd", newpwd);
			
		}else{
			//修改登录密码
			params.put("user_id", userId);
		//	oldpwd= MD5Tools.toMD5(oldpwd);
		//	newpwd=MD5Tools.toMD5(newpwd);
			params.put("old_pwd", oldpwd);
			params.put("new_pwd", newpwd);
			url = "http://112.126.72.250/ut_app/index.php?m=User&a=up_login_pwd";
			
		}
		HttpUtils.post(url, params, new JsonHttpResponseHandler() {
            public void onStart() {  
                super.onStart();  
            } 
            public void onFailure(int statusCode,
                    org.apache.http.Header[] headers,
                    java.lang.Throwable throwable,
                    org.json.JSONObject errorResponse) {
            	Utils.showToast(ModifyPasswordActivity.this, "无法连接服务器，请稍后重试");
            };
            public void onFinish() {
            };
            public void onSuccess(int statusCode,
                    org.apache.http.Header[] headers,
                    org.json.JSONObject response){ 
                try {
                	String state =  response.getString("code");
                	if(state.equals("1")){
                		Utils.showToast(ModifyPasswordActivity.this, response.getString("info"));
                		/**
                		 * 如果是修改支付密码，那么先判断原有支付密码是否正确，再设置新的密码，分为两步
                		 */
                		if("0".equals(code)){
                			code="1";
                			submit();
                			//setResult(re, data)
                		}
                		
                		if("0".equals(code)||"1".equals(code)){
                			//已经设置过密码
                			YoutiApplication.getInstance().myPreference.setIsSet(true);
                		}
                		finish();
            			
                	}else{
                		Utils.showToast(ModifyPasswordActivity.this, response.getString("info"));
                	}
                } catch (Exception e) {
                	
                }
            }
			
        });
	}
	
	
}
