package com.youti.yonghu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbDialogUtil;
import com.alipay.sdk.encrypt.MD5;
import com.example.youti_geren.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.Constants;
import com.youti.appConfig.YoutiApplication;
import com.youti.base.BaseActivity;
import com.youti.utils.HttpUtils;
import com.youti.utils.IntentJumpUtils;
import com.youti.utils.Utils;
import com.youti.view.CustomProgressDialog;
/**
 * 
* @ClassName: SettingPayPassManagerActivity 
* @Description: TODO(修改 密码 ) 
* @author zychao 
* @date 2015-6-25 下午4:16:38 
*
 */
public class YuePayActivity extends BaseActivity implements OnClickListener{
	
//	private TitleBar mTitleBar;
	private TextView mTxvForget,tvPrice;
	private Button mButton;
	private EditText mEdit;
	
	private String mPass;
	private float youhuiAmount;
	float you_price,price;
	private String you_id,isYouhui,status,order_id,ton_id;
	int payCode;
	public final String mPageName ="YuePayActivity";
	private String event_id;
	private String user_id;
	private SharedPreferences sp;
	private Editor editor;
	CustomProgressDialog dialog;
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
		setContentView(R.layout.yue_pay_activity);
		//mTitleBar = (TitleBar)findViewById(R.id.titlebar);
		mTxvForget = (TextView)findViewById(R.id.forget);
		tvPrice = (TextView)findViewById(R.id.tv_price);
		mButton = (Button)findViewById(R.id.button);
		mEdit = (EditText)findViewById(R.id.pass_1);
		sp = getSharedPreferences("abc", MODE_PRIVATE);
		editor = sp.edit();
		
		dialog = new CustomProgressDialog(this, R.string.pay_tips,R.anim.frame2);
		
		status  = getIntent().getExtras().getString("status");
		isYouhui = getIntent().getExtras().getString("isYouhui");
		user_id =YoutiApplication.getInstance().myPreference.getUserId();
		youhuiAmount=getIntent().getExtras().getFloat("you_price");
		if(!isYouhui.equals("0")){
			you_price = getIntent().getExtras().getFloat("you_price");
			you_id = getIntent().getExtras().getString("you_id");
		}
		
		if(status.equals("3")){//通卡 余额支付,通卡支付 不产生订单
			ton_id = getIntent().getExtras().getString("ton_id");
		}else if(status.equals("4")) {//活动报名 
			event_id  = getIntent().getExtras().getString("event_id");
		}else {
			order_id  = getIntent().getExtras().getString("order_id");
		}
		
		price = getIntent().getExtras().getFloat("price");
		tvPrice.setText("支付金额："+price+"");
		
		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				payYue();
			}
		});
		
		mTxvForget.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putString("code", "0");
				IntentJumpUtils.nextActivity(ForgetPasswordActivity.class, YuePayActivity.this, bundle, Constants.REQUEST_CODE_YUE_HOME);
			}
		});
	}
	
	
	private void payYue(){
		String pass = mEdit.getText().toString();
		RequestParams params = new RequestParams(); //
		String url ;
		if(!isYouhui.equals("0")){
			params.put("you_id",you_id);
			params.put("you_price",youhuiAmount);
		}
		if(status.equals("3")){//通卡 支付 
			params.put("ton_id",ton_id);
			url = Constants.PAY_TONGKA_YUE;
		}else if(status.equals("4")) {// 活动支付 
			params.put("event_id",event_id);
			url = Constants.PAY_ACTIVE_YUE;
			
		}else{// 购课 支付
			params.put("status",status);
			params.put("order_id",order_id);
			url = Constants.PAY_YUE;
		}
		if(pass.equals("")||pass==null){
			Utils.showToast(mContext, "请输入支付密码");
			return;
		}
		if(user_id.equals("")||user_id==null){
			Utils.showToast(mContext, "请先登录账户");
			return;
		}
		pass =MD5.a(pass);
		params.put("paypwd",pass);//账户 密码
		params.put("price",price);
		params.put("user_id",user_id);
		
		HttpUtils.post(url, params,
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

					public void onSuccess(int statusCode,
							org.apache.http.Header[] headers,
							org.json.JSONObject response) {
						dialog.dismiss();
						try {
							
							 //code：403支付密码不正确
							 // code：404账户余额不足
							 // code：0支付失败
							 // code：1支付成功
							response.toString();
							String code = response.getString("code");
							if(code.equals("403")){
								Toast.makeText(mContext, "忘记密码，请重新设置密码",Toast.LENGTH_LONG);
								//Bundle bundle = new Bundle();
								//bundle.putString("code", "0");
								//IntentJumpUtils.nextActivity(ForgetPasswordActivity.class, YuePayActivity.this, bundle, Constants.REQUEST_CODE_YUE_HOME);
								//finish();
							}else if(code.equals("404")){
								Utils.showToast(mContext, "账户余额不足，请充值");
								Bundle bundle = new Bundle();
								bundle.putString("code", "0");
								IntentJumpUtils.nextActivity(MyAccountActivity.class, YuePayActivity.this, bundle, Constants.REQUEST_CODE_YUE_HOME);
								finish();
							}else if(code.equals("1")){
								if(status.equals("1")){//支付成功 跳转 我的 教练 
									Utils.showToast(mContext, "跳转1");
									IntentJumpUtils.nextActivity(MyCoachActivity.class, YuePayActivity.this);
									finish();
								}else if(status.equals("2")){//支付成功 跳转 我的 课程 
									Utils.showToast(mContext, "跳转2");
									IntentJumpUtils.nextActivity(MyCourseActivity.class, YuePayActivity.this);
									finish();
								}else if(status.equals("3")){//通卡 支付成功 跳转 主页 
									Utils.showToast(mContext, "通卡支付成功");
									editor.putString("ton_id", ton_id);
									editor.commit();
									Bundle bundle = new Bundle();
									bundle.putString("val", "全部");
									IntentJumpUtils.nextActivity(CourseListActivity.class, YuePayActivity.this,bundle);
									finish();
								}else if(status.equals("4")){//支付成功 跳转 活动报名页 
									Utils.showToast(mContext, "报名成功");
									editor.putString("active_id"+event_id, user_id+event_id);
									editor.commit();
									Bundle bundle = new Bundle();
									bundle.putString("id", event_id);
									IntentJumpUtils.nextActivity(ActivePageActivity.class, YuePayActivity.this,bundle);
									finish();
								}
								
							}else if(code.equals("0")){
								Utils.showToast(mContext, "支付失败");
							}
							
						} catch (Exception e) {

						}
					};
				});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK){
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}	
