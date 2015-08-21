package com.youti.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.youti_geren.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
/**
 * 该类是发表评论或者发帖时，选择图片用到的自定义控件
 * @author xiaguangcheng
 *
 */
public class CommunityBitmapCheckbox extends RelativeLayout {
	
	private ImageView mBitmapView;
	private ImageView mCheckIcon;
	
	private ImageView mGlobal;
	
	private DisplayImageOptions options;
	private ImageLoader imageLoader;

	public CommunityBitmapCheckbox(Context context) {
		super(context);
		
		inflate(context, R.layout.community_image_checkbox, this);
		
		options = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(((Activity)context).getBaseContext()));
		
		mBitmapView = (ImageView)findViewById(R.id.image_icon);
		mCheckIcon = (ImageView)findViewById(R.id.check_icon);
		mGlobal = (ImageView)findViewById(R.id.image_global);
		
		
	}
	
	public CommunityBitmapCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
	
	public void setBodyImage(Drawable drawable){
		mBitmapView.setBackgroundDrawable(drawable);
	}
	
	public void setBodyImage(String path){
		imageLoader.displayImage(path, mBitmapView);
	}
	
	public void setCheckIconVisibility(boolean isVisible){
		if(isVisible){
			mCheckIcon.setVisibility(View.VISIBLE);
		}else{
			mCheckIcon.setVisibility(View.INVISIBLE);
		}
	}
	
	public void setOnClickListener(View.OnClickListener listener){
		mCheckIcon.setOnClickListener(listener);
	}
	
	public void setGlobalVisibility(boolean isVisible){
		if(isVisible){
			mGlobal.setVisibility(View.VISIBLE);
		}else{
			mGlobal.setVisibility(View.INVISIBLE);
		}
	}
	
	public void setGlobalOnClickListener(View.OnClickListener listener){
		mGlobal.setOnClickListener(listener);
	}
	
}
