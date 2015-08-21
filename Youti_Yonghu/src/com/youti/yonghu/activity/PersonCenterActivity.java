package com.youti.yonghu.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.YoutiApplication;
import com.youti.chat.domain.User;
import com.youti.fragment.PersonCenterCoachFragment;
import com.youti.fragment.PersonCenterCourseFragment;
import com.youti.utils.HttpUtils;
import com.youti.utils.IBitmapUtils;
import com.youti.utils.ImageUtils;
import com.youti.utils.Utils;
import com.youti.view.CircleImageView;
import com.youti.view.HorizontalListView;
import com.youti.yonghu.bean.UserCenterBean;
import com.youti.yonghu.bean.UserCenterBean.UserPhoto;
public class PersonCenterActivity extends FragmentActivity implements OnClickListener{
	TextView tv_first,name,userSignature;
	TextView tv_second;
	TextView tv1 ,tv2;
	FrameLayout fl_content;
	ViewPager view_pager;
	ArrayList<Fragment> list =new ArrayList<Fragment>();
	int screenWidth;
	View indicate_line;
	ImageView iv_send_add,iv_near,sex,iv_back;
	CircleImageView headportrait;
	HorizontalListView hlv;
	List<UserPhoto> photo_list=new ArrayList<UserPhoto>();
	MyHLVAdapter hlvAdapter;
	Uri uri_Photo;
	String user_id;
	Dialog dialog;
	DisplayImageOptions options;
    private Dialog createProgressBarDialog;

	private ArrayList<String> photoList1;
	  private final static String FILE_SAVEPATH = Environment
	            .getExternalStorageDirectory().getAbsolutePath()
	            + "/youti_yonghu/Portrait/";
	    private Uri origUri;
	    private Uri cropUri;
	    private File protraitFile;
	    
