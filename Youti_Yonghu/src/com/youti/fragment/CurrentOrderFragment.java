package com.youti.fragment;

import java.util.HashSet;
import java.util.List;

import org.apache.http.Header;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import android.widget.Toast;

import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.YoutiApplication;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
import com.youti.view.CircleImageView;
import com.youti.view.CustomProgressDialog;
import com.youti.view.SwipeLayout;
import com.youti.view.SwipeLayout.SwipeListener;
import com.youti.yonghu.activity.OrderDetailActivity;
import com.youti.yonghu.activity.PersonCenterActivity;
import com.youti.yonghu.bean.InfoBean;
import com.youti.yonghu.bean.MyCoachOrderBean;
import com.youti.yonghu.bean.MyCoachOrderBean.MyCoachOrderDetailBean;

public class CurrentOrderFragment extends Fragment{
	ListView listView;
	String user_id;
	List<MyCoachOrderDetailBean> list;
	
	FrameLayout fl_content;
	ImageView iv;
	ProgressBar pb;
	TextView tv;
	View v;
	private Dialog dialog;
	YoutiApplication youtiApplication;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	public final String mPageName="CurrentOrderFragment";
	private Dialog createProgressBarDialog;
	  


			@Override
			public void onResume() {
				// TODO Auto-generated method stub
				super.onResume();
				requestData();
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
		user_id=((YoutiApplication)getActivity().getApplication()).myPreference.getUserId();
		
		fl_content=(FrameLayout) view.findViewById(R.id.fl_content);
		
		v = View.inflate(getActivity(), R.layout.view_error_layout, null);
		iv=(ImageView) v.findViewById(R.id.img_error_layout);
		tv=(TextView) v.findViewById(R.id.tv_error_layout);
		
		pb=(ProgressBar) v.findViewById(R.id.animProgress);
		youtiApplication = YoutiApplication.getInstance();
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		
		
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(dialog!=null){
			dialog.dismiss();
		}
	}
	@Override
	public void onPause() {
		super.onPause();
		if(dialog!=null){
			dialog.dismiss();
			dialog=null;
		}
		
		if(createProgressBarDialog!=null){
			createProgressBarDialog.dismiss();
			createProgressBarDialog=null;
		}
		MobclickAgent.onPageEnd( mPageName );
	}
	
