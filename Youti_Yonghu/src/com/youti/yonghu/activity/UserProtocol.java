package com.youti.yonghu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.example.youti_geren.R;
import com.umeng.analytics.MobclickAgent;
import com.youti.view.TitleBar;

public class UserProtocol extends Activity {
	TitleBar titleBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_userprotocol);
		if(getIntent()!=null){
			url = getIntent().getStringExtra("url");
			title = getIntent().getStringExtra("title");
		}
		init();
	}

	public final String mPageName ="UserProtocol";
	private String url;
	private String title;
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
	private void init() {
		// TODO Auto-generated method stub
		TitleBar titleBar = (TitleBar) findViewById(R.id.index_titlebar);
		titleBar.setSearchGone();
		titleBar.setShareGone(false);
		titleBar.setTitleBarTitle(title);
		
		WebView mWebPage = (WebView) findViewById(R.id.webView);
		mWebPage.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		mWebPage.loadUrl(url);
	};
}
