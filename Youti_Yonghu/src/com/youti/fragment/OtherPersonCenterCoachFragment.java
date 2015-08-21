package com.youti.fragment;


import java.util.List;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.youti.appConfig.YoutiApplication;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
import com.youti.view.CustomProgressDialog;
import com.youti.yonghu.activity.OtherPersonCenterActivity;
import com.youti.yonghu.bean.PersonCenterCoachBean;
import com.youti.yonghu.bean.PersonCenterCoachBean.PersonCenterCoach;

public class OtherPersonCenterCoachFragment extends Fragment{
	ListView listView;
	String user_id;
	private List<PersonCenterCoach> list;
	CustomProgressDialog dialog;
	
	FrameLayout fl_content;
	private View v;
	private ImageView iv;
	private TextView tv;
	private ProgressBar pb;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/*TextView tv =new TextView(getActivity());
		tv.setText("咨询信息Fragment");*/
		View view =View.inflate(getActivity(), R.layout.fragment_systemmessage, null);
		listView=(ListView) view.findViewById(R.id.listview);
		if(getActivity().getIntent()!=null){
			user_id=((OtherPersonCenterActivity)getActivity()).user_id;
			
		}else{
			
			user_id=YoutiApplication.getInstance().myPreference.getUserId();
		}
		dialog = new CustomProgressDialog(getActivity(), R.string.laoding_tips,R.anim.frame2);
		dialog.show();
		
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
	public final String mPageName = "PersonCenterCoachFragment";
	 @Override
		public void onPause() {
			super.onPause();
			MobclickAgent.onPageEnd( mPageName );
		}


		@Override
		public void onResume() {
			super.onResume();
			RequestData();
			MobclickAgent.onPageStart( mPageName );	
		}
	private void RequestData() {
		RequestParams params = new RequestParams();
		params.put("user_id", user_id);
		String urlString = "http://112.126.72.250/ut_app/index.php?m=User&a=my_coach";
		HttpUtils.post(urlString, params, new TextHttpResponseHandler() {
			


			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson = new Gson();
				PersonCenterCoachBean personCenterCoachBean = gson.fromJson(arg2, PersonCenterCoachBean.class);
				if("1".equals(personCenterCoachBean.code)){
					dialog.dismiss();
					if(personCenterCoachBean.list!=null&&!personCenterCoachBean.list.isEmpty()){
						list = personCenterCoachBean.list;
						listView.setAdapter(new MyAdapter());
					}else{
						fl_content.removeAllViews();
						iv.setBackgroundResource(R.drawable.page_icon_empty);
						iv.setVisibility(View.VISIBLE);
						tv.setText("暂无数据");
						pb.setVisibility(View.GONE);
						fl_content.addView(v);
						//Utils.showToast(getActivity(), "暂无已完成教练订单数据");
					}
				}else if("0".equals(personCenterCoachBean.code)){
					Utils.showToast(getActivity(), "暂无数据");
				}else if("400".equals(personCenterCoachBean.code)){
					Utils.showToast(getActivity(), "用户id为空");
				}else{
					Utils.showToast(getActivity(), "未知状态");
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				fl_content.removeAllViews();
				iv.setBackgroundResource(R.drawable.pagefailed_bg);
				iv.setVisibility(View.VISIBLE);
				tv.setText("网络连接异常，点击重试");
				pb.setVisibility(View.GONE);
				fl_content.addView(v);
				tv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						RequestData();
					}
				});
				Utils.showToast(getActivity(), "网络连接异常");
			}
		});
		
		
	}
	class ViewHolder{
		TextView tv_coach_name;
		ImageView iv_coach_head;
		TextView tv_coach_xiangmu;
		TextView tv_time;
	}

	class MyAdapter extends BaseAdapter{

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
		public View getView(int position, View convertView, ViewGroup parent) {
			View view ;
			ViewHolder vh;
			if(convertView==null){
				view=View.inflate(getActivity(), R.layout.item_personcentercoach, null);
				vh=new ViewHolder();
				vh.tv_coach_name=(TextView) view.findViewById(R.id.tv_coach_name);
				vh.iv_coach_head=(ImageView) view.findViewById(R.id.iv_coach_head);
				vh.tv_coach_xiangmu=(TextView) view.findViewById(R.id.tv_coach_xiangmu);
				vh.tv_time=(TextView) view.findViewById(R.id.tv_time);
				view.setTag(vh);
			}else{
				view =convertView;
				vh=(ViewHolder) view.getTag();
			}
			
			vh.tv_coach_name.setText(list.get(position).coach_name);
			vh.tv_coach_xiangmu.setText(list.get(position).pro_val+"教练");
			vh.tv_time.setText(list.get(position).add_time);
			ImageLoader.getInstance().displayImage("http://112.126.72.250/ut_app"+list.get(position).coach_img, vh.iv_coach_head);
			return view;
		}
		
	}
}
