package com.youti.yonghu.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.youti.base.BaseAdapter;
import com.youti.base.BaseHolder;
import com.youti.yonghu.bean.FilterItem;

public class FilterItenAdapter extends BaseAdapter<FilterItem, FilterItenAdapter.FilterItemHolder >{

	
	public FilterItenAdapter(Context context, List<FilterItem> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.filter_pop_itme;
	}

	@Override
	protected FilterItemHolder createHolder(View convertView,int postion) {
		FilterItemHolder holder = new FilterItemHolder();
		if(postion==0){
			holder.rlItem = (RelativeLayout) convertView.findViewById(R.id.rl_item);
			convertView.setBackgroundResource(R.color.chosed_ll_buttom);
		}
		holder.name = (TextView) convertView.findViewById(R.id.tv_item_name);
		return holder;
	}
	@Override
	protected void initHolderByBean(FilterItem bean, FilterItemHolder holder) {
		holder.name.setText(bean.getItemName());
		holder.name.setTag(bean);
		
	}
	
	public class FilterItemHolder extends BaseHolder {
		public RelativeLayout rlItem;
		/** id **/
		public TextView id;
		/** 名称 **/
		public TextView name;
	}

	

	

}
