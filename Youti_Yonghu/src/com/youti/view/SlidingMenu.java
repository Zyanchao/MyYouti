package com.youti.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.example.youti_geren.R;
import com.nineoldandroids.view.ViewHelper;
import com.youti.utils.ScreenUtils;

/**
 * @author Administrator
 *
 */
public class SlidingMenu extends HorizontalScrollView {

	/**
	 * 屏幕宽度
	 */
	private int mScreenWidth;
	/**
	 * 左边的slidingmenu的右边距
	 */
	private int mMenuRightPadding;
	/**
	 * 右边的slidingmenu的右边距
	 */
	private int mMenuLeftPadding;
	/**
	 * 菜单的宽度
	 */
	private int mMenuWidth;
	//private int mMenuLeftWidth;
	private int mHalfMenuWidth;

	private boolean isNeedScale;// 是否需要缩放

	public boolean isLeftOpen = false, isRightOpen = false;

	private boolean once;

	private ViewGroup mLeftMenu, mRightMenu;
	private ViewGroup mContent;

	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScreenWidth = ScreenUtils.getScreenWidth(context);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.SlidingMenu, defStyle, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.SlidingMenu_rightPadding:
				mMenuRightPadding = a.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50f,
								getResources().getDisplayMetrics()));
				break;
			case R.styleable.SlidingMenu_leftPadding:
				mMenuLeftPadding = a.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50f,
								getResources().getDisplayMetrics()));
				break;
			}
		}
		a.recycle();
	}
	
	public SlidingMenu(Context context) {
		this(context, null, 0);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		/**
		 * 显示的设置一个宽度
		 */
		if (!once) {
			LinearLayout wrapper = (LinearLayout) getChildAt(0);
			mLeftMenu = (ViewGroup) wrapper.getChildAt(0);
			mContent = (ViewGroup) wrapper.getChildAt(1);
			mRightMenu = (ViewGroup) wrapper.getChildAt(2);

			mMenuWidth = mScreenWidth - mMenuRightPadding;
			//mMenuLeftWidth = (int) (mMenuWidth*0.7);
			Log.i("_dbc",mScreenWidth+";"+mMenuRightPadding);
			mLeftMenu.getLayoutParams().width = mMenuWidth;
			mContent.getLayoutParams().width = mScreenWidth;
			mRightMenu.getLayoutParams().width = mMenuWidth;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}
	
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			// 将菜单隐藏
			this.scrollTo(mMenuWidth, 0);
			once = true;
		}
	}
	
private float startX, offsetX, startY, offsetY;
	
	// 滑动距离
	private float xDistance, yDistance, xLast, yLast;
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDistance = yDistance = 0f;
			xLast = ev.getX();
			Log.e("startX1",xLast+"");
			yLast = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float curX = ev.getX();
			final float curY = ev.getY();

			xDistance += Math.abs(curX - xLast);
			yDistance += Math.abs(curY - yLast);
			xLast = curX;
			yLast = curY;

			if (isLeftOpen && xLast > mMenuWidth) {
				return true;
			}else if(isRightOpen && xLast < mScreenWidth - mMenuWidth){
				return true;
			}else if (yDistance > xDistance) {
				return false; // 表示向下传递事件
			}
		case MotionEvent.ACTION_UP:

			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	
	
	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 记录用户按下时x，y的坐标
			startX = ev.getX();
			Log.e("startX",startX+"");
			startY = ev.getY();
			Log.e("startY",startY+"");
			break;
		case MotionEvent.ACTION_MOVE:
			ev.getX();
			break;

			// Up时，进行判断，如果显示区域大于菜单宽度一半则完全显示，否则隐藏
		case MotionEvent.ACTION_UP:
			// 记录用户手指离开时候相差的ֵ
			offsetX = ev.getX() - xLast;
			offsetY = ev.getY() - startY;
			
			/*if(startY<70){
				return false;
			}*/
			Log.e("endX",ev.getX()+"");

			// 手指向右滑动
			if (offsetX > 0) {
				// 位于中间的view 打开左slidingmenu
				if (!isLeftOpen && !isRightOpen) {
					isNeedScale = true;
					this.smoothScrollTo(0, 0);
					isLeftOpen = true;
					isRightOpen = false;
				}
				// 位于右边的slidingmenu
				else if (!isLeftOpen && isRightOpen) {
					this.smoothScrollTo(mMenuWidth, 0);
					isRightOpen = false;
					return true;
				}
				// 位于左边的slidingmenu
				else if (isLeftOpen && !isRightOpen) {

				}
			}
			//手指向左滑动
			else {
				// 位于中间的view
				if (!isLeftOpen && !isRightOpen && yDistance>1) {
					this.smoothScrollTo(2*mMenuWidth, 0);
					isRightOpen = true;
					isLeftOpen = false;
				}
				// 位于右边的slidingmenu
				else if (!isLeftOpen && isRightOpen) {

				}
				// 位于左边的slidingmenu
				else if (isLeftOpen && !isRightOpen) {
					isNeedScale = true;
					this.smoothScrollTo(mMenuWidth, 0);
					isLeftOpen = false;
					return true;
				}
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 打开左边菜单
	 */
	public void openLeftMenu() {
		if (isLeftOpen)
			return;
		this.smoothScrollTo(0, 0);
		isLeftOpen = true;
	}
	
	
	
	/**
	 * �ر���߲˵�
	 */
	public void closeLeftMenu() {
		if (isLeftOpen) {
			this.smoothScrollTo(mMenuWidth, 0);
			isLeftOpen = false;
		}
	}
	
	/**
	 * 打开右边菜单
	 */
	public void openRightMenu() {
		if (isRightOpen)
			return;
		this.smoothScrollTo(2*mMenuWidth, 0);
		isRightOpen = true;
	}
	/**
	 * 关闭右边菜单
	 */
	public void closeRightMenu() {
		if (isRightOpen) {
			this.smoothScrollTo(mMenuWidth, 0);
			isRightOpen = false;
		}
	}

	/**
	 * 关闭左边菜单״̬
	 */
	public void toggleLeft() {
		if (isLeftOpen) {
			closeLeftMenu();
		} else {
			openLeftMenu();
		}
	}
	
	/**
	 * 打开右边菜״̬
	 */
	public void toggleRight() {
		if (isRightOpen) {
			closeRightMenu();
		} else {
			openRightMenu();
		}
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
			
			boolean isLeftNow = l<mMenuWidth;
			boolean isRightNow = l>mMenuWidth;
			int distance = isLeftNow?l:2*mMenuWidth - l;
			
			float scale = distance * 1.0f / mMenuWidth;
			float menuScale = 1f - 0.3f*scale;
			float contentScale = 0.8f + 0.2f*scale;
			
			
			if(isLeftNow){				
				ViewHelper.setScaleX(mLeftMenu, menuScale);
				ViewHelper.setScaleY(mLeftMenu, menuScale);
				ViewHelper.setPivotX(mContent, 0);
				ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
				if (contentScale <= 1) {
					ViewHelper.setScaleX(mContent, contentScale);
					ViewHelper.setScaleY(mContent, contentScale);
				}
			} else if(isRightNow) {
				ViewHelper.setScaleX(mRightMenu, menuScale);
				ViewHelper.setScaleY(mRightMenu, menuScale);
				ViewHelper.setPivotX(mContent, mScreenWidth);
				ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
				if (contentScale <= 1) {
					ViewHelper.setScaleX(mContent, contentScale);
					ViewHelper.setScaleY(mContent, contentScale);
				}				
			}
	}
}
