package com.youti.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ab.util.AbLogUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.youti_geren.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youti.appConfig.Constants;
import com.youti.utils.ACache;
import com.youti.utils.HttpUtils;
import com.youti.view.HorizontalListView;
import com.youti.yonghu.adapter.HorizntalClubPicListAdapter;
import com.youti.yonghu.bean.ClubBean;
import com.youti.yonghu.bean.ClubBean.ClubPhoto;

/**
 * 俱乐部 简介
 * @author Administrator
 */
public class ClubDetailIntroduceFragment extends Fragment implements OnClickListener {

	/** ViewContainer组件 */

	View mView;
	private TextView mTvcontent1,tv_about;
	private boolean isShow = false;
	public ACache cache;
	public ImageLoader imageLoader;
	public DisplayImageOptions options;
	HorizontalListView hlv1;
	ArrayList<ClubPhoto> clubPhoto=new ArrayList<ClubPhoto>();
	private String id = "";
	HorizntalClubPicListAdapter hlvAdapter1;
	private ClubBean clubDetailBean;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = mView.inflate(getActivity(), R.layout.club_detail_introduce,null);
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
		
		initView();
		getDetail();
		initListener();
		return mView;
	}
	

	private void getDetail() {
			
			/**
			 * 无网络 获取缓存数据
			 */
			
			/**
			 * 请求数据 
			 */
			RequestParams params = new RequestParams();
			params.put("club_id", id);
			HttpUtils.post(Constants.CLUB_DETAIL, params,
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
								//教练 详情
								String state = response.getString("code");
								if (state.equals("1")) {
									com.alibaba.fastjson.JSONObject object = JSON
											.parseObject(response.toString());
									JSONObject jsonObject = object.getJSONObject("list"); 
									clubDetailBean = JSON.parseObject(jsonObject.toString(), ClubBean.class); 
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
		mTvcontent1 = (TextView) mView.findViewById(R.id.coach_tv_content1);
		tv_about = (TextView) mView.findViewById(R.id.tv_club_about);
		hlv1 = (HorizontalListView) mView.findViewById(R.id.hlv_pic1);
		mTvcontent1.setFocusable(false);
		hlv1.setFocusable(false);
	}
	
	private void setDatas() {
		mTvcontent1.setText(clubDetailBean.getClub_about());
		tv_about.setText(clubDetailBean.getClub_address());
		if(clubDetailBean.getClub_photo()!=null){
			clubPhoto = (ArrayList<ClubPhoto>) clubDetailBean.getClub_photo();
			if(clubPhoto.size()>0){
				if(clubPhoto.size()>0){
					hlvAdapter1=new HorizntalClubPicListAdapter(getActivity(),clubPhoto);
					hlv1.setAdapter(hlvAdapter1);
				}
			}
		}
	}
	
	private void initListener() {
		
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}
	}

	
	
}
