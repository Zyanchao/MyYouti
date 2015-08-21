package com.youti.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ab.util.AbToastUtil;
import com.alibaba.fastjson.JSON;
import com.example.youti_geren.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.youti.appConfig.Constants;
import com.youti.appConfig.YoutiApplication;
import com.youti.utils.HttpUtils;
import com.youti.view.XListView;
import com.youti.view.XListView.IXListViewListener;
import com.youti.view.specialcalender.DateAdapter;
import com.youti.view.specialcalender.SpecialCalendar;
import com.youti.yonghu.adapter.CommentAdapter;
import com.youti.yonghu.adapter.CourseListAdapter;
import com.youti.yonghu.adapter.CourseListAdapterTest;
import com.youti.yonghu.adapter.PraisePicListAdapter;
import com.youti.yonghu.bean.Comment;
import com.youti.yonghu.bean.CommentBean;
import com.youti.yonghu.bean.CourseBean;
import com.youti.yonghu.bean.UserEntity;

/**
 * 俱乐部其他课程 列表 页
 * @author zyc
 *
 */
public class ClubCourseListFragment extends Fragment implements OnGestureListener,IXListViewListener{

	
	private ViewFlipper flipper1 = null;
	private GridView gridView = null;
	private GestureDetector gestureDetector = null;
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private int week_c = 0;
	private int week_num = 0;
	private String currentDate = "";
	private static int jumpWeek = 0;
	private static int jumpMonth = 0;
	private static int jumpYear = 0;
	private DateAdapter dateAdapter;
	private int daysOfMonth = 0; // 某月的天数
	private int dayOfWeek = 0; // 具体某一天是星期几
	private int weeksOfMonth = 0;
	private SpecialCalendar sc = null;
	private boolean isLeapyear = false; // 是否为闰年
	private int selectPostion = 0;
	private String dayNumbers[] = new String[7];
	private TextView tvDate;
	private int currentYear;
	private int currentMonth;
	private int currentWeek;
	private int currentDay;
	private int currentNum;
	private boolean isStart;// 是否是交接的月初
	private List<CourseBean> mCourseLists = new ArrayList<CourseBean>();
	private CourseListAdapterTest mAdapter;
	private int currentPage=1;//当期页码
	boolean flagLoadMore = false;
	private Handler mHander;
	String year;
	String month = null;
	String day = null;
	String dateM = null;
	
