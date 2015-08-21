package com.youti.yonghu.activity;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbLogUtil;
import com.alibaba.fastjson.JSON;
import com.example.youti_geren.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pingplusplus.android.PaymentActivity;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.Constants;
import com.youti.appConfig.YoutiApplication;
import com.youti.base.BaseActivity;
import com.youti.utils.HttpUtils;
import com.youti.utils.IntentJumpUtils;
import com.youti.utils.PopWindowCoursePg;
import com.youti.utils.ScreenUtils;
import com.youti.utils.Utils;
import com.youti.view.CustomProgressDialog;
import com.youti.yonghu.adapter.RechargeListAdapter;
import com.youti.yonghu.bean.Recharge;

/**
 * 
* @ClassName: MyAccountActivity 
* @Description: TODO(我的账户) 
* @author zychao 
* @date 2015-6-28 下午3:47:11 
*
 */
public class UpAccountActivity extends BaseActivity implements OnClickListener{
	private static final int REQUEST_CODE_PAYMENT = 2;
	public static int REQUEST = 1;
	private TextView mAccountMoney;
	private TextView mRechargeMoney;
	private LinearLayout mRechargeLayout;
	private LinearLayout mPassManagerLayout;
	private LinearLayout mPayzfb;
	private Button mButton;
	private Handler mHandler;
	private ImageView iv_paytype;
	private double money = 0;
	private double rechargeMoney = 0.01;
	private String userId;
	String payType="";
	private List<Recharge> rechargeList;
	private String payMount;
	PopWindowCoursePg coursePg;
	FrameLayout coursePgLayout;
	ListView coursePglist;
	private RechargeListAdapter fAdapter;
	private Recharge recharge;
	
	private String amcount;
	

