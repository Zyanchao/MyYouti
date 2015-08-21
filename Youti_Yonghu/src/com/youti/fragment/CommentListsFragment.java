package com.youti.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.util.AbLogUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.youti_geren.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.Constants;
import com.youti.utils.HttpUtils;
import com.youti.utils.ScreenUtils;
import com.youti.view.CustomProgressDialog;
import com.youti.yonghu.adapter.CommentAdapter;
import com.youti.yonghu.adapter.PraisePicListAdapter;
import com.youti.yonghu.bean.Comment;
import com.youti.yonghu.bean.CommentBean;
import com.youti.yonghu.bean.UserEntity;

/**
 * 评论 列表 页
 * @author zyc
 *
 */
public class CommentListsFragment extends Fragment{

	
	View mView;
	GridView mGridView;
	ListView mListView;
	TextView mTvdianzan;
	CommentBean commentBean;
	List<Comment> commentList= new ArrayList<Comment>();
	List<UserEntity> userList =new ArrayList<UserEntity>();
	PraisePicListAdapter praiseAdapter;
	CommentAdapter commentAdapter;
	String id;
	CustomProgressDialog dialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	public final String mPageName="CommentListsFragment";
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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = mView.inflate(getActivity(), R.layout.item_videocomment, null);
		mGridView=(GridView) mView.findViewById(R.id.gv_dianzan);
		mListView=(ListView) mView.findViewById(R.id.lv_comment);
		mListView.setFocusable(false);
		mTvdianzan = (TextView) mView.findViewById(R.id.tv_dianzan);
		id = getActivity().getIntent().getStringExtra(Constants.KEY_ID);
		commentBean = new CommentBean();
		dialog = new CustomProgressDialog(getActivity(), R.string.laoding_tips,R.anim.frame2);
		dialog.show();
		getData();
		return mView;
	}

	/**
	 * 
	* @Title: setDatas 
	* @Description: TODO(根据 请求获取的数据 设置 评论页) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void setDatas(){
		userList = commentBean.getUser_heads();
		if(userList!=null&&userList.size()>0){
			praiseAdapter = new PraisePicListAdapter(getActivity(), userList);
			mGridView.setAdapter(praiseAdapter);
			//praiseAdapter.notifyDataSetChanged();
		}
		commentList = commentBean.getPraise();
		if(commentList!=null&&commentList.size()>0){
			commentAdapter = new CommentAdapter(getActivity(), commentList);
			mListView.setAdapter(commentAdapter);
			ScreenUtils.setListViewHeightBasedOnChildren(mListView);
		}
		mTvdianzan.setText(commentBean.getPraise_num()+"人喜欢");
	}
	private void getData() {
		/**
		 * 无网络 获取缓存数据
		 */
		
		/**
		 * 请求数据 
		 */
		RequestParams params = new RequestParams();
		params.put("coach_id", id);
		HttpUtils.post(Constants.COACH_DETAIL_COMMENT, params,
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
							//教练 详情 评论列表
							String state = response.getString("code");
							if (state.equals("1")) {
								com.alibaba.fastjson.JSONObject object = JSON
										.parseObject(response.toString());
								JSONObject jsonObject = object.getJSONObject("list"); 
								commentBean = JSON.parseObject(jsonObject.toString(), CommentBean.class); 
								setDatas();
							} else {
							}
						} catch (Exception e) {
							AbLogUtil.d(getActivity(), e.toString());
						}
					};
				});
		
	}
}
