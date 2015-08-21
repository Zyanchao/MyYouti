package com.youti.yonghu.activity;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youti.appConfig.Constants;
import com.youti.chat.activity.ChatActivity;
import com.umeng.analytics.MobclickAgent;
import com.youti.utils.HttpUtils;
import com.youti.utils.IntentJumpUtils;
import com.youti.utils.Utils;
import com.youti.view.CircleImageView;
import com.youti.view.CustomProgressDialog;
import com.youti.view.TitleBar;
import com.youti.yonghu.bean.CoachOrderDetailBean;
import com.youti.yonghu.bean.InfoBean;
import com.youti.yonghu.bean.OrderCoach;

@SuppressLint("NewApi")
public class OrderDetailActivity extends Activity implements OnClickListener{
	TitleBar titleBar;
	Button btn_sqtk,btn_fqyk,btn_qxdd,btn_qrzf,btn_scdd;
	TextView tv_cancle,tv_sure;
	String status;
	private Dialog dialog;
	String order_id;
	String remnant;
	String coach_id;
	TextView tv_state;//状态
	LinearLayout ll_hasFinished;
	TextView tv_finish;
	TextView tv_time;
	TextView tv_money_name;//金额名称
	TextView tv_money;//金额
	TextView tv_coach_name;//教练名称
	TextView tv_coach_project;//项目名称
	Button btn_mszx;//马上咨询
	Button btn_bddh;//拨打电话
	TextView tv_way;//上课方式
	TextView tv_price;//单价
	TextView tv_hour;//订单时长
	TextView tv_zkou;//折扣
	TextView tv_totalprice;//总价
	TextView tv_realprice;//实付金额
	TextView tv_ordernum;//订单编号
	TextView order_time;//下单时间
	CircleImageView head_protrait;
	OrderCoach orderCoach;
	private CoachOrderDetailBean fromJson;
	
