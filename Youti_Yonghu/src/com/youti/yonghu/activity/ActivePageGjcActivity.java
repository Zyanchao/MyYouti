package com.youti.yonghu.activity;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.util.AbToastUtil;
import com.alibaba.fastjson.JSON;
import com.example.youti_geren.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.Constants;
import com.youti.appConfig.YoutiApplication;
import com.youti.base.BaseActivity;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
import com.youti.view.CustomProgressDialog;
import com.youti.yonghu.adapter.ActiveListAdapter;
import com.youti.yonghu.bean.CoachTopBean;
import com.youti.yonghu.bean.UserEntity;

/**
 * 首页 活动 页
 * @author zyc
 * 2015-2-12
 */
public class ActivePageGjcActivity extends BaseActivity implements OnClickListener{

	
	private Context mContext;
	private EditText etName, etAddress,etTel;
	private Button btBook;
	private String userId,userName,eqName,telNum;
	CustomProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activie_page_gjc);
		dialog = new CustomProgressDialog(this, R.string.laoding_tips,R.anim.frame2);
		mContext = this;
		initView();
		initListener();
	}
	

	private void initView() {
		etName = (EditText) findViewById(R.id.et_name);
		etAddress = (EditText) findViewById(R.id.et_eq_name);
		etTel= (EditText) findViewById(R.id.et_tel);
		btBook = (Button) findViewById(R.id.bt_book);
	}
	
	
	private void initListener() {
		btBook.setOnClickListener(this);
	}
	
	
	
	

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.bt_book:
			userName = etName.getText().toString();
			eqName = etAddress.getText().toString();
			telNum = etTel.getText().toString();
			if(telNum==null){
				Utils.showToast(mContext, "您还没填写电话");
				return;
			}
			userId=((YoutiApplication)(mContext.getApplicationContext())).myPreference.getUserId();
			submit();
			
			break;

		default:
			break;
		}
	}
	
	
	
	/**
	* @Title: submit 
	* @Description: TODO(提交报名信息) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void submit(){
		RequestParams params = new RequestParams(); //
		params.put("username",userName);
		params.put("comname",eqName);
		params.put("phone",telNum);
		
		HttpUtils.post(Constants.HOME_ACTIVE_GJC, params,
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

					public void onFinish() {

						dialog.dismiss();
					};

					public void onSuccess(int statusCode,
							org.apache.http.Header[] headers,
							org.json.JSONObject response) {
						dialog.dismiss();
						try {
							response.toString();
							String state = response.getString("code");
							String info = response.getString("info");
							if (state.equals("1")) {
								
								
							} else {

								AbToastUtil.showToast(mContext, R.string.data_tips);
							}
						} catch (Exception e) {

						}
					};
			});

	}
	
	 public final String mPageName="ActivePageGjcActivity";
	    
	    @Override
		protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
			MobclickAgent.onPageEnd(mPageName);
			MobclickAgent.onPause(this);
		}

		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
			MobclickAgent.onPageStart(mPageName);
			MobclickAgent.onResume(this);
		}
}
