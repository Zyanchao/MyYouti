package com.youti.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

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
import com.youti.utils.ACache;
import com.youti.utils.HttpUtils;
import com.youti.utils.IntentJumpUtils;
import com.youti.utils.ScreenUtils;
import com.youti.utils.Utils;
import com.youti.view.CircleImageView1;
import com.youti.view.CustomProgressDialog;
import com.youti.view.HorizontalListView;
import com.youti.view.MGridView;
import com.youti.view.TitleBar;
import com.youti.yonghu.activity.ClubDetailActivity;
import com.youti.yonghu.activity.CommentActivity;
import com.youti.yonghu.activity.CourseDetailActivity;
import com.youti.yonghu.activity.LoginActivity;
import com.youti.yonghu.activity.OtherPersonCenterActivity;
import com.youti.yonghu.adapter.CommentAdapter;
import com.youti.yonghu.adapter.HorizntalPicListAdapter;
import com.youti.yonghu.adapter.OtherCourseAdapter;
import com.youti.yonghu.adapter.PraisePicListAdapter;
import com.youti.yonghu.bean.Comment;
import com.youti.yonghu.bean.CommentBean;
import com.youti.yonghu.bean.CourseBean;
import com.youti.yonghu.bean.CourseDetailBean;
import com.youti.yonghu.bean.InfoBean;
import com.youti.yonghu.bean.PicsBean;
import com.youti.yonghu.bean.UserEntity;

/**
 *  课程详情 -教练介绍
 * @author zyc
 */
public class CourseDetailJsFragment extends Fragment implements OnClickListener {

	View mView;
	ListView mCommentListView;
	TextView  moreTip,tvAgreePeople,peopleNumber,tvPlan,tvPinglun,price,startTime,address,carWay,tips,zan;
	private boolean isShow1 = false;
	private boolean isShow2 = false;
	private LinearLayout ll_zan,ll_pinglun;
	private RadioGroup rg;
	private ImageView iv_pinglun,zan_img;
	private Button btOtherCourse;
	RadioButton rbAll, rb_bad,rb_middle,rb_great;
	ArrayList<String> list2 =new ArrayList<String>();
	public ACache cache;
	public ImageLoader imageLoader;
	public DisplayImageOptions options;
	HorizontalListView hlv1,hlv2;
	private List<CourseBean> mCourseList = new ArrayList<CourseBean>();
	private List<UserEntity> mUser_headsList=new ArrayList<UserEntity>();
	ArrayList<PicsBean> photoList1=new ArrayList<PicsBean>();
	ArrayList<PicsBean> photoList2=new ArrayList<PicsBean>();
	ArrayList<ImageView> photo_list=new ArrayList<ImageView>();
	private String id = "";
	HorizntalPicListAdapter hlvAdapter1,hlvAdapter2;
	private OtherCourseAdapter mAdapter;
	private ListView mOhterListView;
	private CourseDetailBean courseDetailBean;
	CommentBean commentBean;
	List<Comment> commentList= new ArrayList<Comment>();
	List<UserEntity> userList =new ArrayList<UserEntity>();
	PraisePicListAdapter praiseAdapter;
	CommentAdapter commentAdapter;
	private Handler mHander;
	MGridView mgv;
	MyAdapter mgvAdapter;
	YoutiApplication youtiApplication;
	String course_id;
	CustomProgressDialog dialog;
	boolean isLogin ;
	private Context mContext;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	public final String mPageName ="CourseDetailJsFragment";
	private ImageView contentControl1;
	private ImageView contentControl2;
	private String culb_id;
	  @Override
			public void onPause() {
				// TODO Auto-generated method stub
				super.onPause();
				MobclickAgent.onPageEnd( mPageName );
			}

