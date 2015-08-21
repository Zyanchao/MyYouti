package com.youti.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 禁止viewpager滑动
 * 
 * @author Administrator
 * 
 */
public class MyLazyViewPager extends LazyViewPager {

	private boolean HAS_TOUCH_MODE = false;

	public MyLazyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyLazyViewPager(Context context) {
		super(context);
	}
	
	//向内部控件传递
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}

	//哪怕回传，也不响应
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}
	
}
