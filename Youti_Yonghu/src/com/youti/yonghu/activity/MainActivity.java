package com.youti.yonghu.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.activity.AbActivity;
import com.ab.bitmap.AbImageDownloader;
import com.ab.fragment.AbLoadDialogFragment;
import com.ab.util.AbToastUtil;
import com.ab.view.sliding.AbSlidingPlayView;
import com.ab.view.sliding.AbSlidingPlayView.AbOnItemClickListener;
import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.example.youti_geren.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.youti.UpdateManager;
import com.youti.appConfig.Constants;
import com.youti.appConfig.YoutiApplication;
import com.youti.base.BaseActivity;
import com.youti.utils.ACache;
import com.youti.utils.HttpUtils;
import com.youti.utils.IntentJumpUtils;
import com.youti.utils.NetTips;
import com.youti.utils.ScreenUtils;
import com.youti.utils.Utils;
import com.youti.view.CustomProgressDialog;
import com.youti.view.SlidingMenu;
import com.youti.yonghu.adapter.CoachListAdapter;
import com.youti.yonghu.adapter.CourseListAdapter;
import com.youti.yonghu.bean.Carousel;
import com.youti.yonghu.bean.CoachBean;
import com.youti.yonghu.bean.CourseBean;
import com.youti.yonghu.bean.Hotitem;

public class MainActivity extends BaseActivity implements OnClickListener {

	private final String TAG = "MainActivity";
	private AbImageDownloader mAbImageLoader = null;
	private ListView mList;
	private AbLoadDialogFragment mDialogFragment = null;
	private AbSlidingPlayView mAbSlidingPlayView;
	private UpdateManager updater;
	private ImageView img;
	private CoachListAdapter mCoachAdapter;
	private CourseListAdapter mCourseAdapter;
	private List<Carousel> mCarousels = new ArrayList<Carousel>();
	private List<Hotitem> mHotitem = new ArrayList<Hotitem>();
	private List<Hotitem> mProjects = new ArrayList<Hotitem>();
	private List<CoachBean> mPopularcoach = new ArrayList<CoachBean>();
	private List<CourseBean> mPopularcourse = new ArrayList<CourseBean>();
	public static int REQUEST = 1;
	private Handler mHandler = new Handler();
	private TextView loaction;
	private String citys;

	int hotType = 1;// 默认 为 人么教练
	private long mExitTime;
	private final  String mPageName = "MainActivity";
	private SlidingMenu menuLayout;
	/* 左侧菜单项* */
	private ImageView mMenuLeft_kc;// 课程
	private ImageView mMenuLeft_jl;// 教练
	private ImageView mMenuLeft_sp;// 视频
	private ImageView mMenuLeft_sq;// 社区
	/*
	 * private RelativeLayout mMenuLeft_llkc;//课程 private RelativeLayout
	 * mMenuLeft_lljl;//教练 private RelativeLayout mMenuLeft_llsp;//视频 private
	 * RelativeLayout mMenuLeft_llsq;//社区
	 */

	// private MyAdapter adapter;
	private ImageView mImg_ksyd;
	private ImageView mImg_jz;
	private ImageView mImg_ch;
	private ImageView mImg_tz;
	private ImageView mImg_sr;
	private TextView mTv_jz;
	private TextView mTv_ch;
	private TextView mTv_tz;
	private TextView mTv_sr;
	private ImageView mImg_qx;
	private ImageView mImg_tennis;
	private ImageView mImg_golf;
	private ImageView mImg_gd;
	private TextView mTv_qx;
	private TextView mTv_tennis;
	private TextView mTv_golf;
	private TextView mTv_gd;
	private RelativeLayout mHOME_jz;
	private RelativeLayout mHOME_ch;
	private RelativeLayout mHOME_tz;
	private RelativeLayout mHOME_rs;
	private LinearLayout mHOME_qx;
	private LinearLayout mHOME_tennis;
	private LinearLayout mHOME_golf;
	private LinearLayout mHOME_gd;
	private RelativeLayout mHOME_yd;
	private ListView mLv;
	private TextView mTv_hotjl, mTv_hotkc, mTvmore;
	Resources resource;
	ColorStateList select, def;
	private ImageView ivMenu_left, ivMenu_right, mImg_titleBack,
			mImg_titleSearch;
	private int w;
	View viewDilog;
	private Context mContext;