	private void requestData() {
		dialog =new CustomProgressDialog(getActivity(), R.string.laoding_tips,R.anim.frame2);
		dialog.show();
		String urlString="http://112.126.72.250/ut_app/index.php?m=User&a=my_order";
		RequestParams params =new RequestParams();
		user_id=youtiApplication.myPreference.getUserId();
		params.put("user_id", user_id);
		HttpUtils.post(urlString, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				dialog.dismiss();
				Gson gson =new Gson();
				MyCoachOrderBean mcod = gson.fromJson(arg2, MyCoachOrderBean.class);
				if(mcod.code.equals("1")){
					
					if(mcod.list!=null&&!mcod.list.isEmpty()){						
						list=mcod.list;
						listView.setAdapter(new MyAdapter());
					}else{
						fl_content.removeAllViews();
						iv.setBackgroundResource(R.drawable.page_icon_empty);
						iv.setVisibility(View.VISIBLE);
						tv.setText("暂无订单数据");
						pb.setVisibility(View.GONE);
						fl_content.addView(v);
						
						Utils.showToast(getActivity(), "暂无数据");
					}
					
				}else if(mcod.code.equals("0")){
					
				}else{
					Utils.showToast(getActivity(), "用户id为空");
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				dialog.dismiss();
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
	/**
	 *  status:1取消订单  2申请退款  3删除订单
	 */
	private void RequestSQTK(String content,String status,String order_id) {
		RequestParams params =new RequestParams();
		params.put("order_id", order_id);
		params.put("status", status);
		Utils.showToast(getActivity(), content);
		params.put("content", content);
		String urlString="http://112.126.72.250/ut_app/index.php?m=User&a=operation_order";
		createProgressBarDialog = Utils.createProgressBarDialog(getActivity(), "正在删除...");
		createProgressBarDialog.show();
		HttpUtils.post(urlString, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				createProgressBarDialog.dismiss();
				Gson gson =new Gson();
				InfoBean infoBean = gson.fromJson(arg2, InfoBean.class);
				if("403".equals(infoBean.code)){
					Utils.showToast(getActivity(), "课程正在进行不能退款，课程结束后再申请退款");
				}else if("402".equals(infoBean.code)){
					Utils.showToast(getActivity(), "描述不能为空");
				}else if("404".equals(infoBean.code)){
					Utils.showToast(getActivity(), "参数有误");
				}else if("401".equals(infoBean.code)){
					Utils.showToast(getActivity(), "操作方式不能为空");
				}else if("400".equals(infoBean.code)){
					Utils.showToast(getActivity(), "订单id为空");
				}else if("0".equals(infoBean.code)){
					Utils.showToast(getActivity(), "操作失败");
				}else if("1".equals(infoBean.code)){
					Utils.showToast(getActivity(), "操作成功");
					dialog.dismiss();
				}else{
					Utils.showToast(getActivity(), "错误码："+infoBean.code);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				createProgressBarDialog.dismiss();
				Utils.showToast(getActivity(), "网络连接异常，请检查网络，重新尝试");
			}
		});
	}
	
	class ViewHolder{
		CircleImageView coach_head;
		Button wannaorder,ljzf,bt_cancel,bt_delete;
		TextView coach_name,coach_project,coach_price,coach_totaltime,coach_remnant,coach_zkou,coach_totalprice,coach_state,coach_zongjia;
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
					int index = p.intValue();
					closeAllLayout();
					if("1".equals(list.get(index).status)||"3".equals(list.get(index).status)){
						final Dialog okDialog =new Dialog(getActivity(),R.style.tkdialog);
						View okView=View.inflate(getActivity(), R.layout.dialog_ok, null);
						TextView tv_tip =(TextView) okView.findViewById(R.id.tv_tip);
						tv_tip.setText("订单正在处理中,暂时无\n法删除");						
						okDialog.setContentView(okView);
						okDialog.show();
						
						okView.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								okDialog.dismiss();
							}
						});
			
					}else{
						
						RequestSQTK("", "3", list.get(p).order_id);
						list.remove(index);					
						notifyDataSetChanged();
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
				view=(SwipeLayout) View.inflate(getActivity(), R.layout.item_order, null);
				vh=new ViewHolder();
				vh.coach_head=(CircleImageView) view.findViewById(R.id.coach_head);
				vh.coach_name=(TextView) view.findViewById(R.id.coach_name);
				vh.coach_price=(TextView) view.findViewById(R.id.coach_price);
				vh.coach_project=(TextView) view.findViewById(R.id.coach_project);
				vh.coach_remnant=(TextView) view.findViewById(R.id.coach_remnant);
				vh.coach_state=(TextView) view.findViewById(R.id.coach_state);
				vh.coach_totalprice=(TextView) view.findViewById(R.id.coach_totalprice);
				vh.coach_totaltime=(TextView) view.findViewById(R.id.coach_totaltime);
				vh.coach_zkou=(TextView) view.findViewById(R.id.coach_zkou);
				vh.wannaorder=(Button) view.findViewById(R.id.wannaorder);
				vh.ljzf=(Button) view.findViewById(R.id.ljzf);
				vh.coach_zongjia=(TextView) view.findViewById(R.id.coach_zongjia);
				vh.bt_cancel=(Button) view.findViewById(R.id.bt_cancel);
				vh.bt_delete=(Button) view.findViewById(R.id.bt_delete);
				view.setTag(vh);
			}else{
				view = (SwipeLayout) convertView;
				vh=(ViewHolder) view.getTag();
			}
			
			view.close(false, false);

