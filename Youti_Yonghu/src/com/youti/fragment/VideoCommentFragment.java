package com.youti.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;
import com.youti.view.CircleImageView1;
import com.youti.yonghu.activity.VideoDetailActivity;
import com.youti.yonghu.bean.VideoDetailBean.CommentItem;
import com.youti.yonghu.bean.VideoDetailBean.PraiseItem;
import com.youti.yonghu.bean.VideoDetailBean.VideoDetailItem;

public class VideoCommentFragment extends Fragment{
	GridView gv;
	ListView lv;
	MyListAdapter mla;
	MyGridAdapter mga;
	VideoDetailItem videoDetailItemList;
	TextView tv_dianzan;
	//如果是第一个评论的人，需要创建一个集合来装
	private List<CommentItem> commentList=new ArrayList<CommentItem>();
	//点赞的集合
	List<PraiseItem> praiseImageUrlList =new ArrayList<PraiseItem>();
	private DisplayImageOptions options;
	
	public final String mPageName = "VideoCommentFragment";
	 @Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			MobclickAgent.onPageEnd( mPageName );
		}


		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			MobclickAgent.onPageStart( mPageName );	
		}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v=View.inflate(getActivity(), R.layout.item_videocomment, null);
		gv=(GridView) v.findViewById(R.id.gv_dianzan);
		lv=(ListView) v.findViewById(R.id.lv_comment);
		tv_dianzan=(TextView) v.findViewById(R.id.tv_dianzan);
		
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()  
        .cacheInMemory().cacheOnDisc().build();  

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(  
        getActivity()).defaultDisplayImageOptions(defaultOptions)  
        .threadPriority(Thread.NORM_PRIORITY - 2)  
        .denyCacheImageMultipleSizesInMemory()  
        .discCacheFileNameGenerator(new Md5FileNameGenerator())  
        .tasksProcessingOrder(QueueProcessingType.LIFO).build();  
		ImageLoader.getInstance().init(config);  
		
		
		options = new DisplayImageOptions.Builder()    
        .showStubImage(R.drawable.sq_head)          // 设置图片下载期间显示的图片    
        .showImageForEmptyUri(R.drawable.empty_photo)  // 设置图片Uri为空或是错误的时候显示的图片    
        .showImageOnFail(R.drawable.widget_dface)       // 设置图片加载或解码过程中发生错误显示的图片        
        .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中    
        .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中    
        .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片    
        .build();
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		videoDetailItemList = ((VideoDetailActivity)getActivity()).getVideoDetailItemList();
		commentList = videoDetailItemList.comment;
		praiseImageUrlList=videoDetailItemList.praise;
		
		if(praiseImageUrlList!=null){
			if(mga==null){
				mga=new MyGridAdapter();
				gv.setAdapter(mga);		
				
			}else{
				mga.notifyDataSetChanged();
			}
		}
		if(commentList!=null){
			if(mla==null){
				mla=new MyListAdapter();
				lv.setAdapter(mla);
				
			}else{
				mla.notifyDataSetChanged();
			}			
		}
		
		tv_dianzan.setText(videoDetailItemList.praise_num+"人点赞");
	}
	public ListView getListView(){
		return lv;
	}
	
	public GridView getGridView(){
		return gv;
	}
	public List<CommentItem> getCommentList(){
		return commentList;
	}
	
	public List<PraiseItem> getPraiseList(){
		return praiseImageUrlList;
	}
	public void notifyDataSetChanged(){
		mla.notifyDataSetChanged();
		mga.notifyDataSetChanged();
	}
	
	class MyListAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			return commentList.size();
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
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			CommentHolder ch;
			if(convertView==null){
				view =View.inflate(getActivity(), R.layout.item_pinglun, null);
				ch=new CommentHolder();
				ch.iv=(CircleImageView1) view.findViewById(R.id.iv);
				ch.tv_name=(TextView) view.findViewById(R.id.tv_name);
				ch.tv_content=(TextView) view.findViewById(R.id.tv_content);
				ch.tv_time=(TextView) view.findViewById(R.id.tv_time);
				view.setTag(ch);
			}else{
				view=convertView;
				ch=(CommentHolder) view.getTag();
			}
			ch.tv_content.setText(commentList.get(commentList.size()-position-1).comment_content);
			ch.tv_name.setText(commentList.get(commentList.size()-position-1).user_name);
			ch.tv_time.setText(commentList.get(commentList.size()-position-1).comment_time);
			
			
			
//
//			AbsListView.LayoutParams params1 = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
//			view.setLayoutParams(params1);
			return view;
		};
	}
	
	class CommentHolder{
		CircleImageView1 iv;
		TextView tv_name,tv_content,tv_time;
	}
	
	class MyGridAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return praiseImageUrlList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@SuppressLint("NewApi") 
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v=View.inflate(getActivity(), R.layout.item_image1, null);
			CircleImageView1 civ =(CircleImageView1) v.findViewById(R.id.iv);
			ImageLoader.getInstance().displayImage(praiseImageUrlList.get(praiseImageUrlList.size()-position-1).user_img, civ, options);
			
			//ImageLoader.getInstance().displayImage("http://112.126.72.250/ut_app/video/img/video_sewq.png", civ, options);
			
			/*ImageView iv =new ImageView(getActivity());
			//iv.setBackgroundDrawable(praiseImageUrlList.get(position));
			AbsListView.LayoutParams params = new AbsListView.LayoutParams(75, 75);
			iv.setLayoutParams(params );*/
			return v;
		}
		
	}
}
