package com.youti.yonghu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.example.youti_geren.R;
import com.youti.appConfig.YoutiApplication;
import com.youti.view.JazzyViewPager;
import com.youti.view.JazzyViewPager.TransitionEffect;
import com.youti.view.OutlineContainer;

public class WelcomeActivity extends Activity{
	
	private JazzyViewPager mJazzy;
	private Button but_start;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_welcome);
		setupJazziness(TransitionEffect.Tablet);
		
	}
	
	private void setupJazziness(TransitionEffect effect) {
		mJazzy = (JazzyViewPager) findViewById(R.id.jazzy_pager);
		mJazzy.setTransitionEffect(effect);
		mJazzy.setAdapter(new MainAdapter());
		mJazzy.setPageMargin(20);
		mJazzy.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				if(arg0==4){
					
					but_start.setVisibility(View.VISIBLE);
					final ScaleAnimation scaleAnimation=new ScaleAnimation(0.000001f, 1f, 0.000001f, 1f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
			        scaleAnimation.setDuration(1000);
			        scaleAnimation.setStartOffset(500);
			        but_start.startAnimation(scaleAnimation);
					but_start.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent =new Intent(WelcomeActivity.this,MainMainActivity.class);						
							startActivity(intent);
							YoutiApplication.getInstance().myPreference.setIsFirst(false);
							finish();
							
						}
					});
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}
	
	private int [] imageArray = new int []{R.drawable.welcome1,R.drawable.welcome2,R.drawable.welcome3,R.drawable.welcome4,R.drawable.welcome5};
	private class MainAdapter extends PagerAdapter {
		
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			
			View v =View.inflate(WelcomeActivity.this, R.layout.item_welcome, null);			
			ImageView iv =(ImageView) v.findViewById(R.id.iv_welcome);
			iv.setBackgroundResource(imageArray[position]);
			but_start = (Button) v.findViewById(R.id.but_start);
			
			
			container.addView(v);
			mJazzy.setObjectForPosition(v, position);
			return v;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object obj) {
			container.removeView(mJazzy.findViewFromObject(position));
		}
		@Override
		public int getCount() {
			return 5;
		}
		@Override
		public boolean isViewFromObject(View view, Object obj) {
			if (view instanceof OutlineContainer) {
				return ((OutlineContainer) view).getChildAt(0) == obj;
			} else {
				return view == obj;
			}
		}		
	}
}
