package com.youti.utils;

import android.content.Context;

import com.ab.util.AbToastUtil;
import com.ab.util.AbWifiUtil;
import com.example.youti_geren.R;

public class NetTips {
	
	public static  boolean isNetTips(Context mContext){
		if(AbWifiUtil.isConnectivity(mContext)){
			return true;
		}else{
			AbToastUtil.showToast(mContext, R.string.net_tips);
			return false;
		}
	}
	
}
