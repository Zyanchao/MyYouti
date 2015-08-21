package com.youti.yonghu.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.youti.base.BaseAdapter;
import com.youti.base.BaseHolder;
import com.youti.yonghu.bean.HotCourse;

/**
 * 
* @ClassName;// HotCourseAdapter 
* @Description;// TODO(热门教练 适配器) 
* @author zychao 
* @date 2015-6-4 下午3;//34;//29 
*
 */
public class HotCourseAdapter extends BaseAdapter<HotCourse, HotCourseAdapter.HotCourseHolder >{

	
	/**
	* <p>Description;//构造函数 </p> 
	* @param context
	* @param list
	 */
	public HotCourseAdapter(Context context, List<HotCourse> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.new_itme;
	}

	@Override
	protected HotCourseHolder createHolder(View convertView, int position) {
		HotCourseHolder holder = new HotCourseHolder();
		holder.img = (ImageView) convertView.findViewById(R.id.img_hot);
		holder.praise = (ImageView) convertView.findViewById(R.id.zan_img);
		return holder;
	}

	@Override
	protected void initHolderByBean(HotCourse bean, HotCourseHolder holder) {
		//loadImage(holder.img, bean.getJson_img(), R.drawable.user_lbk,R.drawable.user_lbk);
		
	}
	
	public class HotCourseHolder extends BaseHolder {
		//private course_id;//课程id
        private ImageView img;//图片路径
        private ImageView praise;
        private ImageView comment;
        private TextView praise_num;//点赞数
        private TextView comment_num;//评论数
        //json_img;//点赞的用户头像
	}

	
	
}
