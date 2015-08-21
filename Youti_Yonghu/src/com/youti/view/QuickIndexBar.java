package com.youti.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * 快速索引栏
 * @author poplar
 *
 */
public class QuickIndexBar extends View {

	private static final String TAG = "TAG";

	private static final String[] LETTERS = new String[]{
		"A", "B", "C", "D", "E", "F",
		"G", "H", "I", "J", "K", "L",
		"M", "N", "O", "P", "Q", "R",
		"S", "T", "U", "V", "W", "X",
		"Y", "Z"};
	
	private Paint mPaint;

	private int mHeight;

	private int mCellWidth;

	private float mCellHeight;

	private int touchIndex = -1;
	
	public interface OnLetterChangeListener{
		void OnLetterChange(String letter);
	}
	// 字母变化监听
	private OnLetterChangeListener mLetterChangeListener;

	public OnLetterChangeListener getLetterChangeListener() {
		return mLetterChangeListener;
	}

	public void setLetterChangeListener(
			OnLetterChangeListener mLetterChangeListener) {
		this.mLetterChangeListener = mLetterChangeListener;
	}

	public QuickIndexBar(Context context) {
		this(context, null);
	}

	public QuickIndexBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public QuickIndexBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		// 初始化画笔
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.rgb(3,108,255));
		mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, getResources().getDisplayMetrics()));
		mPaint.setTypeface(Typeface.DEFAULT_BOLD);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// 绘制字母
		for (int i = 0; i < LETTERS.length; i++) {
			String text = LETTERS[i];
			// 求x坐标
			int x = (int) (mCellWidth / 2.0f - mPaint.measureText(text) / 2.0f);
			// 求y坐标
			// 格子高度的一半 + 文字高度的一半 + 其上边所有格子高度
			Rect bounds = new Rect();
			mPaint.getTextBounds(text, 0, text.length(), bounds);
			int y = (int) (mCellHeight / 2.0f + bounds.height() / 2.0f + mCellHeight * i);
			
			canvas.drawText(text, x, y, mPaint);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		switch (MotionEventCompat.getActionMasked(event)) {
			case MotionEvent.ACTION_DOWN:
				// 根据y值获取当前触摸到的字母
				float y = event.getY();
				int index = (int) (y / mCellHeight);
				// 如果字母索引发生变化
				if(index != touchIndex){
					if(index >= 0 && index < LETTERS.length){
						Log.d(TAG, LETTERS[index]);
						if(mLetterChangeListener != null){
							// 执行回调
							mLetterChangeListener.OnLetterChange(LETTERS[index]);
						}
					}
					touchIndex  = index;
				}
//				Utils.showToast(getContext(), "index:" + LETTERS[index]);

				break;
			case MotionEvent.ACTION_MOVE:
				// 根据y值获取当前触摸到的字母
				int i = (int) (event.getY() / mCellHeight);
				// 如果字母索引发生变化
				if(i != touchIndex){
					if(i >= 0 && i < LETTERS.length){
						Log.d(TAG, LETTERS[i]);
						if(mLetterChangeListener != null){
							mLetterChangeListener.OnLetterChange(LETTERS[i]);
						}
					}
					touchIndex  = i;
				}
				break;
			case MotionEvent.ACTION_UP:
				// 恢复默认索引值
				touchIndex = -1;
				break;
			default:
				break;
		}
		invalidate();
		return true;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		mHeight = getMeasuredHeight();
		
		mCellHeight = mHeight * 1.0f / LETTERS.length;
		
		mCellWidth = getMeasuredWidth();
		
	}
	
	

}
