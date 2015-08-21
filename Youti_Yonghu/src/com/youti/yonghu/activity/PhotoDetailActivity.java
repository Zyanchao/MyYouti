package com.youti.yonghu.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
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
import com.youti.utils.HttpUtils;
import com.youti.utils.ScreenUtils;
import com.youti.utils.Utils;
import com.youti.view.ActionSheet;
import com.youti.view.ActionSheet.ActionSheetListener;
import com.youti.view.CircleImageView1;
import com.youti.view.CustomProgressDialog;
import com.youti.view.MGridView;
import com.youti.view.TitleBar;
import com.youti.yonghu.bean.InfoBean;
import com.youti.yonghu.bean.PhotoDetail;
import com.youti.yonghu.bean.PhotoDetail.CommentItem;
import com.youti.yonghu.bean.PhotoDetail.ZanItem;

public class PhotoDetailActivity extends FragmentActivity implements ActionSheetListener{
	protected static final int REFRESH = 0;
	protected static final int FAILURE = 1;
	ListView listView;
	ImageView gaoyuanyuan,iv_share,iv_delete,zan_heart;
	TextView tv_zanNum,tv_pinglunNum;
	List<CommentItem> list2 =new ArrayList<CommentItem>();
	TitleBar titleBar;
	String urldetail ="http://112.126.72.250/ut_app/index.php?m=Community&a=detailshow";
	ArrayList<String> image_url=new ArrayList<String>();
	ScrollView sv;
	LinearLayout ll,ll_comment,ll_pinglun,ll_zan;
	ViewPager viewPager;
	EditText et_comment;
	Button comment_send;
	int height;
	String comment;
	CircleImageView1 civ_head;
	TextView tv_bigname;
	MGridView mgv;
	String userName;
	String userHeadImgPath;
	DisplayImageOptions options;
	boolean isLogin = YoutiApplication.getInstance().myPreference.getHasLogin();
	//private DisplayImageOptions options;
	
