package com.youti.yonghu.download;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.youti.appConfig.YoutiApplication;
import com.youti.utils.Utils;

public class DownloadBroadcastReceiver extends BroadcastReceiver {
	String downloadUrl;
	Context context;
	SQLiteDatabase db;
	YoutiApplication youtiApplication;
	List<DownloadMovieItem> listItem;
	List<DownloadMovieItem> removeList;
	SharedPreferences sp;
	int a;
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context=context;
		System.out.println("动态注册广播");
		sp=context.getSharedPreferences("config", context.MODE_PRIVATE);
		Editor editor =sp.edit();
		
		youtiApplication = (YoutiApplication) context.getApplicationContext();
		if(intent!=null){
			downloadUrl = intent.getStringExtra("downloadUrl");
		}			
		Utils.showToast(context, downloadUrl);	
		editor.putBoolean(downloadUrl, true);
		DownloadMovieItem item =getDownItem(downloadUrl);
		listItem=youtiApplication.getDownloadedItems();
		listItem.add(item);
		//downedFragment=(DownLoadedFragment) ma.getItem(1);
		
		removeList =youtiApplication.getDownloadItems();
		System.out.println("我在broadcast里面，下载成功"+removeList.toString());
		for(int i=0;i<removeList.size();i++){
			if(removeList.get(i).getDownloadUrl().equals(downloadUrl)){
				a=i;
			}
		}
		removeList.remove(a);
		//addUrl(downloadUrl);
		deleteItem(downloadUrl);
		youtiApplication.setDownloadedItems(listItem);
		youtiApplication.setDownloadItems(removeList);
		
		
		System.out.println("我在broadcast里面，下载成功removeList:"+removeList.toString());
		System.out.println("我在broadcast里面，下载成功listItem:"+listItem.toString());
	}

	private void addUrl(String video_url) {
		String dbpath =context.getDatabasePath("coupon.db").getAbsolutePath();
		System.out.println(video_url);
		db = SQLiteDatabase.openDatabase(dbpath, null, 0);
		String sql ="insert into videourl (url) values ("+video_url+") ";
		db.execSQL(sql);
		db.close();
	}

	public void deleteItem(String video_url){
		String dbpath =context.getDatabasePath("coupon.db").getAbsolutePath();
		System.out.println(video_url);
		db = SQLiteDatabase.openDatabase(dbpath, null, 0);
		String sql ="delete from "+AgentConstant.DOWNLOADTASK_TABLE+" where downloadUrl = "+video_url;
//		db.execSQL(sql);
		db.delete(AgentConstant.DOWNLOADTASK_TABLE, "downloadUrl = ?", new String []{video_url});
		db.close();
		System.out.println("删除成功");
	}
	
	/**
	 * 根据广播传递回来的downloadUrl，查询下载好的视频，然后将其封装为downLoadMovieITEm对象
	 * @param video_url
	 * @return
	 */
	public DownloadMovieItem getDownItem(String video_url){
		String dbpath =context.getDatabasePath("coupon.db").getAbsolutePath();
		System.out.println(video_url);
		db = SQLiteDatabase.openDatabase(dbpath, null, 0);
		DownloadMovieItem downItem =new DownloadMovieItem();
		String sql ="select * from "+AgentConstant.DOWNLOADTASK_TABLE+" where downloadUrl=?";
		String[] selectionArgs=new String[] {video_url};
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		System.out.println(cursor.toString());
		if(cursor.moveToNext()){
			String fileSize =cursor.getString(cursor.getColumnIndex("fileSize"));
			String movieName=cursor.getString(cursor.getColumnIndex("movieName"));
			String filePath=cursor.getString(cursor.getColumnIndex("filePath"));
			String movieHeadImagePath=cursor.getString(cursor.getColumnIndex("movieHeadImagePath"));
			
			System.out.println(fileSize+movieName+filePath+movieHeadImagePath);
			downItem.setFilePath(filePath);
			downItem.setMovieHeadImagePath(movieHeadImagePath);
			downItem.setFileSize("下载完成");
			downItem.setMovieName(movieName);
			downItem.setDownloadUrl(video_url);
			
		}
		cursor.close();
		db.close();
		return downItem;
	}
}
