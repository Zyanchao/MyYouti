/**   
 * @Title: DownloadAdapter.java
 * @Package com.cloud.coupon.adapter
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 陈红建
 * @date 2013-7-3 下午5:50:29
 * @version V1.0
 */
package com.youti.yonghu.download;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youti_geren.R;
import com.youti.appConfig.YoutiApplication;
import com.youti.base.MyBaseAdapter;

/**
 * @ClassName: DownloadAdapter
 * @Description: 下载列表
 * @author 陈红建
 * @date 2013-7-3 下午5:50:29
 * 
 */
public class DownloadAdapter extends MyBaseAdapter
{

	private Context mContext;
	private ListView downloListView;
	private List<DownloadMovieItem> movies;
	private YoutiApplication myApp;
	private boolean isEditState;

	/**
	 * Title: Description:
	 */
	public DownloadAdapter(Context mContext, ListView downloadListView,List<DownloadMovieItem> movies){
		this.mContext = mContext;
		this.downloListView = downloadListView;
		this.movies = movies;
		this.downloListView.setAdapter(this);
		myApp = (YoutiApplication) mContext.getApplicationContext();
	}

	/**
	 * (非 Javadoc) Title: getCount Description:
	 * 
	 * @return
	 * @see com.cloud.coupon.adapter.MyBaseAdapter#getCount()
	 */
	@Override
	public int getCount(){
		return movies.size();
	}

	static class ViewHolder{
		ImageView movie_headimage; // 电影头像
		TextView movie_name_item; // 电影名称
		TextView movie_file_size; // 电影大小
		ProgressBar download_progressBar; // 电影的下载进度
		TextView current_progress; // 电影的当前进度
		Button stop_download_bt;
		ImageView delete_movie;
	}

	/**
	 * (非 Javadoc) Title: getView Description:
	 * 
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 * @see com.cloud.coupon.adapter.MyBaseAdapter#getView(int,
	 *      android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		final DownloadMovieItem d = movies.get(position);
		View view = null;
		ViewHolder holder = null;
		if (convertView != null){
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		else{
			view = View.inflate(mContext, R.layout.list_download_item, null);
			holder = new ViewHolder();
			holder.current_progress = (TextView) view
					.findViewById(R.id.current_progress);
			holder.download_progressBar = (ProgressBar) view
					.findViewById(R.id.download_progressBar);
			holder.movie_file_size = (TextView) view
					.findViewById(R.id.movie_file_size);
			holder.movie_headimage = (ImageView) view
					.findViewById(R.id.movie_headimage);
			holder.movie_name_item = (TextView) view
					.findViewById(R.id.movie_name_item);
			holder.stop_download_bt = (Button) view
					.findViewById(R.id.stop_download_bt);
			holder.delete_movie = (ImageView) view
					.findViewById(R.id.delete_movie);
			view.setTag(holder);
		}
		if (d != null){
//			System.out.println(d.getMovieName()+"的下载状态："+d.getDownloadState());
			switch (d.getDownloadState()){
			case DOWNLOAD_STATE_SUCCESS:
				// 如果下载完成,可以播放
				holder.stop_download_bt.setBackgroundResource(R.drawable.start);
				holder.stop_download_bt.setVisibility(View.VISIBLE);
				holder.current_progress.setText(d.getPercentage());
				holder.stop_download_bt.setText("");
				holder.current_progress.setTextColor(Color.parseColor("#fc2727"));
				break;
			case DOWNLOAD_STATE_DOWNLOADING:
				// 如果下载中,可以停止
				holder.stop_download_bt.setBackgroundResource(R.drawable.stop);
				holder.stop_download_bt.setVisibility(View.VISIBLE);
				holder.stop_download_bt.setText("");
				holder.current_progress.setText(d.getPercentage());
				holder.current_progress.setTextColor(Color.parseColor("#fc2727"));
				break;
			case DOWNLOAD_STATE_SUSPEND:
				// 如果已经停止,可以开始
				holder.stop_download_bt.setBackgroundResource(R.drawable.start);
				holder.stop_download_bt.setVisibility(View.VISIBLE);
				holder.stop_download_bt.setText("");
				holder.current_progress.setText(d.getPercentage());
				holder.current_progress.setTextColor(Color.parseColor("#fc2727"));
				break;
			case DOWNLOAD_STATE_EXCLOUDDOWNLOAD:
				// 如果不在当前下载队列之内
				holder.stop_download_bt.setBackgroundResource(R.drawable.stop);
				holder.stop_download_bt.setText("");
				holder.stop_download_bt.setVisibility(View.INVISIBLE);
				holder.current_progress.setText("等待中");
				holder.current_progress.setTextColor(Color.parseColor("#fc2727"));
				break;
			case DOWNLOAD_STATE_FAIL:
				// 如果是下载失败状态
				holder.stop_download_bt.setBackgroundResource(R.drawable.button_bg_retry);//重试
				holder.stop_download_bt.setText("重试");
				holder.stop_download_bt.setTextColor(Color.parseColor("#333333"));
				holder.current_progress.setTextColor(Color.parseColor("#f39801"));
				holder.current_progress.setText("下载失败");
				break;
			default:
				break;
			}
			if (isEditState){
				// 如果是编辑状态,显示删除按钮
				holder.delete_movie.setVisibility(View.VISIBLE);
				// 为删除按钮设置点击事件
				holder.delete_movie.setOnClickListener(new MyOnClick(holder, d,
						true , position));
				// 设置点击事件
			}
			else{
				holder.delete_movie.setOnClickListener(null);
				holder.delete_movie.setVisibility(View.GONE);
			}
			int count = d.getProgressCount().intValue();
			int currentPress = d.getCurrentProgress().intValue();
			holder.download_progressBar.setMax(count);
			holder.download_progressBar.setProgress(currentPress);
			
			holder.movie_file_size.setText(d.getFileSize());
			holder.movie_name_item.setText(d.getMovieName());
//			String imagePath = new File(mContext.getCacheDir(),d.getMovieId()+".png").getAbsolutePath();
			holder.stop_download_bt.setOnClickListener(new MyOnClick(holder, d,
					false, position));
		}
		return view;
	}

	public List<DownloadMovieItem> getMovies(){
		return movies;
	}

	public void setMovies(List<DownloadMovieItem> movies){
		this.movies = movies;
	}

	public boolean isEditState(){
		return isEditState;
	}

	public void setEditState(boolean isEditState){
		this.isEditState = isEditState;
	}

	class MyOnClick implements OnClickListener{

		private ViewHolder holder;
		private DownloadMovieItem dmi;
		private boolean isDeleteMovie;
		private int position;
		/**
		 * Title: Description:
		 */
		public MyOnClick(ViewHolder holder, DownloadMovieItem dmi,boolean isDeleteMovie , int position){
			this.holder = holder;
			this.dmi = dmi;
			this.isDeleteMovie = isDeleteMovie;// 是否是电影图片的点击事件
			this.position = position;
		}

