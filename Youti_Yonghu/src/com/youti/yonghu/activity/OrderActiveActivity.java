package com.youti.yonghu.activity;

import u.aly.ao;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbDialogUtil;
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
import com.youti.utils.Utils;
import com.youti.yonghu.bean.ActiveBean;
import com.youti.yonghu.bean.OrderCoach;

/**
 *  私教课 订单确定 页
 * @author zyc
 * 2015-6-1
 */
public class OrderActiveActivity extends BaseActivity implements OnClickListener{

	private static final int REQUEST_CODE_PAYMENT = 1;
	ImageView iv_paytype,iv_yuetype,iv_weichat;
	private TextView tvOrder,tvCourseName,tvCoachName,time,tvPrice,amount,tvyouhui;
	private LinearLayout ll_pay,ll_yue,ll_weichat;//支付渠道
	private Button /*btBuy299,btBuy399,*/btAddCard,btBuy;
	private TextView tv_1yue,tv_3yue,tv_6yue,tvAcount;
	String payType=Constants.CHANNEL_ALIPAY;
	private RelativeLayout ll_youhuiquan;
	private ActiveBean  active;
	private float youhuiAmount ;
	private String id,val,arlTime,price;
	private SharedPreferences sp;
	private Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_active);
		price = getIntent().getExtras().getString("price");
		val = getIntent().getExtras().getString("val");
		arlTime = getIntent().getExtras().getString("alrTime");
		id = getIntent().getExtras().getString("id");
		sp = getSharedPreferences("abc", MODE_PRIVATE);
		editor = sp.edit();
		initView();
		initListener();
		getData();
	}
	public final String mPageName = "OrderActiveActivity";
	private String user_id;
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
		user_id = YoutiApplication.getInstance().myPreference.getUserId();
		iv_paytype = (ImageView) findViewById(R.id.iv_paytype);
		iv_yuetype = (ImageView) findViewById(R.id.iv__yue_paytype);
		//iv_weichat = (ImageView) findViewById(R.id.iv__weichat);
		
		ll_pay = (LinearLayout) findViewById(R.id.ll_zfb);
		ll_yue = (LinearLayout) findViewById(R.id.ll_yue);
		//ll_weichat = (LinearLayout) findViewById(R.id.ll_weichat);
		btBuy = (Button) findViewById(R.id.bt_pay);
		tvOrder = (TextView) findViewById(R.id.tv_order_name);
		time = (TextView) findViewById(R.id.tv_active_sc);
		tvPrice = (TextView) findViewById(R.id.tv_active_prive);
		//tvyouhui = (TextView) findViewById(R.id.tv_youhuiquan);
		//vll_youhuiquan = (RelativeLayout) findViewById(R.id.ll_youhuiquan);
		tvAcount = (TextView) findViewById(R.id.tv_amount);
	}
	

	private void initListener() {
		btBuy.setOnClickListener(this);
		ll_pay.setOnClickListener(this);
		ll_yue.setOnClickListener(this);
		//ll_youhuiquan.setOnClickListener(this);
	}

	
	/**
	* @Title: getData 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void getData(){
		
		setDatas();
	}
	/**
	* @Title: getData 
	* @Description: TODO(获取套餐信息) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	
	
	private void setDatas(){
		tvOrder.setText(val);
		time.setText(arlTime+"小时");
		tvPrice.setText(price+"元");
		tvAcount.setText(price+"元");
	}
	
	private void pay() {
		RequestParams params = new RequestParams(); //
		
		if(price==null||price.equals("")){
			Utils.showToast(mContext, "支付金额有误");
			return;
		}
		if(payType==null){
			Utils.showToast(mContext, "请选择支付方式");
			return;
		}
		
		params.put("amount",price);
		params.put("channel",payType);
		params.put("event_id",id);
		params.put("user_id",user_id);
		HttpUtils.post(Constants.PAY_ORDER_ACTIVE, params,
				new JsonHttpResponseHandler() {
					public void onStart() {
						super.onStart();
						AbDialogUtil.showLoadDialog(mContext, R.anim.show,
								"支付请求中...");
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
				            finish();
						} catch (Exception e) {
						}
	};
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_zfb:
			iv_paytype.setImageResource(R.drawable.coach_search_title_selected);
			iv_yuetype.setImageResource(R.drawable.coach_search_title_normal);
			//iv_weichat.setImageResource(R.drawable.coach_search_title_normal);
			payType = Constants.CHANNEL_ALIPAY;
			break;
			
		case R.id.ll_yue:
			iv_yuetype.setImageResource(R.drawable.coach_search_title_selected);
			iv_paytype.setImageResource(R.drawable.coach_search_title_normal);
			//iv_weichat.setImageResource(R.drawable.coach_search_title_normal);
			payType = "yue";
			break;
			
		/*case R.id.ll_youhuiquan:
			Bundle bundle = new Bundle();
			bundle.putInt("payCode", Constants.REQUEST_CODE_PAY_ACTIVE);
			IntentJumpUtils.nextActivity(MyAccountActivity.class, this, null, Constants.REQUEST_CODE_PAY_ACTIVE);
			break;*/
			
		case R.id.bt_pay:
	
			if(payType.equals("")){
				Utils.showToast(mContext, "请选择支付方式");
				return;
			}
			if(payType.equals("yue")){
				payYue();
			}else {
				pay();
			}
			break;

		default:
			break;
		}
	}
	
	
	/*
	 * 余额 支付 
	 */
	private void payYue(){
		if(YoutiApplication.getInstance().myPreference.getIsSet()){
			Bundle bundle = new Bundle();
			bundle.putString("isYouhui", "0");//表示未使用优惠券
			bundle.putString("status", "4");//活动报名 支付
			bundle.putString("event_id", id);//活动id
			bundle.putFloat("price",Float.parseFloat(price));
			IntentJumpUtils.nextActivity(YuePayActivity.class, OrderActiveActivity.this, bundle, Constants.REQUEST_CODE_COACH);
			finish();
		}else {
			Utils.showToast(mContext, "请先设置账户支付密码");
			Bundle bundle = new Bundle();
			bundle.putString("code", "1");
			IntentJumpUtils.nextActivity(ModifyPasswordActivity.class, OrderActiveActivity.this, bundle,Constants.REQUEST_CODE_PAY_COACH);
		}
	}
	
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {


		 if (resultCode == RESULT_OK) {
			//支付页面返回处理
		        if (requestCode == REQUEST_CODE_PAYMENT) {
		        	 String result = data.getExtras().getString("pay_result");
		        	 if(result.equals("success")){
		        		 editor.putString("active_id"+id, user_id+id);
						 editor.commit();
		        		 Bundle bundle = new Bundle();
		        		 bundle.putString(Constants.KEY_ID, id);
		        		 IntentJumpUtils.nextActivity(ActivePageActivity.class, OrderActiveActivity.this, bundle);
		        	 }
		             /* 处理返回值
		              * "success" - payment succeed
		              * "fail"    - payment failed
		              * "cancel"  - user canceld
		              * "invalid" - payment plugin not installed
		              * 如果是银联渠道返回 invalid，调用 UPPayAssistEx.installUPPayPlugin(this); 安装银联安全支付控件。
		              */
		            // String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
		             Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
		         } else if (resultCode == Activity.RESULT_CANCELED) {
		             Toast.makeText(this, "User canceled", Toast.LENGTH_SHORT).show();
		         } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
		             Toast.makeText(this, "An invalid Credential was submitted.", Toast.LENGTH_SHORT).show();
		         }/*else if (resultCode == Constants.REQUEST_CODE_PAY_ACTIVE) {
		        	 youhuiAmount = data.getExtras().getFloat("youhuiAmount");
		        	 tvyouhui.setText("-"+youhuiAmount) ;
		        	 youhuiId = data.getExtras().getString("id");
		         }*/
		     }
		 }
}
