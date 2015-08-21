package com.youti.fragment;

import java.util.HashSet;
import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
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
import com.youti.view.CustomProgressDialog;
import com.youti.view.SwipeLayout;
import com.youti.view.SwipeLayout.SwipeListener;
import com.youti.view.WaitDialog;
import com.youti.yonghu.activity.CommentActivity;
import com.youti.yonghu.activity.CourseOrderDetailActivity;
import com.youti.yonghu.bean.CurrentCourseBean;
import com.youti.yonghu.bean.InfoBean;
import com.youti.yonghu.bean.CurrentCourseBean.CurrentCourseDetailBean;

public class PastCourseFragment extends Fragment {
	ListView listView;
	String state;//0:已预约 1：已购买 2 未支付
	String user_id;
	private List<CurrentCourseDetailBean> list;
	MyAdapter myAdapter;
	String sta;
	String comment;
	
	FrameLayout fl_content;
	ImageView iv;
	ProgressBar pb;
	TextView tv;
	View v;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/*TextView tv =new TextView(getActivity());
		tv.setText("咨询信息Fragment");*/
		View view =View.inflate(getActivity(), R.layout.pastcourselistview, null);
		listView=(ListView) view.findViewById(R.id.listview);
		
		listView.setDividerHeight(0);
		
		user_id=YoutiApplication.getInstance().myPreference.getUserId();
		
		fl_content=(FrameLayout) view.findViewById(R.id.fl_content);
		
		v = View.inflate(getActivity(), R.layout.view_error_layout, null);
		iv=(ImageView) v.findViewById(R.id.img_error_layout);
		tv=(TextView) v.findViewById(R.id.tv_error_layout);
		
		pb=(ProgressBar) v.findViewById(R.id.animProgress);
		
		return view;
	}
	public final String mPageName = "PastCourseFragment";
	private CustomProgressDialog waitDialog;
	 @Override
		public void onPause() {
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
			requestData();
			MobclickAgent.onPageStart( mPageName );	
		}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//requestData();
		
	}
	private void requestData() {
		String url = "http://112.126.72.250/ut_app/index.php?m=User&a=history_class";
		RequestParams params =new RequestParams();
		params.put("user_id", user_id);
		waitDialog = new CustomProgressDialog(getActivity(), R.string.laoding_tips,R.anim.frame2);
		waitDialog.show();
		HttpUtils.post(url, params, new TextHttpResponseHandler() {
			
			

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				waitDialog.dismiss();
				Gson gson =new Gson();
				CurrentCourseBean fromJson = gson.fromJson(arg2, CurrentCourseBean.class);
				if("1".equals(fromJson.code)){
					if(fromJson.list!=null&&!fromJson.list.isEmpty()){
						list = fromJson.list;
						myAdapter = new MyAdapter();
						listView.setAdapter(myAdapter);
						
					}else{
						
						fl_content.removeAllViews();
						iv.setBackgroundResource(R.drawable.page_icon_empty);
						iv.setVisibility(View.VISIBLE);
						tv.setText("暂无历史订单数据");
						pb.setVisibility(View.GONE);
						fl_content.addView(v);
						
						Utils.showToast(getActivity(), "暂无数据");
						
						
					}
					
				}else if("0".equals(fromJson.code)){
					fl_content.removeAllViews();
					iv.setBackgroundResource(R.drawable.page_icon_empty);
					iv.setVisibility(View.VISIBLE);
					tv.setText("暂无历史订单数据");
					pb.setVisibility(View.GONE);
					fl_content.addView(v);
					
					Utils.showToast(getActivity(), "暂无数据");
				}else{
					Utils.showToast(getActivity(), "数据加载失败");
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
			}
		});
	}
	class ViewHolder{
		ImageView iv_qpl,iv_course,iv_cancel,iv_zhifu;
		TextView currentcourse_name,tv_state,tv_time,tv_yipingjia;
		Button bt_cancel,bt_delete;
		
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
					int index = p.intValue();
					OperateOrder(list.get(index).order_id);
					list.remove(index);					
					notifyDataSetChanged();
					
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
				view=(SwipeLayout) View.inflate(getActivity(), R.layout.item_currentcourse, null);
				vh =new ViewHolder();
//				vh.iv_qpl=(ImageView) view.findViewById(R.id.iv_qupinglun);
				vh.iv_course=(ImageView) view.findViewById(R.id.iv_course);
//				vh.iv_cancel=(ImageView) view.findViewById(R.id.iv_cancel);
//				vh.iv_zhifu=(ImageView) view.findViewById(R.id.iv_zhifu);
				vh.currentcourse_name=(TextView) view.findViewById(R.id.currentcourse_name);
				vh.tv_state=(TextView) view.findViewById(R.id.tv_state);
				vh.tv_time=(TextView) view.findViewById(R.id.tv_time);
//				vh.tv_yipingjia=(TextView) view.findViewById(R.id.tv_yipingjia);
				vh.bt_cancel=(Button) view.findViewById(R.id.bt_cancel);
				vh.bt_delete=(Button) view.findViewById(R.id.bt_delete);
				view.setTag(vh);
			}else{
				view =(SwipeLayout) convertView;
				vh=(ViewHolder) view.getTag();
			}
			
			view.close(false, false);
			view.setSwipeListener(mSwipeListener);
			
