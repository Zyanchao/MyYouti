package com.youti.yonghu.activity;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.bitmap.AbImageDownloader;
import com.ab.fragment.AbLoadDialogFragment;
import com.ab.view.sliding.AbSlidingPlayView;
import com.alibaba.fastjson.JSON;
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
import com.youti.appConfig.Constants;
import com.youti.appConfig.YoutiApplication;
import com.youti.base.BaseActivity;
import com.youti.base.BaseHolder;
import com.youti.utils.DistanceUtils;
import com.youti.utils.HttpUtils;
import com.youti.utils.MyPreference;
import com.youti.utils.PopWindowUtil;
import com.youti.utils.StringUtil;
import com.youti.utils.Utils;
import com.youti.view.CircleImageView1;
import com.youti.view.MGridView;
import com.youti.view.TitleBar;
import com.youti.view.XListView;
import com.youti.view.XListView.IXListViewListener;
import com.youti.yonghu.adapter.FilterItenAdapter;
import com.youti.yonghu.bean.FilterItem;
import com.youti.yonghu.bean.InfoBean;
import com.youti.yonghu.bean.VideoListBean;
import com.youti.yonghu.bean.VideoListBean.VideoListItem;
import com.youti.yonghu.bean.VideoListBean.VideoZanItem;

public class VideoListActivity extends BaseActivity implements OnClickListener,IXListViewListener{

	protected  final int PRAISEVIDEOSUCCESS = 0;
	protected final int CANCLEVIDEOPRAISE = 1;
	protected  final int PRAISEVIDEOFAIL = 2;
	public final int INITVIDEO=100001;
	XListView mListView;
	TextView footerview;
	View pview,popView;
	ListView mItemList;
	private LinearLayout llFooter;
	private LinearLayout llOne;
	private LinearLayout llFour;
	private LinearLayout llThree;
	private LinearLayout llTwo;
	
	private ImageView ivOne,ivTwo,ivThree,ivFour,title_search;
	
	
	
	private AbImageDownloader mAbImageLoader = null;
	private AbSlidingPlayView mAbSlidingPlayView;

	
	private VideoListAdapterTest mAdapter;
	
	private FilterItenAdapter fAdapter;
	private FilterItem filterItem;
	private List<FilterItem> filterItems = new ArrayList<FilterItem>();
	private PopupWindow popupWindow;
	PopWindowUtil popu;
	private AbLoadDialogFragment  mDialogFragment = null;
	boolean firstin = true;
	boolean islast = false;
	boolean flagLoadMore = false;
	private int buttomHeight,screenWidth,screenHeight;
	private int currentPage=1,//当期页码
	             pageCount = 1,//总页数
                 pageSize = 15;//每页数据量
	List<VideoListItem> list =new ArrayList<VideoListItem>();
	
	
	private VideoListBean videoListBean;
	
	ProgressBar pb_loading;
	FrameLayout fl_videocontent;
	
	
	
	Handler mHander = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case INITVIDEO:
				pb_loading.setVisibility(View.GONE);
				if(mAdapter==null){
					mAdapter=new VideoListAdapterTest(VideoListActivity.this,list);
					mListView.setAdapter(mAdapter);
				}else{
					mAdapter.notifyDataSetChanged();					
				}
				break;
			case PRAISEVIDEOSUCCESS:
				Utils.showToast(VideoListActivity.this, "点赞成功，已添加到我的喜欢");
				
				//getVideo(flagLoadMore);
				break;
			case PRAISEVIDEOFAIL:
				
				Utils.showToast(VideoListActivity.this, "网络连接错误");
				break;
			case CANCLEVIDEOPRAISE:
				Utils.showToast(VideoListActivity.this, "取消点赞，已移除我的喜欢");
				
