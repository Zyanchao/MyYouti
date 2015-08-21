package com.youti.yonghu.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.youti.appConfig.Constants;
import com.youti.base.BaseAdapter;
import com.youti.base.BaseHolder;
import com.youti.view.CircleImageView1;
import com.youti.view.MGridView;
import com.youti.yonghu.activity.OtherPersonCenterActivity;
import com.youti.yonghu.bean.Comment;
import com.youti.yonghu.bean.UserEntity;

public class CommentAdapter  extends BaseAdapter<Comment, CommentAdapter.CommentHolder>{

	
	private Context mContext;
	private List<Comment> mDataList ;
	
	public CommentAdapter(Context mContext,List<Comment> mDataList){
		super(mContext, mDataList);
		this.mContext = mContext;
		this.mDataList = mDataList;
	}

	public class CommentHolder extends BaseHolder {
		public LinearLayout llViewGroup;
		public ImageView img;
		/*public ImageView img1;
		public ImageView img2;
		public ImageView img3;*/
		public MGridView gv;
		public TextView name;
		public TextView content;
		public TextView time;
	}
	@Override
	protected int getLayoutId() {
		return R.layout.item_coach_pinglun;
	}

	@Override
	protected CommentHolder createHolder(View convertView, int position) {
		CommentHolder holder = new CommentHolder();
		holder.img = (ImageView) convertView.findViewById(R.id.iv);
		/*holder.img1 = (ImageView) convertView.findViewById(R.id.img_one);
		holder.img2 = (ImageView) convertView.findViewById(R.id.img_two);
		holder.img3 = (ImageView) convertView.findViewById(R.id.img_three);*/
		holder.llViewGroup = (LinearLayout) convertView.findViewById(R.id.viewGroup);
		holder.gv=(MGridView) convertView.findViewById(R.id.gv_pinglin);
		holder.name = (TextView) convertView.findViewById(R.id.tv_name);
		holder.content = (TextView) convertView.findViewById(R.id.tv_content);
		holder.time = (TextView) convertView.findViewById(R.id.tv_time);
		return holder;
	}

	@Override
	protected void initHolderByBean(Comment bean, CommentHolder holder) {
		imageLoader.displayImage(Constants.PIC_CODE+bean.getHead_img(), holder.img, options);
		holder.name.setText(bean.getUser_name());
		holder.content.setText(bean.getContent());
		holder.time.setText(bean.getAddtime());
		int picSize;
		String pic[] = bean.getComment_img();
		if(pic==null){
			picSize = 0;
			holder.llViewGroup.setVisibility(View.GONE);
		}else{
			picSize= pic.length;
			holder.gv.setAdapter(new MyAdapter(pic));
		}
	}
	public class MyAdapter extends android.widget.BaseAdapter{
		String [] pics;
		public MyAdapter(String [] pics){
			this.pics=pics;
		}
		@Override
		public int getCount() {
				return pics.length;
		}

		@Override
		public Object getItem(int position) {
			return pics[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v =View.inflate(mContext, R.layout.item_image_pinglun, null);
			ImageView iv =(ImageView) v.findViewById(R.id.iv);
			imageLoader.displayImage(Constants.PIC_CODE+pics[position], iv,options);
			return v;					
		}
		
	}	

}