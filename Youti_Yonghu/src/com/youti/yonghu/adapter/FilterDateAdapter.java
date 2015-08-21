package com.youti.yonghu.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.youti.utils.Utils;
import com.youti.yonghu.bean.FilterDate;
import com.youti.yonghu.bean.FilterWeek;

public class FilterDateAdapter extends BaseAdapter {

	List<FilterWeek> list;
	Context context;
	private FilterDate fd;
	String format2;
	private String dateTime;

	public FilterDateAdapter(Context context,List<FilterWeek> list) {
		this.context = context;
		this.list=list;
		

	}

	public class FilterDateHolder {
		public RelativeLayout rlItem;
		public TextView tv_date;
		public TextView tv_week;
		
	}

	@Override
	public int getCount() {
		return list.size()+1;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		
		View v = View.inflate(context, R.layout.pop_date, null);
		TextView tv_week = (TextView) v.findViewById(R.id.tv_week);
		TextView tv_date = (TextView) v.findViewById(R.id.tv_date);
		RelativeLayout rlItem =(RelativeLayout) v.findViewById(R.id.rl_item);

		v.setTag(position);
		
		if(position==0){
			rlItem.removeAllViews();
			RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
			//layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
			//rlItem.setGravity(Gravity.CENTER);
			TextView tv=new TextView(context);
			
			tv.setLayoutParams(layoutParams);
			rlItem.setBackgroundColor(Color.parseColor("#ffdb55"));
			tv.setText("全部");
			tv.setGravity(Gravity.CENTER);
			tv.setTextSize(20);
			tv.setTextColor(Color.parseColor("#ffffff"));
			rlItem.addView(tv,layoutParams);
		}else{
			tv_week.setText(list.get(position-1).getWek());
			tv_date.setText(list.get(position-1).getMd());
			tv_date.setTag(list.get(position-1).getYmd());
		}
		/*v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Utils.showToast(context, list.get(position).wholdDate);
			}
		});*/
		
		return v;
	}

}
