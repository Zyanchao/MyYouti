package com.youti.yonghu.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youti_geren.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.youti.appConfig.Constants;
import com.youti.appConfig.YoutiApplication;
import com.youti.base.BaseActivity;
import com.youti.chat.activity.ChatActivity;
import com.youti.chat.domain.User;
import com.youti.fragment.CoachDetailIntroduceFragment;
import com.youti.fragment.CommentListsFragment;
import com.youti.utils.HttpUtils;
import com.youti.utils.IntentJumpUtils;
import com.youti.utils.Utils;
import com.youti.view.CustomVideoView;
import com.youti.yonghu.bean.CoachDetailBean;
import com.youti.yonghu.bean.VideoDetailBean;
import com.youti.yonghu.bean.VideoDetailBean.VideoDetailItem;

/**
 * 教练 详情页
 * @author zyc 2015-2-12
 */
public class CoachDetailActivity extends BaseActivity implements
		OnClickListener {

	private Context mContext;
	TextView tv_first;
	TextView tv_second;
	TextView tv1, tv2;
	FrameLayout fl_content;
	ArrayList<Fragment> fragmentLists;
	int screenWidth;
	View indicate_line;
	private ImageView iv_play;
	private CoachDetailBean coachDetailBean;
	private String id;
	private Button btZixun, btBuy;
	private List<CoachDetailBean> mCoachDetailList = new ArrayList<CoachDetailBean>();
	private CoachDetailBean mCoachDetail;

	private static final int MESSAGE_UPDATE_PLAY_PROGRESS = 100;
	// SurfaceView sv;
	CustomVideoView cvv;
	ImageView iv, iv_pause, iv_full, ivShare;
	ProgressBar pb;
	SeekBar seekBar;
	SurfaceHolder sh = null;
	String video_url;
	private String de_img;
	// MediaPlayer mp = null;
	boolean isPlay;
	private boolean isShowContol;
	TextView describe, tv_jianjie, tv_comment, tv_totalTime, tv_progressTime;
	LinearLayout controller_bottom;
	GestureDetector gesture;
	Display display;
	ImageView iv_video_bg;
	
	String tel, userName;
	private final int MESSAGE_HIDE_CONTROL = 0;// 延时隐藏控制面板
	private final int TOCOMMENT = 6;
	/**
	 * 访问网络请求成功，更新ui
	 */
	public final int INITVIDEODETAIL = 100011;

	VideoDetailItem videoDetailItemList;
	private LinearLayout ll_coach_detail;

	/**
	 * FragmentTabhost
	 */
	private FragmentTabHost mTabHost;

	/**
	 * 布局填充器
	 * 
	 */
	private LayoutInflater mLayoutInflater;

	/**
	 * Fragment数组界面
	 * 
	 */
	private Class mFragmentArray[] = { CoachDetailIntroduceFragment.class,
			CommentListsFragment.class };

	/**
	 * 选修卡文字
	 * 
	 */
	private String mTextArray[] = { "简介", "评论" };
	private String video_url2;
	private BroadcastReceiver receiver =new BroadcastReceiver() {
		

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent!=null){
				video_url2 = intent.getStringExtra("video_url");
				de_img = intent.getStringExtra("de_img");
				//Utils.showToast(context, "收到广播了"+video_url2+":de_img:"+de_img);
				ImageLoader.getInstance().displayImage(de_img, iv_video_bg);
				
				cvv.setVideoURI(Uri.parse(video_url2));
				/**
				 * 自动播放的回调onPrepared
				 */
				
				cvv.setOnPreparedListener(new OnPreparedListener() {

					@Override
					public void onPrepared(MediaPlayer mp) {
						cvv.setBackgroundColor(Color.TRANSPARENT);
						pb.setVisibility(View.GONE);
						//Utils.showToast(CoachDetailActivity.this, "可以播放了");
						iv.setVisibility(View.VISIBLE);
						cvv.pause();
						
						
						
						/*iv_video_bg.setVisibility(View.GONE);
						iv.setVisibility(View.GONE);
						pb.setVisibility(View.GONE);
						cvv.setBackgroundColor(Color.TRANSPARENT);
						cvv.start();
						seekBar.setMax(cvv.getDuration());
						describe.setVisibility(View.GONE);
						controller_bottom.setVisibility(View.VISIBLE);
						tv_totalTime.setText(formatVideoDuration(cvv.getDuration()));
						updatePlayProgress();*/
						
						
					}
				});
				
			}
		}
	};
	private String headImg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coach_detail);
		mContext = this;
		id = getIntent().getStringExtra(Constants.KEY_ID);
		tel = getIntent().getStringExtra(Constants.KEY_CHAT_TEL)+Constants.CHAT_COACH_CODE;
		userName = getIntent().getStringExtra(Constants.KEY_CHAT_USERNAME);
		headImg =  getIntent().getStringExtra(Constants.KEY_CHAT_AVATAR);
		
		IntentFilter filter =new IntentFilter();
		filter.addAction("com.example.yoti_geren.play");
		registerReceiver(receiver, filter);
		
		
		initView();
		initListener();
		initData();
		configPlatforms();
		setShareContent();
		
	}

	private void initView() {
		mLayoutInflater = LayoutInflater.from(this);
		
		//设置 scrollView 定位到顶部
		ll_coach_detail = (LinearLayout) findViewById(R.id.ll_coach_detail);
		ll_coach_detail.setFocusable(true);
		ll_coach_detail.setFocusableInTouchMode(true);
		ll_coach_detail.requestFocus();
		// 找到TabHost
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		// 得到fragment的个数
		int count = mFragmentArray.length;
		for (int i = 0; i < count; i++) {
			// 给每个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(mTextArray[i]).setIndicator(
					getTabItemView(i));
			// 将Tab按钮添加进Tab选项卡中
			Bundle bundle = new Bundle();
			bundle.putString(Constants.KEY_ID, id);
			mTabHost.addTab(tabSpec, mFragmentArray[i], bundle);
			// 设置Tab按钮的背景
			mTabHost.getTabWidget().getChildAt(i)
					.setBackgroundResource(R.drawable.selector_detail_tab);
		}

		btZixun = (Button) findViewById(R.id.bt_book);
		btBuy = (Button) findViewById(R.id.bt_buy);
		btZixun.setText("咨询");
		ivShare = (ImageView) findViewById(R.id.title_share);
		cvv = (CustomVideoView) findViewById(R.id.cvv);
		controller_bottom = (LinearLayout) findViewById(R.id.controller_bottom);
		describe = (TextView) findViewById(R.id.describe);
		iv = (ImageView) findViewById(R.id.iv_play);
		iv.setVisibility(View.GONE);
		iv_pause = (ImageView) findViewById(R.id.iv_pause);
		iv_full = (ImageView) findViewById(R.id.iv_full);
		pb = (ProgressBar) findViewById(R.id.pb_video);
		tv_totalTime = (TextView) findViewById(R.id.tv_totaltime);
		tv_progressTime = (TextView) findViewById(R.id.tv_progresstime);
		seekBar = (SeekBar) findViewById(R.id.video_seekbar);
		iv_video_bg=(ImageView) findViewById(R.id.iv_video_bg);
	}

	private void initListener() {
		btZixun.setOnClickListener(this);
		btBuy.setOnClickListener(this);

		ivShare.setOnClickListener(this);

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
				if (fromUser) {
					cvv.seekTo(progress);
					tv_progressTime.setText(formatVideoDuration(progress));
				}
			}
		});

		// 视频播放按钮点击监听
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
					isShowContol = false;
				} else {
					iv.setVisibility(View.GONE);
					cvv.start();
				
				iv_video_bg.setVisibility(View.GONE);
				cvv.setBackgroundColor(Color.TRANSPARENT);
				seekBar.setMax(cvv.getDuration());
				describe.setVisibility(View.GONE);
				controller_bottom.setVisibility(View.VISIBLE);
				tv_totalTime.setText(formatVideoDuration(cvv.getDuration()));
				updatePlayProgress();
				
				}
			}
		});

		// 视频暂停点击监听
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
				} else {
					iv.setVisibility(View.GONE);
					cvv.start();
				}
			}
		});
		// 视频全屏点击监听
		iv_full.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				Intent intent = new Intent(CoachDetailActivity.this,
						VideoActivity.class);
				intent.putExtra("video_url", video_url2);
				startActivity(intent);

			}
		});
	}

	
	private void initData() {

		String urlDetail = "http://112.126.72.250/ut_app/index.php?m=Video&a=detaillist";

		gesture = new GestureDetector(new MyGestureLitener());
		display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		//cvv.setBackgroundResource(R.drawable.spxq_pic);
		// cvv.setMediaController(new MediaController(this));
		cvv.requestFocus();
		User user = new User();
		user.setAvatar(Constants.PIC_CODE+headImg);
		user.setUsername(tel);
		YoutiApplication.getInstance().setContact(user);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gesture.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

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
				// iv.setVisibility(View.VISIBLE);
				isShowContol = false;
				break;

			default:
				break;
			}
		};
	};

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

	/**
	 * 更新播放进度
	 */
	private void updatePlayProgress() {
		tv_progressTime.setText(formatVideoDuration(cvv.getCurrentPosition()));
		seekBar.setProgress(cvv.getCurrentPosition());
		handler.sendEmptyMessageDelayed(MESSAGE_UPDATE_PLAY_PROGRESS, 1000);
	}

	public String formatVideoDuration(long duration) {
		int HOUR = 60 * 60 * 1000;// 1小时所占的毫秒
		int MINUTE = 60 * 1000;// 1分钟所占的毫秒
		int SECOND = 1000;// 1秒钟

		// 1.算出多少小时，然后拿剩余的时间去算分钟
		int hour = (int) (duration / HOUR);// 得到多少小时
		long remainTime = duration % HOUR;// 算完小时后剩余的时间
		// 2.算分钟，然后拿剩余的时间去算秒
		int minute = (int) (remainTime / MINUTE);// 得到多少分钟
		remainTime = remainTime % MINUTE;// 得到算完分钟后剩余的时间
		// 3.算秒
		int second = (int) (remainTime / SECOND);// 得到多少秒

		if (hour == 0) {
			// 说明不足1小时,只有2:33
			return String.format("%02d:%02d", minute, second);
		} else {
			return String.format("%02d:%02d:%02d", hour, minute, second);
		}
	}

	/**
	 * 
	 * 给每个Tab按钮设置图标和文字
	 */
	private View getTabItemView(int index) {
		View view = mLayoutInflater.inflate(R.layout.tab_item_view, null);
		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText(mTextArray[index]);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_book:// 咨询


			if(!YoutiApplication.getInstance().myPreference.getHasLogin()){
				IntentJumpUtils.nextActivity(LoginActivity.class, this, null, Constants.REQUEST_CODE_DETAIL_COURSE);
				return;
			}
			
			SetHailFellow();

			
			// SetHailFellow();
			/*
			 * if(((YoutiApplication)getApplication()).myPreference.getHasLogin()
			 * ){ SetHailFellow(); }else {
			 * IntentJumpUtils.nextActivity(LoginActivity.class, this);
			 * Toast.makeText(getApplicationContext(), "亲，登陆后才能咨询哦", 0).show();
			 * }
			 */

			break;

		case R.id.bt_buy:// 购买
			if(!YoutiApplication.getInstance().myPreference.getHasLogin()){
				IntentJumpUtils.nextActivity(LoginActivity.class, this, null, Constants.REQUEST_CODE_DETAIL_COURSE);
				return;
			}
			Bundle bundle = new Bundle();
			bundle.putString(Constants.KEY_ID, id);
			IntentJumpUtils.nextActivity(BuyCoachCourseActivity.class, this, bundle);

			break;
		case R.id.title_share:
			if(!YoutiApplication.getInstance().myPreference.getHasLogin()){
				IntentJumpUtils.nextActivity(LoginActivity.class, this, null, Constants.REQUEST_CODE_DETAIL_COURSE);
				return;
			}
			//Utils.showToast(CoachDetailActivity.this, "分享该页面开始了");
			 mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
	                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,SHARE_MEDIA.SINA);
	            mController.openShare(CoachDetailActivity.this, false);
			break;
		default:
			break;
		}
	}

	// 建立好友关系
	private void SetHailFellow() {
		// TODO Auto-generated method stub
		String urlString = Constants.HAIL_FELLOW;
		RequestParams params = new RequestParams(); // 绑定参数
		params.put("user_id",((YoutiApplication) getApplication()).myPreference.getUserId());
		params.put("coach_id", id);

		HttpUtils.post(urlString, params, new JsonHttpResponseHandler() {
			public void onStart() {
				super.onStart();
			}

			public void onFailure(int statusCode,
					org.apache.http.Header[] headers,
					java.lang.Throwable throwable,
					org.json.JSONObject errorResponse) {
			};

			public void onFinish() {
			};

			public void onSuccess(int statusCode,
					org.apache.http.Header[] headers,
					org.json.JSONObject response) {
				try {
					// 接口返回
					String state = response.getString("code");
					if (state.equals("1")) {
						Bundle b = new Bundle();
						b.putString(Constants.KEY_CHAT_TEL, tel);
						b.putString(Constants.KEY_CHAT_USERNAME,userName);
						b.putInt("chatType", 1);
						IntentJumpUtils.nextActivity(ChatActivity.class,CoachDetailActivity.this, b);
					} else {
						Intent intent = new Intent(CoachDetailActivity.this,
								ChatActivity.class);
						// intent.putExtra("userId", phone);
						// intent.putExtra("cttx", coachHead);
						if (((YoutiApplication) getApplication()).myPreference
								.getHeadImgPath().length() != 0) {
							intent.putExtra(
									"uttx",
									((YoutiApplication) getApplication()).myPreference
											.getHeadImgPath());

						}
						// intent.putExtra("name", ((TextView)
						// view1.findViewById(R.id.info_username)).getText().toString());
						startActivity(intent);
					}
				} catch (Exception e) {

				}
			};
		});
	}
	
	
	
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
	        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this,
	                appId, appKey);
	        qqSsoHandler.setTargetUrl("http://wtapp.yoti.cn/index.php?m=Share&a=socialdetail&social_id="+id);
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
	        String appId = "wxbc29ec947c61f0e1";
	        String appSecret = "3f7f7b0bbf4d48f3dcc2647d95c3c688";
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
	        mController.setShareContent("今夏最火的运动健身APP，大家一起来体验吧！http://112.126.72.250/ut_app/index.php?m=Share&a=coachdetail");


	        UMImage localImage = new UMImage(this, R.drawable.sp_head);
	        UMImage urlImage = new UMImage(this,
	                "http://wtapp.yoti.cn/show/more_logo.png");
	        // UMImage resImage = new UMImage(getActivity(), R.drawable.icon);

	        
	        WeiXinShareContent weixinContent = new WeiXinShareContent();
	        weixinContent.setShareContent("今夏最火的运动健身APP，大家一起来体验吧！http://112.126.72.250/ut_app/index.php?m=Share&a=coachdetail");
	        weixinContent.setTitle("优体教练");
	        weixinContent.setTargetUrl("http://112.126.72.250/ut_app/index.php?m=Share&a=coachdetail");
	        weixinContent.setShareMedia(urlImage);
	        mController.setShareMedia(weixinContent);
	        
	        SnsPostListener mSnsPostListener  = new SnsPostListener() {

		        @Override
		    public void onStart() {

		    }

		    @Override
		    public void onComplete(SHARE_MEDIA platform, int stCode,
		        SocializeEntity entity) {
		      if (stCode == 200) {
		        Toast.makeText(CoachDetailActivity.this, "分享成功.......", Toast.LENGTH_SHORT)
		            .show();
		      } else {
		        Toast.makeText(CoachDetailActivity.this,
		            "分享失败 : error code : " + stCode, Toast.LENGTH_SHORT)
		            .show();
		      }
		    }
		  };
		  mController.registerListener(mSnsPostListener);
		  

	        // 设置朋友圈分享的内容
	        CircleShareContent circleMedia = new CircleShareContent();
	        circleMedia.setShareContent("今夏最火的运动健身APP，大家一起来体验吧！http://112.126.72.250/ut_app/index.php?m=Share&a=coachdetail");
	        circleMedia.setTitle("优体教练用户端");
	        circleMedia.setShareMedia(urlImage);
	        // circleMedia.setShareMedia(uMusic);
	        // circleMedia.setShareMedia(video);
	        circleMedia.setTargetUrl("http://112.126.72.250/ut_app/index.php?m=Share&a=coachdetail");
	        mController.setShareMedia(circleMedia);

	     

	        UMImage qzoneImage = new UMImage(this,
	                "http://wtapp.yoti.cn/show/more_logo.png");
	        qzoneImage
	                .setTargetUrl("http://112.126.72.250/ut_app/index.php?m=Share&a=coachdetail");

	        // 设置QQ空间分享内容
	        QZoneShareContent qzone = new QZoneShareContent();
	        qzone.setShareContent("优体教练用户端");
	        qzone.setTargetUrl("http://112.126.72.250/ut_app/index.php?m=Share&a=coachdetail");
	        qzone.setTitle("优体教练用户端");
	        qzone.setShareMedia(urlImage);
//	        qzone.setShareMedia(uMusic);
	        mController.setShareMedia(qzone);


	        QQShareContent qqShareContent = new QQShareContent();
//	        qqShareContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能 -- QQ");
	        qqShareContent.setTitle("优体教练用户端");
	        qqShareContent.setTargetUrl("http://112.126.72.250/ut_app/index.php?m=Share&a=coachdetail");
	        mController.setShareMedia(qqShareContent);





	        SinaShareContent sinaContent = new SinaShareContent();
	        sinaContent.setShareContent("优体教练用户端");
	        sinaContent.setTargetUrl("http://112.126.72.250/ut_app/index.php?m=Share&a=coachdetail");
	        sinaContent.setTitle("优体教练用户端");
	        sinaContent.setShareMedia(urlImage);
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
		protected void onPause() {
			super.onPause();
			MobclickAgent.onPause(this);
		}

		@Override
		protected void onResume() {
			super.onResume();
			MobclickAgent.onResume(this);
		}
}
