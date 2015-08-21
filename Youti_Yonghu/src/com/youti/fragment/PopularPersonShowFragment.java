package com.youti.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
import com.youti.utils.android.bitmapfun.ImageFetcher;
import com.youti.view.CircleImageView1;
import com.youti.view.waterfallview.ImageListView;
import com.youti.view.waterfallview.ImageListView.IXListViewListener;
import com.youti.view.waterfallview.PLA_AdapterView;
import com.youti.view.waterfallview.PLA_AdapterView.OnItemClickListener;
import com.youti.yonghu.activity.PhotoDetailActivity;
import com.youti.yonghu.bean.Picture;
import com.youti.yonghu.bean.Picture.PicItem;

public class PopularPersonShowFragment extends Fragment implements IXListViewListener {

	private ImageFetcher mImageFetcher;
	private ImageListView mAdapterView = null;
	private StaggeredAdapter mAdapter = null;
	private int currentPage = 1;

	private Picture picture;

	private LinkedList<PicItem> mInfos;

	public final String mPageName="PopularPersonShowFragment";
	 @Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			MobclickAgent.onPageEnd( mPageName );
		}

	public class StaggeredAdapter extends BaseAdapter {
			
		
		private Context mContext;
		private ImageListView mListView;
		ImageLoader imageLoader;
		private DisplayImageOptions options;
		
		public StaggeredAdapter(Context context, ImageListView xListView){
			mContext = context;
			mInfos = new LinkedList<PicItem>();
			mListView = xListView;
			DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()  
            .cacheInMemory().cacheOnDisc().build();  

			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(  
            context).defaultDisplayImageOptions(defaultOptions)  
            .threadPriority(Thread.NORM_PRIORITY - 2)  
            .denyCacheImageMultipleSizesInMemory()  
            .discCacheFileNameGenerator(new Md5FileNameGenerator())  
            .tasksProcessingOrder(QueueProcessingType.LIFO).build();  
			ImageLoader.getInstance().init(config);  
			
			options = new DisplayImageOptions.Builder()    
            .showStubImage(R.drawable.sq_head)          // 设置图片下载期间显示的图片    
            .showImageForEmptyUri(R.drawable.default_pic)  // 设置图片Uri为空或是错误的时候显示的图片    
            .showImageOnFail(R.drawable.default_pic)       // 设置图片加载或解码过程中发生错误显示的图片        
            .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中    
            .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中    
            .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片    
            .build();
  
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			PicItem duitangInfo = mInfos.get(position);

			if (convertView == null) {
				convertView = View.inflate(parent.getContext(),R.layout.infos_list, null);
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView.findViewById(R.id.news_pic);
				holder.contentView = (TextView) convertView.findViewById(R.id.news_title);
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tv_pinglun = (TextView) convertView.findViewById(R.id.tv_pinglun);
				holder.tv_zan = (TextView) convertView.findViewById(R.id.tv_zan);
				holder.iv_head = (CircleImageView1) convertView.findViewById(R.id.iv_head);
				convertView.setTag(holder);
			}

			holder = (ViewHolder) convertView.getTag();
			
			holder.imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
			
			
			holder.tv_name.setText(duitangInfo.user_name);
			holder.tv_pinglun.setText(duitangInfo.comment_num);
			holder.tv_zan.setText(duitangInfo.praise_num);
			ImageLoader.getInstance().displayImage(duitangInfo.user_img, holder.iv_head, options);    
			holder.contentView.setText(duitangInfo.content);
			mImageFetcher.loadImage("http://112.126.72.250/ut_app"+duitangInfo.json_img,holder.imageView);
			return convertView;
		}

		   /** 
	     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
	     */  
	    public  int dip2px(Context context, float dpValue) {  
	        final float scale = context.getResources().getDisplayMetrics().density;  
	        return (int) (dpValue * scale + 0.5f);  
	    }  
	  
	    /** 
	     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
	     */  
	    public  int px2dip(Context context, float pxValue) {  
	        final float scale = context.getResources().getDisplayMetrics().density;  
	        return (int) (pxValue / scale + 0.5f);  
	    }  
	    
		class ViewHolder {
			ImageView imageView;
			TextView contentView;
			TextView timeView;
			TextView tv_name, tv_pinglun, tv_zan;
			CircleImageView1 iv_head;
		}

		@Override
		public int getCount() {
			return mInfos.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mInfos.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		public void addItemLast(List<PicItem> datas) {
			mInfos.addAll(datas);
		}

		public void addItemTop(List<PicItem> datas) {
			for (PicItem info : datas) {
				mInfos.addFirst(info);
			}
		}
	}

	//ProgressBar pb;
	//FrameLayout fl;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = View.inflate(getActivity(),
				R.layout.act_pull_to_refresh_sample, null);
		mAdapterView = (ImageListView) v.findViewById(R.id.list);
	//	pb = (ProgressBar) v.findViewById(R.id.pb);
	//	fl = (FrameLayout) v.findViewById(R.id.fl);
		//mAdapterView.set
		mAdapterView.setPullLoadEnable(true);
		mAdapterView.setPullRefreshEnable(true);
		mAdapterView.setXListViewListener(PopularPersonShowFragment.this);
		mAdapter = new StaggeredAdapter(getActivity(), mAdapterView);

		mImageFetcher = new ImageFetcher(getActivity(), 500);
		mImageFetcher.setLoadingImage(R.drawable.default_sq);

		return v;
	}

	public final int ERROR = 100003;
	public final int LOADING = 100004;

