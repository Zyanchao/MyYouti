package com.youti.appConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;

import com.ab.util.AbLogUtil;
import com.baidu.location.LocationClient;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.youti.chat.DemoHXSDKHelper;
import com.youti.chat.domain.User;
import com.youti.utils.MyPreference;
import com.youti.yonghu.download.DownloadMovieItem;

public class YoutiApplication  extends Application{

	
	private  static YoutiApplication instance = null;
	public static final String TAG = "yoti_exception";
	public static Context mContext;
	//public CoachSearchInfo coachSearchInfo = new CoachSearchInfo();
	public Bitmap tempHeadBitmap = null;
	// login user name
	public final String PREF_USERNAME = "username";
	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";
	public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();
	public int orderPageIndex = 0;
	public final String SCODE = "yoti";
	public final int APP_TYPE = 1;
	public final int OS_TYPE = 0;
	private double lon = 0, lat = 0;//当前位置 
	private String uuid = "";
	private final String UTYPE = "0";
	public LocationClient mLocationClient;
	
	public String getUtype(){
		return UTYPE;
	}
	public boolean checkUpdateVersion = true;
	public MyPreference myPreference;
	public String getUuid() {
		if(uuid.length()<1){
			uuid = UUID.randomUUID().toString();
		}
		return uuid;
	}
	
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		if(mContext == null){
			mContext = this;
		}
	        /**
	         * this function will initialize the HuanXin SDK
	         * 
	         * @return boolean true if caller can continue to call HuanXin related APIs after calling onInit, otherwise false.
	         * 
	         * 环信初始化SDK帮助函数
	         * 返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
	         * 
	         * for example:
	         * 例子：
	         * 
	         * public class DemoHXSDKHelper extends HXSDKHelper
	         * 
	         * HXHelper = new DemoHXSDKHelper();
	         * if(HXHelper.onInit(context)){
	         *     // do HuanXin related work
	         * }
	         */
	        hxSDKHelper.onInit(mContext);
	        EMChatOptions      chatOptions = EMChatManager.getInstance().getChatOptions();
	        chatOptions.setNotifyBySoundAndVibrate(true);
	        chatOptions.setNoticeBySound(true);
	        chatOptions.setNoticedByVibrate(true);
	        chatOptions.setUseSpeaker(true);
	        chatOptions.setShowNotificationInBackgroud(true) ;
	        myPreference = MyPreference.getInstance(this);
	        
		// 处理程序异常退出
	        /**
	         * 如果启动该方法，友盟错误统计就会被覆盖
	         */
		//Thread.currentThread().setUncaughtExceptionHandler(new MyExceptionHandler());
		/**
		 * 关闭友盟的activity自动统计功能。
		 */
		MobclickAgent.openActivityDurationTrack(false);
		/**
		 * 发送模式
		 */
		MobclickAgent.updateOnlineConfig( mContext );
		//MobclickAgent.setDebugMode( true );
		myPreference = MyPreference.getInstance(this);
		initImageLoader(getApplicationContext());
		
