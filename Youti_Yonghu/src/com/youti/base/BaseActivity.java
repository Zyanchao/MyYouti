package com.youti.base;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.ab.activity.AbActivity;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.NotificationCompat;
import com.easemob.util.EasyUtils;
import com.example.youti_geren.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.YoutiApplication;
import com.youti.chat.utils.CommonUtils;
import com.youti.utils.ACache;
import com.youti.view.CustomProgressDialog;

public class BaseActivity extends AbActivity{
	
	public Context mContext;
	public ACache cache;
	public ImageLoader imageLoader;
	public DisplayImageOptions options;
	private static final int notifiId = 11;
    protected NotificationManager notificationManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mContext = this;
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.user_lbk)// 正在加载
				.showImageForEmptyUri(R.drawable.user_lbk)// 空图片
				.showImageOnFail(R.drawable.user_lbk)// 错误图片
				.cacheInMemory(true)//设置 内存缓存
				.cacheOnDisk(true)//设置硬盘缓存
				.displayer(new RoundedBitmapDisplayer(10))//是否设置为圆角，弧度为多少  
				.displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间  
				.imageScaleType(ImageScaleType.EXACTLY) // default 推荐.imageScaleType(ImageScaleType.EXACTLY) 节省内存
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		cache = ACache.get(abApplication);
	}
	
	public void startActivity(Class activityClass){
		Intent intent = new Intent(this,activityClass);
		startActivity(intent);		
	}
	@Override
	protected void onStop() {
		super.onStop();
	}
	private LocationClient mLocationClient;
	/**
	 * 初始化百度地图定位
	 */
	public void initLocation(){
		mLocationClient = ((YoutiApplication)getApplication()).mLocationClient;
				
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);//包含地址信息
		mLocationClient.setLocOption(option);
		mLocationClient.start();			
		new Handler().postDelayed(new Runnable(){   

		    public void run() {   

		    	mLocationClient.stop();
		    }   

		 }, 5000); 
	}

    @Override
    protected void onResume() {
        super.onResume();
        // onresume时，取消notification显示
        EMChatManager.getInstance().activityResumed();
        // umeng
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // umeng
        MobclickAgent.onPause(this);
    }

    /**
     * 当应用在前台时，如果当前消息不是属于当前会话，在状�?�栏提示�?�?
     * 如果不需要，注释掉即�?
     * @param message
     */
    protected void notifyNewMessage(EMMessage message) {
        //如果是设置了不提醒只显示数目的群�?(这个是app里保存这个数据的，demo里不做判�?)
        //以及设置了setShowNotificationInbackgroup:false(设为false后，后台时sdk也发送广�?)
        if(!EasyUtils.isAppRunningForeground(this)){
            return;
        }
        
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getApplicationInfo().icon)
                .setWhen(System.currentTimeMillis()).setAutoCancel(true);
        
        String ticker = CommonUtils.getMessageDigest(message, this);
        if(message.getType() == Type.TXT)
            ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
        //设置状�?�栏提示
        mBuilder.setTicker(message.getFrom()+": " + ticker);

        Notification notification = mBuilder.build();
        notificationManager.notify(notifiId, notification);
        notificationManager.cancel(notifiId);
    }

    /**
     * 返回
     * 
     * @param view
     */
    public void back(View view) {
        finish();
    }
}