	private void initView() {
		btn_sqtk=(Button) findViewById(R.id.btn_sqtk);
		btn_fqyk=(Button) findViewById(R.id.btn_fqyk);
		btn_qxdd=(Button) findViewById(R.id.btn_qxdd);
		btn_qrzf=(Button) findViewById(R.id.btn_qrzf);
		btn_scdd=(Button) findViewById(R.id.btn_scdd);
		
		tv_state= (TextView) findViewById(R.id.tv_state);
		ll_hasFinished=(LinearLayout) findViewById(R.id.ll_hasfinished);
		tv_money_name=(TextView) findViewById(R.id.tv_money_name);
		tv_money=(TextView) findViewById(R.id.tv_money);
		tv_finish=(TextView) findViewById(R.id.tv_finish);
		tv_time=(TextView) findViewById(R.id.tv_time);
		
		tv_coach_name=(TextView) findViewById(R.id.tv_coach_name);
		tv_coach_project=(TextView) findViewById(R.id.tv_coach_project);
		btn_mszx=(Button) findViewById(R.id.btn_mszx);
		btn_bddh=(Button) findViewById(R.id.btn_bddh);
		head_protrait=(CircleImageView) findViewById(R.id.head_protrait);
		
		tv_way=(TextView) findViewById(R.id.tv_way);
		tv_price=(TextView) findViewById(R.id.tv_price);
		tv_hour=(TextView) findViewById(R.id.tv_hour);
		tv_zkou=(TextView) findViewById(R.id.tv_zkou);
		tv_totalprice=(TextView) findViewById(R.id.tv_totalprice);
		tv_realprice=(TextView) findViewById(R.id.tv_realprice);
		tv_ordernum=(TextView) findViewById(R.id.tv_ordernum);
		order_time=(TextView) findViewById(R.id.order_time);
		
		
	}
	public final String mPageName ="OrderDetailActivity";
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
		setContentView(R.layout.layout_orderdetail);
		if(getIntent()!=null){
			order_id=getIntent().getStringExtra("order_id");
			remnant =getIntent().getStringExtra("remnant");
			coach_id=getIntent().getStringExtra("coach_id");
			status=getIntent().getStringExtra("status");
		}
		titleBar =(TitleBar) findViewById(R.id.index_titlebar);
		orderCoach = new OrderCoach();
		titleBar.setTitleBarTitle("订单详情");
		titleBar.setSearchGone();
		titleBar.setShareGone(false);
		initView();
		initListener();
		initData();
	}
	/**
	 * 访问网络，请求数据
	 */
	private void initData() {
		String urlString="http://112.126.72.250/ut_app/index.php?m=User&a=order_info";
		RequestParams params =new RequestParams();
		customProgressDialog = new CustomProgressDialog(OrderDetailActivity.this, R.string.laoding_tips,R.anim.frame2);
		customProgressDialog.show();
		params.put("order_id", order_id);
		HttpUtils.post(urlString, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				customProgressDialog.dismiss();
				Gson gson =new Gson();
				fromJson = gson.fromJson(arg2, CoachOrderDetailBean.class);
				if(fromJson!=null&&fromJson.code.equals("1")){
					Utils.showToast(OrderDetailActivity.this, "请求成功");
					//显示数据
					showData();
				}else if(fromJson!=null&&fromJson.code.equals("0")){
					Utils.showToast(OrderDetailActivity.this, "数据库错误");
				}else{
					Utils.showToast(OrderDetailActivity.this, "订单id为空");
				}	
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				if(customProgressDialog!=null){
					customProgressDialog.dismiss();
				}
				Utils.showToast(OrderDetailActivity.this, "网络连接异常");
			}
		});
	}
	/**
	 * 显示数据   0代表未支付  1代表已支付  2已完结  3代表退款中  4已退款 5已取消
	 */
	protected void showData() {
		if("0".equals(fromJson.list.status)){
			tv_state.setText("未支付");
			tv_state.setTextColor(Color.RED);
			ll_hasFinished.setVisibility(View.GONE);
			tv_money_name.setText("应付金额:");
			btn_qxdd.setVisibility(View.VISIBLE);
			btn_qrzf.setVisibility(View.VISIBLE);
			btn_bddh.setVisibility(View.GONE);
			btn_sqtk.setVisibility(View.GONE);
			btn_fqyk.setVisibility(View.GONE);
			
			
		}else if("1".equals(fromJson.list.status)){
			tv_state.setText("已支付");
			tv_state.setTextColor(Color.BLACK);
			int a = Integer.parseInt(fromJson.list.remnant);
			int b=Integer.parseInt(fromJson.list.total_time);
			tv_finish.setText((b-a)+"");
			tv_time.setText(fromJson.list.total_time);
			tv_money_name.setText("实付金额:");
			btn_sqtk.setVisibility(View.VISIBLE);
			btn_fqyk.setVisibility(View.VISIBLE);
			
		}else if("2".equals(fromJson.list.status)){
			tv_state.setText("已完成");
			tv_state.setTextColor(Color.BLACK);
			ll_hasFinished.setVisibility(View.GONE);
			tv_money_name.setText("实付金额:");
			tv_finish.setText(fromJson.list.total_time);
			tv_time.setText(fromJson.list.total_time);
					
			btn_sqtk.setVisibility(View.GONE);
			btn_sqtk.setClickable(false);
			btn_fqyk.setVisibility(View.GONE);
			
			btn_scdd.setVisibility(View.VISIBLE);
			btn_scdd.setText("删除订单");
			
		}else if("3".equals(fromJson.list.status)){
			tv_state.setText("退款中");
			tv_state.setTextColor(Color.parseColor("#323232"));
			ll_hasFinished.setVisibility(View.VISIBLE);
			int a = Integer.parseInt(fromJson.list.remnant);
			int b=Integer.parseInt(fromJson.list.total_time);
			tv_finish.setText((b-a)+"");
			tv_time.setText(fromJson.list.total_time);
			tv_money_name.setText("退款金额:");
			btn_bddh.setVisibility(View.GONE);
			btn_sqtk.setVisibility(View.GONE);
			btn_fqyk.setVisibility(View.GONE);
			
		}else if("4".equals(fromJson.list.status)){
			tv_state.setText("已退款");
			tv_state.setTextColor(Color.BLACK);
			ll_hasFinished.setVisibility(View.VISIBLE);
			int a = Integer.parseInt(fromJson.list.remnant);
			int b=Integer.parseInt(fromJson.list.total_time);
			tv_finish.setText((b-a)+"");
			tv_time.setText(fromJson.list.total_time);
			tv_money_name.setText("退款金额:");
			btn_sqtk.setVisibility(View.GONE);
			btn_sqtk.setClickable(false);
			btn_scdd.setVisibility(View.VISIBLE);
			btn_scdd.setText("删除订单");
			btn_fqyk.setVisibility(View.GONE);
			btn_bddh.setVisibility(View.GONE);
			
		}else if("5".equals(fromJson.list.status)){
			tv_state.setText("已取消");
			ll_hasFinished.setVisibility(View.GONE);
			tv_money_name.setText("应付金额:");
			btn_bddh.setVisibility(View.GONE);
			btn_sqtk.setVisibility(View.GONE);
			btn_sqtk.setClickable(false);
			btn_fqyk.setVisibility(View.GONE);
			btn_scdd.setVisibility(View.VISIBLE);
			btn_scdd.setText("删除订单");
		}else{
			Utils.showToast(OrderDetailActivity.this, "错误的状态码");
			return;
		}
		
		
		tv_money.setText(fromJson.list.refund_agree);
		tv_coach_name.setText(fromJson.list.coach_name);
		tv_coach_project.setText(fromJson.list.pro_val);
		ImageLoader.getInstance().displayImage("http://112.126.72.250/ut_app"+fromJson.list.head_img, head_protrait);
		if("1".equals(fromJson.list.class_way)){
			tv_way.setText("教练上门");
		}
		
		if("2".equals(fromJson.list.class_way)){
			tv_way.setText("学员上门");
		}
		
		if("3".equals(fromJson.list.class_way)){
			tv_way.setText("协商地点");
		}
		
		tv_price.setText(fromJson.list.price);
		tv_hour.setText(fromJson.list.total_time);
		tv_zkou.setText(fromJson.list.zkou+"折");
		tv_totalprice.setText(fromJson.list.total_price);
		tv_realprice.setText(fromJson.list.fact_total_price);
		tv_ordernum.setText(fromJson.list.order_num);
		order_time.setText(fromJson.list.add_time);
		
		if("10".equals(fromJson.list.zkou)){
			tv_zkou.setText("无");
		}
		
		
		btn_bddh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent =new Intent();
				intent.setAction(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:"+fromJson.list.coach_phone));  
				startActivity(intent);
			}
		});
		
	}

	private void initListener() {
		btn_sqtk.setOnClickListener(this);
		btn_fqyk.setOnClickListener(this);
		btn_scdd.setOnClickListener(this);
		btn_qxdd.setOnClickListener(this);
		btn_qrzf.setOnClickListener(this);
		btn_mszx.setOnClickListener(this);
		
	}

	LinearLayout ll1,ll2,ll3,ll4;
	ImageView iv1,iv2,iv3,iv4;
	boolean isll1,isll2,isll3,isll4;
	String content="下错单了";
	private CustomProgressDialog customProgressDialog;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_scdd:
			showSQTKDialog(content,"3");
			break;
		case R.id.btn_qxdd:
			showSQTKDialog(content,"1");
			break;
		case R.id.btn_qrzf:
			
			orderCoach.setOrder_id(fromJson.list.order_num);//后台要求传递ord_num;fromJson.list.order_num
			orderCoach.setCoach_name(fromJson.list.coach_name);
			orderCoach.setProject_type(fromJson.list.pro_val);
			orderCoach.setPrice(fromJson.list.price);
			orderCoach.setLong_time(fromJson.list.total_time);
			orderCoach.setTotal_price(fromJson.list.fact_total_price);
			Bundle b = new Bundle();
			b.putSerializable(Constants.KEY_ORDER_COACH, orderCoach);
			b.putInt("orderType", Constants.REQUEST_CODE_PAY_COACH);
			IntentJumpUtils.nextActivity(OrderCoachActivity.class, OrderDetailActivity.this, b);
			break;
		case R.id.btn_sqtk:
			showSQTKDialog(content,"2");
			break;
		case R.id.btn_mszx:
			String coachName=fromJson.list.coach_name;
			String coachPhone=fromJson.list.coach_phone;
			Bundle bundle = new Bundle();
			bundle.putString(Constants.KEY_CHAT_TEL, coachPhone+Constants.CHAT_CODE);//环信 用户名   规则---- 用户电话+“3”
			bundle.putString(Constants.KEY_CHAT_USERNAME,coachName);
			bundle.putInt("chatType", 1);
			IntentJumpUtils.nextActivity(ChatActivity.class, this, bundle);
			
			
			break;
		case R.id.btn_fqyk:
			Intent intent =new Intent(this,SelectLessonTimeActivity.class);
			intent.putExtra("order_id", order_id);
			intent.putExtra("remnant", remnant);
			intent.putExtra("coach_id", coach_id);
			startActivity(intent);
			finish();
			break;
		case R.id.tv_cancle:
			dialog.dismiss();
			break;
		
		case R.id.ll1:
			if(isll1){
				isll1=false;
				iv1.setVisibility(View.VISIBLE);
				setVisiable(iv2, iv3, iv4);
				isll2=false;
				isll3=false;
				isll4=false;
				content="下错单了";
				Utils.showToast(OrderDetailActivity.this, content);
			}else{
				isll1=true;
				iv1.setVisibility(View.GONE);
				
			}
			break;
		case R.id.ll2:
			if(isll2){
				isll2=false;
				iv2.setVisibility(View.GONE);
			}else{
				isll2=true;
				iv2.setVisibility(View.VISIBLE);
				setVisiable(iv1, iv3, iv4);
				isll1=true;
				isll3=false;
				isll4=false;
				content="对教练不满意";
				Utils.showToast(OrderDetailActivity.this, content);
			}
			
			break;
		case R.id.ll3:
			if(isll3){
				isll3=false;
				iv3.setVisibility(View.GONE);
			}else{
				isll3=true;
				iv3.setVisibility(View.VISIBLE);
				setVisiable(iv2, iv1, iv4);
				isll1=true;
				isll2=false;
				isll4=false;
				content="无法与教练达成一致";
				Utils.showToast(OrderDetailActivity.this, content);
			}
			break;
		case R.id.ll4:
			if(isll4){
				isll4=false;
				iv4.setVisibility(View.GONE);
			}else{
				isll4=true;
				iv4.setVisibility(View.VISIBLE);
				setVisiable(iv2, iv3, iv1);
				isll1=true;
				isll3=false;
				isll2=false;
				content="其他";
				Utils.showToast(OrderDetailActivity.this, content);
			}
			break;
		default:
			break;
		}
	}
	/**
	 *  status:1取消订单  2申请退款  3删除订单
	 */
	private void RequestSQTK(String content,String status) {
		RequestParams params =new RequestParams();
		params.put("order_id", order_id);
		params.put("status", status);
		Utils.showToast(OrderDetailActivity.this, content);
		params.put("content", content);
		String urlString="http://112.126.72.250/ut_app/index.php?m=User&a=operation_order";
		HttpUtils.post(urlString, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson =new Gson();
				InfoBean infoBean = gson.fromJson(arg2, InfoBean.class);
				if("403".equals(infoBean.code)){
					Utils.showToast(OrderDetailActivity.this, "课程正在进行不能退款，课程结束后再申请退款");
				}else if("402".equals(infoBean.code)){
					Utils.showToast(OrderDetailActivity.this, "描述不能为空");
				}else if("404".equals(infoBean.code)){
					Utils.showToast(OrderDetailActivity.this, "参数有误");
				}else if("401".equals(infoBean.code)){
					Utils.showToast(OrderDetailActivity.this, "操作方式不能为空");
				}else if("400".equals(infoBean.code)){
					Utils.showToast(OrderDetailActivity.this, "订单id为空");
				}else if("0".equals(infoBean.code)){
					Utils.showToast(OrderDetailActivity.this, "操作失败");
				}else if("1".equals(infoBean.code)){
					Utils.showToast(OrderDetailActivity.this, "操作成功");
					dialog.dismiss();
				}else{
					Utils.showToast(OrderDetailActivity.this, "错误码："+infoBean.code);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Utils.showToast(OrderDetailActivity.this, "网络连接异常，请检查网络，重新尝试");
			}
		});
	}
	public void setVisiable(ImageView iv11,ImageView iv22,ImageView iv33){
		iv11.setVisibility(View.GONE);
		iv22.setVisibility(View.GONE);
		iv33.setVisibility(View.GONE);
	}
	
	//显示申请退款Dialog
	private void showSQTKDialog(final String content,final String status) {
		dialog = new Dialog(OrderDetailActivity.this, R.style.tkdialog);
		View v =View.inflate(this, R.layout.dialog_tuikuan, null);
		ll1=(LinearLayout) v.findViewById(R.id.ll1);
		ll2=(LinearLayout) v.findViewById(R.id.ll2);
		ll3=(LinearLayout) v.findViewById(R.id.ll3);
		ll4=(LinearLayout) v.findViewById(R.id.ll4);
		
		iv1=(ImageView) v.findViewById(R.id.iv1);
		iv2=(ImageView) v.findViewById(R.id.iv2);
		iv3=(ImageView) v.findViewById(R.id.iv3);
		iv4=(ImageView) v.findViewById(R.id.iv4);
		
		
		
		tv_cancle=(TextView) v.findViewById(R.id.tv_cancle);
		tv_sure=(TextView) v.findViewById(R.id.tv_sure);
		
		ll1.setOnClickListener(this);
		ll2.setOnClickListener(this);
		ll3.setOnClickListener(this);
		ll4.setOnClickListener(this);
		
		tv_cancle.setOnClickListener(this);
		tv_sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!iv1.isShown()&&!iv2.isShown()&&!iv3.isShown()&&!iv4.isShown()){
					Toast.makeText(OrderDetailActivity.this, "请选择一个原因", 0).show();
					return;
				}
				Utils.showToast(OrderDetailActivity.this, content);
				/**
				 * 申请退款
				 */
				RequestSQTK(content,status);
				
			}
		});
		
		dialog.setContentView(v);
		dialog.show();
	}
}
