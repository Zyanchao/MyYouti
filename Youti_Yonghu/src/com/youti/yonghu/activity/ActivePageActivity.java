package com.youti.yonghu.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;

import u.aly.bu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbToastUtil;
import com.ab.view.sliding.AbSlidingPlayView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.youti_geren.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youti.appConfig.Constants;
import com.youti.appConfig.YoutiApplication;
import com.youti.base.BaseActivity;
import com.youti.utils.DistanceUtils;
import com.youti.utils.HttpUtils;
import com.youti.utils.IntentJumpUtils;
import com.youti.utils.NetTips;
import com.youti.utils.Utils;
import com.youti.view.CustomProgressDialog;
import com.youti.view.TitleBar;
import com.youti.yonghu.adapter.ActiveListAdapter;
import com.youti.yonghu.bean.ActiveBean;
import com.youti.yonghu.bean.Carousel;
import com.youti.yonghu.bean.Hotitem;
import com.youti.yonghu.bean.SingBean;

/**
 * 首页 活动 页
 * @author zyc
 * 2015-2-12
 */
public class ActivePageActivity extends BaseActivity implements OnClickListener{
	
	private Context mContext;
	private List<SingBean> singList;
	private TextView tvNum,tvName,tvTime,tvDistance,TvPrice,tvAddress,end_time;
	private ListView mListView;
	private ActiveListAdapter mAdapter;
	private Button btBook;
	private String userId;
	private String userHeadImageUrl;
	private String userName;
	private RelativeLayout rl_top;
	private TitleBar title;
	private ImageView activeImg;
	CustomProgressDialog dialog;
	private ActiveBean  active;
	private boolean isLogin;
	private String loadUrl;
	String endTime;
	String id ;
	private boolean isSign = false;
	private LatLng lat1,lat2;
	private SharedPreferences sp;
	private Editor editor;
	
	
	private AbSlidingPlayView mAbSlidingPlayView;
	private List<Carousel> mCarousels = new ArrayList<Carousel>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_activie_page);
		
		mContext = this;
		initView();
		id = getIntent().getStringExtra(Constants.KEY_ID);
		dialog = new CustomProgressDialog(this, R.string.laoding_tips,R.anim.frame2);
		dialog.show();
		sp = getSharedPreferences("abc", MODE_PRIVATE);
		editor = sp.edit();
		userId=((YoutiApplication)(mContext.getApplicationContext())).myPreference.getUserId();
		initListener();
		getData();
	}
	
	/**
	* @Title: initView 
	* @Description: TODO(初始化 组件) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void initView() {
		title = (TitleBar) findViewById(R.id.index_titlebar);
		tvNum = (TextView) findViewById(R.id.tv_num);
		tvName = (TextView) findViewById(R.id.tv_active_name);
		tvTime = (TextView) findViewById(R.id.tv_time);
		tvAddress = (TextView) findViewById(R.id.tv_arddess);
		tvDistance = (TextView) findViewById(R.id.tv_distance);
		TvPrice = (TextView) findViewById(R.id.tv_price);
		mListView = (ListView) findViewById(R.id.lv_baoming);
		btBook = (Button) findViewById(R.id.bt_book);
		rl_top = (RelativeLayout) findViewById(R.id.rl_top);
		end_time = (TextView) findViewById(R.id.tv_end_time);
		//activeImg = (ImageView) findViewById(R.id.active_Img);
		
		mAbSlidingPlayView = (AbSlidingPlayView) findViewById(R.id.mAbSlidingPlayView);
		mAbSlidingPlayView.setNavHorizontalGravity(Gravity.CENTER);
		mAbSlidingPlayView.startPlay();
		//mAbSlidingPlayView.setParentListView(mListView);
	}
	

	
	private void initListener() {
		btBook.setOnClickListener(this);
		rl_top.setOnClickListener(this);
		mListView.setOnItemClickListener(new MyItemClickListener());
	}
	
	private void setData(){
		
		String img[] = active.getImg();
		if(img.length>0){
		for (int i = 0; i < img.length; i++) {
			final View mView = View.inflate(ActivePageActivity.this,R.layout.play_view_item, null);
			ImageView mView2 = (ImageView) mView.findViewById(R.id.mPlayImage);
			imageLoader.displayImage(Constants.PIC_CODE+img[i], mView2, options);
			mAbSlidingPlayView.addView(mView);
		}
		}
		title.setTitleBarTitle(active.getVal());
		tvName.setText(active.getVal());
		tvTime.setText(active.getStart_time());
		String time = active.getEnd_time();
		tvNum.setText(active.getBaoming_num()=="0"?"亲！快来抢沙发啦！":active.getBaoming_num());
		if(active.getWeek().equals("0")){
			endTime = "周日"+time;
		}else if(active.getWeek().equals("1")){
			endTime = "周一"+time;
		}else if(active.getWeek().equals("2")){
			endTime = "周二"+time;
		}else if(active.getWeek().equals("3")){
			endTime = "周三"+time;
		}else if(active.getWeek().equals("4")){
			endTime = "周四"+time;
		}else if(active.getWeek().equals("5")){
			endTime = "周五"+time;
		}else if(active.getWeek().equals("6")){
			endTime = "周六"+active.getEnd_time();
		}
		tvAddress.setText(active.getAddress());
		end_time.setText(endTime);
		TvPrice.setText(active.getPrice()+"元");
		//imageLoader.displayImage(Constants.PIC_CODE+active.getImg(), activeImg);
		lat1 = new LatLng(Double.parseDouble(active.getJd()), Double.parseDouble(active.getWd()));
		lat2 = new LatLng(Double.parseDouble(YoutiApplication.getInstance().myPreference.getLocation_j()),Double.parseDouble(YoutiApplication.getInstance().myPreference.getLocation_w()));
		Double  dis = DistanceUtil.getDistance(lat1, lat2);
		String  distance = DistanceUtils.formatDistance(dis);
		tvDistance.setText(distance);
	}
	
	/**
	* @Title: getData 
	* @Description: TODO(获取 活动 内容 ) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void getData() {
		if(!NetTips.isNetTips(mContext)){
			//读取缓存 信息
			if(cache.getAsString("active")!=null){
				setData();
			}
			return;
		}
		
		RequestParams params = new RequestParams(); //
		params.put(Constants.KEY_USER_ID, YoutiApplication.getInstance().myPreference.getUserId());
		params.put(Constants.KEY_ID, id);
		HttpUtils.post(Constants.HOME_POPULAR_EVENT, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if(arg0==200){
					dialog.dismiss();
					String json = new String(arg2);
					JSONObject jsonObject = JSON.parseObject(json);
					String code = jsonObject.getString("code");
					if("1".equals(code)){
//						com.alibaba.fastjson.JSONObject object = JSON.parseObject(response.toString());
						com.alibaba.fastjson.JSONObject jsonObject1 = jsonObject.getJSONObject("list");
						active = JSON.parseObject(jsonObject1.toString(), ActiveBean.class);
						singList = new ArrayList<SingBean>();
						singList.clear();
						singList = active.getUser_list();
						if(cache.getAsString("active")==null){
							cache.put("active", active);
						}
						setData();
						refreshList();
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Toast.makeText(getApplicationContext(), "失败", Toast.LENGTH_LONG).show();
			}
			public void onFinish() {
				dialog.dismiss();
			};
		});
	}
	

	/**
	* @Title: refreshList 
	* @Description: TODO(报名列表) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private  void  refreshList(){
		
		if(singList.size()==0){
			tvNum.setText("亲！快来抢沙发啦！");
			return;
		}
		tvNum.setText(singList.size()+" 人已报名");
		mAdapter = new ActiveListAdapter(this, singList);
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.bt_book:
			
			isLogin=((YoutiApplication)(mContext.getApplicationContext())).myPreference.getHasLogin();
			if(isLogin){
				
				if(sp.getString("active_id"+id, "0").equals(userId+id)){
					Utils.showToast(mContext, "您已经报过名");
					return;
				}
				if(Float.parseFloat(active.getPrice())==0){//价格 等于 0
				submaitBook();
				}else {
					Bundle bundle = new Bundle();
					bundle.putString("price", active.getPrice());
					bundle.putString("val", active.getVal());
					bundle.putString("alrTime", active.getAlr_time());
					bundle.putString("id", active.getId());
					IntentJumpUtils.nextActivity(OrderActiveActivity.class, this, bundle);
					finish();
				}
			}else{
				IntentJumpUtils.nextActivity(LoginActivity.class, this, null);
			}
			break;
			
		case R.id.rl_top:
			Bundle bundle = new Bundle();
			//bundle.putString("title", active.getVal());
			bundle.putString("url", active.getDe_url());
			IntentJumpUtils.nextActivity(ActiveDetailActivity.class, this, bundle);
			
			break;

		default:
			break;
		}
	}


	private void singActive() {
		//报名 人员
		SingBean vb = new SingBean();
		userHeadImageUrl=((YoutiApplication)(mContext.getApplicationContext())).myPreference.getHeadImgPath();
		userName=((YoutiApplication)(mContext.getApplicationContext())).myPreference.getUserName();
		SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm");     
		Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间     
		String   str   =   formatter.format(curDate);  
		vb.setUser_id(userId);
		vb.setHead_img(userHeadImageUrl);
		vb.setUser_name(userName);
		vb.setSign_time(str);
		singList.add(vb);
		mAdapter = new ActiveListAdapter(mContext, singList);
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
		tvNum.setText(singList.size()+" 少人已报名");
	}
	
	
	class MyItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent =new Intent(mContext,OtherPersonCenterActivity.class);
			intent.putExtra("user_id", singList.get(position).getUser_id());
			mContext.startActivity(intent);
			
		}
		
	}
	
	
	 public final String mPageName="ActivePageActivity";
	    
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
	
	/**
	* @Title: submaitBook 
	* @Description: TODO(提交报名信息) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void submaitBook(){
		
		
		
		
		
		RequestParams params = new RequestParams(); //
		params.put("id", id);
		params.put("user_id", YoutiApplication.getInstance().myPreference.getUserId());
		HttpUtils.post(Constants.HOME_POPULAR_BOOK, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if(arg0==200){
					dialog.dismiss();
					String json = new String(arg2);
					JSONObject jsonObject = JSON.parseObject(json);
					String code = jsonObject.getString("code");
					if (code.equals("1")) {
						singActive();
						Toast.makeText(abApplication, "恭喜您报名成功", 1000).show();
						editor.putString("active_id"+id, userId+id);
						editor.commit();
					}else if(code.equals("5")){
						editor.putString("active_id"+id, userId+id);
						editor.commit();
						Toast.makeText(abApplication, "您已经报过名", 1000).show();
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Toast.makeText(getApplicationContext(), "失败", Toast.LENGTH_LONG).show();
			}
			public void onFinish() {
				dialog.dismiss();
			};
		});
		
	}
}
