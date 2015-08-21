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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
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
import com.youti.view.WaitDialog;
import com.youti.yonghu.activity.OtherPersonCenterActivity;
import com.youti.yonghu.bean.PersonCenterCourseBean;
import com.youti.yonghu.bean.PersonCenterCourseBean.PersonCenterCourse;

public class OtherPersonCenterCourseFragment extends Fragment{
	ListView listView;
	String user_id;
	private WaitDialog waitDialog;
	private List<PersonCenterCourse> list;
	private double j;
	private double w;
	private LatLng latLng1;
	private LatLng latLng2;
	boolean flag;
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
		View view =View.inflate(getActivity(), R.layout.fragment_systemmessage, null);
		listView=(ListView) view.findViewById(R.id.listview);
		if(getActivity().getIntent()!=null){
			user_id=((OtherPersonCenterActivity)getActivity()).user_id;
			
		}else{
			
			user_id=YoutiApplication.getInstance().myPreference.getUserId();
		}
		
		String location_j = YoutiApplication.getInstance().myPreference.getLocation_j();
		String location_w = YoutiApplication.getInstance().myPreference.getLocation_w();
		dialog = new CustomProgressDialog(getActivity(), R.string.laoding_tips,R.anim.frame2);
		dialog.show();
		try {
			j = Double.parseDouble(location_j);
			w = Double.parseDouble(location_w);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag=true;
		}
		
		latLng1 = new LatLng(w, j);
		
		fl_content=(FrameLayout) view.findViewById(R.id.fl_content);
		
		v = View.inflate(getActivity(), R.layout.view_error_layout, null);
		iv=(ImageView) v.findViewById(R.id.img_error_layout);
		tv=(TextView) v.findViewById(R.id.tv_error_layout);
		
		pb=(ProgressBar) v.findViewById(R.id.animProgress);
		
		return view;
	}
	public final String mPageName = "PersonCenterCourseFragment";
	 @Override
		public void onPause() {
			super.onPause();
			MobclickAgent.onPageEnd( mPageName );
		}


		@Override
		public void onResume() {
			super.onResume();
			
			MobclickAgent.onPageStart( mPageName );	
		}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		requestData();
		
	}
	
	
	private void requestData() {
		String urlString = "http://112.126.72.250/ut_app/index.php?m=User&a=my_course";
		RequestParams params = new RequestParams();
		params.put("user_id", user_id);
		HttpUtils.post(urlString, params, new TextHttpResponseHandler() {
			
			

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				dialog.dismiss();
				Gson gson = new Gson();
				PersonCenterCourseBean personCenterCourseBean = gson.fromJson(arg2, PersonCenterCourseBean.class);
				if(personCenterCourseBean!=null&&"1".equals(personCenterCourseBean.code)){
					//Utils.showToast(getActivity(), "请求成功");
					if(personCenterCourseBean.list!=null&&!personCenterCourseBean.list.isEmpty()){
						list = personCenterCourseBean.list;						
						listView.setAdapter(new MyAdapter());
					}else{
						fl_content.removeAllViews();
						iv.setBackgroundResource(R.drawable.page_icon_empty);
						iv.setVisibility(View.VISIBLE);
						tv.setText("暂无数据");
						pb.setVisibility(View.GONE);
						fl_content.addView(v);
						//Utils.showToast(getActivity(), "暂无已完成课程订单数据");						
					}
					
				}else if(personCenterCourseBean!=null&&"0".equals(personCenterCourseBean.code)){
					fl_content.removeAllViews();
					iv.setBackgroundResource(R.drawable.page_icon_empty);
					iv.setVisibility(View.VISIBLE);
					tv.setText("暂无数据");
					pb.setVisibility(View.GONE);
					fl_content.addView(v);
					Utils.showToast(getActivity(), "暂无数据");
				}else if(personCenterCourseBean!=null&&"0".equals(personCenterCourseBean.code)){
					Utils.showToast(getActivity(), "用户id为空");
				}else{
					Utils.showToast(getActivity(), "未知的状态码");
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				//waitDialog.dismiss();
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
				Utils.showToast(getActivity(), "请检查网络，重新连接");
			}
		});
	}

	class ViewHolder{
		LinearLayout ll;
		ImageView kc_Img;
		TextView kc_studyCounts;
		TextView kc_charge;
		TextView kc_datetime;
		TextView kc_location_distance;
		
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
				view=View.inflate(getActivity(), R.layout.myfavoriatecourse, null);
				vh=new ViewHolder();
				vh.ll=(LinearLayout) view.findViewById(R.id.ll);
				vh.kc_Img=(ImageView) view.findViewById(R.id.kc_Img);
				vh.kc_studyCounts =(TextView) view.findViewById(R.id.kc_studyCounts);
				vh.kc_charge=(TextView) view.findViewById(R.id.kc_charge);
				vh.kc_datetime=(TextView) view.findViewById(R.id.kc_datetime);
				vh.kc_location_distance=(TextView) view.findViewById(R.id.kc_location_distance);
				view.setTag(vh);
			}else{
				view =convertView;
				vh=(ViewHolder) view.getTag();
			}
			vh.ll.setVisibility(View.GONE);
			ImageLoader.getInstance().displayImage("http://112.126.72.250/ut_app"+list.get(position).de_img, vh.kc_Img);
			vh.kc_studyCounts.setText(list.get(position).study_num+"人学习过");
			vh.kc_charge.setText(list.get(position).price+"元");
			vh.kc_datetime.setText(list.get(position).time);
			
			if(flag){
				vh.kc_location_distance.setText("获取位置信息失败");
			}else{
				
				double wei=Double.parseDouble(list.get(position).wd);
				double jin=Double.parseDouble(list.get(position).jd);
				
				latLng2=new LatLng(wei, jin);
				double distance = DistanceUtil.getDistance(latLng1, latLng2)/1000;
				vh.kc_location_distance.setText((int)distance+"km");
			}
			
			
			
			
			return view;
		}
		
	}
}
