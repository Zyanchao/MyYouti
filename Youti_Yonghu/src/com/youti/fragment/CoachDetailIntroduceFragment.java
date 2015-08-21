package com.youti.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbLogUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.Constants;
import com.youti.appConfig.YoutiApplication;
import com.youti.chat.domain.User;
import com.youti.utils.ACache;
import com.youti.utils.HttpUtils;
import com.youti.utils.IntentJumpUtils;
import com.youti.utils.ScreenUtils;
import com.youti.utils.Utils;
import com.youti.view.CircleImageView1;
import com.youti.view.CustomProgressDialog;
import com.youti.view.HorizontalListView;
import com.youti.view.MGridView;
import com.youti.yonghu.activity.CommentActivity;
import com.youti.yonghu.activity.CourseDetailActivity;
import com.youti.yonghu.activity.LoginActivity;
import com.youti.yonghu.activity.OtherPersonCenterActivity;
import com.youti.yonghu.adapter.HorizntalPicListAdapter;
import com.youti.yonghu.adapter.OtherCourseAdapter;
import com.youti.yonghu.bean.CoachDetailBean;
import com.youti.yonghu.bean.CourseBean;
import com.youti.yonghu.bean.DateTimeBean;
import com.youti.yonghu.bean.InfoBean;
import com.youti.yonghu.bean.PicsBean;
import com.youti.yonghu.bean.UserEntity;

/**
 * 教练详情 简介
 * 
 * @author Administrator
 * 
 */
public class CoachDetailIntroduceFragment extends Fragment implements OnClickListener {

	/** ViewContainer组件 */
	// private ViewContainer mViewContainer;

	View mView;
	private ImageView mHeadImg;
	private ImageView contentControl1,contentControl2,contentControl3;
	private TextView mTvname,
					mTvage,
					mtTvaddress,
					mtvPrice,
					mTvcontent1,mTvcontent2,mTvcontent3;
	private boolean isShow = false;

