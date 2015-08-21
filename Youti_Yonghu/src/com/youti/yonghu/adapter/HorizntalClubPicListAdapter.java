package com.youti.yonghu.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.example.youti_geren.R;
import com.youti.appConfig.Constants;
import com.youti.base.BaseAdapter;
import com.youti.base.BaseHolder;
import com.youti.yonghu.bean.ClubBean.ClubPhoto;



public class HorizntalClubPicListAdapter extends BaseAdapter<ClubPhoto, HorizntalClubPicListAdapter.ClubPhotoHolder>{

	
	private Context mContext;
	private List<ClubPhoto> mDataList ;
	
	public HorizntalClubPicListAdapter(Context mContext,List<ClubPhoto> mDataList){
		super(mContext, mDataList);
		this.mDataList = mDataList;
	}

	public class ClubPhotoHolder extends BaseHolder {
		public ImageView img;
	}
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.item_imageview_gallary;
	}

	@Override
	protected ClubPhotoHolder createHolder(View convertView, int position) {
		ClubPhotoHolder holder = new ClubPhotoHolder();
		holder.img = (ImageView) convertView.findViewById(R.id.iv_photo);
		return holder;
	}

	@Override
	protected void initHolderByBean(ClubPhoto bean, ClubPhotoHolder holder) {
		imageLoader.displayImage(Constants.PIC_CODE+bean.getImg_url(), holder.img, options);
		
	}
}
