package com.youti.yonghu.download;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.youti_geren.R;

public class DownloadActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_download);
		setmContext(DownloadActivity.this);
		initView();
	}

	

	/** (非 Javadoc) 
	* Title: finish
	* Description:
	* @see android.app.Activity#finish()
	*/ 
	@Override
	public void finish(){
		new AlertDialog.Builder(this)
		.setTitle("提示")
		.setMessage("要将下载任务转入后台吗？") //二次提示
		.setNegativeButton("取消", new OnClickListener(){
			public void onClick(DialogInterface dialog, int which){
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		}).setPositiveButton("确定",  new OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which){
				DownloadActivity.super.finish();
			}
		}).show();
	}
	/**
	 * (非 Javadoc) Title: onClick Description:
	 * 
	 * @param view
	 * @see demo.mydownload.BaseActivity#onClick(android.view.View)
	 */
	

	
	@Override
	public void onClick(View view){
		super.onClick(view);

		Intent i = getServerIntent();
		DownloadMovieItem d = new DownloadMovieItem();
		switch (view.getId()){
		case R.id.download_manager_bt:
			startActivity(new Intent(this, DownloadManagerActivity.class));
			break;
		case R.id.bt_1:
			// 添加下载任务
			d.setDownloadUrl("http://112.126.72.250/ut_app/apk/Youti_Coach.apk");
			d.setFileSize("10M"); // 电影大小
			d.setMovieName("当你老了"); // 电影名称,不包括后缀名.后缀名在下载服务指定为MP4
			d.setDownloadState(DOWNLOAD_STATE_WATTING);// 设置默认的下载状态为等待状态
			i.putExtra(SERVICE_TYPE_NAME, START_DOWNLOAD_MOVIE);
			i.putExtra(DOWNLOAD_TAG_BY_INTENT, d);
			startService(i);
			showTost("添加下载任务成功", getmContext());
			((Button)findViewById(R.id.bt_1)).setText("下载中..");
			((Button)findViewById(R.id.bt_1)).setClickable(false);
			break;
		case R.id.bt_2:
			d.setDownloadUrl("http://192.168.1.128:8080/far.mp4");
			d.setFileSize("10M"); // 电影大小
			d.setMovieName("远方"); // 电影名称,不包括后缀名.后缀名在下载服务指定为MP4
			d.setDownloadState(DOWNLOAD_STATE_WATTING);// 设置默认的下载状态为等待状态
			i.putExtra(SERVICE_TYPE_NAME, START_DOWNLOAD_MOVIE);
			i.putExtra(DOWNLOAD_TAG_BY_INTENT, d);
			startService(i);
			showTost("添加下载任务成功", getmContext());
			((Button)findViewById(R.id.bt_2)).setText("下载中..");
			((Button)findViewById(R.id.bt_2)).setClickable(false);
			break;
		case R.id.bt_3:
			d.setDownloadUrl("http://192.168.1.128:8080/juechang.mp4");
			d.setFileSize("10M"); // 电影大小
			d.setMovieName("绝唱"); // 电影名称,不包括后缀名.后缀名在下载服务指定为MP4
			d.setDownloadState(DOWNLOAD_STATE_WATTING);// 设置默认的下载状态为等待状态
			i.putExtra(SERVICE_TYPE_NAME, START_DOWNLOAD_MOVIE);
			i.putExtra(DOWNLOAD_TAG_BY_INTENT, d);
			startService(i);
			showTost("添加下载任务成功", getmContext());
			((Button)findViewById(R.id.bt_3)).setText("下载中..");
			((Button)findViewById(R.id.bt_3)).setClickable(false);
			break;
		case R.id.bt_4:
			d.setDownloadUrl("http://192.168.1.128:8080/wanlai.mp4");
			d.setFileSize("10M"); // 电影大小
			d.setMovieName("晚来芳"); // 电影名称,不包括后缀名.后缀名在下载服务指定为MP4
			d.setDownloadState(DOWNLOAD_STATE_WATTING);// 设置默认的下载状态为等待状态
			i.putExtra(SERVICE_TYPE_NAME, START_DOWNLOAD_MOVIE);
			i.putExtra(DOWNLOAD_TAG_BY_INTENT, d);
			startService(i);
			showTost("添加下载任务成功", getmContext());
			((Button)findViewById(R.id.bt_4)).setText("下载中..");
			((Button)findViewById(R.id.bt_4)).setClickable(false);
			break;
		case R.id.bt_5:
			d.setDownloadUrl("http://192.168.1.80:8080/wenold.mp4");
			d.setFileSize("10M"); // 电影大小
			d.setMovieName("测试下载5"); // 电影名称,不包括后缀名.后缀名在下载服务指定为MP4
			d.setDownloadState(DOWNLOAD_STATE_WATTING);// 设置默认的下载状态为等待状态
			i.putExtra(SERVICE_TYPE_NAME, START_DOWNLOAD_MOVIE);
			i.putExtra(DOWNLOAD_TAG_BY_INTENT, d);
			startService(i);
			showTost("添加下载任务成功", getmContext());
			((Button)findViewById(R.id.bt_5)).setText("下载中..");
			((Button)findViewById(R.id.bt_5)).setClickable(false);
			break;
		case R.id.bt_6:
			d.setDownloadUrl("http://192.168.1.80:8080/wenold.mp4");
			d.setFileSize("10M"); // 电影大小
			d.setMovieName("测试下载5"); // 电影名称,不包括后缀名.后缀名在下载服务指定为MP4
			d.setDownloadState(DOWNLOAD_STATE_WATTING);// 设置默认的下载状态为等待状态
			i.putExtra(SERVICE_TYPE_NAME, START_DOWNLOAD_MOVIE);
			i.putExtra(DOWNLOAD_TAG_BY_INTENT, d);
			startService(i);
			showTost("添加下载任务成功", getmContext());
			((Button)findViewById(R.id.bt_6)).setText("下载中..");
			((Button)findViewById(R.id.bt_6)).setClickable(false);
			break;

		default:
			break;
		}
	}
}