			@Override
			public void onResume() {
				// TODO Auto-generated method stub
				super.onResume();
				MobclickAgent.onPageStart( mPageName );	
			}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = mView.inflate(getActivity(), R.layout.course_detail_introduce,null);
		mContext = getActivity();
		id = getActivity().getIntent().getStringExtra(Constants.KEY_ID);
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
		youtiApplication = (YoutiApplication)(getActivity().getApplication());
		dialog = new CustomProgressDialog(getActivity(), R.string.laoding_tips,R.anim.frame2);
		dialog.show();
		initView();
		initListener();
		initData();
		return mView;
	}
	
	

	private void initData() {
		getCourseDetail();
		getComment("4");
	}

	
	private void getCourseDetail(){
		/**
		 * 无网络 获取缓存数据
		 */
		
		/**
		 * 请求数据 
		 */
		/**
		 * 假设都是course_id=1
		 */
		RequestParams params = new RequestParams();
		params.put("user_id", youtiApplication.myPreference.getUserId());
		params.put("course_id", id);
		/*if (w < 720) {
			params.put("resolution", "480");
			;
		} else {
			params.put("resolution", w + "");
		}*/
		HttpUtils.post(Constants.COURSE_DETAIL, params,
				new JsonHttpResponseHandler() {
					

					public void onStart() {
						super.onStart();
					}

					public void onFailure(int statusCode,
							org.apache.http.Header[] headers,
							java.lang.Throwable throwable,
							org.json.JSONObject errorResponse) {
						Utils.showToast(mContext, "网络错误，加载失败");
					};

					public void onFinish() {
						dialog.dismiss();
					};

					public void onSuccess(int statusCode,
							org.apache.http.Header[] headers,
							org.json.JSONObject response) {
						dialog.dismiss();

						try {
							//课程 详情
							String state = response.getString("code");
							if (state.equals("1")) {
								com.alibaba.fastjson.JSONObject object = JSON
										.parseObject(response.toString());
								JSONObject jsonObject = object.getJSONObject("list"); 
								
								courseDetailBean = JSON.parseObject(jsonObject.toString(), CourseDetailBean.class); 
								setDetailDatas();
							} else {
							}
						} catch (Exception e) {
							AbLogUtil.d(getActivity(), e.toString());
						}
					};
				});
	}
	
	private void getComment(String status) {
		/**
		 * 无网络 获取缓存数据
		 */
		
		/**
		 * 请求数据 
		 */
		RequestParams params = new RequestParams();
		params.put("course_id", id);
		params.put("status", status);
		HttpUtils.post(Constants.COURSE_DETAIL_COMMENT, params,
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
					};

					public void onSuccess(int statusCode,
							org.apache.http.Header[] headers,
							org.json.JSONObject response) {

						try {
							//课程 评论 详情
							String state = response.getString("code");
							if (state.equals("1")) {
								com.alibaba.fastjson.JSONObject object = JSON
										.parseObject(response.toString());
								
								com.alibaba.fastjson.JSONArray jsonArray = object
										.getJSONArray("list");
								commentList.clear();
								commentList = JSON.parseArray(jsonArray.toString(), Comment.class); 
								setCommentDatas();
							} else {
							}
						} catch (Exception e) {
							AbLogUtil.d(getActivity(), e.toString());
						}
					};
				});
	}

	private void initView() {
		/*ll_course_detail = (LinearLayout) mView.findViewById(R.id.ll_course_detail);
		ll_course_detail.setFocusable(true);
		ll_course_detail.setFocusableInTouchMode(true);
		ll_course_detail.requestFocus();*/
		mCommentListView=(ListView) mView.findViewById(R.id.lv_comment);
		mOhterListView = (ListView) mView.findViewById(R.id.lv_ohter_course);
		/*rg = (RadioGroup) mView.findViewById(R.id.pinglun_rg);
		rbAll = (RadioButton) mView.findViewById(R.id.rb_all);
		rb_bad = (RadioButton) mView.findViewById(R.id.rb_bad);
		rb_middle= (RadioButton) mView.findViewById(R.id.rb_middle);
		rb_great = (RadioButton) mView.findViewById(R.id.rb_great);*/
		btOtherCourse = (Button) mView.findViewById(R.id.bt_course_other);
		/*View mPlayViews = getActivity().getLayoutInflater().inflate(R.layout.home_list_more, null);
		moreTip = (TextView) mPlayViews.findViewById(R.id.more_tip);*/
		
		tvAgreePeople = (TextView) mView.findViewById(R.id.course_agree_people);
		peopleNumber = (TextView) mView.findViewById(R.id.course_people_number);
		price = (TextView) mView.findViewById(R.id.course_price);
		startTime = (TextView) mView.findViewById(R.id.course_start_time);
		address = (TextView) mView.findViewById(R.id.course_tv_address);
		carWay = (TextView) mView.findViewById(R.id.course_car_way);
		tips = (TextView) mView.findViewById(R.id.course_tv_tips);
		tvPlan = (TextView) mView.findViewById(R.id.course_plan);
		
		tvPlan.setSingleLine(true);
		tips.setSingleLine(true);
		ll_pinglun = (LinearLayout) mView.findViewById(R.id.ll_pinglun);
		iv_pinglun = (ImageView) mView.findViewById(R.id.pinglun_img);
		contentControl1 = (ImageView) mView.findViewById(R.id.contentShowControl1);
		contentControl2 = (ImageView) mView.findViewById(R.id.contentShowControl2);
		contentControl1.setFocusable(false);
		contentControl2.setFocusable(false);
		ll_zan = (LinearLayout) mView.findViewById(R.id.ll_zan);//点赞
		zan_img=(ImageView) mView.findViewById(R.id.zan_img);//点赞心形图片
		zan=(TextView) mView.findViewById(R.id.tv_zan);//点赞数量
		tvPinglun = (TextView) mView.findViewById(R.id.tv_pinglun);
		mgv=(MGridView) mView.findViewById(R.id.gv);//点赞头像gridview
		//moreTip.setText("更多评论");
		//mListView.addFooterView(mPlayViews);
		
		
	}

	private void initListener() {
		mHander = new Handler();
		contentControl1.setOnClickListener(this);
		contentControl2.setOnClickListener(this);
		//rg.setOnCheckedChangeListener(new MyOnChangeListener());
		ll_zan.setOnClickListener(this);
		ll_pinglun.setOnClickListener(this);
		btOtherCourse.setOnClickListener(this);
	}
	
	
