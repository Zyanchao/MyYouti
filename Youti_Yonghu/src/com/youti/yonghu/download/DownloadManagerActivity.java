/**   
* @Title: DownloadManagerActivity.java
* @Package com.cloud.coupon.ui
* @Description: TODO(用一句话描述该文件做什么)
* @author 陈红建
* @date 2013-7-3 下午4:36:59
* @version V1.0
*/ 
package com.youti.yonghu.download;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.YoutiApplication;
import com.youti.fragment.DownLoadedFragment;
import com.youti.fragment.DownLoadingFragment;
import com.youti.view.TitleBar;

/** 
 * @ClassName: DownloadManagerActivity
 * @Description: 下载管理界面
 * @author 陈红建
 * @date 2013-7-3 下午4:36:59
 * 
 */
public class DownloadManagerActivity extends BaseActivity implements android.view.View.OnClickListener{
	TitleBar titleBar;
	TextView tv_first;
	TextView tv_second;
	TextView tv1 ,tv2;
	FrameLayout fl_content;
	ViewPager view_pager;
	ArrayList<Fragment> list =new ArrayList<Fragment>();
	int screenWidth;
	View indicate_line;
	String downloadUrl;
	List<DownloadMovieItem> listItem ;
	List<DownloadMovieItem> removeList;
	DownLoadedFragment downedFragment;
	YoutiApplication youtiApplication;
	private SQLiteDatabase db;
	int a ;
	  @Override
			protected void onPause() {
				super.onPause();
				MobclickAgent.onPause(this);
			}

			@Override
			protected void onResume() {
				super.onResume();
				MobclickAgent.onResume(this);
			}
	/**
	 * 当下载完成后，发送一个广播，这里接收这个广播。接受到广播后，通过这个广播，我们可以得到传递过来的downloadUrl
	 * 通过这个唯一的下载地址，分别在下载成功集合listItem中添加这个下载对象，在原有的下载中集合removeList中移除这个对象。
	 */
	BroadcastReceiver downReceiver =new BroadcastReceiver(){	

		@Override
		public void onReceive(Context context, Intent intent) {
			/*if(intent!=null){
				downloadUrl = intent.getStringExtra("downloadUrl");
			}			
			Utils.showToast(DownloadManagerActivity.this, downloadUrl);	
			DownloadMovieItem item =getDownItem(downloadUrl);
			listItem=youtiApplication.getDownloadedItems();
			listItem.add(item);
			downedFragment=(DownLoadedFragment) ma.getItem(1);
			
			removeList =youtiApplication.getDownloadItems();
			System.out.println("我在broadcast里面，下载成功"+removeList.toString());
			for(int i=0;i<removeList.size();i++){
				if(removeList.get(i).getDownloadUrl().equals(downloadUrl)){
					a=i;
				}
			}
			removeList.remove(a);
			
			
			youtiApplication.setDownloadedItems(listItem);
			youtiApplication.setDownloadItems(removeList);
			System.out.println("我在broadcast里面，下载成功"+downloadUrl);
			System.out.println("我在broadcast里面，下载成功"+listItem.toString());*/
		}
		
	
	};
	private MyAdapter ma;
	
	
	/**
	 * 根据下载状态查询数据库
	 */
	public DownloadMovieItem getDownItem(int downloadState){
		DownloadMovieItem downItem =new DownloadMovieItem();
		String dbpath = getBaseContext().getDatabasePath("coupon.db").getAbsolutePath();
		
		db = SQLiteDatabase.openDatabase(dbpath, null, 0);
		String sql ="select * from "+AgentConstant.DOWNLOADTASK_TABLE+" where downloadState=?";
		String[] selectionArgs=new String[] {""+downloadState};
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		if(cursor.moveToNext()){
			String fileSize =cursor.getString(cursor.getColumnIndex("fileSize"));
			String movieName=cursor.getString(cursor.getColumnIndex("movieName"));
			String filePath=cursor.getString(cursor.getColumnIndex("filePath"));
			String movieHeadImagePath=cursor.getString(cursor.getColumnIndex("movieHeadImagePath"));
			System.out.println(fileSize+movieName+filePath+movieHeadImagePath);
			downItem.setFilePath(filePath);
			downItem.setMovieHeadImagePath(movieHeadImagePath);
			downItem.setFileSize(fileSize);
			downItem.setMovieName(movieName);
		}
		cursor.close();
		db.close();
		return downItem;
	}
	/**
	 * 根据广播传递回来的downloadUrl，查询下载好的视频，然后将其封装为downLoadMovieITEm对象
	 * @param video_url
	 * @return
	 */
	public DownloadMovieItem getDownItem(String video_url){
		String dbpath = getBaseContext().getDatabasePath("coupon.db").getAbsolutePath();
		System.out.println(video_url);
		db = SQLiteDatabase.openDatabase(dbpath, null, 0);
		DownloadMovieItem downItem =new DownloadMovieItem();
		String sql ="select * from "+AgentConstant.DOWNLOADTASK_TABLE+" where downloadUrl=?";
		String[] selectionArgs=new String[] {video_url};
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		System.out.println(cursor.toString());
		if(cursor.moveToNext()){
			String fileSize =cursor.getString(cursor.getColumnIndex("fileSize"));
			String movieName=cursor.getString(cursor.getColumnIndex("movieName"));
			String filePath=cursor.getString(cursor.getColumnIndex("filePath"));
			String movieHeadImagePath=cursor.getString(cursor.getColumnIndex("movieHeadImagePath"));
			System.out.println(fileSize+movieName+filePath+movieHeadImagePath);
			downItem.setFilePath(filePath);
			downItem.setMovieHeadImagePath(movieHeadImagePath);
			downItem.setFileSize("下载完成");
			downItem.setMovieName(movieName);
			
		}
		cursor.close();
		db.close();
		return downItem;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download_manager);
		youtiApplication=(YoutiApplication) getApplication();
		
