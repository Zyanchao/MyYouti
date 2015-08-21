package com.youti.yonghu.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.youti.base.BaseAdapter;
import com.youti.base.BaseHolder;
import com.youti.yonghu.bean.FilterRegion;

public class FilterRegionAdapter extends BaseAdapter<FilterRegion, FilterRegionAdapter.FilterValHolder >{

	
	public FilterRegionAdapter(Context context, List<FilterRegion> list) {
		super(context, list);
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.filter_pop_itme;
	}

	@Override
	protected FilterValHolder createHolder(View convertView,int postion) {
		FilterValHolder holder = new FilterValHolder();
		if(postion==0){
			holder.rlItem = (RelativeLayout) convertView.findViewById(R.id.rl_item);
			convertView.setBackgroundResource(R.color.chosed_ll_buttom);
		}
		holder.name = (TextView) convertView.findViewById(R.id.tv_item_name);
		return holder;
	}
	@Override
	protected void initHolderByBean(FilterRegion bean, FilterValHolder holder) {
		holder.name.setText(bean.getRegion_name());
		holder.name.setTag(bean.getRegion_name());
		
	}
	
	public class FilterValHolder extends BaseHolder {
		public RelativeLayout rlItem;
		/** id **/
		public TextView id;
		/** 名称 **/
		public TextView name;
	}

}
