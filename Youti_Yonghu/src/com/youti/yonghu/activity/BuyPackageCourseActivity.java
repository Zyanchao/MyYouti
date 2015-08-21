package com.youti.yonghu.activity;

import java.util.ArrayList;
import java.util.List;

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
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.youti.yonghu.bean.TongKaBean;
import com.youti.yonghu.bean.TongKaBean.Tongka;

/**
 *  购买 课程  套课 99
 * @author zyc
 * 2015-6-1
 */
public class BuyPackageCourseActivity extends BaseActivity implements OnClickListener{

	private static final int REQUEST_CODE_PAYMENT = 1;
	private ImageView ivSerach;
	ImageView iv_paytype,iv_yuetype,iv_weichat;
	private TextView tvDescription;//产品说明
	private LinearLayout ll_pay,ll_yue;//支付渠道
	private LinearLayout ll_1yue,ll_3yue,ll_6yue;//课程套餐
	private ImageView iv_1yue,iv_3yue,iv_6yue;//课程套餐
	private Button /*btBuy299,btBuy399,*/btAddCard,btBuy;
	private TextView tv_1yue,tv_3yue,tv_6yue,tvAcount,tvYouhui;
	private RelativeLayout rl_youhuiquan;
	String payType;
	private List<Tongka> tkList;
	private TongKaBean tkBean;
	String user_id,price;
	private float youhuiAmount;
	float total;
	private String youhuiId;
	private String ton_id;
	CustomProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buy_package_course);
		user_id = YoutiApplication.getInstance().myPreference.getUserId();
		dialog = new CustomProgressDialog(this, R.string.laoding_tips,R.anim.frame2);
		dialog.show();
		tkList = new ArrayList<Tongka>();
		initView();
		initListener();
		getData();
	}

	private void initView() {
		ivSerach = (ImageView) findViewById(R.id.title_search);
		ivSerach.setVisibility(View.GONE);
		iv_paytype = (ImageView) findViewById(R.id.iv_paytype);
		iv_yuetype = (ImageView) findViewById(R.id.iv__yue_paytype);
		//iv_weichat = (ImageView) findViewById(R.id.iv__weichat);
		iv_1yue = (ImageView) findViewById(R.id.iv_1yue);
		iv_3yue = (ImageView) findViewById(R.id.iv_3yue);
		iv_6yue = (ImageView) findViewById(R.id.iv_6yue);
		
		ll_pay = (LinearLayout) findViewById(R.id.ll_zfb);
		ll_yue = (LinearLayout) findViewById(R.id.ll_yue);
		//ll_weichat = (LinearLayout) findViewById(R.id.ll_weichat);
		ll_1yue = (LinearLayout) findViewById(R.id.ll_1yue);
		ll_3yue = (LinearLayout) findViewById(R.id.ll_3yue);
		ll_6yue = (LinearLayout) findViewById(R.id.ll_6yue);
		//btBuy299 = (Button) findViewById(R.id.bt_buy299);
		//btBuy399 = (Button) findViewById(R.id.bt_buy399);
		btBuy = (Button) findViewById(R.id.bt_pay);
		tvDescription = (TextView) findViewById(R.id.tv_description);
		tvAcount = (TextView) findViewById(R.id.tv_acount);
		tv_1yue = (TextView) findViewById(R.id.tv_1yue);
		tv_3yue = (TextView) findViewById(R.id.tv_3yue);
		tv_6yue = (TextView) findViewById(R.id.tv_6yue);
		rl_youhuiquan = (RelativeLayout) findViewById(R.id.rl_youhuiquan);
		tvYouhui = (TextView) findViewById(R.id.tv_youhuiquan);
	}
	

	private void initListener() {
		btBuy.setOnClickListener(this);
		ll_pay.setOnClickListener(this);
		ll_yue.setOnClickListener(this);
		rl_youhuiquan.setOnClickListener(this);
		ll_1yue.setOnClickListener(this);
		ll_3yue.setOnClickListener(this);
		ll_6yue.setOnClickListener(this);
	}

	
	/**
	* @Title: getData 
	* @Description: TODO(获取套餐信息) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	
	public void getData(){
		/**
		 * 无网络 获取缓存数据
		 */
		
		/**
		 * 请求数据 
		 */
		RequestParams params = new RequestParams();
		params.put("user_id", user_id);
		HttpUtils.post(Constants.COURSE_TONGKA, params,
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
							//教练 详情
							String state = response.getString("code");
							if (state.equals("1")) {
								com.alibaba.fastjson.JSONObject object = JSON
										.parseObject(response.toString());
								JSONObject jsonObject = object.getJSONObject("list"); 
								tkBean = JSON.parseObject(jsonObject.toString(), TongKaBean.class); 
								tkList = tkBean.getTon_list();
								setDatas();
							} else {
							}
						} catch (Exception e) {
						}
					};
				});
	}
	
	private void setDatas(){
		tvDescription.setText(tkBean.getDesc().toString());
		
		tv_1yue.setText(tkList.get(0).getTitle());
		tv_3yue.setText(tkList.get(1).getTitle());
		tv_6yue.setText(tkList.get(2).getTitle());
		
	}
	
	
	

	private void pay() {
		RequestParams params = new RequestParams(); //
		
		if(payType==null){
			Utils.showToast(mContext, "请选择支付方式");
			return;
		}
		if(youhuiId!=null){
			params.put("you_id",youhuiId);
			params.put("you_price",youhuiAmount);
		}
		params.put("user_id",user_id);
		params.put("ton_id",ton_id);
		params.put("amount",total);
		if(payType==null){
			Utils.showToast(mContext, "请选择支付方式");
			return;
		}
		params.put("channel",payType);
		HttpUtils.post(Constants.PAY_ORDER_TONGKA, params,
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
						AbDialogUtil.removeDialog(mContext);
					};

					public void onSuccess(int statusCode,
							org.apache.http.Header[] headers,
							org.json.JSONObject response) {
						
						AbDialogUtil.removeDialog(mContext);
						try {
							String str= response.toString();
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
		case R.id.ll_zfb:
			iv_paytype.setImageResource(R.drawable.coach_search_title_selected);
			iv_yuetype.setImageResource(R.drawable.coach_search_title_normal);
			//iv_weichat.setImageResource(R.drawable.coach_search_title_normal);
			payType = Constants.CHANNEL_ALIPAY;
			//tvAcount
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
			bundle.putInt("payCode", Constants.REQUEST_CODE_PAY_TONGKA);
			intent.putExtras(bundle);
			startActivityForResult(intent, Constants.REQUEST_CODE_PAY_TONGKA);
			
			break;
			
		case R.id.ll_1yue:
			iv_1yue.setImageResource(R.drawable.coach_search_title_selected);
			iv_3yue.setImageResource(R.drawable.coach_search_title_normal);
			iv_6yue.setImageResource(R.drawable.coach_search_title_normal);
			if(tkList.size()==0){
				return;
			}
			price = tkList.get(0).getPrice();
			tvAcount.setText(price);
			total = Float.parseFloat(price);
			ton_id = tkList.get(0).getTon_id();
			//tvAcount
			break;
			
		case R.id.ll_3yue:
			iv_3yue.setImageResource(R.drawable.coach_search_title_selected);
			iv_1yue.setImageResource(R.drawable.coach_search_title_normal);
			iv_6yue.setImageResource(R.drawable.coach_search_title_normal);
			if(tkList.size()==0){
				
				return;
			}
			price = tkList.get(1).getPrice();
			tvAcount.setText(price);
			total = Float.parseFloat(price);
			ton_id = tkList.get(1).getTon_id();
			break;
			
		case R.id.ll_6yue:
			iv_6yue.setImageResource(R.drawable.coach_search_title_selected);
			iv_1yue.setImageResource(R.drawable.coach_search_title_normal);
			iv_3yue.setImageResource(R.drawable.coach_search_title_normal);
			payType = Constants.CHANNEL_WECHAT;
			if(tkList.size()==0){
				return;
			}
			price = tkList.get(2).getPrice();
			tvAcount.setText(price);
			total = Float.parseFloat(price);
			ton_id = tkList.get(2).getTon_id();
			break;
			
			
		case R.id.bt_pay:
			if(!YoutiApplication.getInstance().myPreference.getHasLogin()){
				IntentJumpUtils.nextActivity(LoginActivity.class, BuyPackageCourseActivity.this);
			}else {
				if(ton_id==null||total==0){
					Utils.showToast(mContext, "请选择套餐");
					return;
				}
				if(payType.equals("yue")){
					payYue();
				}else if(payType.equals(Constants.CHANNEL_ALIPAY)) {
					pay();
				}else {
					Utils.showToast(mContext, "请选择支付方式");
					return;
				}
			}
			
			break;

			
		default:
			break;
		}
		
	}

	private void payYue() {
		if(YoutiApplication.getInstance().myPreference.getIsSet()){
			
			Bundle bundle = new Bundle();
			if(youhuiId!=null){
				bundle.putFloat("you_price", youhuiAmount);
				bundle.putString("you_id", youhuiId);
				bundle.putString("isYouhui", "1");
			}
			bundle.putString("status", "3");
			bundle.putString("isYouhui", "0");//表示未使用优惠券
			bundle.putString("ton_id",ton_id);
			bundle.putFloat("price",total);
			IntentJumpUtils.nextActivity(YuePayActivity.class, BuyPackageCourseActivity.this, bundle, Constants.REQUEST_CODE_PAY_TONGKA);
			finish();
		}else {
			Utils.showToast(mContext, "请先设置账户支付密码");
			Bundle bundle = new Bundle();
			bundle.putString("code", "1");
			IntentJumpUtils.nextActivity(ModifyPasswordActivity.class, BuyPackageCourseActivity.this, bundle,Constants.REQUEST_CODE_PAY_TONGKA);
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
		             Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
		             String mes = null;
		             if(result.equals("cancel")){
		            	 mes = "已取消支付";
		             }else if(result.equals("fail")) {
		            	 mes = "支付失败";
		             }
		             Utils.showToast(mContext, mes);
		             
		         } else if (resultCode == Activity.RESULT_CANCELED) {
		             Toast.makeText(this, "User canceled", Toast.LENGTH_SHORT).show();
		         } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
		             Toast.makeText(this, "An invalid Credential was submitted.", Toast.LENGTH_SHORT).show();
		         }
		     }else if (resultCode ==  Constants.REQUEST_CODE_PAY_TONGKA) {
	        	 youhuiAmount = data.getExtras().getFloat("youhuiAmount");
	        	 youhuiId = data.getExtras().getString("id");
	        	 
	        	total = Float.parseFloat(price)-youhuiAmount;
	        	 if(total>0){
	        		 tvAcount.setText(""+total);
	        		 tvYouhui.setText("-"+youhuiAmount);
	        	 }else {
	        		 Utils.showToast(mContext, "亲!支付金额很小，优惠券留着下次再用吧！");
	        	 }
	         }
	 }
	 
	 public final String mPageName="BuyPackageCourseActivity";
	    
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
