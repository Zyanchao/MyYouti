package com.youti.yonghu.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.youti.appConfig.YoutiApplication;
import com.youti.fragment.VideoCommentFragment;
import com.youti.fragment.VideoIntroduceFragment;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
import com.youti.view.CustomVideoView;
import com.youti.view.MyViewPager;
import com.youti.view.TitleBar;
import com.youti.yonghu.bean.InfoBean;
import com.youti.yonghu.bean.VideoDetailBean;
import com.youti.yonghu.bean.VideoDetailBean.CommentItem;
import com.youti.yonghu.bean.VideoDetailBean.PraiseItem;
import com.youti.yonghu.bean.VideoDetailBean.VideoDetailItem;
import com.youti.yonghu.download.BaseActivity;
import com.youti.yonghu.download.DownloadManagerActivity;

/**
 * 视频详情页
 * 
 * @author dbc
 * 
 */
public class VideoDetailActivity extends BaseActivity {
	
	private static final int MESSAGE_UPDATE_PLAY_PROGRESS = 100;
	//SurfaceView sv;
	CustomVideoView cvv;
	ImageView iv, iv_pause,iv_full;
	SurfaceHolder sh = null;
	//MediaPlayer mp = null;
	boolean isPlay;
	TextView describe,tv_jianjie,tv_comment,tv_totalTime,tv_progressTime;
	MyViewPager viewPager;
	LinearLayout controller_bottom,ll_comment,bottom,spxq_pl,spxq_zan,spxq_xz,spxq_fx;
	ArrayList<Fragment> list = new ArrayList<Fragment>();
	private boolean isShowContol;
	GestureDetector gesture;
	Display display;
	TitleBar titleBar;
	Button comment_send;
	EditText et_comment;
	private VideoDetailBean videoDetailBean;
	VideoDetailItem videoDetailItemList;
	ProgressBar pb;
	SeekBar seekBar;
	
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
	
	public VideoDetailItem getVideoDetailItemList() {
		return videoDetailItemList;
	}
	public void setVideoDetailItemList(VideoDetailItem videoDetailItemList) {
		this.videoDetailItemList = videoDetailItemList;
	}
	String video_url;
	//友盟分享
	// 首先在您的Activity中添加如下成员变量
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	
	
	 /* * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	     *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	     *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	     *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	     * @return
	     */
	    private void addQQQZonePlatform() {
	        String appId = "1103176396";
	        String appKey = "iY2uCRqXmaooIL7b";
	        // 添加QQ支持, 并且设置QQ分享内容的target url
	        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this,appId, appKey);
	        qqSsoHandler.setTargetUrl("http://www.umeng.com/social");
	        qqSsoHandler.addToSocialSDK();

	        // 添加QZone平台
	        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, appId, appKey);
	        qZoneSsoHandler.addToSocialSDK();
	    }
	    /**
	     * @功能描述 : 添加微信平台分享
	     * @return
	     */
	    private void addWXPlatform() {
	        // 注意：在微信授权的时候，必须传递appSecret
	        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
	        String appId = "wx967daebe835fbeac";
	        String appSecret = "5bb696d9ccd75a38c8a0bfe0675559b3";
	        // 添加微信平台
	        UMWXHandler wxHandler = new UMWXHandler(this, appId, appSecret);
	        wxHandler.addToSocialSDK();

	        // 支持微信朋友圈
	        UMWXHandler wxCircleHandler = new UMWXHandler(this, appId, appSecret);
	        wxCircleHandler.setToCircle(true);
	        wxCircleHandler.addToSocialSDK();
	    }

	    private void setShareContent() {

	        // 配置SSO
	        mController.getConfig().setSsoHandler(new SinaSsoHandler());
	        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

	        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this,
	                "1103176396", "iY2uCRqXmaooIL7b");
	        qZoneSsoHandler.addToSocialSDK();
	        mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能。http://www.umeng.com/social");


	        UMImage localImage = new UMImage(this, R.drawable.sp_head);
	        UMImage urlImage = new UMImage(this,
"http://www.umeng.com/images/pic/social/integrated_3.png");
	        // UMImage resImage = new UMImage(getActivity(), R.drawable.icon);

	        
	        WeiXinShareContent weixinContent = new WeiXinShareContent();
	        weixinContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-微信。http://www.umeng.com/social");
	        weixinContent.setTitle("友盟社会化分享组件-微信");
	        weixinContent.setTargetUrl("http://www.umeng.com/social");
	        weixinContent.setShareMedia(urlImage);
	        mController.setShareMedia(weixinContent);

	        // 设置朋友圈分享的内容
	        CircleShareContent circleMedia = new CircleShareContent();
	        circleMedia.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-朋友圈。http://www.umeng.com/social");
	        circleMedia.setTitle("友盟社会化分享组件-朋友圈");
	        circleMedia.setShareMedia(urlImage);
	        // circleMedia.setShareMedia(uMusic);
	        // circleMedia.setShareMedia(video);
	        circleMedia.setTargetUrl("http://www.umeng.com/social");
	        mController.setShareMedia(circleMedia);

	     
	        UMImage qzoneImage = new UMImage(this,
	                "http://www.umeng.com/images/pic/social/integrated_3.png");
	        qzoneImage
	                .setTargetUrl("http://www.umeng.com/images/pic/social/integrated_3.png");

	        // 设置QQ空间分享内容
	        QZoneShareContent qzone = new QZoneShareContent();
	        qzone.setShareContent("share test");
	        qzone.setTargetUrl("http://www.umeng.com");
	        qzone.setTitle("QZone title");
	        qzone.setShareMedia(urlImage);
