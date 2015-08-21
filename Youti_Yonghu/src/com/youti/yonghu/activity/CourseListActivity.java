package com.youti.yonghu.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.bitmap.AbImageDownloader;
import com.ab.util.AbToastUtil;
import com.ab.view.sliding.AbSlidingPlayView;
import com.ab.view.sliding.AbSlidingPlayView.AbOnItemClickListener;
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
import com.youti.utils.Utils;
import com.youti.view.CustomProgressDialog;
import com.youti.view.XListView;
import com.youti.view.XListView.IXListViewListener;
import com.youti.yonghu.adapter.CourseListAdapterTest;
import com.youti.yonghu.adapter.FilerValAdapter;
import com.youti.yonghu.adapter.FilterDateAdapter;
import com.youti.yonghu.adapter.FilterItenAdapter;
import com.youti.yonghu.adapter.FilterPriceAdapter;
import com.youti.yonghu.adapter.FilterRegionAdapter;
import com.youti.yonghu.bean.Carousel;
import com.youti.yonghu.bean.CourseBean;
import com.youti.yonghu.bean.FilterDate;
import com.youti.yonghu.bean.FilterItem;
import com.youti.yonghu.bean.FilterLists;
import com.youti.yonghu.bean.FilterVal;
import com.youti.yonghu.bean.FilterWeek;

/**
 * 课程 列表 
 */
public class CourseListActivity extends BaseActivity implements OnClickListener, IXListViewListener{
	
	/** ViewContainer组件 */
	//private ViewContainer mViewContainer;
	XListView mListView;
	TextView footerview;
	View pview,popView,rlSearch;
	ListView mValList,mRgionList,mItemList;
	private Handler mHander;
	private LinearLayout llFooter;
	private LinearLayout llOne;
	private LinearLayout llFour;
	private LinearLayout llThree;
	private LinearLayout llTwo;
	
	private RelativeLayout rl_search;
	private ImageView imSearch;
	private EditText etSearch;
	private ImageView ivOne,ivTwo,ivThree,ivFour;
	private AbImageDownloader mAbImageLoader = null;
	private AbSlidingPlayView mAbSlidingPlayView;
	private List<Carousel> mCarousels = new ArrayList<Carousel>();
	private CourseListAdapterTest mAdapter;
	
	private List<CourseBean> mCourseLists = new ArrayList<CourseBean>();
	private FilterItenAdapter fAdapter;
	private FilerValAdapter filterValAdapter;
	private FilterRegionAdapter filterRegionAdapter;
	private FilterPriceAdapter filterPriceAdapter;
	private FilterItem filterItem;
	private List<FilterItem> filterItems = new ArrayList<FilterItem>();
	private PopupWindow popupWindow;
	private List<FilterVal> filterVals = new ArrayList<FilterVal>();
	private List<FilterWeek> filterWeek = new ArrayList<FilterWeek>();
	private FilterLists filterLists ;
	PopWindowUtil popu;
	boolean firstin = true;
	boolean islast = false;
	boolean flagLoadMore = false;
	private int buttomHeight,screenWidth,screenHeight;
	private int currentPage=1,//当期页码
	             pageCount = 1,//总页数
                 pageSize = 15;//每页数据量
	String userId;
	FrameLayout fl_videocontent;
	CustomProgressDialog dialog;
	String  filterStr,val = "",time="",title = "";

