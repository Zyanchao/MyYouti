package com.youti.yonghu.activity;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
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
import com.youti.appConfig.YoutiApplication;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
import com.youti.utils.android.bitmapfun.ImageFetcher;
import com.youti.view.CircleImageView1;
import com.youti.view.TitleBar;
import com.youti.view.WaitDialog;
import com.youti.view.waterfallview.ImageListView;
import com.youti.view.waterfallview.ImageListView.IXListViewListener;
import com.youti.view.waterfallview.PLA_AdapterView;
import com.youti.view.waterfallview.PLA_AdapterView.OnItemClickListener;
import com.youti.yonghu.bean.Picture;
import com.youti.yonghu.bean.Picture.PicItem;

public class MyIssuedPhotoActivity extends Activity implements IXListViewListener {

	private ImageFetcher mImageFetcher;
	private ImageListView mAdapterView = null;
	private StaggeredAdapter mAdapter = null;
	private int currentPage = 0;

	private Picture picture;
	String user_id;
	
	
	public final String mPageName = "MyIssuedPhotoActivity";
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd( mPageName );
		MobclickAgent.onPause(this);
	}
	
	public class StaggeredAdapter extends BaseAdapter {
		private Context mContext;
		private LinkedList<PicItem> mInfos;
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
            .showStubImage(R.drawable.default_sq)          // 设置图片下载期间显示的图片    
            .showImageForEmptyUri(R.drawable.default_sq)  // 设置图片Uri为空或是错误的时候显示的图片    
            .showImageOnFail(R.drawable.default_sq)       // 设置图片加载或解码过程中发生错误显示的图片        
            .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中    
            .cacheOnDisc(true).build();                          // 设置下载的图片是否缓存在SD卡中    
  
			
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
			return arg0;
		}

		public void addItemLast(List<PicItem> datas) {
			mInfos.addAll(datas);
		}

		public void addItemTop(List<PicItem> datas) {
			mInfos.clear();
			for (PicItem info : datas) {
				mInfos.addFirst(info);
			}
		}
	}

	//ProgressBar pb;
	FrameLayout fl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_pull_to_refresh_sample);
		
		TitleBar titleBar =(TitleBar) findViewById(R.id.index_titlebar);
		titleBar.setVisibility(View.VISIBLE);
		titleBar.setTitleBarTitle("我的帖子");
		titleBar.setSearchGone();
		titleBar.setShareGone(false);
		user_id=((YoutiApplication)getApplication()).myPreference.getUserId();
		mAdapterView = (ImageListView) findViewById(R.id.list);
		//pb = (ProgressBar) findViewById(R.id.pb);
		fl = (FrameLayout) findViewById(R.id.fl);

		mAdapterView.setPullLoadEnable(false);
		mAdapterView.setPullRefreshEnable(false);
		mAdapterView.setXListViewListener(this);
		mAdapter = new StaggeredAdapter(this, mAdapterView);

		mImageFetcher = new ImageFetcher(this, 500);
		mImageFetcher.setLoadingImage(R.drawable.default_sq);
		
		
		
		mImageFetcher.setExitTasksEarly(false);
		/**
		 * 第一次设置数据
		 */
		//mAdapterView.setAdapter(mAdapter);
		
		// AddItemToContainer(currentPage, 2);
		
		mAdapterView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {
				Utils.showToast(MyIssuedPhotoActivity.this, "" + position);
				Intent intent = new Intent(MyIssuedPhotoActivity.this,PhotoDetailActivity.class);
				//这里由于添加了listView刷新头，因此position的值多1
				if(position>0){
					intent.putExtra("social_id", picture.list.get(picture.list.size()-position).social_id);	
					intent.putExtra("author_id", user_id);
				}
				MyIssuedPhotoActivity.this.startActivity(intent);
			}
		});
	}
	
	

	public final int ERROR = 100003;
	public final int LOADING = 100004;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ERROR:
				// fl.removeAllViews();
				//pb.setVisibility(View.GONE);
//				View v = View.inflate(MyIssuedPhotoActivity.this,R.layout.layout_error, null);
//				fl.addView(v);
				break;
			case LOADING:
				//pb.setVisibility(View.GONE);
				break;

			}
		};
	};
	private WaitDialog waitDialog;

	

	/**
	 * 添加内容
	 * 
	 * @param pageindex
	 * @param type
	 *            1为下拉刷新 2为加载更多
	 */
	private void AddItemToContainer(int pageindex, final int type) {

		String url = "http://112.126.72.250/ut_app/index.php?m=Community&a=overphoto";
		RequestParams params = new RequestParams();
		params.put("user_id", user_id);
		params.put("user_type", "2");
		waitDialog = Utils.getWaitDialog(MyIssuedPhotoActivity.this, "正在加载...");
		waitDialog.show();
		HttpUtils.post(url, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				waitDialog.dismiss();
				try {
					Gson gson = new Gson();
					
					picture = gson.fromJson(arg2, Picture.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println(arg2.toString());
			/*	Message msg = Message.obtain();
				msg.what = LOADING;
				handler.sendMessage(msg);*/
				setData(type);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				waitDialog.dismiss();
				Message msg = Message.obtain();
				msg.what = ERROR;
				handler.sendMessage(msg);
			}
			
		});
	}

	private void setData(int type) {
		if (picture != null) {
			if (type == 1) {				
				mAdapter.addItemTop(picture.list);
				//Utils.showToast(MyIssuedPhotoActivity.this, "这是什么情况看见了可玩儿就完全哦iutqo");
				mAdapter.notifyDataSetChanged();
				mAdapterView.stopRefresh();
			} else if (type == 2) {
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
		mAdapterView.setAdapter(mAdapter);
		//AddItemToContainer(3, 1);
		AddItemToContainer(0, 1);
		
		MobclickAgent.onPageStart( mPageName );
		MobclickAgent.onResume(this);
	}

	@Override
	public void onRefresh() {
		// AddItemToContainer(++currentPage, 1);

	}

	@Override
	public void onLoadMore() {
		// AddItemToContainer(++currentPage, 2);

	}
}