	private TextView mTvtips;
	public ClubCourseListFragment() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
		currentDate = sdf.format(date);
		year_c = Integer.parseInt(currentDate.split("-")[0]);
		month_c = Integer.parseInt(currentDate.split("-")[1]);
		day_c = Integer.parseInt(currentDate.split("-")[2]);
		currentYear = year_c;
		currentMonth = month_c;
		currentDay = day_c;
		sc = new SpecialCalendar();
		getCalendar(year_c, month_c);
		week_num = getWeeksOfMonth();
		currentNum = week_num;
		if (dayOfWeek == 7) {
			week_c = day_c / 7 + 1;
		} else {
			if (day_c <= (7 - dayOfWeek)) {
				week_c = 1;
			} else {
				if ((day_c - (7 - dayOfWeek)) % 7 == 0) {
					week_c = (day_c - (7 - dayOfWeek)) / 7 + 1;
				} else {
					week_c = (day_c - (7 - dayOfWeek)) / 7 + 2;
				}
			}
		}
		currentWeek = week_c;
		getCurrent();

	}

	/**
	 * 判断某年某月所有的星期数
	 * 
	 * @param year
	 * @param month
	 */
	public int getWeeksOfMonth(int year, int month) {
		// 先判断某月的第一天为星期几
		int preMonthRelax = 0;
		int dayFirst = getWhichDayOfWeek(year, month);
		int days = sc.getDaysOfMonth(sc.isLeapYear(year), month);
		if (dayFirst != 7) {
			preMonthRelax = dayFirst;
		}
		if ((days + preMonthRelax) % 7 == 0) {
			weeksOfMonth = (days + preMonthRelax) / 7;
		} else {
			weeksOfMonth = (days + preMonthRelax) / 7 + 1;
		}
		return weeksOfMonth;

	}

	/**
	 * 判断某年某月的第一天为星期几
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public int getWhichDayOfWeek(int year, int month) {
		return sc.getWeekdayOfMonth(year, month);

	}

	/**
	 * 
	 * @param year
	 * @param month
	 */
	public int getLastDayOfWeek(int year, int month) {
		return sc.getWeekDayOfLastMonth(year, month,
				sc.getDaysOfMonth(isLeapyear, month));
	}

	public void getCalendar(int year, int month) {
		isLeapyear = sc.isLeapYear(year); // 是否为闰年
		daysOfMonth = sc.getDaysOfMonth(isLeapyear, month); // 某月的总天数
		dayOfWeek = sc.getWeekdayOfMonth(year, month); // 某月第一天为星期几
	}

	public int getWeeksOfMonth() {
		// getCalendar(year, month);
		int preMonthRelax = 0;
		if (dayOfWeek != 7) {
			preMonthRelax = dayOfWeek;
		}
		if ((daysOfMonth + preMonthRelax) % 7 == 0) {
			weeksOfMonth = (daysOfMonth + preMonthRelax) / 7;
		} else {
			weeksOfMonth = (daysOfMonth + preMonthRelax) / 7 + 1;
		}
		return weeksOfMonth;
	}
	
	View mView;
	GridView mGridView;
	XListView mListView;
	TextView mTvdianzan;
	CommentBean commentBean;
	List<Comment> commentList= new ArrayList<Comment>();
	List<UserEntity> userList =new ArrayList<UserEntity>();
	PraisePicListAdapter praiseAdapter;
	CommentAdapter commentAdapter;
	String id;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = mView.inflate(getActivity(), R.layout.club_course_list, null);
		
		id = getActivity().getIntent().getStringExtra(Constants.KEY_ID);
		commentBean = new CommentBean();
		mGridView=(GridView) mView.findViewById(R.id.gv_dianzan);
		mListView=(XListView) mView.findViewById(R.id.club_kc_list);
		mListView.setFocusable(false);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this);
		mHander = new Handler();
		mTvdianzan = (TextView) mView.findViewById(R.id.tv_dianzan);
		mTvtips = (TextView) mView.findViewById(R.id.tv_tips);
		gestureDetector = new GestureDetector(this);
		flipper1 = (ViewFlipper) mView.findViewById(R.id.flipper1);
		dateAdapter = new DateAdapter(getActivity(), getResources(), currentYear,
				currentMonth, currentWeek, currentNum, selectPostion,
				currentWeek == 1 ? true : false);
		addGridView();
		
		dayNumbers = dateAdapter.getDayNumbers();
		gridView.setAdapter(dateAdapter);
		selectPostion = dateAdapter.getTodayPosition();
		gridView.setSelection(selectPostion);
		flipper1.addView(gridView, 0);
		
		
		if(currentMonth<10){
			month = "0"+currentMonth;
		}else{
			month = ""+currentMonth;
		}
		if(currentDay<10){
			day = "0"+currentDay;
		}else{
			day = ""+currentDay;
		}
	    year = currentYear+"";
	    dateM = year+month+day;
	    getCourse(flagLoadMore,dateM);
		return mView;
	}
	
	
	
	