		File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "yoti/Cache");

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions 推荐
        .diskCacheExtraOptions(480, 800, null) //.推荐diskCacheExtraOptions(480, 800, null)
        .threadPoolSize(3) // default 推荐1-5
        .threadPriority(Thread.NORM_PRIORITY - 2) // default
        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
        .denyCacheImageMultipleSizesInMemory()  //设置内存缓存不允许缓存一张图片的多个尺寸，默认允许。
        .memoryCache(new LruMemoryCache(2 * 1024 * 1024)) //使用强引用的缓存使用它，不过推荐使用weak与strong引用结合的UsingFreqLimitedMemoryCache或者使用全弱引用的WeakMemoryCache
        .memoryCacheSize(2 * 1024 * 1024)
        .memoryCacheSizePercentage(13) // default
        .discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径  
        .diskCacheSize(50 * 1024 * 1024)
        .diskCacheFileCount(100)
        .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
        .imageDownloader(new BaseImageDownloader(mContext)) // default
        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
        .writeDebugLogs()
        .build();
		ImageLoader.getInstance().init(config);
		//initLocation();
	}
	
	
	public void logout() {
		// TODO Auto-generated method stub
//		if(YotiSocket.hasInstance){
//			YotiSocket.getInstance(this,myPreference.getUserId(), myPreference.getToken()).stopMsgSocket();
//		}
		myPreference.setHasLogin(false);
		myPreference.setUserId("0");
		myPreference.setToken("");
		myPreference.setHeadImgPath("");
		myPreference.setHistory1("");
		myPreference.setHistory2("");
		myPreference.setLoginName("");
		myPreference.setTelNumber("");
		myPreference.setUserId("0");
		myPreference.setUserName("");
		myPreference.setUserSex("0");
	}
	public static String getTag() {
		return TAG;
	}
	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}
	
	
	
	public static void initImageLoader(Context context) {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
         .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中  
         .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中  
		 .build();

		@SuppressWarnings("deprecation")
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).defaultDisplayImageOptions(defaultOptions)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		        ImageLoader.getInstance().init(config);
	}
	public static YoutiApplication getInstance() {
		return instance;
	}
	
	/**
	 * 获取内存中好友user list
	 *
	 * @return
	 */
    public Map<String, User> getContactList() {
	    return hxSDKHelper.getContactList();
	}

	/**
	 * 设置好友user list到内存中
	 *
	 * @param contactList
	 */
	public void setContactList(Map<String, User> contactList) {
	    hxSDKHelper.setContactList(contactList);
	}

	
	public void setContact(User user) {
	    hxSDKHelper.setContact(user);
	}
	/**
	 * 获取当前登陆用户名
	 *
	 * @return
	 */
	public String getUserName() {
	    return hxSDKHelper.getHXId();
	}

	/**
	 * 获取密码
	 *
	 * @return
	 */
	public String getPassword() {
		return hxSDKHelper.getPassword();
	}

	/**
	 * 设置用户名
	 *
	 * @param user
	 */
	public void setUserName(String username) {
	    hxSDKHelper.setHXId(username);
	}

	/**
	 * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
	 * 内部的自动登录需要的密码，已经加密存储了
	 *
	 * @param pwd
	 */
	 
	public void setPassword(String pwd) {
	    hxSDKHelper.setPassword(pwd);
	}


/**
	 * 退出登录,清空数据
	 */
	public void logout(final EMCallBack emCallBack) {
		// 先调用sdk logout，在清理app中自己的数据
	    hxSDKHelper.logout(emCallBack);
	}

	
	//�����쳣 ��־����
	/*private class MyExceptionHandler implements UncaughtExceptionHandler {
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			StringWriter sw = new StringWriter();
			PrintWriter writer = new PrintWriter(sw);
			ex.printStackTrace(writer);
			StringBuilder sb = new StringBuilder();
			sb.append(sw.toString() + "\n");
			sb.append("----------\n");
			try {
				Field[] fileds = Build.class.getFields();
				for (Field field : fileds) {
					String name = field.getName();
					String value = (String) field.get(null);
					sb.append(name + ":" + value + "\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			sb.append("-------------\n");
			sb.append(new Date().toString());
			try {
				File file = new File(Environment.getExternalStorageDirectory(),
						"error.log");
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(sb.toString().getBytes());
				AbLogUtil.e(TAG, "error==" + sb.toString());
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MobclickAgent.onKillProcess(mContext);
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}*/
	private DownloadMovieItem stopOrStartDownloadMovieItem; //需要停止下载的任务
	private List<DownloadMovieItem> downloadItems = new ArrayList<DownloadMovieItem>(); //下载队列
	private List<DownloadMovieItem> downloadedItems = new ArrayList<DownloadMovieItem>(); //已下载队列


	public List<DownloadMovieItem> getDownloadItems(){
		return downloadItems;
	}
	public List<DownloadMovieItem> getDownloadedItems(){
		return downloadedItems;
	}
	public void setDownloadedItems(List<DownloadMovieItem> downloadedItems) {
		this.downloadedItems = downloadedItems;
	}
	public void setDownloadItems(List<DownloadMovieItem> downloadItems){
		this.downloadItems = downloadItems;
	}

	public DownloadMovieItem getStopOrStartDownloadMovieItem(){
		return stopOrStartDownloadMovieItem;
	}

	public void setStopOrStartDownloadMovieItem(DownloadMovieItem stopOrStartDownloadMovieItem){
		this.stopOrStartDownloadMovieItem = stopOrStartDownloadMovieItem;
	}
}