		/**
		 * (非 Javadoc) Title: onClick Description:
		 * 
		 * @param v
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v){
			if( position <= movies.size()) {
				dmi = movies.get(position);
			final Intent i = new Intent(mContext, DownloadService.class);
							if (isDeleteMovie){
								// 如果点击的是删除按钮,删除一个下载任务
								System.out.println("删除一个下载任务");
								// 弹出是否删除的对话框
								new AlertDialog.Builder(mContext)
										.setTitle("要删除下载的影片吗？")
										.setMessage("此操作将会永久删除影片")
										// 二次提示
										.setNegativeButton("确定",
												new DialogInterface.OnClickListener(){
													public void onClick(DialogInterface dialog,int which){
														i.putExtra(SERVICE_TYPE_NAME,DOWNLOAD_STATE_DELETE);
														myApp.setStopOrStartDownloadMovieItem(dmi);
														mContext.startService(i);
													}
												})
										.setPositiveButton("取消",
												new DialogInterface.OnClickListener(){
													public void onClick(DialogInterface dialog,int which){
														
													}
												}).show();
							}else{
								// 如果点击的是 开始,暂停,播放
								if (dmi.getDownloadState() == DOWNLOAD_STATE_SUCCESS){
									// 进入视频播放界面
									// TODO 未实现的逻辑,当下载成功后跳转到视频播放界面
									System.out.println("播放：" + dmi.getMovieName() + "--路径是："
											+ dmi.getFilePath());
									//06-11 12:44:00.283: I/System.out(32714): 下载成功：/storage/emulated/0/测试下载3.mp4
				
							        Intent intent = new Intent(Intent.ACTION_VIEW);
							        String type = "video/mp4";
							        Uri uri = Uri.parse("file://"+dmi.getFilePath());
							        intent.setDataAndType(uri, type);
							        mContext.startActivity(intent);  
							        
							        
									Toast.makeText(mContext,"播放功能将在下个版本实现", Toast.LENGTH_SHORT).show();
									//在这里添加下载完成时的代码
								}
								else{
				
									int code = dmi.getDownloadState();
									switch (code){
										case DOWNLOAD_STATE_SUSPEND:
											// 如果是停止状态,设置为开始
											holder.stop_download_bt
													.setBackgroundResource(R.drawable.stop);
											// 停止这个下载任务
											i.putExtra(SERVICE_TYPE_NAME, DOWNLOAD_STATE_START);
											dmi.setDownloadState(DOWNLOAD_STATE_WATTING);
											break;
										case DOWNLOAD_STATE_DOWNLOADING:
											// 暂停这个下载任务
											holder.stop_download_bt
													.setBackgroundResource(R.drawable.start);
											dmi.setDownloadState(DOWNLOAD_STATE_SUSPEND);
											i.putExtra(SERVICE_TYPE_NAME,
													DOWNLOAD_STATE_SUSPEND);
											break;
										case DOWNLOAD_STATE_FAIL:
											//如果下载失败,重新下载
											// 开始这个下载任务
											dmi.setDownloadState(DOWNLOAD_STATE_WATTING);
											i.putExtra(SERVICE_TYPE_NAME,
													DOWNLOAD_STATE_START);
											Toast.makeText(mContext,"重新下载："+dmi.getMovieName(), Toast.LENGTH_SHORT).show();
											break;
						
										default:
											break;
									}
								}
							}
							myApp.setStopOrStartDownloadMovieItem(dmi);
							mContext.startService(i);
						}else {
							//数组角标越界
					}
				}
		}

}
