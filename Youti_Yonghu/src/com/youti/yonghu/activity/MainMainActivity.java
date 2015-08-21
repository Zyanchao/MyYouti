package com.youti.yonghu.activity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.Toast;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMConversation.EMConversationType;
import com.example.youti_geren.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.umeng.analytics.MobclickAgent;
import com.youti.applib.controller.HXSDKHelper;
import com.youti.fragment.ContentFragment;
import com.youti.fragment.LeftMenuFragment;
import com.youti.fragment.RightMenuFragment;

public class MainMainActivity extends SlidingFragmentActivity  {

	private Fragment mContent;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_mainmainactivity);

//		if (findViewById(R.id.menu_frame) == null) {
			setBehindContentView(R.layout.layout_leftmenuframe);
			getSlidingMenu().setSlidingEnabled(true);
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//		} else {
//			View v = new View(this);
//			setBehindContentView(v);
//			getSlidingMenu().setSlidingEnabled(false);
//			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
//		}

//		if (savedInstanceState != null) {
//			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
//		}
//
//		if (mContent == null) {
			mContent = new ContentFragment();
//		}
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mContent).commit();
		
		getSupportFragmentManager().beginTransaction().replace(R.id.left_menu_frame, new LeftMenuFragment()).commit();
		
		sm = getSlidingMenu();
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		
		sm.setFadeEnabled(false);
		sm.setBehindScrollScale(1f);
		sm.setFadeDegree(0.5f);
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		sm.setBackgroundImage(R.drawable.bk_bg);
		sm.setSecondaryMenu(R.layout.layout_rightmenuframe);
		
		getSupportFragmentManager().beginTransaction().replace(R.id.right_menu_frame, new RightMenuFragment()).commit();
		  
	 
		sm.setBehindCanvasTransformer(new SlidingMenu.CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float) (percentOpen * 0.25 + 0.75);
				canvas.scale(scale, scale, -canvas.getWidth()/2 ,
						canvas.getHeight() / 2);
			}
		});

		sm.setAboveCanvasTransformer(new SlidingMenu.CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float) (1 - percentOpen * 0.25);
				canvas.scale(scale, scale, canvas.getWidth()/2, canvas.getHeight() / 2);
			}
		});

	}
	@Override
	protected void onResume() {
		super.onResume();
		
		if (sm.isMenuShowing()) {
			sm.showContent(true);
		} 
		
		MobclickAgent.onResume(this);
	}
	
	public final String mPageName="MainMainActivity";
	@Override
	protected void onPause() {
		super.onPause();
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				if (sm.isMenuShowing()) {
					
					sm.showContent(true);
				} 
			}
		}, 500);
		
		MobclickAgent.onPause(this);
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}
	private long mExitTime;
	private SlidingMenu sm;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, R.string.app_press_again_to_exit,Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				// mMyApplication.exit();
				finish();
			}
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