//	        qzone.setShareMedia(uMusic);
	        mController.setShareMedia(qzone);


	        QQShareContent qqShareContent = new QQShareContent();
//	        qqShareContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能 -- QQ");
	        qqShareContent.setTitle("hello, title");
	        qqShareContent.setTargetUrl("http://www.umeng.com/social");
	        mController.setShareMedia(qqShareContent);

	        SinaShareContent sinaContent = new SinaShareContent();
	        sinaContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-新浪微博。http://www.umeng.com/social");
	        mController.setShareMedia(sinaContent);
	        
	    }
	    	    
	    /**
	     * 配置分享平台参数</br>
	     */
	    private void configPlatforms() {
	        // 添加新浪SSO授权
	        mController.getConfig().setSsoHandler(new SinaSsoHandler());
	        // 添加腾讯微博SSO授权
	        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
	      //设置新浪SSO handler
			mController.getConfig().setSinaCallbackUrl("https://api.weibo.com/oauth2/default.html");

	        // 添加QQ、QZone平台
	        addQQQZonePlatform();

	        // 添加微信、微信朋友圈平台
	        addWXPlatform();
	    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	
		if(getIntent()!=null){
			video_id = getIntent().getStringExtra("video_id");
			pinglun=getIntent().getStringExtra("pinglun");
		}
		setContentView(R.layout.layout_spxq);
		//调用父类的方法
		setmContext(VideoDetailActivity.this);
		initView();
		
		initView1();
		initListener();
		initData();
		
		//分享
		configPlatforms();
		setShareContent();
		
		
		
		if(!TextUtils.isEmpty(pinglun)&&pinglun.equals("pinglun")){
			viewPager.setCurrentItem(1);
		}
		
		

	}
	/**
	 * 访问网络请求成功，更新ui
	 */
	public final int INITVIDEODETAIL=100011;
		

	private void initData() {
		titleBar.setTitleBarTitle("视频详情");
		titleBar.setSearchGone();
		
		String urlDetail="http://112.126.72.250/ut_app/index.php?m=Video&a=detaillist";
		
		gesture= new GestureDetector(new MyGestureLitener());
		display =getWindowManager().getDefaultDisplay();
		int width =display.getWidth();
		int height =display.getHeight();
		cvv.setBackgroundResource(R.drawable.spxq_pic);
	//	cvv.setMediaController(new MediaController(this));
		cvv.requestFocus();
		iv.setVisibility(View.GONE);
		cvv.setVideoURI(Uri.parse("http://112.126.72.250/ut_app/video/xiaou_3.mp4"));
		
		cvv.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
			//	pb.setVisibility(View.GONE);
				iv.setVisibility(View.GONE);
				pb.setVisibility(View.GONE);
				cvv.setBackgroundColor(Color.TRANSPARENT);
				cvv.start();
				seekBar.setMax(cvv.getDuration());
				describe.setVisibility(View.GONE);
				controller_bottom.setVisibility(View.VISIBLE);
				tv_totalTime.setText(formatVideoDuration(cvv.getDuration()));
				updatePlayProgress();
			}
		});
		
			
		RequestParams params =new RequestParams();
		//目前只有1中有目录
		params.put("video_id", "1");
		HttpUtils.post(urlDetail, params, new TextHttpResponseHandler() {
			
			

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				try{
					Gson gson =new Gson();
					videoDetailBean = gson.fromJson(arg2, VideoDetailBean.class);
					//System.out.println(arg2.toString());
					videoDetailItemList=videoDetailBean.list;
					video_url=videoDetailItemList.video_url;
					System.out.println(video_url);
					Message msg =Message.obtain();
					msg.what=INITVIDEODETAIL;
					handler.sendMessage(msg);
					
					
				}catch (Exception e) {
				}
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				System.out.println("视频详情访问网络失败");
			}
		});

	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gesture.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	
	/**
	 * 发送评论内容到服务器
	 * @param text 评论内容
	 * 传入参数：
		User_id：用户id
		Video_id：视频id
		User_img：用户头像
		User_name：用户姓名
		Content：评论内容
		返回参数：
		Code：400 用户id不能为空
		Code：401 视频id不能为空
		Code：1 成功
		Code：0 失败
	 */
	String url_comment="http://112.126.72.250/ut_app/index.php?m=Video&a=video_comment";
	private List<PraiseItem> praiseList;
	private void sendComment(String text) {
		RequestParams params =new RequestParams();
		params.put("user_id", "1");
		params.put("video_id", "1");
		params.put("user_img", "111");
		params.put("user_name", "XGC");
		params.put("content", text);
		
		HttpUtils.post(url_comment, params, new TextHttpResponseHandler() {
			Message msg =Message.obtain();
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Message msg =Message.obtain();
				msg.what=SENDCOMMENT_SUCCESS;
				handler.sendMessage(msg);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Message msg =Message.obtain();
				msg.what=SENDCOMMENT_FAIL;
				handler.sendMessage(msg);
			}
		});
	}
	
	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    /**使用SSO授权必须添加如下代码 */
	    UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
	    if(ssoHandler != null){
	       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	    }
	}
	private void initListener() {
		//分享点击事件监听
		spxq_fx.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Utils.showToast(VideoDetailActivity.this, "分享");
				 mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
		                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,SHARE_MEDIA.SINA);
		            mController.openShare(VideoDetailActivity.this, false);
			}
		});
		//下载点击事件监听
		spxq_xz.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Utils.showToast(VideoDetailActivity.this, "下载");
				Intent intent =new Intent(VideoDetailActivity.this,DownloadManagerActivity.class);
				startActivity(intent);
			}
		});
		//点赞点击事件监听
		spxq_zan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Utils.showToast(VideoDetailActivity.this, "点赞");
				
				//sendPraise(v);
				requestPraise(((YoutiApplication)getApplication()).myPreference.getUserId());
			}
		});
		//评论点击事件监听
		spxq_pl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//调用软键盘
				if(viewPager.getCurrentItem()==1){
					InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					return;
				}
				viewPager.setCurrentItem(1);
			}
		});
		//评论信息发送点击监听
		comment_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				String text = et_comment.getText().toString().trim();
				if(TextUtils.isEmpty(text)){
					Utils.showToast(VideoDetailActivity.this, "评论内容最好不要为空");
					return;
				}
				VideoDetailBean vdb =new VideoDetailBean();
				VideoDetailBean.CommentItem ci =vdb.new CommentItem();
				
				ci.setComment_content(text);
				ci.setComment_time(System.currentTimeMillis()+"");
				ci.setUser_name("XGC");
				
				list2 = vcf.getCommentList();
				list2.add(ci);
				sendComment(text);
				
			}

			
		});
		
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
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
					cvv.seekTo(progress);
					tv_progressTime.setText(formatVideoDuration(progress));
				}
			}
		});
		
		//视频播放按钮点击监听
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (cvv.isPlaying()) {
					cvv.pause();
					iv.setVisibility(View.VISIBLE);
					ViewPropertyAnimator.animate(controller_bottom)
					.translationY(controller_bottom.getHeight())
					.setDuration(200);
					iv.setVisibility(View.GONE);
					isShowContol=false;
				}else{
					iv.setVisibility(View.GONE);
					cvv.start();
				}
			}
		});
		
		//评论标题点击监听
		tv_comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				viewPager.setCurrentItem(1);
			}
		});
		//简介标题点击监听
		tv_jianjie.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				viewPager.setCurrentItem(0);
			}
		});
		
		//视频暂停点击监听
		iv_pause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (cvv.isPlaying()) {
					cvv.pause();
					iv.setVisibility(View.VISIBLE);
					ViewPropertyAnimator.animate(controller_bottom)
					.translationY(controller_bottom.getHeight())
					.setDuration(200);
					isShowContol = false;
				}else{
					iv.setVisibility(View.GONE);
					cvv.start();
				}
			}
		});
		//视频全屏点击监听
		iv_full.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			//	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				Intent intent =new Intent(VideoDetailActivity.this,VideoActivity.class);
				intent.putExtra("video_url", video_url);
				startActivity(intent);
				
			}
		});
		//viewPager的页面改变监听
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				lightAndScaleTabTitle();
				if(arg0==0){
					vif.smoothScrollTo();
					ll_comment.setVisibility(View.GONE);
				}else{
					ll_comment.setVisibility(View.VISIBLE);
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
	
	/**
	 * 发送点赞请求，本地做+1或者-1操作
	 */

	private void requestPraise(String user_id) {
		//发送请求到服务器
		String urlString = "http://112.126.72.250/ut_app/index.php?m=Video&a=praise_video";
		RequestParams params =new RequestParams();
		params.put("user_id", user_id);
		params.put("video_id", 1);
		HttpUtils.post(urlString, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson =new Gson();
				InfoBean fromJson = gson.fromJson(arg2, InfoBean.class);
				Message msg =Message.obtain();
				if(fromJson.code.equals("1")){
					//点赞成功
					Utils.showToast(VideoDetailActivity.this, "点赞成功");
				}else if(fromJson.code.equals("0")){
					//点赞取消成功
					Utils.showToast(VideoDetailActivity.this, "取消点赞");
				}
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Utils.showToast(VideoDetailActivity.this, "网络连接异常");
			}
		});
	}
	
	/**
	 * 更新播放进度
	 */
	private void updatePlayProgress(){
		tv_progressTime.setText(formatVideoDuration(cvv.getCurrentPosition()));
		seekBar.setProgress(cvv.getCurrentPosition());
		handler.sendEmptyMessageDelayed(MESSAGE_UPDATE_PLAY_PROGRESS, 1000);
	}
	
	public  String formatVideoDuration(long duration){
		int HOUR = 60 * 60 * 1000;//1小时所占的毫秒
		int MINUTE = 60 * 1000;//1分钟所占的毫秒
		int SECOND = 1000;//1秒钟
		
		//1.算出多少小时，然后拿剩余的时间去算分钟
		int hour = (int) (duration/HOUR);//得到多少小时
		long remainTime = duration%HOUR;//算完小时后剩余的时间
		//2.算分钟，然后拿剩余的时间去算秒
		int minute = (int) (remainTime/MINUTE);//得到多少分钟
		remainTime = remainTime%MINUTE;//得到算完分钟后剩余的时间
		//3.算秒
		int second = (int) (remainTime/SECOND);//得到多少秒
		
		if(hour==0){
			//说明不足1小时,只有2:33
			return String.format("%02d:%02d", minute,second);
		}else {
			return String.format("%02d:%02d:%02d", hour,minute,second);
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		handler.removeCallbacksAndMessages(null);
		
	}
	private void initView1() {
		cvv=(CustomVideoView) findViewById(R.id.cvv);
		//sv = (SurfaceView) findViewById(R.id.surfaceView);
		describe = (TextView) findViewById(R.id.describe);
		iv = (ImageView) findViewById(R.id.iv_play);
		viewPager = (MyViewPager) findViewById(R.id.view_pager);
		controller_bottom = (LinearLayout) findViewById(R.id.controller_bottom);
		iv_pause = (ImageView) findViewById(R.id.iv_pause);
		iv_full=(ImageView) findViewById(R.id.iv_full);
		tv_jianjie=(TextView) findViewById(R.id.tv_jianjie);
		tv_comment=(TextView) findViewById(R.id.tv_comment);
		titleBar=(TitleBar) findViewById(R.id.index_titlebar);
		ll_comment=(LinearLayout) findViewById(R.id.ll_comment);
		bottom=(LinearLayout) findViewById(R.id.bottom);
		comment_send=(Button) findViewById(R.id.comment_send);
		et_comment=(EditText) findViewById(R.id.et_comment);
		spxq_pl=(LinearLayout) findViewById(R.id.spxq_pl);
		spxq_zan=(LinearLayout) findViewById(R.id.spxq_zan);
		spxq_xz=(LinearLayout) findViewById(R.id.spxq_xz);
		spxq_fx=(LinearLayout) findViewById(R.id.spxq_fx);
		pb=(ProgressBar) findViewById(R.id.pb);
		tv_totalTime=(TextView) findViewById(R.id.tv_totaltime);
		tv_progressTime=(TextView) findViewById(R.id.tv_progresstime);
		seekBar=(SeekBar) findViewById(R.id.video_seekbar);
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

	private final int MESSAGE_HIDE_CONTROL = 0;// 延时隐藏控制面板
	private final int SENDCOMMENT_FAIL=1;
	private final int SENDCOMMENT_SUCCESS=2;
	private  final int PRAISESUCCESS = 3;
	private final int PRAISEFAIL=4;
	private final int CANCLEPRAISE=5;
	private final int TOCOMMENT=6;
	private List<CommentItem> list2;
	Handler handler = new Handler() {
		

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MESSAGE_UPDATE_PLAY_PROGRESS:
				updatePlayProgress();
				break;
			case TOCOMMENT:
				
				break;
			case MESSAGE_HIDE_CONTROL:
				ViewPropertyAnimator.animate(controller_bottom)
						.translationY(controller_bottom.getHeight())
						.setDuration(200);
				//iv.setVisibility(View.VISIBLE);
				isShowContol = false;
				break;
			case INITVIDEODETAIL:
				//网络请求成功，获得数据，设置数据
				describe.setText(videoDetailItemList.title);
				
				vcf = new VideoCommentFragment();
				vif = new VideoIntroduceFragment();
				list.add(vif);
				list.add(vcf);
				viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
				if(!TextUtils.isEmpty(pinglun)&&pinglun.equals("pinglun")){
					viewPager.setCurrentItem(1);
				}
				break;
			case SENDCOMMENT_FAIL:
				Utils.showToast(VideoDetailActivity.this, "评论失败，请稍后再试");
				break;
			case SENDCOMMENT_SUCCESS:
				
				
				vcf.notifyDataSetChanged();
				et_comment.setText("");
				ListView lv =vcf.getListView();
				lv.setSelection(0);
				
				InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				Utils.showToast(VideoDetailActivity.this, "评论成功");
				break;
			case PRAISESUCCESS:
				//点赞成功，更新adapter
				Utils.showToast(VideoDetailActivity.this, "点赞成功");
				break;
			case CANCLEPRAISE:
				Utils.showToast(VideoDetailActivity.this, "点赞取消");
				break;
			case PRAISEFAIL:
				Utils.showToast(VideoDetailActivity.this, "点赞失败，请稍后再试");
				break;
			default:
				break;
			}
		};
	};
	private VideoIntroduceFragment vif;
	private VideoCommentFragment vcf;
	private String pinglun;
	private String video_id;
	private void lightAndScaleTabTitle(){
		int currentPage = viewPager.getCurrentItem();
		
		tv_jianjie.setTextColor(currentPage==0?Color.parseColor("#6049a1")
				:Color.parseColor("#666666"));
		tv_comment.setTextColor(currentPage==1?Color.parseColor("#6049a1")
				:Color.parseColor("#666666"));
		
		ViewPropertyAnimator.animate(tv_jianjie).scaleX(currentPage==0?1f:0.8f).setDuration(100);
		ViewPropertyAnimator.animate(tv_jianjie).scaleY(currentPage==0?1f:0.8f).setDuration(100);
		ViewPropertyAnimator.animate(tv_comment).scaleX(currentPage==1?1f:0.8f).setDuration(100);
		ViewPropertyAnimator.animate(tv_comment).scaleY(currentPage==1?1f:0.8f).setDuration(100);
	}
	class MyAdapter extends FragmentPagerAdapter {

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
	
	
}
