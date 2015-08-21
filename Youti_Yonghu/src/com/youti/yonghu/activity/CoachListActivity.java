package com.youti.yonghu.activity;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ab.bitmap.AbImageDownloader;
import com.ab.fragment.AbLoadDialogFragment;
import com.ab.util.AbLogUtil;
import com.ab.util.AbToastUtil;
import com.ab.util.AbWifiUtil;
import com.ab.view.sliding.AbSlidingPlayView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.youti_geren.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.Constants;
import com.youti.appConfig.YoutiApplication;
import com.youti.base.BaseActivity;
import com.youti.utils.DistanceUtils;
import com.youti.utils.HttpUtils;
import com.youti.utils.IntentJumpUtils;
import com.youti.utils.NetTips;
import com.youti.utils.PopWindowUtil;
import com.youti.utils.StringUtil;
import com.youti.utils.Utils;
import com.youti.view.CustomProgressDialog;
import com.youti.view.TitleBar;
import com.youti.view.XListView;
import com.youti.view.XListView.IXListViewListener;
import com.youti.yonghu.adapter.CoachListAdapter;
import com.youti.yonghu.adapter.CoachListAdapterTest;
import com.youti.yonghu.adapter.FilerValAdapter;
import com.youti.yonghu.adapter.FilterItenAdapter;
import com.youti.yonghu.adapter.FilterPriceAdapter;
import com.youti.yonghu.adapter.FilterRegionAdapter;
import com.youti.yonghu.bean.Carousel;
import com.youti.yonghu.bean.CoachBean;
import com.youti.yonghu.bean.CoachTopBean;
import com.youti.yonghu.bean.FilterItem;
import com.youti.yonghu.bean.FilterLists;
import com.youti.yonghu.bean.FilterPrice;
import com.youti.yonghu.bean.FilterRegion;
import com.youti.yonghu.bean.FilterVal;

/**
 * 教练 列表界面
 * 
 * @author zyc
 */
