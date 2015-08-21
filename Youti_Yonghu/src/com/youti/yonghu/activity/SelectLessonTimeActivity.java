package com.youti.yonghu.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.Constants;
import com.youti.appConfig.YoutiApplication;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
import com.youti.view.TitleBar;
import com.youti.yonghu.bean.DateTimeBeanTest;
import com.youti.yonghu.bean.DateTimeBeanTest.DateTime;
import com.youti.yonghu.bean.InfoBean;

public class SelectLessonTimeActivity extends Activity {
	/**
	 * 整体实现思路：从第一天到第七天，每天都有一个int it与之对应，分别是1到7.为Map集合的key
	 * 				这第一天到第七天的时间都保存在list集合中。这个list集合为每个key对应的value
	 * 
	 * 			 当我们点击某个key所对应的天时，展示从网络获取的数据。当我们选择小时时，将选择的小时保存在这个list中。
	 * 			每当我们点击小时时，就判断这个小时是否被点击过，如果是选中状态，就移除list集合，状态设置未选中。如果是未选中状态，就添加到list集中，状态设置为选中状态。
	 * 
	 * 		这样当我们切换到其他界面再切换回来，就能从对于的key的list集合中，取回选中的数据。
	 */
	View mView;
	TitleBar titleBar;
	Button btn_sure;
	String user_id;
	String order_id;
	String remnant;
	String coach_id;
	private TextView mTv_Date1;
	private TextView mTv_Date2;
	private TextView mTv_Date3;
	private TextView mTv_Date4;
	private TextView mTv_Date5;
	private TextView mTv_Date6;
	private TextView mTv_Date7;
	private DateTimeBeanTest fromJson;
	//HashMap用来存放每天被点击的时间段
	HashMap<Integer,ArrayList> timeMap=new HashMap<Integer,ArrayList>();
	//下面7个集合，分别用来存放七天当中某天被点击的时间段
	ArrayList<Integer> list1=new ArrayList<Integer>();
	ArrayList<Integer> list2=new ArrayList<Integer>();
	ArrayList<Integer> list3=new ArrayList<Integer>();
	ArrayList<Integer> list4= new ArrayList<Integer>();
	ArrayList<Integer> list5=new ArrayList<Integer>();
	ArrayList<Integer> list6=new ArrayList<Integer>();
	ArrayList<Integer> list7=new ArrayList<Integer>();
	private List<DateTime> listdate;
	public final String mPageName ="SelectLessonTimeActivity";
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
	
	TextView hourable;
	private final int BEGIN=0;
	ArrayList<ImageView> photo_list=new ArrayList<ImageView>();
	//private List<DateTime> mSchedule = new ArrayList<DateTime>();
	
	private HashMap<Integer,Boolean> isSelected ;
	
