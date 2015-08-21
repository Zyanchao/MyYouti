package com.youti.yonghu.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;

import com.example.youti_geren.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.Constants;
import com.youti.utils.IBitmapUtils;
import com.youti.utils.Utils;

@SuppressLint("NewApi")
public class PhotoShowActivity extends Activity{
	
	ViewPager vp;
	private ArrayList<String> photo_list;
	private int window_width;
	private int window_height;
	private LinearLayout.LayoutParams params;
	ProgressBar animProgress;
	int p;
	private Bitmap bitmap;
	public final String mPageName ="PhotoShowActivity";
	MyAdapter myAdapter;
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.showphoto);
		/** 获取可見区域高度 **/
		WindowManager manager = getWindowManager();
		window_width = manager.getDefaultDisplay().getWidth();
		window_height = manager.getDefaultDisplay().getHeight();
		if(getIntent()!=null){
			photo_list = getIntent().getStringArrayListExtra("photo_list");
			position = getIntent().getStringExtra("location");
		}
		
		if(TextUtils.isEmpty(position)){
			
		}else{
			p=Integer.parseInt(position);
		}
		//Utils.showToast(PhotoShowActivity.this, position);
		vp=(ViewPager) findViewById(R.id.view_pager);
		animProgress=(ProgressBar) findViewById(R.id.animProgress);
		animProgress.setVisibility(View.GONE);
		myAdapter=new MyAdapter();
		vp.setAdapter(myAdapter);
		vp.setCurrentItem(p);
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}
	
	private Bitmap getBitmapFromUri(Uri uri){
	  try
	  {
	   // 读取uri所在的图片
	   Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
	   return bitmap;
	  }
	  catch (Exception e){
	   e.printStackTrace();
	   return null;
	  }
	 }
	public final int OK=1;
	Handler handler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case OK:
				animProgress.setVisibility(View.GONE);
				if (myAdapter!=null) {
					
					myAdapter.notifyDataSetChanged();
				}
				
				break;

			default:
				break;
			}
		};
	};
	private String position;
	class MyAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return photo_list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(final ViewGroup container, final int position) {
			/*final View v =View.inflate(PhotoShowActivity.this, R.layout.item_imageview_gallary, null);						
			final ImageView iv =(ImageView) v.findViewById(R.id.iv_gallary);
			ImageView iv1=(ImageView) v.findViewById(R.id.iv_photo);
		//	iv.setLayoutParams(params);
			new Thread(new Runnable() {	

				

				@Override
				public void run() {

					params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					if(photo_list.get(p).startsWith("http")){
						
						bitmap = IBitmapUtils.getBitmapFromUrl(photo_list.get(p));
					}else if(photo_list.get(p).startsWith("file")){
						bitmap=BitmapFactory.decodeFile(photo_list.get(p).substring(8,photo_list.get(p).length()));
					}else{
						bitmap=getBitmapFromUri(Uri.parse(photo_list.get(p)));
					}
					int width =bitmap.getWidth();
					int height = bitmap.getHeight();
					
					
					int scall = (int) (((window_width*1.0/width))*height);
					params.height=scall;
					
					
					iv.setLayoutParams(params);
					ImageLoader.getInstance().displayImage(photo_list.get(position), iv);
					
					
					Message msg=Message.obtain();
					msg.what=OK;
					handler.sendMessage(msg);
				}
			}).start();
			iv1.setVisibility(View.GONE);
			container.addView(v);*/
			
			View view =View.inflate(PhotoShowActivity.this, R.layout.item_photoshow, null);
			ImageView iv =(ImageView) view.findViewById(R.id.photodetail);
			if(photo_list.get(position).startsWith("http")){
				
				ImageLoader.getInstance().displayImage(photo_list.get(position), iv);
			}else{
				ImageLoader.getInstance().displayImage(Constants.PIC_CODE+photo_list.get(position), iv);
			}
			container.addView(view);
			return view;
		}
		
	
		
		
	}
	
}