package com.youti.yonghu.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youti_geren.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.Constants;
import com.youti.appConfig.YoutiApplication;
import com.youti.utils.HttpUtils;
import com.youti.utils.IBitmapUtils;
import com.youti.utils.IntentJumpUtils;
import com.youti.utils.Utils;
import com.youti.view.CommunityBitmapCheckbox;
import com.youti.yonghu.bean.CardEntity;

import edu.hust.ui.base.LineBreakGroup;
public class CommentActivity extends Activity implements OnClickListener{
	private TextView tvcancel,tvIssue;
	private RadioGroup rg;
	private RadioButton rb_bad,rb_middle,rb_great;
	
	private EditText et_comment;
	private LineBreakGroup mImageGroup;
//	private OwnCardTitleBar mTitleBar;
	private CardEntity mCardEntity;
	private List<String> mTargetBitmapList;
	
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	private String status = "1";
	private List<String> mUploadBitmap = new ArrayList<String>();
	//private ProgressDialog dialog;
	String coach_img;
	int code;
	private String agree_id,id,order_id,coach_tel,coach_name,courseTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_pinglun);
		code = getIntent().getIntExtra("code", 1001);
		id = getIntent().getStringExtra(Constants.KEY_ID);
		order_id = getIntent().getStringExtra("order_id");
		/**从 我的教练 评论 跳转 过来 需要获取 tel ，username*/
		agree_id = getIntent().getStringExtra("agree_id");
		coach_tel = getIntent().getStringExtra(Constants.KEY_CHAT_TEL);
		coach_name = getIntent().getStringExtra(Constants.KEY_CHAT_USERNAME);
		courseTitle=getIntent().getStringExtra(Constants.KEY_TITLE);
		coach_img=getIntent().getStringExtra(Constants.KEY_CHAT_AVATAR);
		initView();
		initListenter();
	}
	
	public final String mPageName="CommentActivity";
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
	private void initView() {
		
		tvcancel = (TextView) findViewById(R.id.cancel);
		tvIssue = (TextView) findViewById(R.id.issue);
		rg = (RadioGroup) findViewById(R.id.rg);
		rb_bad = (RadioButton) findViewById(R.id.rb_bad);
		rb_middle= (RadioButton) findViewById(R.id.rb_middle);
		rb_great = (RadioButton) findViewById(R.id.rb_great);
		et_comment = (EditText) findViewById(R.id.et_comment);
		mImageGroup = (LineBreakGroup)findViewById(R.id.image_group);
		
		mTargetBitmapList = new ArrayList<String>();

		
		options = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getBaseContext()));
		 init();
		
	}
	
	private void initListenter() {
		tvcancel.setOnClickListener(this);
		tvIssue.setOnClickListener(this);
		rg.setOnCheckedChangeListener(new MyOnChangeListener());
	}

	
	
	private void init(){
		
		initImageView();
		initForward();
	}
	
	private void initForward(){
		if(mCardEntity != null){
			//mEditText.setText("//"+ mCardEntity.getName() +":"+mCardEntity.getMainBody());
			List<String> bitmapUrl = mCardEntity.getSourceList();
			if(bitmapUrl != null){
				mUploadBitmap = bitmapUrl;
				for(String bmUrl : bitmapUrl){
					loadImageGroup(bmUrl,false);
				}
			}
		}
	}
	
	private void initImageView(){
		if(mCardEntity == null){
			CommunityBitmapCheckbox cbck = new CommunityBitmapCheckbox(CommentActivity.this);
			cbck.setBodyImage(getResources().getDrawable(R.drawable.send_add));
			cbck.setCheckIconVisibility(false);
			cbck.setGlobalVisibility(true);
			cbck.setGlobalOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					doSelectImageFromLoacal();
				}
			});
			mImageGroup.addView(cbck);
		}else{
			if(mCardEntity.getSourceList() == null || mCardEntity.getSourceList().size() == 0){
				findViewById(R.id.image_layout).setVisibility(View.GONE);
			}
		}
	}
	

	
	private void loadImageGroup(final String path,Boolean editAble){
		final CommunityBitmapCheckbox cbck = new CommunityBitmapCheckbox(CommentActivity.this);
		//cbck.setBodyImage(path);
		cbck.setBodyImage(path);
		if(editAble){
			cbck.setCheckIconVisibility(true);
			cbck.setGlobalVisibility(false);
		}else{
			cbck.setCheckIconVisibility(false);
			cbck.setGlobalVisibility(false);
		}
		cbck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mImageGroup.removeView(cbck);
				mTargetBitmapList.remove(path);
			}
		});
		
		mImageGroup.addView(cbck, mImageGroup.getChildCount() - 1);
	}
	
	
	/*用来标识请求照相功能的activity*/        
	private static final int CAMERA_WITH_DATA = 1001;        
	/*用来标识请求gallery的activity*/        
	private static final int PHOTO_PICKED_WITH_DATA = 1002;  
	
	private String encodedPhotoStr;
	
	/**     
     * 从本地手机中选择图片     
     */    
	private void doSelectImageFromLoacal(){     
		if(mTargetBitmapList.size()>=9){
			//IToastUtils.showMsg(this, "最多可以上传9张图片！");
			Toast.makeText(this, "9", 0).show();
			return;
		}
        Intent localIntent = new Intent();      
        localIntent.setType("image/*");      
        localIntent.setAction("android.intent.action.GET_CONTENT");      
        Intent localIntent2 = Intent.createChooser(localIntent, "选择图片");      
        startActivityForResult(localIntent2, PHOTO_PICKED_WITH_DATA);      
    }
	
  
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {      
        if (resultCode != RESULT_OK)      
            return;      
        switch (requestCode) {      
        case PHOTO_PICKED_WITH_DATA: //从本地选择图片      
            Uri selectedImageUri = data.getData();      
            
            if(selectedImageUri != null){      
            	loadImageGroup(selectedImageUri.toString(),true);
                mTargetBitmapList.add(selectedImageUri.toString());
            }      
            break;      
 
        }      
    }  
	
	/**
	 * 处理rg　点击事件
	 *
	 */
	class MyOnChangeListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (rg.getCheckedRadioButtonId()) {
			case R.id.rb_great:
				status = "1";
				break;
			case R.id.rb_middle:
				status = "2";
				break;
			case R.id.rb_bad:
				status = "3";
				break;

			default:
				break;
			}
		}
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			
			finish();
			break;

		case R.id.issue:
			issueComment();
			break;
		default:
			break;
		}
	}
	

	/**
	 * 
	* @Title: issueComment 
	* @Description: TODO(发布评论 ) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	
	public static int count;
	private Dialog createProgressBarDialog;
	
	private void issueComment(){
		createProgressBarDialog = Utils.createProgressBarDialog(CommentActivity.this, "正在提交评论内容...");
		createProgressBarDialog.show();
		String urlStr="";
		RequestParams params = new RequestParams();
		SharedPreferences sp =getSharedPreferences("config", MODE_PRIVATE);
		String photo = "";
		count = mTargetBitmapList.size();
		if(mTargetBitmapList != null && mTargetBitmapList.size() > 0&&count>0){			
			for(String tBitmap : mTargetBitmapList){			
				
				Bitmap upBitmap = imageLoader.loadImageSync(tBitmap);
				upBitmap = IBitmapUtils.zoomBitmap(upBitmap, 500, 600);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				upBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
				byte[] byts = IBitmapUtils.Bitmap2Bytes(upBitmap);
				encodedPhotoStr = Base64.encodeToString(byts, Base64.DEFAULT);
				photo = photo + encodedPhotoStr+ "!";
				/*String path =Environment.getExternalStorageDirectory().getAbsolutePath();
				
				File file =new File(path, "abcdef.txt");
				try {
					OutputStream os =new FileOutputStream(file);
					os.write(encodedPhotoStr.getBytes("utf-8"));
					os.flush();
					os.close();
				} catch (Exception e) {
					e.printStackTrace();
				}*/
				count--;
			}
		}
		
		 //coach_id:教练id
		//user_id:用户id
		// content:评论内容
		//file_base：评论图片
		// status:1好评2中评3差评

		if(code==Constants.REQUEST_CODE_COACH){
			params.put("coach_id",id);
			if(agree_id!=null){
				params.put("agree_id",agree_id);
			}
			urlStr=Constants.COACH_COMMENT;
			 
		}else if(code==Constants.REQUEST_CODE_COURSE){
			if(id!=null){
				params.put("course_id",id);
			}else {
				Utils.showToast(CommentActivity.this, "课程参数有误");
				return;
			}
			//params.put("status", status);
			if(order_id!=null){
				params.put("order_id",order_id);
			}else {
				//Utils.showToast(CommentActivity.this, "课程参数有误");
				//return;
			}
			urlStr=Constants.COURSE_COMMENT;
			
		}else if(code==Constants.REQUEST_CODE_VIDEO){
			params.put("video_id","1");
		}
		
		params.put("content", et_comment.getText().toString() == null ? "今天累了不写评论了。。。":et_comment.getText().toString());
		params.put("user_id", YoutiApplication.getInstance().myPreference.getUserId());
		
		if(!photo.equals("")){
			params.put("file_base", photo);
		}
		
		
		
		
		
		HttpUtils.post(urlStr, params, new JsonHttpResponseHandler(){
			public void onStart() {  
                super.onStart(); 
                System.out.println("onStart");
            } 
            public void onFailure(int statusCode,
                    org.apache.http.Header[] headers,
                    java.lang.Throwable throwable,
                    org.json.JSONObject errorResponse) {
            	createProgressBarDialog.dismiss();
            	Utils.showToast(CommentActivity.this, "评论失败");
            };
            public void onFinish() {
            };
            public void onSuccess(int statusCode,
                    org.apache.http.Header[] headers,
                    org.json.JSONObject response){ 
            	createProgressBarDialog.dismiss();
            	
				try {
					String info =URLDecoder.decode(response.getString("info"),"UTF-8");
					System.out.println(info);
					String state = response.getString("code");
					if(state.equals("1")){
						Bundle bundle = new Bundle();
						
						if(code==Constants.REQUEST_CODE_COURSE){//跳转到 课程详情页
							Utils.showToast(CommentActivity.this, "跳转到 课程详情页");
							bundle.putString(Constants.KEY_ID, id);
							bundle.putString(Constants.KEY_TITLE, courseTitle);
							IntentJumpUtils.nextActivity(CourseDetailActivity.class, CommentActivity.this, bundle);
						}else if(code==Constants.REQUEST_CODE_COACH){//跳转到 教练详情页
							Utils.showToast(CommentActivity.this, "跳转到 教练详情页");
							bundle.putString(Constants.KEY_ID, id);
							bundle.putString(Constants.KEY_CHAT_TEL, coach_tel);
							bundle.putString(Constants.KEY_CHAT_USERNAME, coach_name);
							bundle.putString(Constants.KEY_CHAT_AVATAR, coach_img);
							IntentJumpUtils.nextActivity(CoachDetailActivity.class, CommentActivity.this, bundle);
						}
						finish();
	            	}else{
	            		Utils.showToast(CommentActivity.this, response.getString("code")+"0");
	            		//System.out.println(response.getString("code")+"0");
	            	}
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
		});
	}
}
