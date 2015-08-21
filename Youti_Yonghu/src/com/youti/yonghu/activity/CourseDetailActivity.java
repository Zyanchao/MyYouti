package com.youti.yonghu.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbDialogUtil;
import com.ab.util.AbToastUtil;
import com.ab.view.sliding.AbSlidingPlayView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.youti_geren.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
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
import com.youti.fragment.CourseDetailCoachJsFragment;
import com.youti.fragment.CourseDetailJsFragment;
import com.youti.utils.HttpUtils;
import com.youti.utils.IntentJumpUtils;
import com.youti.utils.NetTips;
import com.youti.utils.Utils;
import com.youti.view.CustomProgressDialog;
import com.youti.view.TitleBar;
import com.youti.yonghu.bean.Carousel;
import com.youti.yonghu.bean.OrderCourse;

/**
 * 课程 详情页
 * @author zyc
 * 2015-2-12
 */
public class CourseDetailActivity extends BaseActivity implements OnClickListener{

	
	private Context mContext;
	TextView tv_first;
	TextView tv_second;
	TextView tv1 ,tv2;
	FrameLayout fl_content;
	private TitleBar titleBar ;
	ArrayList<Fragment> fragmentLists ;
	int screenWidth;
	View indicate_line;
	private List<Carousel> mCarousels = new ArrayList<Carousel>();
	private AbSlidingPlayView mAbSlidingPlayView;
	Button btZixun,btBuy;
	ImageView ivShare;
	String id,title;
	private  TitleBar titlebar;
	private OrderCourse orderCourse;
	private String[]deImg;
	/*** FragmentTabhost */
	private FragmentTabHost mTabHost;

	/** * 布局填充器*/
	private LayoutInflater mLayoutInflater;

	/*** Fragment数组界面*/
	private Class mFragmentArray[] = { CourseDetailJsFragment.class, CourseDetailCoachJsFragment.class};