	public final String mPageName ="PhotoDetailActivity";
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
	//友盟分享
		// 首先在您的Activity中添加如下成员变量
		final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	private Dialog createProgressBarDialog;
		
		
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
		        qqSsoHandler.setTargetUrl("http://wtapp.yoti.cn/index.php?m=Share&a=socialdetail&social_id=80");
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
			        Toast.makeText(PhotoDetailActivity.this, "分享成功.......", Toast.LENGTH_SHORT)
			            .show();
			      } else {
			        Toast.makeText(PhotoDetailActivity.this,
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
//		        qzone.setShareMedia(uMusic);
		        mController.setShareMedia(qzone);


		        QQShareContent qqShareContent = new QQShareContent();
//		        qqShareContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能 -- QQ");
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

	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_photodetail);
		
		if(getIntent()!=null){
			social_id = getIntent().getStringExtra("social_id");
			author_id = getIntent().getStringExtra("author_id");
			//System.out.println(social_id);
		}
		iv_delete= (ImageView) findViewById(R.id.iv_delete);
		user_id = ((YoutiApplication)getApplication()).myPreference.getUserId();
		userName=((YoutiApplication)getApplication()).myPreference.getUserName();
		userHeadImgPath=((YoutiApplication)getApplication()).myPreference.getHeadImgPath();
		
		if(user_id.equals(author_id)){
			iv_delete.setVisibility(View.VISIBLE);
		}
		
		iv_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setTheme(R.style.ActionSheetStyleIOS7);
				showActionSheet();
			}
		});
		
		options = new DisplayImageOptions.Builder()    
        .showStubImage(R.drawable.default_pic)          // 设置图片下载期间显示的图片    
        .showImageForEmptyUri(R.drawable.default_pic)  // 设置图片Uri为空或是错误的时候显示的图片    
        .showImageOnFail(R.drawable.default_pic)       // 设置图片加载或解码过程中发生错误显示的图片        
        .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中    
        .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中    
        .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片    
        .build();
		
		listView =(ListView) findViewById(R.id.listview);
		sv=(ScrollView) findViewById(R.id.sv);
		ll=(LinearLayout) findViewById(R.id.ll);
		ll_comment=(LinearLayout) findViewById(R.id.ll_comment);
		ll_pinglun=(LinearLayout) findViewById(R.id.ll_pinglun);
		ll_zan=(LinearLayout) findViewById(R.id.ll_zan);
		et_comment=(EditText) findViewById(R.id.et_comment);
		comment_send=(Button) findViewById(R.id.comment_send);
		tv_bigname=(TextView) findViewById(R.id.tv_name);
		civ_head=(CircleImageView1) findViewById(R.id.civ_head);
		mgv=(MGridView) findViewById(R.id.mgv);
		tv_zanNum=(TextView) findViewById(R.id.zan);
		zan_heart=(ImageView) findViewById(R.id.zan_img);
		tv_pinglunNum=(TextView) findViewById(R.id.pinglun);
		
		configPlatforms();
		setShareContent();
		
		

		comment_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(user_id.equals("")){
					
					Intent intent =new Intent(PhotoDetailActivity.this,LoginActivity.class);
					startActivity(intent);
					return;
				}
				comment=et_comment.getText().toString().trim();
				if(TextUtils.isEmpty(comment)){
					Utils.showToast(PhotoDetailActivity.this, "请输入评论内容");
					return;
				}
				
				//评论请求
				initData(comment);
			}

			
		});
		
		viewPager=(ViewPager) findViewById(R.id.view_pager);
		height=ll.getTop();
		
		
		sv.smoothScrollTo(0, height);
		listView.setFocusable(false);
		
		iv_share=(ImageView) findViewById(R.id.iv_share);
		iv_share.setVisibility(View.VISIBLE);
	//	gaoyuanyuan=(ImageView) findViewById(R.id.gaoyuanyuan);
		titleBar =(TitleBar) findViewById(R.id.index_titlebar);
		titleBar.setTitleBarTitle("照片详情");
		titleBar.setSearchGone();
		titleBar.setShareGone(false);
		
		initDate();
		iv_share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//分享
				 mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
		                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,SHARE_MEDIA.SINA);
		            mController.openShare(PhotoDetailActivity.this, false);
			}
		});
		ll_zan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(user_id.equals("")){
					Intent intent =new Intent(PhotoDetailActivity.this,LoginActivity.class);
					startActivity(intent);
					return;
				}
				if(!YoutiApplication.getInstance().myPreference.getHasLogin()){
					Intent intent =new Intent(PhotoDetailActivity.this,LoginActivity.class);
					startActivity(intent);
					return;
				}
				sendPraise(v);
				
			}
		});
		
		ll_pinglun.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(user_id.equals("")){
					Intent intent =new Intent(PhotoDetailActivity.this,LoginActivity.class);
					startActivity(intent);
					return;
				}
				ll_comment.setVisibility(View.VISIBLE);
				InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});
		
		
	}
	

	/**
	 * 发送点赞请求，本地做+1或者-1操作
	 */
	protected void sendPraise(View v) {
		
		
		LinearLayout ll=(LinearLayout) v;
		ImageView iv = (ImageView) ll.findViewById(R.id.zan_img);
		TextView tv=(TextView) ll.findViewById(R.id.zan);
		if(zanList==null){
			//当没有发现该用户的id，那么就创建一个该户的ZanItem对象，然后将该用户的信息，封装到点赞集合中，然后更新gideview
			PhotoDetail vdb =new PhotoDetail();
			PhotoDetail.ZanItem pi =vdb.new ZanItem();
			pi.setUser_id(user_id);
			pi.setUser_type("2");
			String img =YoutiApplication.getInstance().myPreference.getHeadImgPath();
			if(isLogin){
				if(TextUtils.isEmpty(img)){
					pi.setUser_img("");
				}else{
					if(img.startsWith("http://112.126.72.250/ut_app")){
						pi.setUser_img(img.substring(28, img.length()));					
					}else{
						pi.setUser_img(img);
					}
				}
			}else{
				pi.setUser_img("");
			}
			//Toast.makeText(PhotoDetailActivity.this, "地址"+img.substring(28, img.length()), 1).show();
			zanList=new ArrayList<ZanItem>();
			zanList.add(0,pi);	
			if(mga==null){
				mga=new MyGridAdapter();
				mgv.setAdapter(mga);						
			}else{
				mga.notifyDataSetChanged();
			}
			iv.setBackgroundResource(R.drawable.home_heart_h);
			tv.setText(zanList.size()+"");
			requestPraise(user_id);
			return;
		}
		
		//本地做+1-1操作
		//判断点赞集合中是否有该用户的id，如果没有就取消。如果有就点赞
		List<ZanItem> listcopy =new ArrayList<ZanItem>();
		
		int size=zanList.size();	
		for(int i=0;i<size;i++){
			listcopy.add(zanList.get(i));
		}
		for(int i=0;i<size;i++){
			//如果发现当前id已经存在在点赞列表中，就移除掉该用户。然后发送网络请求，然后结束该方法
			if(listcopy.get(i).getUser_id().equals(user_id)){	
				
				iv.setBackgroundResource(R.drawable.home_heart);
				zanList.remove(i);
				tv.setText((zanList.size())+"");
				mga.notifyDataSetChanged();
				requestPraise(user_id);
				return;
			}			
		}
		//当没有发现该用户的id，那么就创建一个该户的ZanItem对象，然后将该用户的信息，封装到点赞集合中，然后更新gideview
		PhotoDetail vdb =new PhotoDetail();
		PhotoDetail.ZanItem pi =vdb.new ZanItem();
		pi.setUser_id(user_id);
		pi.setUser_type("2");
		String img =YoutiApplication.getInstance().myPreference.getHeadImgPath();
		if(isLogin){
			if(TextUtils.isEmpty(img)){
				pi.setUser_img("");
			}else{
				if(img.startsWith("http://112.126.72.250/ut_app")){
					pi.setUser_img(img.substring(28, img.length()));					
				}else{
					pi.setUser_img(img);
				}
			}
		}else{
			pi.setUser_img("");
		}
		zanList.add(0,pi);		
		mga.notifyDataSetChanged();	
		iv.setBackgroundResource(R.drawable.home_heart_h);
		tv.setText((zanList.size())+"");
		requestPraise(user_id);
	}
	
	private void requestPraise(String user_id) {
		//发送请求到服务器
		String urlString = "http://112.126.72.250/ut_app/index.php?m=Community&a=praise_social";
		RequestParams params =new RequestParams();
		params.put("user_id", user_id);
		params.put("social_id", social_id);
		params.put("user_type", "2");
		HttpUtils.post(urlString, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson =new Gson();
				InfoBean fromJson = gson.fromJson(arg2, InfoBean.class);
				Message msg =Message.obtain();
				if(fromJson.code.equals("1")){
					//点赞成功
					Utils.showToast(PhotoDetailActivity.this, "点赞成功");
				}else if(fromJson.code.equals("0")){
					//点赞取消成功
					Utils.showToast(PhotoDetailActivity.this, "点赞取消成功");
				}
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Utils.showToast(PhotoDetailActivity.this, "网络连接异常");
			}
		});
	}
	protected void delete() {
		createProgressBarDialog = Utils.createProgressBarDialog(PhotoDetailActivity.this, "正在删除...");
		createProgressBarDialog.show();
		RequestParams params =new RequestParams();
		params.put("user_id", user_id);
		params.put("social_id", social_id);
		params.put("user_type", "2");
		HttpUtils.post("http://112.126.72.250/ut_app/index.php?m=Community&a=delissueShow", params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				createProgressBarDialog.dismiss();
				Gson gson =new Gson();
				InfoBean fromJson2 = gson.fromJson(arg2, InfoBean.class);
				if(fromJson2.code.equals("1")){
					Utils.showToast(PhotoDetailActivity.this, "删除成功");
					finish();
				}else{
					Utils.showToast(PhotoDetailActivity.this, "删除失败");
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				createProgressBarDialog.dismiss();
				Utils.showToast(PhotoDetailActivity.this, "网络连接异常");
			}
		});
	}


	/**
	 * 发送评论
	 * @param comment2 
	 */
	protected void initData(String comment2) {
		
		RequestParams params =new RequestParams();
		params.put("user_id", user_id);
		params.put("social_id", social_id);
		params.put("user_name", userName);
		params.put("user_img", userHeadImgPath);
		params.put("content", comment2);
		params.put("user_type", "2");
		
		String urlcomment="http://112.126.72.250/ut_app/index.php?m=Community&a=social_comment";
		HttpUtils.post(urlcomment, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				System.out.println("发送评论成功");
				Message msg =Message.obtain();
				msg.what=REFRESH;
				handler.sendMessage(msg);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				System.out.println("发送评论获取网络失败");
				Message msg= Message.obtain();
				msg.what=FAILURE;
				handler.sendMessage(msg);
			}
		});
	}



	class myViewPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return image_url.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			View view =View.inflate(PhotoDetailActivity.this, R.layout.item_photoshow, null);
			ImageView iv =(ImageView) view.findViewById(R.id.photodetail);
			
			iv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent =new Intent(PhotoDetailActivity.this,PhotoShowActivity.class);
					intent.putStringArrayListExtra("photo_list", image_url);
					intent.putExtra("location", position+"");
					startActivity(intent);
				}
			});
			container.addView(view);
			//bitmapUtils.display(iv, image_url.get(position));
			//iv.setBackgroundResource(R.drawable.empty_photo);
			ImageLoader.getInstance().displayImage("http://112.126.72.250/ut_app"+image_url.get(position), iv);
			
			int width = iv.getWidth();
			int height2 = iv.getHeight();
			
			
			
			
			return view;
		}
		
		
		
	}
	private PhotoDetail fromJson;

	private final int LOADDATA=1001;
	private List<ZanItem> zanList;
	List li=new ArrayList();
	public int shortcut;
	Handler handler =new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOADDATA:
				et_comment.setHint("发表您的评论");
				if(fromJson.list!=null){
					
					ImageLoader.getInstance().displayImage(fromJson.list.user_img, civ_head);
					image_url=fromJson.list.json_img;
					list2=fromJson.list.comment;
					//设置好点赞个数
					tv_zanNum.setText(fromJson.list.praise_num+"");
					tv_bigname.setText(fromJson.list.user_name);
					//设置点赞图标是否为红色
					if(fromJson.list.is_zan.equals("1")){
						zan_heart.setBackgroundResource(R.drawable.home_heart_h);
						
					}else{
						zan_heart.setBackgroundResource(R.drawable.home_heart);
					}
					//设置评论个数
					tv_pinglunNum.setText(fromJson.list.comment_num+"");
					
					zanList = fromJson.list.praise;
					if(zanList!=null){
						shortcut = zanList.size();
						if(zanList.size()>10){
							shortcut=10;
						}
						
						if(mga==null){
							mga=new MyGridAdapter();
							mgv.setAdapter(mga);						
						}else{
							mga.notifyDataSetChanged();
						}
						
					}
					if(image_url!=null){
						
						viewPager.setAdapter(new myViewPagerAdapter());
					}
					if(list2!=null){
						listView.setAdapter(new MyAdapter());
						
					}
					ScreenUtils.setListViewHeightBasedOnChildren(listView);
					listView.setFocusable(false);
				}
				
				
				break;
			case REFRESH:
				initDate();
				InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				et_comment.setText("");
				Utils.showToast(PhotoDetailActivity.this, "评论成功");
				break;
			case FAILURE:
				Utils.showToast(PhotoDetailActivity.this, "请求网络失败，请稍后再试");
			default:
				break;
			}
		};
	};
	MyGridAdapter mga;
	
	class MyGridAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(zanList==null){
				return 0;
			}else{
				
				return zanList.size();
			}
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if(zanList==null){
				return null;
			}
			View view =View.inflate(PhotoDetailActivity.this, R.layout.item_image1, null);
			CircleImageView1 ci=(CircleImageView1) view.findViewById(R.id.iv);
			if(zanList.get(position).user_img.startsWith("http:")){
				ImageLoader.getInstance().displayImage(zanList.get(position).user_img, ci);
			}else{
				
				ImageLoader.getInstance().displayImage("http://112.126.72.250/ut_app"+zanList.get(position).user_img, ci);
			}
			
			
				//用户
				view.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						