//			vh.bt_cancel.setTag(position);
//			vh.bt_cancel.setOnClickListener(onActionClick);

			vh.bt_delete.setTag(position);
			vh.bt_delete.setOnClickListener(onActionClick);
			
			
			view.getFrontView().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent =new Intent(getActivity(),CourseOrderDetailActivity.class);
					intent.putExtra("order_id", list.get(position).order_id);
					intent.putExtra("status", list.get(position).status);
					if("1".equals(list.get(position).comment)){
						//已评价
						intent.putExtra("comment", "1");
					}else if("0".equals(list.get(position).comment)){
						//未评价
						intent.putExtra("comment", "0");
					}else{
						
					}
					startActivity(intent);	
					
				}
			});
			
			
//			vh.iv_cancel.setVisibility(View.GONE);
//			vh.iv_zhifu.setVisibility(View.GONE);
			//vh.iv_qpl.setVisibility(View.VISIBLE);
//			vh.tv_yipingjia.setVisibility(View.GONE);	
			if("4".equals(list.get(position).status)){
				vh.tv_state.setText("已退款");
//				vh.iv_qpl.setVisibility(View.GONE);
			}else if("5".equals(list.get(position).status)){
				vh.tv_state.setText("已取消");
//				vh.iv_qpl.setVisibility(View.GONE);
			}else if("6".equals(list.get(position).status)){
				vh.tv_state.setText("已完成");
				
				
				if("1".equals(list.get(position).comment)){
					//已评价
//					vh.iv_qpl.setVisibility(View.GONE);
//					vh.tv_yipingjia.setVisibility(View.VISIBLE);
				}else if("0".equals(list.get(position).comment)){
					//未评价
//					vh.iv_qpl.setVisibility(View.VISIBLE);
//					vh.tv_yipingjia.setVisibility(View.GONE);
				}else{
//					vh.iv_qpl.setVisibility(View.GONE);
//					vh.tv_yipingjia.setVisibility(View.GONE);
				}
				
			}else{
				vh.tv_state.setText("未知状态");
			}
			vh.tv_time.setText(list.get(position).begin_time);
			vh.currentcourse_name.setText(list.get(position).title);
			ImageLoader.getInstance().displayImage("http://112.126.72.250/ut_app"+list.get(position).de_img, vh.iv_course);
			
//			vh.iv_qpl.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					Intent intent =new Intent(getActivity(),CommentActivity.class);
//					intent.putExtra("order_id", list.get(position).order_id);
//					intent.putExtra("status", list.get(position).status);
//					intent.putExtra(Constants.KEY_ID, list.get(position).course_id);
//					intent.putExtra("code", Constants.REQUEST_CODE_COURSE);
//					startActivity(intent);
//				}
//			});
			/*view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent =new Intent(getActivity(),CourseOrderDetailActivity.class);
					intent.putExtra("order_id", list.get(position).order_id);
					intent.putExtra("status", list.get(position).status);
					if("1".equals(list.get(position).comment)){
						//已评价
						intent.putExtra("comment", "1");
					}else if("0".equals(list.get(position).comment)){
						//未评价
						intent.putExtra("comment", "0");
					}else{
						
					}
					startActivity(intent);			
				}
			});*/
			return view;
		}
		
	}
	
	/**
	 * 删除订单 申请退款 取消预约 取消订单
	 * 
	 * 3表示删除订单
	 */
	protected void OperateOrder(String order_id) {
		String str="http://112.126.72.250/ut_app/index.php?m=User&a=cancou_order";
		RequestParams params =new RequestParams ();
		params.put("order_id", order_id);
		params.put("status", "3");
		HttpUtils.post(str, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson =new Gson();
				InfoBean infoBean = gson.fromJson(arg2, InfoBean.class);
				if("1".equals(infoBean.code)){
					Utils.showToast(getActivity(), "操作成功");
					
				}else if("0".equals(infoBean.code)){
					Utils.showToast(getActivity(), "操作失败");
				}else if("400".equals(infoBean.code)){
					Utils.showToast(getActivity(), "订单id为空");
				}else if("402".equals(infoBean.code)){
					Utils.showToast(getActivity(), infoBean.info);
				}else if("401".equals(infoBean.code)){
					Utils.showToast(getActivity(), "操作方式为空");
				}else{
					Utils.showToast(getActivity(), "错误的返回码");
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Utils.showToast(getActivity(), "网络连接异常");
			}
		});
		
		
	}
}