private void addGridView() {
	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	gridView = new GridView(getActivity());
	gridView.setNumColumns(7);
	gridView.setGravity(Gravity.CENTER_VERTICAL);
	gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
	gridView.setVerticalSpacing(1);
	gridView.setHorizontalSpacing(1);
	gridView.setOnTouchListener(new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			return ClubCourseListFragment.this.gestureDetector.onTouchEvent(event);
		}
	});
	
	gridView.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			selectPostion = position;
			dateAdapter.setSeclection(position);
			dateAdapter.notifyDataSetChanged();
			
			if(dateAdapter.getCurrentMonth(selectPostion)<10){
				month = "0"+dateAdapter.getCurrentMonth(selectPostion);
			}else{
				month = ""+dateAdapter.getCurrentMonth(selectPostion);
			}
			if(Integer.parseInt(dayNumbers[position])<10){
				day = "0"+dayNumbers[position];
			}else{
				day = ""+dayNumbers[position];
			}
		    year = dateAdapter.getCurrentYear(selectPostion)+"";
		    dateM = year+month+day;
			getCourse(flagLoadMore,dateM);
		}
	});
	gridView.setLayoutParams(params);
}

	
	private void getCourse(final Boolean flagLoadMore,String dateM) {
		
		
		RequestParams params = new RequestParams();
		//params.put("user_id", user_id);
		
		params.put("club_id", id);
		params.put("ser_time", dateM);
		params.put("jd", YoutiApplication.getInstance().myPreference.getLocation_j());
		params.put("wd", YoutiApplication.getInstance().myPreference.getLocation_w());
		
		//if(YoutiApplication.getInstance().)
		//params.put("user_id", currentPage);
		//params.put("page", currentPage);
		HttpUtils.post(Constants.CLUB_COURSE, params, new JsonHttpResponseHandler(){
			public void onStart() {
				super.onStart();
			}

			public void onFailure(int statusCode,
					org.apache.http.Header[] headers,
					java.lang.Throwable throwable,
					org.json.JSONObject errorResponse) {
			};

			public void onFinish() {
					    
			};
			public void onSuccess(int statusCode,
					org.apache.http.Header[] headers,
					org.json.JSONObject response) {
				try {
					response.toString();
					String state = response.getString("code");
					System.out.println(response.toString());
					if (state.equals("1")) {
						com.alibaba.fastjson.JSONObject object = JSON.parseObject(response.toString()).getJSONObject("list");
						String fdd = object.getString("list1");
						mCourseLists = JSON.parseArray(fdd,CourseBean.class);
						if(mCourseLists.size()==0){
							mTvtips.setVisibility(View.VISIBLE);
							AbToastUtil.showToast(getActivity(), "没有更多数据...");
							return;
						}
						if(mAdapter==null){
							mAdapter = new CourseListAdapterTest(getActivity(), mCourseLists,mListView,true);
							mListView.setAdapter(mAdapter);
							mAdapter.notifyDataSetChanged();
						}else{
							if(flagLoadMore){
								mAdapter.addAndRefreshListView(mCourseLists);
								if(mCourseLists.size()==0){
									AbToastUtil.showToast(getActivity(), "没有更多数据了...");
								}
							}else{
								mAdapter.refreshListView(mCourseLists);
							}
						}
						
					} else {
						AbToastUtil.showToast(getActivity(), "没有更多数据了...");
					}
				} catch (Exception e) {

				}
			};
		});
		
	}
	
	
	/**
	 * 重新计算当前的年月
	 */
	public void getCurrent() {
		if (currentWeek > currentNum) {
			if (currentMonth + 1 <= 12) {
				currentMonth++;
			} else {
				currentMonth = 1;
				currentYear++;
			}
			currentWeek = 1;
			currentNum = getWeeksOfMonth(currentYear, currentMonth);
		} else if (currentWeek == currentNum) {
			if (getLastDayOfWeek(currentYear, currentMonth) == 6) {
			} else {
				if (currentMonth + 1 <= 12) {
					currentMonth++;
				} else {
					currentMonth = 1;
					currentYear++;
				}
				currentWeek = 1;
				currentNum = getWeeksOfMonth(currentYear, currentMonth);
			}

		} else if (currentWeek < 1) {
			if (currentMonth - 1 >= 1) {
				currentMonth--;
			} else {
				currentMonth = 12;
				currentYear--;
			}
			currentNum = getWeeksOfMonth(currentYear, currentMonth);
			currentWeek = currentNum - 1;
		}
	}
	
	
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int gvFlag = 0;
		if (e1.getX() - e2.getX() > 80) {
			// 向左滑
			addGridView();
			currentWeek++;
			getCurrent();
			dateAdapter = new DateAdapter(getActivity(), getResources(), currentYear,
					currentMonth, currentWeek, currentNum, selectPostion,
					currentWeek == 1 ? true : false);
			dayNumbers = dateAdapter.getDayNumbers();
			gridView.setAdapter(dateAdapter);
			/*tvDate.setText(dateAdapter.getCurrentYear(selectPostion) + "年"
					+ dateAdapter.getCurrentMonth(selectPostion) + "月"
					+ dayNumbers[selectPostion] + "日");*/
			gvFlag++;
			flipper1.addView(gridView, gvFlag);
			dateAdapter.setSeclection(selectPostion);
			this.flipper1.setInAnimation(AnimationUtils.loadAnimation(getActivity(),
					R.anim.push_left_in));
			this.flipper1.setOutAnimation(AnimationUtils.loadAnimation(getActivity(),
					R.anim.push_left_out));
			this.flipper1.showNext();
			flipper1.removeViewAt(0);
			return true;

		} else if (e1.getX() - e2.getX() < -80) {
			addGridView();
			currentWeek--;
			getCurrent();
			dateAdapter = new DateAdapter(getActivity(), getResources(), currentYear,
					currentMonth, currentWeek, currentNum, selectPostion,
					currentWeek == 1 ? true : false);
			dayNumbers = dateAdapter.getDayNumbers();
			gridView.setAdapter(dateAdapter);
			/*tvDate.setText(dateAdapter.getCurrentYear(selectPostion) + "年"
					+ dateAdapter.getCurrentMonth(selectPostion) + "月"
					+ dayNumbers[selectPostion] + "日");*/
			gvFlag++;
			flipper1.addView(gridView, gvFlag);
			dateAdapter.setSeclection(selectPostion);
			this.flipper1.setInAnimation(AnimationUtils.loadAnimation(getActivity(),
					R.anim.push_right_in));
			this.flipper1.setOutAnimation(AnimationUtils.loadAnimation(getActivity(),
					R.anim.push_right_out));
			this.flipper1.showPrevious();
			flipper1.removeViewAt(0);
			return true;
			// }
		}
		return false;
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		return this.gestureDetector.onTouchEvent(event);
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
				  getCourse(flagLoadMore,dateM);
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
	
protected void loadMore() {
		
		currentPage++;//每次 点击加载更多，请求页码 +1
		flagLoadMore = true;
		getCourse(flagLoadMore,dateM);
	}
}
