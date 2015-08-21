package com.youti.yonghu.activity;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.umeng.analytics.MobclickAgent;
import com.youti.fragment.CurrentCourseFragment;
import com.youti.fragment.PastCourseFragment;
import com.youti.view.LazyViewPager.OnPageChangeListener;
import com.youti.view.MyLazyViewPager;
import com.youti.view.TitleBar;

public class MyCourseActivity extends FragmentActivity implements OnClickListener{
	TitleBar titleBar;
	TextView tv_first;
	TextView tv_second;
	TextView tv1 ,tv2;
	FrameLayout fl_content;
	MyLazyViewPager view_pager;
	ArrayList<Fragment> list =new ArrayList<Fragment>();
	int screenWidth;
	View indicate_line;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_mycourse);
		
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
		titleBar.setTitleBarTitle(getResources().getString(R.string.user_course));
		titleBar.setSearchGone();
		titleBar.setShareGone(false);
		tv_first.setText("当前课程");
		tv_second.setText("历史课程");
		CurrentCourseFragment ccf =new CurrentCourseFragment();
		PastCourseFragment pcf =new PastCourseFragment();
		list.add(ccf);
		list.add(pcf);
		
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
		view_pager=(MyLazyViewPager) findViewById(R.id.view_pager);
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
}
