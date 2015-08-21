package com.youti.yonghu.activity;

import java.io.File;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.Constants;
import com.youti.appConfig.YoutiApplication;
import com.youti.utils.DownLoadUtil;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
import com.youti.view.TitleBar;
import com.youti.yonghu.bean.LoginBean;
import com.youti.yonghu.bean.VersionBean;

public class MoreActivity extends Activity implements OnClickListener{
	protected static final int DOWNLOAD_SUCCESS = 0;
	protected static final int DOWNLOAD_ERROR = 1;
	Intent intent;
	TitleBar titleBar;
	RelativeLayout rl_service,rl_wenti,rl_shangwu,rl_zhaopin,rl_update,rl_delete,rl_xieyi,rl_xpytj,rl_score,rl_recom;
	Button exit;
	YoutiApplication youtiApplication;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_more);
		titleBar =(TitleBar) findViewById(R.id.index_titlebar);
		titleBar.setSearchGone();
		titleBar.setShareGone(false);
		titleBar.setTitleBarTitle("更多");
		youtiApplication =(YoutiApplication)getApplication();
	
		initView();
		initListener();
	}
	
	public final String mPageName = "MoreActivity";
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart( mPageName );
		MobclickAgent.onResume(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd( mPageName );
		MobclickAgent.onPause(this);
	}
	private void initListener() {
		rl_service.setOnClickListener(this);
		rl_wenti.setOnClickListener(this);
		rl_shangwu.setOnClickListener(this);
		rl_zhaopin.setOnClickListener(this);
		rl_update.setOnClickListener(this);
		rl_delete.setOnClickListener(this);
		rl_xieyi.setOnClickListener(this);
		rl_xpytj.setOnClickListener(this);
		rl_score.setOnClickListener(this);
		rl_recom.setOnClickListener(this);
		exit.setOnClickListener(this);
		
	}
	private void initView() {
		rl_service=(RelativeLayout) findViewById(R.id.rl_service);
		rl_wenti=(RelativeLayout) findViewById(R.id.rl_wenti);
		rl_shangwu=(RelativeLayout) findViewById(R.id.rl_shangwu);
		rl_zhaopin=(RelativeLayout) findViewById(R.id.rl_zhaopin);
		rl_update=(RelativeLayout) findViewById(R.id.rl_update);
		rl_delete=(RelativeLayout) findViewById(R.id.rl_delete);
		rl_xieyi=(RelativeLayout) findViewById(R.id.rl_xieyi);
		rl_xpytj=(RelativeLayout) findViewById(R.id.rl_xpytj);
		rl_score=(RelativeLayout) findViewById(R.id.rl_score);
		rl_recom=(RelativeLayout) findViewById(R.id.rl_recom);
		exit=(Button) findViewById(R.id.exit);
		if(youtiApplication.myPreference.getHasLogin()){
			exit.setVisibility(View.VISIBLE);
		}else{
			exit.setVisibility(View.GONE);
		}
	}
	@Override
	public void onClick(View v) {
		 
		switch (v.getId()) {
		case R.id.rl_service:	
			Utils.showToast(getApplicationContext(), "联系客服");
			final Dialog dialog =new Dialog(MoreActivity.this, R.style.tkdialog);
			View view = View.inflate(MoreActivity.this, R.layout.layout_contactservice, null);
			dialog.setContentView(view);
			view.findViewById(R.id.ll_dial).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					intent =new Intent();
					intent.setAction(Intent.ACTION_DIAL);
					intent.setData(Uri.parse("tel:"+"4006968080"));  
					MoreActivity.this.startActivity(intent);
					dialog.dismiss();
				}
			});
			view.findViewById(R.id.ll_back).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			dialog.show();
			break;
		case R.id.rl_wenti:
			Utils.showToast(getApplicationContext(), "问题反馈");
			intent =new Intent(MoreActivity.this,FeedbackActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_shangwu:
			Utils.showToast(getApplicationContext(), "商务合作");
			intent =new Intent(MoreActivity.this,UserProtocol.class);
			intent.putExtra("title", "商务合作");
			intent.putExtra("url", "http://wtapp.yoti.cn/index.php/Agreement/swhz");
			startActivity(intent);
			break;
		case R.id.rl_zhaopin:
			Utils.showToast(getApplicationContext(), "优体招聘");
			intent =new Intent(MoreActivity.this,UserProtocol.class);
			intent.putExtra("title", "优体招聘");
			intent.putExtra("url", "http://wtapp.yoti.cn/index.php/Agreement/zpyq");
			startActivity(intent);
			break;
		case R.id.rl_update:
			Utils.showToast(getApplicationContext(), "检查新版本");
			requestData();
			break;
		case R.id.rl_delete:
			Utils.showToast(getApplicationContext(), "清除缓存");
			deleteCache();
			
			break;
		case R.id.rl_xieyi:
			Utils.showToast(getApplicationContext(), "用户协议");
			intent =new Intent(MoreActivity.this,UserProtocol.class);
			startActivity(intent);
			break;
		case R.id.rl_xpytj:
			Utils.showToast(getApplicationContext(), "向朋友推荐优体");
			intent =new Intent(getApplicationContext(),SimpleActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_score:
			Utils.openAppInMarket(MoreActivity.this);
			Utils.showToast(getApplicationContext(), "给我们打分");
			break;
		case R.id.rl_recom:
		//	Dialog dialog1 =Utils.getWaitDialog(MoreActivity.this, "请稍等");
			//dialog1.show();
			
			//sendData("13391769131","123456789");
			Utils.showToast(getApplicationContext(), "该版本尚未添加该功能，敬请期待下个版本");
			break;
		case R.id.exit:
			Utils.showToast(getApplicationContext(), "退出当前账号");
			youtiApplication.myPreference.setHasLogin(false);
			youtiApplication.myPreference.setHeadImgPath("");
			youtiApplication.myPreference.setUserId("");
			youtiApplication.myPreference.setTelNumber("");
			youtiApplication.myPreference.setLoginName("");
			exit.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}
	
	
	
	
	 private void deleteCache() {
		 final Dialog deleteDialog =new Dialog(MoreActivity.this, R.style.tkdialog);
		 View deleteViewDialog =View.inflate(MoreActivity.this, R.layout.layout_contactservice, null);
		 ((TextView)deleteViewDialog.findViewById(R.id.tv_content)).setText("确认要清理缓存吗？");
	     ((TextView)deleteViewDialog.findViewById(R.id.tv_dial)).setText("当然了");
	     deleteDialog.setContentView(deleteViewDialog);
	     deleteDialog.show();
	     deleteViewDialog.findViewById(R.id.ll_dial).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 File directory =getCacheDir();
				  if (directory != null && directory.exists() && directory.isDirectory()) {
			            for (File item : directory.listFiles()) {
			                item.delete();
			                System.out.println(item.getName());
			            }
			        }
				  Utils.showToast(MoreActivity.this, "清除缓存成功");
				  deleteDialog.dismiss();
			}
		});
	     deleteViewDialog.findViewById(R.id.ll_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteDialog.dismiss();
			}
		});
		
	}
	//获取当前应用程序的版本号  
    private String getVersion() {  
        String version = "";  
        //获取系统包管理器  
        PackageManager pm = this.getPackageManager();  
        try {  
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);  
            version = info.versionName;  
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
				Utils.showToast(MoreActivity.this, "下载失败");
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
        pd = new ProgressDialog(MoreActivity.this);  
        //设置进度条在显示时的提示消息  
        pd.setMessage("正在下载");  
        //设置下载进度条为水平形状  
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
        
        final Dialog updateDialog =new Dialog(MoreActivity.this, R.style.tkdialog);
        View updateView =View.inflate(MoreActivity.this, R.layout.layout_contactservice, null);
        updateView.findViewById(R.id.ll_title).setVisibility(View.VISIBLE);
        ((TextView)updateView.findViewById(R.id.tv_content)).setVisibility(View.GONE);
        ((TextView)updateView.findViewById(R.id.tv_dial)).setText("下载新版本");
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
                            file = DownLoadUtil.getFile(MoreActivity.this,url, file.getAbsolutePath(), pd);  
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
				
            updateDialog.dismiss();
			}
		});
       
    }  
    String apkUrl="";
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
                			Utils.showToast(MoreActivity.this, "获取地址失败");
                		}
                	}else{
                		Utils.showToast(MoreActivity.this, "获取网络失败");
                	}
                } catch (Exception e) {
                }
            };
        });
	}
}