//						Utils.showToast(PhotoDetailActivity.this, zanList.get(position).user_type);
						if(zanList.get(position).user_type.equals("1")){
							//教练
							
							Intent intent =new Intent(PhotoDetailActivity.this,CoachDetailActivity.class);
							intent.putExtra(Constants.KEY_ID, zanList.get(position).user_id);
							intent.putExtra(Constants.KEY_CHAT_AVATAR, zanList.get(position).user_img);
							intent.putExtra(Constants.KEY_CHAT_USERNAME, "");
							intent.putExtra(Constants.KEY_CHAT_TEL, zanList.get(position).coach_tel);
							startActivity(intent);
						}else if(zanList.get(position).user_type.equals("2")){
							Intent intent =new Intent(PhotoDetailActivity.this,OtherPersonCenterActivity.class);
							intent.putExtra("user_id", zanList.get(position).user_id);
							startActivity(intent);
							
							
						}else{
							
						}
						
						
						
						
					}
				});
		
			
			
			return view;
		}
		
	}
	private ImageLoader instance;
	private String social_id;
	private String author_id;	
	private String user_id;
	private CustomProgressDialog waitDialog;
	/**
	 * 照片详情获取数据
	 */
	private void initDate() {
		RequestParams params =new RequestParams();
		SharedPreferences sp =getSharedPreferences("config", MODE_PRIVATE);
		waitDialog = new CustomProgressDialog(PhotoDetailActivity.this, R.string.laoding_tips,R.anim.frame2);
		waitDialog.show();
		params.put("social_id", social_id);
		params.put("user_id", user_id);
		params.put("user_type", "2");
		HttpUtils.post(urldetail, params, new TextHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				waitDialog.dismiss();
				try{
					Gson gson =new Gson();
					fromJson = gson.fromJson(arg2, PhotoDetail.class);					
					System.out.println(arg2.toString());	
					if(fromJson!=null){
						Message msg=Message.obtain();
						msg.what=LOADDATA;
						handler.sendMessage(msg);						
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				waitDialog.dismiss();
				Utils.showToast(PhotoDetailActivity.this, "网络连接异常");
			}
		});
	}

	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return list2.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view ;
			ViewHolder vh;
			if(convertView==null){
				view =View.inflate(PhotoDetailActivity.this, R.layout.item_pinglun, null);
				vh=new ViewHolder();
				vh.tv_content=(TextView) view.findViewById(R.id.tv_content);
				vh.tv_name=(TextView) view.findViewById(R.id.tv_name);
				vh.civ=(CircleImageView1) view.findViewById(R.id.iv);	
				vh.tv_time=(TextView) view.findViewById(R.id.tv_time);
				view.setTag(vh);
			}else{
				view=convertView;
				vh=(ViewHolder) view.getTag();
			}
			vh.tv_content.setText(list2.get(list2.size()-position-1).comment_content);
			vh.tv_name.setText(list2.get(list2.size()-position-1).user_name);
			ImageLoader.getInstance().displayImage(list2.get(list2.size()-position-1).user_img, vh.civ);    
			SimpleDateFormat format =  new SimpleDateFormat("MM-dd HH:mm");  
		    Long time= Long.parseLong(list2.get(list2.size()-position-1).comment_time+"000");
		    String d = format.format(time);  
		   
			vh.tv_time.setText(d);
			/*vh.civ.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent =new Intent(PhotoDetailActivity.this,OtherPersonCenterActivity.class);
					intent.putExtra("user_id", list2.get(list2.size()-position-1).user_id);
					startActivity(intent);
				}
			});*/

				
			return view;
		}
		
	}
	
	class ViewHolder{
		TextView tv_name;
		TextView tv_content;
		CircleImageView1 civ;
		TextView tv_time;
		
	}
	public void showActionSheet() {
		ActionSheet.createBuilder(this, getSupportFragmentManager())
				.setCancelButtonTitle("Cancel")
				.setOtherButtonTitles("删除该相册")
				.setCancelableOnTouchOutside(true).setListener(this).show();
	}
	@Override
	public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
		
	}
	
	@Override
	public void onOtherButtonClick(ActionSheet actionSheet, int index) {
		delete();
	}
}
