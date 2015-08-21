package com.youti.yonghu.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.app.a;
import com.example.youti_geren.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.Constants;
import com.youti.appConfig.YoutiApplication;
import com.youti.base.BaseActivity;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
public class WanshanCoachActivity extends BaseActivity implements OnClickListener{
	private TextView tvcancel,tvIssue;
	private EditText et_comment;
	private String  id;
	//private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_wanshan);
		id = getIntent().getExtras().getString(Constants.KEY_ID);
		initView();
		initListenter();
	}
	
	public final String mPageName = "WanshanCoachActivity";
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
	private void initView() {
		et_comment = (EditText) findViewById(R.id.et_comment);
		tvcancel = (TextView) findViewById(R.id.cancel);
		tvIssue = (TextView) findViewById(R.id.issue);
	}
	
	private void initListenter() {
		tvcancel.setOnClickListener(this);
		tvIssue.setOnClickListener(this);
	}

	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			finish();
			break;

		case R.id.issue:
			issueComment();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 
	* @Title: issueComment 
	* @Description: TODO(完善资料 ) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	
	
	private void issueComment(){
		RequestParams params = new RequestParams();
		params.put("user_id", YoutiApplication.getInstance().myPreference.getUserId());
		params.put("course_id", id);
		String cc = et_comment.getText().toString();
		if(!cc.equals("")){
			params.put("content", et_comment.getText().toString());
		}else {
			Utils.showToast(mContext, "您还没写教练相关信息哦，请填写完再提交");
		}

		HttpUtils.post(Constants.ISSUE_DETAIL_COACH_JS, params, new JsonHttpResponseHandler(){
			public void onStart() {  
                super.onStart(); 
                System.out.println("onStart");
            } 
            public void onFailure(int statusCode,
                    org.apache.http.Header[] headers,
                    java.lang.Throwable throwable,
                    org.json.JSONObject errorResponse) {
            };
            public void onFinish() {
            };
            public void onSuccess(int statusCode,
                    org.apache.http.Header[] headers,
                    org.json.JSONObject response){ 
            	
				try {
					String state = response.getString("code");
					if(state.equals("1")){
						Utils.showToast(mContext, "您已提交成功，请等待审核...");
						finish();
	            	}else{
	            	}
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
		});
	}
}
