package com.youti.yonghu.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.youti_geren.R;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.umeng.analytics.MobclickAgent;
import com.youti.utils.StringUtil;

public class VideoActivity extends Activity{
	VideoView vv;
	ImageView iv_pause;
	TextView tv_progressTime,tv_totalTime;
	SeekBar video_seekbar;
	LinearLayout controller_bottom;
	ImageView iv_play;
	private boolean isShowContol;
	private int currentPosition;
	private final int updateProgress=0;
	ProgressBar animProgress;
	GestureDetector gesture;
	ImageView iv_exit;
	
	private Handler handler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case updateProgress:
				updatePlaySeekBar();
				break;
			
			case MESSAGE_HIDE_CONTROL:
				ViewPropertyAnimator.animate(controller_bottom)
						.translationY(controller_bottom.getHeight())
						.setDuration(200);
				//iv.setVisibility(View.VISIBLE);
				isShowContol = false;
				break;
			default:
				break;
			}
		};
	};
	
	public final String mPageName ="VideoActivity";
	private String video_url;
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart( mPageName );
		MobclickAgent.onResume(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd( mPageName );
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_videoactivity);
		if (getIntent()!=null) {
			video_url = getIntent().getStringExtra("video_url");
		}
		initView();
		initListener();
		initData();
	}
	private void initData() {
		vv.setVideoURI(Uri.parse(video_url));
		gesture= new GestureDetector(new MyGestureLitener());
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gesture.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	private class MyGestureLitener extends SimpleOnGestureListener {
		@Override
		public void onLongPress(MotionEvent e) {
			super.onLongPress(e);
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			return super.onDoubleTap(e);
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			if (isShowContol) {
				// 隐藏操作
				ViewPropertyAnimator.animate(controller_bottom)
						.translationY(controller_bottom.getHeight())
						.setDuration(200);
				isShowContol = false;
			} else {
				// 显示操作
				ViewPropertyAnimator.animate(controller_bottom).translationY(0)
						.setDuration(200);
				isShowContol = true;

				handler.sendEmptyMessageDelayed(MESSAGE_HIDE_CONTROL, 5000);
			}
			return super.onSingleTapConfirmed(e);
		}
	}
	private void initListener() {
		vv.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				vv.seekTo(0);
				vv.pause();
				iv_play.setVisibility(View.VISIBLE);
			}
		});
		
		vv.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				animProgress.setVisibility(View.GONE);
				vv.start();
				video_seekbar.setMax(vv.getDuration());
				tv_totalTime.setText(StringUtil.formatVideoDuration(vv.getDuration()));
				
				currentPosition = vv.getCurrentPosition();
				updatePlaySeekBar();
			}
		});
		
		//视频播放按钮点击监听
		iv_play.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (vv.isPlaying()) {
							vv.pause();
							iv_play.setVisibility(View.VISIBLE);
							ViewPropertyAnimator.animate(controller_bottom)
							.translationY(controller_bottom.getHeight())
							.setDuration(200);
							iv_play.setVisibility(View.GONE);
							isShowContol=false;
						}else{
							iv_play.setVisibility(View.GONE);
							vv.start();
						}
					}
				});
		
		//退出按钮
		iv_exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				VideoActivity.this.finish();
			}
		});
		
		//视频暂停点击监听
				iv_pause.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						if (vv.isPlaying()) {
							vv.pause();
							iv_play.setVisibility(View.VISIBLE);
							ViewPropertyAnimator.animate(controller_bottom)
							.translationY(controller_bottom.getHeight())
							.setDuration(200);
							isShowContol = false;
						}else{
							iv_play.setVisibility(View.GONE);
							vv.start();
						}
					}
				});
		video_seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				handler.sendEmptyMessageDelayed(MESSAGE_HIDE_CONTROL, 5000);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				handler.removeMessages(MESSAGE_HIDE_CONTROL);
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if(fromUser){
					vv.seekTo(progress);
					tv_progressTime.setText(StringUtil.formatVideoDuration(progress));
				}
			}
		});
	}
	
	private final int MESSAGE_HIDE_CONTROL = 1;// 延时隐藏控制面板
	private void initView() {
		vv=(VideoView) findViewById(R.id.video);
		iv_pause=(ImageView) findViewById(R.id.iv_pause);
		tv_progressTime=(TextView) findViewById(R.id.tv_progresstime);
		tv_totalTime =(TextView) findViewById(R.id.tv_totaltime);
		video_seekbar=(SeekBar) findViewById(R.id.video_seekbar);
		animProgress=(ProgressBar) findViewById(R.id.animProgress);
		controller_bottom=(LinearLayout) findViewById(R.id.controller_bottom);
		iv_play=(ImageView) findViewById(R.id.iv_play);
		iv_exit=(ImageView) findViewById(R.id.iv_exit);
	}
	
	public void updatePlaySeekBar(){
		tv_progressTime.setText(StringUtil.formatVideoDuration(vv.getCurrentPosition()));
		video_seekbar.setProgress(vv.getCurrentPosition());
		handler.sendEmptyMessageDelayed(updateProgress, 1000);
	}
	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}
}
