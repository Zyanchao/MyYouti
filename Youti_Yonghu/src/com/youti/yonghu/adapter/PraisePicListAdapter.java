package com.youti.yonghu.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.example.youti_geren.R;
import com.youti.appConfig.Constants;
import com.youti.base.BaseAdapter;
import com.youti.base.BaseHolder;
import com.youti.yonghu.bean.UserEntity;



public class PraisePicListAdapter extends BaseAdapter<UserEntity, PraisePicListAdapter.PraisePicHolder>{

	
	private Context mContext;
	private List<UserEntity> mDataList ;
	
	public PraisePicListAdapter(Context mContext,List<UserEntity> mDataList){
		super(mContext, mDataList);
		this.mDataList = mDataList;
	}

	public class PraisePicHolder extends BaseHolder {
		public ImageView img;
	}
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.item_praise_image;
	}

	@Override
	protected PraisePicHolder createHolder(View convertView, int position) {
		PraisePicHolder holder = new PraisePicHolder();
		holder.img = (ImageView) convertView.findViewById(R.id.iv_head);
		return holder;
	}

	@Override
	protected void initHolderByBean(UserEntity bean, PraisePicHolder holder) {
		imageLoader.displayImage(Constants.PIC_CODE+bean.getHead_img(), holder.img, options);
		holder.img.setTag(bean.getUser_id());
	}
}
