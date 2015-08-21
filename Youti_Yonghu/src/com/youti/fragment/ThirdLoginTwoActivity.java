package com.youti.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.YoutiApplication;
import com.youti.chat.Constant;
import com.youti.chat.db.UserDao;
import com.youti.chat.domain.User;
import com.youti.chat.utils.CommonUtils;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
import com.youti.yonghu.activity.LoginActivity;
import com.youti.yonghu.activity.PersonCenterActivity;
import com.youti.yonghu.bean.LoginBean;

public class ThirdLoginTwoActivity extends Activity implements OnClickListener{
	String yanzhengma,recomcode;
	String phone="";
	String paw="";
	String scode="";
	EditText et_yanzhengma,et_recomcode;
	Button sendAgain,btn_finish;
	int count=60;
	String status;
	String uid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_signsecond);
		initView();
		initData();
		
		
	}
	public final String mPageName = "ThirdLoginTwoActivity";
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
			status=getIntent().getStringExtra("status");
			uid=getIntent().getStringExtra("uid");
		}
		//开始倒计时
		sendAginCount();
	}

	private void initView() {
		et_yanzhengma=(EditText) findViewById(R.id.et_yanzhengma);
		et_recomcode=(EditText)findViewById(R.id.recomcode);
		et_recomcode.setVisibility(View.GONE);
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
		RequestParams params = new RequestParams(); // 绑定参数
		params.put("tel_phone", phone);
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
                		Utils.showToast(ThirdLoginTwoActivity.this, "重新获取验证码成功");
                	}else{
                		Utils.showToast(ThirdLoginTwoActivity.this, response.getString("msg"));
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
		if(TextUtils.isEmpty(yanzhengma)){
			Utils.showToast(ThirdLoginTwoActivity.this, "验证码不能为空");
			return;
		}
		
		RequestParams params = new RequestParams(); // 绑定参数
		
		params.put("status", status);
		params.put("phone", phone);
		params.put("uid", uid);
		params.put("code", yanzhengma);
        HttpUtils.post("http://112.126.72.250/ut_app/index.php?m=User&a=alr_bangreg", params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson =new Gson();
				LoginBean loginBean = gson.fromJson(arg2, LoginBean.class);
				if(loginBean!=null){
					if(loginBean.code.equals("1")){
						//登录环信
						loginChat(phone,"111111");
						Utils.showToast(ThirdLoginTwoActivity.this, "绑定成功");
						String user_id =loginBean.list.user_id;
						((YoutiApplication)getApplication()).myPreference.setUserId(user_id);
						Intent intent =new Intent(ThirdLoginTwoActivity.this,PersonCenterActivity.class);
						startActivity(intent);
						finish();
					}else{
						Utils.showToast(ThirdLoginTwoActivity.this, "绑定失败");
					}
				}
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				
			}
		});
	}
	 /**
		 * 环信 服务器登录
		 * 
		 * @param view
		 */
	private boolean progressShow;
	private Dialog pd;
		public void loginChat(final String phone,String pwd) {
			if (!CommonUtils.isNetWorkConnected(this)) {
				Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
				return;
			}

			if (TextUtils.isEmpty(phone)) {
				Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(pwd)) {
				Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
				return;
			}

			progressShow = true;
			/*final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
			pd.setCanceledOnTouchOutside(false);
			pd.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					progressShow = false;
				}
			});
			pd.setMessage("正在登陆...");
			pd.show();*/
			
			pd = Utils.createProgressBarDialog(ThirdLoginTwoActivity.this, "正在登陆...");
			pd.show();

			final long start = System.currentTimeMillis();
			// 调用sdk登陆方法登陆聊天服务器
			EMChatManager.getInstance().login(phone, "111111", new EMCallBack() {

				@Override
				public void onSuccess() {
					if (!progressShow) {
						return;
					}
					// 登陆成功，保存用户名密码
					//YoutiApplication.getInstance().setUserName(phone);
					//YoutiApplication.getInstance().setPassword("111111");

					
					try {
						// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
						// ** manually load all local groups and
					    EMGroupManager.getInstance().loadAllGroups();
						EMChatManager.getInstance().loadAllConversations();
						// 处理好友和群组
						initializeContacts();
					} catch (Exception e) {
						e.printStackTrace();
						// 取好友或者群聊失败，不让进入主页面
						runOnUiThread(new Runnable() {
							public void run() {
								if(pd!=null&&pd.isShowing()){
									
									pd.dismiss();
								}
								YoutiApplication.getInstance().logout(null);
								Toast.makeText(getApplicationContext(), R.string.login_failure_failed, 1).show();
							}
						});
						
						return;
					}
					// 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
					boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
							YoutiApplication.currentUserNick.trim());
					if (!updatenick) {
						Log.e("LoginActivity", "update current user nick fail");
					}
					if (!ThirdLoginTwoActivity.this.isFinishing() &&pd!=null&& pd.isShowing()) {
						pd.dismiss();
					}
					/*Message msg=Message.obtain();
					msg.arg1=0;
					handler.sendMessage(msg);*/
				}

				@Override
				public void onProgress(int progress, String status) {
				}

				@Override
				public void onError(final int code, final String message) {
					if (!progressShow) {
						return;
					}
					runOnUiThread(new Runnable() {
						public void run() {
							if (pd!=null&&pd.isShowing()) {							
								pd.dismiss();
							}
							
							Toast.makeText(getApplicationContext(), "登陆失败：" + message,Toast.LENGTH_SHORT).show();
						}
					});
					
				}
			});
		}

		private void initializeContacts() {
			Map<String, User> userlist = new HashMap<String, User>();
			// 添加user"申请与通知"
			User newFriends = new User();
			newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
			String strChat = getResources().getString(
					R.string.Application_and_notify);
			newFriends.setNick(strChat);

			userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
			// 添加"群聊"
			User groupUser = new User();
			String strGroup = getResources().getString(R.string.group_chat);
			groupUser.setUsername(Constant.GROUP_USERNAME);
			groupUser.setNick(strGroup);
			groupUser.setHeader("");
			userlist.put(Constant.GROUP_USERNAME, groupUser);

			// 存入内存
			YoutiApplication.getInstance().setContactList(userlist);
			// 存入db
			UserDao dao = new UserDao(ThirdLoginTwoActivity.this);
			List<User> users = new ArrayList<User>(userlist.values());
			dao.saveContactList(users);
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
