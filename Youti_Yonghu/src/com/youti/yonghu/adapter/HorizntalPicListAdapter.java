package com.youti.yonghu.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.example.youti_geren.R;
import com.youti.appConfig.Constants;
import com.youti.base.BaseAdapter;
import com.youti.base.BaseHolder;
import com.youti.yonghu.bean.PicsBean;



public class HorizntalPicListAdapter extends BaseAdapter<PicsBean, HorizntalPicListAdapter.PicsBeanHolder>{

	
	private Context mContext;
	private List<PicsBean> mDataList ;
	
	public HorizntalPicListAdapter(Context mContext,List<PicsBean> mDataList){
		super(mContext, mDataList);
		this.mDataList = mDataList;
	}

	public class PicsBeanHolder extends BaseHolder {
		public ImageView img;
	}
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.item_imageview_gallary;
	}

	@Override
	protected PicsBeanHolder createHolder(View convertView, int position) {
		PicsBeanHolder holder = new PicsBeanHolder();
		holder.img = (ImageView) convertView.findViewById(R.id.iv_photo);
		return holder;
	}

	@Override
	protected void initHolderByBean(PicsBean bean, PicsBeanHolder holder) {
		imageLoader.displayImage(Constants.PIC_CODE+bean.getImg_url(), holder.img, options);
		
	}
}
