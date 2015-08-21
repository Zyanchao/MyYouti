package com.youti.yonghu.activity;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.Constants;
import com.youti.utils.HttpUtils;
import com.youti.utils.IntentJumpUtils;
import com.youti.utils.Utils;
import com.youti.view.TitleBar;
import com.youti.yonghu.bean.InfoBean;
import com.youti.yonghu.bean.OrderCourse;
import com.youti.yonghu.bean.UCourseOrderBean;
import com.youti.yonghu.bean.UCourseOrderBean.UCourseOrderDetail;

public class CourseOrderDetailActivity extends Activity {
	TitleBar titleBar;
//	String state;
	String order_id;
	LinearLayout ll_pingjia;
	TextView tv_pingjia;
	TextView hasused;
	Button ljfk,qxdd,qxyy,sqtk,scdd;
	ImageView course_img;
	TextView course_title;
	TextView course_price;
	TextView course_begin_time;
	TextView youti_code;
	TextView order_num;
	TextView mobilephone;
	TextView order_time;
	TextView buy_data_phone;
	TextView data_time;
//	LinearLayout ll_count;
//	TextView tv_count;
	LinearLayout ll_price;
	TextView tv_price;
	String status;
	LinearLayout ll_youtiquan;
	LinearLayout ll_bottombutton;
	LinearLayout ll_cc;
	String comment;
	private UCourseOrderDetail list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_courseorderdetailactivity);
		if(getIntent()!=null){
			order_id=getIntent().getStringExtra("order_id");
			status =getIntent().getStringExtra("status");
			comment=getIntent().getStringExtra("comment");
		}
		ll_pingjia=(LinearLayout) findViewById(R.id.ll_pingjia);
		tv_pingjia=(TextView) findViewById(R.id.tv_pingjia);
		ll_cc=(LinearLayout) findViewById(R.id.ll_cc);
		
		
		ljfk=(Button) findViewById(R.id.ljfk);
		qxdd=(Button) findViewById(R.id.qxdd);
		qxyy=(Button) findViewById(R.id.qxyy);
		sqtk=(Button) findViewById(R.id.sqtk);
		scdd=(Button) findViewById(R.id.scdd);
		
		course_img =(ImageView) findViewById(R.id.course_img);
		course_title=(TextView) findViewById(R.id.course_title);
		course_price=(TextView) findViewById(R.id.course_price);
		course_begin_time=(TextView) findViewById(R.id.course_begin_time);
		youti_code=(TextView) findViewById(R.id.youti_code);
		order_num=(TextView) findViewById(R.id.order_num);
		mobilephone=(TextView) findViewById(R.id.mobilephone);
		order_time=(TextView) findViewById(R.id.order_time);
		buy_data_phone=(TextView) findViewById(R.id.buy_data_phone);
		data_time=(TextView) findViewById(R.id.data_time);
//		ll_count=(LinearLayout) findViewById(R.id.ll_count);
//		tv_count=(TextView) findViewById(R.id.tv_count);
		ll_price=(LinearLayout) findViewById(R.id.ll_price);
		tv_price=(TextView) findViewById(R.id.tv_price);
		ll_youtiquan=(LinearLayout) findViewById(R.id.ll_youtiquan);
		ll_bottombutton=(LinearLayout) findViewById(R.id.ll_bottombutton);
		
		hasused=(TextView) findViewById(R.id.hasused);
		titleBar =(TitleBar) findViewById(R.id.index_titlebar);
		titleBar.setSearchGone(true);
		titleBar.setShareGone(false);
		titleBar.setTitleBarTitle("订单详情");
		
		requestData();
		
