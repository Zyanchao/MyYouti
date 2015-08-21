package com.youti.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbToastUtil;
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
import com.youti.view.WaitDialog;
import com.youti.view.XListView;
import com.youti.view.XListView.IXListViewListener;
import com.youti.yonghu.activity.CourseDetailActivity;
import com.youti.yonghu.activity.CourseListActivity;
import com.youti.yonghu.adapter.CourseListAdapterTest;
import com.youti.yonghu.bean.CourseBean;
import com.youti.yonghu.bean.FavoriateCourseBean;
import com.youti.yonghu.bean.FavoriateCourseBean.CourseItemBean;
import com.youti.yonghu.bean.InfoBean;

public class MyFavoriateCourseFragment extends Fragment implements IXListViewListener {
	XListView listView;
	String userId;
	String courseId;
	private AlertDialog create;
	
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
		
		if(view != null){
			ViewGroup parent = (ViewGroup) view.getParent();  
			if (parent != null) {  
				parent.removeView(view);  
			}   
			return view;
		}
		view = View.inflate(getActivity(), R.layout.fragment_myfavoriate, null);
		
		
		
		listView=(XListView) view.findViewById(R.id.xlistview);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent =new Intent(getActivity(),CourseDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(Constants.KEY_ID, list1.get(position-1).course_id);
				intent.putExtras(bundle);
				getActivity().startActivity(intent);
			}
		});
		userId=((YoutiApplication)getActivity().getApplication()).myPreference.getUserId();
		
		fl_content=(FrameLayout) view.findViewById(R.id.fl_content);
		
		v = View.inflate(getActivity(), R.layout.view_error_layout, null);
		iv=(ImageView) v.findViewById(R.id.img_error_layout);
		tv=(TextView) v.findViewById(R.id.tv_error_layout);
		
		pb=(ProgressBar) v.findViewById(R.id.animProgress);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
		return view;
		
		
	}
	
	String urlString="";
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	
		
		
	}
	public final String mPageName = "MyFavoriateCourseFragment";
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
				// TODO Auto-generated method stub
				super.onResume();
				submit(false,userId,"1",currentPage);	
				MobclickAgent.onPageStart( mPageName );	
			}
	MyAdapter myAdapter;
	public void submit(final boolean flag,String userId2,final String status,int page) {
		String urlString ="http://112.126.72.250/ut_app/index.php?m=User&a=praise_list";
		RequestParams params =new RequestParams();
		params.put("user_id", userId);
		params.put("status", status);
		params.put("page", page+"");
		waitDialog = new CustomProgressDialog(getActivity(), R.string.laoding_tips,R.anim.frame2);
		waitDialog.show();
		HttpUtils.post(urlString, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				waitDialog.dismiss();
				Gson gson =new Gson();
				if(status.equals("1")){
					//解析的是课程
					FavoriateCourseBean fcb = gson.fromJson(arg2, FavoriateCourseBean.class);
					
					if(fcb.code.equals("1")){
						listCourse = fcb.list;
						if(myAdapter==null){
							myAdapter = new MyAdapter(listCourse);
							listView.setAdapter(myAdapter);
							myAdapter.notifyDataSetChanged();
						}else{
							if(flag){
								myAdapter.addAndRefreshListView(listCourse);
								if(listCourse.size()==0){
									AbToastUtil.showToast(getActivity(), "没有更多数据了...");
								}
							}else{
								myAdapter.refreshListView(listCourse);
							}
						}
						
						
					}else{
						/*fl_content.removeAllViews();
						iv.setBackgroundResource(R.drawable.page_icon_empty);
						iv.setVisibility(View.VISIBLE);
						tv.setText("暂无数据");
						pb.setVisibility(View.GONE);
						fl_content.addView(v);
						Utils.showToast(getActivity(), fcb.info);*/
					}
				
				}
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				if(waitDialog!=null){
					
					waitDialog.dismiss();
				}
				
				fl_content.removeAllViews();
				iv.setBackgroundResource(R.drawable.pagefailed_bg);
				iv.setVisibility(View.VISIBLE);
				tv.setText("网络连接异常，点击重试");
				pb.setVisibility(View.GONE);
				fl_content.addView(v);
				tv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						submit(false,userId,"1",0);	
					}
				});
				Utils.showToast(getActivity(), "网络连接异常");
			}
		});
	}
	
	private List<CourseItemBean> listCourse;
	private CustomProgressDialog waitDialog;
	List<CourseItemBean> list1;
	class MyAdapter extends BaseAdapter{
		
		public MyAdapter(List<CourseItemBean> list){
			list1=list;
		}
		public void addAndRefreshListView(List<CourseItemBean> lists) {
			if(list1==null){
				list1=new ArrayList<CourseItemBean>();
			}
			list1.addAll(lists);
			notifyDataSetChanged();

		}

		public void refreshListView(List<CourseItemBean> lists) {
			if(list1==null){
				list1=new ArrayList<CourseItemBean>();
				
			}else{
				list1.clear();
			}
			list1 .addAll(lists);
			notifyDataSetChanged();

		}
		
		@Override
		public int getCount() {
			return list1.size();
		}

		@Override
		public Object getItem(int position) {
			return list1.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view ;
			ViewHolder vh;
			if(convertView==null){
				view=View.inflate(getActivity(), R.layout.myfavoriatecourse, null);
				vh=new ViewHolder();
				vh.kc_charge=(TextView) view.findViewById(R.id.kc_charge);
				vh.kc_datetime=(TextView)view.findViewById(R.id.kc_datetime);
				vh.kc_studyCounts=(TextView) view.findViewById(R.id.kc_studyCounts);
				vh.kc_img=(ImageView) view.findViewById(R.id.kc_Img);
				vh.tv_num=(TextView) view.findViewById(R.id.tv_num);
				vh.iv_heart=(ImageView) view.findViewById(R.id.iv_heart);
				vh.ll=(LinearLayout) view.findViewById(R.id.ll);
				view.setTag(vh);
			}else{
				view =convertView;
				vh=(ViewHolder) view.getTag();
			}
			vh.iv_heart.setVisibility(View.VISIBLE);
			vh.tv_num.setVisibility(View.VISIBLE);
			vh.tv_num.setText(list1.get(position).praise_num);
			vh.kc_charge.setText(list1.get(position).price+"元");
			vh.kc_datetime.setText(list1.get(position).start_time);
			vh.kc_studyCounts.setText(list1.get(position).study_num+"人学习过");
			ImageLoader.getInstance().displayImage("http://112.126.72.250/ut_app"+list1.get(position).img, vh.kc_img);
			vh.ll.setOnClickListener(new OnClickListener() {
				
				

				@Override
				public void onClick(View v) {
					
					final Dialog deleteDialog =new Dialog(getActivity(), R.style.tkdialog);
					 View deleteViewDialog =View.inflate(getActivity(), R.layout.layout_contactservice, null);
					 ((TextView)deleteViewDialog.findViewById(R.id.tv_content)).setText("确认要移除该喜欢吗？");
				     ((TextView)deleteViewDialog.findViewById(R.id.tv_dial)).setText("当然了");
				     deleteDialog.setContentView(deleteViewDialog);
				     deleteDialog.show();
				     deleteViewDialog.findViewById(R.id.ll_dial).setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							removeMyCourse(position);
							list1.remove(position);
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

			
			});
			return view;
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	  if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
		  create.dismiss();
	  }
	  return false;
	 }
	
	
	class ViewHolder{
		ImageView kc_img,iv_heart;
		TextView kc_studyCounts,kc_charge,kc_datetime,tv_num;
		LinearLayout  ll;
		
	}
	
	
	private void removeMyCourse(int position) {
		RequestParams params =new RequestParams();
		params.put("user_id", userId);
		params.put("course_id",list1.get(position).course_id);
		HttpUtils.post(Constants.COURSE_LIST_PRAISE, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson =new Gson();
				InfoBean fromJson = gson.fromJson(arg2, InfoBean.class);
				if("0".equals(fromJson.code)){
					Utils.showToast(getActivity(), "移除成功");
				}else{
					Utils.showToast(getActivity(), "移除失败");
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Utils.showToast(getActivity(), "网络连接失败");
			}
		});
	}
	Handler handler = new Handler();
	private boolean flagLoadMore;
	private int currentPage;
	private View view;
	
	@Override
	public void onRefresh() {
		handler.postDelayed(new Runnable() {//处理耗时 操作
			

			@Override
			public void run() {
				  Date currentTime = new Date();
				  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
				  String dateString = formatter.format(currentTime);
				  flagLoadMore = false;
				  currentPage=0;
				 // getCourse(flagLoadMore,val,time,title);
				  submit(flagLoadMore, userId, "1", currentPage);
				  
				  listView.stopRefresh();  
				  listView.stopLoadMore();  
				  listView.setRefreshTime(dateString);  
					}
				}, 1000);
		
		}

	

	protected void loadMore() {
		
		currentPage++;//每次 点击加载更多，请求页码 +1
		flagLoadMore = true;
	//	getCourse(flagLoadMore,val,time,title);
		submit(flagLoadMore, userId, "1", currentPage);
	}
	
	
	
	@Override
	public void onLoadMore() {
		handler.postDelayed(new Runnable() {//处理耗时 操作
			@Override
			public void run() {
				loadMore();
				listView.stopRefresh();  
				listView.stopLoadMore();  
			}
		}, 200);
	}
}
