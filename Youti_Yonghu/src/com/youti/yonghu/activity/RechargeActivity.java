package com.youti.yonghu.activity;

import org.apache.http.Header;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.YoutiApplication;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
import com.youti.view.TitleBar;
import com.youti.yonghu.bean.InfoBean;

public class RechargeActivity extends Activity{
	EditText et_code;
	Button but,delete;
	TitleBar titleBar;
	String user_id;
	private String card;
	public final String mPageName ="RechargeActivity";
	private Dialog createProgressBarDialog;
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
		setContentView(R.layout.layout_recharge);
		
		titleBar =(TitleBar) findViewById(R.id.index_titlebar);
		titleBar.setSearchGone();
		titleBar.setTitleBarTitle("储值卡充值");
		titleBar.setShareGone(false);
		et_code=(EditText) findViewById(R.id.et_code);
		but=(Button) findViewById(R.id.but);
		delete=(Button) findViewById(R.id.delete);
		
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				et_code.setText("");
			}
		});
		
		but.setOnClickListener(new OnClickListener() {
			
		

			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(et_code.getText().toString().replace(" ", ""))){
					Utils.showToast(RechargeActivity.this, "请输入正确的充值卡号");
					return;
				}
				card = et_code.getText().toString().replace(" ", "");
				requestData();
			}
		});
		
	}
	protected void requestData() {
		RequestParams params =new RequestParams();
		user_id=YoutiApplication.getInstance().myPreference.getUserId();
		params.put("user_id", user_id);
		params.put("card", card);
		createProgressBarDialog = Utils.createProgressBarDialog(RechargeActivity.this, "正在提交...");
		createProgressBarDialog.show();
		HttpUtils.post("http://112.126.72.250/ut_app/index.php?m=User&a=user_card", params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				createProgressBarDialog.dismiss();
				Gson gson =new Gson();
				InfoBean fromJson = gson.fromJson(arg2, InfoBean.class);
				if("1".equals(fromJson.code)){
					//将服务器返回过来的value值，带到myAccountActiviyt页面中。
					Utils.showToast(RechargeActivity.this, "充值成功");
					finish();
					/*Intent intent =new Intent(RechargeActivity.this,MyAccountActivity.class);
					startActivity(intent);*/
				}else {
					Utils.showToast(RechargeActivity.this, fromJson.info);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				createProgressBarDialog.dismiss();
				Utils.showToast(RechargeActivity.this,"网络连接异常");
			}
		});
	}
}
