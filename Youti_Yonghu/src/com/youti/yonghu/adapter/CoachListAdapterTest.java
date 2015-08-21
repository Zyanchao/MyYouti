package com.youti.yonghu.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.bitmap.AbImageDownloader;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youti.appConfig.Constants;
import com.youti.appConfig.YoutiApplication;
import com.youti.base.BaseHolder;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
import com.youti.view.CircleImageView1;
import com.youti.view.MGridView;
import com.youti.view.XListView;
import com.youti.yonghu.activity.CommentActivity;
import com.youti.yonghu.activity.LoginActivity;
import com.youti.yonghu.activity.OtherPersonCenterActivity;
import com.youti.yonghu.bean.CoachBean;
import com.youti.yonghu.bean.InfoBean;
import com.youti.yonghu.bean.UserEntity;

public class CoachListAdapterTest extends BaseAdapter {

	private Context mContext;
	private List<CoachBean> mList;

	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	AbImageDownloader abImageDownloader;
	String userId;
	String userHeadImageUrl;
	XListView mListView;
	double w;
	double j;
	List<UserEntity> zanList = new ArrayList<UserEntity>();
	List<UserEntity> zanListCopy;
	AbImageDownloader ab;
	boolean isindex;
	boolean flagLocation;
	boolean isLogin =YoutiApplication.getInstance().myPreference.getHasLogin();
	public CoachListAdapterTest(Context mContext, List<CoachBean> list,XListView mListView,boolean isindex) {
		this.mContext = mContext;
		this.mList = list;
		this.mListView=mListView;
		this.isindex=isindex;
		
		
		ab = new AbImageDownloader(mContext);
		ab.setErrorImage(R.drawable.userhome_pic);// 正在加载
		ab.setLoadingImage(R.drawable.userhome_pic);// 正在加载
		ab.setHeight(200);
		ab.setWidth(250);
		
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_photo)// 正在加载
				.showImageForEmptyUri(R.drawable.default_photo)// 空图片
				.showImageOnFail(R.drawable.default_photo)// 错误图片
				.cacheOnDisk(true)//设置硬盘缓存
				.considerExifParams(true)
				
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		userId=((YoutiApplication)(mContext.getApplicationContext())).myPreference.getUserId();
		userHeadImageUrl=((YoutiApplication)(mContext.getApplicationContext())).myPreference.getHeadImgPath();
		try {
			w=Double.parseDouble(YoutiApplication.getInstance().myPreference.getLocation_w());
			j=Double.parseDouble(YoutiApplication.getInstance().myPreference.getLocation_j());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			flagLocation=true;
		}
	}
	

	
	
	public void addAndRefreshListView(List<CoachBean> lists) {
		if(mList==null){
			this.mList=new ArrayList<CoachBean>();
		}
		this.mList .addAll(lists);
		notifyDataSetChanged();

	}
	
	public void refreshListView(List<CoachBean> lists) {
		if(this.mList==null){
			this.mList=new ArrayList<CoachBean>();
		}else{
			this.mList.clear();
		}
		this.mList .addAll(lists);
		notifyDataSetChanged();
	}
	
	

	public class CoachHolder extends BaseHolder {
		public ImageView img,zan_img;
		private CircleImageView1 iv_user_head;
		public TextView name;//教练名称
		public TextView price;//价格
		public TextView project;//项目
		public TextView distance;//距离
		public TextView desc;//签名
		public TextView zan;
		public TextView pl;
		public LinearLayout zanPerson;
		public LinearLayout plPerson;
		public LinearLayout llzanimgs;
		
		MGridView gv;
	}


		@Override
		public int getCount() {
			return mList.size();
		}
	
	
	
		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}
	
	
	
		@Override
		public long getItemId(int position) {
			return position;
		}
	
	
	
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view ;
			final CoachHolder holder ;
			if(convertView==null){
				view =View.inflate(mContext, R.layout.new_coach_list_item, null);
				holder = new CoachHolder();
				holder.price = (TextView) view.findViewById(R.id.coach_tv_price);
				holder.img = (ImageView) view.findViewById(R.id.kc_Img);
				holder.name = (TextView) view.findViewById(R.id.coach_tv_name);
				holder.project = (TextView) view.findViewById(R.id.coach_tv_project);
				holder.distance = (TextView) view.findViewById(R.id.coach_tv_distance);
				holder.desc = (TextView) view.findViewById(R.id.coach_tv_desc);
				holder.pl = (TextView) view.findViewById(R.id.pinglun);
				holder.zanPerson = (LinearLayout) view.findViewById(R.id.ll_zan);
				holder.plPerson = (LinearLayout) view.findViewById(R.id.ll_pinglun);
				holder.iv_user_head = (CircleImageView1) view.findViewById(R.id.iv_user_head);
				holder.llzanimgs = (LinearLayout) view.findViewById(R.id.pinglun_zan_users);
				
				holder.zan_img=(ImageView) view.findViewById(R.id.zan_img);//点赞心形图片
				holder.zan = (TextView) view.findViewById(R.id.zan);//点赞总人数
				holder.gv=(MGridView) view.findViewById(R.id.gv);//点赞用户头像列表
				
				view.setTag(holder);
			}else{
				view=convertView;
				holder=(CoachHolder) view.getTag();
			}
			final CoachBean bean =mList.get(position);
			ab.display( holder.img, Constants.PIC_CODE+bean.getHead_img());
			//imageLoader.displayImage(Constants.PIC_CODE+bean.getHead_img(), holder.img, options);
			holder.price.setTag(bean);
			holder.name.setText(bean.getCoach_name());
			holder.price.setText(bean.getPrice()+"元");
			holder.project.setText(bean.getVal());
			holder.desc.setText(bean.getSign());
			holder.distance.setText(bean.getDistance()+"km");
			holder.zan.setText(bean.getPraise_num()+"");
			holder.pl.setText(bean.getComment_num()+"");
			holder.zan.setText(bean.getPraise_num());
	
			holder.name.setText(bean.getCoach_name());
			
		/*	  // 天安门坐标
		    double mLat1 = 39.915291;
		    double mLon1 = 116.403857;

		    
		    //纬度在前，经度在后
		    if(flagLocation){
		    	holder.distance.setText("获取位置信息失败");
		    }else{
		    	
		    	LatLng pt_start = new LatLng(mLat1, mLon1);
		    	
		    	
		    	LatLng pt_end = new LatLng(w, j);
		    	
		    	double distance = DistanceUtil.getDistance(pt_start, pt_end);
		    	
		    	//Toast.makeText(mContext, lati+""+lonti+""+addr, 1).show();
		    	
		    	holder.distance.setText((int)(distance/1000)+"km");
		    }*/
			
			holder.zanPerson.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if(!YoutiApplication.getInstance().myPreference.getHasLogin()){
						Intent intent =new Intent(mContext,LoginActivity.class);
						mContext.startActivity(intent);
						return;
					}
					
					CoachBean item =mList.get(position);
					item.getPraise_num();
					item.getCoach_id();
					
					//已经赞过
					if("1".equals(item.getPraise())&&item.getUser_heads()!=null){
						//该用户已经点过赞，此时取消点赞
						for(int i=0;i<item.getUser_heads().size();i++){
							if(item.getUser_heads().get(i).getUser_id().equals(userId)){
								item.getUser_heads().remove(i);
								holder.zan_img.setBackgroundResource(R.drawable.sp_heart);
								break;
							}
						}
						item.setPraise("0");
						item.setPraise_num(item.getUser_heads().size()+"");
					}else if("0".equals(item.getPraise())&&item.getUser_heads()!=null){
						//该用户没有点过赞，此时点赞
						UserEntity vb =new UserEntity();
						vb.setUser_id(userId);
						if(isLogin){
							if(TextUtils.isEmpty(userHeadImageUrl)){
								vb.setHead_img("");
							}else{
								if(userHeadImageUrl.startsWith(Constants.PIC_CODE)){
									vb.setHead_img(userHeadImageUrl.substring(28, userHeadImageUrl.length()));
									
								}else{
									vb.setHead_img(userHeadImageUrl);
								}
							}
						}else{
							vb.setHead_img("");
						}
						//将封装好的用户对象，添加到集合
						item.getUser_heads().add(0,vb);	
						holder.zan_img.setBackgroundResource(R.drawable.sp_heart_h);
						item.setPraise("1");
						item.setPraise_num(item.getUser_heads().size()+"");
					}else{
						//自己是第一个点赞的情况
						List<UserEntity> abc=new ArrayList<UserEntity>();
						UserEntity vb =new UserEntity();
						vb.setUser_id(userId);
						if(isLogin){
							if(TextUtils.isEmpty(userHeadImageUrl)){
								vb.setHead_img("");
							}else{
								
								if(userHeadImageUrl.startsWith(Constants.PIC_CODE)){
									vb.setHead_img(userHeadImageUrl.substring(28, userHeadImageUrl.length()));
									
								}else{
									vb.setHead_img(userHeadImageUrl);
								}
							}
						}else{
							vb.setHead_img("");
						}
						//将封装好的用户对象，添加到集合
						
						//zanList在listview的adapter中已经被null给赋值了，因此需要在getView方法里面，做一个判断处理
						abc.add(vb);
						holder.zan_img.setBackgroundResource(R.drawable.sp_heart_h);
						item.setPraise("1");
						item.setPraise_num(abc.size()+"");
						item.setUser_heads(abc);
						
					}
					// 更新那个位置的信息
					item.setDataId(position);
					/**
					 * ListView的局部刷新方法。
					 */	
					
					updateItemData(item);
					requestPraise(userId, mList.get(position).getCoach_id());
				}
			});
			
			holder.plPerson.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if(!YoutiApplication.getInstance().myPreference.getHasLogin()){
						Intent intent =new Intent(mContext,LoginActivity.class);
						((Activity) mContext).startActivity(intent);
					}else{
						Intent intent = new Intent(mContext, CommentActivity.class);
						//intent.putExtra(Constants.KEY_ID, Constants.REQUEST_CODE_COACH);
						intent.putExtra(Constants.KEY_CODE, Constants.REQUEST_CODE_COACH);
						intent.putExtra(Constants.KEY_ID, bean.getCoach_id());
						intent.putExtra(Constants.KEY_CHAT_TEL, bean.getCoach_tel());
						intent.putExtra(Constants.KEY_CHAT_USERNAME, bean.getCoach_name());
						intent.putExtra(Constants.KEY_CHAT_AVATAR, bean.getHead_img());
						((Activity) mContext).startActivityForResult(intent, Constants.REQUEST_CODE_COACH);
					}
				}
			});
			
			
			
			//如果是1，则表示该用户点赞了
			if("1".equals(bean.praise)){
				holder.zan_img.setBackgroundResource(R.drawable.sp_heart_h);
			}else{
				holder.zan_img.setBackgroundResource(R.drawable.sp_heart);
			}
			
			/**
			 * 刷新item时，会走以下的方法。如果点击做本地的添加头像效果，则不会走这个方法。但是当item离开屏幕，不在显示屏幕上时，如果再次出现在屏幕上，还是会走以下的方法
			 */
				zanList=bean.getUser_heads();
				
			zanListCopy=new ArrayList<UserEntity>();
			
			
			
			if(zanList!=null){
				zanListCopy.clear();
				int size=zanList.size();
				if(zanList.size()<7){
					for(int i=size-1;i>-1;i--){					
						zanListCopy.add(zanList.get(i));
					}
					holder.gv.setAdapter(new MyAdapter(zanListCopy));	
				}else{
					for(int i=size-1;i>size-7;i--){
						zanListCopy.add(zanList.get(i));
					}
					holder.gv.setAdapter(new MyAdapter(zanListCopy));
				}				
			}else{
				holder.gv.setAdapter(new MyAdapter(zanListCopy));
			}
			
			
			return view;
		}	
		/**
		 * 发送点赞到服务器
		 * @param user_id
		 * @param course_id
		 */
		private void requestPraise(String user_id,String coach_id) {
			//发送请求到服务器		
			RequestParams params =new RequestParams();
			params.put("user_id", user_id);
			params.put("coach_id", coach_id);
			HttpUtils.post(Constants.COACH_LIST_PRAISE, params, new TextHttpResponseHandler() {
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, String arg2) {
					Gson gson =new Gson();
					InfoBean fromJson = gson.fromJson(arg2, InfoBean.class);
					Message msg =Message.obtain();
					
					if(fromJson.code.equals("1")){
						//点赞成功
//						Utils.showToast(mContext, "点赞成功");
					}else if(fromJson.code.equals("0")){
						//点赞取消成功
//						Utils.showToast(mContext, "取消点赞");
					}
					
				}
				
				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					Utils.showToast(mContext, "连接网络异常");
				}
			});
		}
		/**
		 * 局部刷新的方法
		 * @param item
		 */
		public void updateItemData(CoachBean item) {
			Message msg = Message.obtain();
			
			msg.arg1 = item.getDataId();
			msg.obj=item;
			// 更新mDataList对应位置的数据
			mList.set(item.getDataId(), item);
			// handle刷新界面
			han.sendMessage(msg);
		}
		
		private Handler han = new Handler() {
			public void handleMessage(android.os.Message msg) {
				updateItem(msg.arg1,(CoachBean) msg.obj);
			};
		};
		
		/**
		 * 将某个位置的，某个对象进行刷新
		 * @param index
		 * @param item
		 */
		private void updateItem(int index,CoachBean item) {
			if (mListView == null) {
				return;
			}

			// 获取当前可以看到的item位置
			int visiblePosition = mListView.getFirstVisiblePosition();
			// 如添加headerview后 firstview就是hearderview
			// 所有索引+1 取第一个view
			 View view;
			if(isindex){
				view = mListView.getChildAt(index - visiblePosition + 1);
			}else{
				view = mListView.getChildAt(index - visiblePosition + 2);
			}
			// 获取点击的view
			 //如果没有添加headerview，则使用以下的方法，获取view
			 //View view = mListView.getChildAt(index-visiblePosition);
			TextView txt = (TextView) view.findViewById(R.id.zan);
			// 获取mDataList.set(ids, item);更新的数据
			CoachBean data = (CoachBean) getItem(index);
			// 重新设置界面显示数据,显示点赞个数
			txt.setText(data.getPraise_num()+"");
			GridView gv =(GridView) view.findViewById(R.id.gv);
			gv.setAdapter(new MyAdapter(item.getUser_heads()));
			
		}
		
		
		public class MyAdapter extends android.widget.BaseAdapter{
			List<UserEntity> list;
			public MyAdapter(List<UserEntity> list){
				this.list=list;
			}
			@Override
			public int getCount() {
				if(list.size()<7){
					return list.size();				
					
				}else{
					return 6;
				}
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
				
				View v =View.inflate(mContext, R.layout.item_image1, null);
				CircleImageView1 iv =(CircleImageView1) v.findViewById(R.id.iv);
				if(list.get(position).head_img.startsWith("http")){
					imageLoader.displayImage(list.get(position).head_img, iv,options);
				}else{
					
					imageLoader.displayImage("http://112.126.72.250/ut_app"+list.get(position).head_img, iv,options);
				}
				
				v.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent =new Intent(mContext,OtherPersonCenterActivity.class);
						intent.putExtra("user_id", list.get(position).user_id);
						mContext.startActivity(intent);
						
					}
				});
				return v;					
			}
			
					
		}	
}