	    private final static int CROP = 200;
		protected static final int SUCCESS = 0;
		protected static final int ABC = 1;
	    private String protraitPath;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_personcenter);
		
		options = new DisplayImageOptions.Builder()    
        .showStubImage(R.drawable.sq_head)          // 设置图片下载期间显示的图片    
        .showImageForEmptyUri(R.drawable.empty_photo)  // 设置图片Uri为空或是错误的时候显示的图片    
        .showImageOnFail(R.drawable.empty_photo)       // 设置图片加载或解码过程中发生错误显示的图片        
        .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中    
        .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中    
        .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片    
        .build();
		
			user_id=((YoutiApplication)getApplication()).myPreference.getUserId();
		initView();
		
		initListener();
		initData();
	
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	@SuppressLint("NewApi")
	private void initData() {
		tv_first.setScaleX(0.8f);
		tv_first.setScaleY(0.8f);
		tv_second.setScaleX(0.8f);
		tv_second.setScaleY(0.8f);
		//设置圆形头像
		/*Bitmap bitmap =BitmapFactory.decodeResource(getResources(), R.drawable.spxq_pic);
		headportrait.setImageDrawable(new CircleImageDrawable(bitmap));*/
		
		
		//创建课程与教练的Fragment
		PersonCenterCourseFragment pccf =new PersonCenterCourseFragment();
		PersonCenterCoachFragment pccf2=new PersonCenterCoachFragment();
		list.add(pccf);
		list.add(pccf2);
		view_pager.setAdapter(new MyAdapter(getSupportFragmentManager()));
		
		requestData();
		
		
		
		
		
		
	}
	private UserCenterBean userCenterBean;
	/**
	 * 请求网络
	 */
	private void requestData() {
		RequestParams params =new RequestParams();
//		String user_id = ((YoutiApplication)getApplication()).myPreference.getUserId();
		params.put("user_id", user_id );
		HttpUtils.post("http://112.126.72.250/ut_app/index.php?m=User&a=click_head", params, new TextHttpResponseHandler() {
			
			

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson =new Gson();
				userCenterBean = gson.fromJson(arg2, UserCenterBean.class);
				if(userCenterBean.code.equals("1")){
					Message msg =Message.obtain();
					msg.arg1=SUCCESS;
					handler.sendMessage(msg);
				}else{
					Utils.showToast(PersonCenterActivity.this, "连接失败");
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				
			}
		});
	}
	
	
	private void initListener() {
		tv_second.setOnClickListener(this);
		tv_first.setOnClickListener(this);
		headportrait.setOnClickListener(this);
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		ViewPropertyAnimator.animate(indicate_line).translationX(screenWidth/5).setDuration(0);
		
		view_pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				lightAndScaleTabTitle();
				int targetPosition=0;
				if(position==0){
					targetPosition = screenWidth/5 ;
				}else{
					targetPosition = screenWidth/5+screenWidth/2 ;
				}
				ViewPropertyAnimator.animate(indicate_line).translationX(targetPosition).setDuration(0);
			}
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				
				
			}
			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
		
		/**
		 * 相册点击事件
		 */
		hlv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position==photoList.size()){
					if(photoList.size()>8){
						Utils.showToast(PersonCenterActivity.this, "最多上传9张图片,请长按相片点击删除");
						return;
					}
					//打开相册，选择照片
					showImagePickDialog(true);
				}else{
					//查看相片
					boolean isFirstClikcPhoto = YoutiApplication.getInstance().myPreference.getIsFirstClikcPhoto();
					if(isFirstClikcPhoto){
						//小U提示，长按可以点击删除相片
						final Dialog okDialog =new Dialog(PersonCenterActivity.this,R.style.tkdialog);
						View okView=View.inflate(PersonCenterActivity.this, R.layout.dialog_ok, null);
						okDialog.setContentView(okView);
						okDialog.show();
						
						okView.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								okDialog.dismiss();
								YoutiApplication.getInstance().myPreference.setIsFirstClikcPhoto(false);
							}
						});
					}else{
						
//						Utils.showToast(PersonCenterActivity.this, photoList.size()+"");
						Intent intent =new Intent(PersonCenterActivity.this,PhotoShowActivity.class);
						intent.putStringArrayListExtra("photo_list", photoList1);
						intent.putExtra("location", position+"");
						startActivity(intent);
					}
					
					
				}
			}
		});
		//相册长按点击事件
		hlv.setOnItemLongClickListener(new OnItemLongClickListener() {

			private Vibrator vibrator;

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				if(position==photoList.size()){
					return false;
				}else{
					//Utils.showToast(PersonCenterActivity.this, photoList.get(position).photo_id);
					/*new AlertDialog.Builder(PersonCenterActivity.this)
					.setTitle("是否要删除该照片")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							deletePhoto(position);					
						}
					})
					.setNegativeButton("取消", null)
					.show();*/
					 vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);  
				        long [] pattern = {1,50};   // 停止 开启 停止 开启   
				        vibrator.vibrate(pattern,-1);  
				        
					final Dialog deletePhotoDialog =new Dialog(PersonCenterActivity.this, R.style.tkdialog);
					View v =View.inflate(PersonCenterActivity.this, R.layout.layout_contactservice, null);
					LinearLayout ll_title=(LinearLayout) v.findViewById(R.id.ll_title);
					ll_title.setVisibility(View.VISIBLE);
					ImageView iv_logo =(ImageView) v.findViewById(R.id.iv_logo);
					ImageLoader.getInstance().displayImage(photoList.get(position).photo_url, iv_logo);
					TextView tv_title=(TextView) v.findViewById(R.id.tv_title);
					tv_title.setText("确定删除该照片吗？");
					TextView tv_content =(TextView) v.findViewById(R.id.tv_content);
					tv_content.setVisibility(View.GONE);
					LinearLayout ll_dial =(LinearLayout) v.findViewById(R.id.ll_dial);
					LinearLayout ll_back=(LinearLayout) v.findViewById(R.id.ll_back);
					TextView tv_dial=(TextView) v.findViewById(R.id.tv_dial);
					tv_dial.setText("确定删除");
					
					deletePhotoDialog.setContentView(v);
					deletePhotoDialog.show();
					ll_dial.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							deletePhotoDialog.dismiss();
							deletePhoto(position);	
						}
					});
					
					ll_back.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							deletePhotoDialog.dismiss();
						}
					});
					
				}
				return false;
			}
		});
	}
	protected void deletePhoto(final int position) {
		String urlDel="http://112.126.72.250/ut_app/index.php?m=User&a=del_photo";
		RequestParams params =new RequestParams();
		params.put("user_id", user_id);
		params.put("photo_id", photoList.get(position).photo_id);
		//Utils.showToast(getApplicationContext(), photoList.get(position).photo_id);
		createProgressBarDialog2 = Utils.createProgressBarDialog(PersonCenterActivity.this, "正在删除...");
		createProgressBarDialog2.show();
		HttpUtils.post(urlDel, params, new JsonHttpResponseHandler(){

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				createProgressBarDialog2.dismiss();
				Utils.showToast(PersonCenterActivity.this, "删除失败");
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				createProgressBarDialog2.dismiss();
				String code;
				try {
					code = response.getString("code");
					if(code.equals("1")){
						photoList.remove(position);
						photoList1.remove(position);
						hlv.setAdapter(new MyHLVAdapter(photoList));
						Utils.showToast(PersonCenterActivity.this, "删除照片成功");
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Utils.showToast(PersonCenterActivity.this, "删除失败");
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				
			}

			@Override
			public void onStart() {

			}
			
		});
	}
	private void initView() {
		tv_second=(TextView) findViewById(R.id.tv_second);
		tv_first=(TextView) findViewById(R.id.tv_first);
		view_pager=(ViewPager) findViewById(R.id.view_pager);
		fl_content= (FrameLayout) findViewById(R.id.fl_content);
		indicate_line =findViewById(R.id.indicate_line);
		hlv=(HorizontalListView) findViewById(R.id.hlv);
		headportrait=(CircleImageView) findViewById(R.id.headportrait);
		name=(TextView) findViewById(R.id.name);
		userSignature=(TextView) findViewById(R.id.userSignature);
		sex=(ImageView) findViewById(R.id.sex);
		
		iv_back=(ImageView) findViewById(R.id.iv_back);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.tv_second:
				view_pager.setCurrentItem(1);
				
				break;
			case R.id.tv_first:
			
				view_pager.setCurrentItem(0);
				break;
			case R.id.headportrait:
				showImagePickDialog(false);
				break;
		}
	}
	
	class MyAdapter extends FragmentPagerAdapter{

		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public int getCount() {
			return list.size();
		}	
		
		
	}
	
	class MyHLVAdapter extends BaseAdapter{
		List<UserCenterBean.UserPhoto> list;
		public MyHLVAdapter(List<UserCenterBean.UserPhoto> list){
			this.list=list;
		}
		@Override
		public int getCount() {
			if(list==null){
				return 1;
			}else{
				return list.size()+1;
			}
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v =View.inflate(PersonCenterActivity.this, R.layout.item_xiangce, null);
			ImageView iv=(ImageView) v.findViewById(R.id.iv_photo);
			if(list==null){
				iv.setBackgroundResource(R.drawable.send_add);
				return v;
			}
			if(position==list.size()){
				iv.setBackgroundResource(R.drawable.send_add);
			}else{
				ImageLoader.getInstance().displayImage(list.get(position).photo_url, iv);
				
			}
			
			return v;
		}
		
	}
	
	private void lightAndScaleTabTitle(){
		int currentPage = view_pager.getCurrentItem();
		
		tv_first.setTextColor(currentPage==0?Color.parseColor("#6049a1")
				:Color.parseColor("#333333"));
		tv_second.setTextColor(currentPage==1?Color.parseColor("#6049a1")
				:Color.parseColor("#333333"));
		
		ViewPropertyAnimator.animate(tv_first).scaleX(currentPage==0?1f:0.8f).setDuration(200);
		ViewPropertyAnimator.animate(tv_first).scaleY(currentPage==0?1f:0.8f).setDuration(200);
		ViewPropertyAnimator.animate(tv_second).scaleX(currentPage==1?1f:0.8f).setDuration(200);
		ViewPropertyAnimator.animate(tv_second).scaleY(currentPage==1?1f:0.8f).setDuration(200);
	}
	private List<UserCenterBean.UserPhoto> photoList;

	Handler handler= new Handler(){

	

		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case SUCCESS:
				
				String userName = userCenterBean.list.user_info.user_name;
				name.setText(userName);
				userSignature.setText(userCenterBean.list.user_info.sign);
				if("1".equals(userCenterBean.list.user_info.sex)){
					sex.setBackgroundResource(R.drawable.userhome_boy);
				}else{
					sex.setBackgroundResource(R.drawable.userhome_girl);
				}
				//保存获取到的用户头像
				
					
					((YoutiApplication)getApplication()).myPreference.setUserName(userName);
					((YoutiApplication)getApplication()).myPreference.setUserId(user_id);
					User user = new User();
					
					if(userCenterBean.list.user_info.head_img.startsWith("http:")){
						ImageLoader.getInstance().displayImage(userCenterBean.list.user_info.head_img, headportrait);
						((YoutiApplication)getApplication()).myPreference.setHeadImgPath(userCenterBean.list.user_info.head_img);
						user.setAvatar(userCenterBean.list.user_info.head_img);
					}else{					
						ImageLoader.getInstance().displayImage("http://112.126.72.250/ut_app"+userCenterBean.list.user_info.head_img, headportrait);
						((YoutiApplication)getApplication()).myPreference.setHeadImgPath("http://112.126.72.250/ut_app"+userCenterBean.list.user_info.head_img);
						user.setAvatar("http://112.126.72.250/ut_app"+userCenterBean.list.user_info.head_img);
					}
					user.setUsername(userName);
					
					YoutiApplication.getInstance().setContact(user);
				
				/*User u = new User();
				u.setAvatar("http://112.126.72.250/ut_app"+userCenterBean.list.user_info.head_img);
				Map<String, User>  map = new Map<String, User>();
				YoutiApplication.getInstance().setContactList(map);*/
				//尚未添加性别字段
				/*ImageView iv1=new ImageView(PersonCenterActivity.this);
				iv1.setBackgroundResource(R.drawable.send_add);
				photo_list.add(iv1);*/
				
				photo_list=userCenterBean.list.user_photo;
				photoList = new ArrayList<UserCenterBean.UserPhoto>();
				photoList1 = new ArrayList<String>();
				if(photo_list!=null){
					
					for(int i=0;i<photo_list.size();i++){
						String str ="http://112.126.72.250/ut_app"+photo_list.get(i).photo_url;
						UserCenterBean ucb1 =new UserCenterBean();
						UserCenterBean.UserPhoto up1 =ucb1.new UserPhoto();
						up1.setPhoto_url(str);
						up1.setPhoto_id(photo_list.get(i).photo_id);
						photoList.add(up1);
						photoList1.add(str);
					}
				}
					
					if(hlvAdapter==null){
						hlvAdapter=new MyHLVAdapter(photoList);
						hlv.setAdapter(hlvAdapter);
					}else{
						hlvAdapter.notifyDataSetChanged();
					}
				
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
	
	
	
	public void showImagePickDialog(final boolean b) {
		/*String title = "获取图片方式";
		String[] choices = new String[]{"拍照", "从手机中选择"};
		
		new AlertDialog.Builder(this)
			.setTitle(title)
			.setItems(choices, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					switch (which) {
					case 0:
						 startTakePhoto(b);
						
						break;
					case 1:
						startImagePick(b);
						break;
					}
				}
			})
			.setNegativeButton("返回", null)
			.show();*/
		
		
		
		final Dialog selectDialog = new Dialog(PersonCenterActivity.this, R.style.tkdialog);
		View v =View.inflate(PersonCenterActivity.this, R.layout.dialog_getimage, null);
		selectDialog.setContentView(v);
		selectDialog.show();
		v.findViewById(R.id.tv_takephoto).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectDialog.dismiss();
				startTakePhoto(b);
			}
		});
		v.findViewById(R.id.tv_selectphoto).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectDialog.dismiss();
				startImagePick(b);
			}
		});
		
		v.findViewById(R.id.tv_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectDialog.dismiss();
			}
		});
		
	}
	
	
	/**
     * 选择图片裁剪
     * 
     * @param output
     */
    private void startImagePick(boolean b) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            if(!b){
            	startActivityForResult(Intent.createChooser(intent, "选择图片"),
            			ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);            	
            }else{
            	startActivityForResult(Intent.createChooser(intent, "选择图片"),
            			ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP1);   
            }
        } else {
            intent = new Intent(Intent.ACTION_PICK,
                    Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            if(!b){
            	startActivityForResult(Intent.createChooser(intent, "选择图片"),
            			ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);            	
            }else{
            	startActivityForResult(Intent.createChooser(intent, "选择图片"),
            			ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP1);   
            }
        }
    }
    
    
    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
            final Intent imageReturnIntent) {
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
        case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
            startActionCrop(origUri,false);// 拍照后裁剪
            break;
        case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
            startActionCrop(imageReturnIntent.getData(),false);// 选图后裁剪
            break;
        case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
            uploadNewPhoto(false);
            break;
        case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP1:
        	startActionCrop(imageReturnIntent.getData(),true);// 选图后裁剪
        	break;
        case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA1:
        	 startActionCrop(origUri,true);// 拍照后裁剪
        	break;
        case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD1:
        	uploadNewPhoto(true);
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
    private void startActionCrop(Uri data,boolean b) {
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
        if(!b){
        	
        	startActivityForResult(intent,
        			ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
        }else{
        	startActivityForResult(intent,
        			ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD1);

        }
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
        String cropFileName = "osc_crop_" + timeStamp + "." + ext;
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
    private void uploadNewPhoto(boolean b ) {
      //  showWaitDialog("正在上传头像...");
        // 获取头像缩略图
        if (!TextUtils.isEmpty(protraitPath) && protraitFile.exists()) {
            protraitBitmap = ImageUtils.loadImgThumbnail(protraitPath, 600, 480);
        } else {
        	Utils.showToast(PersonCenterActivity.this, "图像不存在，上传失败");
        }
        
        
        if(protraitBitmap!=null&&b){
        	RequestParams params =new RequestParams();
        	params.put("user_id", user_id);
        	byte[] byts = IBitmapUtils.Bitmap2Bytes(protraitBitmap);
			String encodedPhotoStr = Base64.encodeToString(byts, Base64.DEFAULT);
        	params.put("photo_url", encodedPhotoStr);
        	
        	
        	UserCenterBean ucb =new UserCenterBean();
        	 up = ucb.new UserPhoto();
        	up.setPhoto_url("file:///"+protraitPath);
        	//String path =Environment.getExternalStorageDirectory().getAbsolutePath();
			//System.out.print(path);
			
			/*File file =new File(path, "abcd.txt");
			try {
				OutputStream os =new FileOutputStream(file);
				os.write(encodedPhotoStr.getBytes("utf-8"));
				os.flush();
				os.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
        	
        	
        	
        	HttpUtils.post("http://112.126.72.250/ut_app/index.php?m=User&a=upload_photo", params, new JsonHttpResponseHandler() {
                public void onStart() {  
                    super.onStart(); 
                    createProgressBarDialog = Utils.createProgressBarDialog(PersonCenterActivity.this, "正在上传相片...");
                    createProgressBarDialog.show();
                   
                } 
                public void onFailure(int statusCode,
                        org.apache.http.Header[] headers,
                        java.lang.Throwable throwable,
                        org.json.JSONObject errorResponse) {   
                	 createProgressBarDialog.dismiss();
                	 Utils.showToast(PersonCenterActivity.this, "上传失败");
                };
                public void onFinish() {
                };
                public void onSuccess(int statusCode,
                        org.apache.http.Header[] headers,
                        org.json.JSONObject response){ 
                	 createProgressBarDialog.dismiss();
                    try {
                    	//接口返回
                    	String state =  response.getString("code");
                    	if(state.equals("1")){
                    		String photo_id=response.getJSONObject("list").getString("photo_id");
                    		//String head_image=response.getJSONObject("list").getString("head_img");
                    		//((YoutiApplication)getApplication()).myPreference.setHeadImgPath(head_image);
                    		// headportrait.setImageBitmap(protraitBitmap);
                    		//hlvAdapter.notifyDataSetChanged();
                    		up.setPhoto_id(photo_id);
                    		photoList.add(0,up);
                    		photoList1.add(0,"file:///"+protraitPath);
                    		hlv.setAdapter(new MyHLVAdapter(photoList));
                    		Utils.showToast(PersonCenterActivity.this, "上传成功");
                    	}else{
                    		Utils.showToast(PersonCenterActivity.this, response.getString("上传失败"));
                    	}
                    } catch (Exception e) {
                    	
                    }
                };
            });
        	
        }
        
        if (protraitBitmap != null&&!b) {
        	RequestParams params =new RequestParams();
        	params.put("user_id", user_id);
			byte[] byts = IBitmapUtils.Bitmap2Bytes(protraitBitmap);
			String encodedPhotoStr = Base64.encodeToString(byts, Base64.DEFAULT);
        	params.put("head_img", encodedPhotoStr);
        	HttpUtils.post("http://112.126.72.250/ut_app/index.php?m=User&a=upload_head", params, new JsonHttpResponseHandler() {
				public void onStart() {  
                    super.onStart(); 
                    createProgressBarDialog = Utils.createProgressBarDialog(PersonCenterActivity.this, "正在上传头像...");
                    createProgressBarDialog.show();
                    
                } 
                public void onFailure(int statusCode,
                        org.apache.http.Header[] headers,
                        java.lang.Throwable throwable,
                        org.json.JSONObject errorResponse) {  
                	 createProgressBarDialog.dismiss();
                };
                public void onFinish() {
//                	Utils.showToast(PersonCenterActivity.this, "onFinish...");
                };
                public void onSuccess(int statusCode,
                        org.apache.http.Header[] headers,
                        org.json.JSONObject response){ 
                	createProgressBarDialog.dismiss();
                    try {
                    	//接口返回
                    	String state =  response.getString("code");
                    	if(state.equals("1")){
                    		String head_image=response.getJSONObject("list").getString("head_img");
                    		((YoutiApplication)getApplication()).myPreference.setHeadImgPath(head_image);
                    		 headportrait.setImageBitmap(protraitBitmap);
                    		Utils.showToast(PersonCenterActivity.this, "上传成功");
                    	}else{
                    		Utils.showToast(PersonCenterActivity.this, response.getString("上传失败"));
                    	}
                    } catch (Exception e) {
                    	
                    }
                };
            });
        }
    }
    
    private String theLarge;
	private UserCenterBean.UserPhoto up;
	private Dialog createProgressBarDialog2;
    
    private void startTakePhoto(boolean b) {
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
        String fileName = "osc_" + timeStamp + ".jpg";// 照片命名
        File out = new File(savePath, fileName);
        Uri uri = Uri.fromFile(out);
        origUri = uri;

        theLarge = savePath + fileName;// 该照片的绝对路径
        
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        if(!b){
        	 startActivityForResult(intent,
                     ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);           	
        }else{
        	startActivityForResult(intent,
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA1);    
        }
        
    }
	
}
