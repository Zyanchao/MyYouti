package com.youti.yonghu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youti.appConfig.Constants;
import com.youti.appConfig.YoutiApplication;
import com.youti.base.BaseAdapter;
import com.youti.base.BaseHolder;
import com.youti.utils.Utils;
import com.youti.view.CircleImageView1;
import com.youti.view.MGridView;
import com.youti.view.XListView;
import com.youti.yonghu.activity.CommentActivity;
import com.youti.yonghu.bean.CourseBean;
import com.youti.yonghu.bean.UserEntity;

public class CourseListAdapter  extends BaseAdapter<CourseBean, CourseListAdapter.CourseItemBeanHolder> {

	Context mContext;
	List<CourseBean> mList;
	String userId;
	String userHeadImageUrl;
	XListView mListView;
	List<UserEntity> zanList = new ArrayList<UserEntity>();
	List<UserEntity> zanListCopy;
	public CourseListAdapter(Context mContext, List<CourseBean> list,XListView mListView) {
		super(mContext, list);
		this.mContext = mContext;
		this.mList = list;
		userId = ((YoutiApplication)(mContext.getApplicationContext())).myPreference.getUserId();
		userHeadImageUrl=((YoutiApplication)(mContext.getApplicationContext())).myPreference.getHeadImgPath();
		this.mListView=mListView;
	}

	public CourseListAdapter(Context mContext, List<CourseBean> list) {
		super(mContext, list);
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
		public TextView studyCount;
		public TextView charge;
		public TextView datetime;
		public TextView distance;
		public TextView zan;
		public TextView pl;
		public LinearLayout zanPerson;
		public LinearLayout plPerson;
		public MGridView gv;
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.course_list_item;
	}