	View titleBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_list);
		userId = ((YoutiApplication)getApplication()).myPreference.getUserId();
		val = getIntent().getExtras().getString("val");
		initView();
		dialog = new CustomProgressDialog(this, R.string.laoding_tips,R.anim.frame2);
		dialog.show();
		initListenter();
		//开始定位
		//initLocation();
		getCarousel();
		getCourse(flagLoadMore,val,time,title);
		getFilterItem();
		setListener();
		getDateList();
		mAbImageLoader = new AbImageDownloader(abApplication);
		screenWidth = DistanceUtils.getScreenWidth(abApplication);
		
	}

	public final String mPageName="CourseListActivity";
	private ImageView mIconSearch;
	private boolean isSearch = true;
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
	private void getCourse(final Boolean flagLoadMore,String val,String price,String server_area) {
			
		dialog.show();
		RequestParams params = new RequestParams();
		params.put("page", currentPage);
		params.put("user_id", userId);
		params.put("val", val);
		params.put("time", time);
		params.put("jd", YoutiApplication.getInstance().myPreference.getLocation_j());
		params.put("wd", YoutiApplication.getInstance().myPreference.getLocation_w());
		params.put("title", title);
		//Utils.showToast(mContext, userId);
		HttpUtils.post(Constants.COURSE_LIST, params, new JsonHttpResponseHandler(){
			public void onStart() {
				super.onStart();
			}

			public void onFailure(int statusCode,
					org.apache.http.Header[] headers,
					java.lang.Throwable throwable,
					org.json.JSONObject errorResponse) {
				dialog.dismiss();
				View v = View.inflate(CourseListActivity.this,R.layout.layout_error, null);
				fl_videocontent.addView(v);
				Utils.showToast(CourseListActivity.this, "获取网络失败，请稍后再试");
			};

			public void onFinish() {
				dialog.dismiss();   
			};
			public void onSuccess(int statusCode,
					org.apache.http.Header[] headers,
					org.json.JSONObject response) {
				try {
					response.toString();
					String state = response.getString("code");
					
					if (state.equals("1")) {
						dialog.dismiss();
						com.alibaba.fastjson.JSONObject object = JSON.parseObject(response.toString());
						com.alibaba.fastjson.JSONArray jsonArray = object.getJSONArray("list");
						mCourseLists = JSON.parseArray(jsonArray.toString(),CourseBean.class);
						if(mAdapter==null){
							mAdapter = new CourseListAdapterTest(CourseListActivity.this, mCourseLists,mListView,false);
							mListView.setAdapter(mAdapter);
							mAdapter.notifyDataSetChanged();
						}else{
							if(flagLoadMore){
								mAdapter.addAndRefreshListView(mCourseLists);
								if(mCourseLists.size()==0){
									AbToastUtil.showToast(getApplicationContext(), "没有更多数据了...");
								}
							}else{
								mAdapter.refreshListView(mCourseLists);
							}
						}
					} else {

					}
				} catch (Exception e) {

					dialog.dismiss();
				}
			};
		});
		
	}



	/**
	 * 获取 底部 筛选条件
	 */

	private void getFilterItem() {
		 
		 dialog.show();
			/**
			 * 无网络 获取缓存数据
			 */
			if (!NetTips.isNetTips(mContext)) {
				if (cache.getAsString("HotCoachCourse") != null) {
					String hotcourse = cache.getAsString("HotCoachCourse");

					com.alibaba.fastjson.JSONObject object = JSON
							.parseObject(hotcourse);
					com.alibaba.fastjson.JSONArray jsonArray = object.getJSONArray("list1");

					return;
				}
			}

			/**
			 * 请求数据
			 */
			RequestParams params = new RequestParams();
			HttpUtils.post(Constants.HOME_FLITER_ITEM_COURSE, params,
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

									filterWeek = filterLists.getWeek();
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
		llTwo = (LinearLayout) findViewById(R.id.footer_two);
		llThree = (LinearLayout) findViewById(R.id.footer_three);
		//llFour = (LinearLayout) findViewById(R.id.footer_four);
		ivOne = (ImageView) findViewById(R.id.im_one);
		ivTwo = (ImageView) findViewById(R.id.im_two);
		ivThree = (ImageView) findViewById(R.id.im_three);
		//ivFour = (ImageView) findViewById(R.id.im_four);
		mIconSearch = (ImageView)findViewById(R.id.title_search);
		
		
		titleBar = (View) findViewById(R.id.index_titlebar);
		TextView title = (TextView) titleBar.findViewById(R.id.tv_title);
		((ImageView) findViewById(R.id.title_menu_left))
				.setVisibility(View.GONE);
		((ImageView) findViewById(R.id.title_menu_right))
				.setVisibility(View.GONE);
		View mPlayView = mInflater.inflate(R.layout.course_list_head, null);
		mListView = (XListView) findViewById(R.id.kc_list);
		mHander = new Handler();
		fl_videocontent=(FrameLayout) findViewById(R.id.fl_videocontent);
		pview = mInflater.inflate(R.layout.pop_list, null);
		popView = pview.findViewById(R.id.mLayout);
		rlSearch = mInflater.inflate(R.layout.serach_bar, null);
		imSearch = (ImageView) rlSearch.findViewById(R.id.im_search);
		etSearch = (EditText) rlSearch.findViewById(R.id.et_search);
		mItemList = (ListView) pview.findViewById(R.id.lv_pop);
		
		/*// 设置监听器
		refresh_view.getHeaderView().setHeaderProgressBarDrawable(
						this.getResources().getDrawable(R.drawable.progress_circular));
		refresh_view.getFooterView().setFooterProgressBarDrawable(
						this.getResources().getDrawable(R.drawable.progress_circular));*/
		mListView.addHeaderView(mPlayView);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this);
		mAbSlidingPlayView = (AbSlidingPlayView) mPlayView.findViewById(R.id.mAbSlidingPlayView);
		mAbSlidingPlayView.setNavHorizontalGravity(Gravity.CENTER);
		mAbSlidingPlayView.startPlay();
		mAbSlidingPlayView.setParentListView(mListView);
	}

	private void initListenter() {
		llOne.setOnClickListener(this);
		//llFour.setOnClickListener(this);
		llThree.setOnClickListener(this);
		llTwo.setOnClickListener(this);
		mIconSearch.setOnClickListener(this);
		imSearch.setOnClickListener(this);
		mAbSlidingPlayView.setOnItemClickListener(new PagerItemClickListener());
	}
	
	// 获取 广告页
	private void getCarousel() {
		if(!NetTips.isNetTips(mContext)){
			//读取缓存 信息
			if(cache.getAsString("Carousel")!=null){
				String str = cache.getAsString("Carousel");
				com.alibaba.fastjson.JSONObject object1 = JSON.parseObject(str);
				com.alibaba.fastjson.JSONArray jsonArray1 = object1.getJSONArray("list");
				mCarousels.clear();
				mCarousels = JSON.parseArray(jsonArray1.toString(), Carousel.class);
				
				for (int i = 0; i < mCarousels.size(); i++) {
					final View mView = View.inflate(CourseListActivity.this,R.layout.play_view_item, null);
					ImageView mView2 = (ImageView) mView.findViewById(R.id.mPlayImage);
					imageLoader.displayImage(Constants.PIC_CODE+mCarousels.get(i).getAdvert_img(), mView2, options);
					mAbSlidingPlayView.addView(mView);
				}
			}
			return;
		}
			
		RequestParams params = new RequestParams(); //
		params.put("city",((YoutiApplication) getApplication()).myPreference.getCity());
		HttpUtils.post(Constants.HOME_CAROUSEL, params,
				new JsonHttpResponseHandler() {
					public void onStart() {
						super.onStart();

						mAbSlidingPlayView.removeAllViews();
						if (mCarousels.size() != 0) {
							mCarousels.clear();
						}
					}

					public void onFailure(int statusCode,
							org.apache.http.Header[] headers,
							java.lang.Throwable throwable,
							org.json.JSONObject errorResponse) {
						
						//读取缓存 信息
						if(cache.getAsString("Carousel")!=null){
							String str = cache.getAsString("Carousel");
							com.alibaba.fastjson.JSONObject object1 = JSON.parseObject(str);
							com.alibaba.fastjson.JSONArray jsonArray1 = object1.getJSONArray("list");
							mCarousels.clear();
							mCarousels = JSON.parseArray(jsonArray1.toString(), Carousel.class);
							
							for (int i = 0; i < mCarousels.size(); i++) {
								final View mView = View.inflate(CourseListActivity.this,R.layout.play_view_item, null);
								ImageView mView2 = (ImageView) mView.findViewById(R.id.mPlayImage);
								imageLoader.displayImage(Constants.PIC_CODE+mCarousels.get(i).getAdvert_img(), mView2, options);
								mAbSlidingPlayView.addView(mView);
							}
						}
						return;
					};

					public void onFinish() {
					};

					public void onSuccess(int statusCode,
							org.apache.http.Header[] headers,
							org.json.JSONObject response) {
						try {
							response.toString();
							String state = response.getString("code");
							String info = response.getString("info");
							if (state.equals("1")) {
								com.alibaba.fastjson.JSONObject object = JSON
										.parseObject(response.toString());
								com.alibaba.fastjson.JSONArray jsonArray = object
										.getJSONArray("list");

								mCarousels = JSON.parseArray(
										jsonArray.toString(), Carousel.class);

								if(cache.getAsString("Carousel")==null){
									cache.put("Carousel", response.toString());
									
								}
								for (int i = 0; i < mCarousels.size(); i++) {
									final View mView = View.inflate(
											CourseListActivity.this,
											R.layout.play_view_item, null);
									ImageView mView2 = (ImageView) mView.findViewById(R.id.mPlayImage);
									imageLoader.displayImage(Constants.PIC_CODE+mCarousels.get(i).getAdvert_img(), mView2, options);
									mAbSlidingPlayView.addView(mView);
								}
								
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
			TextView tv = (TextView) view.findViewById(R.id.kc_studyCounts);
			CourseBean bean = (CourseBean) tv.getTag();
			Bundle bundle = new Bundle();
			bundle.putString(Constants.KEY_ID, bean.getCourse_id());
			bundle.putString(Constants.KEY_TITLE, bean.getDescribe());
			IntentJumpUtils.nextActivity(CourseDetailActivity.class, CourseListActivity.this, bundle);
		}
	}
	
	private class PagerItemClickListener implements AbOnItemClickListener{

		@Override
		public void onClick(int position) {
			switch (position) {
			case 0:
				startActivity(BuyPackageCourseActivity.class);
				break;

			case 1:
				startActivity(BuyPackageCourseActivity.class);
				break;
			case 2:
				startActivity(BuyPackageCourseActivity.class);
				break;
			default:
				break;
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
					getCourse(flagLoadMore,val,time,title);
				}
			});
			break;

		case R.id.footer_two:
			fda = new FilterDateAdapter(CourseListActivity.this,filterWeek);
			//mItemList.setAdapter(filterValAdapter);
			mItemList.setAdapter(fda);
			
			
			
			
			llOne.setBackgroundResource(R.color.white);
			llTwo.setBackgroundResource(R.color.chosed_ll_buttom);
			llThree.setBackgroundResource(R.color.white);
			//llFour.setBackgroundResource(R.color.white);
			ivOne.setBackgroundResource(R.drawable.jl_arrow);
			ivTwo.setBackgroundResource(R.drawable.jl_arrow_h);
			ivThree.setBackgroundResource(R.drawable.jl_arrow);
			//ivFour.setBackgroundResource(R.drawable.jl_arrow);
			popu = new PopWindowUtil(abApplication, screenWidth*1/3, screenHeight*7/12,screenWidth*1/3,buttomHeight, pview, popView);
			mItemList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					PopWindowUtil.colsePop();
					if(position==0){
						time = "全部";
					}else{
						TextView v = (TextView) view.findViewById(R.id.tv_date);
						time = (String) v.getTag();
						PopWindowUtil.colsePop();
					}
					getCourse(flagLoadMore,val,time,title);
				}
			});
			/*mItemList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					
					Integer position = (Integer) arg1.getTag();
					if(position==0){
						
					}else{
						
						Utils.showToast(CourseListActivity.this,dateList.get(position-1).getWholdDate() );
					}
				}
			});*/
			
			
			
			break;
		case R.id.footer_three:

			Utils.showToast(mContext, "小U已经把附近的教练给您筛选出来了");
			
			llOne.setBackgroundResource(R.color.white);
			llTwo.setBackgroundResource(R.color.white);
			llThree.setBackgroundResource(R.color.chosed_ll_buttom);
			//llFour.setBackgroundResource(R.color.chosed_ll_buttom);
			ivOne.setBackgroundResource(R.drawable.jl_arrow);
			ivTwo.setBackgroundResource(R.drawable.jl_arrow);
			ivThree.setBackgroundResource(R.drawable.jl_arrow_h);
			
			
			break;
		/*case R.id.footer_four:
			filterValAdapter = new FilerValAdapter(mContext, filterVals);
			mItemList.setAdapter(filterValAdapter);
			llOne.setBackgroundResource(R.color.white);
			llTwo.setBackgroundResource(R.color.chosed_ll_buttom);
			llThree.setBackgroundResource(R.color.white);
			llFour.setBackgroundResource(R.color.white);
			ivOne.setBackgroundResource(R.drawable.jl_arrow);
			ivTwo.setBackgroundResource(R.drawable.jl_arrow);
			ivThree.setBackgroundResource(R.drawable.jl_arrow);
			ivFour.setBackgroundResource(R.drawable.jl_arrow_h);
			popu = new PopWindowUtil(abApplication, screenWidth*1/4, screenHeight*7/12,screenWidth*3/4,buttomHeight, pview, popView);
			mItemList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					TextView v = (TextView) view.findViewById(R.id.tv_item_name);
					filterStr = (String) v.getTag();
					
					PopWindowUtil.colsePop();
				}
			});
			break;*/
			
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
			if(!val.equals("")){
				getCourse(false, val, "", "");
			}else {
				Utils.showToast(mContext, "请输入查询内容");
			}
			break;
		default:
			break;
		}
		
	}

	


	protected void loadMore() {
		
		currentPage++;//每次 点击加载更多，请求页码 +1
		flagLoadMore = true;
		getCourse(flagLoadMore,val,time,title);
	}
	
	ArrayList<FilterDate> dateList =new ArrayList<FilterDate>();
	String dateTime;
	String format2;
	private FilterDateAdapter fda;
	
	public ArrayList<FilterDate> getDateList(){
		FilterDate fd;
		String[] weeks = { "周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日",
				"周一", "周二", "周三", "周四", "周五", "周六" };
		Date date = new Date();
		// 返回一周中的某一天。0表示周日，6表示周六
		int week = date.getDay();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		
		for (int i = 0; i < 7; i++) {
			fd = new FilterDate();
			fd.setItemId(i + "");
			fd.setTv_week(weeks[week + i]);

			dateTime = df.format(System.currentTimeMillis());

			format2 = sdf.format(new Date(System.currentTimeMillis() + i * 24
					* 60 * 60 * 1000));

			fd.setTv_date(format2);
			dateTime = df.format(System.currentTimeMillis()+ i * 24
					* 60 * 60 * 1000);
			fd.setWholdDate(dateTime);
			dateList.add(fd);

		}
		return dateList;
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
				  getCourse(flagLoadMore,val,time,title);
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
