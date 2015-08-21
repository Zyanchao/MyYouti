package com.youti.yonghu.activity;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.youti.appConfig.Constants;
import com.youti.appConfig.YoutiApplication;
import com.youti.base.BaseActivity;
import com.youti.chat.Constant;
import com.youti.chat.db.UserDao;
import com.youti.chat.domain.User;
import com.youti.chat.utils.CommonUtils;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
import com.youti.yonghu.bean.LoginBean;

public class LoginActivity extends BaseActivity implements OnClickListener{
	protected static final int FAILUER = 0;
	public static final int REQUEST_CODE_SETNICK = 1;
	Button btn_sign,denglu;
	ImageView iv_weixing,iv_weibo,iv_qq,iv_back;
	TextView forgetpassword;
	EditText et_phoneNum,et_password;
	
	private String currentUsername;
	private String currentPassword;
	private boolean progressShow;
	private boolean autoLogin = false;
	/**
	 * 登录成功系统返回一个user_id
	 */
	private String user_id;
	
	private String phoneNum,password;
	private Handler handler;
	 // 整个平台的Controller, 负责管理整个SDK的配置、操作等处理
    private UMSocialService mController = UMServiceFactory
            .getUMSocialService("com.umeng.login");
    
    public final String mPageName = "LoginActivity";
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
		setContentView(R.layout.layout_login);
		
		 sp = getSharedPreferences("abc", MODE_PRIVATE);
		 editor = sp.edit();
		
		iv_back=(ImageView) findViewById(R.id.iv_back);
		btn_sign=(Button) findViewById(R.id.btn_sign);
		iv_weixing=(ImageView) findViewById(R.id.iv_weixing);
		iv_weibo=(ImageView) findViewById(R.id.iv_weibo);
		iv_qq=(ImageView) findViewById(R.id.iv_qq);
		denglu=(Button) findViewById(R.id.denglu);
		forgetpassword=(TextView) findViewById(R.id.forgetpassword);
		et_password=(EditText) findViewById(R.id.et_password);
		et_phoneNum=(EditText) findViewById(R.id.et_phone);
		
		et_phoneNum.setText(sp.getString("tel", ""));
		et_password.setText(sp.getString("pwd", ""));
		