		/*IntentFilter filter=new IntentFilter();
		filter.addAction("com.youti_yonghu.download");
		registerReceiver(downReceiver, filter);
		System.out.println("注册广播接受者");*/
		
		
		setmContext(DownloadManagerActivity.this);
		initView();
		initListener();
		initData();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//如果这里给注销的话，那么当用户离开该页面，该页面的广播接收者就会被注销掉，就无法实现下载完成的更新
		/*if(downReceiver!=null){
			unregisterReceiver(downReceiver);
		}*/
	}
	
	private void initData() {
		titleBar.setTitleBarTitle(getResources().getString(R.string.user_download));
		titleBar.setSearchGone();
		titleBar.setShareGone(false);
		tv_first.setText("下载中");
		tv_second.setText("已下载");
		DownLoadingFragment dlingf =new DownLoadingFragment();
		DownLoadedFragment dledf=new DownLoadedFragment();
		list.add(dlingf);
		list.add(dledf);
		if(ma==null){
			
			ma = new MyAdapter(getSupportFragmentManager());
			view_pager.setAdapter(ma);
		}else{
			ma.notifyDataSetChanged();
		}
		
	}

	
	
	private void initListener() {
		tv_second.setOnClickListener(this);
		tv_first.setOnClickListener(this);
		
		
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
	}

	@Override
	public void initView(){
		super.initView();
		titleBar =(TitleBar) findViewById(R.id.index_titlebar);
		titleBar.setSearchGone();
		titleBar.setTitleBarTitle("我的下载");
		
	//	listView = (ListView) findViewById(R.id.download_listview);
		
		
		titleBar =(TitleBar) findViewById(R.id.index_titlebar);
		tv_second=(TextView) findViewById(R.id.tv_second);
		tv_first=(TextView) findViewById(R.id.tv_first);
		view_pager=(ViewPager) findViewById(R.id.view_pager);
		fl_content= (FrameLayout) findViewById(R.id.fl_content);
		indicate_line =findViewById(R.id.indicate_line);
		
		
		
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
		
		private void lightAndScaleTabTitle(){
			int currentPage = view_pager.getCurrentItem();
			
			tv_first.setTextColor(currentPage==0?Color.parseColor("#6049a1")
					:Color.parseColor("#666666"));
			tv_second.setTextColor(currentPage==1?Color.parseColor("#6049a1")
					:Color.parseColor("#666666"));
			
			ViewPropertyAnimator.animate(tv_first).scaleX(currentPage==0?1f:0.8f).setDuration(200);
			ViewPropertyAnimator.animate(tv_first).scaleY(currentPage==0?1f:0.8f).setDuration(200);
			ViewPropertyAnimator.animate(tv_second).scaleX(currentPage==1?1f:0.8f).setDuration(200);
			ViewPropertyAnimator.animate(tv_second).scaleY(currentPage==1?1f:0.8f).setDuration(200);
		}
	/** (非 Javadoc) 
	* Title: onClick
	* Description:
	* @param view
	* @see com.cloud.coupon.ui.BaseActivity#onClick(android.view.View)
	* 
	*/ 
	@Override
	public void onClick(View view){
		super.onClick(view);
		switch (view.getId()){
		case R.id.tv_second:
			view_pager.setCurrentItem(1);
			
			break;
		case R.id.tv_first:
		
			view_pager.setCurrentItem(0);
			break;
		
		
		}
	}
	/** 
	 * @Title: clearDownlodItem
	 * @Description: 删除下载的条目 
	 * @param 
	 * @return void
	 * @author 陈红建
	 * @throws 
	 */
	
}


