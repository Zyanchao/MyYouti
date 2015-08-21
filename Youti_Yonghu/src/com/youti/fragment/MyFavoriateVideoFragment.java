package com.youti.fragment;

import java.util.List;

import org.apache.http.Header;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.Constants;
import com.youti.appConfig.YoutiApplication;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
import com.youti.view.WaitDialog;
import com.youti.yonghu.activity.VideoDetailActivity;
import com.youti.yonghu.bean.FavoriateVideoBean;
import com.youti.yonghu.bean.FavoriateVideoBean.VideoItemBean;
import com.youti.yonghu.bean.InfoBean;

public class MyFavoriateVideoFragment extends Fragment {
	ListView listView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	String videoUrl="";
	String userId;
	AlertDialog create;
	
	FrameLayout fl_content;
	private View v;
	private ImageView iv;
	private TextView tv;
	private ProgressBar pb;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/*TextView tv =new TextView(getActivity());
		tv.setText("咨询信息Fragment");*/
		View view =View.inflate(getActivity(), R.layout.fragment_systemmessage, null);
		listView=(ListView) view.findViewById(R.id.listview);
		userId=((YoutiApplication)getActivity().getApplication()).myPreference.getUserId();
		listView.setDividerHeight(1);

		fl_content=(FrameLayout) view.findViewById(R.id.fl_content);
		
		v = View.inflate(getActivity(), R.layout.view_error_layout, null);
		iv=(ImageView) v.findViewById(R.id.img_error_layout);
		tv=(TextView) v.findViewById(R.id.tv_error_layout);
		
		pb=(ProgressBar) v.findViewById(R.id.animProgress);
		
		
		fl_content.removeAllViews();
		iv.setBackgroundResource(R.drawable.page_icon_empty);
		iv.setVisibility(View.VISIBLE);
		tv.setText("暂无数据");
		pb.setVisibility(View.GONE);
		fl_content.addView(v);
		
		return view;
	}
	public final String mPageName = "MyFavoriateVideoFragment";
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
				MobclickAgent.onPageStart( mPageName );	
			}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//submit("3");
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent =new Intent(getActivity(),VideoDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("video_id", listVideo.get(position).video_id);
				intent.putExtras(bundle);
				getActivity().startActivity(intent);
			}
		});
	
		
	}
	
	public void submit(final String status) {
		String urlString ="http://112.126.72.250/ut_app/index.php?m=User&a=praise_list";
		RequestParams params =new RequestParams();
		params.put("user_id", userId);
		params.put("status", status);
		waitDialog = Utils.getWaitDialog(getActivity(), "正在加载...");
		waitDialog.show();
		HttpUtils.post(urlString, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				waitDialog.dismiss();
				Gson gson =new Gson();
				if(status.equals("3")){
					//解析的是视频
					FavoriateVideoBean fvb = gson.fromJson(arg2, FavoriateVideoBean.class);
					
					if(fvb.code.equals("1")){
						listVideo = fvb.list;
						
						if(listVideo!=null){
							Utils.showToast(getActivity(), listVideo.size()+"");
							listView.setAdapter(new MyAdapter());
						}
					}else{
						fl_content.removeAllViews();
						iv.setBackgroundResource(R.drawable.page_icon_empty);
						iv.setVisibility(View.VISIBLE);
						tv.setText("暂无数据");
						pb.setVisibility(View.GONE);
						fl_content.addView(v);
						Utils.showToast(getActivity(), fvb.info);

					}
				
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
						submit("3");	
					}
				});
				Utils.showToast(getActivity(), "网络连接异常");
			}
		});
	}
	
	private void removeMyVideo(int position) {
		RequestParams params =new RequestParams();
		params.put("user_id", userId);
		params.put("video_id",listVideo.get(position).video_id);
		HttpUtils.post(Constants.VIDEO_PRAISE, params, new TextHttpResponseHandler() {
			
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
	List<VideoItemBean> listVideo;
	private DisplayImageOptions options;
	private WaitDialog waitDialog;
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return listVideo.size();
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view ;
			ViewHolder vh;
			if(convertView==null){
				view=View.inflate(getActivity(), R.layout.item_myvideo, null);
				vh=new ViewHolder();
				vh.iv_picture=(ImageView) view.findViewById(R.id.iv_picture);
				vh.tv_num=(TextView) view.findViewById(R.id.tv_num);
				vh.tv_title=(TextView) view.findViewById(R.id.tv_title);
				vh.iv_heart=(ImageView) view.findViewById(R.id.iv_heart);
				vh.rl_heart=(RelativeLayout) view.findViewById(R.id.rl_heart);
				view.setTag(vh);
			}else{
				view =convertView;
				vh=(ViewHolder) view.getTag();
				
			}

			vh.tv_num.setText(listVideo.get(position).praise_num);
			vh.rl_heart.setOnClickListener(new OnClickListener() {
				
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
							removeMyVideo(position);
							listVideo.remove(position);
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
			ImageLoader.getInstance().displayImage(listVideo.get(position).img, vh.iv_picture);
			
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
		ImageView iv_picture,iv_heart;
		TextView tv_num,tv_title;
		RelativeLayout rl_heart;
		
	}
}