	CustomProgressDialog dialog;
	public final String mPageName ="UpAccountActivity";
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
		setContentView(R.layout.setting_account_activity);
		userId=((YoutiApplication)getApplication()).myPreference.getUserId();
		amcount = getIntent().getExtras().getString("amcount");
		dialog = new CustomProgressDialog(this, R.string.laoding_tips,R.anim.frame2);
		dialog.show();
		initView();
		initListener();
		getData();
	}

	
	/**
	* @Title: getData 
	* @Description: TODO(获取账户余额信息) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void getData() {
		
		/**
		 * 无网络 获取缓存数据
		 */
		
		/**
		 * 请求数据 
		 */
		RequestParams params = new RequestParams();
		params.put("user_id", YoutiApplication.getInstance().myPreference.getUserId());
		/*if (w < 720) {
			params.put("resolution", "480");
			;
		} else {
			params.put("resolution", w + "");
		}*/
		HttpUtils.post(Constants.RECHARGE_LIST, params,
				new JsonHttpResponseHandler() {
					public void onStart() {
						super.onStart();
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
							//课程 详情
							String state = response.getString("code");
							if (state.equals("1")) {
								com.alibaba.fastjson.JSONObject object = JSON.parseObject(response.toString());
								com.alibaba.fastjson.JSONArray jsonArray = object.getJSONArray("list");
								rechargeList = JSON.parseArray(jsonArray.toString(), Recharge.class); 
							} else {
							}
						} catch (Exception e) {
							AbLogUtil.d(UpAccountActivity.this, e.toString());
						}
					};
				});
	}
	
	
	
	private void initListener() {
		mRechargeLayout.setOnClickListener(this);
		mPassManagerLayout.setOnClickListener(this);
		mPayzfb.setOnClickListener(this);
		mButton.setOnClickListener(this);
	}


	private void initView() {
		mAccountMoney = (TextView)findViewById(R.id.money);
		mRechargeMoney = (TextView)findViewById(R.id.recharge_money);
		mRechargeLayout = (LinearLayout)findViewById(R.id.recharge_layout);
		mPassManagerLayout = (LinearLayout)findViewById(R.id.pay_pass_manager_layout);
		mPayzfb = (LinearLayout) findViewById(R.id.ll_zfb);
		iv_paytype = (ImageView) findViewById(R.id.iv_paytype);
		mButton = (Button)findViewById(R.id.button);
		coursePgLayout = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.sort_list, null);
		coursePglist = (ListView) coursePgLayout.findViewById(R.id.sort_list);
		mAccountMoney.setText(amcount);
		mHandler = new Handler();
		
	}

	private void pay() {
		RequestParams params = new RequestParams(); //
		
		if(payMount==null){
			Utils.showToast(mContext, "请选择充值金额");
			return;
		}
		params.put("amount",1);
		if(payType==null){
			Utils.showToast(mContext, "请选择支付方式");
			return;
		}
		params.put("channel",payType);
		params.put("user_id", YoutiApplication.getInstance().myPreference.getUserId());
		HttpUtils.post(Constants.PAY_RECHARGE, params,
				new JsonHttpResponseHandler() {
					public void onStart() {
						super.onStart();
					}

					public void onFailure(int statusCode,
							org.apache.http.Header[] headers,
							java.lang.Throwable throwable,
							org.json.JSONObject errorResponse) {
					};

					public void onSuccess(int statusCode,
							org.apache.http.Header[] headers,
							org.json.JSONObject response) {
						try {
							String str= response.toString();
							//charge = JSONObject.parseObject(str, Charge.class);
							Intent intent = new Intent();
				            String packageName = getPackageName();
				            ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
				            intent.setComponent(componentName);
				            intent.putExtra(PaymentActivity.EXTRA_CHARGE, str);
				            startActivityForResult(intent, REQUEST_CODE_PAYMENT);
							
						} catch (Exception e) {

						}
					};
				});
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.recharge_layout:
			
			if(rechargeList!=null&rechargeList.size()>0){
				fAdapter = new RechargeListAdapter(getApplicationContext(), rechargeList);
				coursePglist.setAdapter(fAdapter);
				coursePg = new PopWindowCoursePg(this,ScreenUtils.getScreenWidth(this),ScreenUtils.getScreenHeight(this)*2/7, coursePgLayout,mRechargeLayout);
				coursePglist.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						TextView v = (TextView) view.findViewById(R.id.tv_item_name);
						recharge = (Recharge) v.getTag();
						payMount = recharge.getMoney();
						mRechargeMoney.setText(payMount);
						PopWindowCoursePg.colsePop();
					}
				});
			}
			break;
		case R.id.pay_pass_manager_layout:
			IntentJumpUtils.nextActivity(ForgetPasswordActivity.class, UpAccountActivity.this);
			break;
		case R.id.ll_zfb:
			iv_paytype.setImageResource(R.drawable.coach_search_title_selected);
			//iv_weichat.setImageResource(R.drawable.coach_search_title_normal);
			payType = Constants.CHANNEL_ALIPAY;
			break;

		case R.id.button:
			pay();
			break;
		default:
			break;
		}
		
	}
	
	
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {


		 if (resultCode == RESULT_OK) {
			//支付页面返回处理
		        if (requestCode == REQUEST_CODE_PAYMENT) {
		        	 String result = data.getExtras().getString("pay_result");
		             /* 处理返回值
		              * "success" - payment succeed
		              * "fail"    - payment failed
		              * "cancel"  - user canceld
		              * "invalid" - payment plugin not installed
		              * 如果是银联渠道返回 invalid，调用 UPPayAssistEx.installUPPayPlugin(this); 安装银联安全支付控件。
		              */
		             String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
		             Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
		         } else if (resultCode == Activity.RESULT_CANCELED) {
		             Toast.makeText(this, "User canceled", Toast.LENGTH_SHORT).show();
		         } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
		             Toast.makeText(this, "An invalid Credential was submitted.", Toast.LENGTH_SHORT).show();
		         }
		        
		     }
		 }
}
