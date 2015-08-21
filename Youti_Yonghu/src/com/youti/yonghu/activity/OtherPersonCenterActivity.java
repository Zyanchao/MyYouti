package com.youti.yonghu.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.YoutiApplication;
import com.youti.fragment.OtherPersonCenterCoachFragment;
import com.youti.fragment.OtherPersonCenterCourseFragment;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
import com.youti.view.CircleImageView;
import com.youti.view.HorizontalListView;
import com.youti.yonghu.bean.UserCenterBean;
import com.youti.yonghu.bean.UserCenterBean.UserPhoto;
public class OtherPersonCenterActivity extends FragmentActivity implements OnClickListener{
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
	public String user_id;
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
		
		if(getIntent()!=null){
			user_id=getIntent().getStringExtra("user_id");
		}else{
			user_id=((YoutiApplication)getApplication()).myPreference.getUserId();
		}
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
		OtherPersonCenterCourseFragment pccf =new OtherPersonCenterCourseFragment();
		OtherPersonCenterCoachFragment pccf2=new OtherPersonCenterCoachFragment();
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
					Utils.showToast(OtherPersonCenterActivity.this, "连接失败");
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
				
					//查看相片
					
//						Utils.showToast(PersonCenterActivity.this, photoList.size()+"");
						Intent intent =new Intent(OtherPersonCenterActivity.this,PhotoShowActivity.class);
						intent.putStringArrayListExtra("photo_list", photoList1);
						intent.putExtra("location", position+"");
						startActivity(intent);
		
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
			
				return list.size();
			
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
			View v =View.inflate(OtherPersonCenterActivity.this, R.layout.item_xiangce, null);
			ImageView iv=(ImageView) v.findViewById(R.id.iv_photo);
			ImageLoader.getInstance().displayImage(list.get(position).photo_url, iv);
				
			
			
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
				
				ImageLoader.getInstance().displayImage("http://112.126.72.250/ut_app"+userCenterBean.list.user_info.head_img, headportrait);
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


	
	
	
	
	
}
