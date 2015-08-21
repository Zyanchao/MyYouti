package com.youti.fragment;

import java.util.ArrayList;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.Constants;
import com.youti.appConfig.YoutiApplication;
import com.youti.chat.activity.ChatActivity;
import com.youti.utils.HttpUtils;
import com.youti.utils.IntentJumpUtils;
import com.youti.utils.Utils;
import com.youti.view.CircleImageView1;
import com.youti.view.CustomProgressDialog;
import com.youti.yonghu.activity.CoachDetailActivity;
import com.youti.yonghu.bean.ActiveBean;
import com.youti.yonghu.bean.OnLineBean;
import com.youti.yonghu.bean.SingBean;

@SuppressLint("NewApi")
public class OnLineConsultFragment extends Fragment  {
	
	ListView listView;
	LinearLayout ll_footer,llOne,llTwo,llThree;
	ImageView ivOne,ivTwo,ivThree;
	private OnLineBean onLineBean;
	private DisplayImageOptions options;
	String user_id;
	private Context mContext;
	
	private String coach_id;
	String urlString ="http://112.126.72.250/ut_app/index.php?m=Community&a=onlinetalk";
	CustomProgressDialog dialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	public final String mPageName="OnLineConsultFragment";
	 @Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			MobclickAgent.onPageEnd( mPageName );
		}


		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			MobclickAgent.onPageStart( mPageName );	
		}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		user_id = YoutiApplication.getInstance().myPreference.getUserId();
		View view =View.inflate(getActivity(), R.layout.fragment_systemmessage, null);
		listView=(ListView) view.findViewById(R.id.listview);
		listView.setDividerHeight(1);
		
		
		listView.setOnItemClickListener(new MyOnItemClick());
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()  
        .cacheInMemory().cacheOnDisc().build();  

		/*ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(  
        getActivity()).defaultDisplayImageOptions(defaultOptions)  
        .threadPriority(Thread.NORM_PRIORITY - 2)  
        .denyCacheImageMultipleSizesInMemory()  
        .discCacheFileNameGenerator(new Md5FileNameGenerator())  
        .tasksProcessingOrder(QueueProcessingType.LIFO).build();  
		ImageLoader.getInstance().init(config);  
		
		options = new DisplayImageOptions.Builder()    
        .showStubImage(R.drawable.sq_head)          // 设置图片下载期间显示的图片    
        .showImageForEmptyUri(R.drawable.empty_photo)  // 设置图片Uri为空或是错误的时候显示的图片    
        .showImageOnFail(R.drawable.empty_photo)       // 设置图片加载或解码过程中发生错误显示的图片        
        .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中    
        .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中    
        .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片    
        .build();
<<<<<<< .mine
		initData();
=======
*/
		dialog = new CustomProgressDialog(getActivity(), R.string.laoding_tips,R.anim.frame2);
		dialog.show();
		
		return view;
	}
	
	final int INITDATA=10;
	Handler handler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case INITDATA:
				if(onLineBean!=null&&onLineBean.list!=null){
					listView.setAdapter(new MyAdapter());
					
				}
				break;

			default:
				break;
			}
		};
	};
	private void initData() {
		RequestParams params= new RequestParams();
		params.put("user_id", user_id);
		HttpUtils.post(urlString, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				try {
					dialog.dismiss();
					Gson gson =new Gson();
					onLineBean = gson.fromJson(arg2, OnLineBean.class);
					listView.setAdapter(new MyAdapter());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Utils.showToast(getActivity(), "网络连接异常");
				dialog.dismiss();
			}
		});
	}

	
	
	class MyOnItemClick implements OnItemClickListener{

	

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			
			String  tel = onLineBean.list.get(position).coach_tel;
			String userName = onLineBean.list.get(position).coach_name;
			coach_id = onLineBean.list.get(position).coach_id;
			
			SetHailFellow(tel,userName);
			
			/*Bundle b = new Bundle();
			Intent intent = new Intent(getActivity(), ChatActivity.class);
			b.putString(Constants.KEY_CHAT_USERNAME, name);
			b.putString(Constants.KEY_CHAT_TEL, tel);
			b.putInt("chatType", 1);
			getActivity().startActivity(intent, b);*/
			
			/*Bundle b = new Bundle();
			b.putString(Constants.KEY_CHAT_TEL, tel);
			b.putString(Constants.KEY_CHAT_USERNAME,userName);
			b.putInt("chatType", 1);
			IntentJumpUtils.nextActivity(ChatActivity.class,getActivity(), b);*/
			//IntentJumpUtils.nextActivity(ChatActivity.class,mContext, b);
			
		}
		
	}
	
	
	// 建立好友关系
		private void SetHailFellow(final String tel,final String userName) {
			
			RequestParams params = new RequestParams(); //
			params.put("user_id",user_id);
			params.put("coach_id", coach_id);
			HttpUtils.post(Constants.HAIL_FELLOW, params, new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					if(arg0==200){
						String json = new String(arg2);
						JSONObject jsonObject = JSON.parseObject(json);
						String code = jsonObject.getString("code");
						// 接口返回
						if (code.equals("1")) {
							Bundle b = new Bundle();
							b.putString(Constants.KEY_CHAT_TEL, tel);
							b.putString(Constants.KEY_CHAT_USERNAME,userName);
							b.putInt("chatType", 1);
							IntentJumpUtils.nextActivity(ChatActivity.class,getActivity(), b);
						} else {
							/*Intent intent = new Intent(CoachDetailActivity.this,
									ChatActivity.class);
							// intent.putExtra("userId", phone);
							// intent.putExtra("cttx", coachHead);
							if (((YoutiApplication) getApplication()).myPreference
									.getHeadImgPath().length() != 0) {
								intent.putExtra(
										"uttx",
										((YoutiApplication) getApplication()).myPreference
												.getHeadImgPath());

							}
							// intent.putExtra("name", ((TextView)
							// view1.findViewById(R.id.info_username)).getText().toString());
							startActivity(intent);*/
						}
					}
				}
				
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					Utils.showToast(mContext, "获取好友信息失败");
					dialog.dismiss();
				}
				public void onFinish() {
				};
			});
			
			
		}
		
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		initData();
	}
	
	
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return onLineBean.list.size();
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
		public View getView(int position, View convertView, ViewGroup parent) {
			View view ;
			ViewHolder vh;
			
			if(convertView==null){
				view=View.inflate(getActivity(), R.layout.item_consult_message, null);
				vh=new ViewHolder();
				vh.tv_name=(TextView) view.findViewById(R.id.tv_name);
				vh.tv_state=(TextView) view.findViewById(R.id.tv_state);
				vh.tv_xiangmuitem=(TextView) view.findViewById(R.id.tv_xiangmuitem);
				vh.iv_dot=(ImageView) view.findViewById(R.id.iv_dot);
				vh.iv_header=(CircleImageView1) view.findViewById(R.id.iv_header);
				vh.iv_location=(ImageView) view.findViewById(R.id.iv_location);
				vh.iv_rz=(ImageView) view.findViewById(R.id.iv_rz);
				vh.city=(TextView) view.findViewById(R.id.city);
				vh.tv_num=(TextView) view.findViewById(R.id.tv_num);
				view.setTag(vh);
			}else{
				view =convertView;
				vh=(ViewHolder) view.getTag();
			}
			vh.tv_name.setText(onLineBean.list.get(position).coach_name);
			vh.tv_state.setText(onLineBean.list.get(position).line.equals("online")?"[在线]":"[离线请留言]");
			vh.iv_rz.setVisibility(onLineBean.list.get(position).pro_cert==1?View.VISIBLE:View.GONE);
			vh.tv_xiangmuitem.setText(onLineBean.list.get(position).project_type);
			vh.iv_location.setBackgroundResource(onLineBean.list.get(position).line.equals("online")?R.drawable.sq_location:R.drawable.sq_location_gray);
			vh.iv_dot.setBackgroundResource(onLineBean.list.get(position).line.equals("online")?R.drawable.sq_on:R.drawable.sq_off);
			vh.city.setText(onLineBean.list.get(position).server_city);
			vh.tv_num.setText(onLineBean.list.get(position).number);
			ImageLoader.getInstance().displayImage(Constants.PIC_CODE+onLineBean.list.get(position).head_img, vh.iv_header);
			return view;
		}
		
	}

	class ViewHolder{
		TextView tv_name,tv_state,tv_xiangmuitem,city,tv_num;
		ImageView iv_dot,iv_location,iv_rz;
		CircleImageView1 iv_header;
	}
	

}