	public HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		this.isSelected = isSelected;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.date_time_surface);
		if(getIntent()!=null){
			order_id=getIntent().getStringExtra("order_id");
			remnant=getIntent().getStringExtra("remnant");
			coach_id=getIntent().getStringExtra("coach_id");
		}
		titleBar =(TitleBar) findViewById(R.id.index_titlebar);
		titleBar.setTitleBarTitle("选择上课时间");
		titleBar.setSearchGone();
		titleBar.setShareGone(false);
		btn_sure=(Button) findViewById(R.id.btn_sure);
		hourable=(TextView) findViewById(R.id.tv_hourable);		
		hourable.setText(remnant);
		//hourable.setText(100+"");
		checkNum=Integer.parseInt(remnant);
		/**
		 * 为什么从1开始添加？而不是从0开始添加？
		 * 
		 * 因为it是从1开始的
		 */
		timeMap.put(1, list1);
		timeMap.put(2, list2);
		timeMap.put(3, list3);
		timeMap.put(4, list4);
		timeMap.put(5, list5);
		timeMap.put(6, list6);
		timeMap.put(7, list7);
		
		user_id=((YoutiApplication)getApplication()).myPreference.getUserId();
		mGridView = (ListView) findViewById(R.id.grid_view);
		btn_sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			//	Utils.showToast(SelectLessonTimeActivity.this, "一次约课，只能选择一个连续时间段");
				
				yueke();
			}
		});
		showmView1();
		//gainData();
	}

	
	protected void yueke() {
		long start_time=0L;
		long end_time=0l;
		long start_Temp=0l;
		//遍历timeMap集合，找出被选中的时间。
		/**
		 * 将添加到数组中的位置进行排序，这样方便判断
		 */
		boolean flag=false;
		boolean first=true;
		for(int i=1;i<8;i++){		
			long temp=0L;
			
			
			
			for(int a=0;a<timeMap.get(i).size();a++){
				//每个被选中的时间段在list集合中的position
				Integer t = (Integer) timeMap.get(i).get(a);
				//得到被选中的时间段的起始小时，比如九点的9，下午四点的16. i表示这是第几天的				
				Integer time = t+9;
				//传入第几天，哪个小时，返回一个十位数的时间戳。
				start_time = calcuteTimeStamp(i,time);
				if(first){
					start_Temp=start_time;
					first=false;
				}
				end_time=start_time+3600L;			
				if(timeMap.get(i).size()!=1&&temp>0&&Math.abs(temp-start_time)>3600){
					//如果用户选择了多个小时，但是不连续，那么就不放过
					Utils.showToast(SelectLessonTimeActivity.this, "一次约课，只能选择一个连续时间段");	
					return;
				}else if(timeMap.get(i).size()!=1&&temp>0&&Math.abs(temp-start_time)==3600&&a==timeMap.get(i).size()-1){
					//如果用户选择了多个小时，那么就判断是否连续，如果全是连续，并且达到了集合的最后一个，那么也直接放过 
					//连续的
					flag=false;
					   for(int b=i+1;b<8;b++){
						   if(!timeMap.get(b).isEmpty()){
							   flag=true;
						   }
					   }
					break;
				}else if(timeMap.get(i).size()==1){
					//如果用户只选择了一个小时，那么久直接放过。。这里没有考虑第一天选择了一个小时，第二天选择了多个小时的情况
					
					for(int b=i+1;b<8;b++){
						   if(!timeMap.get(b).isEmpty()){
							   flag=true;
						   }
					   }
					break;
				}
				temp=start_time;
				
				flag=true;
			}
			if(flag){
				
				Utils.showToast(SelectLessonTimeActivity.this, "一次约课，只能选择一个连续时间段");
				return;
			}
		}
		//如果被选择的时间是不连贯的，那么就提醒用户，选择连续时间段
		
		
		RequestParams params =new RequestParams();
		/**
		 * 这里的用户Id暂时设置为1
		 */
		Utils.showToast(SelectLessonTimeActivity.this, start_time+":"+end_time);
		params.put("user_id", "1");
		params.put("order_id", order_id);
		params.put("start_time", start_Temp+"");
		params.put("end_time", end_time+"");
		String strYueke="http://112.126.72.250/ut_app/index.php?m=User&a=agree_class";
		System.out.println(start_time+":"+end_time);
		HttpUtils.post(strYueke, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson =new Gson();
				InfoBean infoBean = gson.fromJson(arg2, InfoBean.class);
				if("1".equals(infoBean.code)){
					Utils.showToast(SelectLessonTimeActivity.this, "约课成功");
					finish();
				}else if("0".equals(infoBean.code)){
					Utils.showToast(SelectLessonTimeActivity.this, "约课失败");
				}else if("404".equals(infoBean.code)){
					Utils.showToast(SelectLessonTimeActivity.this, "约课时长大于订单剩余时长");
				}else if("403".equals(infoBean.code)){
					Utils.showToast(SelectLessonTimeActivity.this, "约课结束时间为空");
				}else if("402".equals(infoBean.code)){
					Utils.showToast(SelectLessonTimeActivity.this, "约课开始时间为空");
				}else if("401".equals(infoBean.code)){
					Utils.showToast(SelectLessonTimeActivity.this, "订单id为空");
				}else{
					Utils.showToast(SelectLessonTimeActivity.this, "用户id为空");
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Utils.showToast(SelectLessonTimeActivity.this, "网络连接异常");
			}
		});
	}

	/**
	 * 返回十位数的时间戳
	 * @param i  表示那一天
	 * @param time 表示哪个时间段
	 */
	private long calcuteTimeStamp(int i, Integer time) {
		String dayOneYear = getDayOneYear(i);			
		
		
		SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddHH");

		 try {
			 if(time>10){
				 t = time+""; 
			 }else{
				 t="0"+time;
			 }
			 //201507099   2015070909  的值是一样的 都是1436403600000
			 //2015070929  是10号的5点
			 long millionSeconds=simple.parse(dayOneYear+time).getTime();
			Utils.showToast(SelectLessonTimeActivity.this, dayOneYear+time+":"+i+":"+millionSeconds);
			return millionSeconds/1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 return -1;
	}


	int it = 1;
	private String yue;
	private String days;
	TimeAdapter adapter;
	
	public String getDayOneYear(int it){
		Time time = new Time("GMT+8");
		time.setToNow(); // 获取系统时间
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyy");
		int year = time.year;
		int month = time.month;
		
		Calendar ca = Calendar.getInstance();
		 int s =ca.get(Calendar.MONTH)+1;
		if (it == 1) {
			String  ss = mTv_Date1.getText().toString();
			int index=ss.indexOf("-");
			yue = ss.substring(0,index);
			//yue = s+"";
			days=ss.substring(index+1, ss.length());
			/*int day = time.monthDay;
			if (day < 10) {
				days = "0" + day;
			} else {
				days = "" + day;
			}*/
		} else if (it == 2) {
			String  ss = mTv_Date2.getText().toString();
			int index=ss.indexOf("-");
			yue = ss.substring(0,index);
			days=ss.substring(index+1,ss.length());
//			year = new Date(System.currentTimeMillis() + 1 * 24 * 60 * 60* 1000).getYear();
			year=Integer.parseInt(sdf1.format(new Date(System.currentTimeMillis() + 1 * 24 * 60 * 60* 1000)));

		} else if (it == 3) {
			String  ss = mTv_Date3.getText().toString();
			int index=ss.indexOf("-");
			yue = ss.substring(0,index);
			days=ss.substring(index+1,ss.length());

//			year= new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60
//					* 1000).getYear();
			year=Integer.parseInt(sdf1.format(new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60* 1000)));
		
		} else if (it == 4) {
			String  ss = mTv_Date4.getText().toString();
			int index=ss.indexOf("-");
			yue = ss.substring(0,index);
			days=ss.substring(index+1,ss.length());

//			year = new Date(System.currentTimeMillis() + 3 * 24 * 60 * 60
//					* 1000).getYear();
			year=Integer.parseInt(sdf1.format(new Date(System.currentTimeMillis() + 3 * 24 * 60 * 60* 1000)));
			
		} else if (it == 5) {
			String  ss = mTv_Date5.getText().toString();
			int index=ss.indexOf("-");
			yue = ss.substring(0,index);
			days=ss.substring(index+1,ss.length());

//			year = new Date(System.currentTimeMillis() + 4 * 24 * 60 * 60
//					* 1000).getYear();
			year=Integer.parseInt(sdf1.format(new Date(System.currentTimeMillis() + 4 * 24 * 60 * 60* 1000)));
			
		}else if (it == 6) {
			String  ss = mTv_Date6.getText().toString();
			int index=ss.indexOf("-");
			yue = ss.substring(0,index);
			days=ss.substring(index+1,ss.length());

//			year= new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60
//					* 1000).getYear();
			year=Integer.parseInt(sdf1.format(new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60* 1000)));
		
		}else if (it == 7) {
			String  ss = mTv_Date7.getText().toString();
			int index=ss.indexOf("-");
			yue = ss.substring(0,index);
			days=ss.substring(index+1,ss.length());
//			year= new Date(System.currentTimeMillis() + 6 * 24 * 60 * 60
//					* 1000).getYear();
			year=Integer.parseInt(sdf1.format(new Date(System.currentTimeMillis() + 6 * 24 * 60 * 60* 1000)));
		
		}
		return year+yue+days;
	}
	
	private void gainData() {

		Time time = new Time("GMT+8");
		time.setToNow(); // 获取系统时间
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyy");
		int year = time.year;
		int month = time.month;
		
		Calendar ca = Calendar.getInstance();
		 int s =ca.get(Calendar.MONTH)+1;
		if (it == 1) {
			String  ss = mTv_Date1.getText().toString();
			int index=ss.indexOf("-");
			yue = ss.substring(0,index);
			//yue = s+"";
			days=ss.substring(index+1, ss.length());
			/*int day = time.monthDay;
			if (day < 10) {
				days = "0" + day;
			} else {
				days = "" + day;
			}*/
		} else if (it == 2) {
			String  ss = mTv_Date2.getText().toString();
			int index=ss.indexOf("-");
			yue = ss.substring(0,index);
			days=ss.substring(index+1,ss.length());
//			year = new Date(System.currentTimeMillis() + 1 * 24 * 60 * 60* 1000).getYear();
			year=Integer.parseInt(sdf1.format(new Date(System.currentTimeMillis() + 1 * 24 * 60 * 60* 1000)));

		} else if (it == 3) {
			String  ss = mTv_Date3.getText().toString();
			int index=ss.indexOf("-");
			yue = ss.substring(0,index);
			days=ss.substring(index+1,ss.length());

//			year= new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60
//					* 1000).getYear();
			year=Integer.parseInt(sdf1.format(new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60* 1000)));
		
		} else if (it == 4) {
			String  ss = mTv_Date4.getText().toString();
			int index=ss.indexOf("-");
			yue = ss.substring(0,index);
			days=ss.substring(index+1,ss.length());

//			year = new Date(System.currentTimeMillis() + 3 * 24 * 60 * 60
//					* 1000).getYear();
			year=Integer.parseInt(sdf1.format(new Date(System.currentTimeMillis() + 3 * 24 * 60 * 60* 1000)));
			
		} else if (it == 5) {
			String  ss = mTv_Date5.getText().toString();
			int index=ss.indexOf("-");
			yue = ss.substring(0,index);
			days=ss.substring(index+1,ss.length());

//			year = new Date(System.currentTimeMillis() + 4 * 24 * 60 * 60
//					* 1000).getYear();
			year=Integer.parseInt(sdf1.format(new Date(System.currentTimeMillis() + 4 * 24 * 60 * 60* 1000)));
			
		}else if (it == 6) {
			String  ss = mTv_Date6.getText().toString();
			int index=ss.indexOf("-");
			yue = ss.substring(0,index);
			days=ss.substring(index+1,ss.length());

//			year= new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60
//					* 1000).getYear();
			year=Integer.parseInt(sdf1.format(new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60* 1000)));
		
		}else if (it == 7) {
			String  ss = mTv_Date7.getText().toString();
			int index=ss.indexOf("-");
			yue = ss.substring(0,index);
			days=ss.substring(index+1,ss.length());
//			year= new Date(System.currentTimeMillis() + 6 * 24 * 60 * 60
//					* 1000).getYear();
			year=Integer.parseInt(sdf1.format(new Date(System.currentTimeMillis() + 6 * 24 * 60 * 60* 1000)));
		
		}

		urlString = Constants.GET_DATA;
		params = new RequestParams();
		
		params.put("time",year+yue+days);
		Utils.showToast(SelectLessonTimeActivity.this, year+yue+days);
		//params.put("uid","");
		//params.put("cid", "1000000163");
		
		
		//params.put("orderid", "");
		params.put("coach_id", coach_id);
		
		
		HttpUtils.post(urlString, params, new TextHttpResponseHandler() {
			
			

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson =new Gson();
				DateTimeBeanTest dateTimeBeanTest = gson.fromJson(arg2, DateTimeBeanTest.class);
				if(dateTimeBeanTest!=null&&"1".equals(dateTimeBeanTest.code)){
					listdate = dateTimeBeanTest.list;
					adapter=new TimeAdapter();
					mGridView.setAdapter(adapter);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				
			}
		});
		
	}
	
	Handler handler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case BEGIN:
				
				break;

			default:
				break;
			}
		};
	};
	private void showmView1() {
		
		String[] weeks = { "周日", "周一", "周二", "周三", "周四", "周五", "周六",
				"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
		Date date = new Date();
		//返回一周中的某一天。0表示周日，6表示周六
		int week = date.getDay();
		
		final 	RelativeLayout mR1 = (RelativeLayout) findViewById(R.id.rl_1);
		final	RelativeLayout mR2 = (RelativeLayout) findViewById(R.id.rl_2);
		final	RelativeLayout mR3 = (RelativeLayout) findViewById(R.id.rl_3);
		final	RelativeLayout mR4 = (RelativeLayout)findViewById(R.id.rl_4);
		final	RelativeLayout mR5 = (RelativeLayout)findViewById(R.id.rl_5);
		final	RelativeLayout mR6 = (RelativeLayout)findViewById(R.id.rl_6);
		final	RelativeLayout mR7 = (RelativeLayout)findViewById(R.id.rl_7);
		
		
		
		TextView mTv_1 = (TextView) findViewById(R.id.Tv_1);
		TextView mTv_2 = (TextView) findViewById(R.id.Tv_2);
		TextView mTv_3 = (TextView) findViewById(R.id.Tv_3);
		TextView mTv_4 = (TextView) findViewById(R.id.Tv_4);
		TextView mTv_5 = (TextView) findViewById(R.id.Tv_5);
		TextView mTv_6 = (TextView) findViewById(R.id.Tv_6);
		TextView mTv_7 = (TextView) findViewById(R.id.Tv_7);
		
		mTv_Date1 = (TextView)findViewById(R.id.tv_date1);		
		mTv_Date2 = (TextView) findViewById(R.id.tv_date2);
		mTv_Date3 = (TextView) findViewById(R.id.tv_date3);
		mTv_Date4 = (TextView) findViewById(R.id.tv_date4);
		mTv_Date5 = (TextView) findViewById(R.id.tv_date5);
		mTv_Date6=(TextView) findViewById(R.id.tv_date6);
		mTv_Date7=(TextView) findViewById(R.id.tv_date7);
		
		
		
		
		//ImageView mImg = (ImageView) mView.findViewById(R.id.im_de);
		
			//adapter = new TimeAdapter();
		  mGridView.setAdapter(adapter);
		
		
			mR1.setBackgroundResource(R.drawable.yksjb_select1);
			mR2.setBackgroundResource(R.drawable.jlxq_sjb2);
			mR3.setBackgroundResource(R.drawable.jlxq_sjb2);
			mR4.setBackgroundResource(R.drawable.jlxq_sjb2);
			mR5.setBackgroundResource(R.drawable.jlxq_sjb2);
			mR6.setBackgroundResource(R.drawable.jlxq_sjb2);
			mR7.setBackgroundResource(R.drawable.jlxq_sjb2);
			it=1;
	//		mSchedule.clear();
			gainData() ;
			//这里需要注意的是子线程还没有访问网络结束，adaoter就可爱是调用了
			//adapter.notifyDataSetChanged();
		
		
		
		mR1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mR1.setBackgroundResource(R.drawable.yksjb_select1);
				mR2.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR3.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR4.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR5.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR6.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR7.setBackgroundResource(R.drawable.jlxq_sjb2);
				it=1;
//				mSchedule.clear();
				gainData() ;
				adapter.notifyDataSetChanged();
				
				
				
			}
		});
		
	  mR2.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			mR1.setBackgroundResource(R.drawable.jlxq_sjb2);
			mR2.setBackgroundResource(R.drawable.yksjb_select1);
			mR3.setBackgroundResource(R.drawable.jlxq_sjb2);
			mR4.setBackgroundResource(R.drawable.jlxq_sjb2);
			mR5.setBackgroundResource(R.drawable.jlxq_sjb2);
			mR6.setBackgroundResource(R.drawable.jlxq_sjb2);
			mR7.setBackgroundResource(R.drawable.jlxq_sjb2);
			it=2;
