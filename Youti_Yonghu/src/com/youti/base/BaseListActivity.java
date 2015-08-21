package com.youti.base;

import android.content.Intent;

public class BaseListActivity extends BaseActivity{
	
	
	public void startActivity(Class activityClass,int flag){
		Intent intent = new Intent(this,activityClass);
		intent.setFlags(flag);
		startActivity(intent);
	}
	public void startActivity(Class activityClass){
		Intent intent = new Intent(this,activityClass);
		startActivity(intent);		
	}
}
