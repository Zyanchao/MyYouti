package com.youti.yonghu.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youti.appConfig.Constants;
import com.youti.base.BaseHolder;
import com.youti.yonghu.bean.CoachBean;
import com.youti.yonghu.bean.CoachTopBean;

public class CoachTouTiaoListAdapter extends  BaseAdapter {

	private Context mContext;
	private List<CoachBean> mList;
	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	public CoachTouTiaoListAdapter(Context mContext,List<CoachBean> mList){
		this.mList = mList;
		this.mContext = mContext;
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.user_lbk)// 正在加载
				.showImageForEmptyUri(R.drawable.user_lbk)// 空图片
				.showImageOnFail(R.drawable.user_lbk)// 错误图片
				.cacheInMemory(true)//设置 内存缓存
				.cacheOnDisk(true)//设置硬盘缓存
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CoachHolder holder ;
		View view;
		if(convertView==null){
			view =View.inflate(mContext, R.layout.item_coach_toutiao, null);
			holder = new CoachHolder();
			holder.head_image = (ImageView) view.findViewById(R.id.img_headportrait);
			
			holder.name = (TextView) view.findViewById(R.id.tv_name);
			holder.val = (TextView) view.findViewById(R.id.tv_val);
			holder.topnum = (TextView) view.findViewById(R.id.tv_top_num);
			holder.address = (TextView) view.findViewById(R.id.tv_address_city);
			holder.zan = (TextView) view.findViewById(R.id.tv_zan_count);
			view.setTag(holder);
		}else {
			view=convertView;
			holder=(CoachHolder) view.getTag();
		}
		CoachBean bean =mList.get(position);
		imageLoader.displayImage(Constants.PIC_CODE+bean.getHead_img(), holder.head_image, options);
		holder.top_image = (ImageView) view.findViewById(R.id.iv_top_num);
		if(position==0){
			holder.top_image.setVisibility(View.VISIBLE);
			holder.top_image.setBackgroundResource(R.drawable.toutiao_top2);
		}else if(position==1){
			holder.top_image.setVisibility(View.VISIBLE);
			holder.top_image.setBackgroundResource(R.drawable.toutiao_top3);
		}else {
			holder.top_image.setVisibility(View.GONE);
		}
		holder.name.setTag(bean);
		holder.name.setText(bean.getCoach_name());
		holder.topnum.setText(position+2+"");
		holder.val.setText(bean.getProject_type());
		holder.address.setText(bean.getServer_province());
		holder.zan.setText(bean.getPraise_num()+"");
		return view;
	}

	public class CoachHolder extends BaseHolder {
		public ImageView head_image;
		public ImageView top_image;
		public TextView name;//教练名称
		public TextView topnum;//名次
		public TextView val;//项目
		public TextView address;//距离
		public TextView zan;
	}


	/*private Context mContext;
	private List<CoachTopBean> mList;

	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	
	public CoachTouTiaoListAdapter(Context context, List<CoachTopBean> list) {
		super(context, list);
		this.mList = list;
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.user_lbk)// 正在加载
				.showImageForEmptyUri(R.drawable.user_lbk)// 空图片
				.showImageOnFail(R.drawable.user_lbk)// 错误图片
				.cacheInMemory(true)//设置 内存缓存
				.cacheOnDisk(true)//设置硬盘缓存
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	
	@Override
	protected int getLayoutId() {
		return R.layout.item_coach_toutiao;
	}



	@Override
	protected CoachHolder createHolder(View convertView, int position) {
		CoachHolder holder = new CoachHolder();
		holder.head_image = (ImageView) convertView.findViewById(R.id.img_headportrait);
		holder.top_image = (ImageView) convertView.findViewById(R.id.iv_top_num);
		if(position==0){
			holder.top_image.setVisibility(View.VISIBLE);
			holder.top_image.setBackgroundResource(R.drawable.toutiao_top2);
		}else if(position==1){
			holder.top_image.setVisibility(View.VISIBLE);
			holder.top_image.setBackgroundResource(R.drawable.toutiao_top3);
		}
		
		holder.name = (TextView) convertView.findViewById(R.id.tv_name);
		holder.val = (TextView) convertView.findViewById(R.id.tv_val);
		holder.topnum = (TextView) convertView.findViewById(R.id.tv_top_num);
		holder.address = (TextView) convertView.findViewById(R.id.tv_address_city);
		holder.zan = (TextView) convertView.findViewById(R.id.tv_zan_count);
		convertView.setTag(holder);
	return holder;
	}
*/



}
