package com.youti.yonghu.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.umeng.analytics.MobclickAgent;
import com.youti.fragment.DownLoadedFragment;
import com.youti.fragment.DownLoadingFragment;
import com.youti.view.TitleBar;
import com.youti.yonghu.download.DownloadMovieItem;

public class MyDownloadActivity extends FragmentActivity implements OnClickListener {
	TitleBar titleBar;
	TextView tv_first;
	TextView tv_second;
	TextView tv1 ,tv2;
	FrameLayout fl_content;
	ViewPager view_pager;
	ArrayList<Fragment> list =new ArrayList<Fragment>();
	int screenWidth;
	View indicate_line;
	List<DownloadMovieItem> moviewItemList;
	
	public List<DownloadMovieItem> getMoviewItemList() {
		return moviewItemList;
	}
	public void setMoviewItemList(List<DownloadMovieItem> moviewItemList) {
		this.moviewItemList = moviewItemList;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_community);
		
		initView();
		initListener();
		initData();
	
	}
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
	private void initData() {
		titleBar.setTitleBarTitle(getResources().getString(R.string.user_download));
		titleBar.setSearchGone();
		tv_first.setText("下载中");
		tv_second.setText("已下载");
		DownLoadingFragment dlingf =new DownLoadingFragment();
		DownLoadedFragment dledf=new DownLoadedFragment();
		list.add(dlingf);
		list.add(dledf);
		
		view_pager.setAdapter(new MyAdapter(getSupportFragmentManager()));
		
		
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
	private void initView() {
		titleBar =(TitleBar) findViewById(R.id.index_titlebar);
		tv_second=(TextView) findViewById(R.id.tv_second);
		tv_first=(TextView) findViewById(R.id.tv_first);
		view_pager=(ViewPager) findViewById(R.id.view_pager);
		fl_content= (FrameLayout) findViewById(R.id.fl_content);
		indicate_line =findViewById(R.id.indicate_line);
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
	//下载fragment，当某个条目下载完成后，调用该方法
	

	
	
}
