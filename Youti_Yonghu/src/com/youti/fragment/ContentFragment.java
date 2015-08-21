package com.youti.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.example.youti_geren.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.Constants;
import com.youti.appConfig.YoutiApplication;
import com.youti.utils.ACache;
import com.youti.utils.HttpUtils;
import com.youti.utils.IntentJumpUtils;
import com.youti.utils.NetTips;
import com.youti.utils.ScreenUtils;
import com.youti.utils.Utils;
import com.youti.view.CustomProgressDialog;
import com.youti.view.SlidingMenu;
import com.youti.view.XListView;
import com.youti.view.XListView.IXListViewListener;
import com.youti.yonghu.activity.ActiveDetailActivity;
import com.youti.yonghu.activity.ActivePageActivity;
import com.youti.yonghu.activity.ActivePageGjcActivity;
import com.youti.yonghu.activity.BuyPackageCourseActivity;
import com.youti.yonghu.activity.CoachDetailActivity;
import com.youti.yonghu.activity.CoachListActivity;
import com.youti.yonghu.activity.CourseDetailActivity;
import com.youti.yonghu.activity.CourseListActivity;
import com.youti.yonghu.activity.FastBookActivity;
import com.youti.yonghu.activity.MainMainActivity;
import com.youti.yonghu.activity.MoreOptionActivity;
import com.youti.yonghu.activity.SelectCityActivity;
import com.youti.yonghu.adapter.CoachListAdapterTest;
import com.youti.yonghu.adapter.CourseListAdapterTest;
import com.youti.yonghu.bean.Carousel;
import com.youti.yonghu.bean.CoachBean;
import com.youti.yonghu.bean.CourseBean;
import com.youti.yonghu.bean.Hotitem;

public class ContentFragment extends Fragment implements OnClickListener, IXListViewListener{
	
	private final String TAG = "MainActivity";
	private XListView mList;
	private AbSlidingPlayView mAbSlidingPlayView;
	private ImageView img;
	private CoachListAdapterTest mCoachAdapter;
	private CourseListAdapterTest mCourseAdapter;
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
	

	private SlidingMenu menuLayout;

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
			mImg_titleSearch,imSearch;
	private EditText et_search;
	private int w;
	View viewDilog;
	private Context mContext;

	ACache cache;
	// AbFileCache fileCache;
	// AbImageCache imageCahe;
	ImageLoader imageLoader;
	private DisplayImageOptions options;

