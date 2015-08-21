/*  
* @Package com.cloud.coupon.service
* @Description: TODO(用一句话描述该文件做什么)
* @author 陈红建
* @date 2013-6-27 下午3:23:33
* @version V1.0
*/ 
package com.youti.yonghu.download;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.http.AjaxCallBack;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.widget.Toast;

import com.youti.appConfig.YoutiApplication;

/** 
 * @ClassName: MyService
 * @Description: 驻留后台的服务
 * @author 陈红建
 * @date 2013-6-27 下午3:23:33
 * 
 */
public class DownloadService extends BaseService {
	private List<DownloadMovieItem> items = new ArrayList<DownloadMovieItem>(); //当前所有的任务
	private Handler handler = new Handler( );
	private Runnable runnable = new Runnable( ) {
		public void run ( ) {
			//将下载池更新
		
			m.setDownloadItems(items);
			handler.postDelayed(this,500);
		}

		
	};
	
	@Override
	public void onDestroy(){
		//服务结束的时候结束定时器
		handler.removeCallbacks(runnable);
		System.out.println("服务停止");
		db.close();
		super.onDestroy();
	}
	/** (非 Javadoc) 
	* Title: onCreate
	* Description:
	* @see com.cloud.coupon.service.BaseService#onCreate()
	*/ 
	SQLiteDatabase sd;
	@Override
	public void onCreate(){
		super.onCreate();
		m = (YoutiApplication) getApplication();
		fb = FinalDb.create(getBaseContext(), "coupon.db");
	//	helper =new MySqliteHelper(this);
	//	sd=helper.getWritableDatabase();
	//	sd.close();
		
		clazz = DownloadMovieItem.class;
		dbpath = getBaseContext().getDatabasePath("coupon.db").getAbsolutePath();
		
		db = SQLiteDatabase.openDatabase(dbpath, null, 0);
		System.out.println("服务开启成功");
	}
	/** (非 Javadoc) 
	* Title: onStart
	* Description:
	* @param intent
	* @param startId
	* @see com.cloud.coupon.service.BaseService#onStart(android.content.Intent, int)
	*/ 
	@Override
	public void onStart(Intent intent, int startId){
		super.onStart(intent, startId);
		System.out.println("服务开始");
		if(intent != null) {
		int code = intent.getIntExtra(SERVICE_TYPE_NAME, ERROR_CODE);
		toPath = intent.getStringExtra(CACHE_DIR);
		if(TextUtils.isEmpty(toPath)) {
			//如果存储路径未设置
			toPath = "/mnt/emulated/0/cloud_coupon/";
		}
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			toPath = getBaseContext().getCacheDir().getAbsolutePath();
		}
		
		toPath=Environment.getExternalStorageDirectory().getAbsolutePath();
		File file =new File(toPath,"1234.txt");
		switch (code){
			case START_DOWNLOAD_MOVIE:
				//开启下载电影
				DownloadMovieItem startdmi = (DownloadMovieItem) intent.getSerializableExtra(DOWNLOAD_TAG_BY_INTENT);
	//			startdmi.setFilePath(toPath);
				items.add(startdmi);//讲接到的任务存放到下载池中
				startDownloadMovie(toPath ,startdmi);
				//开启一个定时器,每隔1秒钟刷新一次下载进度
				startTimerUpdateProgress();
				break;
			case DOWNLOAD_STATE_SUSPEND:
				//暂停一个下载任务
				DownloadMovieItem stopdmi = m.getStopOrStartDownloadMovieItem();
				if(stopdmi != null)
				stopDownload(stopdmi , false);
				break;
			case DOWNLOAD_STATE_START:
				//开始一个正在暂停的下载任务
				DownloadMovieItem startDmiByPausing = m.getStopOrStartDownloadMovieItem();
				if(startDmiByPausing != null)
				startPausingDownload(startDmiByPausing , toPath);
				break;
			case DOWNLOAD_STATE_DELETE:
				DownloadMovieItem delDownload = m.getStopOrStartDownloadMovieItem();
				deleteDownload(delDownload, toPath , false);
				break;
			case DOWNLOAD_STATE_CLEAR:
				clearAllDownload();
				break;
			case START_DOWNLOAD_LOADITEM:
				//数据库中装载下载任务
				if(items != null && items.size() != 0) {
				}else {
					//尝试从数据库中得到items
					items = fb.findAll(clazz);
					if(items != null && items.size() != 0)
						for(int i = 0 ; i < items.size() ; i++) {
							//保证不管应用以什么方式终止 重新启动的时候 所有状态都为 暂停
							if(items.get(i).getDownloadState() != DOWNLOAD_STATE_SUCCESS) {
								//如果当前不是下载完成的状态
								items.get(i).setDownloadState(DOWNLOAD_STATE_SUSPEND);
							}
						}
						m.setDownloadItems(items); 
					//如果是退出之后
				}
				break;
			case START_DOWNLOAD_ALLSUSPEND:
				//设置所有下载状态为暂停
				setAllDownloadTaskSuspend();
				break;
			default:
				break;
			}
		}
	}
	
	/** 
	 * @Title: setAllDownloadTaskSuspend
	 * @Description: 
	 * @param 
	 * @return void
	 * @author 陈红建
	 * @throws 
	 */
	private void setAllDownloadTaskSuspend()
	{
		for(int i = 0 ; i < items.size() ; i++) {
			DownloadMovieItem sdmi = items.get(i);
			sdmi.setDownloadState(DOWNLOAD_STATE_SUSPEND);
			String sql = "UPDATE "+AgentConstant.DOWNLOADTASK_TABLE+" SET "+"movieId='"+sdmi.getMovieId()+"',movieName='"+sdmi.getMovieName()+"',fileSize='"+sdmi.getFileSize()+"',currentProgress="+sdmi.getCurrentProgress()+",percentage='"+sdmi.getPercentage()+"',filePath='"+sdmi.getFilePath()+"',downloadUrl='"+sdmi.getDownloadUrl()+"',uuid="+sdmi.getUuid()+",progressCount="+sdmi.getProgressCount()+",downloadState="+DOWNLOAD_STATE_WATTING+",setCount='"+sdmi.getSetCount()+"' WHERE movieName='"+sdmi.getMovieName()+"'";
			
			System.out.println(sql);
			db.execSQL(sql);
		}
	}
	/** 
	 * @Title: stopDownload
	 * @Description: 暂停指定的下载任务
	 * @param dmi 接收一个 下载任务
	 * @return void
	 * @author 陈红建
	 * @throws 
	 */
	private void stopDownload(DownloadMovieItem dmi , boolean isDel)
	{
		DownloadMovieItem sdmi = currentDownloadItems.get(dmi.getUuid());
		if(sdmi != null) {
			Toast.makeText(getApplicationContext(),"暂停："+sdmi.getMovieName(), Toast.LENGTH_SHORT).show();
			sdmi.setDownloadState(DOWNLOAD_STATE_SUSPEND);
			//将下载任务暂停
			if(sdmi.getDownloadFile() != null) {
				sdmi.getDownloadFile().stopDownload();
			}
			//设置下载任务状态
			currentDownloadItems.remove(sdmi.getUuid());//将任务从下载队列中移除
			if(!isDel) {
				//如果不是删除,讲数据库中的状态进行变更
				String sql = "UPDATE "+AgentConstant.DOWNLOADTASK_TABLE+" SET "+"movieId='"+sdmi.getMovieId()+"',movieName='"+sdmi.getMovieName()+"',fileSize='"+sdmi.getFileSize()+"',currentProgress="+sdmi.getCurrentProgress()+",movieHeadImagePath="+sdmi.getMovieHeadImagePath()+",percentage='"+sdmi.getPercentage()+"',filePath='"+sdmi.getFilePath()+"',downloadUrl='"+sdmi.getDownloadUrl()+"',uuid="+sdmi.getUuid()+",progressCount="+sdmi.getProgressCount()+",downloadState="+sdmi.getDownloadState()+",setCount='"+sdmi.getSetCount()+"' WHERE movieName='"+sdmi.getMovieName()+"'";
				System.out.println(sql);
				db.execSQL(sql);
				startDownloadMovie(toPath , null);
			}
			System.out.println(sdmi.getMovieName()+"：任务暂停："+sdmi.getPercentage() + "状态："+sdmi.getDownloadState());
			//继续查找新的任务
		}else {
			System.out.println("暂停失败,原因是未知的下载状态");
		}
	}
	
	/** 
	 * @Title: deleteDownload
	 * @Description: 删除一个下载任务 
	 * @param 
	 * @return void
	 * @author 陈红建
	 * @throws 
	 */
	private void deleteDownload(DownloadMovieItem dmi , String toPath , boolean allDel){
		//停止这个任务 
//		stopDownload(dmi , true);
		String delPath = dmi.getFilePath();
		if(TextUtils.isEmpty(delPath)) {
			delPath = toPath + dmi.getMovieName();
		}
		File delFile = new File(delPath);
		if(delFile!=null && delFile.delete()) {
			//删除成功
			//移除条目
			items.remove(dmi);
			currentDownloadItems.remove(dmi.getUuid());
			//如果是删除一个任务,此标志作废
			if(!allDel)
			{
				String sql = "DELETE from "+AgentConstant.DOWNLOADTASK_TABLE+" WHERE movieName='"+dmi.getMovieName()+"';";
				db.execSQL(sql);
			}
		}else {
			//删除失败
			System.out.println(dmi.getMovieName()+"：删除失败");
			items.remove(dmi);
			String sql = "DELETE from "+AgentConstant.DOWNLOADTASK_TABLE+" WHERE movieName='"+dmi.getMovieName()+"';";
			db.execSQL(sql);
		}
		
	}
	/** 
	 * @Title: clearAllDownload
	 * @Description: 清除所有下载任务 
	 * @param 
	 * @return void
	 * @author 陈红建
	 * @throws 
	 */
	private void clearAllDownload()
	{
		for(int i = 0 ; i < items.size() ; i++) {
			String name = items.get(i).getMovieName();
			String sql = "DELETE from "+AgentConstant.DOWNLOADTASK_TABLE+" WHERE movieName='"+name+"';";
			System.out.println(sql);
			db.execSQL(sql);
			System.out.println("删除："+name);
			if(items.get(i).getDownloadFile()!=null) {
				items.get(i).getDownloadFile().stopDownload();
			}
			if(!TextUtils.isEmpty(items.get(i).getFilePath())) {
				new File(items.get(i).getFilePath()).delete();
			}
		}
		items.clear();
		currentDownloadItems.clear();
		m.getDownloadItems().clear();
	}
	/** 
	 * @Title: startPausingDownload
	 * @Description: 开始一个 当前是暂停状态的任务 
	 * @param dmi 接收一个 下载任务
	 * @return void
	 * @author 陈红建
	 * @throws 
	 */
	private void startPausingDownload(DownloadMovieItem dmi , String toPath)
	{
		
		for (int i = 0; i < items.size(); i++)
		{
			if(dmi.getUuid() == items.get(i).getUuid()) {
				items.get(i).setDownloadState(DOWNLOAD_STATE_WATTING);
				Toast.makeText(getApplicationContext(),dmi.getMovieName()+":开始下载", Toast.LENGTH_SHORT).show();
				System.out.println(dmi.getUuid()+"任务开始："+dmi.getCurrentProgress());
				db.update(AgentConstant.DOWNLOADTASK_TABLE, setDbValues(items.get(i)), "uuid=?", new String[] {items.get(i).getUuid()+""});
				//更改下载状态
				startDownloadMovie(toPath , items.get(i));
			}
		}
		
		
	}
	/** 
	* @Title: startTimerUpdateProgress
	* @Description: 更新下载进度
	* @param 
	* @return void
	* @author 陈红建
	* @throws 
	*/ 
	private void startTimerUpdateProgress()
	{
		handler.postDelayed(runnable, 500);
	}

	private Map<Long , DownloadMovieItem> currentDownloadItems = new HashMap<Long, DownloadMovieItem>();
	/** 
	 * @Title: startDownloadMovie
	 * @Description: 开启下载电影的任务 
	 * @param Intent
	 * @param List<DownloadMovieItem> items 存放要下载的电影
	 * @return void
	 * @author 陈红建
	 * @throws 
	 */
	private String toPath;
	private YoutiApplication m;
	private FinalDb fb;
	private Class<DownloadMovieItem> clazz;
	private SQLiteDatabase db;
	private String dbpath;
	public void startDownloadMovie(final String toPath , DownloadMovieItem dmid){
				
				int size = items.size();
				for (int g = 0 ; g < size ; g++){
					
						if(items.get(g).getDownloadState() == DOWNLOAD_STATE_WATTING  || items.get(g).getDownloadState() == DOWNLOAD_STATE_EXCLOUDDOWNLOAD) {
						//如果状态为等待状态
					//如果当前正在下载的任务 不够三个
					if(currentDownloadItems.size() < 3) {
						//取得下载任务
						final DownloadMovieItem dmi = items.get(g);
						//构建存储文件路径
						File f = new File(toPath,dmi.getMovieName()+".mp4"); // 扩展名默认为.MP4
						dmi.setFilePath(f.getAbsolutePath());
						dmi.setDownloadState(DOWNLOAD_STATE_DOWNLOADING);
						if(dmi.getUuid() == null) {
							Long id = Long.parseLong("0");
							id = UUID.randomUUID().getLeastSignificantBits();
							dmi.setUuid(id);
						}
						if(dmi.getUuid() != null) {
							currentDownloadItems.put(dmi.getUuid(), dmi);//讲下载任务存入当前的队列中
						}
						DownloadFile d = download(dmi.getDownloadUrl(), f.getAbsolutePath(), new AjaxCallBack<File>(){
							
							/** (非 Javadoc) 
							* Title: onStart
							* Description:
							* @see net.tsz.afinal.http.AjaxCallBack#onStart()
							*/ 
							@Override
							public void onStart(){
								//如果这个任务在等待,开启这个下载任务 
								//讲下载状态设置为 正在下载
								//开启任务
								dmi.setDownloadState(DOWNLOAD_STATE_DOWNLOADING);
								System.out.println(dmi.getMovieName()+"--开始下载"+dbpath);
								//为下载任务分配ID号
								//将当前下载的对象以及状态写入到数据库中
								//如果这个对象在数据库中已经存在了
								List<DownloadMovieItem> ls = fb.findAllByWhere(clazz, dmi.getUuid()+"");
								if(ls != null && ls.size() != 0 ) {
									for( int i = 0 ; i < ls.size() ; i++) {
										if(ls.get(i).getUuid() == dmi.getUuid() || ls.get(i).getMovieName().equals(dmi.getMovieName())) {
											//如果数据库中有此记录
										}else {
											
											//将这个对象插入到数据库中
											db.insert(AgentConstant.DOWNLOADTASK_TABLE, null, setDbValues(dmi));
										}
									}
								}else {
									db.insert(AgentConstant.DOWNLOADTASK_TABLE, null, setDbValues(dmi));
								}
								
							}
							/** (非 Javadoc) 
							* Title: onLoading
							* Description:
							* @param count
							* @param current
							* @see net.tsz.afinal.http.AjaxCallBack#onLoading(long, long)
							*/ 
							@Override
							public void onLoading(long count, long current){
								//设置当前的进度
								dmi.setProgressCount(count);
								dmi.setCurrentProgress(current);
								if( count != 0 ) {
									int p = (int) ((current*100) / count)  ;
									dmi.setPercentage("下载中："+p+"%");
								}else {
									dmi.setPercentage("下载中：0%");
								}
								dmi.setDownloadState(DOWNLOAD_STATE_DOWNLOADING);
								//设置下载的电影大小
								//dmi.setFileSize(count+"");
								dmi.setFileSize(Formatter.formatFileSize(getApplicationContext(), current)+"/"+Formatter.formatFileSize(getApplicationContext(), count));
							}
							/** (非 Javadoc) 
							* Title: onSuccess 
							* Description:
							* @param t
							* @see net.tsz.afinal.http.AjaxCallBack#onSuccess(java.lang.Object)
							*/ 
							@Override
							public void onSuccess(File t){
								dmi.setDownloadState(DOWNLOAD_STATE_SUCCESS);
								System.out.println("下载成功："+t.getAbsolutePath());
								currentDownloadItems.remove(dmi.getUuid());//成功的时候从当前下载队列中去除
								//将下载的状态插入到数据库中
								DownloadMovieItem sdmi = dmi;
								
								String sql = "UPDATE "+AgentConstant.DOWNLOADTASK_TABLE+" SET "+"movieId='"+sdmi.getMovieId()+"',movieName='"+sdmi.getMovieName()+"',fileSize='"+sdmi.getFileSize()+"',currentProgress="+sdmi.getCurrentProgress()+",percentage='"+sdmi.getPercentage()+"',filePath='"+sdmi.getFilePath()+"',downloadUrl='"+sdmi.getDownloadUrl()+"',uuid="+sdmi.getUuid()+",progressCount="+sdmi.getProgressCount()+",downloadState="+sdmi.getDownloadState()+",setCount='"+sdmi.getSetCount()+"' WHERE movieName='"+sdmi.getMovieName()+"'";
								db.execSQL(sql);
								startDownloadMovie(toPath , null);//继续寻找可以下载的任务
								
								/**
								 * 当下载完成时，发送一个广播，downloadManageractivity接收到广播后，让已下载的fragment进行显示
								 *  Parcelable encountered IOException writing serializable object 
								 */
								Intent intent =new Intent("com.youti_yonghu.download");
								//Bundle bundle =new Bundle();
								//bundle.putSerializable("downItem", dmi);
								intent.putExtra("downloadUrl", sdmi.getDownloadUrl());
								sendBroadcast(intent);
								System.out.println("开始发广播了");
							}
							/** (非 Javadoc) 
							* Title: onFailure
							* Description:
							* @param t
							* @param strMsg
							* @see net.tsz.afinal.http.AjaxCallBack#onFailure(java.lang.Throwable, java.lang.String)
							*/ 
							@Override
							public void onFailure(Throwable t, String strMsg){
								dmi.setDownloadState(DOWNLOAD_STATE_FAIL);
								System.out.println("下载失败:"+strMsg+":"+dmi.getDownloadUrl()+"：\n原因："+t.getMessage()+"异常信息:"+t.getLocalizedMessage());
								Toast.makeText(getApplicationContext(),dmi.getMovieName()+"：下载失败", Toast.LENGTH_SHORT).show();
								t.printStackTrace();
								currentDownloadItems.remove(dmi.getUuid());
								SystemClock.sleep(1000);
								startDownloadMovie(toPath , null);
								System.out.println(toPath);
								//更新数据库状态
//								fb.update(dmi, dmi.getUuid()+"");
								DownloadMovieItem sdmi = dmi;
								String sql = "UPDATE "+AgentConstant.DOWNLOADTASK_TABLE+" SET "+"movieId='"+sdmi.getMovieId()+"',movieName='"+sdmi.getMovieName()+"',fileSize='"+sdmi.getFileSize()+"',currentProgress="+sdmi.getCurrentProgress()+",percentage='"+sdmi.getPercentage()+"',filePath='"+sdmi.getFilePath()+"',downloadUrl='"+sdmi.getDownloadUrl()+"',uuid="+sdmi.getUuid()+",progressCount="+sdmi.getProgressCount()+",downloadState="+sdmi.getDownloadState()+",setCount='"+sdmi.getSetCount()+"' WHERE movieName='"+sdmi.getMovieName()+"'";
								db.execSQL(sql);
								Toast.makeText(getApplicationContext(),sdmi.getMovieName()+"下载失败,可能是存储空间不足或者网络环境太差", Toast.LENGTH_SHORT).show();
							}
						});
						dmi.setDownloadFile(d);
						
				}
					else {
						//将下载任务插入到数据库中
						if(dmid != null) {
							dmid.setDownloadState(DOWNLOAD_STATE_EXCLOUDDOWNLOAD);
							db.insert(AgentConstant.DOWNLOADTASK_TABLE, null, setDbValues(dmid));
						}
					}
				}
			}
			
	}
	/** 
	 * @Title: setDbValues
	 * @Description: 
	 * @param 
	 * @return void
	 * @author 陈红建
	 * @throws 
	 */
	private ContentValues setDbValues(DownloadMovieItem dmi)
	{
		ContentValues values = new ContentValues();
		values.put("id", dmi.getId());
		values.put("editState", dmi.isEditState());
		values.put("movieName", dmi.getMovieName());
		values.put("fileSize", dmi.getFileSize());
		values.put("currentProgress", dmi.getCurrentProgress());
		values.put("isSelected", dmi.isSelected());
		values.put("percentage", dmi.getPercentage());
		values.put("filePath", dmi.getFilePath());
		values.put("downloadUrl", dmi.getDownloadUrl());
		values.put("uuid", dmi.getUuid());
		values.put("progressCount", dmi.getProgressCount());
		values.put("downloadState", dmi.getDownloadState());
		values.put("setCount", dmi.getId());
		values.put("movieId", dmi.getMovieId());
		return values;
		
	}

	/** 
	* @Title: download
	* @Description: 开启一个普通的下载任务 
	* @param 
	* @return void
	* @author 陈红建
	* @throws 
	*/ 
	private DownloadFile download(String url , String toPath , AjaxCallBack<File> downCallBack)
	{
		return new DownloadFile().startDownloadFileByUrl(url, toPath, downCallBack);
	}
}

