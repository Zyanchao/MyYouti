package com.youti.yonghu.activity;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.YoutiApplication;
import com.youti.chat.Constant;
import com.youti.chat.db.UserDao;
import com.youti.chat.domain.User;
import com.youti.chat.utils.CommonUtils;
import com.youti.utils.HttpUtils;
import com.youti.utils.IBitmapUtils;
import com.youti.utils.ImageUtils;
import com.youti.utils.Utils;
import com.youti.view.ActionSheet;
import com.youti.view.ActionSheet.ActionSheetListener;
import com.youti.view.CircleImageView;
import com.youti.yonghu.bean.LoginBean;
import com.youti.yonghu.bean.UserCenterBean;

public class FillDataActivity extends FragmentActivity implements OnClickListener,ActionSheetListener {
	TextView tv_cancle,tv_save,tv_sex;
	EditText et_nickname,et_introduce,et_pwd,et_pwd2;
	RelativeLayout rl_modifypassword;
	LinearLayout ll_sex;
	CircleImageView headportrait;
	String nickname,sex,introduce,phone,password,password2;
	private String userId;
	private String userName;
	private String userId2;
	private boolean hasLogin;
	String status;
	String uid;
	public final String mPageName = "FillDataActivity";
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
	 private final static String FILE_SAVEPATH = Environment
	            .getExternalStorageDirectory().getAbsolutePath()
	            + "/youti_yonghu/Portrait/";
	    private Uri origUri;
	    private Uri cropUri;
	    private File protraitFile;
	    
	    private final static int CROP = 200;
	    private String protraitPath;
	    String head_img;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		hasLogin = ((YoutiApplication)getApplication()).myPreference.getHasLogin();
		userId=((YoutiApplication)getApplication()).myPreference.getUserId();
		
		if(getIntent()!=null){
			phone=getIntent().getStringExtra("phone");
			status=getIntent().getStringExtra("status");
			uid=getIntent().getStringExtra("uid");
			head_img=getIntent().getExtras().getString("head_img");
			nickname=getIntent().getExtras().getString("nickname");
			sex=getIntent().getExtras().getString("sex");
		}
		
		if(!hasLogin){
			Intent intent =new Intent (this,LoginActivity.class);
			startActivity(intent);
		}
		
		setContentView(R.layout.layout_filldata);
		initView();
		initListener();
		initData();
		
		
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
		sex=String.valueOf(index);
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
		et_nickname.setText(nickname);
		tv_sex.setText("1".equals(sex)?"男":"女");
		ImageLoader.getInstance().displayImage(head_img, headportrait);
		
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
		et_pwd=(EditText) findViewById(R.id.et_pwd);
		ll_sex = (LinearLayout) findViewById(R.id.ll_sex);
		et_pwd2=(EditText) findViewById(R.id.et_pwd2);		
		headportrait= (CircleImageView) findViewById(R.id.headportrait);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_cancle:
			finish();
			break;
		case R.id.tv_save:
			saveData();
			break;
		
