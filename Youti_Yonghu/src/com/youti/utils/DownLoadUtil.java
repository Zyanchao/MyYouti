package com.youti.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.format.Formatter;
  
/** 
 * Created by sing on 13-12-24. 
 * desc: 
 */  
public class DownLoadUtil {  
  
    /** 
     * 下载文件 
     * @param urlpath 网址 
     * @param filepath 本地文件路径 
     * @param pd 进度条 
     * @return 
     */  
    public static File getFile(Context context,String urlpath, String filepath, ProgressDialog pd) {  
        File file = null;  
  
        try {  
            URL url = new URL(urlpath);  
            file = new File(filepath);  
            FileOutputStream fos = new FileOutputStream(file);  
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
            conn.setRequestMethod("GET");  
            conn.setConnectTimeout(5000);  
  
            //设置文件长度为进度条的最大值  
            int max = conn.getContentLength();  
            String apkSize = Formatter.formatFileSize(context , max );  
            pd.setMax(max);  
  
            
            float all = max/1024/1024;
           
          
            InputStream is = conn.getInputStream();  
            byte[] buffer = new byte[1024];  
            int len = 0;  
            int progress = 0;  
            while ( (len = is.read(buffer))!=-1 ) {  
                fos.write(buffer, 0 , len);  
                progress += len;  
                pd.setProgress(progress);  
                float percent = progress/1024/1024;
                pd.setProgressNumberFormat(String.format("%.2fM/%.2fM", percent, all));
                
                Thread.sleep(30);  
            }  
  
            fos.flush();  
            fos.close();  
            is.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return file;  
    }  
  
    /** 
     * 获取网址路径中的文件名 
     * @param urlpath 
     * @return 
     */  
    public static String getFileName(String urlpath) {  
        return urlpath.substring(urlpath.lastIndexOf("/") + 1, urlpath.length());  
    }  
}  