package com.youti.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import com.youti.yonghu.activity.ClubDetailActivity;
import com.youti.yonghu.activity.CommentActivity;
import com.youti.yonghu.activity.CourseDetailActivity;
import com.youti.yonghu.activity.OtherPersonCenterActivity;
import com.youti.yonghu.activity.WanshanCoachActivity;
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
public class CourseDetailCoachJsFragment extends Fragment implements OnClickListener {

	View mView;
	ListView mCommentListView;
	TextView  tvContent,tips,zan;
	ArrayList<String> list2 =new ArrayList<String>();
	public ACache cache;
	public ImageLoader imageLoader;
	public DisplayImageOptions options;
	HorizontalListView hlv1,hlv2;
	ArrayList<PicsBean> photoList1=new ArrayList<PicsBean>();
	ArrayList<PicsBean> photoList2=new ArrayList<PicsBean>();
	ArrayList<ImageView> photo_list=new ArrayList<ImageView>();
	private String id = "";
	HorizntalPicListAdapter hlvAdapter1,hlvAdapter2;
	
	
	CommentBean commentBean;
	List<Comment> commentList= new ArrayList<Comment>();
	List<UserEntity> userList =new ArrayList<UserEntity>();
	PraisePicListAdapter praiseAdapter;
	CommentAdapter commentAdapter;
	YoutiApplication youtiApplication;
	String course_id,content;
	private Button bt_wanshan;
	CustomProgressDialog dialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = mView.inflate(getActivity(), R.layout.course_detail_coach_introduce,null);
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
	}
	
	private void getCourseDetail(){
		/**
		 * 请求数据 
		 */
		RequestParams params = new RequestParams();
		params.put("user_id", youtiApplication.myPreference.getUserId());
		params.put("course_id", id);
		HttpUtils.post(Constants.COURSE_DETAIL_COACH_JS, params,
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
								com.alibaba.fastjson.JSONObject object = JSON.parseObject(response.toString());
								content =object.getString("list"); 
								tvContent.setText(content);
							} else {
								return;
							}
						} catch (Exception e) {
							AbLogUtil.d(getActivity(), e.toString());
						}
					};
				});
	}
	

	private void initView() {
		bt_wanshan = (Button) mView.findViewById(R.id.bt_wanshan);
		tvContent = (TextView) mView.findViewById(R.id.tv_content);
		
	}

	private void initListener() {
		bt_wanshan.setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.bt_wanshan:
			Bundle bundle1 = new Bundle();
			bundle1.putString(Constants.KEY_ID, id);
			IntentJumpUtils.nextActivity(WanshanCoachActivity.class, getActivity(), bundle1);
			break;
		default:
			break;
		}
		
	}
	

}
