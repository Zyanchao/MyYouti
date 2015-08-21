package com.youti.yonghu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.youti_geren.R;

public class HorizntalListViewAdapter extends BaseAdapter{

	
	private Context mContext;
	private List<ImageView> picLists = new ArrayList<ImageView>();
	
	public HorizntalListViewAdapter(Context mContext,List<ImageView> picLists){
		this.mContext = mContext;
		this.picLists = picLists;
		
	}
	@Override
	public int getCount() {
		return picLists.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v =View.inflate(mContext, R.layout.item_imageview_gallary, null);
		ImageView iv=(ImageView) v.findViewById(R.id.iv_photo);
		if(position==picLists.size()-1){
			iv.setBackgroundResource(R.drawable.send_add);
			iv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//showImagePickDialog();
				}
			});
			iv.setBackgroundResource(R.drawable.userhome_pic);
		
	}
		return v;
	}
}