//			mSchedule.clear();
			gainData() ;
			adapter.notifyDataSetChanged();
		}
	});
	  mR3.setOnClickListener(new OnClickListener() {
		  
		  @Override
		  public void onClick(View arg0) {
			  mR1.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR2.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR3.setBackgroundResource(R.drawable.yksjb_select1);
				mR4.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR5.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR6.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR7.setBackgroundResource(R.drawable.jlxq_sjb2);
			  it=3;
//			  mSchedule.clear();
			  gainData() ;
				adapter.notifyDataSetChanged();
		  }
	  });
	  mR4.setOnClickListener(new OnClickListener() {
		  
		  @Override
		  public void onClick(View arg0) {
			  mR1.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR2.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR3.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR4.setBackgroundResource(R.drawable.yksjb_select1);
				mR5.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR6.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR7.setBackgroundResource(R.drawable.jlxq_sjb2);
			  it=4;
//			  mSchedule.clear();
			  gainData() ;
				adapter.notifyDataSetChanged();
		  }
	  });
	  mR5.setOnClickListener(new OnClickListener() {
		  
		  @Override
		  public void onClick(View arg0) {
			  mR1.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR2.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR3.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR4.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR5.setBackgroundResource(R.drawable.yksjb_select1);
				mR6.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR7.setBackgroundResource(R.drawable.jlxq_sjb2);
			  it=5;
//			  mSchedule.clear();
			  gainData() ;
				adapter.notifyDataSetChanged();
		  }
	  });
	  
	  mR6.setOnClickListener(new OnClickListener() {
		  
		  @Override
		  public void onClick(View arg0) {
			  mR1.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR2.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR3.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR4.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR5.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR6.setBackgroundResource(R.drawable.yksjb_select1);
				mR7.setBackgroundResource(R.drawable.jlxq_sjb2);
			  it=6;
//			  mSchedule.clear();
			  gainData() ;
				adapter.notifyDataSetChanged();
		  }
	  });
	  
	  mR7.setOnClickListener(new OnClickListener() {
		  
		  @Override
		  public void onClick(View arg0) {
			  mR1.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR2.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR3.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR4.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR5.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR6.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR7.setBackgroundResource(R.drawable.yksjb_select1);
			  it=7;
//			  mSchedule.clear();
			  gainData() ;
				adapter.notifyDataSetChanged();
		  }
	  });
		
		mTv_1.setText(weeks[week]);
		mTv_2.setText(weeks[week + 1]);
		mTv_3.setText(weeks[week + 2]);
		mTv_4.setText(weeks[week + 3]);
		mTv_5.setText(weeks[week + 4]);
		mTv_6.setText(weeks[week+5]);
		mTv_7.setText(weeks[week+6]);
		
		Time time = new Time("GMT+8");
		time.setToNow(); // 获取系统时间
		Calendar ca = Calendar.getInstance();
		int days=ca.getActualMaximum(Calendar.DATE);
		int s =ca.get(Calendar.MONTH)+1;
		int year = time.year;
		int day = time.monthDay;
		String format = time.format("%m-%d");
		
		SimpleDateFormat sdf =new SimpleDateFormat("MM-dd");
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyy");
		if (days==day) {
			mTv_Date1.setText(format);
			if (s!=12) {
				s=s+1;
			}else {
				s=1;
			}
		}else {
			mTv_Date1.setText(format);
			
		}
		if (days==new Date(System.currentTimeMillis() + 1 * 24 * 60
				* 60 * 1000).getDate()) {
			
			format2 = sdf.format(new Date(System.currentTimeMillis() + 1 * 24 * 60
					* 60 * 1000));
		
			mTv_Date2.setText(format2);
			if (s!=12) {
				s=s+1;
			}else {
				s=1;
			}
			
		}else {
			format2 = sdf.format(new Date(System.currentTimeMillis() + 1 * 24 * 60
					* 60 * 1000));
			mTv_Date2.setText(format2);
			/*mTv_Date2
			.setText(s
					+ "."
					+ new Date(System.currentTimeMillis() + 1 * 24 * 60
							* 60 * 1000).getDate());*/

		}
		if (days==new Date(System.currentTimeMillis() + 2 * 24 * 60
								* 60 * 1000).getDate()) {
			
			format2 = sdf.format(new Date(System.currentTimeMillis() + 2 * 24 * 60
					* 60 * 1000));
			mTv_Date3.setText(format2);
			if (s!=12) {
				s=s+1;
			}else {
				s=1;
			}
		}else {
			format2 = sdf.format(new Date(System.currentTimeMillis() + 2 * 24 * 60
					* 60 * 1000));
			mTv_Date3.setText(format2);
		}
		
		if (days==new Date(System.currentTimeMillis() + 3 * 24 * 60
								* 60 * 1000).getDate()) {
			format2 = sdf.format(new Date(System.currentTimeMillis() + 3 * 24 * 60
					* 60 * 1000));
			mTv_Date4.setText(format2);
			if (s!=12) {
				s=s+1;
			}else {
				s=1;
			}
		}else {
			format2 = sdf.format(new Date(System.currentTimeMillis() + 3 * 24 * 60
					* 60 * 1000));
			mTv_Date4.setText(format2);
		}
		
		if (days== new Date(System.currentTimeMillis() + 4 * 24 * 60
								* 60 * 1000).getDate()) {
			format2 = sdf.format(new Date(System.currentTimeMillis() + 4 * 24 * 60
					* 60 * 1000));
			mTv_Date5.setText(format2);
			if (s!=12) {
				s=s+1;
			}else {
				s=1;
			}
		}else {
			format2 = sdf.format(new Date(System.currentTimeMillis() + 4 * 24 * 60
					* 60 * 1000));
			mTv_Date5.setText(format2);
		}
		
		
		
		if (days== new Date(System.currentTimeMillis() + 5 * 24 * 60
								* 60 * 1000).getDate()) {
			format2 = sdf.format(new Date(System.currentTimeMillis() + 5 * 24 * 60
					* 60 * 1000));
			mTv_Date6.setText(format2);
			if (s!=12) {
				s=s+1;
			}else {
				s=1;
			}
		}else {
			format2 = sdf.format(new Date(System.currentTimeMillis() + 5 * 24 * 60
					* 60 * 1000));
			mTv_Date6.setText(format2);
		}
		
		if (days== new Date(System.currentTimeMillis() + 6 * 24 * 60
								* 60 * 1000).getDate()) {
			
			format2 = sdf.format(new Date(System.currentTimeMillis() + 6 * 24 * 60
					* 60 * 1000));
			mTv_Date7.setText(format2);
			if (s!=12) {
				s=s+1;
			}else {
				s=1;
			}
		}else {
			format2 = sdf.format(new Date(System.currentTimeMillis() + 6 * 24 * 60
					* 60 * 1000));
			mTv_Date7.setText(format2);
		}
		
		
		
		
		mGridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
			}
		});
		
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				Calendar cal = Calendar.getInstance();// 当前日期
				int hour1 = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
				
				if(it==1){
					
					if(!(Integer.parseInt(listdate.get(arg2).hour)>hour1)){
						Utils.showToast(SelectLessonTimeActivity.this, "请提前一小时预约");
						return;
					}
				}
				
				//adapter.setSeclection(arg2);
				if(getIsClickable().get(arg2)){
					
					if(Integer.parseInt(hourable.getText().toString().trim())<1){
						if(!timeMap.get(it).contains(arg2)){
							Utils.showToast(SelectLessonTimeActivity.this, "订单内没有更多时长了,请约课后，重新下单");
							return;							
						}
					}
					
					ViewHolder vh =(ViewHolder) arg1.getTag();
					vh.cb1.toggle();
					getIsSelected().put(arg2, vh.cb1.isChecked());
					Utils.showToast(SelectLessonTimeActivity.this, arg2+":"+vh.cb1.isChecked());
					//adapter.notifyDataSetChanged();
					//获取初始化的时候，在timeMap中存放的，每天的list集合。该集合中存放的是，用户点击的小时时间段
					//ArrayList<Integer> list =timeMap.get(it);
					//将点击的某天的时间段，放置在集合中。
					//list.add(arg2);
					
					
					if(vh.cb1.isChecked()==true){
						checkNum--;
						timeMap.get(it).add(arg2);
						Collections.sort(timeMap.get(it));
						long calcuteTimeStamp = calcuteTimeStamp(it,Integer.parseInt(listdate.get(arg2).hour));
						//Utils.showToast(SelectLessonTimeActivity.this, calcuteTimeStamp+":"+mSchedule.get(arg2).hour);
					}else{
						checkNum++;
							
							for(int i=0;i<timeMap.get(it).size();i++){
								if(arg2==(Integer)timeMap.get(it).get(i)){
									timeMap.get(it).remove(i);
									break;
								}
							}
						
					}
					
					
					hourable.setText(checkNum+"");
				}
			}
		});
	}
	
	
	private int checkNum; // 记录选中的条目数量
	private TextView mTv_Time;
	private ImageView mIv_Time;
	private RelativeLayout layout;
	private String format2;
	private TextView tv_busy;
	public HashMap<Integer,Boolean> isClickable;
	
	public HashMap<Integer, Boolean> getIsClickable() {
		return isClickable;
	}
	public void setIsClickable(HashMap<Integer, Boolean> isClickable) {
		this.isClickable = isClickable;
	}


	private String urlString;
	private RequestParams params;
	private ListView mGridView;
	private String  t;
	
	class ViewHolder{
		CheckBox cb1;
	}
	// 时间适配器
		@SuppressLint("ResourceAsColor")
	class TimeAdapter extends BaseAdapter {

			Calendar cal = Calendar.getInstance();// 当前日期
			int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
			private int clickTemp = -1;
			private RelativeLayout rl_time_item;

			public TimeAdapter (){
				isSelected=new HashMap<Integer,Boolean>();
				isClickable=new HashMap<Integer,Boolean>();
				
				/*for(int i=0;i<mSchedule.size();i++){
					if(mSchedule.get(i).status.equals("0")){
						//表示第几个位置可以点，第几个位置不可以点击
						isClickable.put(i, true);
					}else{
						isClickable.put(i, false);
					}
							
				}*/
				
				for(int i=0;i<listdate.size();i++){
					//1忙   0不忙
					if("1".equals(listdate.get(i).status)){
						isClickable.put(i, false);
					}else{
						isClickable.put(i, true);
					}
				}
				
				
				for(Integer in:isClickable.keySet()){
					//如果某个位置可以点击，那么集合中的值就是true。
					if(isClickable.get(in)){
						//那么就将可以点击设置为false
						isSelected.put(in, false);
					}else{
						isSelected.put(in, false);
					}
				}
				
				for(Integer ina:isSelected.keySet()){
					System.out.println(ina+":"+isSelected.get(ina));
				}
							
			}
			
			
			@Override
			public int getCount() {
				if (listdate == null) {

					return 0;
				} else {
					return listdate.size();

				}
			}

			private void setSeclection(int position) {
				clickTemp = position;
			}

			@Override
			public Object getItem(int arg0) {
				return null;
			}

			@Override
			public long getItemId(int arg0) {
				return 0;
			}

			@Override
			public View getView(final int arg0, View arg1, ViewGroup arg2) {
				View view;
				ViewHolder vh;
				if(arg1==null){
					view = View.inflate(SelectLessonTimeActivity.this, R.layout.date_time_item1, null);
					vh=new ViewHolder();
					vh.cb1=(CheckBox) view.findViewById(R.id.cb);
					view.setTag(vh);
				}else{
					view=arg1;
					vh=(ViewHolder) view.getTag();
				}
				mTv_Time = (TextView) view.findViewById(R.id.Tv_time);
				rl_time_item = (RelativeLayout) view.findViewById(R.id.rl_time_item);
				if(arg0==11){
					mTv_Time.setText(listdate.get(arg0).time_tab+"-21:00");
				}else{					
					mTv_Time.setText(listdate.get(arg0).time_tab+"-"+listdate.get(arg0+1).time_tab);
				}
				tv_busy=(TextView) view.findViewById(R.id.tv_busy);
				vh.cb1.setChecked(getIsSelected().get(arg0));
				//通过取得timeMap中的list集合中存放的被点击的position，然后比较集合中的position是否和arg0相等，如果相等，就设置为已被点击。
				ArrayList<Integer> list=timeMap.get(it);
				for(Integer i:list){
					if(arg0==i){						
						vh.cb1.setChecked(true);
					}
				}
				
				
				
				/*if(arg0<4){
					mTv_Time.setTextColor(Color.parseColor("#cccccc"));
					tv_busy.setTextColor(Color.parseColor("#cccccc"));
				}
				if(arg0>3&&arg0<8){
					mTv_Time.setTextColor(Color.parseColor("#333333"));
					tv_busy.setTextColor(Color.parseColor("#333333"));
				}
				
				if(arg0>7){
					mTv_Time.setTextColor(Color.parseColor("#cccccc"));
					tv_busy.setTextColor(Color.parseColor("#cccccc"));
				}*/
				/**
				 * 前四行是上午，中间四行是下午，后面四行是晚上
				 */
				if (it == 1) {
					//hour表示当前时间,int 类型
					//if(Integer.parseInt(listdate.get(arg0).hour)>hour){//已超过当前时间
						//如果已超过当前时间，就无法预约，提示需提前三小时预约。
						//Utils.showToast(SelectLessonTimeActivity.this, "请提前三个小时预约");
						if (listdate.get(arg0).status .equals("1")) {
							tv_busy.setText("忙碌");
							mTv_Time.setTextColor(Color.parseColor("#cccccc"));
							tv_busy.setTextColor(Color.parseColor("#cccccc"));
							//adapter.notifyDataSetChanged();
						}else{
							tv_busy.setText("空闲");
							mTv_Time.setTextColor(Color.parseColor("#333333"));
							tv_busy.setTextColor(Color.parseColor("#333333"));
							//adapter.notifyDataSetChanged();
						}
						/**
						 * 如果超过当前时间显示灰色
						 */
						if(Integer.parseInt(listdate.get(arg0).hour)<hour||Integer.parseInt(listdate.get(arg0).hour)==hour){
							mTv_Time.setTextColor(Color.parseColor("#cccccc"));
							tv_busy.setTextColor(Color.parseColor("#cccccc"));
						}else{
							mTv_Time.setTextColor(Color.parseColor("#333333"));
							tv_busy.setTextColor(Color.parseColor("#333333"));
						}
						
					/*}else{
						*//**
						 * 在此处设置点击事件onitemclicklistener就无效了
						 *//*
						view.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								Utils.showToast(SelectLessonTimeActivity.this, "请提前1小时预约"+listdate.get(arg0).hour+"arg0:"+arg0);
							}
						});
						if (listdate.get(arg0).status .equals("1")) {
							tv_busy.setText("忙碌");
							//adapter.notifyDataSetChanged();
						}else{
							tv_busy.setText("空闲");
							//adapter.notifyDataSetChanged();
						}
					}*/
					adapter.notifyDataSetChanged();
					
				} else if (it == 2) {
					
						if (listdate.get(arg0).status .equals("1")) {
							tv_busy.setText("忙碌");
							mTv_Time.setTextColor(Color.parseColor("#cccccc"));
							tv_busy.setTextColor(Color.parseColor("#cccccc"));
							adapter.notifyDataSetChanged();
						}else{
							tv_busy.setText("空闲");
							mTv_Time.setTextColor(Color.parseColor("#333333"));
							tv_busy.setTextColor(Color.parseColor("#333333"));
							adapter.notifyDataSetChanged();
						}
					
					adapter.notifyDataSetChanged();
					
				} else if (it == 3) {
					if (listdate.get(arg0).status .equals( "0")) {
						tv_busy.setText("空闲");
						mTv_Time.setTextColor(Color.parseColor("#333333"));
						tv_busy.setTextColor(Color.parseColor("#333333"));
						adapter.notifyDataSetChanged();
					}else {
						tv_busy.setText("忙碌");
						mTv_Time.setTextColor(Color.parseColor("#cccccc"));
						tv_busy.setTextColor(Color.parseColor("#cccccc"));
						adapter.notifyDataSetChanged();
					}
					
				} else if (it == 4) {
					if (listdate.get(arg0).status.equals( "0")) {
						tv_busy.setText("空闲");
						mTv_Time.setTextColor(Color.parseColor("#333333"));
						tv_busy.setTextColor(Color.parseColor("#333333"));
						adapter.notifyDataSetChanged();
					}else {
						tv_busy.setText("忙碌");
						mTv_Time.setTextColor(Color.parseColor("#cccccc"));
						tv_busy.setTextColor(Color.parseColor("#cccccc"));
						adapter.notifyDataSetChanged();
					}
					
				} else if (it == 5) {
					if (listdate.get(arg0).status.equals( "0")) {
						tv_busy.setText("空闲");
						mTv_Time.setTextColor(Color.parseColor("#333333"));
						tv_busy.setTextColor(Color.parseColor("#333333"));
						adapter.notifyDataSetChanged();
					}else {
						tv_busy.setText("忙碌");
						mTv_Time.setTextColor(Color.parseColor("#cccccc"));
						tv_busy.setTextColor(Color.parseColor("#cccccc"));
						adapter.notifyDataSetChanged();
					}
					
				}else if (it == 6) {
					if (listdate.get(arg0).status.equals( "0")) {
						tv_busy.setText("空闲");
						mTv_Time.setTextColor(Color.parseColor("#333333"));
						tv_busy.setTextColor(Color.parseColor("#333333"));
						adapter.notifyDataSetChanged();
					}else {
						tv_busy.setText("忙碌");
						mTv_Time.setTextColor(Color.parseColor("#cccccc"));
						tv_busy.setTextColor(Color.parseColor("#cccccc"));
						adapter.notifyDataSetChanged();
					}
					
				}else if (it == 7) {
					if (listdate.get(arg0).status .equals( "0")) {	
						tv_busy.setText("空闲");
						mTv_Time.setTextColor(Color.parseColor("#333333"));
						tv_busy.setTextColor(Color.parseColor("#333333"));
						adapter.notifyDataSetChanged();
					}else {
						tv_busy.setText("忙碌");
						mTv_Time.setTextColor(Color.parseColor("#cccccc"));
						tv_busy.setTextColor(Color.parseColor("#cccccc"));
						adapter.notifyDataSetChanged();
					}
					
				}else{
					
				}

				return view;
			}

		}
}
