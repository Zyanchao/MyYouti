package com.youti.yonghu.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
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
import com.youti.view.CustomProgressDialog;
import com.youti.yonghu.bean.OrderCoach;
import com.youti.yonghu.bean.OrderCourse;

/**
 *  私教课 订单确定 页
 * @author zyc
 * 2015-6-1
 */
public class OrderCoachActivity extends BaseActivity implements OnClickListener{

	private static final int REQUEST_CODE_PAYMENT = 1;
	ImageView iv_paytype,iv_yuetype,iv_weichat;
	private TextView tvOrder,tvCourseName,tvCoachName,tvStartTime,tvTime,price,amount,youhui;
	//tvTitle,
	private LinearLayout ll_pay,ll_yue,ll_weichat;//支付渠道
	private RelativeLayout rl_youhuiquan;
	private Button /*btBuy299,btBuy399,*/btAddCard,btBuy;
	private TextView tv_1yue,tv_3yue,tv_6yue,tvAcount;
	private LinearLayout llTime,llStartTime,llCoachName;
	String payType="";
	private OrderCourse orderCourse;
	private OrderCoach orderBean;
	private float youhuiAmount;
	float total;
	private String youhuiId;
	int orderType;
	CustomProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_coach_course);
		orderType = getIntent().getExtras().getInt("orderType");
		if(orderType==Constants.REQUEST_CODE_PAY_COACH){
			orderBean = (OrderCoach) getIntent().getSerializableExtra(Constants.KEY_ORDER_COACH);
		}else if(orderType==Constants.REQUEST_CODE_PAY_COURSE){
			orderCourse = (OrderCourse) getIntent().getSerializableExtra(Constants.KEY_ORDER_COURSE);
		}
		dialog = new CustomProgressDialog(this, R.string.pay_tips,R.anim.frame2);
		initView();
		initListener();
		setDatas();
	}

	public final String mPageName = "OrderCoachActivity";
	private String status;
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
		iv_paytype = (ImageView) findViewById(R.id.iv_paytype);
		iv_yuetype = (ImageView) findViewById(R.id.iv__yue_paytype);
		//iv_weichat = (ImageView) findViewById(R.id.iv__weichat);
		
		ll_pay = (LinearLayout) findViewById(R.id.ll_zfb);
		ll_yue = (LinearLayout) findViewById(R.id.ll_yue);
		//ll_weichat = (LinearLayout) findViewById(R.id.ll_weichat);
		
		//tvTitle = (TextView) findViewById(R.id.tv_course_title);
		btBuy = (Button) findViewById(R.id.bt_pay);
		tvOrder = (TextView) findViewById(R.id.tv_order_name);
		tvCourseName = (TextView) findViewById(R.id.tv_course_name);
		tvCoachName = (TextView) findViewById(R.id.tv_coach_name);
		tvTime = (TextView) findViewById(R.id.tv_course_time);
		tvStartTime = (TextView) findViewById(R.id.tv_course_starttime);
		llTime = (LinearLayout) findViewById(R.id.ll_course_time);
		llStartTime= (LinearLayout) findViewById(R.id.ll_course_starttime);
		llCoachName = (LinearLayout) findViewById(R.id.ll_coach_name);
		price = (TextView) findViewById(R.id.tv_course_price);
		rl_youhuiquan = (RelativeLayout) findViewById(R.id.rl_youhuiquan);
		youhui = (TextView) findViewById(R.id.tv_youhuiquan);
		tvAcount = (TextView) findViewById(R.id.tv_amount);
		if(orderType==Constants.REQUEST_CODE_PAY_COACH){
			llStartTime.setVisibility(View.GONE);
			tvCoachName.setVisibility(View.VISIBLE);
			status="1";
		}else if(orderType==Constants.REQUEST_CODE_PAY_COURSE){
			status="2";
			llCoachName.setVisibility(View.GONE);
			llTime.setVisibility(View.GONE);
		}
	}
	

	private void initListener() {
		btBuy.setOnClickListener(this);
		ll_pay.setOnClickListener(this);
		ll_yue.setOnClickListener(this);
		rl_youhuiquan.setOnClickListener(this);
	}

	
	/**
	* @Title: getData 
	* @Description: TODO(获取套餐信息) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void setDatas(){
		if(orderType==Constants.REQUEST_CODE_PAY_COACH){
			tvOrder.setText(orderBean.getOrder_id());
			tvCourseName.setText(orderBean.getProject_type());
			price.setText(orderBean.getPrice()+" 元");
			total = Float.parseFloat(orderBean.getPrice());
			tvCoachName.setText(orderBean.getCoach_name());
			tvTime.setText(orderBean.getLong_time()+" 小时");
			tvAcount.setText(orderBean.getPrice()+" 元");
		}else if(orderType==Constants.REQUEST_CODE_PAY_COURSE){
			tvOrder.setText(orderCourse.getOrder_id());
			tvCourseName.setText(orderCourse.getProject_type());
			price.setText(orderCourse.getPrice());
			tvStartTime.setText(orderCourse.getStart_time());
			total = Float.parseFloat(orderCourse.getPrice());
			tvAcount.setText(orderCourse.getPrice()+" 元");
		}
	}
	
	
	/*
	 * 在线 支付 
	 */
	private void pay() {
		RequestParams params = new RequestParams(); //
		String  url = "" ;
		if(orderType==Constants.REQUEST_CODE_PAY_COACH){
			url = Constants.PAY_ORDER_COACH;
			params.put("order_id",orderBean.getOrder_id());
			if(payType==null){
				Utils.showToast(mContext, "请选择支付方式");
				return;
			}
		}else if(orderType==Constants.REQUEST_CODE_PAY_COURSE){
			url = Constants.PAY_ORDER_COURSE;
			params.put("order_id",orderCourse.getOrder_id());
		}
		
		params.put("amount",total);
		if(youhuiId!=null){
			params.put("you_id",youhuiId);
			params.put("you_price",youhuiAmount);
		}
		params.put("channel",payType);
		
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
						try {
							dialog.dismiss();
							String str= response.toString();
							//charge = JSONObject.parseObject(str, Charge.class);
							Intent intent = new Intent();
				            String packageName = getPackageName();
				            ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
				            intent.setComponent(componentName);
				            intent.putExtra(PaymentActivity.EXTRA_CHARGE, str);
				            startActivityForResult(intent, Constants.REQUEST_CODE_PAY_COACH);
							
						} catch (Exception e) {

						}
					};
				});
		
	}
	
	
	
	/*
	 * 余额 支付 
	 */
	private void payYue(){
		if(YoutiApplication.getInstance().myPreference.getIsSet()){
			Bundle bundle = new Bundle();
			if(youhuiId!=null){
				bundle.putFloat("you_price", youhuiAmount);
				bundle.putString("you_id", youhuiId);
				bundle.putString("isYouhui", "1");
			}else {
				bundle.putString("isYouhui", "0");//表示未使用优惠券
			}
			if(status.equals("1")){
				bundle.putString("order_id",orderBean.getOrder_id());
			}else if(status.equals("2")){
				bundle.putString("order_id",orderCourse.getOrder_id());
			}
			
			bundle.putString("status", status);
			bundle.putFloat("price",total);
			IntentJumpUtils.nextActivity(YuePayActivity.class, OrderCoachActivity.this, bundle, Constants.REQUEST_CODE_COACH);
			finish();
			
		}else {
			Utils.showToast(mContext, "请先设置账户支付密码");
			Bundle bundle = new Bundle();
			bundle.putString("code", "1");
			IntentJumpUtils.nextActivity(ModifyPasswordActivity.class, OrderCoachActivity.this, bundle,Constants.REQUEST_CODE_PAY_COACH);
		}
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
			
		case R.id.rl_youhuiquan:
			Intent intent = new Intent(mContext,MyAccountActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("payCode", Constants.REQUEST_CODE_PAY_COACH);
			intent.putExtras(bundle);
			startActivityForResult(intent, Constants.REQUEST_CODE_PAY_COACH);
			//IntentJumpUtils.nextActivity(MyAccountActivity.class, this, bundle, Constants.REQUEST_CODE_PAY_COACH);
			
			break;
			
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
	
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		 if (resultCode == RESULT_OK) {
			//支付页面返回处理
		        if (requestCode == REQUEST_CODE_PAYMENT) {
		        	 String result = data.getExtras().getString("pay_result");
		        	 if(result.equals("success")){
		        		 Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		        		 if(orderType==Constants.REQUEST_CODE_PAY_COACH){
		        			 IntentJumpUtils.nextActivity(MyCoachActivity.class, OrderCoachActivity.this);
		        			 finish();
		        		 }else if(orderType==Constants.REQUEST_CODE_PAY_COURSE){
		        			 IntentJumpUtils.nextActivity(MyCourseActivity.class, OrderCoachActivity.this);
		        			 finish();
			        	 }
		        	 }
		             /* 处理返回值
		              * "success" - payment succeed
		              * "fail"    - payment failed
		              * "cancel"  - user canceld
		              * "invalid" - payment plugin not installed
		              * 如果是银联渠道返回 invalid，调用 UPPayAssistEx.installUPPayPlugin(this); 安装银联安全支付控件。
		              */
		             String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
		             
		             
		         } else if (resultCode == Activity.RESULT_CANCELED) {
		             Toast.makeText(this, "您已取消支付", Toast.LENGTH_SHORT).show();
		         } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
		             Toast.makeText(this, "An invalid Credential was submitted.", Toast.LENGTH_SHORT).show();
		         }
		     }else if (resultCode ==  Constants.REQUEST_CODE_PAY_COACH) {
	        	 youhuiAmount = data.getExtras().getFloat("youhuiAmount");
	        	 youhuiId = data.getExtras().getString("id");
	        	 
	        	 if(orderType==Constants.REQUEST_CODE_PAY_COACH){
	        		 total = Float.parseFloat(orderBean.getTotal_price())-youhuiAmount;
	        	 }else if(orderType==Constants.REQUEST_CODE_PAY_COURSE){
	        		 total = Float.parseFloat(orderCourse.getPrice())-youhuiAmount;
	        	 }
	        	 if(total>=0){
	        		 tvAcount.setText(""+total);
	        		 youhui.setText("-"+youhuiAmount);
	        	 }else {
	        		 Utils.showToast(mContext, "亲!支付金额很小，优惠券留着下次再用吧！");
	        	 }
	         }
		 }
}
