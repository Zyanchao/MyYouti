package com.youti.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;


@SuppressLint("NewApi")
public class ImageCacheUtils {
	
	public static final int SUCCESS = 50;
	public static final int FAIL = 60;
	private LruCache<String, Bitmap> lruCache;
	private File cacheDir;
	private ExecutorService newFixedThreadPool;
	Handler handler;

	public ImageCacheUtils(Context context,Handler handler){
		int maxSize=(int)(Runtime.getRuntime().maxMemory()/8);
		lruCache = new LruCache<String, Bitmap>(maxSize){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes()*value.getHeight();
			}
		};
		cacheDir = context.getCacheDir();
		
		newFixedThreadPool = Executors.newFixedThreadPool(5);
		this.handler=handler;
	}
	
	
	@SuppressLint("NewApi")
	public Bitmap getBitmap(String imgUrl){
		Bitmap bitmap=null;
		
		bitmap=lruCache.get(imgUrl);
		if(bitmap!=null){
			return bitmap;
		}
		
		bitmap =getBitmapfromLocal(imgUrl);
		if(bitmap!=null){
			return bitmap;
		}
		
		getBitmapFromNet(imgUrl);
		return null;
	}


	private void getBitmapFromNet(String imgUrl) {
		newFixedThreadPool.execute(new RunnableTask(imgUrl));
	}

	class  RunnableTask implements Runnable{
		String imageUrl;
		public RunnableTask(String imgUrl){
			this.imageUrl=imgUrl;
		}
		@Override
		public void run() {
			Message msg= Message.obtain();
			try {
				URL url =new URL(imageUrl);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setReadTimeout(5000);
				con.setConnectTimeout(5000);
				con.setRequestMethod("GET");
				System.out.println(con.getResponseCode()+"&&&&center");
				InputStream inputStream = con.getInputStream();
				Bitmap bitmap =BitmapFactory.decodeStream(inputStream);
				
				
				msg.what=SUCCESS;
				msg.obj=bitmap;
				handler.sendMessage(msg);
			
				lruCache.put(imageUrl, bitmap);
				
				writeToLocal(imageUrl,bitmap);
				return;			
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			
				msg.what=FAIL;
				handler.sendMessage(msg);
		}
		
	}
	@SuppressLint("NewApi")
	private Bitmap getBitmapfromLocal(String imgUrl) {
		try {
			String picName= MD5Encoder.encode(imgUrl).substring(10);
			File file = new File(cacheDir, picName);
			Bitmap bitmap =BitmapFactory.decodeFile(file.getAbsolutePath());
			lruCache.put(imgUrl, bitmap);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}


	public void writeToLocal(String imageUrl, Bitmap bitmap) {
		try {
			String picName =MD5Encoder.encode(imageUrl).substring(10);
			File file =new File(cacheDir,picName);
			FileOutputStream fos =new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 100, fos);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
