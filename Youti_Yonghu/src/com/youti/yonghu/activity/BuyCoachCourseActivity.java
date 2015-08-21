package com.youti.yonghu.activity;


import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.util.AbDialogUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.youti_geren.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
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
import com.youti.yonghu.adapter.CoursePackageAdapter;
import com.youti.yonghu.bean.CoursePackageBean;
import com.youti.yonghu.bean.CoursePackageBean.ClassPackage;
import com.youti.yonghu.bean.CoursePackageBean.ClassWay;
import com.youti.yonghu.bean.OrderCoach;

/**
 *  购买 私教课程 
 * @author zyc
 * 2015-6-1
 */
public class BuyCoachCourseActivity extends BaseActivity implements OnClickListener{

	
	/** 增加 操作 */
	private static final int OPERATION_TYPE_ADD = 0;
	/** 减少 操作 */
	private static final int OPERATION_TYPE_SUB = 1;
	/** 门票数量"减少"按钮 */
	ImageView ivCounterSub;
	/** 门票数量"增加"按钮 */
	ImageView ivCounterAdd,iv_paytype,iv_yuetype;
	private ImageView ivSerach;
	private TextView tvCounter;//时长
	private TextView tvCourseType;//上课方式
	private TextView tvCoursePrice;//上课费用
	private TextView tvAllPrice;//课程总价
	private TextView tvTotalPrice;//总价
	private TextView tvzkPrice;//折扣
	private TextView tvPackage;//tv_package
	private String zkou;
	private RelativeLayout rlSetCount;
	//LinearLayout layRg;
	LinearLayout layAddress;
	String courseTypeFlag = "用户上门" ;
	
	float coursePrice = 100 ;
	int count = 1;
	float totalPrice = 100 ;
	String payType;
	EditText addAddress;
	private Button btNext;
	RadioGroup rgCourseType;
	RadioButton rbCourseOne,rbCourseTwo,rbCourseThree;
	PopWindowCoursePg coursePg;
	FrameLayout coursePgLayout;
	ListView coursePglist;
	private LinearLayout ll_course_packge;
	private CoursePackageAdapter fAdapter;
	private CoursePackageBean packageBean;
	private OrderCoach orderCoach;
	private  Button bt_next;
	private ClassPackage page;
	private ClassWay wayBean ;
	private List<ClassPackage> pagList;
	private String priceCoach;
	private String priceStu;
	private String priceDis;
	private String price,long_time,address,class_way = "1";
	private float zekou =  1.0f;
	CustomProgressDialog dialog;
	
