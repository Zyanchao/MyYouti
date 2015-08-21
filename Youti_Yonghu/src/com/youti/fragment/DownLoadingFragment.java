package com.youti.fragment;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.youti_geren.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.YoutiApplication;
import com.youti.yonghu.download.ContentValue;
import com.youti.yonghu.download.DownloadAdapter;
import com.youti.yonghu.download.DownloadManagerActivity;
import com.youti.yonghu.download.DownloadMovieItem;

public class DownLoadingFragment extends Fragment implements ContentValue,OnClickListener{
	ListView listView;
	List<DownloadMovieItem> list;
	RelativeLayout rl1;
	private DisplayImageOptions options;
	
	
	private DownloadAdapter adapter;
	private List<DownloadMovieItem> movies;
	private Handler handler = new Handler( ){
		public void handleMessage(android.os.Message msg) {
			runnable.run();
		};
	};
	public final String mPageName = "DownLoadingFragment";
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
	private Runnable runnable = new Runnable( ) {


		public void run ( ) {
			movies = ((YoutiApplication)(getActivity().getApplication())).getDownloadItems();
			if(movies != null) {
				if(adapter != null) {
					adapter.setMovies(movies);
					System.out.println("DownLoadingFragment:"+movies.toString());
					adapter.notifyDataSetChanged();
				}else {
					adapter = new DownloadAdapter(getActivity(), listView, movies);
				}
			}
			handler.postDelayed(this,500);
		}		
	};
	
	private Button download_manager_edit;
	private Button download_manager_clear;
	@Override
	public void onDestroy(){
		handler.removeCallbacks(runnable);
		super.onDestroy();
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/*TextView tv =new TextView(getActivity());
		tv.setText("咨询信息Fragment");*/
		View view =View.inflate(getActivity(), R.layout.fragment_downloading, null);
		listView=(ListView) view.findViewById(R.id.listview);
		download_manager_edit = (Button) view.findViewById(R.id.download_manager_edit);
		download_manager_clear = (Button)view. findViewById(R.id.download_manager_clear);
		 rl1 = (RelativeLayout) view.findViewById(R.id.relativeLayout1);
		// rl1.setVisibility(View.GONE);
		 
		 
		 
		 download_manager_edit.setVisibility(View.VISIBLE);
		if(((DownloadManagerActivity)getActivity()).getMyApp().getDownloadItems() != null &&((DownloadManagerActivity)getActivity()). getMyApp().getDownloadItems().size() != 0) {
			//如果Applaction里面有数据
		}else {
			//从数据库中加载
			Intent i = ((DownloadManagerActivity)getActivity()).getServerIntent();
			i.putExtra(SERVICE_TYPE_NAME, START_DOWNLOAD_LOADITEM);
			((DownloadManagerActivity)getActivity()).startService(i);
		}
		handler.postDelayed(runnable, 500);
		
		
		
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
        .showImageOnFail(R.drawable.empty_photo)       // 设置图片加载或解码过程中发生错误显示的图片        
        .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中    
        .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中    
        .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片    
        .build();
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//list=((MyDownloadActivity)getActivity()).getMoviewItemList();		
		//listView.setAdapter(new MyAdapter());
		download_manager_edit.setOnClickListener(this);
		download_manager_clear.setOnClickListener(this);
		
		//长按点击删除任务
	/*	listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			private String dbpath;
			private SQLiteDatabase db;

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				Utils.showToast(getActivity(), "长按点击");
				//从数据库中删除该条目
			
	
				new AlertDialog.Builder(getActivity())
				.setTitle("要删除下载的影片吗？")
				.setMessage("此操作将会永久删除影片")
				// 二次提示
				.setNegativeButton("确定",
						new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface dialog,int which){
								DownloadMovieItem dm =movies.get(position);
								String downloadUrl =dm.getDownloadUrl();
								dbpath = getActivity().getBaseContext().getDatabasePath("coupon.db").getAbsolutePath();
								
								db = SQLiteDatabase.openDatabase(dbpath, null, 0);
								
								 String sql ="DELETE from "+AgentConstant.DOWNLOADTASK_TABLE+" WHERE downloadUrl='"+dm.getDownloadUrl()+"';";
								 System.out.println(sql);
								 db.execSQL(sql);
								System.out.println("删除成功");
								db.close();
								handler.postDelayed(runnable,500);
							}
						})
				.setPositiveButton("取消",
						new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface dialog,int which){
								
							}
						}).show();
				
				
			
				return false;
			}
		});*/
	}
	
	
	
	private void clearDownlodItem(){
		if(adapter != null ) {
			int size = adapter.getMovies().size();
			if( size != 0) {
				for(int i = 0  ; i < size ; i++) {
					if(adapter.getMovies().get(i).isSelected()) {
						//拿到选中的条目
						System.out.println(adapter.getMovies().get(i).getMovieName()+"：需要清除");
					}
				}
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.download_manager_edit:
			
			
			new AlertDialog.Builder(getActivity())
			.setTitle("提示")
			.setMessage("是否清空所有任务？") //二次提示
			.setNegativeButton("确定", new android.content.DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which){
				//	Intent i = ((MyDownloadActivity)getActivity()).getServerIntent();
					Intent i = ((DownloadManagerActivity)getActivity()).getServerIntent();
					i.putExtra(SERVICE_TYPE_NAME, DOWNLOAD_STATE_CLEAR);
					((DownloadManagerActivity)getActivity()).startService(i);
					//切换为编辑状态
					//download_manager_clear.setVisibility(View.GONE);
					//download_manager_edit.setText("编辑");//设置文本为编辑
				}
			})
			.setPositiveButton("取消",  new android.content.DialogInterface.OnClickListener(){

				public void onClick(DialogInterface dialog, int which){
					
				}
			}).show();
			break;
		case R.id.download_manager_clear:
			
			
			
			if(adapter != null && movies != null && movies.size()!=0) {
				if(download_manager_edit.getText().toString().contains("编辑")) {
					//如果是不是编辑状态,按钮文本为"编辑",还未进入编辑状态
					//设置按钮文字"完成"
					download_manager_edit.setText("完成");
					//显示清除按钮
					download_manager_clear.setVisibility(View.VISIBLE);
					adapter.setEditState(true);//设置为编辑状态
					adapter.notifyDataSetChanged();
					
				}else {
					//已经是编辑状态,按钮文本是"完成"
					//隐藏 清除按钮
					download_manager_clear.setVisibility(View.GONE);
					download_manager_edit.setText("编辑");//设置文本为编辑
					adapter.setEditState(false);//取消编辑状态
					adapter.notifyDataSetChanged();
					//清除选中的条条目
					clearDownlodItem();
				}
			}
			break;
			
		default:
			break;
		}
		
	}
}