		iv_qq.setOnClickListener(this);
		iv_weibo.setOnClickListener(this);
		iv_weixing.setOnClickListener(this);
		btn_sign.setOnClickListener(this);
		denglu.setOnClickListener(this);
		forgetpassword.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		
		 //参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "1103176396",
                "iY2uCRqXmaooIL7b");
		qqSsoHandler.addToSocialSDK();
		
		UMWXHandler wxHandler =new UMWXHandler(this, "wxbc29ec947c61f0e1", "3f7f7b0bbf4d48f3dcc2647d95c3c688");
		wxHandler.addToSocialSDK();
		
		//设置新浪SSO handler
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.getConfig().setSinaCallbackUrl("https://api.weibo.com/oauth2/default.html");
		
		handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				if(msg.arg1==0){
					Toast.makeText(getApplicationContext(), "环信登录成功", Toast.LENGTH_SHORT).show();
					/*Intent intent = new Intent(LoginActivity.this,MainActivity.class);
					startActivity(intent);
					finish();*/
				}
			};
		};

		SnsPostListener mSnsPostListener  = new SnsPostListener() {

	        @Override
	    public void onStart() {

	    }

	    @Override
	    public void onComplete(SHARE_MEDIA platform, int stCode,
	        SocializeEntity entity) {
	      if (stCode == 200) {
	        Toast.makeText(LoginActivity.this, "分享成功", Toast.LENGTH_SHORT)
	            .show();
	      } else {
	        Toast.makeText(LoginActivity.this,
	            "分享失败 : error code : " + stCode, Toast.LENGTH_SHORT)
	            .show();
	      }
	    }
	  };
	  mController.registerListener(mSnsPostListener);
	}
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_sign:
			intent =new Intent(this,SignActivityFirst.class);
			startActivity(intent);
			break;
		case R.id.iv_weibo:
			login(SHARE_MEDIA.SINA,2);
			break;
		case R.id.iv_qq:
			login(SHARE_MEDIA.QQ,3);
			break;
		case R.id.iv_weixing:
			login(SHARE_MEDIA.WEIXIN,1);
			break;
		case R.id.forgetpassword:
			intent =new Intent(this,ForgetPasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.denglu:
			//((YoutiApplication) getApplication()).myPreference.setHasLogin(true);
			phoneNum = et_phoneNum.getText().toString().replace(" ", "");
			password=et_password.getText().toString().replace(" ", "");
			if(phoneNum.length()!=11){
				Utils.showToast(this, "请核实手机号，请重新输入");
				return;
			}
			if(TextUtils.isEmpty(password)){
				Utils.showToast(this, "密码不能为空，请重新输入");
				return;
			}
			//发送用户名和密码到服务器
			sendData(phoneNum,password);
			loginChat();
			
			break;
		case R.id.iv_back:
			finish();
			break;
		default:
			break;
		}
	}
		//访问服务器，登录操作
	  private void sendData(final String phoneNum2, String password2) {
		InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		 
		  editor.putString("tel", phoneNum2);
		  editor.putString("pwd", password2);
		  editor.commit();
		String url ="http://112.126.72.250/ut_app/index.php?m=User&a=login";
		RequestParams params =new RequestParams();
		params.put("tel_phone", phoneNum2);
		//password2=MD5Tools.toMD5(password2);
		
		params.put("login_pwd", password2);
		
		HttpUtils.post(url, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson =new Gson();
				LoginBean loginBean = gson.fromJson(arg2, LoginBean.class);
				
				String code =loginBean.code;
				if(code.equals("403")){
					Utils.showToast(LoginActivity.this, "密码错误");
				}else if(code.equals("404")){
					Utils.showToast(LoginActivity.this, "此账号已被禁用");
				}else if(code.equals("1")){
				//	loginChat();//登陆 环信服务器
					
					Utils.showToast(LoginActivity.this, "登录成功");
					user_id = loginBean.list.user_id;
					
					((YoutiApplication) getApplication()).myPreference.setUserId(user_id);
					((YoutiApplication) getApplication()).myPreference.setLoginName(phoneNum2);
					((YoutiApplication) getApplication()).myPreference.setHasLogin(true);
					YoutiApplication.getInstance().myPreference.setUserName(loginBean.list.user_name);
					if("0".equals(loginBean.list.status)){
						YoutiApplication.getInstance().myPreference.setIsSet(false);
					}else{
						YoutiApplication.getInstance().myPreference.setIsSet(true);
					}
					//Intent intent =new Intent(LoginActivity.this,PersonCenterActivity.class);
					//startActivity(intent);
					finish();
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Utils.showToast(LoginActivity.this, "网络连接异常");
				/*Message msg =Message.obtain();
				msg.what=FAILUER;
				handler.sendMessage(msg);*/
			}
		});
		
	}
	  String thirdUserName;//screen_name
	  String thirdProfile;//profile_image_url
	  String gender;//新浪男为1，腾讯为男
	private Dialog pd;
	private SharedPreferences sp;
	private Editor editor;
	  
	  /**
	   * 这个方法尚未被调用
	   * @param qq
	   */
	private void loginqq(SHARE_MEDIA qq) {
		  mController.doOauthVerify(this, SHARE_MEDIA.QQ, new UMAuthListener() {
			    @Override
			    public void onStart(SHARE_MEDIA platform) {
			        Toast.makeText(LoginActivity.this, "授权开始", Toast.LENGTH_SHORT).show();
			    }
			    @Override
			    public void onError(SocializeException e, SHARE_MEDIA platform) {
			        Toast.makeText(LoginActivity.this, "授权错误", Toast.LENGTH_SHORT).show();
			    }
			    @Override
			    public void onComplete(Bundle value, SHARE_MEDIA platform) {
			    	/**
			    	 * bundle中获取qq的accesstoken
			    	 */
			    	final StringBuilder sb1 = new StringBuilder();
	                Set<String> keys = value.keySet();
	                for(String key : keys){
	                   sb1.append(key+"="+value.get(key).toString()+"\r\n");  
	                }
			    	
			        //获取相关授权信息
			        mController.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, new UMDataListener() {
			    @Override
			    public void onStart() {
			        Toast.makeText(LoginActivity.this, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
			    }                                              
			    @Override
			        public void onComplete(int status, Map<String, Object> info) {
			            if(status == 200 && info != null){
			                StringBuilder sb = new StringBuilder();
			                Set<String> keys = info.keySet();
			                for(String key : keys){
			                   sb.append(key+"="+info.get(key).toString()+"\r\n");  
			                }
			                
			                Log.d("TestData",sb.toString());
			                String path =Environment.getExternalStorageDirectory().getAbsolutePath();
							System.out.print(path);
							
							File file =new File(path, "abcqq.txt");
							try {
								OutputStream os =new FileOutputStream(file);
								os.write(sb1.toString().getBytes("utf-8"));
								os.flush();
								os.close();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			            }else{
			               Log.d("TestData","发生错误："+status);
			           }
			        }
			});
			    }
			    @Override
			    public void onCancel(SHARE_MEDIA platform) {
			        Toast.makeText(LoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
			    }
			} );
		
	}
	/**
     * 授权。如果授权成功，则获取用户信息</br>
	 * @param i 
     */
    private void login(final SHARE_MEDIA platform, final int i) {
    	mController.doOauthVerify(this, platform,new UMAuthListener() {
            private String openid;
			@Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
            	 Toast.makeText(LoginActivity.this, "授权错误...",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onComplete(Bundle value, SHARE_MEDIA platform) {
            	
                if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
                    if(i==3){
                    	//此时是qq登录，需要从value中获得openid作为唯一标记
                    	openid = value.getString("openid");
                    	// Toast.makeText(LoginActivity.this, "授权成功...openid"+openid,Toast.LENGTH_SHORT).show();
                    }
                   // Toast.makeText(LoginActivity.this, "授权成功",Toast.LENGTH_SHORT).show();
                    getUserInfo(platform,openid,i);
                    
                } else {
                    Toast.makeText(LoginActivity.this, "授权失败",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancel(SHARE_MEDIA platform) {
            	 Toast.makeText(LoginActivity.this, "授权取消...",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStart(SHARE_MEDIA platform) {
            	 Toast.makeText(LoginActivity.this, "授权开始...",Toast.LENGTH_SHORT).show();
            }
    	});
    }
    
    /**
     * 获取授权平台的用户信息</br>
     * @param openid 
     * @param i 
     */
    private void getUserInfo(SHARE_MEDIA platform, final String openid, final int i) {
    	mController.getPlatformInfo(this, platform, new UMDataListener() {
    		String onlyId;
    		String head_img;
    		String screen_name;
    		String sex;
    	    @Override
    	    public void onStart() {
    	        Toast.makeText(LoginActivity.this, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
    	    }                                              
    	    @Override
    	        public void onComplete(int status, Map<String, Object> info) {
    	            if(status == 200 && info != null){
    	                StringBuilder sb = new StringBuilder();
    	                Set<String> keys = info.keySet();
    	                for(String key : keys){
    	                   sb.append(key+"="+info.get(key).toString()+"\r\n");
    	                  
    	                }
    	                if(i==1){
    	                	//微信登录，获得unionid
    	                	onlyId=info.get("unionid").toString();
    	                	head_img=info.get("headimgurl").toString();
    	                	screen_name=info.get("nickname").toString();
    	                	sex=info.get("sex").toString();
    	                	//Toast.makeText(LoginActivity.this, "登录...onlyId"+onlyId,Toast.LENGTH_SHORT).show();
    	                	  isBund(String.valueOf(2),onlyId,head_img,screen_name,sex);
    	                }else if(i==2){
    	                	//新浪微博登录，获得uid
    	                	onlyId=info.get("uid").toString();
    	                	head_img=info.get("profile_image_url").toString();
    	                	screen_name=info.get("screen_name").toString();
    	                	sex=info.get("gender").toString();
    	                	//Toast.makeText(LoginActivity.this, "登录...onlyId"+onlyId,Toast.LENGTH_SHORT).show();
    	                	isBund(String.valueOf(3),onlyId,head_img,screen_name,sex);
    	                }else{
    	                	//qq登录，获得openid
    	                	onlyId=openid;
    	                	head_img=info.get("profile_image_url").toString();
    	                	screen_name=info.get("screen_name").toString();
    	                	sex=info.get("gender").toString();
    	                	if("男".equals(sex)){
    	                		sex="1";
    	                	}else{
    	                		sex="0";
    	                	}
    	                	//Toast.makeText(LoginActivity.this, "登录...onlyId"+onlyId,Toast.LENGTH_SHORT).show();
    	                	isBund(String.valueOf(1),onlyId,head_img,screen_name,sex);
    	                }
    	                
    	               /* String path =Environment.getExternalStorageDirectory().getAbsolutePath();
    	                File file =new File(path, "abcsina.txt");
						try {
							OutputStream os =new FileOutputStream(file);
							os.write(sb.toString().getBytes("utf-8"));
							os.flush();
							os.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
						
    	              
						
    	                Log.d("TestData",sb.toString());
    	               
    	            }else{
    	               Log.d("TestData","发生错误："+status);
    	           }
    	        }
    	});
    }
    
    /**
	 * 环信 服务器登录
	 * @param view
	 */
	public void loginChat() {
		if (!CommonUtils.isNetWorkConnected(this)) {
			Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
			return;
		}
		currentUsername = et_phoneNum.getText().toString().trim();
		currentPassword = et_password.getText().toString().trim();

		if (TextUtils.isEmpty(currentUsername)) {
			Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(currentPassword)) {
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
		
		pd = Utils.createProgressBarDialog(LoginActivity.this, "正在登陆...");
		pd.show();

		final long start = System.currentTimeMillis();
		// 调用sdk登陆方法登陆聊天服务器
		EMChatManager.getInstance().login(currentUsername+Constants.CHAT_CODE, "111111", new EMCallBack() {

			@Override
			public void onSuccess() {
				if (!progressShow) {
					return;
				}
				// 登陆成功，保存用户名密码
				YoutiApplication.getInstance().setUserName(currentUsername+Constants.CHAT_CODE);//环信用户名   规则  手机号+“3”
				YoutiApplication.getInstance().setPassword(currentPassword);

				
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
				if (!LoginActivity.this.isFinishing() &&pd!=null&& pd.isShowing()) {
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
		UserDao dao = new UserDao(LoginActivity.this);
		List<User> users = new ArrayList<User>(userlist.values());
		dao.saveContactList(users);
	}
	
	/*第三方登陆判断是否绑定过账号
	 *  status：登陆方式 1QQ 2 微信  3微博
		   uid:第三方登陆返回的唯一标识

	 * */
    public void isBund(final String i,final String uid,final String head_img,final String nickname,final String sex){
    	String str="http://112.126.72.250/ut_app/index.php?m=User&a=tpos_login";
    	RequestParams params =new RequestParams();
    	params.put("status", i);
    	params.put("uid", uid);
    	Utils.showToast(LoginActivity.this, i+"onSuccess..."+uid);
    	HttpUtils.post(str, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Utils.showToast(LoginActivity.this, "onSuccess...");
				Gson gson =new Gson();
				LoginBean loginBean = gson.fromJson(arg2, LoginBean.class);
				if(loginBean!=null&loginBean.code.equals("0")){
					//登录成功，需要绑定账号
					Intent intent =new Intent(LoginActivity.this,ThirdLoginActivity.class);
					intent.putExtra("uid", uid);
					intent.putExtra("status",i);
					intent.putExtra("head_img", head_img);
					intent.putExtra("nickname", nickname);
					intent.putExtra("sex", sex);
					startActivity(intent);
					finish();
					
				}else if(loginBean.code.equals("1")){
					//登录成功，已经绑定账号
					String userId = loginBean.list.user_id;
					((YoutiApplication) getApplication()).myPreference.setUserId(userId);
					((YoutiApplication) getApplication()).myPreference.setHasLogin(true);
					Intent intent =new Intent(LoginActivity.this,PersonCenterActivity.class);
					startActivity(intent);
					finish();
				}else{
					Utils.showToast(LoginActivity.this, "第三方登录失败");
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Utils.showToast(LoginActivity.this, "网络连接失败");
			}
		});
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK) {
			/**使用SSO授权必须添加如下代码 */  
	        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
	        if(ssoHandler != null){
	           ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	        }
	    }
		}
        
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	if(pd!=null){
    		pd.dismiss();
    		pd=null;
    				
    	}
    }
   

}