			view.getFrontView().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent =new Intent(getActivity(),OrderDetailActivity.class);
					intent.putExtra("order_id", list.get(position).order_id);
					intent.putExtra("remnant", list.get(position).remnant);
					intent.putExtra("coach_id", list.get(position).coach_id);
					intent.putExtra("status", list.get(position).status);
					getActivity().startActivity(intent);
					
				}
			});

			view.setSwipeListener(mSwipeListener);
			
			
			vh.bt_cancel.setTag(position);
			vh.bt_cancel.setOnClickListener(onActionClick);

			vh.bt_delete.setTag(position);
			vh.bt_delete.setOnClickListener(onActionClick);
			
			
			
			
			vh.coach_name.setText(list.get(position).coach_name);
			vh.coach_project.setText(list.get(position).pro_val);
			vh.coach_price.setText(list.get(position).price);
			ImageLoader.getInstance().displayImage("http://112.126.72.250/ut_app"+list.get(position).head_img, vh.coach_head);
			vh.coach_totaltime.setText(list.get(position).total_time);
			vh.coach_remnant.setText(list.get(position).remnant);
			vh.coach_totalprice.setText(list.get(position).ag_total_price);
			
			
			
			
			String status=list.get(position).status;
			if("0".equals(status)){
				vh.coach_state.setText("待支付");
				vh.coach_zongjia.setText("总价:");
				vh.coach_state.setTextColor(Color.RED);
				vh.wannaorder.setVisibility(View.GONE);
				vh.ljzf.setVisibility(View.VISIBLE);
			}else if("1".equals(status)){
				vh.coach_state.setText("已支付");
				vh.coach_zongjia.setText("总价:");
				vh.wannaorder.setVisibility(View.VISIBLE);
				vh.ljzf.setVisibility(View.GONE);
				vh.coach_state.setTextColor(Color.parseColor("#666666"));
			}else if("2".equals(status)){
				vh.ljzf.setVisibility(View.GONE);
				vh.coach_state.setText("已完成");
				vh.wannaorder.setVisibility(View.GONE);
				vh.coach_state.setTextColor(Color.parseColor("#333333"));
				vh.coach_zongjia.setText("总价:");
			}else if("3".equals(status)){
				vh.ljzf.setVisibility(View.GONE);
				vh.coach_state.setText("退款中");
				vh.wannaorder.setVisibility(View.GONE);
				vh.coach_state.setTextColor(Color.parseColor("#ffc901"));
				vh.coach_zongjia.setText("总价:");
				
			}else if("4".equals(status)){
				vh.ljzf.setVisibility(View.GONE);
				vh.coach_state.setText("已退款");
				vh.wannaorder.setVisibility(View.GONE);
				vh.coach_state.setTextColor(Color.parseColor("#666666"));
				vh.coach_zongjia.setText("退款总额:");
			}else if("5".equals(status)){
				vh.ljzf.setVisibility(View.GONE);
				vh.coach_state.setText("已取消");
				vh.coach_state.setTextColor(Color.parseColor("#666666"));
				vh.wannaorder.setVisibility(View.GONE);
				vh.coach_zongjia.setText("总价:");
			}
			
			
			if(TextUtils.isEmpty(list.get(position).zkou)){
				vh.coach_zkou.setVisibility(View.GONE);
			}else if("10".equals(list.get(position).zkou)){
				vh.coach_zkou.setVisibility(View.VISIBLE);
				vh.coach_zkou.setText("无");
			}else{
				vh.coach_zkou.setVisibility(View.VISIBLE);
				vh.coach_zkou.setText(list.get(position).zkou);
			}
			/**
			 * 我要约课
			 */
			vh.wannaorder.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent =new Intent(getActivity(),OrderDetailActivity.class);
					intent.putExtra("order_id", list.get(position).order_id);
					intent.putExtra("remnant", list.get(position).remnant);
					intent.putExtra("coach_id", list.get(position).coach_id);
					intent.putExtra("status", list.get(position).status);
					getActivity().startActivity(intent);
				}
			});
			/**
			 * 立即支付
			 */
			vh.ljzf.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent =new Intent(getActivity(),OrderDetailActivity.class);
					intent.putExtra("order_id", list.get(position).order_id);
					intent.putExtra("remnant", list.get(position).remnant);
					intent.putExtra("coach_id", list.get(position).coach_id);
					intent.putExtra("status", list.get(position).status);
					getActivity().startActivity(intent);
				}
			});
			/*view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
				}
			});*/
			return view;
		}
		
	}
}
