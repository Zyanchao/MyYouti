package com.youti.yonghu.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.bitmap.AbImageDownloader;
import com.alibaba.fastjson.JSON;
import com.example.youti_geren.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.Constants;
import com.youti.utils.HttpUtils;
import com.youti.view.NoScrollGridView;
import com.youti.yonghu.bean.Option;

//首頁 更多  項目
public class MoreOptionActivity extends AbActivity{
	
	private ListView mListView;
	private List<Option> mOption = new ArrayList<Option>();
	private MyAdapter adapter;
	public static int REQUEST = 1;
	// 图片下载类
	//private AbImageLoader mAbImageLoader = null;
	private AbImageDownloader mAbImageLoader = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.new_option);
		// 图片的下载
		mAbImageLoader = new AbImageDownloader(this);
		iniView();
		Getqbxm();
		iniData();
	}
	
	public final String mPageName = "MoreOptionActivity";
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
	
	private void Getqbxm() {
		RequestParams params = new RequestParams(); // 绑定参数
		HttpUtils.post(Constants.HOME_MORE_ITEM, params,
				new JsonHttpResponseHandler() {
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
							org.json.JSONObject response) {
						try {
							// 接口返回
							response.toString();
							String state = response.getString("code");
							if (state.equals("1")) {
								com.alibaba.fastjson.JSONObject object = JSON
										.parseObject(response.toString());
								com.alibaba.fastjson.JSONArray jsonArray = object
										.getJSONArray("list");
								mOption = JSON.parseArray(
										jsonArray.toString(), Option.class);
								adapter.notifyDataSetChanged();
							} else {

							}
						} catch (Exception e) {
						}
					};
				});
	}

	private void iniView() {
		mListView = (ListView) findViewById(R.id.listView);
		mLayout = (RelativeLayout) findViewById(R.id.backBtn);
		adapter = new MyAdapter();
		mListView.setAdapter(adapter);
	}

	private void iniData() {
		mLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	private int itm;
	private RelativeLayout mLayout;
	class MyAdapter extends BaseAdapter {


		@Override
		public int getCount() {
			return mOption.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int pas, View arg1, ViewGroup arg2) {
			System.out.println("Item"+pas);
			ViewHolder holder = null;
			if (arg1 == null) {
				holder = new ViewHolder();
				arg1 = LayoutInflater.from(MoreOptionActivity.this).inflate(R.layout.listview_item, null, false);
				holder.imageView = (TextView) arg1.findViewById(R.id.new_mc);
				holder.gridView = (NoScrollGridView) arg1.findViewById(R.id.listview_item_gridview);
				arg1.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();
			}
			holder.imageView .setText(mOption.get(pas).getTypename() );
			GridViewAdapter gridViewAdapter = new GridViewAdapter(pas);
			holder.gridView.setAdapter(gridViewAdapter);
			return arg1;
		}

	}
	private class ViewHolder {
		TextView imageView;
		NoScrollGridView gridView;
	}
	class GridViewAdapter extends BaseAdapter {
		int pa =0;
		private String val;
		public GridViewAdapter(int pa) {
			super();
			this.pa = pa;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mOption.get(pa).getCoachtype().size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			GViewHolder holder = null;
			if (arg1 == null) {
				holder = new GViewHolder();
				arg1 = LayoutInflater.from(MoreOptionActivity.this).inflate(
						R.layout.gridview_item, null, false);
				holder.img = (ImageView) arg1
						.findViewById(R.id.gridview_item_img);
				
				holder.tv = (TextView) arg1
						.findViewById(R.id.gridview_item_tv);
				
				holder.ids = (TextView) arg1.findViewById(R.id.ids);
				holder.qxm = (LinearLayout)arg1.findViewById(R.id.new_qxm);
				arg1.setTag(holder);
				
			}else {
				holder = (GViewHolder) arg1.getTag();
			}
			mAbImageLoader.display(holder.img, Constants.PIC_CODE+mOption.get(pa).getCoachtype().get(arg0).getPic());
			val = mOption.get(pa).getCoachtype().get(arg0).getVal();
			holder.tv.setText(mOption.get(pa).getCoachtype().get(arg0).getVal());
			holder.ids.setText(mOption.get(pa).getCoachtype().get(arg0).getId());
			holder.qxm.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Bundle b = new Bundle();
					b.putString("val", val);
					/*((YoutiApplication) getApplication()).coachSearchInfo.type =mTextView.getText().toString();
					((YoutiApplication) getApplication()).coachSearchInfo.typeId = Integer.parseInt(mTextViews.getText().toString());*/
					nextActivity(CoachListActivity.class, b);
				}
			});
			return arg1;
		}
	}
	private class GViewHolder {
		TextView tv;
		TextView ids;
		ImageView   img;
		LinearLayout  qxm;
		
	}
	private void nextActivity(Class<?> next, Bundle b) {
		Intent intent = new Intent();
		intent.setClass(this, next);
		if (b != null) {
			intent.putExtras(b);
		}
		startActivityForResult(intent, REQUEST);
	}
}