//		if("0".equals(state)){
//			ll_pingjia.setVisibility(View.GONE);
//			hasused.setText("未使用");
//			hasused.setTextColor(Color.RED);
//			ljfk.setVisibility(View.GONE);
//			qxdd.setVisibility(View.INVISIBLE);
//			qxyy.setVisibility(View.VISIBLE);
//			
//		}else if("1".equals(state)){
//			//ll_pingjia.setVisibility(visibility)
//		}
	}
	
	public final String mPageName="CourseOrderDetailActivity";
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
	protected void setData() {
		ImageLoader.getInstance().displayImage("http://112.126.72.250/ut_app"+list.de_img, course_img);
		course_title.setText(list.title);
		course_price.setText(list.price);
		
		if("0".equals(status)){
			//未支付
			qxdd.setVisibility(View.VISIBLE);
			ljfk.setVisibility(View.VISIBLE);
			ll_pingjia.setVisibility(View.GONE);
			ll_youtiquan.setVisibility(View.GONE);
			buy_data_phone.setText("购买手机号：");
			data_time.setText("下单时间：");
			
		}else if("1".equals(status)){
			//已支付
			ll_pingjia.setVisibility(View.GONE);
			qxdd.setVisibility(View.INVISIBLE);
			qxdd.setClickable(false);
			ljfk.setVisibility(View.GONE);
			sqtk.setVisibility(View.VISIBLE);
			hasused.setText("未使用");
			hasused.setTextColor(Color.RED);
			youti_code.setText(list.ele_code);
			course_begin_time.setText(list.begin_time);
			buy_data_phone.setText("购买手机号：");
			data_time.setText("付款时间：");
			ll_youtiquan.setVisibility(View.VISIBLE);
		}else if("2".equals(status)){
			//已预约
			ll_pingjia.setVisibility(View.GONE);
			qxdd.setVisibility(View.GONE);
			qxdd.setClickable(false);
			ljfk.setVisibility(View.GONE);
			qxyy.setVisibility(View.VISIBLE);
			hasused.setText("未使用");
			hasused.setTextColor(Color.RED);
			youti_code.setText(list.ele_code);
			course_begin_time.setText(list.begin_time);
			tv_price.setVisibility(View.GONE);
			data_time.setText("付款时间：");
			ll_youtiquan.setVisibility(View.VISIBLE);
		}else if("3".equals(status)){
			//退款中
			ll_pingjia.setVisibility(View.GONE);
			ll_youtiquan.setVisibility(View.GONE);
			ll_bottombutton.setVisibility(View.GONE);
			buy_data_phone.setText("购买手机号：");
			data_time.setText("付款时间：");
			
		}else if("4".equals(status)){
			//已退款
			ll_pingjia.setVisibility(View.GONE);
			ll_youtiquan.setVisibility(View.GONE);
			qxdd.setVisibility(View.GONE);
			qxdd.setClickable(false);
			ljfk.setVisibility(View.GONE);
			scdd.setVisibility(View.VISIBLE);
			data_time.setText("付款时间：");
		}else if("5".equals(status)){
			//已取消
			if("1".equals(list.sta)){
				titleBar.setAgainpayVisiable(true);
				titleBar.setAgainpayText("再次预约");
			}else if("0".equals(list.sta)){
				titleBar.setAgainpayVisiable(true);
				titleBar.setAgainpayText("再次购买");
			}else{
				titleBar.setAgainpayVisiable(false);
			}
			ll_pingjia.setVisibility(View.GONE);
			ll_youtiquan.setVisibility(View.GONE);
			qxdd.setVisibility(View.GONE);
			qxdd.setClickable(false);
			ljfk.setVisibility(View.GONE);
			scdd.setVisibility(View.VISIBLE);
			
		}else if("6".equals(status)){
			//已完成
			ll_youtiquan.setVisibility(View.VISIBLE);
			hasused.setText("已使用");
			youti_code.setText(list.ele_code);
			course_begin_time.setText(list.begin_time);
			qxdd.setVisibility(View.GONE);
			qxdd.setClickable(false);
			ljfk.setVisibility(View.GONE);
			scdd.setVisibility(View.VISIBLE);
			
			if("1".equals(comment)){
				tv_pingjia.setText("已评价");
			}else if("0".equals(comment)){
				tv_pingjia.setText("未评价");
			}else{
				ll_pingjia.setVisibility(View.GONE);
			}
			
			if("1".equals(list.sta)){
				titleBar.setAgainpayVisiable(true);
				titleBar.setAgainpayText("再次预约");
			}else if("0".equals(list.sta)){
				titleBar.setAgainpayVisiable(true);
				titleBar.setAgainpayText("再次购买");
			}else{
				titleBar.setAgainpayVisiable(false);
			}
			
		}else{
			Utils.showToast(CourseOrderDetailActivity.this, "错误的状态码");
		}
		
		order_num.setText(list.order_num);
		mobilephone.setText(list.tel_phone);
		order_time.setText(list.add_time);
		tv_price.setText(list.price);
		
		ll_pingjia.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if("1".equals(comment)){
					Intent intent =new Intent(CourseOrderDetailActivity.this,CourseDetailActivity.class);
					intent.putExtra("id", list.course_id);
					intent.putExtra("title", list.title);
					startActivity(intent);
				}else if("0".equals(comment)){
					Intent intent =new Intent(CourseOrderDetailActivity.this,CommentActivity.class);
					intent.putExtra("order_id", list.order_id);
					intent.putExtra("status", list.status);
					intent.putExtra(Constants.KEY_ID, list.course_id);
					intent.putExtra("code", Constants.REQUEST_CODE_COURSE);
					intent.putExtra(Constants.KEY_TITLE, list.title);
					startActivity(intent);
				}
				
				//Utils.showToast(CourseOrderDetailActivity.this, "评价");
			}
		});
		titleBar.getAgainPayView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent =new Intent(CourseOrderDetailActivity.this,CourseDetailActivity.class);
				intent.putExtra("id", list.course_id);
				intent.putExtra("title", list.title);
				startActivity(intent);
			}
		});
		
		qxdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Utils.showToast(CourseOrderDetailActivity.this, "取消订单");
				OperateOrder("2");
			}
		});
		
		ljfk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				OrderCourse orderCourse = new OrderCourse();
				orderCourse.setOrder_id(list.order_num);
				orderCourse.setProject_type(list.title);
				orderCourse.setPrice(list.price);
				orderCourse.setStart_time(list.begin_time);
				b.putInt("orderType", Constants.REQUEST_CODE_PAY_COURSE);
				b.putSerializable(Constants.KEY_ORDER_COURSE, orderCourse);
				IntentJumpUtils.nextActivity(OrderCoachActivity.class, CourseOrderDetailActivity.this, b);
			}
		});
		qxyy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Utils.showToast(CourseOrderDetailActivity.this, "取消预约");
				OperateOrder("1");
			}
		});
		scdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Utils.showToast(CourseOrderDetailActivity.this, "删除订单");
				OperateOrder("3");
			}
		});
		sqtk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Utils.showToast(CourseOrderDetailActivity.this, "申请退款");
				OperateOrder("4");
			}
		});
		
		ll_cc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent =new Intent(CourseOrderDetailActivity.this,CourseDetailActivity.class);
				intent.putExtra("id", list.course_id);
				intent.putExtra("title", list.title);
				startActivity(intent);
				finish();
			}
		});
	}
	/**
	 * 删除订单 申请退款 取消预约 取消订单
	 */
	protected void OperateOrder(String opreateStatus) {
		String str="http://112.126.72.250/ut_app/index.php?m=User&a=cancou_order";
		RequestParams params =new RequestParams ();
		params.put("order_id", order_id);
		params.put("status", opreateStatus);
		HttpUtils.post(str, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson =new Gson();
				InfoBean infoBean = gson.fromJson(arg2, InfoBean.class);
				if("1".equals(infoBean.code)){
					Utils.showToast(CourseOrderDetailActivity.this, "操作成功");
					finish();
				}else if("0".equals(infoBean.code)){
					Utils.showToast(CourseOrderDetailActivity.this, "操作失败");
				}else if("400".equals(infoBean.code)){
					Utils.showToast(CourseOrderDetailActivity.this, "订单id为空");
				}else if("402".equals(infoBean.code)){
					Utils.showToast(CourseOrderDetailActivity.this, infoBean.info);
				}else if("401".equals(infoBean.code)){
					Utils.showToast(CourseOrderDetailActivity.this, "操作方式为空");
				}else{
					Utils.showToast(CourseOrderDetailActivity.this, "错误的返回码");
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Utils.showToast(CourseOrderDetailActivity.this, "网络连接异常");
			}
		});
		
		
	}
	private void requestData() {
		String url ="http://112.126.72.250/ut_app/index.php?m=User&a=course_info";
		RequestParams params =new RequestParams();
		params.put("order_id", order_id);
		HttpUtils.post(url, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson =new Gson();
				UCourseOrderBean uCourseOrderBean = gson.fromJson(arg2, UCourseOrderBean.class);
				if("1".equals(uCourseOrderBean.code)){
//					Utils.showToast(CourseOrderDetailActivity.this, "获得数据");
					list = uCourseOrderBean.list;
					setData();
				}else if("0".equals(uCourseOrderBean.code)){
					Utils.showToast(CourseOrderDetailActivity.this, "无数据");
				}else{
					Utils.showToast(CourseOrderDetailActivity.this, "订单id为空");
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Utils.showToast(CourseOrderDetailActivity.this, "网络连接异常");
			}
		});
		
	}


	


	
}
