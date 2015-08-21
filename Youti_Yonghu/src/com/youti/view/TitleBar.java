package com.youti.view;


import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.youti_geren.R;

public class TitleBar extends RelativeLayout{

	
	
    protected Context mContext;
	private ImageView mIconBack;
	private ImageView mIconMenu;
	private ImageView mIconUser;
	private ImageView mIconSearch;
	private ImageView mIconShare;
	private ImageView mIconAdd;
	//private RelativeLayout mRlUser;
	//private RelativeLayout mBtnUser;
	private TextView mTxvTitle,mTvUser,againpay;

	private ImageView img;
	public TitleBar(Context context) {
		super(context);
	}
	
	public TitleBar(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        inflate(context, R.layout.widget_titlebar, this);
        mIconMenu = (ImageView)findViewById(R.id.title_menu_left);
        mIconBack = (ImageView)findViewById(R.id.title_back);
        mIconUser = (ImageView)findViewById(R.id.title_menu_right);
        mIconSearch = (ImageView)findViewById(R.id.title_search);
        mIconShare = (ImageView)findViewById(R.id.title_share);
        mTvUser = (TextView)findViewById(R.id.user_red);
        mTxvTitle = (TextView)findViewById(R.id.tv_title);
        mIconAdd=(ImageView) findViewById(R.id.add);
        againpay=(TextView) findViewById(R.id.againpay);
        
        TypedArray t = context.obtainStyledAttributes(attrs,R.styleable.Widget_TitleBar,0,
        		R.style.Widget_TitleBar_Style);
        
        boolean isBackVisible = t.getBoolean(R.styleable.Widget_TitleBar_btnBackVisible, true);
        boolean isSearchVisible = t.getBoolean(R.styleable.Widget_TitleBar_btnsearchVisible, true);
        boolean isShareVisible = t.getBoolean(R.styleable.Widget_TitleBar_btnShareVisible, true);
        mIconBack.setVisibility(isBackVisible ? View.VISIBLE : View.GONE);
        mIconSearch.setVisibility(isSearchVisible ? View.VISIBLE : View.GONE);
        mIconShare.setVisibility(isShareVisible ? View.VISIBLE : View.GONE);
        mTxvTitle.setText(t.getText(R.styleable.Widget_TitleBar_titleText));
        
        if (context instanceof Activity) {
        	mIconBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity a = (Activity) context;
                    a.finish();
                }
            });
        }
       
	}
	/**
	 * 设置分享按钮消失的方法
	 */
	public void setShareGone(boolean visiable){
		if(visiable){
			mIconShare.setVisibility(View.VISIBLE);
		}else{
			mIconShare.setVisibility(View.GONE);
		}
	}
	/**
	 * 设置再次购买，再次预约的方法
	 */
	public void setAgainpayText(String text){
		againpay.setText(text);
	}
	/**
	 * 设置再次购买，再次预约是否显示的方法
	 */
	public void setAgainpayVisiable(boolean flag){
		if(flag){
			againpay.setVisibility(View.VISIBLE);
		}else{
			againpay.setVisibility(View.GONE);
		}
	}
	public ImageView getSearchIcon(){
		return mIconSearch;
	}
	
	public TextView getAgainPayView(){
		return againpay;
	}
	/**
	 * titlebar设置标题的方法
	 */
	public void setTitleBarTitle(String text){
		mTxvTitle.setText(text);
	}
	
	/**
	 * titleBar设置查询图标消失的方法
	 */
	public void setSearchGone(){
		mIconSearch.setVisibility(View.GONE);
	}
	/**
	 * titleBar设置查询图标显示隐藏的方法
	 */
	public void setSearchGone(boolean visiable){
		if(visiable){
			mIconSearch.setVisibility(View.GONE);
		}else{
			mIconSearch.setVisibility(View.VISIBLE);
		}
	}
	/**
	 * titleBar设置添加图标显示隐藏的方法
	 */
	public void setAddVisiable(boolean visiable){
		if(visiable){
			mIconAdd.setVisibility(View.VISIBLE);
		}else{
			mIconAdd.setVisibility(View.GONE);
		}
	}
	
	public ImageView getAddIcon(){
		return mIconAdd;
	}

}