/*	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ERROR:
				// fl.removeAllViews();
				//pb.setVisibility(View.GONE);
				View v = View.inflate(getActivity(),R.layout.layout_error, null);
			//	fl.addView(v);
				break;
			case LOADING:
			//	pb.setVisibility(View.GONE);
				break;

			}
		};
	};*/

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//mImageFetcher.setExitTasksEarly(false);
		
		/**
		 * 第一次设置数据
		 */
		mAdapterView.setAdapter(mAdapter);
        
		AddItemToContainer(1, 2);
		mAdapterView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {
			//	Utils.showToast(getActivity(), "" + position);
				Intent intent = new Intent(getActivity(),PhotoDetailActivity.class);
				//这里由于添加了listView刷新头，因此position的值多1
				if(position>0){
					intent.putExtra("social_id", mInfos.get(position-1).social_id);	
					intent.putExtra("author_id", mInfos.get(position-1).user_id);
				}
				getActivity().startActivity(intent);
			}
		});
	}

	/**
	 * 添加内容
	 * 
	 * @param pageindex
	 * @param type 1为下拉刷新 2为加载更多
	 */
	private void AddItemToContainer(int pageindex, final int type) {

		String url = "http://112.126.72.250/ut_app/index.php?m=Community&a=showlist";
		RequestParams params = new RequestParams();
		params.put("page", currentPage);
		params.put("user_type", "2");
		HttpUtils.post(url, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				try {
					Gson gson = new Gson();
					picture = gson.fromJson(arg2, Picture.class);
					if("-1".equals(picture.code)){
						Utils.showToast(getActivity(), "没有更多数据了");
						mAdapterView.stopLoadMore();
					}else if("0".equals(picture.code)){
						Utils.showToast(getActivity(), "数据库错误");
						mAdapterView.stopLoadMore();
						mAdapterView.stopRefresh();
					}else{
						setData(type);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) {
				Utils.showToast(getActivity(), "网络连接异常");
			}
		});
	}

	private void setData(int type) {
		if (picture != null) {
			if (type == 1) {
			//	mAdapter.addItemTop(picture.list);
				mAdapter.notifyDataSetChanged();
				mAdapterView.stopRefresh();
			} else if (type == 2) {
				//mInfos.clear();
				mAdapterView.stopLoadMore();
				mAdapter.addItemLast(picture.list);
				mAdapter.notifyDataSetChanged();
			}
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		mImageFetcher.setExitTasksEarly(false);
		//AddItemToContainer(1, 2);
		//mAdapterView.setAdapter(mAdapter);
		if(mAdapter!=null){
			mAdapter.notifyDataSetChanged();
			//Utils.showToast(getActivity(), "onResume");
		}
		MobclickAgent.onPageStart( mPageName );	
	}

	@Override
	public void onRefresh() {
		 AddItemToContainer(currentPage, 1);
		 mAdapterView.stopRefresh();
		 
		 handler.postDelayed(new Runnable() {//处理耗时 操作
				

				@Override
				public void run() {
					  Date currentTime = new Date();
					  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
					  String dateString = formatter.format(currentTime);
					  AddItemToContainer(currentPage, 1);
					  mAdapterView.stopRefresh();	 
					  mAdapterView.setRefreshTime(dateString);  
				}
			}, 1000);
			
		}
		 
		 
		 
	
	
	Handler handler = new Handler();
	@Override
	public void onLoadMore() {
		
		handler.postDelayed(new Runnable() {//处理耗时 操作
			@Override
			public void run() {
				AddItemToContainer(++currentPage, 2);
				mAdapterView.stopRefresh();  
				mAdapterView.stopLoadMore();  
				 Utils.showToast(getActivity(), "没有更多内容了");
			}
		}, 200);
		
		
	}
}