	private String id;
	private String userId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buy_coach_course);
		initView();
		dialog = new CustomProgressDialog(this, R.string.laoding_tips,R.anim.frame2);
		dialog.show();
		userId = YoutiApplication.getInstance().myPreference.getUserId();
		id = getIntent().getExtras().getString(Constants.KEY_ID);
		initListener();
		getCoursePackage();
		
	}

	private void initView() {
		ivSerach = (ImageView) findViewById(R.id.title_search);
		ivSerach.setVisibility(View.GONE);
		ivCounterSub = (ImageView) findViewById(R.id.iv_counter_sub);
		ivCounterAdd = (ImageView) findViewById(R.id.iv_counter_add);
		tvCounter = (TextView) findViewById(R.id.tv_counter_txt);
		iv_paytype = (ImageView) findViewById(R.id.iv_paytype);
		iv_yuetype = (ImageView) findViewById(R.id.iv__yue_paytype);
		tvCourseType = (TextView) findViewById(R.id.tv_class_type);
		tvCoursePrice = (TextView) findViewById(R.id.tv_course_price);
		tvAllPrice = (TextView) findViewById(R.id.tv_all_price);
		tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
		tvzkPrice = (TextView) findViewById(R.id.tv_zekou_price);
		tvPackage = (TextView) findViewById(R.id.tv_package);
		rlSetCount = (RelativeLayout) findViewById(R.id.rl_set_count);
		addAddress = (EditText) findViewById(R.id.bt_add_address);
		btNext = (Button) findViewById(R.id.bt_next);
		rgCourseType = (RadioGroup) findViewById(R.id.course_type_rg);
		rbCourseOne = (RadioButton) findViewById(R.id.rb_classtype_one);
		rbCourseTwo = (RadioButton) findViewById(R.id.rb_classtype_two);
		rbCourseThree = (RadioButton) findViewById(R.id.rb_classtype_three);
		layAddress = (LinearLayout) findViewById(R.id.layout_address);
		ll_course_packge = (LinearLayout) findViewById(R.id.ll_course_package);
		coursePgLayout = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.sort_list, null);
		coursePglist = (ListView) coursePgLayout.findViewById(R.id.sort_list);
		bt_next = (Button) findViewById(R.id.bt_next);
		
	}
	
	public final String mPageName="BuyCoachCourseActivity";
	@Override
	protected void onResume() {tvCounter.setText(count + "");
		tvAllPrice.setText(count*totalPrice + "元");
		tvCoursePrice.setText(coursePrice+"元 / 小时");
		tvCourseType.setText(courseTypeFlag);
		super.onResume();
		//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(this);
		
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(this);
	}
	
	
	private void initListener() {
		ivCounterSub.setOnClickListener(this);
		ivCounterAdd.setOnClickListener(this);
		btNext.setOnClickListener(this);
		ll_course_packge.setOnClickListener(this);
		bt_next.setOnClickListener(this);
		rgCourseType.setOnCheckedChangeListener(new CourseTypeChangeListener());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_counter_sub:
			
			changeCourseCount(OPERATION_TYPE_SUB);
			
			break;

		case R.id.iv_counter_add:
			changeCourseCount(OPERATION_TYPE_ADD);
			break;
		case R.id.bt_next:
			if(!YoutiApplication.getInstance().myPreference.getHasLogin()){
				IntentJumpUtils.nextActivity(LoginActivity.class, BuyCoachCourseActivity.this);
			}
			submitOrder();
			break;
			
		case R.id.ll_course_package:
			
			pagList = packageBean.getYouhui();
			
			if(pagList!=null&pagList.size()>0){
				fAdapter = new CoursePackageAdapter(getApplicationContext(), pagList);
				coursePglist.setAdapter(fAdapter);
				coursePg = new PopWindowCoursePg(this,ScreenUtils.getScreenWidth(this),ScreenUtils.getScreenHeight(this)*2/7, coursePgLayout,ll_course_packge);
				coursePglist.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						TextView v = (TextView) view.findViewById(R.id.tv_item_name);
						page = (ClassPackage) v.getTag();
						zekou = Float.parseFloat(page.getYouhui_discount());
						count = Integer.parseInt(page.getLong_time());
						if(position>0){
							rlSetCount.setVisibility(View.GONE);
							tvPackage.setText(page.getYouhui_desc());
						}else{
							rlSetCount.setVisibility(View.VISIBLE);
							tvPackage.setText(page.getYouhui_desc());
						}
						calculateCourseTotalPrice(count,coursePrice,zekou,courseTypeFlag);
						PopWindowCoursePg.colsePop();
					}
				});
			}else {
				Utils.showToast(mContext, "该教练暂无优惠包，请自由选择课时");
			}
			break;
		}
	}

	
