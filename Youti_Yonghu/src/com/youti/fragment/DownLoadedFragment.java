package com.youti.fragment;

import java.io.File;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import com.youti.appConfig.YoutiApplication;
import com.youti.utils.Utils;
import com.youti.yonghu.download.ContentValue;
import com.youti.yonghu.download.DownloadManagerActivity;
import com.youti.yonghu.download.DownloadMovieItem;
import com.youti.yonghu.download.DownloadService;

public class DownLoadedFragment extends Fragment implements ContentValue {
	
	ListView listView;
	List<DownloadMovieItem> list;	
	private DisplayImageOptions options;
	YoutiApplication youtiApplication;
	
	
	
	private List<DownloadMovieItem> movies;
	private Handler handler = new Handler( ){
		public void handleMessage(android.os.Message msg) {
			runnable.run();
		};
	};
	
	public final String mPageName = "DownLoadedFragment";
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
	private MyAdapter adapter;
	private Runnable runnable = new Runnable( ) {


		public void run ( ) {
			movies = ((YoutiApplication)(getActivity().getApplication())).getDownloadedItems();
			if(movies != null) {
				if(adapter != null) {
					
					adapter.notifyDataSetChanged();
				}else {
					adapter =new MyAdapter();
					listView.setAdapter(adapter);
				}
			}
			handler.postDelayed(this,1500);
		}		
	};
	
	
	
	private Button download_manager_edit;
	private Button download_manager_clear;
	@Override
	public void onDestroy(){
		handler.removeCallbacks(runnable);
		super.onDestroy();
	}
	
	
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return movies.size();
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
			View v=View.inflate(getActivity(), R.layout.list_download_item, null);
			TextView tv =(TextView) v.findViewById(R.id.movie_name_item);
			TextView movie_file_size =(TextView) v.findViewById(R.id.movie_file_size);
			TextView current_progress=(TextView) v.findViewById(R.id.current_progress);
			Button bt =(Button) v.findViewById(R.id.stop_download_bt);
			ImageView iv =(ImageView) v.findViewById(R.id.delete_movie);
			current_progress.setVisibility(View.GONE);
			movie_file_size.setText("下载完成");
			tv.setText(movies.get(position).getMovieName());
			iv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					final Intent i =new Intent(getActivity(),DownloadService.class);
					// 弹出是否删除的对话框
					new AlertDialog.Builder(getActivity())
							.setTitle("要删除下载的影片吗？")
							.setMessage("此操作将会永久删除影片")
							// 二次提示
							.setNegativeButton("确定",
									new DialogInterface.OnClickListener(){
										public void onClick(DialogInterface dialog,int which){
											//i.putExtra(SERVICE_TYPE_NAME,DOWNLOAD_STATE_DELETE);
											//youtiApplication.setStopOrStartDownloadMovieItem(movies.get(position));
											String url =movies.get(position).getDownloadUrl();
//											System.out.println(url+"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
											editor.putBoolean(url, false);
											editor.commit();
											String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
											File file =new File(absolutePath+"/"+movies.get(position).getMovieName()+".mp4");
//											System.out.println(absolutePath+"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
//											System.out.println(movies.get(position).getMovieName()+"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
//											System.out.println(file.exists()+"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
										
												file.delete();
											
											movies.remove(position);
											youtiApplication.setDownloadedItems(movies);
											
										//	getActivity().startService(i);
											
										}
									})
							.setPositiveButton("取消",
									new DialogInterface.OnClickListener(){
										public void onClick(DialogInterface dialog,int which){
											
										}
									}).show();
				}
			});
			
			bt.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
				      String type = "video/mp4";
				      String path =Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+movies.get(position).getMovieName()+".mp4";
				      Utils.showToast(getActivity(), path);
				     // System.out.println("file://"+path);
				      Uri uri = Uri.parse("file://"+path);
				      intent.setDataAndType(uri, type);
				      getActivity().startActivity(intent);  
				}
			});
			
			return v;
		}
		
	}
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	SharedPreferences sp;
	private Editor editor;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view =View.inflate(getActivity(), R.layout.fragment_downloading, null);
		listView=(ListView) view.findViewById(R.id.listview);
		sp=getActivity().getSharedPreferences("config", getActivity().MODE_PRIVATE);
		editor = sp.edit();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Utils.showToast(getActivity(), position+"");
			}
		});
		download_manager_edit = (Button) view.findViewById(R.id.download_manager_edit);
		download_manager_clear = (Button)view. findViewById(R.id.download_manager_clear);
		
		download_manager_edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(getActivity())
				.setTitle("提示")
				.setMessage("是否清空所有任务？") //二次提示
				.setNegativeButton("确定", new android.content.DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which){
					//	Intent i = ((MyDownloadActivity)getActivity()).getServerIntent();
						Intent i = ((DownloadManagerActivity)getActivity()).getServerIntent();
						i.putExtra(SERVICE_TYPE_NAME, DOWNLOAD_STATE_CLEAR);
						((DownloadManagerActivity)getActivity()).startService(i);
						movies = ((DownloadManagerActivity)getActivity()).getMyApp().getDownloadItems();
						String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
						for(DownloadMovieItem item:movies){
							editor.putBoolean(item.getDownloadUrl(), false);
						}
						editor.commit();
						movies = ((YoutiApplication)(getActivity().getApplication())).getDownloadedItems();
						for(DownloadMovieItem item:movies){
							editor.putBoolean(item.getDownloadUrl(), false);
							File file =new File(absolutePath+"/"+item.getMovieName()+".mp4");
							file.delete();
						}
						editor.commit();
					
						//切换为编辑状态
						//download_manager_clear.setVisibility(View.GONE);
						//download_manager_edit.setText("编辑");//设置文本为编辑
					}
				})
				.setPositiveButton("取消",  new android.content.DialogInterface.OnClickListener(){

					public void onClick(DialogInterface dialog, int which){
						
					}
				}).show();
				
			}
		});
		
		
		
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
		youtiApplication = (YoutiApplication) getActivity().getApplication();
	}
	
}
