package com.youti.yonghu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.youti_geren.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.youti.view.TitleBar;
/**
 * 更多页面条目点击进入的页面
 * @author Administrator
 *
 */
public class SimpleActivity extends FragmentActivity implements OnClickListener {
	FrameLayout fl_container;
	TitleBar titleBar;
	Button iv_weixin,iv_xinlang,iv_qq,iv_friendcircle;
	
	public final String mPageName ="SimpleActivity";
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
		setContentView(R.layout.layout_recommend);
		titleBar =(TitleBar) findViewById(R.id.index_titlebar);
		titleBar.setSearchGone();
		titleBar.setShareGone(false);
		titleBar.setTitleBarTitle("向朋友推荐优体");
		
		iv_weixin=(Button) findViewById(R.id.iv_weixin);
		iv_xinlang=(Button) findViewById(R.id.iv_weibo);
		iv_qq=(Button) findViewById(R.id.iv_qq);
		iv_friendcircle=(Button) findViewById(R.id.iv_friendcircle);
		
		iv_weixin.setOnClickListener(this);
		iv_xinlang.setOnClickListener(this);
		iv_qq.setOnClickListener(this);
		iv_friendcircle.setOnClickListener(this);
		
		configPlatforms();
		setShareContent();
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
	        qqSsoHandler.setTargetUrl("http://jiaolian.yoti.cn/web/download.php");
	        qqSsoHandler.addToSocialSDK();

	       
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
	        mController.setShareContent("今夏最火的运动健身APP，大家一起来体验吧！");


	        UMImage localImage = new UMImage(this, R.drawable.sp_head);
	        UMImage urlImage = new UMImage(this,
	                "http://wtapp.yoti.cn/show/more_logo.png");

	        
	        WeiXinShareContent weixinContent = new WeiXinShareContent();
	        weixinContent.setShareContent("今夏最火的运动健身APP，大家一起来体验吧！");
	        weixinContent.setTitle("优体教练用户端");
	        weixinContent.setTargetUrl("http://jiaolian.yoti.cn/web/download.php");
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
		        Toast.makeText(SimpleActivity.this, "分享成功.......", Toast.LENGTH_SHORT)
		            .show();
		      } else {
		        Toast.makeText(SimpleActivity.this,
		            "分享失败 : error code : " + stCode, Toast.LENGTH_SHORT)
		            .show();
		      }
		    }
		  };
		  mController.registerListener(mSnsPostListener);
		  

	        // 设置朋友圈分享的内容
	        CircleShareContent circleMedia = new CircleShareContent();
	        circleMedia.setShareContent("今夏最火的运动健身APP，大家一起来体验吧！");
	        circleMedia.setTitle("优体教练用户端");
	        circleMedia.setShareMedia(urlImage);
	        circleMedia.setTargetUrl("http://jiaolian.yoti.cn/web/download.php");
	        mController.setShareMedia(circleMedia);

	     

	        UMImage qzoneImage = new UMImage(this,
	                "http://wtapp.yoti.cn/show/more_logo.png");
	        qzoneImage
	                .setTargetUrl("http://jiaolian.yoti.cn/web/download.php");


	        QQShareContent qqShareContent = new QQShareContent();
	        qqShareContent.setTitle("优体教练用户端");
	        qqShareContent.setShareContent("今夏最火的运动健身APP，大家一起来体验吧！");
	        qqShareContent.setTargetUrl("http://jiaolian.yoti.cn/web/download.php");
	        qqShareContent.setShareMedia(urlImage);
	        mController.setShareMedia(qqShareContent);


	        SinaShareContent sinaContent = new SinaShareContent();
	        sinaContent.setShareContent("今夏最火的运动健身APP，大家一起来体验吧！");
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
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_weixin:
				mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN);
				 mController.postShare(SimpleActivity.this, SHARE_MEDIA.WEIXIN,
							mShareListener);
				break;
			case R.id.iv_friendcircle:
				mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN_CIRCLE);
				mController.postShare(SimpleActivity.this, SHARE_MEDIA.WEIXIN_CIRCLE,
						mShareListener);
				break;
			case R.id.iv_qq:
				mController.getConfig().setPlatforms(SHARE_MEDIA.QQ);
				mController.postShare(SimpleActivity.this, SHARE_MEDIA.QQ,
						mShareListener);
				break;
			case R.id.iv_weibo:
				mController.getConfig().setPlatforms(SHARE_MEDIA.SINA);
				mController.postShare(SimpleActivity.this, SHARE_MEDIA.SINA,
						mShareListener);
				break;
			default:
				break;
			}
          //  mController.openShare(SimpleActivity.this, false);
           
		}
		/**
		 * 分享监听器
		 */
		SnsPostListener mShareListener = new SnsPostListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int stCode,
					SocializeEntity entity) {
				if (stCode == 200) {
					Toast.makeText(SimpleActivity.this, "分享成功", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(SimpleActivity.this,
							"分享失败 : error code : " + stCode, Toast.LENGTH_SHORT)
							.show();
				}
			}
		};

		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {

			UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
					resultCode);
			if (ssoHandler != null) {
				ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			}

			super.onActivityResult(requestCode, resultCode, data);
		}
}
