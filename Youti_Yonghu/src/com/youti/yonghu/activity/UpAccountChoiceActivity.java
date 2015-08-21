package com.youti.yonghu.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.umeng.analytics.MobclickAgent;

/**
* @ClassName: SettingChoiceRechargeMoneyActivity 
* @Description: TODO(选择 充值 界面) 
* @author zychao 
* @date 2015-6-25 下午3:53:16 
*/
@SuppressLint("StringFormatMatches")
public class UpAccountChoiceActivity extends Activity{
	
	private ListView mListView;
	private Button mButton;
	
	private List<ReChargeEntry> mChargeEntryList;
	private ReChargeAdapter mReChargeAdapter;
	private SharedPreferences mPreference;
	private double mCurrentMoney;
	
	public final String mPageName ="UpAccountChoiceActivity";
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
		setContentView(R.layout.setting_choice_recharge_money_activity);
		
		initView();
		initListener();
	}

	private void initView(){
		mListView = (ListView)findViewById(R.id.listview);
		mButton = (Button)findViewById(R.id.button);
		
		mChargeEntryList = new ArrayList<ReChargeEntry>();
		mReChargeAdapter = new ReChargeAdapter();
		
		
		mListView.setAdapter(mReChargeAdapter);
		mPreference = PreferenceManager.getDefaultSharedPreferences(this);
		mCurrentMoney = mPreference.getInt("setting_recharge_money", 100);
	}
	
	private void initListener() {
		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.d("cc", "mCurrentMoney : "+mCurrentMoney);
				result();
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ReChargeEntry entry = mChargeEntryList.get(arg2);
				//mPreference.edit().putInt(PreferenceContext.SETTING_RECHARGE_MONEY, entry.reChargeMoney).commit();
				mCurrentMoney = entry.reChargeMoney;
				mReChargeAdapter.notifyDataSetChanged();
			}
		});
	}

	protected void result() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("money", mCurrentMoney);
		setResult(RESULT_OK, intent);
		finish();
	}

	
	private class ReChargeEntry {
		private double reChargeMoney;
		private int giveMoney;
//		private boolean isSelected;
	} 
	
	private class ReChargeHolder{
		private TextView title;
		private ImageView selectd;
	}
	
	private class ReChargeAdapter extends BaseAdapter{

		private LayoutInflater mInflater = LayoutInflater.from(UpAccountChoiceActivity.this);
		
		@Override
		public int getCount() {
			return mChargeEntryList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mChargeEntryList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@SuppressLint("StringFormatMatches")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ReChargeHolder holder;
			if(convertView == null){
				holder = new ReChargeHolder();
				convertView = mInflater.inflate(R.layout.setting_choice_recharge_money_list_item, parent, false);
				holder.title = (TextView)convertView.findViewById(R.id.title);
				holder.selectd = (ImageView)convertView.findViewById(R.id.selected);
				convertView.setTag(holder);
			}else{
				holder = (ReChargeHolder)convertView.getTag();
			}
			
			ReChargeEntry entry = (ReChargeEntry)getItem(position);
			if(entry.giveMoney == 0){
				holder.title.setText(getResources().getString(R.string.setting_recharge_item,entry.reChargeMoney));
			}else{
				holder.title.setText(getResources().getString(R.string.setting_recharge_item,entry.reChargeMoney,entry.giveMoney));
			}
			
			if (entry.reChargeMoney == mCurrentMoney) {
				holder.selectd.setVisibility(View.VISIBLE);
			} else {
				holder.selectd.setVisibility(View.INVISIBLE);
			}
			
//			if(entry.isSelected){
//				holder.selectd.setVisibility(View.VISIBLE);
//			}else{
//				holder.selectd.setVisibility(View.INVISIBLE);
//			}
			
			
			return convertView;
		}
		
	}
	
}
