package com.youti.yonghu.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.youti.appConfig.Constants;
import com.youti.base.BaseAdapter;
import com.youti.base.BaseHolder;
import com.youti.yonghu.bean.CourseBean;

public class OtherCourseAdapter  extends BaseAdapter<CourseBean, OtherCourseAdapter.CourseItemBeanHolder> {

	Context mContext;
	List<CourseBean> mList;
	
	public OtherCourseAdapter(Context context, List<CourseBean> list) {
		super(context, list);
		this.mList = list;
	}


	public class CourseItemBeanHolder extends BaseHolder {
		public ImageView img;
		public TextView title;
		public TextView jianjie;
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.other_course_list_item;
	}

	@Override
	protected CourseItemBeanHolder createHolder(View convertView, int position) {
		CourseItemBeanHolder holder = new CourseItemBeanHolder();
		holder.img = (ImageView) convertView.findViewById(R.id.kc_Img);
		holder.title = (TextView) convertView.findViewById(R.id.tv_other_course_title);
		holder.jianjie = (TextView) convertView.findViewById(R.id.tv_other_course_jianjie);
		convertView.setTag(holder);
		return holder;
	}

	
	@Override
	protected void initHolderByBean(CourseBean bean,
			CourseItemBeanHolder holder) {
		imageLoader.displayImage(Constants.PIC_CODE+bean.getImg(), holder.img, options);
		holder.title.setTag(bean.getCourse_id());
		holder.title.setText(bean.getTitle());
		holder.jianjie.setText(bean.getPrese());
	}
}

