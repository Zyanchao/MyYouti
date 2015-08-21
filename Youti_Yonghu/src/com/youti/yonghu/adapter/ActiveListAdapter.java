package com.youti.yonghu.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youti.appConfig.Constants;
import com.youti.base.BaseAdapter;
import com.youti.base.BaseHolder;
import com.youti.yonghu.bean.SingBean;

public class ActiveListAdapter extends BaseAdapter<SingBean,ActiveListAdapter.CoachHolder > {

	private Context mContext;
	private List<SingBean> mList;

	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	
	public ActiveListAdapter(Context context, List<SingBean> list) {
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

	

	
	/*public void addAndRefreshListView(List<User> lists) {
		if(mList==null){
			this.mList=new ArrayList<User>();
		}
		this.mList .addAll(lists);
		notifyDataSetChanged();

	}
	
	public void refreshListView(List<User> lists) {
		if(this.mList==null){
			this.mList=new ArrayList<User>();
		}else{
			this.mList.clear();
		}
		this.mList .addAll(lists);
		notifyDataSetChanged();
	}
	*/
	

	public class CoachHolder extends BaseHolder {
		public ImageView head_image;
		public TextView time;//教练名称
		public TextView name;//教练名称
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_active;
	}

	@Override
	protected CoachHolder createHolder(View convertView, int position) {
		CoachHolder holder = new CoachHolder();
		holder.head_image = (ImageView) convertView.findViewById(R.id.img_headportrait);
		holder.time = (TextView) convertView.findViewById(R.id.tv_alr_time);
		holder.name = (TextView) convertView.findViewById(R.id.tv_name);
		convertView.setTag(holder);
	return holder;
	}


	@Override
	protected void initHolderByBean(SingBean bean, CoachHolder holder) {
		
		imageLoader.displayImage(Constants.PIC_CODE+bean.getHead_img(), holder.head_image, options);
		holder.name.setTag(bean.getUser_name());
		holder.name.setText(bean.getUser_name());
		holder.time.setText(bean.getSign_time());
		
	}
}
