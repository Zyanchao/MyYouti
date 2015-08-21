package com.youti.yonghu.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.youti.yonghu.bean.City;

public class CityAdapter extends BaseAdapter {

	private final Context mContext;
	private final ArrayList<City> names;

	public CityAdapter(Context mContext, ArrayList<City> names) {
		this.mContext = mContext;
		this.names = names;
	}

	@Override
	public int getCount() {
		return names.size();
	}

	@Override
	public Object getItem(int position) {
		return names.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = convertView;
		if(convertView == null){
			view = View.inflate(mContext, R.layout.item_list, null);
		}
		ViewHolder mViewHolder = ViewHolder.getHolder(view);
		
		City friend = names.get(position);
		
		// 跟上一个进行比较，如果不同，则显示。
				String letter = null;
				String currentLetter = friend.getPinyin().charAt(0) + "";
				if(position == 0){
					// 第一个人直接显示
					letter = currentLetter;
				}else {
					// 获取上一个人的拼音
			String preLetter = names.get(position - 1).getPinyin().charAt(0) + "";
			if(!TextUtils.equals(preLetter, currentLetter)){
				letter = currentLetter;
			}
		}
		
		mViewHolder.mIndex.setVisibility(letter == null ? View.GONE : View.VISIBLE);
		if(letter != null){
			mViewHolder.mIndex.setText(letter);
		}
		
		mViewHolder.mName.setText(friend.getName());
		return view;
	}
	
	static class ViewHolder{

		TextView mIndex;
		TextView mName;
		
		public static ViewHolder getHolder(View view) {
			Object tag = view.getTag();
			if(tag != null){
				return (ViewHolder)tag;
			}else {
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.mIndex = (TextView) view.findViewById(R.id.tv_index);
				viewHolder.mName = (TextView) view.findViewById(R.id.tv_name);
				return viewHolder;
			}
		}
		
	}

}
