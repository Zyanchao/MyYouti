package com.youti.yonghu.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youti_geren.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.YoutiApplication;
import com.youti.utils.HttpUtils;
import com.youti.utils.IBitmapUtils;
import com.youti.utils.Utils;
import com.youti.view.CommunityBitmapCheckbox;
import com.youti.yonghu.bean.CardEntity;

import edu.hust.ui.base.LineBreakGroup;

public class IssuePhotoActivity extends Activity{
	private EditText et_comment;
	private LineBreakGroup mImageGroup;
//	private OwnCardTitleBar mTitleBar;
	private Dialog dialog;
	private CardEntity mCardEntity;
	
	private List<String> mTargetBitmapList;
	
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	Button btn_cancle,btn_issue;
	TextView tv;
	private List<String> mUploadBitmap = new ArrayList<String>();
	//private ProgressDialog dialog;
	SharedPreferences sp;
	
	public final String mPageName = "IssuePhotoActivity";
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
		setContentView(R.layout.layout_issuephoto);
	
		sp=getSharedPreferences("config", MODE_PRIVATE);
		btn_cancle=(Button) findViewById(R.id.btn_cancle);
		btn_issue=(Button) findViewById(R.id.btn_issue);
		mImageGroup = (LineBreakGroup)findViewById(R.id.image_group);
		et_comment=(EditText) findViewById(R.id.et_comment);
		
		tv=(TextView) findViewById(R.id.tv);
		mTargetBitmapList = new ArrayList<String>();

		btn_cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		}); 
		
		btn_issue.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				IssuePhoto();
				//safeInfo();
				//Utils.showToast(IssuePhotoActivity.this, "发布");
				
			}

		});
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
	
	public static int count;
	
	
	
	private void IssuePhoto() {
		dialog = Utils.createProgressBarDialog(IssuePhotoActivity.this, "提交中...");
        dialog.show();
		if(mTargetBitmapList != null && mTargetBitmapList.size() > 0){
			count = mTargetBitmapList.size();
			for(String tBitmap : mTargetBitmapList){
				
				
				Bitmap upBitmap = imageLoader.loadImageSync(tBitmap);
				upBitmap = IBitmapUtils.zoomBitmap(upBitmap, 500, 600);
				byte[] byts = IBitmapUtils.Bitmap2Bytes(upBitmap);
				String encodedPhotoStr = Base64.encodeToString(byts, Base64.DEFAULT);
				encodedPhotoStr1=encodedPhotoStr1 +encodedPhotoStr+"!";
				//System.out.println(encodedPhotoStr1);
				count--;
				if(count==0){
					safeInfo(encodedPhotoStr1);
				}
			}
			
		}
		
	}
	
	private void safeInfo(String Str){
		RequestParams params = new RequestParams();
		String userId = ((YoutiApplication)getApplication()).myPreference.getUserId();
		String userName = ((YoutiApplication)getApplication()).myPreference.getUserName();
		String head_img=((YoutiApplication)getApplication()).myPreference.getHeadImgPath();
		params.put("text", et_comment.getText().toString() == null ? "fafda":et_comment.getText().toString());
		params.put("user_id", userId);
		params.put("user_img", head_img);
		params.put("user_name", userName);
		params.put("photo", Str);
		params.put("user_type", "2");
		HttpUtils.post("http://112.126.72.250/ut_app/index.php?m=Community&a=issueShow", params, new JsonHttpResponseHandler(){
			
			public void onStart() {  
                super.onStart(); 
               // System.out.println("onStart..."); 
                
            } 
            public void onFailure(int statusCode,
                    org.apache.http.Header[] headers,
                    java.lang.Throwable throwable,
                    org.json.JSONObject errorResponse) {
            	dialog.dismiss();
            	Utils.showToast(IssuePhotoActivity.this, "提交失败");
            //	System.out.println("onFailure..."); 
            };
            public void onFinish() {
            //	System.out.println("onFinish..."); 
            };
            public void onSuccess(int statusCode,
                    org.apache.http.Header[] headers,
                    org.json.JSONObject response){ 
            	//System.out.println("onSuccess..."); 
				try {
					String state = response.getString("code");
					if(state.equals("1")){
	            		//((YotiApplication)getApplication()).refreshCardList = true;
						//System.out.println(response.getString("code"));
						dialog.dismiss();
						Intent intent =new Intent(IssuePhotoActivity.this,MyIssuedPhotoActivity.class);
						startActivity(intent);
						finish();
						Toast.makeText(IssuePhotoActivity.this, "上传完成", 0).show();
	            	}else{
	            		//System.out.println(response.getString("code"));
	            	}
					//hideWaitingDialog();
            		//CommunitySendCardActivity.this.finish();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            }
		});
	}
	
	Handler handler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			
		};
		
	};
	