	/*** 选修卡文字*/
	private String mTextArray[] = { "课程介绍", "教练介绍"};
	private LinearLayout ll_course_detail;
	/**
	 * 
	 * 
	 */
	CustomProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_detail);
		
		id = getIntent().getExtras().getString(Constants.KEY_ID);
		title = getIntent().getExtras().getString(Constants.KEY_TITLE);
		mContext = this;
		initView();
		initListener();
		getCarousel();
		configPlatforms();
		setShareContent();
		//initData();
		
		
		
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
	
	private void initView() {
		
		//scrollView_course_detail = (ScrollView) mView.findViewById(R.id.scrollView_course_detail);
		//设置 scrollView 定位到顶部
		ll_course_detail = (LinearLayout) findViewById(R.id.ll_course_detail);
		ll_course_detail.setFocusable(true);
		ll_course_detail.setFocusableInTouchMode(true);
		ll_course_detail.requestFocus();
		mLayoutInflater = LayoutInflater.from(this);
		// 找到TabHost
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		mAbSlidingPlayView = (AbSlidingPlayView) findViewById(R.id.mAbSlidingPlayView);
		mAbSlidingPlayView.setNavHorizontalGravity(Gravity.CENTER);
		// mAbSlidingPlayView.setParentHScrollView(menuLayout);
		mAbSlidingPlayView.startPlay();
		// 得到fragment的个数
		int count = mFragmentArray.length;
		for (int i = 0; i < count; i++) {
			// 给每个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(mTextArray[i]).setIndicator(getTabItemView(i));
			Bundle bundle = new Bundle(); 
			bundle.putString(Constants.KEY_ID, id);
			// 将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, mFragmentArray[i], bundle);
			// 设置Tab按钮的背景
			mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_detail_tab);
		}
		titlebar = (TitleBar) findViewById(R.id.index_titlebar);
		titlebar.setTitleBarTitle(title);
		
		
		ivShare = (ImageView) findViewById(R.id.title_share);
		btZixun = (Button) findViewById(R.id.bt_book);
		btBuy = (Button) findViewById(R.id.bt_buy);
		dialog = new CustomProgressDialog(this, R.string.laoding_tips,R.anim.frame2);
		dialog.show();
	}
	
	
	private void initListener() {
		ivShare.setOnClickListener(this);
		btZixun.setOnClickListener(this);
		btBuy.setOnClickListener(this);
	}
	
	
	// 获取 广告页
			private void getCarousel() {
				
				if(!NetTips.isNetTips(mContext)){
					//读取缓存 信息
					if(cache.getAsString("Carousel")!=null){
						String str = cache.getAsString("Carousel");
						com.alibaba.fastjson.JSONObject object1 = JSON.parseObject(str);
						com.alibaba.fastjson.JSONArray jsonArray1 = object1.getJSONArray("list");
						mCarousels.clear();
						mCarousels = JSON.parseArray(jsonArray1.toString(), Carousel.class);
						
						for (int i = 0; i < mCarousels.size(); i++) {
							final View mView = View.inflate(CourseDetailActivity.this,R.layout.play_view_item, null);
							ImageView mView2 = (ImageView) mView.findViewById(R.id.mPlayImage);
							imageLoader.displayImage(Constants.PIC_CODE+mCarousels.get(i).getAdvert_img(), mView2, options);
							mAbSlidingPlayView.addView(mView);
						}
					}
					return;
				}
					
				
				RequestParams params = new RequestParams(); //
				params.put("course_id",id);
				HttpUtils.post(Constants.COURSE_CAROUSEL, params,
						new JsonHttpResponseHandler() {
							public void onStart() {
								super.onStart();

							}

							public void onFailure(int statusCode,
									org.apache.http.Header[] headers,
									java.lang.Throwable throwable,
									org.json.JSONObject errorResponse) {
								dialog.dismiss();
								//读取缓存 信息
								if(cache.getAsString("Carousel")!=null){
									String str = cache.getAsString("Carousel");
									com.alibaba.fastjson.JSONObject object1 = JSON.parseObject(str);
									com.alibaba.fastjson.JSONArray jsonArray1 = object1.getJSONArray("list");
									mCarousels.clear();
									mCarousels = JSON.parseArray(jsonArray1.toString(), Carousel.class);
									
									for (int i = 0; i < mCarousels.size(); i++) {
										final View mView = View.inflate(CourseDetailActivity.this,R.layout.play_view_item, null);
										ImageView mView2 = (ImageView) mView.findViewById(R.id.mPlayImage);
										imageLoader.displayImage(Constants.PIC_CODE+mCarousels.get(i).getAdvert_img(), mView2, options);
										mAbSlidingPlayView.addView(mView);
									}
								}
								return;
							};

							public void onFinish() {
								dialog.dismiss();
							};

							public void onSuccess(int statusCode,
									org.apache.http.Header[] headers,
									org.json.JSONObject response) {
								try {
									response.toString();
									
									String state = response.getString("code");
									String info = response.getString("info");
									if (state.equals("1")) {
										com.alibaba.fastjson.JSONObject object = JSON.parseObject(response.toString());
										com.alibaba.fastjson.JSONArray jsonArray = object.getJSONArray("list");
										//deImg = (String[]) jsonArray.t;
										for (int i = 0; i < jsonArray.size(); i++) {
											final View mView = View.inflate(
													CourseDetailActivity.this,
													R.layout.play_view_item, null);
											ImageView mView2 = (ImageView) mView.findViewById(R.id.mPlayImage);
											imageLoader.displayImage(Constants.PIC_CODE+jsonArray.get(i).toString(), mView2, options);
											mAbSlidingPlayView.addView(mView);
										}
										
									} else {

										AbToastUtil.showToast(mContext, R.string.data_tips);
									}
								} catch (Exception e) {

								}
							};
						});
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
		case R.id.bt_book:
			
			if(!YoutiApplication.getInstance().myPreference.getHasLogin()){
				IntentJumpUtils.nextActivity(LoginActivity.class, this, null, Constants.REQUEST_CODE_DETAIL_COURSE);
				return;
			}
			bookFlag();
			break;

		case R.id.bt_buy:
			if(!YoutiApplication.getInstance().myPreference.getHasLogin()){
				IntentJumpUtils.nextActivity(LoginActivity.class, this, null, Constants.REQUEST_CODE_DETAIL_COURSE);
				return;
			}
			submitOrder();
			break;
		case R.id.title_share:
			//Utils.showToast(CourseDetailActivity.this, "分享该页面");
			if(!YoutiApplication.getInstance().myPreference.getHasLogin()){
				IntentJumpUtils.nextActivity(LoginActivity.class, this, null, Constants.REQUEST_CODE_DETAIL_COURSE);
				return;
			}
			 mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
	                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,SHARE_MEDIA.SINA);
	         mController.openShare(CourseDetailActivity.this, false);
			break;
		default:
			break;
		}
	}
	
	private void bookFlag(){
		
		
		RequestParams params = new RequestParams(); //
		params.put("user_id",YoutiApplication.getInstance().myPreference.getUserId());
		params.put("course_id",id);
		HttpUtils.post(Constants.BOOK_COURSE_FLAG, params,
				new JsonHttpResponseHandler() {
					public void onStart() {
						super.onStart();
					}

					public void onFailure(int statusCode,
							org.apache.http.Header[] headers,
							java.lang.Throwable throwable,
							org.json.JSONObject errorResponse) {
					};
					
					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						super.onFinish();
					}

					public void onSuccess(int statusCode,
							org.apache.http.Header[] headers,
							org.json.JSONObject response) {
						try {
							
							/* code：402该用户没有购买该俱乐部的通卡
   								code：403约课次数已用完
   								code：0约课失败
   								code：1约课成功
							*/
							String state = response.getString("code");
							if(state.equals("1")){
								IntentJumpUtils.nextActivity(MyCourseActivity.class, CourseDetailActivity.this, null);
							}else if(state.equals("402")){
								Utils.showToast(mContext, "您没有购买该俱乐部的通卡");
							}else if(state.equals("403")){
								Utils.showToast(mContext, "您的约课次数已用完");
							}else if(state.equals("0")){
								Utils.showToast(mContext, "约课失败请稍后重试");
							}
							
						} catch (Exception e) {
						}
					};
				});
	}
	
	/**
	* @Title: submitOrder 
	* @Description: TODO(提交课程 订单) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void submitOrder(){
		RequestParams params = new RequestParams(); //
		params.put("user_id",YoutiApplication.getInstance().myPreference.getUserId());
		params.put("course_id",id);
		HttpUtils.post(Constants.ORDER_COURSE, params,
				new JsonHttpResponseHandler() {
					public void onStart() {
						super.onStart();
						dialog.show();
					}

					public void onFailure(int statusCode,
							org.apache.http.Header[] headers,
							java.lang.Throwable throwable,
							org.json.JSONObject errorResponse) {
						dialog.dismiss();
					};
					
					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						super.onFinish();
						dialog.dismiss();
					}

					public void onSuccess(int statusCode,
							org.apache.http.Header[] headers,
							org.json.JSONObject response) {
						try {
							
							/*code：400教练id为空
							code：401用户id为空
							code：402单价为空
							code：403时长为空
							code：404上课方式为空
							code:0下单失败
							code:1下单成功
							*/
							String state = response.getString("code");
							if(state.equals("1")){
								com.alibaba.fastjson.JSONObject object = JSON.parseObject(response.toString());
								JSONObject jsonObject = object.getJSONObject("list");
								orderCourse = JSON.parseObject(jsonObject.toString(), OrderCourse.class);
								Bundle b = new Bundle();
								b.putInt("orderType", Constants.REQUEST_CODE_PAY_COURSE);
								b.putSerializable(Constants.KEY_ORDER_COURSE, orderCourse);
								IntentJumpUtils.nextActivity(OrderCoachActivity.class, CourseDetailActivity.this, b);
							}
						} catch (Exception e) {
						}
					};
				});
	}
	
	//友盟分享
			// 首先在您的Activity中添加如下成员变量
			final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	private int height;
			
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
			        mController.setShareContent("优体教练，三分钟让你淘到最好的教练。http://wtapp.yoti.cn/index.php?m=Share&a=socialdetail&social_id=80");


			        UMImage localImage = new UMImage(this, R.drawable.sp_head);
			        UMImage urlImage = new UMImage(this,
			                "http://www.umeng.com/images/pic/social/integrated_3.png");
			        // UMImage resImage = new UMImage(getActivity(), R.drawable.icon);

			        
			        WeiXinShareContent weixinContent = new WeiXinShareContent();
			        weixinContent.setShareContent("优体教练，三分钟让你淘到最好的教练。http://wtapp.yoti.cn/index.php?m=Share&a=socialdetail&social_id=80");
			        weixinContent.setTitle("优体教练");
			        weixinContent.setTargetUrl("http://wtapp.yoti.cn/index.php?m=Share&a=socialdetail&social_id=80");
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
				        Toast.makeText(CourseDetailActivity.this, "分享成功.......", Toast.LENGTH_SHORT)
				            .show();
				      } else {
				        Toast.makeText(CourseDetailActivity.this,
				            "分享失败 : error code : " + stCode, Toast.LENGTH_SHORT)
				            .show();
				      }
				    }
				  };
				  mController.registerListener(mSnsPostListener);
				  

			        // 设置朋友圈分享的内容
			        CircleShareContent circleMedia = new CircleShareContent();
			        circleMedia.setShareContent("优体教练，三分钟让你淘到最好的教练。http://wtapp.yoti.cn/index.php?m=Share&a=socialdetail&social_id=80");
			        circleMedia.setTitle("优体教练");
			        circleMedia.setShareMedia(urlImage);
			        // circleMedia.setShareMedia(uMusic);
			        // circleMedia.setShareMedia(video);
			        circleMedia.setTargetUrl("http://wtapp.yoti.cn/index.php?m=Share&a=socialdetail&social_id=80");
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
//			        qzone.setShareMedia(uMusic);
			        mController.setShareMedia(qzone);


			        QQShareContent qqShareContent = new QQShareContent();
//			        qqShareContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能 -- QQ");
			        qqShareContent.setTitle("hello, title");
			        qqShareContent.setTargetUrl("http://wtapp.yoti.cn/index.php?m=Share&a=socialdetail&social_id=80");
			        mController.setShareMedia(qqShareContent);





			        SinaShareContent sinaContent = new SinaShareContent();
			        sinaContent.setShareContent("优体教练，三分钟让你淘到最好的教练。http://wtapp.yoti.cn/index.php?m=Share&a=socialdetail&social_id=80");
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

		
	
}
