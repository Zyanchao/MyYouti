package com.youti.yonghu.activity;

import org.apache.http.Header;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.YoutiApplication;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
import com.youti.view.ActionSheet;
import com.youti.view.CustomProgressDialog;
import com.youti.view.ActionSheet.ActionSheetListener;
import com.youti.view.WaitDialog;
import com.youti.yonghu.bean.UserBean;

public class EditDataActivity extends FragmentActivity implements OnClickListener,ActionSheetListener {
	TextView tv_cancle,tv_save,tv_sex;
	EditText et_nickname,et_introduce,et_phone;
	RelativeLayout rl_modifypassword;
	LinearLayout ll_sex;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		hasLogin = ((YoutiApplication)getApplication()).myPreference.getHasLogin();
		userId=((YoutiApplication)getApplication()).myPreference.getUserId();
		if(!hasLogin){
			Intent intent =new Intent (this,LoginActivity.class);
			startActivity(intent);
		}
		setContentView(R.layout.layout_editdata);
	/*	 // 创建状态栏的管理实例  
	    SystemBarTintManager tintManager = new SystemBarTintManager(this);  
	    // 激活状态栏设置  
	    tintManager.setStatusBarTintEnabled(true);  
	    // 激活导航栏设置  
	    tintManager.setNavigationBarTintEnabled(true);  
	 // 设置一个颜色给系统栏  
	    tintManager.setTintColor(Color.parseColor("#99000FF"));  
	    // 设置一个样式背景给导航栏  
	    tintManager.setNavigationBarTintResource(R.drawable.my_tint);  
	    // 设置一个状态栏资源  
	    tintManager.setStatusBarTintDrawable(MyDrawable);  */
		initView();
		initListener();
		initData();
		
		
	}
	public final String mPageName="EditDataActivity";
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
	public void showActionSheet() {
		ActionSheet.createBuilder(this, getSupportFragmentManager())
				.setCancelButtonTitle("Cancel")
				.setOtherButtonTitles("女", "男")
				.setCancelableOnTouchOutside(true).setListener(this).show();
	}
	private int index;
	//取消意外的其他按钮
	@Override
	public void onOtherButtonClick(ActionSheet actionSheet, int index) {
		this.index=index;
		if(index==0){
			tv_sex.setText("女");
		}else{
			tv_sex.setText("男");
		}
	}
	
	//点击取消
	@Override
	public void onDismiss(ActionSheet actionSheet, boolean isCancle) {

	}
	private void initData() {
		userName = ((YoutiApplication) getApplication()).myPreference.getUserName();
		watchData();
	}

	private void initListener() {
		tv_cancle.setOnClickListener(this);
		tv_save.setOnClickListener(this);
		rl_modifypassword.setOnClickListener(this);
		ll_sex.setOnClickListener(this);
		
	}

	private void initView() {
		tv_cancle=(TextView) findViewById(R.id.tv_cancle);
		tv_save=(TextView) findViewById(R.id.tv_save);
		tv_sex=(TextView) findViewById(R.id.tv_sex);
		et_nickname=(EditText) findViewById(R.id.et_nickname);
		et_introduce=(EditText) findViewById(R.id.et_introduce);
		et_phone=(EditText) findViewById(R.id.et_phone);
		rl_modifypassword = (RelativeLayout) findViewById(R.id.rl_modifypassword);
		ll_sex = (LinearLayout) findViewById(R.id.ll_sex);
		
		
		et_nickname.setSelection(et_nickname.getText().toString().trim().length());
		//et_nickname.setSelection(et_nickname.getText().toString().trim().length());
		//et_nickname.setSelection(et_nickname.getText().toString().trim().length());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_cancle:
			/*//判断隐藏软键盘是否弹出
			if(getWindow().getAttributes().softInputMode==WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED){
				//隐藏软键盘
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
			}*/
			// 虚拟键盘隐藏
			View view = getWindow().peekDecorView();
			if(view!=null){
				InputMethodManager inputmanger = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE);          
				inputmanger.hideSoftInputFromWindow(view.getWindowToken(),0);
			}

			finish();
			break;
		case R.id.tv_save:
			saveData();
			break;
		case R.id.rl_modifypassword:
			Utils.showToast(this, "修改密码");
			Intent intent =new Intent(this,ModifyPasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_sex:
			//Utils.showToast(this, "选择性别");
			setTheme(R.style.ActionSheetStyleIOS7);
			showActionSheet();
			break;
		
		}
	}
	String nickname,sex,introduce,phone,password;
	private String userId;
	private String userName;
	private String userId2;
	private boolean hasLogin;
	private CustomProgressDialog waitDialog;
	private Dialog createProgressBarDialog;
	private void saveData() {
		nickname=et_nickname.getText().toString().trim();
		introduce=et_introduce.getText().toString().trim();
		phone=et_phone.getText().toString().trim();
		if(TextUtils.isEmpty(nickname)){
			Utils.showToast(this, "亲，您的昵称还没填呢");
			return;
		}
		
		if(TextUtils.isEmpty(phone)){
			Utils.showToast(this, "亲，您的手机号还没填呢");
			return;
		}
		
		sendData(nickname,sex,introduce,phone,password);
		
	}
	/**
	 * 查看个人资料
	 */
	public void watchData(){
		String url = "http://112.126.72.250/ut_app/index.php?m=User&a=user_info";
		RequestParams params =new RequestParams();
		params.put("user_id", userId);
		waitDialog = new CustomProgressDialog(EditDataActivity.this, R.string.laoding_tips,R.anim.frame2);
		waitDialog.show();
		HttpUtils.post(url, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				waitDialog.dismiss();
				Gson gson =new Gson ();
				UserBean userBean = gson.fromJson(arg2, UserBean.class);
				et_nickname.setText(userBean.list.user_name);
				et_introduce.setText(userBean.list.sign);
				et_phone.setText(userBean.list.tel_phone);
				tv_sex.setText(userBean.list.sex.equals("0")?"女":"男");
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				waitDialog.dismiss();
				Utils.showToast(EditDataActivity.this, "网络连接异常");
			}
		});
	}
	
	/**
	 * 将修改后的资料发送到后台保存,发送progressbar,子线程
	 * @param phone2 
	 * @param introduce2 
	 * @param sex2 
	 * @param nickname2 
	 * @param password2
	 */
	private void sendData(String nickname2, String sex2, String introduce2, String phone2,String password2) {
		RequestParams params =new RequestParams();
		/**
		 * 传入参数:user_id:用户id
         user_name:昵称
		 sex：性别 1男
		 sign： 签名
		返回参数:
		         code:400用户id为空
		 code:401用户昵称为空
		 code：402性别为空
		 code：403签名为空 
		 code：1成功
		 code：0失败 
		 */
		params.put("user_id", userId);
		params.put("user_name", nickname);
		params.put("sex", index);
		params.put("sign", introduce);
		createProgressBarDialog = Utils.createProgressBarDialog(EditDataActivity.this, "正在上传资料...");
		createProgressBarDialog.show();
		HttpUtils.post("http://112.126.72.250/ut_app/index.php?m=User&a=edit_user", params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				createProgressBarDialog.dismiss();
				View view = getWindow().peekDecorView();
				if(view!=null){
					InputMethodManager inputmanger = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE);          
					inputmanger.hideSoftInputFromWindow(view.getWindowToken(),0);
				}
				Utils.showToast(EditDataActivity.this, "修改资料成功");
				finish();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				createProgressBarDialog.dismiss();
				Utils.showToast(EditDataActivity.this, "修改资料失败，请稍后再试");
			}
		});
	}
}