/*	private void safeInfo(){
		
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
			//	photo = photo + baos.toString() + "#";
			//	System.out.println(baos.toString());
				byte[] byts = IBitmapUtils.Bitmap2Bytes(upBitmap);
				encodedPhotoStr = Base64.encodeToString(byts, Base64.DEFAULT);
				System.out.println(encodedPhotoStr);
				photo = photo + encodedPhotoStr+ "!";
				
				
				String path =Environment.getExternalStorageDirectory().getAbsolutePath();
				System.out.print(path);
				
				File file =new File(path, "abcd.txt");
				try {
					OutputStream os =new FileOutputStream(file);
					baos.write(encodedPhotoStr.getBytes("utf-8"));
					os.flush();
					os.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				//tv.setText(encodedPhotoStr);
				
				System.out.println("wanbi");
				count--;
			}
		}
		
		params.put("text", et_comment.getText().toString() == null ? "fafda":et_comment.getText().toString());
		params.put("user_id", sp.getString("user_id", "111"));
		params.put("user_img", sp.getString("user_img", "1111"));
		params.put("user_name", sp.getString("user_name", "woshishui"));
		//params.put("photo", photo);
		
		
		HttpUtils.post("http://112.126.72.250/ut_app/index.php?m=Community&a=issueShow", params, new JsonHttpResponseHandler(){
			public void onStart() {  
                super.onStart(); 
                System.out.println("onStart");
            } 
            public void onFailure(int statusCode,
                    org.apache.http.Header[] headers,
                    java.lang.Throwable throwable,
                    org.json.JSONObject errorResponse) {
            	 System.out.println("onFailure");
            	 
            };
            public void onFinish() {
            	System.out.println("onFinish............");
            };
            public void onSuccess(int statusCode,
                    org.apache.http.Header[] headers,
                    org.json.JSONObject response){ 
            	System.out.println("onSuccess............");
            	System.out.println(response.toString());
            	
				try {
					String info =URLDecoder.decode(
							response.getString("info"),
							"UTF-8");
					System.out.println(info);
					String state = response.getString("code");
					if(state.equals("1")){
						System.out.println(response.getString("code")+"1");
	            	}else{
	            		System.out.println(response.getString("code")+"0");
	            	}
				} catch (Exception e) {
					e.printStackTrace();
				}
            	
            }
		});
		
		
		
		  params.put("user_id", sp.getString("user_id", "111"));
			params.put("coach_id", sp.getString("user_img", "1111"));
			params.put("content", sp.getString("user_name", "woshishui"));
			params.put("file_base", photo);
		 HttpUtils.post("http://112.126.72.250/ut_app/index.php?m=Coach&a=add_coach_comment", params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				System.out.println(arg2.toString()+"%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				System.out.println("onFailure^^^^^^^");
			}
		});
	}*/
	
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
			CommunityBitmapCheckbox cbck = new CommunityBitmapCheckbox(IssuePhotoActivity.this);
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
		final CommunityBitmapCheckbox cbck = new CommunityBitmapCheckbox(IssuePhotoActivity.this);
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
	private String encodedPhotoStr1="";  
	
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
	
	/**     
     * 拍照获取图片     
     *      
     */      
    protected void doTakePhoto() {      
        try {      
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);      
            startActivityForResult(cameraIntent, CAMERA_WITH_DATA);      
        } catch (ActivityNotFoundException e) {      
            e.printStackTrace();      
        }      
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
}