	public ACache cache;
	//AbFileCache fileCache;
	//AbImageCache imageCahe;
	public ImageLoader imageLoader;
	public DisplayImageOptions options;
	HorizontalListView hlv1,hlv2;
	private List<CourseBean> mCourseList = new ArrayList<CourseBean>();
	ArrayList<PicsBean> photoList1=new ArrayList<PicsBean>();
	ArrayList<PicsBean> photoList2=new ArrayList<PicsBean>();
	ArrayList<ImageView> photo_list=new ArrayList<ImageView>();
	private List<DateTimeBean> mSchedule = new ArrayList<DateTimeBean>();
	private String id = "";
	HorizntalPicListAdapter hlvAdapter1,hlvAdapter2;
	private OtherCourseAdapter mAdapter;
	private ListView mListView;
	private CoachDetailBean coachDetailBean;
	//点赞相关
	MGridView mgv;
	LinearLayout ll_zan;
	ImageView zan_img;
	TextView zan,tvPinglun;
	MyAdapter myAdapter;
	YoutiApplication youtiApplication;
	String coach_id;
	LinearLayout ll_gallary_name,ll_gallary,layout_hlv_pic2;
	String image_url;
	private boolean isShow1 = false;
	private boolean isShow2 = false;
	private boolean isShow3 = false;
	boolean isLogin=YoutiApplication.getInstance().myPreference.getHasLogin();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	
	CustomProgressDialog dialog;
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = mView.inflate(getActivity(), R.layout.coach_detail_introduce,null);
		id = getActivity().getIntent().getStringExtra(Constants.KEY_ID);
		youtiApplication =(YoutiApplication)(getActivity().getApplication());
		image_url=YoutiApplication.getInstance().myPreference.getHeadImgPath();
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.user_lbk)// 正在加载
				.showImageForEmptyUri(R.drawable.user_lbk)// 空图片
				.showImageOnFail(R.drawable.user_lbk)// 错误图片
				.cacheInMemory(true)//设置 内存缓存
				.cacheOnDisk(true)//设置硬盘缓存
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		cache = ACache.get(getActivity());
		dialog = new CustomProgressDialog(getActivity(), R.string.laoding_tips,R.anim.frame2);
		dialog.show();
		initView();
		getDetail();
		showmView1();
		initListener();
		return mView;
	}
	
	public final String mPageName="CoachDetailIntroduceFragment";
	  @Override
			public void onPause() {
				// TODO Auto-generated method stub
				super.onPause();
				MobclickAgent.onPageEnd(mPageName);
			}

			@Override
			public void onResume() {
				// TODO Auto-generated method stub
				super.onResume();
				MobclickAgent.onPageStart(mPageName);
			}

	private void getDetail() {
			
			/**
			 * 无网络 获取缓存数据
			 */
			
			/**
			 * 请求数据 
			 */
			RequestParams params = new RequestParams();
			//假设教练id为1
			//params.put("user_id", youtiApplication.myPreference.getUserId());
			params.put("jd", YoutiApplication.getInstance().myPreference.getLocation_j());
			params.put("wd", YoutiApplication.getInstance().myPreference.getLocation_w());
			params.put("coach_id",id);
			params.put("user_id", youtiApplication.myPreference.getUserId());
			/*if (w < 720) {
				params.put("resolution", "480");
				;
			} else {
				params.put("resolution", w + "");
			}*/
			HttpUtils.post(Constants.COACH_DETAIL, params,
					new JsonHttpResponseHandler() {
						public void onStart() {
							dialog.show();
							super.onStart();
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

							try {
								//教练 详情
								dialog.dismiss();
								String state = response.getString("code");
								if (state.equals("1")) {
									com.alibaba.fastjson.JSONObject object = JSON.parseObject(response.toString());
									JSONObject jsonObject = object.getJSONObject("list"); 
									coachDetailBean = JSON.parseObject(jsonObject.toString(), CoachDetailBean.class); 
									Intent intent =new Intent();
									intent.setAction("com.example.yoti_geren.play");
									intent.putExtra("video_url", "http://112.126.72.250/ut_app"+coachDetailBean.video_url);
									intent.putExtra("de_img", "http://112.126.72.250/ut_app"+coachDetailBean.de_img);
									getActivity().sendBroadcast(intent);
									setDatas();
								} else {
								}
							} catch (Exception e) {
								AbLogUtil.d(getActivity(), e.toString());
							}
						};
					});
		
	}

	private void initView() {
		contentControl1 = (ImageView) mView.findViewById(R.id.contentShowControl1);
		mHeadImg = (ImageView) mView.findViewById(R.id.iv_head_img);
		mTvname = (TextView) mView.findViewById(R.id.coach_tv_name);
		mTvage = (TextView) mView.findViewById(R.id.coach_tv_age);
		mtvPrice = (TextView) mView.findViewById(R.id.coach_tv_price);
		mtTvaddress = (TextView) mView.findViewById(R.id.coach_tv_address);
		mTvcontent1 = (TextView) mView.findViewById(R.id.coach_tv_content1);
		mTvcontent2 = (TextView) mView.findViewById(R.id.coach_tv_content2);
		mTvcontent3 = (TextView) mView.findViewById(R.id.coach_tv_content3);
		contentControl1 = (ImageView) mView.findViewById(R.id.contentShowControl1);
		contentControl2 = (ImageView) mView.findViewById(R.id.contentShowControl2);
		contentControl3 = (ImageView) mView.findViewById(R.id.contentShowControl3);
		contentControl1.setFocusable(false);
		contentControl2.setFocusable(false);
		contentControl3.setFocusable(false);
		//点赞相关
		ll_zan=(LinearLayout) mView.findViewById(R.id.ll_zan);
		zan_img=(ImageView) mView.findViewById(R.id.zan_img);
		zan=(TextView) mView.findViewById(R.id.zan);
		mgv=(MGridView) mView.findViewById(R.id.gv);
		tvPinglun = (TextView) mView.findViewById(R.id.tv_pinglun);
		
		hlv1 = (HorizontalListView) mView.findViewById(R.id.hlv_pic1);
		hlv2 = (HorizontalListView) mView.findViewById(R.id.hlv_pic2);
		mListView = (ListView) mView.findViewById(R.id.lv_ohter_course);
		mHeadImg.setFocusable(false);
		mTvname.setFocusable(false);
		mTvage.setFocusable(false);
		mtTvaddress.setFocusable(false);
		mTvcontent1.setFocusable(false);
		mTvcontent2.setFocusable(false);
		mTvcontent3.setFocusable(false);
		hlv1.setFocusable(false);
		hlv2.setFocusable(false);
		mListView.setFocusable(false);
		
		ll_gallary_name=(LinearLayout) mView.findViewById(R.id.ll_gallary_name);
		ll_gallary=(LinearLayout) mView.findViewById(R.id.ll_gallary);
		layout_hlv_pic2=(LinearLayout) mView.findViewById(R.id.layout_hlv_pic2);
		//屏蔽相册
		ll_gallary_name.setVisibility(View.GONE);
		ll_gallary.setVisibility(View.GONE);
		hlv2.setVisibility(View.GONE);
		layout_hlv_pic2.setVisibility(View.GONE);
	}
	
	
	private void setDatas() {
		mTvname.setFocusable(false);
		mTvname.setText(coachDetailBean.getCoach_name());
		mTvage.setText(coachDetailBean.getTeach_age()+"年");
		//Toast.makeText(getActivity(), "111", 0).show();
		mtvPrice.setText(coachDetailBean.getPrice()+"元");
		mtTvaddress.setText(coachDetailBean.server);
		mTvcontent1.setText(coachDetailBean.getContent_dec());
		if(coachDetailBean.coach_exp!=null){
			
			int size = coachDetailBean.coach_exp.length;
			//setAcatr
			User user = new User();
			user.setAvatar(Constants.PIC_CODE+coachDetailBean.getHead_img());
			user.setUsername(coachDetailBean.getCoach_name());
			YoutiApplication.getInstance().setContact(user);
			imageLoader.displayImage(Constants.PIC_CODE+coachDetailBean.getHead_img(), mHeadImg,options);
			String str = "";
			for(int i = 0;i<size;i++){
				str+= coachDetailBean.coach_exp[0]+"\n";
			}
			mTvcontent2.setText(str.replace("\\n", "\n"));
		}
		mTvcontent3.setText(coachDetailBean.getCoach_result());
		//Toast.makeText(getActivity(), "333", 1).show();
		//photoList1 = (ArrayList<PicsBean>) coachDetailBean.getCoach_photo();
		//if(photoList1!=null&photoList1.size()>0){
			//hlvAdapter1=new HorizntalPicListAdapter(getActivity(),photoList1);
			//hlv1.setAdapter(hlvAdapter1);
		//}
		
		//photoList2 = (ArrayList<PicsBean>) coachDetailBean.getTeach_content_photo();
		//if(photoList2!=null&&photoList2.size()>0){
			//hlvAdapter2=new HorizntalPicListAdapter(getActivity(),photoList2);
			//hlv2.setAdapter(hlvAdapter2);
		//}
		//Toast.makeText(getActivity(), "555", 1).show();
		mCourseList = coachDetailBean.getOther_course();
		if(mCourseList!=null&&mCourseList.size()>0){
			mAdapter = new OtherCourseAdapter(getActivity(), mCourseList);
			mListView.setAdapter(mAdapter);
			mAdapter.notifyDataSetInvalidated();
			ScreenUtils.setListViewHeightBasedOnChildren(mListView);
			mListView.setOnItemClickListener(new MyOnItemListener());
		}
		
		if(coachDetailBean!=null){
			zan.setText(coachDetailBean.getPraise_num());
			tvPinglun.setText(coachDetailBean.getComment_num());
			if(coachDetailBean.getPraise().equals("1")){
				//登录用户点赞，则设置为红心点赞图标
				zan_img.setBackgroundResource(R.drawable.sp_heart_h);
			}else{
				zan_img.setBackgroundResource(R.drawable.sp_heart);
			}
			
			if(coachDetailBean.getUser_heads()!=null){
				mgv.setAdapter(new MyAdapter(coachDetailBean.getUser_heads()));
			}
		}
	}
	public class MyAdapter extends android.widget.BaseAdapter{
		List<UserEntity> list;
		public MyAdapter(List<UserEntity> list){
			this.list=list;
		}
		@Override
		public int getCount() {
			if(list.size()<7){
				return list.size();				
				
			}else{
				return 6;
			}
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v =View.inflate(getActivity(), R.layout.item_image1, null);
			CircleImageView1 iv =(CircleImageView1) v.findViewById(R.id.iv);
			if(list.get(position).head_img.startsWith("http:")){
				imageLoader.displayImage(list.get(position).head_img, iv);
			}else{
				imageLoader.displayImage("http://112.126.72.250/ut_app"+list.get(position).head_img, iv,options);
			}
			
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent =new Intent(getActivity(),OtherPersonCenterActivity.class);
					intent.putExtra("user_id", list.get(position).user_id);
					getActivity().startActivity(intent);
					
				}
			});
			return v;					
		}
	}	
	private void initListener() {
		ll_zan.setOnClickListener(this);
		contentControl1.setOnClickListener(this);
		contentControl2.setOnClickListener(this);
		contentControl3.setOnClickListener(this);
	}

	/**
	 * 
	* @ClassName: MyOnItemListener 
	* @Description: 處理 其他課程點擊事件 ) 
	* @author zychao 
	* @date 2015-6-9 上午11:10:19 
	*
	 */
	class MyOnItemListener implements OnItemClickListener{

		@SuppressLint("NewApi")
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String cid = "";
			TextView tv = (TextView) view.findViewById(R.id.tv_other_course_title);
			cid = (String) tv.getTag();
			Bundle bundle = new Bundle();
			bundle.putString(Constants.KEY_ID, cid);
			IntentJumpUtils.nextActivity(CourseDetailActivity.class, getActivity(),bundle);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.contentShowControl1:
			isShow1(mTvcontent1, contentControl1);
			break;
		case R.id.contentShowControl2:
			isShow2(mTvcontent2, contentControl2);
			break;
		case R.id.contentShowControl3:
			isShow3(mTvcontent3, contentControl3);
			break;
		case R.id.ll_zan:
			if(!YoutiApplication.getInstance().myPreference.getHasLogin()){
				Intent intent =new Intent(getActivity(),LoginActivity.class);
				getActivity().startActivity(intent);
				return;
			}
			clickPraise();
			break;
			
		case R.id.ll_pinglun:
			Intent intent = new Intent(getActivity(), CommentActivity.class);
			intent.putExtra(Constants.KEY_ID, id);
			intent.putExtra("code", Constants.REQUEST_CODE_COACH);
			startActivityForResult(intent, Constants.REQUEST_CODE_COACH);
			
			break;
		default:
			break;
		}
	}

	private void clickPraise() {
		//如果点赞头像集合不为空
		//需要添加一个是否点赞的字段 
		//判断点赞集合是否为null
		if(coachDetailBean.getUser_heads()!=null){
			if(coachDetailBean.getPraise().equals("1")){
				//表示已经点赞 点击 变为取消点赞
				zan_img.setBackgroundResource(R.drawable.sp_heart);
			//	Utils.showToast(getActivity(), youtiApplication.myPreference.getUserId()+"&&&&"+courseDetailBean.getUser_heads().get(0));
				
				for(int i=0;i<coachDetailBean.getUser_heads().size();i++){
					if(youtiApplication.myPreference.getUserId().equals(coachDetailBean.getUser_heads().get(i).getUser_id())){
						coachDetailBean.getUser_heads().remove(i);
					//	Utils.showToast(getActivity(), youtiApplication.myPreference.getUserId()+"aaa"+i);
						break;
					}
				}
				//重新设置点赞人数
				zan.setText((coachDetailBean.getUser_heads().size())+"");
					myAdapter=new MyAdapter(coachDetailBean.getUser_heads());
					mgv.setAdapter(myAdapter);					
					coachDetailBean.setPraise("0");
				
			}else{
				//未点赞点击变为点赞
				zan_img.setBackgroundResource(R.drawable.sp_heart_h);
				coachDetailBean.setPraise("1");
				UserEntity user =new UserEntity();
				
				
				if(isLogin){
					if(TextUtils.isEmpty(image_url)){
						user.setHead_img("");
					}else{
						if(image_url.startsWith(Constants.PIC_CODE)){
							
							user.setHead_img(image_url.substring(28, image_url.length()));
						}else{
							user.setHead_img(image_url);
						}
					}
				}else{
					user.setHead_img("");
				}
				
				user.setUser_id(youtiApplication.myPreference.getUserId());
				coachDetailBean.getUser_heads().add(0,user);
				//重新设置点赞人数
				zan.setText((coachDetailBean.getUser_heads().size())+"");
				
				
				myAdapter=new MyAdapter(coachDetailBean.getUser_heads());
					mgv.setAdapter(myAdapter);
				
				
			}
		}else{
			//该用户为第一个点赞
			List<UserEntity> abc=new ArrayList<UserEntity>();
			//未点赞点击变为点赞
			zan_img.setBackgroundResource(R.drawable.sp_heart_h);
			coachDetailBean.setPraise("1");
			UserEntity user =new UserEntity();
			if(isLogin){
				if(TextUtils.isEmpty(image_url)){
					user.setHead_img("");
				}else{
					
					user.setHead_img(image_url.substring(28, image_url.length()));
				}
			}else{
				user.setHead_img("");
			}
			user.setUser_id(youtiApplication.myPreference.getUserId());
			abc.add(user);
			coachDetailBean.setUser_heads(abc);
			zan.setText(1+"");
			myAdapter=new MyAdapter(abc);
			mgv.setAdapter(myAdapter);
		}
		
		requestPraise(youtiApplication.myPreference.getUserId(),id);
		//Utils.showToast(getActivity(), "点赞呀");
	}
	/**
	 * 发送点赞请求到服务器
	 * @param user_id
	 * @param course_id
	 */
	private void requestPraise(String user_id,String coach_id) {
		//发送请求到服务器		
		RequestParams params =new RequestParams();
		params.put("user_id", user_id);
		params.put("coach_id", coach_id);
		HttpUtils.post(Constants.COACH_LIST_PRAISE, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson =new Gson();
				InfoBean fromJson = gson.fromJson(arg2, InfoBean.class);
				Message msg =Message.obtain();
				
				if(fromJson.code.equals("1")){
					//点赞成功
					Utils.showToast(getActivity(), "点赞成功");
				}else if(fromJson.code.equals("0")){
					//点赞取消成功
					Utils.showToast(getActivity(), "取消点赞");
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Utils.showToast(getActivity(), "连接网络异常");
			}
		});
	}
	int it = 1;
	private String yue;
	private String days;
	final TimeAdapter adapter = new TimeAdapter();
	private void gainData() {

		Time time = new Time("GMT+8");
		time.setToNow(); // 获取系统时间
		int year = time.year;
		int month = time.month;
		
		Calendar ca = Calendar.getInstance();
		 int s =ca.get(Calendar.MONTH)+1;
		if (it == 1) {
			yue = s+"";
			int day = time.monthDay;
			if (day < 10) {
				days = "0" + day;
			} else {
				days = "" + day;
			}
		} else if (it == 2) {
			String  ss = mTv_Date2.getText().toString();
			int index=ss.indexOf(".");
			yue = ss.substring(0,index);
			int day = new Date(System.currentTimeMillis() + 1 * 24 * 60 * 60
					* 1000).getDate();
			if (day < 10) {
				days = "0" + day;
			} else {
				days = "" + day;
			}

		} else if (it == 3) {
			String  ss = mTv_Date3.getText().toString();
			int index=ss.indexOf(".");
			yue = ss.substring(0,index);
			int day = new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60
					* 1000).getDate();
			if (day < 10) {
				days = "0" + day;
			} else {
				days = "" + day;
			}
		} else if (it == 4) {
			String  ss = mTv_Date4.getText().toString();
			int index=ss.indexOf(".");
			yue = ss.substring(0,index);
			int day = new Date(System.currentTimeMillis() + 3 * 24 * 60 * 60
					* 1000).getDate();
			if (day < 10) {
				days = "0" + day;
			} else {
				days = "" + day;
			}
		} else if (it == 5) {
			String  ss = mTv_Date5.getText().toString();
			int index=ss.indexOf(".");
			yue = ss.substring(0,index);
			int day = new Date(System.currentTimeMillis() + 4 * 24 * 60 * 60
					* 1000).getDate();
			if (day < 10) {
				days = "0" + day;
			} else {
				days = "" + day;
			}
		}

		String urlString = Constants.GET_DATA;
		RequestParams params = new RequestParams(); // 绑定参数
		if (Integer.parseInt(yue)>=10) {
			
			params.put("time", year + "" +  yue  + days);
		}else {
			params.put("time", year + "0" +  yue  + days);
		}
	
		params.put("coach_id",id);
		HttpUtils.post(urlString, params, new JsonHttpResponseHandler() {
			public void onStart() {
				super.onStart();
				System.out.println("onStart");
			}

			public void onFailure(int statusCode,
					org.apache.http.Header[] headers,
					java.lang.Throwable throwable,
					org.json.JSONObject errorResponse) {
				
				System.out.println("onFailure");
			};

			public void onFinish() {
				
				System.out.println("onFinish");
			};

			public void onSuccess(int statusCode,
					org.apache.http.Header[] headers,
					org.json.JSONObject response) {
				System.out.println("onSuccess");

				 try {
						com.alibaba.fastjson.JSONObject object = JSON
								.parseObject(response.toString());
						com.alibaba.fastjson.JSONArray jsonArray = object
								.getJSONArray("list");
						  
						if (jsonArray != null) {
							mSchedule = JSON.parseArray(jsonArray.toString(),
									DateTimeBean.class);
							adapter.notifyDataSetChanged();
						}
						
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		});
	}
private void showmView1() {
		
		String[] weeks = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六",
				"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Date date = new Date();
		int week = date.getDay();
		final RelativeLayout mR1 = (RelativeLayout) mView.findViewById(R.id.rl_1);
		final	RelativeLayout mR2 = (RelativeLayout) mView.findViewById(R.id.rl_2);
		final	RelativeLayout mR3 = (RelativeLayout) mView.findViewById(R.id.rl_3);
		final	RelativeLayout mR4 = (RelativeLayout) mView.findViewById(R.id.rl_4);
		final	RelativeLayout mR5 = (RelativeLayout) mView.findViewById(R.id.rl_5);
		TextView mTv_1 = (TextView) mView.findViewById(R.id.Tv_1);
		TextView mTv_2 = (TextView) mView.findViewById(R.id.Tv_2);
		TextView mTv_3 = (TextView) mView.findViewById(R.id.Tv_3);
		TextView mTv_4 = (TextView) mView.findViewById(R.id.Tv_4);
		TextView mTv_5 = (TextView) mView.findViewById(R.id.Tv_5);
		mTv_Date1 = (TextView) mView.findViewById(R.id.tv_date1);
		mTv_Date2 = (TextView) mView.findViewById(R.id.tv_date2);
		mTv_Date3 = (TextView) mView.findViewById(R.id.tv_date3);
		mTv_Date4 = (TextView) mView.findViewById(R.id.tv_date4);
		mTv_Date5 = (TextView) mView.findViewById(R.id.tv_date5);
		//ImageView mImg = (ImageView) mView.findViewById(R.id.im_de);
		GridView mGridView = (GridView) mView.findViewById(R.id.grid_view);
		mGridView.setFocusable(false);
		mGridView.setAdapter(adapter);

		mR1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mR1.setBackgroundResource(R.drawable.jlxq_sjb1);
				mR2.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR3.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR4.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR5.setBackgroundResource(R.drawable.jlxq_sjb2);
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
			mR2.setBackgroundResource(R.drawable.jlxq_sjb1);
			mR3.setBackgroundResource(R.drawable.jlxq_sjb2);
			mR4.setBackgroundResource(R.drawable.jlxq_sjb2);
			mR5.setBackgroundResource(R.drawable.jlxq_sjb2);
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
				mR3.setBackgroundResource(R.drawable.jlxq_sjb1);
				mR4.setBackgroundResource(R.drawable.jlxq_sjb2);
				mR5.setBackgroundResource(R.drawable.jlxq_sjb2);
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
				mR4.setBackgroundResource(R.drawable.jlxq_sjb1);
				mR5.setBackgroundResource(R.drawable.jlxq_sjb2);
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
				mR5.setBackgroundResource(R.drawable.jlxq_sjb1);
			  it=5;
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
		Time time = new Time("GMT+8");
		time.setToNow(); // 获取系统时间
		Calendar ca = Calendar.getInstance();
		int days=ca.getActualMaximum(Calendar.DATE);
		 int s =ca.get(Calendar.MONTH)+1;
		int year = time.year;
		int day = time.monthDay;
		if (days==day) {
			mTv_Date1.setText(s + "." + day);
			if (s!=12) {
				s=s+1;
			}else {
				s=1;
			}
		}else {
			mTv_Date1.setText(s + "." + day);
			
		}
		if (days==new Date(System.currentTimeMillis() + 1 * 24 * 60
				* 60 * 1000).getDate()) {
			
			mTv_Date2
			.setText(s
					+ "."
					+ new Date(System.currentTimeMillis() + 1 * 24 * 60
							* 60 * 1000).getDate());
			if (s!=12) {
				s=s+1;
			}else {
				s=1;
			}
			
		}else {
			mTv_Date2
			.setText(s
					+ "."
					+ new Date(System.currentTimeMillis() + 1 * 24 * 60
							* 60 * 1000).getDate());

		}
		if (days==new Date(System.currentTimeMillis() + 2 * 24 * 60
								* 60 * 1000).getDate()) {
			mTv_Date3
			.setText(s
					+ "."
					+ new Date(System.currentTimeMillis() + 2 * 24 * 60
							* 60 * 1000).getDate());
			if (s!=12) {
				s=s+1;
			}else {
				s=1;
			}
		}else {
			mTv_Date3
			.setText(s
					+ "."
					+ new Date(System.currentTimeMillis() + 2 * 24 * 60
							* 60 * 1000).getDate());
		}
		
		if (days==new Date(System.currentTimeMillis() + 3 * 24 * 60
								* 60 * 1000).getDate()) {
			mTv_Date4
			.setText(s
					+ "."
					+ new Date(System.currentTimeMillis() + 3 * 24 * 60
							* 60 * 1000).getDate());
			if (s!=12) {
				s=s+1;
			}else {
				s=1;
			}
		}else {
			mTv_Date4
			.setText(s
					+ "."
					+ new Date(System.currentTimeMillis() + 3 * 24 * 60
							* 60 * 1000).getDate());
		}
		
		if (days== new Date(System.currentTimeMillis() + 4 * 24 * 60
								* 60 * 1000).getDate()) {
			mTv_Date5
			.setText(s
					+ "."
					+ new Date(System.currentTimeMillis() + 4 * 24 * 60
							* 60 * 1000).getDate());
			if (s!=12) {
				s=s+1;
			}else {
				s=1;
			}
		}else {
			mTv_Date5
			.setText(s
					+ "."
					+ new Date(System.currentTimeMillis() + 4 * 24 * 60
							* 60 * 1000).getDate());
		}

		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				adapter.setSeclection(arg2);
				adapter.notifyDataSetChanged();

				// mTv_Time = (TextView) arg1.findViewById(R.id.Tv_time);
				// Toast.makeText(getApplicationContext(),
				// mTv_Time.getText().toString(), 0).show();
				// layout = (LinearLayout) arg1.findViewById(R.id.Ll_itme);
				//
				// Thread thread = new Thread(new Runnable() {
				//
				// @Override
				// public void run() {
				// Message msg = mHandler.obtainMessage();
				// msg.what = 1;
				// msg.sendToTarget();
				// }});
				// thread.start();
			}
		});
		/*mImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			}
		});*/
		// TextView bt_cancle = (TextView) mView.findViewById(R.id.bt_cancle);
		// TextView bt_comfirm = (TextView)
		// mView.findViewById(R.id.bt_comfirm);
		// bt_cancle.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// mView.dismiss();
		//
		// }
		// });
		// bt_comfirm.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// mView.dismiss();
		//
		// }
		// });
	}
	
	private TextView mTv_Time;
	private ImageView mIv_Time;
	private RelativeLayout layout;
	private View view;
	private TextView mTv_Date1;
	private TextView mTv_Date2;
	private TextView mTv_Date3;
	private TextView mTv_Date4;
	private TextView mTv_Date5;
	// 时间适配器
		@SuppressLint("ResourceAsColor")
		class TimeAdapter extends BaseAdapter {

			Calendar cal = Calendar.getInstance();// 当前日期
			int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
			private int clickTemp = -1;

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				if (mSchedule == null) {

					return 0;
				} else {
					return mSchedule.size();

				}
			}

			private void setSeclection(int position) {
				clickTemp = position;
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				view = View
						.inflate(getActivity(), R.layout.date_time_item, null);
				mTv_Time = (TextView) view.findViewById(R.id.Tv_time);
				mIv_Time = (ImageView) view.findViewById(R.id.iv_time);
				//mTv_Time = (TextView) view.findViewById(R.id.Tv_condition);
				mTv_Time.setText(mSchedule.get(arg0).getTime_tab());
				layout = (RelativeLayout) view.findViewById(R.id.rl_time_item);
				
				String tt = mSchedule.get(arg0).getHour();
				int hourNum = Integer.parseInt(tt) ;
				
				if (it == 1) {
					
					if(hourNum<hour){//已超过当前时间
						mTv_Time.setTextColor(getActivity().getResources().getColor(R.color.text_font_unable));
						mIv_Time.setBackgroundResource(R.drawable.jlxq_sjb4);
						if (mSchedule.get(arg0).getStatus() .equals( "1")) {
							mTv_Time.setText("忙");
							adapter.notifyDataSetChanged();
						}
					}else{
						mTv_Time.setTextColor(getActivity().getResources().getColor(R.color.text_color_select));
						mIv_Time.setBackgroundResource(R.drawable.jlxq_sjb3);
						if (mSchedule.get(arg0).getStatus() .equals( "0")) {
							mTv_Time.setText("忙");
							adapter.notifyDataSetChanged();
						}
					}
					adapter.notifyDataSetChanged();
					
				} else if (it == 2) {
					if(hourNum<hour){//已超过当前时间
						mTv_Time.setTextColor(getActivity().getResources().getColor(R.color.text_font_unable));
						mIv_Time.setBackgroundResource(R.drawable.jlxq_sjb4);
						if (mSchedule.get(arg0).getStatus() .equals( "1")) {
							mTv_Time.setText("忙");
							adapter.notifyDataSetChanged();
						}
					}else{
						mTv_Time.setTextColor(getActivity().getResources().getColor(R.color.text_color_select));
						mIv_Time.setBackgroundResource(R.drawable.jlxq_sjb3);
						if (mSchedule.get(arg0).getStatus() .equals( "0")) {
							mTv_Time.setText("忙");
							adapter.notifyDataSetChanged();
						}
					}
					adapter.notifyDataSetChanged();
					
				} else if (it == 3) {
					mTv_Time.setTextColor(R.color.text_color_select);
					mIv_Time.setBackgroundResource(R.drawable.jlxq_sjb3);
//					adapter.notifyDataSetChanged();
					if (mSchedule.get(arg0).getStatus() .equals( "0")) {
						mTv_Time.setTextColor(R.color.text_color_select);
						mIv_Time.setBackgroundResource(R.drawable.jlxq_sjb3);
						adapter.notifyDataSetChanged();
					}else {
						mTv_Time.setText("忙");
						//mTv_Time.setBackgroundResource(R.color.text_font_unable);
						mIv_Time.setBackgroundResource(R.drawable.jlxq_sjb4);
						adapter.notifyDataSetChanged();
					}
					
				} else if (it == 4) {
					mTv_Time.setTextColor(R.color.text_color_select);
					mIv_Time.setBackgroundResource(R.drawable.jlxq_sjb3);
//					adapter.notifyDataSetChanged();
					if (mSchedule.get(arg0).getStatus() .equals( "0")) {
						
						mTv_Time.setTextColor(R.color.text_color_select);
						mIv_Time.setBackgroundResource(R.drawable.jlxq_sjb3);
						adapter.notifyDataSetChanged();
					}else {
						mTv_Time.setText("忙");
						mTv_Time.setBackgroundResource(R.color.text_font_unable);
						mIv_Time.setBackgroundResource(R.drawable.jlxq_sjb4);
						adapter.notifyDataSetChanged();
					}
					
				} else if (it == 5) {
					mTv_Time.setTextColor(R.color.text_color_select);
					mIv_Time.setBackgroundResource(R.drawable.jlxq_sjb3);
//					adapter.notifyDataSetChanged();
					if (mSchedule.get(arg0).getStatus() .equals( "0")) {
						
						mTv_Time.setTextColor(R.color.text_color_select);
						mIv_Time.setBackgroundResource(R.drawable.jlxq_sjb3);
						adapter.notifyDataSetChanged();
					}else {
						mTv_Time.setText("忙");
						mTv_Time.setBackgroundResource(R.color.text_font_unable);
						mIv_Time.setBackgroundResource(R.drawable.jlxq_sjb4);
						adapter.notifyDataSetChanged();
					}
				}

				// if (mTv_Time.getText().toString().equals("忙")) {
				// layout.setBackgroundResource(R.drawable.frameh);
				// }

				// if (clickTemp == arg0) {
				// if (is == false) {
				// layout.setBackgroundResource(R.drawable.frame);
				// is=true;
				// } else {
				// layout.setBackgroundResource(R.drawable.frameh);
				// is=false;
				// }

				//
				// } else {
				// }

				return view;
			}

		}
		private void isShow1(TextView tv,ImageView iv){
			if (!isShow1) {
				isShow1 = true;
				tv.setSingleLine(false);
				tv.setEllipsize(null);
				iv.setBackgroundResource(R.drawable.spxq_arrow_h);
			} else {
				isShow1 = false;
				tv.setSingleLine(true);
				tv.setEllipsize(TruncateAt.END);
				iv.setBackgroundResource(R.drawable.spxq_arrow);
			}
		}
		private void isShow2(TextView tv,ImageView iv){
			if (!isShow2) {
				isShow2 = true;
				tv.setSingleLine(false);
				tv.setEllipsize(null);
				iv.setBackgroundResource(R.drawable.spxq_arrow_h);
			} else {
				isShow2 = false;
				tv.setSingleLine(true);
				tv.setEllipsize(TruncateAt.END);
				iv.setBackgroundResource(R.drawable.spxq_arrow);
			}
		}
		private void isShow3(TextView tv,ImageView iv){
			if (!isShow3) {
				isShow3 = true;
				tv.setSingleLine(false);
				tv.setEllipsize(null);
				iv.setBackgroundResource(R.drawable.spxq_arrow_h);
			} else {
				isShow3 = false;
				tv.setSingleLine(true);
				tv.setEllipsize(TruncateAt.END);
				iv.setBackgroundResource(R.drawable.spxq_arrow);
			}
		}
	
}