		case R.id.ll_sex:
			setTheme(R.style.ActionSheetStyleIOS7);
			showActionSheet();
			break;
		case R.id.headportrait:
			Utils.showToast(FillDataActivity.this, "添加头像");
			break;
		
		}
	}

	private void saveData() {
		nickname=et_nickname.getText().toString().trim();
		introduce=et_introduce.getText().toString().trim();
		password =et_pwd.getText().toString().trim();
		password2=et_pwd2.getText().toString().trim();
		
		if(TextUtils.isEmpty(nickname)){
			Utils.showToast(this, "亲，您的昵称还没填呢");
			return;
		}
		
		if(TextUtils.isEmpty(password)){
			Utils.showToast(this, "亲，您的密码还没填呢");
			return;
		}
		
		if(password.equals(password2)){			
			sendData(nickname,sex,introduce,phone,password);
			finish();
		}else{
			Utils.showToast(FillDataActivity.this, "两次密码不一致");
		}
	}
	
	
	private void sendData(String nickname, String sex, String introduce, String phone,String password) {
		RequestParams params =new RequestParams();
		/**
		 * 传入参数: 
           status：登陆方式 1QQ 2 微信  3微博
		   phone:绑定的手机号
		   uid:登陆成功的唯一标识
		   user_name：昵称
		   head_img：头像
		   sex：性别 1男0女
		   login_pwd：密码
			返回参数:
			code:403手机号为空
			code:404手机号格式错误
			code:405登陆方式为空
			code:406登陆的唯一标识为空
			code:407头像为空
			code:408昵称为空
			code:409性别
			code:410参数有误
			code:0绑定失败
			code:1绑定成功
			  list:
			  user_id:用户id

		 */
		params.put("status", status);
		params.put("phone", phone);
		params.put("sex", sex);
		//params.put("sign", introduce);
		params.put("uid", uid);
		
		/**
		 * 如果是用户没有修改头像，那么还是第三方头像
		 * 如果用户修改了头像，那么此时的head_img还是第三方头像，因此需要在上传之前，重新获取头像地址
		 */
		head_img =YoutiApplication.getInstance().myPreference.getHeadImgPath();
		params.put("head_img", head_img);//第三方的头像
		params.put("login_pwd", password);
		params.put("user_name", nickname);
		HttpUtils.post("http://112.126.72.250/ut_app/index.php?m=User&a=check_code2", params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson =new Gson();
				LoginBean loginBean = gson.fromJson(arg2, LoginBean.class);
				if(loginBean!=null){
					if(loginBean.code.equals("1")){
						Utils.showToast(FillDataActivity.this, "绑定成功");
						
						String user_id =loginBean.list.user_id;
						
						((YoutiApplication)getApplication()).myPreference.setUserId(user_id);
						((YoutiApplication)getApplication()).myPreference.setHasLogin(true);
						Intent intent =new Intent(FillDataActivity.this,PersonCenterActivity.class);
						startActivity(intent);
						finish();
					}else{
						Utils.showToast(FillDataActivity.this, "绑定失败");
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Utils.showToast(FillDataActivity.this, "网络连接失败，请稍后再试");
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
	public void loginChat(String phone,String pwd) {
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
		
		pd = Utils.createProgressBarDialog(FillDataActivity.this, "正在登陆...");
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
				if (!FillDataActivity.this.isFinishing() &&pd!=null&& pd.isShowing()) {
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
		UserDao dao = new UserDao(FillDataActivity.this);
		List<User> users = new ArrayList<User>(userlist.values());
		dao.saveContactList(users);
	}
	
	public final int SUCCESS=10;
	public final int ABC=11;
	
	
	Handler handler= new Handler(){

		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case SUCCESS:
			
				break;
			case ABC:
				headportrait.setImageBitmap((Bitmap) msg.obj);
				break;
			default:
				break;
			}
		};
	};

	Bitmap bitmap=null;
	public Bitmap returnBitmap(final String url){
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					URL uri = new URL(url);
					HttpURLConnection openConnection = (HttpURLConnection) uri
							.openConnection();
					InputStream inputStream = openConnection.getInputStream();
					bitmap = BitmapFactory.decodeStream(inputStream);
					Message msg =Message.obtain();
					msg.what=ABC;
					msg.obj=bitmap;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}).start();
		return bitmap;
		
	}
	public void showImagePickDialog() {
		String title = "获取图片方式";
		String[] choices = new String[]{"拍照", "从手机中选择"};
		
		new AlertDialog.Builder(this)
			.setTitle(title)
			.setItems(choices, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					switch (which) {
					case 0:
						 startTakePhoto();
						
						break;
					case 1:
						startImagePick();
						break;
					}
				}
			})
			.setNegativeButton("返回", null)
			.show();
	}
	
	
	/**
     * 选择图片裁剪
     * 
     * @param output
     */
    private void startImagePick() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
           
            	startActivityForResult(Intent.createChooser(intent, "选择图片"),
            			ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);            	
           
            
        } else {
            intent = new Intent(Intent.ACTION_PICK,
                    Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");   
            	startActivityForResult(Intent.createChooser(intent, "选择图片"),
            			ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);            	 
            
        }
    }
    
    
    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
            final Intent imageReturnIntent) {
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
        case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
            startActionCrop(origUri);// 拍照后裁剪
            break;
        case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
            startActionCrop(imageReturnIntent.getData());// 选图后裁剪
            break;
        case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
            uploadNewPhoto();
            break;
        }
    }
    
    /**
     * 拍照后裁剪
     * 
     * @param data
     *            原始图片
     * @param output
     *            裁剪后图片
     */
    private void startActionCrop(Uri data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("output", this.getUploadTempFile(data));
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", CROP);// 输出图片大小
        intent.putExtra("outputY", CROP);
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
      
        	
        	startActivityForResult(intent,
        			ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
       
    }
 // 裁剪头像的绝对路径
    private Uri getUploadTempFile(Uri uri) {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(FILE_SAVEPATH);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        } else {
        	Toast.makeText(this, "无法保存上传的头像，请检查SD卡是否挂载", 0).show();
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

        // 如果是标准Uri
        if (TextUtils.isEmpty(thePath)) {
            thePath = ImageUtils.getAbsoluteImagePath(this, uri);
        }
        String ext = getFileFormat(thePath);
        ext = TextUtils.isEmpty(ext) ? "jpg" : ext;
        // 照片命名
        String cropFileName = "yoti_crop_" + timeStamp + "." + ext;
        // 裁剪头像的绝对路径
        protraitPath = FILE_SAVEPATH + cropFileName;
        protraitFile = new File(protraitPath);

        cropUri = Uri.fromFile(protraitFile);
        return this.cropUri;
    }
    
    /**
	 * 获取文件扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	public  String getFileFormat(String fileName) {
		if (TextUtils.isEmpty(fileName))
			return "";

		int point = fileName.lastIndexOf('.');
		return fileName.substring(point + 1);
	}
	
	
    private Bitmap protraitBitmap;
    private void uploadNewPhoto() {
      //  showWaitDialog("正在上传头像...");
        // 获取头像缩略图
        if (!TextUtils.isEmpty(protraitPath) && protraitFile.exists()) {
            protraitBitmap = ImageUtils.loadImgThumbnail(protraitPath, 480, 600);
            headportrait.setImageBitmap(protraitBitmap);
        } else {
        	Utils.showToast(FillDataActivity.this, "图像不存在，上传失败");
        }
            
        if (protraitBitmap != null) {
        	RequestParams params =new RequestParams();
        	//params.put("user_id", user_id);
			byte[] byts = IBitmapUtils.Bitmap2Bytes(protraitBitmap);
			String encodedPhotoStr = Base64.encodeToString(byts, Base64.DEFAULT);
        	params.put("head_img", encodedPhotoStr);
        	HttpUtils.post("http://112.126.72.250/ut_app/index.php?m=User&a=upload_head", params, new JsonHttpResponseHandler() {
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
                    		String head_image=response.getJSONObject("list").getString("head_img");
                    		((YoutiApplication)getApplication()).myPreference.setHeadImgPath(head_image);
                    		
                    		Utils.showToast(FillDataActivity.this, "上传成功:"+head_image);
                    	}else{
                    		Utils.showToast(FillDataActivity.this, response.getString("上传失败"));
                    	}
                    } catch (Exception e) {
                    	
                    }
                };
            });
        }
    }
    
    private String theLarge;
	private UserCenterBean.UserPhoto up;
    
    private void startTakePhoto() {
        Intent intent;
        // 判断是否挂载了SD卡
        String savePath = "";
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/youti_yonghu/Camera/";
            File savedir = new File(savePath);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        }

        // 没有挂载SD卡，无法保存文件
        if (TextUtils.isEmpty(savePath)) {
           // AppContext.showToastShort("无法保存照片，请检查SD卡是否挂载");
            return;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        String fileName = "yoti_" + timeStamp + ".jpg";// 照片命名
        File out = new File(savePath, fileName);
        Uri uri = Uri.fromFile(out);
        origUri = uri;

        theLarge = savePath + fileName;// 该照片的绝对路径
        
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        
        	 startActivityForResult(intent,
                     ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);           	
          
        
        
    }
	
	
	
}