/*	*//**
	 * 处理rg　点击事件
	 *
	 *//*
	class MyOnChangeListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (rg.getCheckedRadioButtonId()) {
			case R.id.rb_all:
				getComment("4");
				
				//commentAdapter.notifyDataSetChanged();
				break;
			case R.id.rb_great:
				getComment("1");
				//commentAdapter.notifyDataSetChanged();			
				break;
			case R.id.rb_middle:
				getComment("2");
				//commentAdapter.notifyDataSetChanged();
				break;
			case R.id.rb_bad:
				getComment("3");
				//commentAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
			
		}
		
	}*/
	
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.more_tip:
			
			break;

		case R.id.ll_zan:
			if(!YoutiApplication.getInstance().myPreference.getHasLogin()){
				Intent intent =new Intent(getActivity(),LoginActivity.class);
				getActivity().startActivity(intent);
				return;
			}
			//如果点赞头像集合不为空
			//需要添加一个是否点赞的字段 
			//判断点赞集合是否为null
			isLogin = YoutiApplication.getInstance().myPreference.getHasLogin();
			if(courseDetailBean.getUser_heads()!=null){
				if(courseDetailBean.getPraise().equals("1")){
					//表示已经点赞 点击 变为取消点赞
					zan_img.setBackgroundResource(R.drawable.sp_heart);
				//	Utils.showToast(getActivity(), youtiApplication.myPreference.getUserId()+"&&&&"+courseDetailBean.getUser_heads().get(0));
					
					for(int i=0;i<courseDetailBean.getUser_heads().size();i++){
						if(youtiApplication.myPreference.getUserId().equals(courseDetailBean.getUser_heads().get(i).getUser_id())){
							courseDetailBean.getUser_heads().remove(i);
						//	Utils.showToast(getActivity(), youtiApplication.myPreference.getUserId()+"aaa"+i);
							break;
						}
					}
					//重新设置点赞人数
					zan.setText((courseDetailBean.getUser_heads().size())+"");
						mgvAdapter=new MyAdapter(courseDetailBean.getUser_heads());
						mgv.setAdapter(mgvAdapter);					
						courseDetailBean.setPraise("0");
					
				}else{
					//未点赞点击变为点赞
					zan_img.setBackgroundResource(R.drawable.sp_heart_h);
					courseDetailBean.setPraise("1");
					UserEntity user =new UserEntity();
					
					if(isLogin){
						if(TextUtils.isEmpty(youtiApplication.myPreference.getHeadImgPath())){
							user.setHead_img("");
						}else{
							if(youtiApplication.myPreference.getHeadImgPath().startsWith(Constants.PIC_CODE)){
								
								user.setHead_img(youtiApplication.myPreference.getHeadImgPath().substring(28, youtiApplication.myPreference.getHeadImgPath().length()));
							}else{
								user.setHead_img(youtiApplication.myPreference.getHeadImgPath());
							}
						}
					}else{
						user.setHead_img("");
					}
					
					//user.setHead_img(youtiApplication.myPreference.getHeadImgPath());
					user.setUser_id(youtiApplication.myPreference.getUserId());
					courseDetailBean.getUser_heads().add(0,user);
					//重新设置点赞人数
					zan.setText((courseDetailBean.getUser_heads().size())+"");
					
					
						mgvAdapter=new MyAdapter(courseDetailBean.getUser_heads());
						mgv.setAdapter(mgvAdapter);
					
					
				}
			}else{
				//该用户为第一个点赞
				List<UserEntity> abc=new ArrayList<UserEntity>();
				//未点赞点击变为点赞
				zan_img.setBackgroundResource(R.drawable.sp_heart_h);
				courseDetailBean.setPraise("1");
				UserEntity user =new UserEntity();
				
				if(isLogin){
					if(TextUtils.isEmpty(youtiApplication.myPreference.getHeadImgPath())){
						user.setHead_img("");
					}else{
						if(youtiApplication.myPreference.getHeadImgPath().startsWith(Constants.PIC_CODE)){
							
							user.setHead_img(youtiApplication.myPreference.getHeadImgPath().substring(28, youtiApplication.myPreference.getHeadImgPath().length()));
						}else{
							user.setHead_img(youtiApplication.myPreference.getHeadImgPath());
						}
					}
				}else{
					user.setHead_img("");
				}
				
				user.setUser_id(youtiApplication.myPreference.getUserId());
				abc.add(user);
				courseDetailBean.setUser_heads(abc);
				zan.setText(1+"");
				mgvAdapter=new MyAdapter(abc);
				mgv.setAdapter(mgvAdapter);
			}
			
			requestPraise(youtiApplication.myPreference.getUserId(),course_id);
			
			break;
		case R.id.ll_pinglun:
			
			//IntentJumpUtils.nextActivity(CommentActivity.class, getActivity(), null, Constants.REQUEST_CODE_COURSE);
			
			Intent intent = new Intent(getActivity(), CommentActivity.class);
			intent.putExtra(Constants.KEY_ID, id);
			intent.putExtra("code", Constants.REQUEST_CODE_COURSE);
			intent.putExtra(Constants.KEY_TITLE, getActivity().getIntent().getStringExtra(Constants.KEY_TITLE));
			startActivityForResult(intent, Constants.REQUEST_CODE_COURSE);
			
			
			//Bundle bundle = new Bundle();
			
			break;
		case R.id.bt_course_other:
			Bundle bundle = new Bundle();
			bundle.putString(Constants.KEY_ID, culb_id);
			IntentJumpUtils.nextActivity(ClubDetailActivity.class, getActivity(), bundle);
			break;
		case R.id.contentShowControl1:
			isShow1(tips, contentControl1);
			break;
		case R.id.contentShowControl2:
			isShow2(tvPlan, contentControl2);
			break;
		default:
			break;
		}
		
	}
	/**
	 * 发送点赞请求到服务器
	 * @param user_id
	 * @param course_id
	 */
	private void requestPraise(String user_id,String course_id) {
		//发送请求到服务器		
		RequestParams params =new RequestParams();
		params.put("user_id", user_id);
		params.put("course_id", course_id);
		HttpUtils.post(Constants.COURSE_LIST_PRAISE, params, new TextHttpResponseHandler() {
			
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
	/**
	 * 
	* @Title: setDatas 
	* @Description: TODO(根据 请求获取的数据 设置 评论页) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void setDetailDatas(){
		if(courseDetailBean!=null){
			culb_id = courseDetailBean.getClub_id();
			tvAgreePeople.setText(courseDetailBean.getAgree_people());
			peopleNumber.setText(courseDetailBean.getPeople_number()+"人");
			price.setText(courseDetailBean.getPrice()+"元");
			startTime.setText(courseDetailBean.getSe_time());
			address.setText(courseDetailBean.getAddress());
			carWay.setText(courseDetailBean.getCar_way());
			tips.setText(courseDetailBean.getWaring());
			zan.setText(courseDetailBean.getPraise_num());
			tvPlan.setText(courseDetailBean.getTeach_plain());
			tvPinglun.setText(courseDetailBean.getComment_num());
			//假设没有点赞
			//*******************************************
			/**
			 * 假设未点赞，如果没有给出，就无法显示
			 */
			//*******************************************
			//courseDetailBean.setPraise("0");
			//设置点赞心形图片
			if("1".equals(courseDetailBean.getPraise())){
				zan_img.setBackgroundResource(R.drawable.sp_heart_h);
			}else{
				zan_img.setBackgroundResource(R.drawable.sp_heart);
			}
		}
		
		mCourseList = courseDetailBean.getOther_course();
		if(mCourseList!=null&&mCourseList.size()>0){
			mAdapter = new OtherCourseAdapter(getActivity(), mCourseList);
			mOhterListView.setAdapter(mAdapter);
			mAdapter.notifyDataSetInvalidated();
			ScreenUtils.setListViewHeightBasedOnChildren(mOhterListView);
			mOhterListView.setOnItemClickListener(new MyOnItemListener());
		}
		if(courseDetailBean.getUser_heads()!=null){
			mUser_headsList=courseDetailBean.getUser_heads();			
			
		}
		
		//为点赞列表填充头像
		if(mUser_headsList!=null){
			if(mgvAdapter==null){
				mgvAdapter=new MyAdapter(mUser_headsList);
				mgv.setAdapter(mgvAdapter);
			}else{
				mgvAdapter.notifyDataSetChanged();      
			}
			
		}
		/*userList = commentBean.getUser_heads();
		if(userList!=null&&userList.size()>0){
			praiseAdapter = new PraisePicListAdapter(getActivity(), userList);
			mGridView.setAdapter(praiseAdapter);
			//praiseAdapter.notifyDataSetChanged();
		}*/
		/*commentList = commentBean.getPraise();
		if(commentList!=null&&commentList.size()>0){
			commentAdapter = new CommentAdapter(getActivity(), commentList);
			mListView.setAdapter(commentAdapter);
			ScreenUtils.setListViewHeightBasedOnChildren(mListView);
		}*/
		//mTvdianzan.setText(commentBean.getPraise_num()+"人喜欢");
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
			if(list.get(position).head_img.startsWith("http")){
				imageLoader.displayImage(list.get(position).head_img, iv);
			}else{				
				imageLoader.displayImage("http://112.126.72.250/ut_app"+list.get(position).head_img, iv,options);
			}
			
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent =new Intent(mContext,OtherPersonCenterActivity.class);
					intent.putExtra("user_id", list.get(position).user_id);
					mContext.startActivity(intent);
					
				}
			});
			
			return v;					
		}
	}	
	
	/**
	 * 
	* @Title: setDatas 
	* @Description: TODO(根据 请求获取的数据 设置 评论页) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void setCommentDatas(){
		
		//commentList = commentBean.getPraise();
		if(commentList!=null&&commentList.size()>0){
			commentAdapter = new CommentAdapter(getActivity(), commentList);
			mCommentListView.setAdapter(commentAdapter);
			ScreenUtils.setListViewHeightBasedOnChildren(mCommentListView);
			commentAdapter.notifyDataSetChanged();
		}
		//mTvdianzan.setText(commentBean.getPraise_num()+"人喜欢");
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
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	class MyListAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			return list2.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view =View.inflate(getActivity(), R.layout.item_course_pinglun, null);
			TextView tv =(TextView) view.findViewById(R.id.tv_content);
			tv.setText(list2.get(position));
			return view;
		};
	}

}