	@Override
	protected CourseItemBeanHolder createHolder(View convertView, int position) {
		CourseItemBeanHolder holder = new CourseItemBeanHolder();
		holder.charge = (TextView) convertView.findViewById(R.id.kc_charge);
		holder.img = (ImageView) convertView.findViewById(R.id.kc_Img);
		holder.studyCount = (TextView) convertView.findViewById(R.id.kc_studyCounts);
		
		holder.datetime = (TextView) convertView.findViewById(R.id.kc_datetime);
		holder.distance = (TextView) convertView.findViewById(R.id.kc_location_distance);
		holder.zan = (TextView) convertView.findViewById(R.id.zan);
		holder.pl = (TextView) convertView.findViewById(R.id.pinglun);
		holder.zanPerson = (LinearLayout) convertView.findViewById(R.id.ll_zan);
		holder.plPerson = (LinearLayout) convertView.findViewById(R.id.ll_pinglun);
		holder.zanPerson.setOnClickListener(new MyOnClicListener(position,holder));
		holder.plPerson.setOnClickListener(new MyOnClicListener(position,holder));
		holder.zan_img=(ImageView) convertView.findViewById(R.id.zan_img);
		holder.gv=(MGridView) convertView.findViewById(R.id.gv);
		convertView.setTag(holder);
		return holder;
	}

	
	@Override
	protected void initHolderByBean(CourseBean bean,
			CourseItemBeanHolder holder) {
		holder.charge.setText(bean.price + "元");
		imageLoader.displayImage(Constants.PIC_CODE+bean.img, holder.img, options);
		holder.studyCount.setText(bean.studentCount + "人学习过");
		holder.studyCount.setTag(bean.getCourse_id());
		holder.datetime.setText(bean.getStart_time());
		holder.distance.setText(bean.distance + "km");
		holder.zan.setText(bean.praise_num+"");
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
		zanList=bean.getUser_heads();
		zanListCopy=new ArrayList<UserEntity>();
		
		
		
		if(zanList!=null){
			if(zanList.size()<7){
				for(int i=zanList.size()-1;i>-1;i--){
					zanListCopy.add(zanList.get(i));
				}
				holder.gv.setAdapter(new MyAdapter(zanListCopy));					
			}else{
				int size=zanList.size();
				for(int i=size-1;i>size-7;i--){
					zanListCopy.add(zanList.get(i));
				}
				holder.gv.setAdapter(new MyAdapter(zanListCopy));
			}				
		}else{
			holder.gv.setAdapter(new MyAdapter(zanListCopy));
		}
	}
	
	
	/**
	 * 局部刷新的方法
	 * @param item
	 */
	public void updateItemData(CourseBean item) {
		Message msg = Message.obtain();
		/*int ids = -1;
		// 进行数据对比获取对应数据在list中的位置
		*//**
		 * 在这里也可以直接将item.getDataId()的值赋值给ids就可以了。
		 * 在ListView的局部刷新Demo中，是在item初始化数据时，就将list.get(i)中的每个对象的id设置为从0到20了，所以可以通过判断是否相等，来取得这个值
		 * 这里没有在最初给id赋值，只是在点击的时候给item.setDataId()，设置了当前的position
		 *//*
		if(item.getDataId()!=0){
			for (int i = 0; i < mList.size(); i++) {
				if (mList.get(i).getDataId() == item.getDataId()) {
					ids = i;
				}
			}
			
		}else{
			ids=0;
		}*/
		
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
		 View view = mListView.getChildAt(index - visiblePosition + 2);
		// 获取点击的view
		 //如果没有添加headerview，则使用以下的方法，获取view
		 //View view = mListView.getChildAt(index-visiblePosition);
		TextView txt = (TextView) view.findViewById(R.id.zan);
		// 获取mDataList.set(ids, item);更新的数据
		CourseBean data = (CourseBean) getItem(index);
		// 重新设置界面显示数据
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
		public View getView(int position, View convertView, ViewGroup parent) {
			View v =View.inflate(mContext, R.layout.item_image1, null);
			CircleImageView1 iv =(CircleImageView1) v.findViewById(R.id.iv);
			ImageLoader.getInstance().displayImage("http://112.126.72.250/ut_app"+list.get(position).head_img, iv, options);
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
				
				CourseBean item =mList.get(position);
				item.getPraise_num();
				item.getCourse_id();
				
				//已经赞过
				Utils.showToast(mContext, position+"......."+item.getPraise()+"&&&&&&"+item.getPraise_num()+"*****"+item.getCourse_id());
				if("1".equals(item.getPraise())&&item.getUser_heads()!=null){
					//该用户已经点过赞，此时取消点赞
					for(int i=0;i<item.getUser_heads().size();i++){
						if(item.getUser_heads().get(i).getUser_id().equals("17")){
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
					vb.setUser_id("17");
					vb.setHead_img(userHeadImageUrl);
					//将封装好的用户对象，添加到集合
					item.getUser_heads().add(0,vb);	
					
					holder.zan_img.setBackgroundResource(R.drawable.sp_heart_h);
					item.setPraise("1");
					item.setPraise_num(item.getUser_heads().size()+"");
				}else{
					//自己是第一个点赞的情况
					UserEntity vb =new UserEntity();
					vb.setUser_id("17");
					vb.setHead_img(userHeadImageUrl);
					//将封装好的用户对象，添加到集合
					zanList.add(0,vb);
					item.setPraise("1");
					item.setPraise_num(zanList.size()+"");
					
				}
				// 更新那个位置的信息
				item.setDataId(position);
				/**
				 * ListView的局部刷新方法。
				 */	
				
				updateItemData(item);
			//	requestPraise(userId, list.get(position).video_id);
			
				break;

			case R.id.ll_pinglun:
				
				Intent intent = new Intent(mContext, CommentActivity.class);
				intent.putExtra("code", Constants.REQUEST_CODE_COURSE);
				((Activity) mContext).startActivityForResult(intent, Constants.REQUEST_CODE_COURSE);
			default:
				break;
			}
		}
		
	}
}