public class CoachListActivity extends BaseActivity implements OnClickListener,
		IXListViewListener {
	
	XListView mListView;
	TextView footerview;
	View pview,popView,rlSearch;
	private ImageView imSearch;
	private EditText etSearch;
	View titleBar;
	private Handler mHander;
	private LinearLayout llFooter;
	private LinearLayout llOne;
	private LinearLayout llFour;
	private LinearLayout llThree;
	private LinearLayout llTwo;
	
	private ImageView ivOne,ivTwo,ivThree,ivFour;
	private TextView tvThree,tvTwo;
	private ImageView imgOne,imgTwo,imgThree,imgFour,imgTou;//头条 教练 头像
	//private ImageView topOne,topTwo,topThree,topFour;//头条 教练 名次
	private AbImageDownloader mAbImageLoader = null;
	private AbSlidingPlayView mAbSlidingPlayView;
	private List<Carousel> mCarousels = new ArrayList<Carousel>();
	private List<CoachBean> mCoachList = new ArrayList<CoachBean>();

	private List<CoachBean> mCoachTopList = new ArrayList<CoachBean>();
	private CoachTopBean topBean;
	private CoachListAdapterTest mAdapter;
	
	private FilterItenAdapter fAdapter;
	private FilterItem filterItem;
	ListView mValList,mRgionList,mItemList;
	private List<FilterVal> filterVals = new ArrayList<FilterVal>();
	private List<FilterPrice> filterPrices = new ArrayList<FilterPrice>();
	private List<FilterRegion> filterRegion = new ArrayList<FilterRegion>();
	private FilerValAdapter filterValAdapter;
	private FilterRegionAdapter filterRegionAdapter;
	private FilterPriceAdapter filterPriceAdapter;
	private FilterLists filterLists ;
	PopWindowUtil popu;
	boolean firstin = true;
	boolean islast = false;
	boolean flagLoadMore = false;
	private int buttomHeight,screenWidth,screenHeight;
	private int currentPage=1,//当期页码
	             pageCount = 1,//总页数
                 pageSize = 15;//每页数据量
	FrameLayout fl_videocontent;
	CustomProgressDialog dialog;
	String  user_id,val = "",price = "",server_area = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coach_list);
		titleBar = (View) findViewById(R.id.index_titlebar);
		TextView title = (TextView) titleBar.findViewById(R.id.tv_title);
		title.setText("教练");
		user_id=YoutiApplication.getInstance().myPreference.getUserId();
		val = getIntent().getExtras().getString("val");
		if(!AbWifiUtil.isConnectivity(this)){
				return;
		}
		
		initView();
		dialog = new CustomProgressDialog(this, R.string.laoding_tips,R.anim.frame2);
		initListenter();
		getToutiao();
		
		getCoachLsit(flagLoadMore,val,price,server_area);
		getFilterItem();
		setListener();
		mListView.setAdapter(new CoachListAdapter(this, mCoachList));
		screenWidth = DistanceUtils.getScreenWidth(abApplication);
	}

	public final String mPageName="CoachListActivity";
	private ImageView mIconSearch;
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
	
	private void getCoachLsit(final Boolean flagLoadMore,String val,String price,String server_area) {
		
		if(!NetTips.isNetTips(mContext)){
			/**
			 * 无网络 获取缓存数据
			 */
			if(cache.getAsString("CoachList")!=null){
				String hotcourse = cache.getAsString("CoachList");
			
				com.alibaba.fastjson.JSONObject object = JSON
						.parseObject(hotcourse);
				com.alibaba.fastjson.JSONArray jsonArray = object
						.getJSONArray("list");
				mCoachList = JSON.parseArray(jsonArray.toString(), CoachBean.class);
				
				mAdapter = new CoachListAdapterTest(CoachListActivity.this, mCoachList,mListView,false);
				mListView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				return;
			}
		}
		
		/**
		 * 请求数据 
		 */
		RequestParams params = new RequestParams();
		params.put("page", currentPage);
		params.put("user_id", user_id);
		params.put("val", val);
		params.put("price", price);
		params.put("server_area", server_area);
		params.put("jd", YoutiApplication.getInstance().myPreference.getLocation_j());
		params.put("wd", YoutiApplication.getInstance().myPreference.getLocation_w());
		HttpUtils.post(Constants.COACH_LIST, params,
				new JsonHttpResponseHandler() {
					public void onStart() {
						super.onStart();
					}

					public void onFailure(int statusCode,
							org.apache.http.Header[] headers,
							java.lang.Throwable throwable,
							org.json.JSONObject errorResponse) {
						dialog.dismiss();
						View v = View.inflate(CoachListActivity.this,R.layout.layout_error, null);
						fl_videocontent.addView(v);
						Utils.showToast(CoachListActivity.this, "获取网络失败，请稍后再试");
					};

					public void onFinish() {
						dialog.dismiss();
					};

					public void onSuccess(int statusCode,
							org.apache.http.Header[] headers,
							org.json.JSONObject response) {
						dialog.dismiss();
						try {
							//热门教练
							String state = response.getString("code");
							if (state.equals("1")) {
								com.alibaba.fastjson.JSONObject object = JSON
										.parseObject(response.toString());
								com.alibaba.fastjson.JSONArray jsonArray = object
										.getJSONArray("list");
								mCoachList = JSON.parseArray(jsonArray.toString(), CoachBean.class);
								if(cache.getAsString("CoachList")==null){
									cache.put("CoachList", response.toString());
								}

								String str="";
								for(int i=0;i<mCoachList.size();i++){
									str += mCoachList.get(i).coach_id+"&&&";
								}

								if(mAdapter==null){
									mAdapter = new CoachListAdapterTest(CoachListActivity.this, mCoachList,mListView,false);
									mListView.setAdapter(mAdapter);
									mAdapter.notifyDataSetChanged();
								}else{
									if(flagLoadMore){
										mAdapter.addAndRefreshListView(mCoachList);
										if(mCoachList.size()==0){
											AbToastUtil.showToast(getApplicationContext(), "没有更多数据了...");
										}
									}else{
										mAdapter.refreshListView(mCoachList);
									}
								}

							} else if(state.equals("0")){
									AbToastUtil.showToast(getApplicationContext(), "没有更多数据了...");
							}
						} catch (Exception e) {
							AbLogUtil.d(mContext, e.toString());
						}
					};
				});
	}



	/**
	 * 获取 底部 筛选条件
	 */

	private void getFilterItem() {
		/**
		 * 请求数据
		 */
		RequestParams params = new RequestParams();
		HttpUtils.post(Constants.HOME_FLITER_ITEM, params,
				new JsonHttpResponseHandler() {
					public void onStart() {
						super.onStart();
					}

					public void onFailure(int statusCode,
							org.apache.http.Header[] headers,
							java.lang.Throwable throwable,
							org.json.JSONObject errorResponse) {

						if (cache.getAsString("HotCoachCourse") != null) {
							dialog.dismiss();
							String hotcourse = cache.getAsString("HotCoachCourse");

						}
					};

					public void onFinish() {
						dialog.dismiss();
					};

					public void onSuccess(int statusCode,
							org.apache.http.Header[] headers,
							org.json.JSONObject response) {
						dialog.dismiss();

						try {
							// 热门教练
							String state = response.getString("code");
							if (state.equals("1")) {
								com.alibaba.fastjson.JSONObject object = JSON.parseObject(response.toString());
								JSONObject jsonObject = object.getJSONObject("list");
								filterLists = JSON.parseObject(jsonObject.toString(), FilterLists.class);

								filterPrices = filterLists.getPrice_list();
								filterRegion = filterLists.getRegion_list();
								filterVals = filterLists.getVal_list();
								
								/*filterValAdapter = new FilerValAdapter(mContext, filterVals);
								mValList.setAdapter(filterValAdapter);
								
								fAdapter = new FilterItenAdapter(getApplicationContext(), filterItems);
								mItemList.setAdapter(fAdapter);*/
							} else {
							}
						} catch (Exception e) {

						}
					};
				});
	}

	private void initView() {
		
		llFooter = (LinearLayout) findViewById(R.id.ll_footer);
		llOne = (LinearLayout) findViewById(R.id.footer_one);
		llThree = (LinearLayout) findViewById(R.id.footer_three);
		llTwo = (LinearLayout) findViewById(R.id.footer_two);
		ivOne = (ImageView) findViewById(R.id.im_one);
		ivTwo = (ImageView) findViewById(R.id.im_two);
		ivThree = (ImageView) findViewById(R.id.im_three);
		//ivFour = (ImageView) findViewById(R.id.im_four);
		tvTwo = (TextView) findViewById(R.id.tv_two);
		tvThree = (TextView) findViewById(R.id.tv_three);
		//TvFour = (TextView) findViewById(R.id.tv_four);
		tvTwo.setText("价 格");
		tvThree.setText("区 域");
		
		View mPlayView = mInflater.inflate(R.layout.coach_list_head, null);
		imgOne = (ImageView) mPlayView.findViewById(R.id.img_one);
		imgTwo = (ImageView) mPlayView.findViewById(R.id.img_two);
		imgThree = (ImageView) mPlayView.findViewById(R.id.img_three);
		imgFour = (ImageView) mPlayView.findViewById(R.id.img_four);
		imgTou = (ImageView) mPlayView.findViewById(R.id.img_toutiao);
		mIconSearch = (ImageView)findViewById(R.id.title_search);
		mListView = (XListView) findViewById(R.id.kc_list);
		
		mHander = new Handler();
		
		pview = mInflater.inflate(R.layout.pop_list, null);
		popView = pview.findViewById(R.id.mLayout);
		mItemList = (ListView) pview.findViewById(R.id.lv_pop);
		
		rlSearch = mInflater.inflate(R.layout.serach_bar, null);
		imSearch = (ImageView) rlSearch.findViewById(R.id.im_search);
		etSearch = (EditText) rlSearch.findViewById(R.id.et_search);
		
		fl_videocontent=(FrameLayout) findViewById(R.id.fl_videocontent);
		/*// 设置监听器
		refresh_view.getHeaderView().setHeaderProgressBarDrawable(
						this.getResources().getDrawable(R.drawable.progress_circular));
		refresh_view.getFooterView().setFooterProgressBarDrawable(
						this.getResources().getDrawable(R.drawable.progress_circular));*/
		mListView.addHeaderView(mPlayView);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this);
	}

	private void initListenter() {
		llOne.setOnClickListener(this);
		//llFour.setOnClickListener(this);
		llThree.setOnClickListener(this);
		llTwo.setOnClickListener(this);
		mIconSearch.setOnClickListener(this);
		imSearch.setOnClickListener(this);
		imgOne.setOnClickListener(this);
		imgTwo.setOnClickListener(this);
		imgThree.setOnClickListener(this);
		imgFour.setOnClickListener(this);
		imgTou.setOnClickListener(this);
	}
	
	/**
	 * 
	* @Title: getToutiao 
	* @Description: TODO(获取 今日头条教练 信息) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void getToutiao() {
		
		RequestParams params = new RequestParams(); //
		params.put("number",4);
		HttpUtils.post(Constants.COACH_TOP_LIST, params,
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
							response.toString();
							String state = response.getString("code");
							String info = response.getString("info");
							if (state.equals("1")) {
								com.alibaba.fastjson.JSONObject object = JSON.parseObject(response.toString());
								com.alibaba.fastjson.JSONObject obj = object.getJSONObject("list");
								topBean = obj.parseObject(obj.toString(), CoachTopBean.class);
								mCoachTopList = topBean.getList();
								imageLoader.displayImage(Constants.PIC_CODE+mCoachTopList.get(0).getHead_img(), imgOne, options);
								imageLoader.displayImage(Constants.PIC_CODE+mCoachTopList.get(1).getHead_img(), imgTwo, options);
								imageLoader.displayImage(Constants.PIC_CODE+mCoachTopList.get(2).getHead_img(), imgThree, options);
								imageLoader.displayImage(Constants.PIC_CODE+mCoachTopList.get(3).getHead_img(), imgFour, options);
								
							} else {

								AbToastUtil.showToast(mContext, R.string.data_tips);
							}
						} catch (Exception e) {

						}
					};
			});

	}
	
	
	private void setListener() {
		mListView.setOnItemClickListener(new ItemtClickListener());
	}

	private class ItemtClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			if(position==1){
				
			}else {
				CoachBean bean  = new CoachBean();
				TextView tv = (TextView) view.findViewById(R.id.coach_tv_price);
				bean = (CoachBean) tv.getTag();
				Bundle bundle = new Bundle();
				bundle.putString(Constants.KEY_ID, bean.getCoach_id());
				bundle.putString(Constants.KEY_CHAT_TEL, bean.getCoach_tel());
				bundle.putString(Constants.KEY_CHAT_USERNAME, bean.getCoach_name());
				bundle.putString(Constants.KEY_CHAT_AVATAR, bean.getHead_img());
				IntentJumpUtils.nextActivity(CoachDetailActivity.class, CoachListActivity.this, bundle);
			}

		}
	}
	
	
	
	/**
	 * 计算 底部菜单 高度
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		
		ViewTreeObserver vto = llFooter.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				Rect rect = new Rect();
				getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);// /取得整个视图部分,注意，如果你要设置标题样
				int top = rect.top;// //状态栏的高度，所以rect.height,rect.width分别是系统的高度的宽度
				View v = getWindow().findViewById(Window.ID_ANDROID_CONTENT);// /获得根视图
				buttomHeight = llFooter.getHeight();
				screenHeight = DistanceUtils.getScreenHeight(abApplication);
				float ss = buttomHeight/screenHeight;
				Log.d("tag", ss+"");
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.footer_one:
			filterValAdapter = new FilerValAdapter(mContext, filterVals);
			mItemList.setAdapter(filterValAdapter);
			llOne.setBackgroundResource(R.color.chosed_ll_buttom);
			llTwo.setBackgroundResource(R.color.white);
			llThree.setBackgroundResource(R.color.white);
			//llFour.setBackgroundResource(R.color.white);
			ivOne.setBackgroundResource(R.drawable.jl_arrow_h);
			ivTwo.setBackgroundResource(R.drawable.jl_arrow);
			ivThree.setBackgroundResource(R.drawable.jl_arrow);
			//ivFour.setBackgroundResource(R.drawable.jl_arrow);
			popu = new PopWindowUtil(abApplication, screenWidth*1/3, screenHeight*7/12,0,buttomHeight, pview, popView);
			mItemList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					TextView v = (TextView) view.findViewById(R.id.tv_item_name);
					val = (String) v.getTag();
					PopWindowUtil.colsePop();
					getCoachLsit(flagLoadMore,val,price,server_area);
				}
			});
			break;

		case R.id.footer_two:
			filterPriceAdapter = new FilterPriceAdapter(mContext, filterPrices);
			mItemList.setAdapter(filterPriceAdapter);
			llOne.setBackgroundResource(R.color.white);
			llTwo.setBackgroundResource(R.color.white);
			llThree.setBackgroundResource(R.color.white);
			//llFour.setBackgroundResource(R.color.chosed_ll_buttom);
			ivOne.setBackgroundResource(R.drawable.jl_arrow);
			ivTwo.setBackgroundResource(R.drawable.jl_arrow_h);
			ivThree.setBackgroundResource(R.drawable.jl_arrow);
			//ivFour.setBackgroundResource(R.drawable.jl_arrow);
			popu = new PopWindowUtil(abApplication, screenWidth*1/3, screenHeight*7/12,screenWidth*1/3,buttomHeight, pview, popView);
			mItemList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					TextView v = (TextView) view.findViewById(R.id.tv_item_name);
					price = (String) v.getTag();
					PopWindowUtil.colsePop();
					getCoachLsit(flagLoadMore,val,price,server_area);
					
				}
			});
			break;
		case R.id.footer_three:
			filterRegionAdapter = new FilterRegionAdapter(mContext,filterRegion);
			mItemList.setAdapter(filterRegionAdapter);
			llOne.setBackgroundResource(R.color.white);
			llTwo.setBackgroundResource(R.color.white);
			llThree.setBackgroundResource(R.color.chosed_ll_buttom);
			//llFour.setBackgroundResource(R.color.white);
			ivOne.setBackgroundResource(R.drawable.jl_arrow);
			ivTwo.setBackgroundResource(R.drawable.jl_arrow);
			ivThree.setBackgroundResource(R.drawable.jl_arrow_h);
			//ivFour.setBackgroundResource(R.drawable.jl_arrow);
			popu = new PopWindowUtil(abApplication, screenWidth*1/3, screenHeight*7/12,screenWidth*2/3,buttomHeight, pview, popView);
			mItemList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					TextView v = (TextView) view.findViewById(R.id.tv_item_name);
					server_area = (String) v.getTag();
					PopWindowUtil.colsePop();
					getCoachLsit(flagLoadMore,val,price,server_area);
					
				}
			});
			break;
		/*case R.id.footer_four:
			llOne.setBackgroundResource(R.color.white);
			llTwo.setBackgroundResource(R.color.chosed_ll_buttom);
			llThree.setBackgroundResource(R.color.white);
			//llFour.setBackgroundResource(R.color.white);
			ivOne.setBackgroundResource(R.drawable.jl_arrow);
			ivTwo.setBackgroundResource(R.drawable.jl_arrow);
			ivThree.setBackgroundResource(R.drawable.jl_arrow);
			//ivFour.setBackgroundResource(R.drawable.jl_arrow_h);
			popu = new PopWindowUtil(abApplication, screenWidth*1/4, screenHeight*7/12,screenWidth*3/4,buttomHeight, pview, popView);
			break;
			*/
			
		case R.id.title_search:
			
			popu = new PopWindowUtil(abApplication, screenWidth, screenHeight*1/10, rlSearch, titleBar);
			/*if(isSearch){
				rl_search.setVisibility(View.VISIBLE);
				isSearch = false;
			}else {
				rl_search.setVisibility(View.GONE);
				isSearch = true;
			}*/
			
			break;
		case R.id.im_search:
			
			PopWindowUtil.colsePop();
			val = etSearch.getText().toString();
			getCoachLsit(flagLoadMore, val, "", "");
			break;
			
		case R.id.img_one:
			getDetailTou(0);
			break;
			
		case R.id.img_two:
			getDetailTou(1);
			break;
		case R.id.img_three:
			getDetailTou(2);
			break;
		case R.id.img_four:
			getDetailTou(3);
			break;
		case R.id.img_toutiao:
			startActivity(ToutiaoCoachActivity.class);
			break;
		default:
			break;
		}
	}

	private void getDetailTou(int i) {
		if(mCoachTopList.get(i).getCoach_id()==null){
			return;
		}
		Bundle b = new Bundle();
		b.putString(Constants.KEY_ID, mCoachTopList.get(i).getCoach_id());
		b.putString(Constants.KEY_CHAT_TEL, mCoachTopList.get(i).getCoach_tel());
		b.putString(Constants.KEY_CHAT_USERNAME, mCoachTopList.get(i).getCoach_name());
		b.putString(Constants.KEY_CHAT_AVATAR, mCoachTopList.get(i).getHead_img());
		IntentJumpUtils.nextActivity(CoachDetailActivity.class, CoachListActivity.this, b);
	}



	
	protected void loadMore() {
		currentPage++;//每次 点击加载更多，请求页码 +1
		flagLoadMore = true;
		getCoachLsit(flagLoadMore,val,price,server_area);
	}



	@Override
	public void onRefresh() {
		mHander.postDelayed(new Runnable() {//处理耗时 操作
			@Override
			public void run() {
				  Date currentTime = new Date();
				  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
				  String dateString = formatter.format(currentTime);
				  flagLoadMore = false;
				  currentPage = 1;
				  getCoachLsit(flagLoadMore,val,price,server_area);
				  mListView.stopRefresh();  
		          mListView.stopLoadMore();  
		          mListView.setRefreshTime(dateString);  
					}
				}, 1000);
		
	}



	@Override
	public void onLoadMore() {
		mHander.postDelayed(new Runnable() {//处理耗时 操作
			@Override
			public void run() {
				loadMore();
				mListView.stopRefresh();  
		        mListView.stopLoadMore();  
			}
		}, 200);
		
	}


}
