package com.youti.yonghu.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.umeng.analytics.MobclickAgent;
import com.youti.fragment.CommentFragment;
import com.youti.fragment.CurrentOrderFragment;
import com.youti.fragment.OrderLessonFragment;
import com.youti.view.LazyViewPager;
import com.youti.view.LazyViewPager.OnPageChangeListener;
import com.youti.view.MyLazyViewPager;
import com.youti.view.TitleBar;

public class MyCoachActivity extends FragmentActivity implements OnClickListener{
	TextView tv_first,tv_second,tv_third;
	MyLazyViewPager view_pager;
	ArrayList<Fragment> list =new ArrayList<Fragment>();
	TitleBar titleBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_mycoach);
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
		tv_third.setScaleX(0.8f);
		tv_third.setScaleY(0.8f);
		tv_first.setText("订单");
		tv_second.setText("约课");
		tv_third.setText("评论");
		titleBar.setTitleBarTitle("我的教练");
		titleBar.setSearchGone();
		titleBar.setShareGone(false);
		CurrentOrderFragment cof =new CurrentOrderFragment();
		OrderLessonFragment of=new OrderLessonFragment();
		CommentFragment cf =new CommentFragment();
		
		list.add(cof);
		list.add(of);
		list.add(cf);
		
		view_pager.setAdapter(new MyAdapter(getSupportFragmentManager()));
		
	}

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
		view_pager= (MyLazyViewPager) findViewById(R.id.view_pager);
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
