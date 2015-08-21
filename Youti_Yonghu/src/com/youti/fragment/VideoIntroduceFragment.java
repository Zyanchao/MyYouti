package com.youti.fragment;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youti_geren.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;
import com.youti.utils.ScreenUtils;
import com.youti.utils.Utils;
import com.youti.view.CircleImageView1;
import com.youti.yonghu.activity.CourseDetailActivity;
import com.youti.yonghu.activity.VideoDetailActivity;
import com.youti.yonghu.bean.VideoDetailBean.CloseCourse;
import com.youti.yonghu.bean.VideoDetailBean.VideoContent;
import com.youti.yonghu.bean.VideoDetailBean.VideoDetailItem;
import com.youti.yonghu.download.AgentConstant;
import com.youti.yonghu.download.ContentValue;
import com.youti.yonghu.download.DownloadManagerActivity;
import com.youti.yonghu.download.DownloadMovieItem;

public class VideoIntroduceFragment extends Fragment implements ContentValue{
	private View view;
	ImageView contentControl;
	CircleImageView1 spxq_head;
	boolean isShow;
	TextView kc,spxq_coach_name,jlcontent,kccontent;
	ListView lv_contents,lv_course;
	ScrollView sc;
	LinearLayout ll;
	int height;
	private VideoDetailItem videoDetailItemList;
	private List<VideoContent> videoContentList;
	private List<CloseCourse> closeCourseList;
	private DisplayImageOptions options;
	SharedPreferences sp;
	private BroadcastReceiver br =new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			
		}
		
	};
	public final String mPageName = "VideoIntroduceFragment";
	 @Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			MobclickAgent.onPageEnd( mPageName );
		}


		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			MobclickAgent.onPageStart( mPageName );	
		}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(getActivity(), R.layout.fragment_videointroduce, null);
		kc=(TextView) view.findViewById(R.id.kccontent);
		contentControl = (ImageView) view.findViewById(R.id.contentShowControl);
		lv_contents =(ListView) view.findViewById(R.id.lv_contents);
		lv_course=(ListView) view.findViewById(R.id.lv_course);
		sc=(ScrollView) view.findViewById(R.id.sc);
		ll=(LinearLayout) view.findViewById(R.id.ll);
		spxq_head=(CircleImageView1) view.findViewById(R.id.spxq_head);
		spxq_coach_name=(TextView) view.findViewById(R.id.spxq_coach_name);
		jlcontent=(TextView) view.findViewById(R.id.jlcontent);
		kccontent=(TextView) view.findViewById(R.id.kccontent);
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()  
        .cacheInMemory().cacheOnDisc().build();  

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(  
        getActivity()).defaultDisplayImageOptions(defaultOptions)  
        .threadPriority(Thread.NORM_PRIORITY - 2)  
        .denyCacheImageMultipleSizesInMemory()  
        .discCacheFileNameGenerator(new Md5FileNameGenerator())  
        .tasksProcessingOrder(QueueProcessingType.LIFO).build();  
		ImageLoader.getInstance().init(config);  
		
		options = new DisplayImageOptions.Builder()    
        .showStubImage(R.drawable.sq_head)          // 设置图片下载期间显示的图片    
        .showImageForEmptyUri(R.drawable.empty_photo)  // 设置图片Uri为空或是错误的时候显示的图片    
        .showImageOnFail(R.drawable.empty_photo)       // 设置图片加载或解码过程中发生错误显示的图片        
        .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中    
        .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中    
        .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片    
        .build();
		
		contentControl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isShow) {
					isShow = true;
					kc.setSingleLine(false);
					kc.setEllipsize(null);
					contentControl.setBackgroundResource(R.drawable.spxq_arrow_h);
				} else {
					isShow = false;
					kc.setSingleLine(true);
					kc.setEllipsize(TruncateAt.END);
					contentControl.setBackgroundResource(R.drawable.spxq_arrow);
				}
			}
		});
		return view;
	}
	
	
	public void smoothScrollTo(){
		sc.smoothScrollTo(0, height);
		lv_contents.setFocusable(false);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(br!=null){
			getActivity().unregisterReceiver(br);
		}
	}
	
	public Context context;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		IntentFilter filter =new IntentFilter("com.youti_yonghu.download");
		getActivity().registerReceiver(br, filter);
		
		
		videoDetailItemList = ((VideoDetailActivity)getActivity()).getVideoDetailItemList();
		System.out.println(videoDetailItemList.toString());
		videoContentList = videoDetailItemList.videolist;
		closeCourseList =videoDetailItemList.course;
		/**
		 * 判断目录列表所在集合是否为null
		 */
		if(videoContentList!=null){
			lv_contents.setAdapter(new MyAdapter());			
		}
		
		if(closeCourseList!=null){
			lv_course.setAdapter(new MyCloseAdapter());
		}
		//设置教练姓名
		spxq_coach_name.setText(videoDetailItemList.coach_name);
		//设置教练头像
		ImageLoader.getInstance().displayImage(videoDetailItemList.coach_img, spxq_head, options);
		//设置 教练简介
		jlcontent.setText(videoDetailItemList.coach_introduce);
		//设置课程简介
		kccontent.setText(videoDetailItemList.course_introduce);
		
		ScreenUtils.setListViewHeightBasedOnChildren(lv_contents);
		
		//ScreenUtils.setListViewHeightBasedOnChildren(lv_course);
		
		sc.smoothScrollTo(0, height);
		height=ll.getTop();
		/**
		 * 点击开始下载
		 */
		context = getActivity();

		/**
		 * 点击进入课程详情
		 */
		lv_course.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Utils.showToast(getActivity(), "点击进入课程详情");
				Intent i = new Intent(getActivity(), CourseDetailActivity.class);
				i.putExtra("course_id", closeCourseList.get(position).id);
				getActivity().startActivity(i);
			}
		});
		
		
		
	
	}
	/**
	 * 最近的线下课程适配器
	 * @author xiaguangcheng
	 *
	 */
	class MyCloseAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return closeCourseList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v=View.inflate(getActivity(), R.layout.item_closecourse, null);
			ImageView iv =(ImageView) v.findViewById(R.id.spxq_zjkc_item_img);
			TextView tv_title =(TextView) v.findViewById(R.id.spxq_zj_title);
			TextView tv_introduce=(TextView) v.findViewById(R.id.spxq_zj_des);
			ImageLoader.getInstance().displayImage(closeCourseList.get(position).img, iv, options);
			tv_title.setText(closeCourseList.get(position).title);
			tv_introduce.setText(closeCourseList.get(position).introduce);
			return v;
		}
		
	}
	/**
	 * 课程视频目录适配器
	 * @author xiaguangcheng
	 *
	 */
	private RelativeLayout rl;
	/**
	 *根据视频url地址，查询数据库该视频的状态
	 * @author xiaguangcheng
	 *
	 */
	SQLiteDatabase db;
	public String findState(String url){
		String state="";
		String dbpath = getActivity().getBaseContext().getDatabasePath("coupon.db").getAbsolutePath();
		System.out.println(url);
		db = SQLiteDatabase.openDatabase(dbpath, null, 0);
		String sql ="select * from "+AgentConstant.DOWNLOADTASK_TABLE+" where downloadUrl = ? ";
		String[] selectionArgs=new String[] {url};
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		System.out.println(cursor.toString());
		if(cursor.moveToNext()){
			state =cursor.getString(cursor.getColumnIndex("downloadState"));			
		}
		cursor.close();
		db.close();
		return state;
	}
	public boolean findState(String url,String name){
		boolean state=false;
		String dbpath = getActivity().getBaseContext().getDatabasePath("coupon.db").getAbsolutePath();
		System.out.println(url);
		db = SQLiteDatabase.openDatabase(dbpath, null, 0);
		String sql ="select * from "+name+" where downloadUrl = ? ";
		String[] selectionArgs=new String[] {url};
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		System.out.println(cursor.toString());
		if(cursor.moveToNext()){
			state = true;			
		}
		cursor.close();
		db.close();
		return state;
	}
	class MyAdapter extends BaseAdapter{

		

		@Override
		public int getCount() {
			return videoContentList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v;
			TextView tv;
			if(convertView==null){
				v =View.inflate(getActivity(), R.layout.item_contents, null);
			}else{
				v=convertView;
			}	
			
			rl = (RelativeLayout) v.findViewById(R.id.rl);
			rl.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					/**
					 * 点击之前先查询数据库，该地址的视频是否被下载过，如果被下载过并且保存在数据库中，那么久提示用户，已经下载。
					 */
					/*if("2".equals(findState(videoContentList.get(position).video_url))){
						Utils.showToast(getActivity(), "当前任务正在下载，请稍后");
						return;
					}else if(findState(videoContentList.get(position).video_url,"videourl")){
						Utils.showToast(getActivity(), "当前任务下载完成，请移步到我的下载中观看");
						return;
					}else{*/
					
						sp=context.getSharedPreferences("config", context.MODE_PRIVATE);
						Editor editor =sp.edit();
						if(sp.getBoolean(videoContentList.get(position).video_url, false)){
							Utils.showToast(getActivity(), "当前任务正在下载，或者下载完成，请勿重复下载");
							return;
						}
						editor.putBoolean(videoContentList.get(position).video_url, true);
						editor.commit();
						
						Toast.makeText(getActivity(), videoContentList.get(position).video_url, 0).show();
						Intent i = ((VideoDetailActivity)getActivity()).getServerIntent();
						DownloadMovieItem d = new DownloadMovieItem();
						d.setDownloadUrl(videoContentList.get(position).video_url);
						d.setFileSize("10M"); // 电影大小
						d.setMovieName(videoContentList.get(position).title); // 电影名称,不包括后缀名.后缀名在下载服务指定为MP4
						d.setMovieHeadImagePath(videoContentList.get(position).coach_img);//设置视频图片
						d.setDownloadState(DOWNLOAD_STATE_WATTING);// 设置默认的下载状态为等待状态
						i.putExtra(SERVICE_TYPE_NAME, START_DOWNLOAD_MOVIE);
						i.putExtra(DOWNLOAD_TAG_BY_INTENT, d);
						getActivity().startService(i);
						//Utils.showToast(getActivity(), findState(videoContentList.get(position).video_url));
						//v.setClickable(false);
						//((TextView)(v.findViewById(R.id.tv_contents))).setText("正在下载...");
						
						
						Intent intent =new Intent(getActivity(),DownloadManagerActivity.class);
						startActivity(intent);
					}
				//}
			});
			
			
			
			tv=(TextView) v.findViewById(R.id.tv_contents);
			tv.setText(videoContentList.get(position).title);
			
			return v;
		}
		
	}
	
	
	
}
