package com.youti.yonghu.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.YoutiApplication;
import com.youti.base.BaseActivity;
import com.youti.fragment.MyFavoriateCoachFragment;
import com.youti.fragment.MyFavoriateCourseFragment;
import com.youti.fragment.MyFavoriateVideoFragment;
import com.youti.view.LazyViewPager;
import com.youti.view.LazyViewPager.OnPageChangeListener;
import com.youti.view.TitleBar;
import com.youti.yonghu.bean.FavoriateCoachBean.CoachItemBean;
import com.youti.yonghu.bean.FavoriateCourseBean.CourseItemBean;
import com.youti.yonghu.bean.FavoriateVideoBean.VideoItemBean;

public class MyFavoriateActivity extends BaseActivity implements OnClickListener{
	protected static final int COURSESUCCESS = 1;
	protected static final int COACHSUCCESS = 2;
	protected static final int VIDEOSUCCESS = 3;
	TextView tv_first,tv_second,tv_third;
	LazyViewPager view_pager;
	ArrayList<Fragment> list =new ArrayList<Fragment>();
	TitleBar titleBar;
	private String userId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_myfavoriteactivity);
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
		userId = ((YoutiApplication)getApplication()).myPreference.getUserId();
	
		
		tv_first.setScaleX(0.8f);
		tv_first.setScaleY(0.8f);
		
		tv_second.setScaleX(0.8f);
		tv_second.setScaleY(0.8f);
		
		tv_third.setScaleX(0.8f);
		tv_third.setScaleY(0.8f);
		
		titleBar.setTitleBarTitle("我的喜欢");
		titleBar.setSearchGone();
		titleBar.setShareGone(false);
		
		mfcof=new MyFavoriateCourseFragment();
		mfcf=new MyFavoriateCoachFragment();
		mfvf=new MyFavoriateVideoFragment();
		list.add(mfcof);
		list.add(mfcf);
		list.add(mfvf);
		myAdapter=new MyAdapter(getSupportFragmentManager());
		view_pager.setAdapter(myAdapter);
		
		
		
	}
	MyAdapter myAdapter;
	public MyAdapter getMyAdapter() {
		return myAdapter;
	}
	public void setMyAdapter(MyAdapter myAdapter) {
		this.myAdapter = myAdapter;
	}
	List<CourseItemBean> listCourse;
	List<CoachItemBean> listCoach;
	List<VideoItemBean> listVideo;
	public List<CourseItemBean> getListCourse() {
		return listCourse;
	}

	public void setListCourse(List<CourseItemBean> listCourse) {
		this.listCourse = listCourse;
	}

	public List<CoachItemBean> getListCoach() {
		return listCoach;
	}

	public void setListCoach(List<CoachItemBean> listCoach) {
		this.listCoach = listCoach;
	}

	public List<VideoItemBean> getListVideo() {
		return listVideo;
	}

	public void setListVideo(List<VideoItemBean> listVideo) {
		this.listVideo = listVideo;
	}
	
	private MyFavoriateCoachFragment mfcf;
	private MyFavoriateCourseFragment mfcof;
	private MyFavoriateVideoFragment mfvf;
	
	

	private void initListener() {
		tv_first.setOnClickListener(this);
		tv_second.setOnClickListener(this);
		tv_third.setOnClickListener(this);
		
		
		view_pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				lightAndScaleTabTitle();				
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
		tv_first=(TextView) findViewById(R.id.tv_first);
		tv_second=(TextView) findViewById(R.id.tv_second);
		tv_third=(TextView) findViewById(R.id.tv_third);
		view_pager= (LazyViewPager) findViewById(R.id.view_pager);
		titleBar =(TitleBar) findViewById(R.id.index_titlebar);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_first:
			view_pager.setCurrentItem(0);
			break;
		case R.id.tv_second:
			view_pager.setCurrentItem(1);
			break;
		case R.id.tv_third:
			view_pager.setCurrentItem(2);
			break;
		default:
			break;
		}
	}
	
	class MyAdapter extends FragmentStatePagerAdapter{

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
		
		@Override
		public int getItemPosition(Object object) {
			return PagerAdapter.POSITION_NONE;
		}
		
	}
	private void lightAndScaleTabTitle(){
		int currentPage = view_pager.getCurrentItem();
		
		tv_first.setTextColor(currentPage==0?Color.parseColor("#6049a1")
				:Color.parseColor("#666666"));
		tv_second.setTextColor(currentPage==1?Color.parseColor("#6049a1")
				:Color.parseColor("#666666"));
		tv_third.setTextColor(currentPage==2?Color.parseColor("#6049a1")
				:Color.parseColor("#666666"));
		
		ViewPropertyAnimator.animate(tv_first).scaleX(currentPage==0?1f:0.8f).setDuration(200);
		ViewPropertyAnimator.animate(tv_first).scaleY(currentPage==0?1f:0.8f).setDuration(200);
		ViewPropertyAnimator.animate(tv_second).scaleX(currentPage==1?1f:0.8f).setDuration(200);
		ViewPropertyAnimator.animate(tv_second).scaleY(currentPage==1?1f:0.8f).setDuration(200);
		ViewPropertyAnimator.animate(tv_third).scaleX(currentPage==2?1f:0.8f).setDuration(200);
		ViewPropertyAnimator.animate(tv_third).scaleY(currentPage==2?1f:0.8f).setDuration(200);
	}
	
}
