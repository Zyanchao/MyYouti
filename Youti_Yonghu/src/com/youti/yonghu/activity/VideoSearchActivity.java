package com.youti.yonghu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.youti_geren.R;
import com.umeng.analytics.MobclickAgent;
import com.youti.yonghu.bean.VedioItem;


public class VideoSearchActivity extends Activity {
	ListView lv_videoSearch;
	ArrayList<VedioItem> list =new ArrayList<VedioItem>();
	EditText et_search;
	private String text;
	
	public final String mPageName = "VideoSearchActivity";
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_videosearch);
		lv_videoSearch=(ListView) findViewById(R.id.lv_search);
		et_search =(EditText) findViewById(R.id.et_search);
		
		et_search.addTextChangedListener(new TextWatcher() {
			
		

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				text = et_search.getText().toString().trim();
				if(!TextUtils.isEmpty(text)){
					
					lv_videoSearch.setAdapter(new MyAdapter());
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
	}
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v =View.inflate(VideoSearchActivity.this, R.layout.video_list_item1, null);
			return v;
		}
		
	}
}
