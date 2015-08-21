package com.youti.fragment;

import java.io.File;
import java.util.HashSet;
import java.util.List;

import org.apache.http.Header;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.Constants;
import com.youti.appConfig.YoutiApplication;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
import com.youti.view.CircleImageView;
import com.youti.view.CustomProgressDialog;
import com.youti.view.SwipeLayout;
import com.youti.view.WaitDialog;
import com.youti.view.SwipeLayout.SwipeListener;
import com.youti.yonghu.activity.CommentActivity;
import com.youti.yonghu.activity.MoreActivity;
import com.youti.yonghu.bean.InfoBean;
import com.youti.yonghu.bean.OrderCommenetListBean;
import com.youti.yonghu.bean.OrderCommenetListBean.OrderCommentList;

public class CommentFragment extends Fragment{
	ListView listView;
	String user_id;
	MyAdapter myAdapter;
	
	private List<OrderCommentList> list;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public final String mPageName = "CommentFragment";
	private CustomProgressDialog waitDialog;
	FrameLayout fl_content;
	private View v;
	private ImageView iv;
	private TextView tv;
	private ProgressBar pb;
	  @Override
			public void onPause() {
				// TODO Auto-generated method stub
				super.onPause();
				if(waitDialog!=null){
					waitDialog.dismiss();
					waitDialog=null;
				}
				MobclickAgent.onPageEnd( mPageName );
			}


			@Override
			public void onResume() {
				super.onResume();
				MobclickAgent.onPageStart( mPageName );	
			}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/*TextView tv =new TextView(getActivity());
		tv.setText("咨询信息Fragment");*/
		View view =View.inflate(getActivity(), R.layout.fragment_systemmessage, null);
		listView=(ListView) view.findViewById(R.id.listview);
		listView.setBackgroundColor(Color.parseColor("#eeeeee"));
		
		
		fl_content=(FrameLayout) view.findViewById(R.id.fl_content);
		
		v = View.inflate(getActivity(), R.layout.view_error_layout, null);
		iv=(ImageView) v.findViewById(R.id.img_error_layout);
		tv=(TextView) v.findViewById(R.id.tv_error_layout);
		