	ACache cache;
	// AbFileCache fileCache;
	// AbImageCache imageCahe;
	ImageLoader imageLoader;
	private DisplayImageOptions options;

	
	private TextView currentCity;
	CustomProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		// mMenu = (SlidingMenu) findViewById(R.id.id_menu);
		mContext = this;
		dialog = new CustomProgressDialog(this, R.string.laoding_tips,R.anim.frame2);
		dialog.show();
		
		viewDilog = mInflater.inflate(R.layout.dilog_itme, null);
 
		mAbImageLoader = new AbImageDownloader(abApplication);
		Display mDisplay = getWindowManager().getDefaultDisplay();
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.user_lbk)
				// 正在加载
				.showImageForEmptyUri(R.drawable.user_lbk)
				// 空图片
				.showImageOnFail(R.drawable.user_lbk)
				// 错误图片
				.cacheInMemory(true)
				// 设置 内存缓存
				.cacheOnDisk(true)
				// 设置硬盘缓存
				.considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		cache = ACache.get(abApplication);
	
		w = mDisplay.getWidth();
		resource = (Resources) getBaseContext().getResources();
		select = (ColorStateList) resource.getColorStateList(R.color.text_color_select);
		def = (ColorStateList) resource.getColorStateList(R.color.text_color_default);
		iniView();
		getCarousel();
		getProjects();
		// getHotitem();
		// getHotCoach();
		getHotCoachAndSource();

		iniData();
		initListener();
		// updater = new UpdateManager(this);
		// updater.autoUpdate();
		 InitLocation();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext 百度地图初始化
		SDKInitializer.initialize(this.getApplicationContext());
	}

	/**
	 * 初始化组件
	 */

	private void iniView() {
		/*
		 * title
		 */

		menuLayout = (SlidingMenu) findViewById(R.id.id_menu);
		ivMenu_left = (ImageView) findViewById(R.id.title_menu_left);
		ivMenu_right = (ImageView) findViewById(R.id.title_menu_right);
		mImg_titleBack = (ImageView) findViewById(R.id.title_back);
		mImg_titleSearch = (ImageView) findViewById(R.id.title_search);
		mImg_titleBack.setVisibility(View.GONE);
		mImg_titleSearch.setVisibility(View.GONE);

		ivMenu_left.setVisibility(View.VISIBLE);
		ivMenu_right.setVisibility(View.VISIBLE);

		/* 左侧菜单* */
		mMenuLeft_kc = (ImageView) findViewById(R.id.iv_menu_kc);// 课程
		mMenuLeft_jl = (ImageView) findViewById(R.id.iv_menu_jl);// 教练
		mMenuLeft_sp = (ImageView) findViewById(R.id.iv_menu_sp);// 视频
		mMenuLeft_sq = (ImageView) findViewById(R.id.iv_menu_sq);// 社区

		/*
		 * mMenuLeft_llkc = (LinearLayout) findViewById(R.id.ll_menu_kc);//课程
		 * mMenuLeft_lljl = (LinearLayout) findViewById(R.id.ll_menu_jl);//教练
		 * mMenuLeft_llsp = (LinearLayout) findViewById(R.id.ll_menu_sp);//视频
		 * mMenuLeft_llsq = (LinearLayout) findViewById(R.id.ll_menu_sq);//社区
		 */
		/* 右侧菜单 */

		mList = (ListView) findViewById(R.id.home_hot_list);

		// View mPlayView = mInflater.inflate(R.layout.home_tou, null);
		View mPlayViews = mInflater.inflate(R.layout.home_list_more, null);
		mTvmore = (TextView) mPlayViews.findViewById(R.id.more_tip);
		mImg_ksyd = (ImageView) findViewById(R.id.img_ksyd);
		mImg_jz = (ImageView) findViewById(R.id.img_jz);
		mImg_ch = (ImageView) findViewById(R.id.img_ch);
		mImg_tz = (ImageView) findViewById(R.id.img_tz);
		mImg_sr = (ImageView) findViewById(R.id.img_sr);

		mTv_jz = (TextView) findViewById(R.id.tv_jz);
		mTv_ch = (TextView) findViewById(R.id.tv_ch);
		mTv_tz = (TextView) findViewById(R.id.tv_tz);
		mTv_sr = (TextView) findViewById(R.id.tv_sr);

		mImg_qx = (ImageView) findViewById(R.id.img_qx);
		mImg_tennis = (ImageView) findViewById(R.id.img_tennis);
		mImg_golf = (ImageView) findViewById(R.id.img_golf);
		mImg_gd = (ImageView) findViewById(R.id.img_gd);

		mTv_qx = (TextView) findViewById(R.id.tv_qx);
		mTv_tennis = (TextView) findViewById(R.id.tv_tennis);
		mTv_golf = (TextView) findViewById(R.id.tv_golf);
		mTv_gd = (TextView) findViewById(R.id.tv_gd);

		mHOME_yd = (RelativeLayout) findViewById(R.id.rl_ksyd);
		mHOME_jz = (RelativeLayout) findViewById(R.id.new_jz);
		mHOME_ch = (RelativeLayout) findViewById(R.id.new_ch);
		mHOME_tz = (RelativeLayout) findViewById(R.id.new_tz);
		mHOME_rs = (RelativeLayout) findViewById(R.id.new_sr);

		mHOME_qx = (LinearLayout) findViewById(R.id.new_qx);
		mHOME_tennis = (LinearLayout) findViewById(R.id.new_tennis);
		mHOME_golf = (LinearLayout) findViewById(R.id.new_golf);
		mHOME_gd = (LinearLayout) findViewById(R.id.new_gd);

		mTv_hotjl = (TextView) findViewById(R.id.home_hot_jl);
		mTv_hotkc = (TextView) findViewById(R.id.home_hot_kc);
		
		loaction=(TextView) findViewById(R.id.current_city);

		mList.addFooterView(mPlayViews);
		mAbSlidingPlayView = (AbSlidingPlayView) findViewById(R.id.mAbSlidingPlayView);
		mAbSlidingPlayView.setNavHorizontalGravity(Gravity.CENTER);
		// mAbSlidingPlayView.setParentHScrollView(menuLayout);
		mAbSlidingPlayView.startPlay();
		mAbSlidingPlayView.setParentListView(mList);
		// adapter = new MyAdapter();
		mAbSlidingPlayView.setOnItemClickListener(new AbOnItemClickListener() {

			@Override
			public void onClick(int position) {
				Toast.makeText(mContext, position + "", 1000).show();
			}
		});
		
		loaction.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent =new Intent(MainActivity.this,SelectCityActivity.class);
				startActivity(intent);
			}
		});

	}

	// 获取 广告页
	private void getCarousel() {

		dialog.show();
		if (!NetTips.isNetTips(mContext)) {
			// 读取缓存 信息
			if (cache.getAsString("Carousel") != null) {
				String str = cache.getAsString("Carousel");
				com.alibaba.fastjson.JSONObject object1 = JSON.parseObject(str);
				com.alibaba.fastjson.JSONArray jsonArray1 = object1
						.getJSONArray("list");
				mCarousels.clear();
				mCarousels = JSON.parseArray(jsonArray1.toString(),
						Carousel.class);

				for (int i = 0; i < mCarousels.size(); i++) {
					final View mView = View.inflate(MainActivity.this,
							R.layout.play_view_item, null);
					ImageView mView2 = (ImageView) mView
							.findViewById(R.id.mPlayImage);
					imageLoader.displayImage(Constants.PIC_CODE
							+ mCarousels.get(i).getAdvert_img(), mView2,
							options);
					mAbSlidingPlayView.addView(mView);
				}
			}
			return;
		}

		RequestParams params = new RequestParams(); //
		params.put("city",
				((YoutiApplication) getApplication()).myPreference.getCity());
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
						dialog.dismiss();

						// 读取缓存 信息
						if (cache.getAsString("Carousel") != null) {
							String str = cache.getAsString("Carousel");
							com.alibaba.fastjson.JSONObject object1 = JSON
									.parseObject(str);
							com.alibaba.fastjson.JSONArray jsonArray1 = object1
									.getJSONArray("list");
							mCarousels.clear();
							mCarousels = JSON.parseArray(jsonArray1.toString(),
									Carousel.class);

							for (int i = 0; i < mCarousels.size(); i++) {
								final View mView = View.inflate(
										MainActivity.this,
										R.layout.play_view_item, null);
								ImageView mView2 = (ImageView) mView
										.findViewById(R.id.mPlayImage);
								imageLoader.displayImage(Constants.PIC_CODE
										+ mCarousels.get(i).getAdvert_img(),
										mView2, options);
								mAbSlidingPlayView.addView(mView);
							}
						}
						
						return;
					};

					public void onFinish() {
						dialog.dismiss();
					};

					public void onSuccess(int statusCode,
							org.apache.http.Header[] headers,
							org.json.JSONObject response) {
						try {
							dialog.dismiss();
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

								if (cache.getAsString("Carousel") == null) {
									cache.put("Carousel", response.toString());

								}
								for (int i = 0; i < mCarousels.size(); i++) {
									final View mView = View.inflate(
											MainActivity.this,
											R.layout.play_view_item, null);
									ImageView mView2 = (ImageView) mView
											.findViewById(R.id.mPlayImage);
									imageLoader.displayImage(
											Constants.PIC_CODE
													+ mCarousels.get(i)
															.getAdvert_img(),
											mView2, options);
									mAbSlidingPlayView.addView(mView);
								}

								

							} else {

								AbToastUtil.showToast(mContext,
										R.string.data_tips);
							}
						} catch (Exception e) {

						}
					};
				});

	}

	private void getProjects() {

		/**
		 * 获取 四小项 缓存信息
		 */
		if (!NetTips.isNetTips(mContext)) {
			if (cache.getAsString("four") != null) {
				String strFour = cache.getAsString("four");
				com.alibaba.fastjson.JSONObject object = JSON
						.parseObject(strFour);
				com.alibaba.fastjson.JSONArray jsonArray = object
						.getJSONArray("list1");
				mProjects = JSON
						.parseArray(jsonArray.toString(), Hotitem.class);

				imageLoader.displayImage(mProjects.get(0).getPic(), mImg_jz,
						options);
				imageLoader.displayImage(mProjects.get(1).getPic(), mImg_ch,
						options);
				imageLoader.displayImage(mProjects.get(2).getPic(), mImg_tz,
						options);
				imageLoader.displayImage(mProjects.get(3).getPic(), mImg_sr,
						options);

				mTv_jz.setText(mProjects.get(0).getVal());
				mTv_ch.setText(mProjects.get(1).getVal());
				mTv_tz.setText(mProjects.get(2).getVal());
				mTv_sr.setText(mProjects.get(3).getVal());

			} else {
				AbToastUtil.showToast(mContext, R.string.net_tips);
				
			}

			/**
			 * 获取 热门项目 缓存信息
			 */
			if (cache.getAsString("mHotitem") != null) {
				String strHot = cache.getAsString("mHotitem");
				com.alibaba.fastjson.JSONObject object1 = JSON
						.parseObject(strHot);
				com.alibaba.fastjson.JSONArray jsonArray1 = object1
						.getJSONArray("list2");
				mHotitem = JSON.parseArray(jsonArray1.toString(), Hotitem.class);

				imageLoader.displayImage(Constants.PIC_CODE
						+ mHotitem.get(0).getPic(), mImg_qx, options);
				imageLoader.displayImage(Constants.PIC_CODE
						+ mHotitem.get(1).getPic(), mImg_tennis, options);
				imageLoader.displayImage(Constants.PIC_CODE
						+ mHotitem.get(2).getPic(), mImg_golf, options);
				// imageLoader.displayImage(mHotitem.get(3).getPic(), mImg_gd,
				// options);
				mTv_qx.setText(mHotitem.get(0).getVal());
				mTv_tennis.setText(mHotitem.get(1).getVal());
				mTv_golf.setText(mHotitem.get(2).getVal());
			}

			return;
		}

		RequestParams params = new RequestParams(); //
		HttpUtils.post(Constants.HOME_PROJECTS_RECOMMENDED, params,
				new JsonHttpResponseHandler() {
					public void onStart() {
						super.onStart();
								
						mAbSlidingPlayView.removeAllViews();
						if (mProjects.size() != 0) {
							mProjects.clear();
						}
					}

					public void onFailure(int statusCode,
							org.apache.http.Header[] headers,
							java.lang.Throwable throwable,
							org.json.JSONObject errorResponse) {

						if (cache.getAsString("four") != null) {
							String strFour = cache.getAsString("four");
							com.alibaba.fastjson.JSONObject object = JSON
									.parseObject(strFour);
							com.alibaba.fastjson.JSONArray jsonArray = object
									.getJSONArray("list1");
							mProjects = JSON.parseArray(jsonArray.toString(),
									Hotitem.class);

							imageLoader.displayImage(Constants.PIC_CODE
									+ mProjects.get(0).getPic(), mImg_jz,
									options);
							imageLoader.displayImage(Constants.PIC_CODE
									+ mProjects.get(1).getPic(), mImg_ch,
									options);
							imageLoader.displayImage(Constants.PIC_CODE
									+ mProjects.get(2).getPic(), mImg_tz,
									options);
							imageLoader.displayImage(Constants.PIC_CODE
									+ mProjects.get(3).getPic(), mImg_sr,
									options);

							mTv_jz.setText(mProjects.get(0).getVal());
							mTv_ch.setText(mProjects.get(1).getVal());
							mTv_tz.setText(mProjects.get(2).getVal());
							mTv_sr.setText(mProjects.get(3).getVal());

						} else {
							AbToastUtil.showToast(mContext, R.string.net_tips);
							
						}

						if (cache.getAsString("mHotitem") != null) {
							String strHot = cache.getAsString("mHotitem");
							com.alibaba.fastjson.JSONObject object1 = JSON
									.parseObject(strHot);
							com.alibaba.fastjson.JSONArray jsonArray1 = object1
									.getJSONArray("list2");
							mHotitem = JSON.parseArray(jsonArray1.toString(),
									Hotitem.class);

							imageLoader.displayImage(Constants.PIC_CODE
									+ mHotitem.get(0).getPic(), mImg_qx,
									options);
							imageLoader.displayImage(Constants.PIC_CODE
									+ mHotitem.get(1).getPic(), mImg_tennis,
									options);
							imageLoader.displayImage(Constants.PIC_CODE
									+ mHotitem.get(2).getPic(), mImg_golf,
									options);
							// imageLoader.displayImage(mHotitem.get(3).getPic(),
							// mImg_gd, options);
							mTv_qx.setText(mHotitem.get(0).getVal());
							mTv_tennis.setText(mHotitem.get(1).getVal());
							mTv_golf.setText(mHotitem.get(2).getVal());
						}

						
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
								// 四小项目
								com.alibaba.fastjson.JSONObject object = JSON
										.parseObject(response.toString());
								com.alibaba.fastjson.JSONArray jsonArray = object
										.getJSONArray("list1");

								if (cache.getAsString("four") == null) {
									cache.put("four", response.toString());

									String strHot = cache
											.getAsString("mHotitem");
								}
								mProjects = JSON.parseArray(
										jsonArray.toString(), Hotitem.class);

								imageLoader.displayImage(Constants.PIC_CODE
										+ mProjects.get(0).getPic(), mImg_jz,
										options);
								imageLoader.displayImage(Constants.PIC_CODE
										+ mProjects.get(1).getPic(), mImg_ch,
										options);
								imageLoader.displayImage(Constants.PIC_CODE
										+ mProjects.get(2).getPic(), mImg_tz,
										options);
								imageLoader.displayImage(Constants.PIC_CODE
										+ mProjects.get(3).getPic(), mImg_sr,
										options);

								mTv_jz.setText(mProjects.get(0).getVal());
								mTv_ch.setText(mProjects.get(1).getVal());
								mTv_tz.setText(mProjects.get(2).getVal());
								mTv_sr.setText(mProjects.get(3).getVal());

								// 热门项目

								com.alibaba.fastjson.JSONObject object2 = JSON
										.parseObject(response.toString());
								com.alibaba.fastjson.JSONArray jsonArray2 = object
										.getJSONArray("list2");

								if (cache.getAsString("mHotitem") == null) {
									cache.put("mHotitem", response.toString());
								}
								mHotitem = JSON.parseArray(
										jsonArray2.toString(), Hotitem.class);

								imageLoader.displayImage(Constants.PIC_CODE
										+ mHotitem.get(0).getPic(), mImg_qx,
										options);
								imageLoader.displayImage(Constants.PIC_CODE
										+ mHotitem.get(1).getPic(),
										mImg_tennis, options);
								imageLoader.displayImage(Constants.PIC_CODE
										+ mHotitem.get(2).getPic(), mImg_golf,
										options);
								// imageLoader.displayImage(mHotitem.get(3).getPic(),
								// mImg_gd, options);

								mTv_qx.setText(mHotitem.get(0).getVal());
								mTv_tennis.setText(mHotitem.get(1).getVal());
								mTv_golf.setText(mHotitem.get(2).getVal());
								// mTv_gd.setText(mHotitem.get(3).getVal());
								
							} else {
								Toast.makeText(abApplication, info, 1000)
										.show();
							}
						} catch (Exception e) {

						}
					};
				});

	}

	/**
	 * 获取 热门课程
	 */
	private void getHotCoachAndSource() {

		dialog.show();
		/**
		 * 无网络 获取缓存数据
		 */
		if (!NetTips.isNetTips(mContext)) {
			if (cache.getAsString("HotCoachCourse") != null) {
				String hotcourse = cache.getAsString("HotCoachCourse");

				com.alibaba.fastjson.JSONObject object = JSON
						.parseObject(hotcourse);
				com.alibaba.fastjson.JSONArray jsonArray = object
						.getJSONArray("list1");
				mPopularcoach = JSON.parseArray(jsonArray.toString(),
						CoachBean.class);

				mCoachAdapter = new CoachListAdapter(getApplicationContext(),
						mPopularcoach);
				mList.setAdapter(mCoachAdapter);
				ScreenUtils.setListViewHeightBasedOnChildren(mList);
				mList.setSelection(1);
				mCoachAdapter.notifyDataSetChanged();

				// 热门课程
				com.alibaba.fastjson.JSONObject object2 = JSON
						.parseObject(hotcourse);
				com.alibaba.fastjson.JSONArray jsonArray2 = object
						.getJSONArray("list2");
				mPopularcourse = JSON.parseArray(jsonArray.toString(),
						CourseBean.class);
				return;
			}
		}

		/**
		 * 请求数据
		 */
		RequestParams params = new RequestParams();
		//params.put("host", "1");
		/*
		if (w < 720) {
			params.put("resolution", "480");
			;
		} else {
			params.put("resolution", w + "");
		}*/
		HttpUtils.post(Constants.HOME_POPULAR_COURSE, params,
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
							String hotcourse = cache
									.getAsString("HotCoachCourse");

							com.alibaba.fastjson.JSONObject object = JSON
									.parseObject(hotcourse);
							com.alibaba.fastjson.JSONArray jsonArray = object
									.getJSONArray("list1");
							mPopularcoach = JSON.parseArray(
									jsonArray.toString(), CoachBean.class);

							mCoachAdapter = new CoachListAdapter(
									getApplicationContext(), mPopularcoach);
							mList.setAdapter(mCoachAdapter);
							ScreenUtils.setListViewHeightBasedOnChildren(mList);
							mList.setSelection(1);
							mCoachAdapter.notifyDataSetChanged();

							// 热门课程
							com.alibaba.fastjson.JSONObject object2 = JSON.parseObject(hotcourse);
							com.alibaba.fastjson.JSONArray jsonArray2 = object2.getJSONArray("list2");
							mPopularcourse = JSON.parseArray(jsonArray2.toString(), CourseBean.class);
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
								com.alibaba.fastjson.JSONObject object = JSON
										.parseObject(response.toString());
								com.alibaba.fastjson.JSONArray jsonArray = object
										.getJSONArray("list1");
								mPopularcoach = JSON.parseArray(
										jsonArray.toString(), CoachBean.class);
								if (cache.getAsString("HotCoachCourse") == null) {
									cache.put("HotCoachCourse",
											response.toString());
								}

								mCoachAdapter = new CoachListAdapter(
										getApplicationContext(), mPopularcoach);
								mList.setAdapter(mCoachAdapter);
								ScreenUtils
										.setListViewHeightBasedOnChildren(mList);
								mList.setSelection(1);
								mCoachAdapter.notifyDataSetChanged();

								// 热门课程
								com.alibaba.fastjson.JSONObject object2 = JSON.parseObject(response.toString());
								com.alibaba.fastjson.JSONArray jsonArray2 = object2.getJSONArray("list2");
								mPopularcourse = JSON.parseArray(jsonArray2.toString(), CourseBean.class);

							} else {
							}
						} catch (Exception e) {

						}
					};
				});
	}

	public void toggleMenu(View view) {
		menuLayout.toggleLeft();
	}

	private void iniData() {
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 != 2) {
					/*
					 * b.putLong("cid",Long.parseLong(mPopularcoach.get(arg2-1).
					 * getId())); b.putString("cname",
					 * mPopularcoach.get(arg2-1).getRname()); b.putString("rz",
					 * mPopularcoach.get(arg2-1).getIsrz());
					 * nextActivity(CoachHomePage.class, b);
					 */
				} else {
					// nextActivity(NewMoreCoaches.class, null);
				}
			}
		});
		mAbSlidingPlayView.setOnItemClickListener(new AbOnItemClickListener() {

			@Override
			public void onClick(int position) {
				/*
				 * b.putString("name", mCarousels.get(position).getTitle());
				 * b.putString("url", mCarousels.get(position).getUrl());
				 * b.putString("content",
				 * mCarousels.get(position).getContent()); b.putString("img",
				 * mCarousels.get(position).getPic());
				 * nextActivity(Webview.class, b);
				 */

			}
		});
	}

	/**
	 * 注册点击事件
	 */
	private void initListener() {

		ivMenu_left.setOnClickListener(this);
		ivMenu_right.setOnClickListener(this);

		mTv_hotjl.setOnClickListener(this);
		mTv_hotkc.setOnClickListener(this);

		mImg_jz.setOnClickListener(this);
		mImg_ch.setOnClickListener(this);
		mImg_tz.setOnClickListener(this);
		mImg_sr.setOnClickListener(this);

		mHOME_qx.setOnClickListener(this);
		mHOME_tennis.setOnClickListener(this);
		mHOME_golf.setOnClickListener(this);
		mHOME_yd.setOnClickListener(this);
		mHOME_gd.setOnClickListener(this);
		mTvmore.setOnClickListener(this);

		mList.setOnItemClickListener(new MyItemClickListener());
	}

	private MyLocationListener mMyLocationListener;
	private LocationClient mLocationClient;

	private void InitLocation() {
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);//
		option.setCoorType("bd09ll");// 默认 gcj02
		int span = 5000;
		option.setScanSpan(span);// 请求 间隔5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
		mLocationClient.start();

	}

	/**
	 * 定位事件处理
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			String loc = "";

			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				loc = location.getCity();
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				loc = location.getCity();
			}
			if (loc != null && loc.length() > 0) {
				((YoutiApplication) getApplication()).myPreference.setCity(loc);
				((YoutiApplication) getApplication()).myPreference
						.setLocation_w("" + location.getLatitude());
				((YoutiApplication) getApplication()).myPreference
						.setLocation_j("" + location.getLongitude());
			}
			final String city = loc;
			final double latitude = location.getLatitude();
			final double ontitude = location.getLongitude();
			Utils.showToast(mContext, "Latitude:"+latitude+"Longitude:"+ontitude);
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					if (city == null || city.equals("")) {
						loaction.setText("定位失败");
						citys = "定位失败";
					} else {
						loaction.setText(city);
						citys = city;
						// GetCarousel();
					}
				}
			});

			if (location.getLocType() != 0) {
				mLocationClient.stop();
			}
		}
	}

	class MyItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position == mPopularcoach.size()) {
				IntentJumpUtils.nextActivity(CoachListActivity.class,
						MainActivity.this);
				return;
			}

			if (hotType == 1) {

				String cid = "";
				TextView tv = (TextView) view.findViewById(R.id.coach_tv_price);
				cid = (String) tv.getTag();
				Bundle bundle = new Bundle();
				bundle.putString(Constants.KEY_ID, cid);
				IntentJumpUtils.nextActivity(CoachDetailActivity.class,
						MainActivity.this, bundle);
			} else if (hotType == 2) {
				String cid = "";
				TextView tv = (TextView) view.findViewById(R.id.kc_studyCounts);
				cid = (String) tv.getTag();
				Bundle bundle = new Bundle();
				bundle.putString(Constants.KEY_ID, cid);
				IntentJumpUtils.nextActivity(CourseDetailActivity.class,
						MainActivity.this, bundle);
			}

		}

	}

	/**
	 * 点击事件处理
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.title_menu_left:
			menuLayout.toggleLeft();

			break;

		case R.id.title_menu_right:
			menuLayout.toggleRight();
			break;

		case R.id.home_hot_jl:

			hotType = 1;
			mTv_hotjl.setTextColor(select);
			mTv_hotkc.setTextColor(def);
			mCoachAdapter = new CoachListAdapter(getApplicationContext(),
					mPopularcoach);
			mList.setAdapter(mCoachAdapter);
			ScreenUtils.setListViewHeightBasedOnChildren(mList);
			// mList.setSelection(1);
			mCoachAdapter.notifyDataSetChanged();
			break;
		case R.id.home_hot_kc:
			hotType = 2;
			mTv_hotkc.setTextColor(select);
			mTv_hotjl.setTextColor(def);

			mCourseAdapter = new CourseListAdapter(getApplicationContext(),
					mPopularcourse);
			mList.setAdapter(mCourseAdapter);
			ScreenUtils.setListViewHeightBasedOnChildren(mList);
			mCourseAdapter.notifyDataSetChanged();
			break;

		case R.id.rl_ksyd:
			if (NetTips.isNetTips(mContext)) {
				IntentJumpUtils.nextActivity(FastBookActivity.class, this, null);
			}
			break;
		case R.id.img_jz:
			if (NetTips.isNetTips(mContext)) {

				Bundle b = new Bundle();
				b.putString(Constants.KEY_ID, mProjects.get(0).getId());
				IntentJumpUtils.nextActivity(ActivePageActivity.class, this, b);
			}
			break;
		case R.id.img_ch:
			if (NetTips.isNetTips(mContext)) {

				if (NetTips.isNetTips(mContext)) {

					Bundle b = new Bundle();
					b.putString(Constants.KEY_ID, mProjects.get(1).getId());
					IntentJumpUtils.nextActivity(ActivePageActivity.class, this, b);
				}
			}
			break;
		case R.id.img_tz:
			if (NetTips.isNetTips(mContext)) {

				if (NetTips.isNetTips(mContext)) {

					Bundle b = new Bundle();
					b.putString(Constants.KEY_ID, mProjects.get(2).getId());
					IntentJumpUtils.nextActivity(ActivePageActivity.class, this, b);
				}
			}
			break;
		case R.id.img_sr:
			if (NetTips.isNetTips(mContext)) {
				if (NetTips.isNetTips(mContext)) {

					Bundle b = new Bundle();
					b.putString("url", mProjects.get(3).getDe_url());
					IntentJumpUtils.nextActivity(ActiveDetailActivity.class, this, b);
				}
			}
			break;
		case R.id.img_qx:
			if (NetTips.isNetTips(mContext)) {

			}
			break;
		case R.id.img_tennis:
			if (NetTips.isNetTips(mContext)) {

			}
			break;
		case R.id.img_golf:
			if (NetTips.isNetTips(mContext)) {

			}
			break;
		case R.id.new_gd:
			if (NetTips.isNetTips(mContext)) {

				IntentJumpUtils.nextActivity(MoreOptionActivity.class, this,
						null);
			}
			break;

		case R.id.more_tip:
			if (NetTips.isNetTips(mContext)) {

				IntentJumpUtils.nextActivity(CoachListActivity.class, this,
						null);
			}

			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		menuLayout.closeLeftMenu();
		super.onResume();
		MobclickAgent.onPageStart( mPageName );
		MobclickAgent.onResume(mContext);
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd( mPageName );
		MobclickAgent.onPause(mContext);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, R.string.app_press_again_to_exit,
						Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				// mMyApplication.exit();
				finish();
			}
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void nextActivity(Class<?> next, Bundle b) {
		Intent intent = new Intent();
		intent.setClass(this, next);
		if (b != null) {
			intent.putExtras(b);
		}
		startActivityForResult(intent, REQUEST);
	}

	/**
	 * 获取未读消息数
	 * 
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		int chatroomUnreadMsgCount = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		for (EMConversation conversation : EMChatManager.getInstance()
				.getAllConversations().values()) {
			if (conversation.getType() == EMConversationType.ChatRoom)
				chatroomUnreadMsgCount = chatroomUnreadMsgCount
						+ conversation.getUnreadMsgCount();
		}
		return unreadMsgCountTotal - chatroomUnreadMsgCount;
	}

}