private void getCoursePackage(){
		
		RequestParams params = new RequestParams(); //
		params.put("user_id",userId);
		params.put("coach_id",id);
		HttpUtils.post(Constants.COURSE_PACKEG, params,
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

					public void onSuccess(int statusCode,
							org.apache.http.Header[] headers,
							org.json.JSONObject response) {
						dialog.dismiss();
						try {
							String state = response.getString("code");
							if(state.equals("1")){
								com.alibaba.fastjson.JSONObject object = JSON.parseObject(response.toString());
								JSONObject jsonObject = object.getJSONObject("list");
								
								packageBean = JSON.parseObject(jsonObject.toString(), CoursePackageBean.class);
								wayBean = packageBean.class_way;
								pagList = packageBean.getYouhui();
							}
							
						} catch (Exception e) {

						}
					};
				});
	}

	/**
	 * @Title: submitOrder
	 * @Description: TODO(提交订单)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */

	private void submitOrder() {

		RequestParams params = new RequestParams(); //
		params.put("user_id",userId);
		params.put("coach_id",id);
		params.put("price",totalPrice);//单价(必传)
		params.put("long_time",count);//时长(必传)
		if(class_way.equals("2")){
		params.put("address",address);//地址
		}
		params.put("class_way",class_way);//上课方式1用户上门2教练上门3协商地点
		params.put("zkou",zekou);//有折扣传 无折扣不传
		
		HttpUtils.post(Constants.ORDER_COACH, params,
				new JsonHttpResponseHandler() {
					public void onStart() {
						super.onStart();
					}

					public void onFailure(int statusCode,
							org.apache.http.Header[] headers,
							java.lang.Throwable throwable,
							org.json.JSONObject errorResponse) {
					};
					
					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						super.onFinish();
					}

					public void onSuccess(int statusCode,
							org.apache.http.Header[] headers,
							org.json.JSONObject response) {
						try {
							/*code：400教练id为空
							code：401用户id为空
							code：402单价为空
							code：403时长为空
							code：404上课方式为空
							code:0下单失败
							code:1下单成功
							*/
							String state = response.getString("code");
							if(state.equals("1")){
								com.alibaba.fastjson.JSONObject object = JSON.parseObject(response.toString());
								JSONObject jsonObject = object.getJSONObject("list");
								orderCoach = JSON.parseObject(jsonObject.toString(), OrderCoach.class);
								Bundle b = new Bundle();
								b.putInt("orderType", Constants.REQUEST_CODE_PAY_COACH);
								b.putSerializable(Constants.KEY_ORDER_COACH, orderCoach);
								IntentJumpUtils.nextActivity(OrderCoachActivity.class, BuyCoachCourseActivity.this, b);
							}
							
						} catch (Exception e) {

						}
					};
				});
	}

	/**
	 * 处理 rg 上课类型 点击事件
	 */
	class CourseTypeChangeListener implements OnCheckedChangeListener{

		

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			
			case R.id.rb_classtype_one://
				
				courseTypeFlag = "用户上门";
				coursePrice = Integer.parseInt(wayBean.getPrice_stu());
				layAddress.setVisibility(View.GONE);
				class_way = "1";
				// 计算总价，改变页面上总价的数字
				calculateCourseTotalPrice(count,coursePrice,zekou,courseTypeFlag);
				strzkou();
				tvzkPrice.setText(strzkou());
				break;

			case R.id.rb_classtype_two://教练上门
				courseTypeFlag = "教练上门";
				coursePrice = Integer.parseInt(wayBean.getPrice_coach());
				layAddress.setVisibility(View.VISIBLE);
				class_way = "2";
				// 计算总价，改变页面上总价的数字
				calculateCourseTotalPrice(count,coursePrice,zekou,courseTypeFlag);
				tvzkPrice.setText(strzkou());
				break;
			case R.id.rb_classtype_three://协商地点
				coursePrice = Integer.parseInt(wayBean.getPrice_discuss());
				courseTypeFlag = "协商地点";
				layAddress.setVisibility(View.GONE);
				class_way = "3";
				// 计算总价，改变页面上总价的数字
				calculateCourseTotalPrice(count,coursePrice,zekou,courseTypeFlag);
				tvzkPrice.setText(strzkou());
				break;
			default:
				break;
			}
		}

		private String strzkou() {
			if(zekou==0.9){
				zkou = "打九折";
			}else if(zekou==0.8){
				zkou = "打八折";
			}else if(zekou==0.7){
				zkou = "打七折";
			}else if(zekou==0.6){
				zkou = "打六折";
			}else if(zekou==0.5){
				zkou = "打五折";
			}else if(zekou==0.4){
				zkou = "打四折";
			}else if(zekou==0.3){
				zkou = "打三折";
			}else if(zekou==0.2){
				zkou = "打二折";
			}else if(zekou==0.1){
				zkou = "打一折";
			}else{
				zkou = "暂无折扣";
			}
			return zkou;
		}
	}

	/**
	 * 计算 课程总价 
	 * courseCount 课时数
	 * coursePrice 对应单价 
	 */
	
	private void calculateCourseTotalPrice(float courseCount,float coursePrice,Float zekou,String typeName) {
		
		tvCourseType.setText(typeName);
		tvCoursePrice.setText(coursePrice+"元/ 小时");
		totalPrice = (float) (courseCount*coursePrice*zekou);
		tvTotalPrice.setText(totalPrice + "元");
		tvAllPrice.setText(totalPrice + "元");
	}
	/**
	 * 添加 或减少 课时
	 * @param operationTypeAdd
	 */

	private void changeCourseCount(int addOrReduceType) {
		
			
		//减少 课时
		if(addOrReduceType==OPERATION_TYPE_SUB){
			if (Integer.valueOf(tvCounter.getText().toString()) > 1) {
				int numReduce = Integer.valueOf(tvCounter.getText().toString())
						-Constants.DEFAULT_NUMBER_ADD_OR_REDUCE;
				count = numReduce;
				tvCounter.setText(count + "");
			}else {
				// 如果数量小于1，则减少按钮的背景设置为灰色，且数字为1
				tvCounter.setText(count + "");
				//ivCounterSub.setClickable(false);
			}
			calculateCourseTotalPrice(count,coursePrice,zekou,courseTypeFlag);
		}
		
		//增加 课时
		if(addOrReduceType==OPERATION_TYPE_ADD){
			if (Integer.valueOf(tvCounter.getText().toString()) <Constants.MAX_NUMBER_ADD_OR_REDUCE) {
				int numAdd = Integer.valueOf(tvCounter.getText().toString())
						+Constants.DEFAULT_NUMBER_ADD_OR_REDUCE;
				count = numAdd;
				tvCounter.setText(count + "");
			}else {
				// 如果数量大于 最大课时限度 ，则增加按钮的背景设置为灰色，且数字为count
				tvCounter.setText(count + "");
				//ivCounterSub.setClickable(false);
			}
			calculateCourseTotalPrice(count,coursePrice,zekou,courseTypeFlag);
		}
		
		// 将新的数量更新到购票车的数据库表中
		//updateTicketQuantityInCartTable(0, quantity);

		// 计算总价，改变页面上总价的数字
		//calculateTicketTotalPrice();
	}

	 
}
