package com.youti.view;


import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

public class CustomViewPager extends ViewPager {
	private GestureDetector mGestureDetector;
	View.OnTouchListener mGestureListener;
	private boolean canScroll = true;
	public CustomViewPager(Context context) {
		super(context);
	}

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		if (v instanceof HorizontalScrollView) {
			return true;
		}
		return super.canScroll(v, checkV, dx, x, y);
	}

	/** 触摸时按下的点 **/
	PointF downP = new PointF();
	/** 触摸时当前的点 **/
	PointF curP = new PointF();
	OnSingleTouchListener onSingleTouchListener;


	
	
	
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean ret = super.dispatchTouchEvent(ev);
        if(ret) 
        {
          requestDisallowInterceptTouchEvent(true);
        }
        return ret;
	}
	
	
	/**
	 * 动态计算 子view 高度
	 */
	
	 @Override
	  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

	    int height = 0;
	    //下面遍历所有child的高度
	    for (int i = 0; i < getChildCount(); i++) {
	      View child = getChildAt(i);
	      child.measure(widthMeasureSpec,
	          MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	      int h = child.getMeasuredHeight();
	      if (h > height) //采用最大的view的高度。
	        height = h;
	    }

	    heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
	        MeasureSpec.EXACTLY);

	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	  }
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		// 每次进行onTouch事件都记录当前的按下的坐标
		curP.x = arg0.getX();
		curP.y = arg0.getY();

		if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
			// 记录按下时候的坐标
			// 切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
			downP.x = arg0.getX();
			downP.y = arg0.getY();
			// 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
			getParent().requestDisallowInterceptTouchEvent(true);
		}

		if (arg0.getAction() == MotionEvent.ACTION_MOVE) {
			// 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
			getParent().requestDisallowInterceptTouchEvent(true);
		}

		if (arg0.getAction() == MotionEvent.ACTION_UP) {
			// 在up时判断是否按下和松手的坐标为一个点
			// 如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick
			if (downP.x == curP.x && downP.y == curP.y) {
				onSingleTouch();
				return true;
			}
		}

		return super.onTouchEvent(arg0);
	}

	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_UP)
			canScroll = false;
		return canScroll;
	}

	class YScrollDetector extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (canScroll)
				if (Math.abs(distanceY) >= Math.abs(distanceX))
					canScroll = false;
				else
					canScroll = true;
			return canScroll;
		}
	}
	
	/**
	 * 单击
	 */
	public void onSingleTouch() {
		if (onSingleTouchListener != null) {

			onSingleTouchListener.onSingleTouch();
		}
	}

	/**
	 * 创建点击事件接口
	 * 
	 * @author wanpg
	 * 
	 */
	public interface OnSingleTouchListener {
		public void onSingleTouch();
	}

	public void setOnSingleTouchListener(
			OnSingleTouchListener onSingleTouchListener) {
		this.onSingleTouchListener = onSingleTouchListener;
	}

}
