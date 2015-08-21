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
import android.widget.Toast;

import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
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
import com.youti.yonghu.bean.CourseBean;
import com.youti.yonghu.bean.InfoBean;
import com.youti.yonghu.bean.UserEntity;

public class CourseListAdapterTest  extends BaseAdapter {

	Context mContext;
	List<CourseBean> mList;
	String userId;
	String userHeadImageUrl;
	XListView mListView;
	List<UserEntity> zanList = new ArrayList<UserEntity>();
	List<UserEntity> abc =new ArrayList<UserEntity>();
	List<UserEntity> zanListCopy;
	ImageLoader imageLoader;
	DisplayImageOptions options;
	double lati;
	double lonti;
	String addr;
	boolean flag;
	boolean isindex;
	boolean isLogin=YoutiApplication.getInstance().myPreference.getHasLogin();
	public CourseListAdapterTest(Context mContext, List<CourseBean> list,XListView mListView,boolean isindex) {
		this.mContext = mContext;
		this.mList = list;
		this.isindex=isindex;
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.user_lbk)// 正在加载
				.showImageForEmptyUri(R.drawable.user_lbk)// 空图片
				.showImageOnFail(R.drawable.user_lbk)// 错误图片
				.cacheInMemory(true)//设置 内存缓存
				.cacheOnDisk(true)//设置硬盘缓存
				.displayer(new RoundedBitmapDisplayer(10))//是否设置为圆角，弧度为多少  
				.displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间  
				.imageScaleType(ImageScaleType.EXACTLY) // default 推荐.imageScaleType(ImageScaleType.EXACTLY) 节省内存
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		userId = ((YoutiApplication)(mContext.getApplicationContext())).myPreference.getUserId();
		userHeadImageUrl=((YoutiApplication)(mContext.getApplicationContext())).myPreference.getHeadImgPath();
		try {
			lati=Double.parseDouble(((YoutiApplication)(mContext.getApplicationContext())).myPreference.getLocation_w());
			lonti=Double.parseDouble(((YoutiApplication)(mContext.getApplicationContext())).myPreference.getLocation_j());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			flag=true;
		}
		addr = ((YoutiApplication)(mContext.getApplicationContext())).myPreference.getCity();
		this.mListView=mListView;
	}

	public CourseListAdapterTest(Context mContext, List<CourseBean> list) {
		this.mContext = mContext;
		this.mList = list;
		
	}
	
	public void addAndRefreshListView(List<CourseBean> lists) {
		if(mList==null){
			this.mList=new ArrayList<CourseBean>();
		}
		this.mList .addAll(lists);
		notifyDataSetChanged();

	}

	public void refreshListView(List<CourseBean> lists) {
		if(this.mList==null){
			this.mList=new ArrayList<CourseBean>();
		}else{
			this.mList.clear();
		}
		this.mList .addAll(lists);
		notifyDataSetChanged();

	}


	public class CourseItemBeanHolder extends BaseHolder {
		public ImageView img,zan_img;
		public TextView describe;
		public TextView studyCount;
		public TextView charge;
		public TextView datetime;
		public TextView distance;
		public TextView zan;
		public TextView pl;
		public LinearLayout zanPerson;
		public LinearLayout plPerson;
		public MGridView gv;
		public TextView kc_location_distance;
	}


	/**
	 * 局部刷新的方法
	 * @param item
	 */
	public void updateItemData(CourseBean item) {
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
			updateItem(msg.arg1,(CourseBean) msg.obj);
		};
	};
	
	
	/**
	 * 将某个位置的，某个对象进行刷新
	 * @param index
	 * @param item
	 */
	private void updateItem(int index,CourseBean item) {
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
		CourseBean data = (CourseBean) getItem(index);
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
			if(list.get(position).head_img.startsWith("http:")){
				imageLoader.displayImage(list.get(position).head_img, iv);
			}else{
				
				imageLoader.displayImage(Constants.PIC_CODE+list.get(position).head_img, iv,options);
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
	class MyOnClicListener implements OnClickListener{
		int position;
		CourseItemBeanHolder holder;
		public MyOnClicListener(int position,CourseItemBeanHolder holder){
			this.position=position;
			this.holder=holder;
		}
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_zan:
				//Toast.makeText(mContext, position+"", 1).show();
				if(!YoutiApplication.getInstance().myPreference.getHasLogin()){
					Intent intent =new Intent(mContext,LoginActivity.class);
					mContext.startActivity(intent);
					return;
				}
				
				CourseBean item =mList.get(position);
				item.getPraise_num();
				item.getCourse_id();
				
				//已经赞过
				if("1".equals(item.getPraise())&&item.getUser_heads()!=null){
					//该用户已经点过赞，此时取消点赞
					//Toast.makeText(mContext, "此时取消", 0).show();
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
					//Toast.makeText(mContext, "此时点赞", 0).show();
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
				requestPraise(userId, mList.get(position).course_id);
			
				break;

			case R.id.ll_pinglun:
				if(!YoutiApplication.getInstance().myPreference.getHasLogin()){
					Intent intent =new Intent(mContext,LoginActivity.class);
					((Activity) mContext).startActivity(intent);
				}else{
					
					Intent intent = new Intent(mContext, CommentActivity.class);
					intent.putExtra("code", Constants.REQUEST_CODE_COURSE);
					intent.putExtra(Constants.KEY_ID, mList.get(position).course_id);
					intent.putExtra(Constants.KEY_TITLE, mList.get(position).describe);
					((Activity) mContext).startActivityForResult(intent, Constants.REQUEST_CODE_COURSE);
				}
			default:
				break;
			}
		}
		
	}

	private void requestPraise(String user_id,String course_id) {
		//发送请求到服务器		
		RequestParams params =new RequestParams();
		params.put("user_id", user_id);
		params.put("course_id", course_id);
		HttpUtils.post(Constants.COURSE_LIST_PRAISE, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson =new Gson();
				InfoBean fromJson = gson.fromJson(arg2, InfoBean.class);
				Message msg =Message.obtain();
				
				if(fromJson.code.equals("1")){
					//点赞成功
					//Utils.showToast(mContext, "点赞成功");
				}else if(fromJson.code.equals("0")){
					//点赞取消成功
				//	Utils.showToast(mContext, "取消点赞");
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Utils.showToast(mContext, "连接网络异常");
			}
		});
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
		View view;
		CourseItemBeanHolder holder;
		if(convertView==null){
			view =View.inflate(mContext, R.layout.new_item_course, null);
			holder=new CourseItemBeanHolder();
			holder.charge = (TextView) view.findViewById(R.id.kc_charge);
			holder.img = (ImageView) view.findViewById(R.id.kc_Img);
			holder.studyCount = (TextView) view.findViewById(R.id.kc_studyCounts);
			holder.describe = (TextView) view.findViewById(R.id.tv_describe);
			
			holder.datetime = (TextView) view.findViewById(R.id.kc_datetime);
			holder.distance = (TextView) view.findViewById(R.id.kc_location_distance);
			holder.zan = (TextView) view.findViewById(R.id.zan);
			holder.pl = (TextView) view.findViewById(R.id.pinglun);
			holder.zanPerson = (LinearLayout) view.findViewById(R.id.ll_zan);
			holder.plPerson = (LinearLayout) view.findViewById(R.id.ll_pinglun);
			
			holder.zan_img=(ImageView) view.findViewById(R.id.zan_img);
			holder.gv=(MGridView) view.findViewById(R.id.gv);
			
			holder.kc_location_distance=(TextView) view.findViewById(R.id.kc_location_distance);
			view.setTag(holder);
			
		}else{
			view=convertView;
			holder=(CourseItemBeanHolder) view.getTag();
		}
		
		
		holder.zanPerson.setOnClickListener(new MyOnClicListener(position,holder));
		holder.plPerson.setOnClickListener(new MyOnClicListener(position,holder));
		
		CourseBean bean =mList.get(position);
		
		
		holder.charge.setText(bean.price + "元/小时");
		imageLoader.displayImage(Constants.PIC_CODE+bean.img, holder.img,options);
		holder.studyCount.setText(bean.study_num + "人学习过");
		holder.studyCount.setTag(bean);
		
		holder.datetime.setText(bean.getStart_time());
		holder.describe.setText(bean.getDescribe());
		holder.distance.setText(bean.distance + "km");
		holder.zan.setText(bean.praise_num+"");
		
		/*  // 天安门坐标
	    double mLat1 = 39.915291;
	    double mLon1 = 116.403857;

	    double mLat2 = lati;
	    double mLon2 = lonti;
	    //纬度在前，经度在后
	    
		//Toast.makeText(mContext, lati+""+lonti+""+addr, 1).show();
		
	    if(flag){
	    	holder.kc_location_distance.setText("获取位置信息失败");
	    }else{
	    	
	    	if(lati>0){
	    		LatLng pt_start = new LatLng(mLat1, mLon1);
			    LatLng pt_end = new LatLng(mLat2, mLon2);
				double dis = DistanceUtil.getDistance(pt_start, pt_end);
				String distance = DistanceUtils.formatDistance(dis);
		    	holder.kc_location_distance.setText(distance+"");
	    	}
	    	
	    }*/
		//如果是1，则表示该用户点赞了
		if("1".equals(bean.praise)){
			holder.zan_img.setBackgroundResource(R.drawable.sp_heart_h);
		}else{
			holder.zan_img.setBackgroundResource(R.drawable.sp_heart);
		}
		holder.pl.setText(bean.comment_num+"");
		
		/**
		 * 刷新item时，会走以下的方法。如果点击做本地的添加头像效果，则不会走这个方法。但是当item离开屏幕，不在显示屏幕上时，如果再次出现在屏幕上，还是会走以下的方法
		 */
		//zanList.clear();
		//if(bean.getUser_heads()!=null){
		zanList=bean.getUser_heads();
			
		/*}else{
			zanList.clear();
		}*/
		zanListCopy=new ArrayList<UserEntity>();
		
		
		/*if(zanList!=null){
			
			holder.gv.setAdapter(new MyAdapter(zanList));
		}*/
		
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
}
