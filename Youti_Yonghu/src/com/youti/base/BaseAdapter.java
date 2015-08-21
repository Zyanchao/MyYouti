package com.youti.base;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ab.bitmap.AbImageDownloader;
import com.example.youti_geren.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class BaseAdapter<T1, T2 extends BaseHolder> extends
		android.widget.BaseAdapter {
	protected Context mContext;
	protected List<T1> mList;
	protected AbImageDownloader mImgLoad;

	protected ImageLoader imageLoader;
	protected DisplayImageOptions options;
	public BaseAdapter(Context context, List<T1> list) {
		this.mContext = context;
		this.mList = list;
		mImgLoad = new AbImageDownloader(context);
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

	public void addAndRefreshListView(List<T1> list) {
		if(this.mList==null){
			this.mList=new ArrayList<T1>();
		}
		this.mList .addAll(list);
		notifyDataSetChanged();
		
	}
	
	private void refreshListView(List<T1> list) {
		if(this.mList==null){
			this.mList=new ArrayList<T1>();
		}else{
			this.mList.clear();
		}
		this.mList .addAll(list);
		notifyDataSetChanged();
		
	}
	
	
	protected abstract int getLayoutId();

	protected abstract T2 createHolder(View convertView,int position);

	protected abstract void initHolderByBean(T1 bean, T2 holder);

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			int id = getLayoutId();
			convertView = View.inflate(mContext, id, null);
		}
		
		T2 holder = (T2) convertView.getTag();
		if (holder == null) {
			holder = createHolder(convertView,position);
			convertView.setTag(holder);
		}
		T1 bean = (T1) getItem(position);
		initHolderByBean(bean, holder);
		return convertView;

	}
	
	/**
	 * 
	 * @param view
	 * @param imgUrl
	 * @param errorImgId
	 * @param loadingImgId
	 */
	protected void loadImage(ImageView view,String imgUrl,int errorImgId,int loadingImgId){
		mImgLoad.setLoadingImage(loadingImgId);
		mImgLoad.setErrorImage(errorImgId);
		mImgLoad.display(view,imgUrl);
		//mImgLoad.setHeight(height)
		//mImgLoad.setWidth(width)

	}
	
	/**
	 * 
	 * @param view
	 * @param imgUrl
	 * @param errorImgId
	 * @param loadingImgId
	 */
	protected void loadImage(ImageView view,String imgUrl,int errorImgId,int loadingImgId,int height,int width){
		mImgLoad.setLoadingImage(loadingImgId);
		mImgLoad.setErrorImage(errorImgId);
		mImgLoad.display(view,imgUrl);
		mImgLoad.setHeight(height);
		mImgLoad.setWidth(width);

	}
}