		pb=(ProgressBar) v.findViewById(R.id.animProgress);
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}
	
	private void requestData() {
		String url ="http://112.126.72.250/ut_app/index.php?m=User&a=my_comment";
		RequestParams params =new RequestParams();
		user_id=YoutiApplication.getInstance().myPreference.getUserId();
		params.put("user_id", user_id);
		waitDialog = new CustomProgressDialog(getActivity(), R.string.laoding_tips,R.anim.frame2);
		waitDialog.show();
		HttpUtils.post(url, params, new TextHttpResponseHandler() {
			
			

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				waitDialog.dismiss();
				Gson gson =new Gson();
				OrderCommenetListBean fromJson = gson.fromJson(arg2, OrderCommenetListBean.class);
				
				if("1".equals(fromJson.code)){
//					Utils.showToast(getActivity(), "评论数据请求成功");
					if(fromJson.list!=null&&!fromJson.list.isEmpty()){
							list = fromJson.list;
							 myAdapter=new MyAdapter();
							 listView.setAdapter(myAdapter);
					}else{
						fl_content.removeAllViews();
						iv.setBackgroundResource(R.drawable.page_icon_empty);
						iv.setVisibility(View.VISIBLE);
						tv.setText("暂无评论数据");
						pb.setVisibility(View.GONE);
						fl_content.addView(v);
						Utils.showToast(getActivity(), "没有查到数据");
					}
				}else if("0".equals(fromJson.code)){
					
				}else{
					fl_content.removeAllViews();
					iv.setBackgroundResource(R.drawable.page_icon_empty);
					iv.setVisibility(View.VISIBLE);
					tv.setText("暂无评论数据");
					pb.setVisibility(View.GONE);
					fl_content.addView(v);
					Utils.showToast(getActivity(), "数据请求失败");
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				waitDialog.dismiss();
				fl_content.removeAllViews();
				iv.setBackgroundResource(R.drawable.pagefailed_bg);
				iv.setVisibility(View.VISIBLE);
				tv.setText("网络连接异常，点击重试");
				pb.setVisibility(View.GONE);
				fl_content.addView(v);
				tv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						requestData();
					}
				});
				Utils.showToast(getActivity(), "网络连接异常");
			}
		});
	}


	class ViewHolder{
		CircleImageView head_protrait;
		TextView tv_coach_name;
		TextView tv_coach_project;
		TextView tv_hour;
		TextView tv_starttime,tv_endtime;
		Button gotocomment,bt_cancel,bt_delete;
		FrameLayout fl_content;
		TextView tv_comment;
		
		
	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser){
			requestData();
		}
	}
	class MyAdapter extends BaseAdapter{
		HashSet<Integer> mRemoved = new HashSet<Integer>();
		HashSet<SwipeLayout> mUnClosedLayouts = new HashSet<SwipeLayout>();
		
		public int getUnClosedCount(){
			return mUnClosedLayouts.size();
		}
		
		public void closeAllLayout() {
			if(mUnClosedLayouts.size() == 0)
				return;
			
			for (SwipeLayout l : mUnClosedLayouts) {
				l.close(true, false);
			}
			mUnClosedLayouts.clear();
		}
		
		
		OnClickListener onActionClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Integer p = (Integer) v.getTag();
				int id = v.getId();
				if (id == R.id.bt_cancel) {
					closeAllLayout();
				} else if (id == R.id.bt_delete) {
					closeAllLayout();
				//	RequestSQTK("", "3", list.get(p).order_id);
					final int index = p.intValue();
					if("1".equals(list.get(index).status)){
						delete(list.get(index).agree_id);
						list.remove(index);					
						notifyDataSetChanged();
						
					}else{
						//尚未评论，确定删除？
						 final Dialog deleteDialog =new Dialog(getActivity(), R.style.tkdialog);
						 View deleteViewDialog =View.inflate(getActivity(), R.layout.layout_contactservice, null);
						 ((TextView)deleteViewDialog.findViewById(R.id.tv_content)).setText("尚未评论，确认要删除吗？");
					     ((TextView)deleteViewDialog.findViewById(R.id.tv_dial)).setText("当然了");
					     deleteDialog.setContentView(deleteViewDialog);
					     deleteDialog.show();
					     deleteViewDialog.findViewById(R.id.ll_dial).setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								delete(list.get(index).agree_id);
								list.remove(index);					
								notifyDataSetChanged();
								deleteDialog.dismiss();
							}
						});
					     deleteViewDialog.findViewById(R.id.ll_back).setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								deleteDialog.dismiss();
							}
						});
					}
					
					
				}
			}
		};
		
		SwipeListener mSwipeListener = new SwipeListener() {
			@Override
			public void onOpen(SwipeLayout swipeLayout) {
//				Utils.showToast(mContext, "onOpen");
				mUnClosedLayouts.add(swipeLayout);
			}

			@Override
			public void onClose(SwipeLayout swipeLayout) {
//				Utils.showToast(mContext, "onClose");
				mUnClosedLayouts.remove(swipeLayout);
			}

			@Override
			public void onStartClose(SwipeLayout swipeLayout) {
//				Utils.showToast(mContext, "onStartClose");
			}

			@Override
			public void onStartOpen(SwipeLayout swipeLayout) {
//				Utils.showToast(mContext, "onStartOpen");
				closeAllLayout();
				mUnClosedLayouts.add(swipeLayout);
			}

		};
		@Override
		public int getCount() {
			return list.size();
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
			SwipeLayout view ;
			ViewHolder vh;
			if(convertView==null){
				view=(SwipeLayout) View.inflate(getActivity(), R.layout.item_ordercomment, null);
				vh=new ViewHolder();
				vh.head_protrait=(CircleImageView) view.findViewById(R.id.head_protrait);
				vh.tv_coach_name=(TextView) view.findViewById(R.id.tv_coach_name);
				vh.tv_coach_project=(TextView) view.findViewById(R.id.tv_coach_project);
				vh.tv_endtime=(TextView) view.findViewById(R.id.tv_endtime);
				vh.tv_hour=(TextView) view.findViewById(R.id.tv_hour);
				vh.tv_starttime=(TextView) view.findViewById(R.id.tv_starttime);
				vh.gotocomment=(Button) view.findViewById(R.id.gotocomment);
				vh.fl_content=(FrameLayout) view.findViewById(R.id.fl_content);
				vh.tv_comment=(TextView) view.findViewById(R.id.tv_comment);
				vh.bt_cancel=(Button) view.findViewById(R.id.bt_cancel);
				vh.bt_delete=(Button) view.findViewById(R.id.bt_delete);
				view.setTag(vh);
			}else{
				view =(SwipeLayout) convertView;
				vh=(ViewHolder) view.getTag();
			}
			
			view.close(false, false);
			view.setSwipeListener(mSwipeListener);
			
			vh.bt_cancel.setTag(position);
			vh.bt_cancel.setOnClickListener(onActionClick);

			vh.bt_delete.setTag(position);
			vh.bt_delete.setOnClickListener(onActionClick);
			
			
			ImageLoader.getInstance().displayImage("http://112.126.72.250/ut_app"+list.get(position).coach_img, vh.head_protrait);
			vh.tv_coach_name.setText(list.get(position).coach_name);
			vh.tv_coach_project.setText(list.get(position).pro_val);
			vh.tv_hour.setText(list.get(position).long_time);
			vh.tv_starttime.setText(list.get(position).start_time);
			vh.tv_endtime.setText(list.get(position).end_time);
			
			if("1".equals(list.get(position).status)){
				//已评论
				vh.gotocomment.setVisibility(View.GONE);
				//vh.fl_content.removeAllViews();
				vh.tv_comment.setText("已评论");
				vh.tv_comment.setVisibility(View.VISIBLE);
			}else{
				//未评论
				vh.gotocomment.setVisibility(View.VISIBLE);
				vh.tv_comment.setVisibility(View.GONE);
			}
			
			vh.gotocomment.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent =new Intent(getActivity(),CommentActivity.class);
					intent.putExtra("code", Constants.REQUEST_CODE_COACH);
					intent.putExtra("agree_id", list.get(position).agree_id);
					intent.putExtra(Constants.KEY_CHAT_TEL, list.get(position).coach_tel);
					intent.putExtra(Constants.KEY_CHAT_USERNAME, list.get(position).coach_name);
					intent.putExtra(Constants.KEY_ID, list.get(position).coach_id);
					intent.putExtra(Constants.KEY_CHAT_AVATAR, list.get(position).coach_img);
					getActivity().startActivity(intent);
				}
			});
			return view;
		}
		
	}
	
	
	public void delete(String agree_id){
		RequestParams params =new RequestParams();
		params.put("agree_id", agree_id);
		HttpUtils.post("http://112.126.72.250/ut_app/index.php?m=User&a=del_agree", params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson = new Gson();
				InfoBean fromJson = gson.fromJson(arg2, InfoBean.class);
				if("1".equals(fromJson.code)){
					Utils.showToast(getActivity(), "删除成功");
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				
			}
		});
	}
}