				//getVideo(flagLoadMore);
				break;
			default:
				break;
			}
		};
	};
	
	public final String mPageName = "VideoListActivity";

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd( mPageName );
		MobclickAgent.onPause(this);
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_sp);
		initView();
		initListenter();
		getVideo(flagLoadMore);
		getFilterItem();
		setListener();
		mAbImageLoader = new AbImageDownloader(abApplication);
	//	mListView.setAdapter(new VedioListAdapter(this, list));
		screenWidth = DistanceUtils.getScreenWidth(abApplication);
		myPreference = ((YoutiApplication)getApplication()).myPreference;
		userId = myPreference.getUserId();
	}

	
	
	private void getVideo(final Boolean flagLoadMore) {
		RequestParams params = new RequestParams();
		params.put("user_id", userId);
		HttpUtils.post("http://112.126.72.250/ut_app/index.php?m=Video&a=showlist", params, new TextHttpResponseHandler() {
			
		

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				try{
					Gson gson =new Gson();
					videoListBean = gson.fromJson(arg2, VideoListBean.class);
					if(videoListBean.code.equals("1")){
						//请求数据成功
					list =	videoListBean.list;
					System.out.println(arg2.toString());
					Message msg =Message.obtain();
					msg.what=INITVIDEO;
					mHander.sendMessage(msg);
					}
				}catch (Exception e) {
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				pb_loading.setVisibility(View.GONE);
				View v = View.inflate(VideoListActivity.this,R.layout.layout_error, null);
				fl_videocontent.addView(v);
				Utils.showToast(VideoListActivity.this, "获取网络失败，请稍后再试");
			}
		});
		
	}



	/**
	 * 获取 底部 筛选条件
	 */

	private void getFilterItem() {
		InputStream inputStream;
		 try {
			inputStream = VideoListActivity.this.getAssets().open("data/filter_kc_item.josn");
			String json = StringUtil.readTextFile(inputStream);
			//String state = json.getString("state");
				com.alibaba.fastjson.JSONObject object = JSON
						.parseObject(json.toString());
				com.alibaba.fastjson.JSONArray jsonArray = object
						.getJSONArray("msg");
				filterItems = JSON.parseArray(jsonArray.toString(),FilterItem.class);
				fAdapter = new FilterItenAdapter(getApplicationContext(), filterItems);
				mItemList.setAdapter(fAdapter);

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	TitleBar titleBar;
	private List<VideoZanItem> zanList=new ArrayList<VideoZanItem>();
	private MyPreference myPreference;
	private String userId;

	private void initView() {
		
		//底部的四个按钮所在的线性布局
		llFooter = (LinearLayout) findViewById(R.id.ll_footer);
		llOne = (LinearLayout) findViewById(R.id.footer_one);
		llFour = (LinearLayout) findViewById(R.id.footer_two);
		llThree = (LinearLayout) findViewById(R.id.footer_three);
		llTwo = (LinearLayout) findViewById(R.id.footer_four);
		//底部四个按钮图标，需要在选中时做改变
		ivOne = (ImageView) findViewById(R.id.im_one);
		ivTwo = (ImageView) findViewById(R.id.im_two);
		ivThree = (ImageView) findViewById(R.id.im_three);
		ivFour = (ImageView) findViewById(R.id.im_four);
		
		
		titleBar =(TitleBar) findViewById(R.id.index_titlebar);
		titleBar.setTitleBarTitle("视频");
		title_search=titleBar.getSearchIcon();	
		
		//视频列表所在的ListView
		mListView = (XListView) findViewById(R.id.sp_vediolist);
		
		
		//popupwindow 底部按钮点击弹出的
		pview = mInflater.inflate(R.layout.pop_list, null);
		popView = pview.findViewById(R.id.mLayout);		
		mItemList = (ListView) pview.findViewById(R.id.lv_pop);
		
		pb_loading=(ProgressBar) findViewById(R.id.pb_loading);
		fl_videocontent=(FrameLayout) findViewById(R.id.fl_videocontent);
		/*// 设置监听器
		refresh_view.getHeaderView().setHeaderProgressBarDrawable(
						this.getResources().getDrawable(R.drawable.progress_circular));
		refresh_view.getFooterView().setFooterProgressBarDrawable(
						this.getResources().getDrawable(R.drawable.progress_circular));*/
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this);
		
		
	
	}

	private void initListenter() {
		llOne.setOnClickListener(this);
		llFour.setOnClickListener(this);
		llThree.setOnClickListener(this);
		llTwo.setOnClickListener(this);
		title_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent =new Intent(VideoListActivity.this,VideoSearchActivity.class);
				startActivity(intent);
				
			}
		});
	}
	

	
	private void setListener() {
		mListView.setOnItemClickListener(new ItemtClickListener());
			
	}

	private class ItemtClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {		
			String video_id=list.get(position).video_id;
			Intent intent =new Intent(VideoListActivity.this,VideoDetailActivity.class);
			intent.putExtra("video_id", video_id);
			startActivity(intent);
			
		}
	}
	
	
	
	/**
	 * 计算 底部菜单 高度
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		
		ViewTreeObserver vto = llFooter.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				Rect rect = new Rect();
				getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);// /取得整个视图部分,注意，如果你要设置标题样
				int top = rect.top;// //状态栏的高度，所以rect.height,rect.width分别是系统的高度的宽度
				View v = getWindow().findViewById(Window.ID_ANDROID_CONTENT);// /获得根视图
				buttomHeight = llFooter.getHeight();
				screenHeight = DistanceUtils.getScreenHeight(abApplication);
				float ss = buttomHeight/screenHeight;
				Log.d("tag", ss+"");
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.footer_one:
			
			llOne.setBackgroundResource(R.color.chosed_ll_buttom);
			llTwo.setBackgroundResource(R.color.white);
			llThree.setBackgroundResource(R.color.white);
			llFour.setBackgroundResource(R.color.white);
			ivOne.setBackgroundResource(R.drawable.jl_arrow_h);
			ivTwo.setBackgroundResource(R.drawable.jl_arrow);
			ivThree.setBackgroundResource(R.drawable.jl_arrow);
			ivFour.setBackgroundResource(R.drawable.jl_arrow);
			popu = new PopWindowUtil(abApplication, screenWidth*1/4, screenHeight*7/12,0,buttomHeight, pview, popView);
			mItemList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					PopWindowUtil.colsePop();
				}
			});
			break;

		case R.id.footer_two:
			llOne.setBackgroundResource(R.color.white);
			llTwo.setBackgroundResource(R.color.white);
			llThree.setBackgroundResource(R.color.white);
			llFour.setBackgroundResource(R.color.chosed_ll_buttom);
			ivOne.setBackgroundResource(R.drawable.jl_arrow);
			ivTwo.setBackgroundResource(R.drawable.jl_arrow_h);
			ivThree.setBackgroundResource(R.drawable.jl_arrow);
			ivFour.setBackgroundResource(R.drawable.jl_arrow);
			popu = new PopWindowUtil(abApplication, screenWidth*1/4, screenHeight*7/12,screenWidth*1/4,buttomHeight, pview, popView);
			break;
		case R.id.footer_three:
	
			llOne.setBackgroundResource(R.color.white);
			llTwo.setBackgroundResource(R.color.white);
			llThree.setBackgroundResource(R.color.chosed_ll_buttom);
			llFour.setBackgroundResource(R.color.white);
			ivOne.setBackgroundResource(R.drawable.jl_arrow);
			ivTwo.setBackgroundResource(R.drawable.jl_arrow);
			ivThree.setBackgroundResource(R.drawable.jl_arrow_h);
			ivFour.setBackgroundResource(R.drawable.jl_arrow);
			popu = new PopWindowUtil(abApplication, screenWidth*1/4, screenHeight*7/12,screenWidth*1/2,buttomHeight, pview, popView);
			break;
		case R.id.footer_four:
			llOne.setBackgroundResource(R.color.white);
			llTwo.setBackgroundResource(R.color.chosed_ll_buttom);
			llThree.setBackgroundResource(R.color.white);
			llFour.setBackgroundResource(R.color.white);
			ivOne.setBackgroundResource(R.drawable.jl_arrow);
			ivTwo.setBackgroundResource(R.drawable.jl_arrow);
			ivThree.setBackgroundResource(R.drawable.jl_arrow);
			ivFour.setBackgroundResource(R.drawable.jl_arrow_h);
			popu = new PopWindowUtil(abApplication, screenWidth*1/4, screenHeight*7/12,screenWidth*3/4,buttomHeight, pview, popView);
			break;
		default:
			break;
		}
		
	}



	
	protected void loadMore() {
		
		currentPage++;//每次 点击加载更多，请求页码 +1
		flagLoadMore = false;
		getVideo(flagLoadMore);
	}


	@Override
	protected void onResume() {
		super.onResume();
		userId=myPreference.getUserId();
		getVideo(true);
		MobclickAgent.onPageStart( mPageName );
		MobclickAgent.onResume(this);
	}
	@Override
	public void onRefresh() {
		mHander.postDelayed(new Runnable() {//处理耗时 操作
			@Override
			public void run() {
				  Date currentTime = new Date();
				  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
				  String dateString = formatter.format(currentTime);
				  flagLoadMore = false;
				  currentPage = 1;
				  getVideo(flagLoadMore);
				  mListView.stopRefresh();  
		          mListView.stopLoadMore();  
		          mListView.setRefreshTime(dateString);  
					}
				}, 1000);
		
	}



	@Override
	public void onLoadMore() {
		/*mHander.postDelayed(new Runnable() {//处理耗时 操作
			@Override
			public void run() {
				loadMore();
				mListView.stopRefresh();  
		        mListView.stopLoadMore();  
			}
		}, 200);
		*/
	}
	

		

		


	
	protected void sendPraise(String video_id,int position) {
		SharedPreferences sp =getSharedPreferences("config", MODE_PRIVATE);
		String user_id = sp.getString("user_id", "80");
		if(TextUtils.isEmpty(user_id)){
			//提示先登录
			return;
		}
		//本地做+1-1操作
		//判断点赞集合中是否有该用户的id，如果没有就取消。如果有就点赞
		List<VideoZanItem> listcopy =new ArrayList<VideoZanItem>();
		if(list.get(position).user_img!=null){
			zanList =list.get(position).user_img;		
		}
		int size;
		if(zanList==null){
			size=0;
			//zanList= new ArrayList<VideoZanItem>();
		}else{
			 size=zanList.size();			
		}
		
		
		//System.out.println(size+"&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		for(int i=0;i<size;i++){
			listcopy.add(zanList.get(i));
		}
		for(int i=0;i<size;i++){
			//如果发现当前id已经存在在点赞列表中，就移除掉该用户。然后发送网络请求，然后结束该方法
			if(listcopy.get(i).getUser_id().equals(user_id)){			
				zanList.remove(i);
				//这里调用的实际上是点赞gridview中的更新方法
				MGridView mg = (MGridView) mListView.findViewWithTag(position);
				//mg.addView(child, index)
				
					mAdapter.notifyDataSetChanged();
					
				
			//	mAdapter.notifyDataSetChanged();
				requestPraise(user_id,video_id);
				return;
			}			
		}
		//当没有发现该用户的id，那么就创建一个该户的praiseitem对象，然后将该用户的信息，封装到点赞集合中，然后更新gideview
		VideoListBean vlb =new VideoListBean();
		VideoListBean.VideoZanItem pi =vlb.new VideoZanItem();
		pi.setUser_id("80");
		pi.setUser_img("http://112.126.72.250/ut_app/user/socialimg/1434534961_1.jpg");
		zanList.add(pi);		
		mAdapter.notifyDataSetChanged();	
		requestPraise(user_id,video_id);
	}
	
	private void requestPraise(String user_id,String video_id) {
		//发送请求到服务器
		String urlString = "http://112.126.72.250/ut_app/index.php?m=Video&a=praise_video";
		RequestParams params =new RequestParams();
		params.put("user_id", user_id);
		params.put("video_id", video_id);
		HttpUtils.post(Constants.VIDEO_PRAISE, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson =new Gson();
				InfoBean fromJson = gson.fromJson(arg2, InfoBean.class);
				Message msg =Message.obtain();
				
				if(fromJson.code.equals("1")){
					//点赞成功
					msg.what=PRAISEVIDEOSUCCESS;
					mHander.sendMessage(msg);
				}else if(fromJson.code.equals("0")){
					//点赞取消成功
					msg.what=CANCLEVIDEOPRAISE;
					mHander.sendMessage(msg);
				}
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Message msg =Message.obtain();
				msg.what=PRAISEVIDEOFAIL;
				mHander.sendMessage(msg);
			}
		});
	}
	
	
	public class VideoListAdapterTest extends BaseAdapter{
		List<VideoListItem> list;
		Context context;
		DisplayImageOptions options;
		public MyAdapter myAdapter;
		public VideoListAdapterTest(Context context,List<VideoListItem> list){
			this.list=list;
			this.context=context;
			//this.call=call;
			
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
	        .showImageForEmptyUri(R.drawable.empty_photo)  // 设置图片Uri为空或是错误的时候显示的图片    
	        .showImageOnFail(R.drawable.empty_photo)       // 设置图片加载或解码过程中发生错误显示的图片        
	        .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中    
	        .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中    
	        .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片    
	        .build();
		}
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view;
			final VedioItemHolder holder;
			if(convertView==null){
				view=View.inflate(context, R.layout.video_list_item1, null);
				holder = new VedioItemHolder();
				holder.des = (TextView) view.findViewById(R.id.sp_describe);
				holder.img = (ImageView) view.findViewById(R.id.bigImg);
				holder.num = (TextView) view.findViewById(R.id.sp_studyNum);
				holder.isTj = (ImageView) view.findViewById(R.id.sp_xiaobiantuijian);
				holder.zan_img=(ImageView) view.findViewById(R.id.zan_img);
				holder.zan=(TextView) view.findViewById(R.id.zan);
				holder.pinglun_img=(ImageView) view.findViewById(R.id.pinglun_img);
				holder.pinglun=(TextView) view.findViewById(R.id.pinglun);
				holder.ll_pinglun=(LinearLayout) view.findViewById(R.id.ll_pinglun);
				holder.ll_zan=(LinearLayout) view.findViewById(R.id.ll_zan);
				holder.gv=(MGridView ) view.findViewById(R.id.gv);
				view.setTag(holder);
			}else{
				view= convertView;
				holder=(VedioItemHolder) view.getTag();
			}
			holder.gv.setTag(position);
			holder.ll_pinglun.setTag(position);
			holder.ll_zan.setTag(position);
			
			holder.ll_pinglun.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent =new Intent(VideoListActivity.this,VideoDetailActivity.class);
					intent.putExtra("pinglun", "pinglun");
					intent.putExtra("video_id", list.get(position).video_id);
					startActivity(intent);
				}
			});
			
			holder.ll_zan.setOnClickListener(new OnClickListener() {
				

				@Override
				public void onClick(View v) {
					//sendPraise(list.get(position).video_id, position);
					if(TextUtils.isEmpty(userId)){
						Intent intent =new Intent(VideoListActivity.this,LoginActivity.class);
						context.startActivity(intent);
						return;
					}
					Toast.makeText(context, "点击了", 0).show();
					VideoListItem item =list.get(position);
					//已经赞过
					/**
					 * 
					 */
					if(item.getIs_zan().equals("1")&&list.get(position).getUser_img()!=null){
						for(int i=0;i<list.get(position).getUser_img().size();i++){
							if(list.get(position).getUser_img().get(i).user_id.equals(userId)){
								item.getUser_img().remove(i);
								//设置点赞心形图像为白色
								holder.zan_img.setBackgroundResource(R.drawable.sp_heart);
								break;
							}
						}
						
						item.setIs_zan("0");
						list.get(position).setIs_zan("0");
						item.setPraise_num(item.getUser_img().size()+"");
						System.out.println(""+item.getUser_img().size());
					}else if(item.getIs_zan().equals("0")&&list.get(position).getUser_img()!=null){
						VideoListBean vb =new VideoListBean();
						VideoZanItem videoItem =vb.new VideoZanItem();
						videoItem.setUser_id(userId);
						videoItem.setUser_img("http://112.126.72.250/ut_app/show/tx.jpg");
						item.getUser_img().add(0,videoItem);
						holder.zan_img.setBackgroundResource(R.drawable.sp_heart_h);
						System.out.println(""+item.getUser_img().size());
						item.setIs_zan("1");
						list.get(position).setIs_zan("1");
						item.setPraise_num(item.getUser_img().size()+"");
					}else{
						 List<VideoZanItem> zanList1=new ArrayList<VideoZanItem>();
						VideoListBean vb =new VideoListBean();
						VideoZanItem videoItem =vb.new VideoZanItem();
						videoItem.setUser_id(userId);
						videoItem.setUser_img("http://112.126.72.250/ut_app/show/tx.jpg");
						zanList1.add(videoItem);
						holder.zan_img.setBackgroundResource(R.drawable.sp_heart_h);
						//System.out.println(""+item.getUser_img().size());
						item.setUser_img(zanList1);
						item.setIs_zan("1");
						list.get(position).setIs_zan("1");
						item.setPraise_num(item.getUser_img().size()+"");
					}
					// 更新界面
					item.setDataId(position);
					/**
					 * ListView的局部刷新方法。
					 */			
					updateItemData(item);
					requestPraise(userId, list.get(position).video_id);
					
					
				}
			});		
			/**
			 * 刷新item时，会走以下的方法。如果点击做本地的添加头像效果，则不会走这个方法。但是当item离开屏幕，不在显示屏幕上时，如果再次出现在屏幕上，还是会走以下的方法
			 */
			/*if(list.get(position).user_img!=null){
				
			}else{
				zanList.clear();
			}*/
			zanList=list.get(position).user_img;
			listco = new ArrayList<VideoZanItem>();
			
			
			if(zanList!=null){
				if(zanList.size()<7){
					for(int i=zanList.size()-1;i>-1;i--){
						listco.add(zanList.get(i));
					}
					holder.gv.setAdapter(new MyAdapter(listco));					
				}else{
					int size=zanList.size();
					for(int i=size-1;i>size-7;i--){
						listco.add(zanList.get(i));
					}
					holder.gv.setAdapter(new MyAdapter(listco));
				}				
			}else{
				holder.gv.setAdapter(new MyAdapter(listco));
			}
			holder.des.setText(list.get(position).title);
			ImageLoader.getInstance().displayImage("http://112.126.72.250/ut_app"+list.get(position).img, holder.img, options);
			holder.num.setText(list.get(position).totalstudy+"人学习过本课程");
			holder.isTj.setVisibility(list.get(position).isshow.equals("1")?View.VISIBLE:View.GONE);
			holder.zan.setText(list.get(position).praise_num);
			holder.pinglun.setText(list.get(position).comment_num);	
			//每个条目中点赞集合的长度
			if(list.get(position).user_img!=null){
				
				int user_imgSize=list.get(position).user_img.size();
				for(int i=0;i<user_imgSize;i++){
					
					String dianzanUerId=list.get(position).user_img.get(i).user_id;
					if(dianzanUerId.equals(userId)){
						//如果当前登录的用户id和循环得出的点赞中的某个id相等，那么点赞图标就设置为：sp_heart_h
						//holder.zan_img.setBackgroundResource()
						holder.zan_img.setBackgroundResource(R.drawable.sp_heart_h);
					}else{
						//如果没有则设置为sp_heart
						holder.zan_img.setBackgroundResource(R.drawable.sp_heart);
					}
				}
			}else{
				holder.zan_img.setBackgroundResource(R.drawable.sp_heart);
			}
			
			
			return view;
		}
		/**
		 * 点赞adapter对外提供的get方法
		 * @return
		 */
		public MyAdapter getAdapter(View v,int position){
			return  myAdapter;
		}
		/**
		 * 点赞adapter对外提供的更新方法
		 * @return xiaguangcheng
		 *
		 */
		public void notifyDataSetChange(){
			myAdapter.notifyDataSetChanged();
		}
		public class VedioItemHolder extends BaseHolder {

			/** 是否是推荐 **/
			public ImageView isTj;
			/** 多少人学过 **/
			public TextView num,zan,pinglun;
			/** 简单介绍 **/
			public TextView des;
			public ImageView img,zan_img,pinglun_img;
			LinearLayout ll_zan,ll_pinglun;
			MGridView  gv;
			
		}
		public void addAndRefreshListView(List<VideoListItem> list) {
			if(this.list==null){
				this.list=new ArrayList<VideoListItem>();
			}
			this.list.addAll(list);
			notifyDataSetChanged();
			
		}
		/**
		 * 局部刷新的方法
		 * @param item
		 */
		public void updateItemData(VideoListItem item) {
			Message msg = Message.obtain();
			int ids = -1;
			// 进行数据对比获取对应数据在list中的位置
			/**
			 * 在这里也可以直接将item.getDataId()的值赋值给ids就可以了。
			 * 在ListView的局部刷新Demo中，是在item初始化数据时，就将list.get(i)中的每个对象的id设置为从0到20了，所以可以通过判断是否相等，来取得这个值
			 * 这里没有在最初给id赋值，只是在点击的时候给item.setDataId()，设置了当前的position
			 */
			if(item.getDataId()!=0){
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getDataId() == item.getDataId()) {
						ids = i;
					}
				}
				
			}else{
				ids=0;
			}
			
			msg.arg1 = ids;
			msg.obj=item;
			// 更新mDataList对应位置的数据
			list.set(ids, item);
			// handle刷新界面
			han.sendMessage(msg);
		}
		
		private ListView listView;
		
		public void setListView(ListView lisv) {
			this.listView = lisv;
		}
		
		private Handler han = new Handler() {
			public void handleMessage(android.os.Message msg) {
				updateItem(msg.arg1,(VideoListItem) msg.obj);
			};
		};
		/**
		 * 将某个位置的，某个对象进行刷新
		 * @param index
		 * @param item
		 */
		private void updateItem(int index,VideoListItem item) {
			if (mListView == null) {
				return;
			}

			// 获取当前可以看到的item位置
			int visiblePosition = mListView.getFirstVisiblePosition();
			// 如添加headerview后 firstview就是hearderview
			// 所有索引+1 取第一个view
			 View view = mListView.getChildAt(index - visiblePosition + 1);
			// 获取点击的view
			 //如果没有添加headerview，则使用以下的方法，获取view
			//View view = mListView.getChildAt(index-visiblePosition);
			TextView txt = (TextView) view.findViewById(R.id.zan);
			// 获取mDataList.set(ids, item);更新的数据
			VideoListItem data = (VideoListItem) getItem(index);
			// 重新设置界面显示数据
			txt.setText(data.getPraise_num()+"");
			GridView gv =(GridView) view.findViewById(R.id.gv);
			gv.setAdapter(new MyAdapter(item.getUser_img()));
			
		}
		ArrayList<Drawable> listImg=new ArrayList<Drawable>();
		private List<VideoZanItem> listco;
		
		public class MyAdapter extends BaseAdapter{
			List<VideoZanItem> list;
			public MyAdapter(List<VideoZanItem> list){
				this.list=list;
			}
			@Override
			public int getCount() {
				if(list.size()<7){
					return list.size();				
					
				}else{
					return 6;
				}
			}

			@Override
			public Object getItem(int position) {
				return list.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v =View.inflate(context, R.layout.item_image1, null);
				CircleImageView1 iv =(CircleImageView1) v.findViewById(R.id.iv);
				ImageLoader.getInstance().displayImage("http://112.126.72.250/ut_app"+list.get(position).user_img, iv, options);
				return v;					
			}
			
					
		}	
		
		
	}	



}
