package com.youti.yonghu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.bitmap.AbImageDownloader;
import com.example.youti_geren.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youti.appConfig.Constants;
import com.youti.base.BaseAdapter;
import com.youti.base.BaseHolder;
import com.youti.view.CircleImageView1;
import com.youti.yonghu.activity.CommentActivity;
import com.youti.yonghu.bean.CoachBean;
import com.youti.yonghu.bean.UserEntity;

public class CoachListAdapter extends BaseAdapter<CoachBean,CoachListAdapter.CoachHolder > {

	private Context mContext;
	private List<CoachBean> mList;

	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	AbImageDownloader abImageDownloader;
	
	public CoachListAdapter(Context mContext, List<CoachBean> list) {
		super(mContext, list);
		this.mContext = mContext;
		this.mList = list;
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.buy_weichat)// 正在加载
				.showImageForEmptyUri(R.drawable.buy_weichat)// 空图片
				.showImageOnFail(R.drawable.buy_weichat)// 错误图片
				.cacheOnDisk(true)//设置硬盘缓存
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
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
		public ImageView img;
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
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.coach_list_item;
	}



	@Override
	protected CoachHolder createHolder(View convertView, int position) {
		CoachHolder holder = new CoachHolder();
		holder.price = (TextView) convertView.findViewById(R.id.coach_tv_price);
		holder.img = (ImageView) convertView.findViewById(R.id.kc_Img);
		holder.name = (TextView) convertView.findViewById(R.id.coach_tv_name);
		holder.project = (TextView) convertView.findViewById(R.id.coach_tv_project);
		holder.distance = (TextView) convertView.findViewById(R.id.coach_tv_distance);
		holder.desc = (TextView) convertView.findViewById(R.id.coach_tv_desc);
		holder.zan = (TextView) convertView.findViewById(R.id.zan);
		holder.pl = (TextView) convertView.findViewById(R.id.pinglun);
		holder.zanPerson = (LinearLayout) convertView.findViewById(R.id.ll_zan);
		holder.plPerson = (LinearLayout) convertView.findViewById(R.id.ll_pinglun);
		holder.iv_user_head = (CircleImageView1) convertView.findViewById(R.id.iv_user_head);
		holder.llzanimgs = (LinearLayout) convertView.findViewById(R.id.pinglun_zan_users);
		convertView.setTag(holder);
		
	return holder;
	}


	@Override
	protected void initHolderByBean(CoachBean bean, final CoachHolder holder) {
		
		imageLoader.displayImage(Constants.PIC_CODE+bean.getHead_img(), holder.img, options);
		holder.price.setTag(bean.getCoach_id());
		holder.price.setText(bean.getPrice()+"元");
		holder.project.setText(bean.getVal());
		holder.desc.setText(bean.getSign());
		holder.distance.setText(bean.getDistance()+"km");
		holder.zan.setText(bean.getPraise_num()+"");
		holder.pl.setText(bean.getComment_num()+"");
		final List<UserEntity> userPic = bean.getUser_heads();
		if(userPic!=null){
			int picSize = userPic.size();
			if(picSize>0){
			ImageView [] imgs =  new ImageView[picSize];
			for(int i=0;i<picSize;i++){
			ImageView imageView = new ImageView(mContext);
			imageView.setPadding(10, 5, 10, 5);
			/*String url = "";
			url = pic[i].toString();*/
			imageView.setLayoutParams(new LayoutParams(150,150));
			imgs[i] = imageView;
			imageLoader.displayImage(Constants.PIC_CODE+userPic.get(i).toString(), imageView,options);
			holder.llzanimgs.addView(imageView);
			}
			}
		}
		
		holder.zanPerson.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ImageView imageView = new ImageView(mContext);
				imageLoader.displayImage(Constants.PIC_CODE+userPic.get(0).toString(), imageView,options);
				holder.llzanimgs.addView(imageView,0);
			}
		});
		
		holder.plPerson.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, CommentActivity.class);
				//intent.putExtra(Constants.KEY_ID, Constants.REQUEST_CODE_COACH);
				intent.putExtra(Constants.KEY_CODE, Constants.REQUEST_CODE_COACH);
				((Activity) mContext).startActivityForResult(intent, Constants.REQUEST_CODE_COACH);
				
			}
		});
		
	}
	
	
	
	
}
