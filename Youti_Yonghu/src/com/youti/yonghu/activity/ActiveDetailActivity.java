package com.youti.yonghu.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.youti_geren.R;
import com.umeng.analytics.MobclickAgent;
import com.youti.base.BaseActivity;
import com.youti.view.TitleBar;

/**
 * 
* @ClassName: ActiveDetailActivity 
* @Description: TODO(活动详情) 
* @author zychao 
* @date 2015-7-2 上午10:25:14 
*
 */
public class ActiveDetailActivity extends BaseActivity{
	
	private WebView webview; 
	private TitleBar titleBar;
	private String url;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.activie_page_detail); 
		 
		// String title = getIntent().getStringExtra("title");
		 url = getIntent().getExtras().getString("url");
		/* titleBar= (TitleBar) findViewById(R.id.index_titlebar);
		 titleBar.setTitleBarTitle(title);*/
		 webview = (WebView) findViewById(R.id.active_detail_webview);  
		 
		 webview.setWebChromeClient(new WebChromeClient() {  
	            public void onProgressChanged(WebView view, int progress) {  
	                // Activity和Webview根据加载程度决定进度条的进度大小  
	                // 当加载到100%的时候 进度条自动消失  
	             ActiveDetailActivity.this.setProgress(progress * 100);  
	            }  
	        });  
	     WebSettings webSettings = webview.getSettings();  
	     //设置WebView属性，能够执行Javascript脚本    
	     webSettings.setJavaScriptEnabled(true);    
	     //设置可以访问文件  
	     webSettings.setAllowFileAccess(true);  
	     //设置支持缩放  
	     webSettings.setBuiltInZoomControls(true);  
	     //加载需要显示的网页    
	     webview.loadUrl(url);    
	     //设置Web视图    
	     webview.setWebViewClient(new webViewClient ());    
	}
	
	  @Override   
	    //设置回退    
	    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法    
	    public boolean onKeyDown(int keyCode, KeyEvent event) {    
	        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {    
	            webview.goBack(); //goBack()表示返回WebView的上一页面    
	            return true;    
	        }    
	        finish();//结束退出程序  
	        return false;    
	    }    
	        
	    //Web视图    
	    private class webViewClient extends WebViewClient {    
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {    
	            view.loadUrl(url);    
	            return true;    
	        }    
	    }    

	    public final String mPageName="ActiveDetailActivity";
	    
	    @Override
		protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
			MobclickAgent.onPageEnd(mPageName);
			MobclickAgent.onPause(this);
		}

		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
			MobclickAgent.onPageStart(mPageName);
			MobclickAgent.onResume(this);
		}
}