	private String val;
	private TextView currentCity;
	CustomProgressDialog dialog;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			v = View.inflate(getActivity(), R.layout.main_home, null);
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mContext = getActivity();
		viewDilog = View.inflate(mContext,R.layout.dilog_itme, null);
		dialog = new CustomProgressDialog(getActivity(), R.string.laoding_tips,R.anim.frame2);
		dialog.show();
		Display mDisplay =((MainMainActivity)getActivity()).getWindowManager().getDefaultDisplay();
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
		cache = ACache.get(getActivity().getApplicationContext());
		w = mDisplay.getWidth();
		resource = (Resources) getActivity().getApplicationContext().getResources();
		select = (ColorStateList) resource
				.getColorStateList(R.color.text_color_select);
		def = (ColorStateList) resource
				.getColorStateList(R.color.text_color_default);
		iniView();
		getCarousel();
		getProjects();
		getHotCoachAndSource();
		iniData();
		initListener();
		InitLocation();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext 百度地图初始化
		SDKInitializer.initialize(getActivity().getApplicationContext());
	}
	/**
	 * 初始化组件
	 */

	private void iniView() {
		/*
		 * title
		 */

		ivMenu_left = (ImageView) v.findViewById(R.id.title_menu_left);
		ivMenu_right = (ImageView) v.findViewById(R.id.title_menu_right);
		mImg_titleBack = (ImageView) v.findViewById(R.id.title_back);
		mImg_titleSearch = (ImageView) v.findViewById(R.id.title_search);
		mImg_titleBack.setVisibility(View.GONE);
		mImg_titleSearch.setVisibility(View.GONE);

		ivMenu_left.setVisibility(View.VISIBLE);
		ivMenu_right.setVisibility(View.VISIBLE);
		
		/*headImageUrl = YoutiApplication.getInstance().myPreference.getHeadImgPath();
		if(headImageUrl.startsWith("http")){
			
			ImageLoader.getInstance().displayImage(headImageUrl, ivMenu_right);
		}else{
			ImageLoader.getInstance().displayImage("http://112.126.72.250/ut_app"+headImageUrl, ivMenu_right);

		}*/
		
		/* 右侧菜单 */

		mList = (XListView)v.findViewById(R.id.home_hot_list);
		// View mPlayView = mInflater.inflate(R.layout.home_tou, null);
		//View mPlayViews = View.inflate(mContext,R.layout.home_list_more, null);
		//mTvmore = (TextView) mPlayViews.findViewById(R.id.more_tip);
		imSearch = (ImageView) v.findViewById(R.id.im_search);
		et_search = (EditText) v.findViewById(R.id.et_search);
		mImg_ksyd = (ImageView)v.findViewById(R.id.img_ksyd);
		mImg_jz = (ImageView)v.findViewById(R.id.img_jz);
		mImg_ch = (ImageView) v.findViewById(R.id.img_ch);
		mImg_tz = (ImageView) v.findViewById(R.id.img_tz);
		mImg_sr = (ImageView) v.findViewById(R.id.img_sr);

		mTv_jz = (TextView) v.findViewById(R.id.tv_jz);
		mTv_ch = (TextView) v.findViewById(R.id.tv_ch);
		mTv_tz = (TextView) v.findViewById(R.id.tv_tz);
		mTv_sr = (TextView) v.findViewById(R.id.tv_sr);

		mImg_qx = (ImageView) v.findViewById(R.id.img_qx);
		mImg_tennis = (ImageView) v.findViewById(R.id.img_tennis);
		mImg_golf = (ImageView) v.findViewById(R.id.img_golf);
		mImg_gd = (ImageView) v.findViewById(R.id.img_gd);

		mTv_qx = (TextView) v.findViewById(R.id.tv_qx);
		mTv_tennis = (TextView) v.findViewById(R.id.tv_tennis);
		mTv_golf = (TextView) v.findViewById(R.id.tv_golf);
		mTv_gd = (TextView) v.findViewById(R.id.tv_gd);

		mHOME_yd = (RelativeLayout) v.findViewById(R.id.rl_ksyd);
		mHOME_jz = (RelativeLayout)v. findViewById(R.id.new_jz);
		mHOME_ch = (RelativeLayout) v.findViewById(R.id.new_ch);
		mHOME_tz = (RelativeLayout)v. findViewById(R.id.new_tz);
		mHOME_rs = (RelativeLayout) v.findViewById(R.id.new_sr);

		mHOME_qx = (LinearLayout) v.findViewById(R.id.new_qx);
		mHOME_tennis = (LinearLayout) v.findViewById(R.id.new_tennis);
		mHOME_golf = (LinearLayout) v.findViewById(R.id.new_golf);
		mHOME_gd = (LinearLayout) v.findViewById(R.id.new_gd);

		mTv_hotjl = (TextView) v.findViewById(R.id.home_hot_jl);
		mTv_hotkc = (TextView) v.findViewById(R.id.home_hot_kc);
		loaction=(TextView) v.findViewById(R.id.current_city);

		//mList.addFooterView(mPlayViews);
		mAbSlidingPlayView = (AbSlidingPlayView) v.findViewById(R.id.mAbSlidingPlayView);
		mAbSlidingPlayView.setNavHorizontalGravity(Gravity.CENTER);
		// mAbSlidingPlayView.setParentHScrollView(menuLayout);
		mAbSlidingPlayView.startPlay();
		mAbSlidingPlayView.setParentListView(mList);
		// adapter = new MyAdapter();
		mAbSlidingPlayView.setOnItemClickListener(new AbOnItemClickListener() {

			@Override
			public void onClick(int position) {

			}
		});
		
		loaction.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent =new Intent(mContext,SelectCityActivity.class);
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
					final View mView = View.inflate(getActivity(),R.layout.play_view_item, null);
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
		params.put("city",YoutiApplication.getInstance().myPreference.getCity());
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
										mContext,
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
											mContext,
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
								AbToastUtil.showToast(mContext,R.string.data_tips);
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
								com.alibaba.fastjson.JSONObject object = JSON.parseObject(response.toString());
								com.alibaba.fastjson.JSONArray jsonArray = object
										.getJSONArray("list1");

								if (cache.getAsString("four") == null) {
									cache.put("four", response.toString());

									String strHot = cache.getAsString("mHotitem");
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

				mCoachAdapter = new CoachListAdapterTest(getActivity().getApplicationContext(),mPopularcoach,mList,true);
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
		params.put("jd", YoutiApplication.getInstance().myPreference.getLocation_j());
		params.put("wd", YoutiApplication.getInstance().myPreference.getLocation_w());
		params.put("user_id", YoutiApplication.getInstance().myPreference.getUserId());
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
							String hotcourse = cache.getAsString("HotCoachCourse");

							com.alibaba.fastjson.JSONObject object = JSON
									.parseObject(hotcourse);
							com.alibaba.fastjson.JSONArray jsonArray = object
									.getJSONArray("list2");
							mPopularcourse = JSON.parseArray(jsonArray.toString(), CourseBean.class);

							mCourseAdapter = new CourseListAdapterTest(getActivity(), mPopularcourse,mList,true);
							mList.setAdapter(mCourseAdapter);
							ScreenUtils.setListViewHeightBasedOnChildren(mList);
							mList.setSelection(1);
							mCourseAdapter.notifyDataSetChanged();

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

								mCoachAdapter = new CoachListAdapterTest(getActivity(), mPopularcoach,mList,true);
								mList.setAdapter(mCoachAdapter);
								ScreenUtils
										.setListViewHeightBasedOnChildren(mList);
								mList.setSelection(1);
								mCoachAdapter.notifyDataSetChanged();

								// 热门课程
								com.alibaba.fastjson.JSONObject object2 = JSON
										.parseObject(response.toString());
								com.alibaba.fastjson.JSONArray jsonArray2 = object2
										.getJSONArray("list2");
								mPopularcourse = JSON.parseArray(
										jsonArray2.toString(), CourseBean.class);

							} else {
							}
						} catch (Exception e) {

						}
					};
				});
	}

	public final String mPageName="ContentFragment";
	  @Override
	  	public void onPause() {
				super.onPause();
				MobclickAgent.onPageEnd(mPageName);
			}

			@Override
			public void onResume() {
				super.onResume();
				headImageUrl = YoutiApplication.getInstance().myPreference.getHeadImgPath();
				if(headImageUrl.startsWith("http")){
					ImageLoader.getInstance().displayImage(headImageUrl, ivMenu_right);
				}else{
					if("http://112.126.72.250/ut_app".equals("http://112.126.72.250/ut_app"+headImageUrl)){
						ImageLoader.getInstance().displayImage("", ivMenu_right);
					}else{
						
						ImageLoader.getInstance().displayImage("http://112.126.72.250/ut_app"+headImageUrl, ivMenu_right);
					}

				}
				MobclickAgent.onPageStart(mPageName);
			}
			
			
			
			
			
	private void iniData() {
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
		imSearch.setOnClickListener(this);
		mImg_jz.setOnClickListener(this);
		mImg_ch.setOnClickListener(this);
		mImg_tz.setOnClickListener(this);
		mImg_sr.setOnClickListener(this);

		mHOME_qx.setOnClickListener(this);
		mHOME_tennis.setOnClickListener(this);
		mHOME_golf.setOnClickListener(this);
		mHOME_yd.setOnClickListener(this);
		mHOME_gd.setOnClickListener(this);
		mAbSlidingPlayView.setOnItemClickListener(new PagerItemClickListener());

		mList.setOnItemClickListener(new MyItemClickListener());
	}

	private MyLocationListener mMyLocationListener;
	private LocationClient mLocationClient;
	private View v;
	private String headImageUrl;

	private void InitLocation() {
		mLocationClient = new LocationClient(getActivity().getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);//
		option.setCoorType("bd09ll");// 默认 gcj02
		int span = 10000;
		option.setScanSpan(span);// 请求 间隔10000ms
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
				YoutiApplication.getInstance().myPreference.setCity(loc);
				YoutiApplication.getInstance().myPreference.setLocation_w("" + location.getLatitude());
				YoutiApplication.getInstance().myPreference.setLocation_j("" + location.getLongitude());
			}
			final String city = loc;
			final double latitude = location.getLatitude();
			final double ontitude = location.getLongitude();
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
			if (position == mPopularcoach.size()+1) {
				Bundle bundle = new Bundle();
				bundle.putString("val", "全部");
				IntentJumpUtils.nextActivity(CoachListActivity.class,getActivity(),bundle);
				return;
			}

			if (position == mPopularcourse.size()+1) {
				Bundle bundle = new Bundle();
				bundle.putString("val", "全部");
				IntentJumpUtils.nextActivity(CourseListActivity.class,getActivity(),bundle);
				return;
			}

			if (hotType == 1) {
				CoachBean bean  = new CoachBean();
				TextView tv = (TextView) view.findViewById(R.id.coach_tv_price);
				bean = (CoachBean) tv.getTag();
				Bundle bundle = new Bundle();
				bundle.putString(Constants.KEY_ID, bean.getCoach_id());
				bundle.putString(Constants.KEY_CHAT_TEL, bean.getCoach_tel());
				bundle.putString(Constants.KEY_CHAT_USERNAME, bean.getCoach_name());
				bundle.putString(Constants.KEY_CHAT_AVATAR, bean.getHead_img());
				IntentJumpUtils.nextActivity(CoachDetailActivity.class,getActivity(), bundle);
			} else if (hotType == 2) {
				CourseBean b = new CourseBean();
				TextView tv = (TextView) view.findViewById(R.id.kc_studyCounts);
				b = (CourseBean) tv.getTag();
				Bundle bundle = new Bundle();
				bundle.putString("val", "全部");
				bundle.putString("title", b.getDescribe());
				bundle.putString(Constants.KEY_ID, b.getCourse_id());
				IntentJumpUtils.nextActivity(CourseDetailActivity.class,getActivity(), bundle);
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
			((MainMainActivity)getActivity()).getSlidingMenu().toggle();

			break;

		case R.id.title_menu_right:
			((MainMainActivity)getActivity()).getSlidingMenu().showSecondaryMenu();
			break;

		case R.id.home_hot_jl:

			hotType = 1;
			checkHotTitle();
			mCoachAdapter = new CoachListAdapterTest(getActivity(),mPopularcoach,mList,true);
			mList.setAdapter(mCoachAdapter);
			ScreenUtils.setListViewHeightBasedOnChildren(mList);
			// mList.setSelection(1);
			mCoachAdapter.notifyDataSetChanged();
			break;
		case R.id.home_hot_kc:
			hotType = 2;
			checkHotTitle();
			mCourseAdapter = new CourseListAdapterTest(getActivity(),mPopularcourse,mList,true);
			mList.setAdapter(mCourseAdapter);
			ScreenUtils.setListViewHeightBasedOnChildren(mList);
			mCourseAdapter.notifyDataSetChanged();
			break;

		case R.id.rl_ksyd:
			if (NetTips.isNetTips(mContext)) {
				IntentJumpUtils.nextActivity(FastBookActivity.class, getActivity(), null);
			}
			break;
		case R.id.img_jz:
			if (NetTips.isNetTips(mContext)) {

				Bundle b = new Bundle();
				b.putString(Constants.KEY_ID, mProjects.get(0).getId());
				IntentJumpUtils.nextActivity(ActivePageActivity.class, getActivity(), b);
			}
			break;
		case R.id.img_ch:
			if (NetTips.isNetTips(mContext)) {
				Bundle b = new Bundle();
				b.putString(Constants.KEY_ID, mProjects.get(1).getId());
				IntentJumpUtils.nextActivity(ActivePageActivity.class, getActivity(), b);
			}
			break;
		case R.id.img_tz:
			if (NetTips.isNetTips(mContext)) {
				Bundle b = new Bundle();
				b.putString(Constants.KEY_ID, mProjects.get(2).getId());
				IntentJumpUtils.nextActivity(ActivePageActivity.class, getActivity(), b);
			}
			break;
		case R.id.img_sr:
			if (NetTips.isNetTips(mContext)) {
				//Bundle b = new Bundle();
				//b.putString("url", mProjects.get(3).getDe_url());
				IntentJumpUtils.nextActivity(ActivePageGjcActivity.class, getActivity(), null);
			}
			break;
		case R.id.new_qx:
			if (NetTips.isNetTips(mContext)) {
				Bundle b = new Bundle();
				b.putString("val", mHotitem.get(0).getVal());
				IntentJumpUtils.nextActivity(CoachListActivity.class, getActivity(), b);
			}
			break;
		case R.id.new_tennis:
			if (NetTips.isNetTips(mContext)) {
				Bundle b = new Bundle();
				b.putString("val", mHotitem.get(1).getVal());
				IntentJumpUtils.nextActivity(CoachListActivity.class, getActivity(), b);
			}
			break;
		case R.id.new_golf:
			if (NetTips.isNetTips(mContext)) {
				Bundle b = new Bundle();
				b.putString("val", mHotitem.get(2).getVal());
				IntentJumpUtils.nextActivity(CoachListActivity.class, getActivity(), b);
			}
			break;
		case R.id.new_gd:
			if (NetTips.isNetTips(mContext)) {
				IntentJumpUtils.nextActivity(MoreOptionActivity.class, getActivity(),
						null);
			}
			break;

		case R.id.im_search:
			if (NetTips.isNetTips(mContext)) {
				val = et_search.getText().toString();
				if(val.equals("")||val==null){
					Utils.showToast(mContext, "请输入教练或者课程名称");
					return;
				}
				searchVal();
			}
			break;

		default:
			break;
		}
	}

	/**
	* @Title: searchVal 
	* @Description: TODO(首頁 搜索 ) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void searchVal(){
		
		RequestParams params = new RequestParams();
		params.put("jd", YoutiApplication.getInstance().myPreference.getLocation_j());
		params.put("wd", YoutiApplication.getInstance().myPreference.getLocation_w());
		params.put("val", val);
		HttpUtils.post(Constants.HOME_SERACH, params,
				new JsonHttpResponseHandler() {
					
					public void onStart() {
						super.onStart();
					}

					public void onFailure(int statusCode,
							org.apache.http.Header[] headers,
							java.lang.Throwable throwable,
							org.json.JSONObject errorResponse) {

					};

					public void onFinish() {
						dialog.dismiss();
					};

					public void onSuccess(int statusCode,
							org.apache.http.Header[] headers,
							org.json.JSONObject response) {
						dialog.dismiss();

						try {
							String state = response.getString("code");
							if (state.equals("1")) {

								Bundle bundle = new Bundle();
								bundle.putString("val", val);
								IntentJumpUtils.nextActivity(CourseListActivity.class, getActivity(),bundle);
							} else if(state.equals("2")) {
								Bundle bundle = new Bundle();
								bundle.putString("val", val);
								IntentJumpUtils.nextActivity(CoachListActivity.class, getActivity(),bundle);
							}
						} catch (Exception e) {

						}
					};
				});
		
	}
	

	private void checkHotTitle() {
		mTv_hotjl.setTextColor(hotType==1?this.getResources().getColor(R.color.text_color_select):this.getResources().getColor(R.color.text_color_list_item));
		mTv_hotkc.setTextColor(hotType==2?this.getResources().getColor(R.color.text_color_select):this.getResources().getColor(R.color.text_color_list_item));
		
		ViewPropertyAnimator.animate(mTv_hotjl).scaleX(hotType==1?1.1f:1f).setDuration(200);
		ViewPropertyAnimator.animate(mTv_hotjl).scaleY(hotType==1?1.1f:1f).setDuration(200);
		ViewPropertyAnimator.animate(mTv_hotkc).scaleX(hotType==2?1.1f:1f).setDuration(200);
		ViewPropertyAnimator.animate(mTv_hotkc).scaleY(hotType==2?1.1f:1f).setDuration(200);
	}
	
	private class PagerItemClickListener implements AbOnItemClickListener{

		@Override
		public void onClick(int position) {
			switch (position) {
			case 0:
				IntentJumpUtils.nextActivity(BuyPackageCourseActivity.class, getActivity(), null);
				break;

			case 1:
				IntentJumpUtils.nextActivity(BuyPackageCourseActivity.class, getActivity(), null);
				break;
			case 2:
				IntentJumpUtils.nextActivity(BuyPackageCourseActivity.class, getActivity(), null);
				break;
			default:
				break;
			}
		}
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore() {
		if (NetTips.isNetTips(mContext)) {
			Bundle bundle = new Bundle();
			bundle.putString("val", "全部");
			IntentJumpUtils.nextActivity(CourseListActivity.class, getActivity(),bundle);
		}
		
	}

	

}
