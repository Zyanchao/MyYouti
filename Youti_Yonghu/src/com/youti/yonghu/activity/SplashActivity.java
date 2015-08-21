package com.youti.yonghu.activity;

import java.io.File;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youti_geren.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.youti.UpdateManager;
import com.youti.appConfig.Constants;
import com.youti.appConfig.YoutiApplication;
import com.youti.utils.DownLoadUtil;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;

public class SplashActivity extends Activity {
	YoutiApplication youtiApplicaiton;
	private boolean isFirst;
	private Intent intent;
	private ImageView iv_splash;
	private ImageView iv_logo;
	private TextView tv_name;
	LinearLayout ll_anim;
	
	protected static final int DOWNLOAD_SUCCESS = 0;
	protected static final int DOWNLOAD_ERROR = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_splash);
		youtiApplicaiton=YoutiApplication.getInstance();
		isFirst = youtiApplicaiton.myPreference.getIsFirst();
		iv_splash = (ImageView) findViewById(R.id.iv_splash);
		iv_logo=(ImageView) findViewById(R.id.iv_logo);
		tv_name =(TextView) findViewById(R.id.tv_name);
		ll_anim=(LinearLayout) findViewById(R.id.ll_anim);
		
		
		Animation anim =AnimationUtils.loadAnimation(SplashActivity.this, R.anim.fade_splash);		

		iv_splash.startAnimation(anim);
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				/*if(isFirst){
					intent = new Intent(SplashActivity.this,WelcomeActivity.class);				
				}else{
					intent= new Intent(SplashActivity.this,MainMainActivity.class);
				}
				startActivity(intent);
				finish();		*/		
				
				requestData();	
			}
		}, 1000);
		
		
	}
	
	
	//获取当前应用程序的版本号  
    private int getVersion() {  
        int version = 0;  
        //获取系统包管理器  
        PackageManager pm = this.getPackageManager();  
        try {  
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);  
            version = info.versionCode;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return version;  
    }  
    
    Handler handler = new Handler(){
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
			case DOWNLOAD_SUCCESS:
			    //得到消息中的文件对象，并安装  
			    File file = (File)msg.obj;  
			    Intent intent = new Intent();  
		        intent.setAction("android.intent.action.VIEW");  
		        intent.addCategory("android.intent.category.DEFAULT");  
		        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");  
		        startActivity(intent);  
				break;
			case DOWNLOAD_ERROR:
				Utils.showToast(SplashActivity.this, "下载失败");
				break;
			default:
				break;
			}
    	};
    };
    ProgressDialog pd;
    /** 
     * 显示升级提示对话框 
     * @param versionBean 
     */  
    protected void showUpdateDialog(final String url) {  
        //创建下载进度条  
        pd = new ProgressDialog(SplashActivity.this);  
        //设置进度条在显示时的提示消息  
        pd.setMessage("正在下载");  
        //设置下载进度条为水平形状  
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
        
        final Dialog updateDialog =new Dialog(SplashActivity.this, R.style.tkdialog);
        View updateView =View.inflate(SplashActivity.this, R.layout.layout_contactservice, null);
        updateView.findViewById(R.id.ll_title).setVisibility(View.VISIBLE);
        ((TextView)updateView.findViewById(R.id.tv_content)).setVisibility(View.GONE);
        ((TextView)updateView.findViewById(R.id.tv_dial)).setText("下载新版本");
       // ((TextView)updateView.findViewById(R.id.tv_back)).setText("以后再说");
        ((TextView)updateView.findViewById(R.id.tv_back)).setText("以后再说");
        updateDialog.setContentView(updateView);
        updateDialog.show();
        updateView.findViewById(R.id.ll_dial).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//开始下载
            	//判断sdcard是否存在  
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  
                    //显示下载进度条  
                    pd.show();  
                    //开启线程下载apk文件  
                    new Thread() {  
                        public void run() {  
                            File file = new File(Environment.getExternalStorageDirectory(), DownLoadUtil.getFileName(url));  
                            file = DownLoadUtil.getFile(SplashActivity.this,url, file.getAbsolutePath(), pd);  
                            if (file != null) {  
                                //下载成功  
                                Message msg = Message.obtain();  
                                msg.what = DOWNLOAD_SUCCESS;  
                                msg.obj = file;  
                                handler.sendMessage(msg);  
                            }else {  
                                //下载失败  
                                Message msg = Message.obtain();  
                                msg.what = DOWNLOAD_ERROR;  
                                handler.sendMessage(msg);  
                            }  
  
                            //下载完毕，关闭进度条  
                            pd.dismiss();  
                            
                        };  
                    }.start();  
                }else{  
                    Toast.makeText(getApplicationContext(), "sd卡不可用", 1).show();  
                    //进入主界面  
                   // loadMainUI();  
                }  
			}
		});
        
        updateView.findViewById(R.id.ll_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				enterNext();
				 updateDialog.dismiss();
				
           
			}
		});
       
    }  
    
  //返回的安装包url
  	private String apkUrl = "";
    //访问数据库
	private void requestData() {
		
		
		RequestParams params =new RequestParams();
		
		params.put("version_id", getVersion());
		params.put("os_type",YoutiApplication.getInstance().OS_TYPE);//0
		params.put("app_type",YoutiApplication.getInstance().APP_TYPE);//1
		
        HttpUtils.post(Constants.CHECK_UPDATE, params, new JsonHttpResponseHandler() {
            public void onStart() {  
                super.onStart();  
            } 
            public void onFailure(int statusCode,
                    org.apache.http.Header[] headers,
                    java.lang.Throwable throwable,
                    org.json.JSONObject errorResponse) {
            	enterNext();
            };
            public void onFinish() {
    
            };
            public void onSuccess(int statusCode,
                    org.apache.http.Header[] headers,
                    org.json.JSONObject response){ 
                try {
                	String state =  response.getString("state");
                	if(state.equals("1")){
                		JSONObject info = response.getJSONObject("msg");
                		if(info.has("dl_url")) apkUrl = info.getString("dl_url");
                		if(apkUrl.length()>0){
                			//showNoticeDialog();
                		
                			//更新对话框，提示是否更新
    						showUpdateDialog(apkUrl);
                		}else{
                			enterNext();
                		}
                	}else{
                		enterNext();
                	}
                } catch (Exception e) {
                }
            };
        });
		
		
		
		
		/*params.put("system", "1");
		HttpUtils.post("http://112.126.72.250/ut_app/index.php?m=User&a=check_version", params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson =new Gson();
				VersionBean versionBean = gson.fromJson(arg2, VersionBean.class);
				
				if(versionBean.code.equals("1")){
					//如果版本号不相等，则提示
					if(!versionBean.list.version_num.equals(getVersion())){
						//更新对话框，提示是否更新
						showUpdateDialog(versionBean);
					}else{
						enterNext();
						//Utils.showToast(SplashActivity.this, "恭喜您，当前已是最新版本");
						
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				//Utils.showToast(SplashActivity.this, "当前网络访问异常");
				enterNext();
			}
		});*/
	}
	
	private long readSDCard() { 
        String state = Environment.getExternalStorageState(); 
        if(Environment.MEDIA_MOUNTED.equals(state)) { 
            File sdcardDir = Environment.getExternalStorageDirectory(); 
            StatFs sf = new StatFs(sdcardDir.getPath()); 
            long blockSize = sf.getBlockSize(); 
            long blockCount = sf.getBlockCount(); 
            long availCount = sf.getAvailableBlocks(); 
            
            return availCount*blockSize;
          //  Log.d("", "block大小:"+ blockSize+",block数目:"+ blockCount+",总大小:"+blockSize*blockCount/1024+"KB"); 
         //   Log.d("", "可用的block数目：:"+ availCount+",剩余空间:"+ availCount*blockSize/1024+"KB"); 
        } 
        return 0;
    } 
	
	public void enterNext(){
		handler.postDelayed(new Runnable() {	

			@Override
			public void run() {
				//requestData();				
				if(isFirst){
					intent = new Intent(SplashActivity.this,WelcomeActivity.class);				
				}else{
					intent= new Intent(SplashActivity.this,MainMainActivity.class);
				}
				startActivity(intent);
				finish();
			}
		}, 500);
	}
	
}
