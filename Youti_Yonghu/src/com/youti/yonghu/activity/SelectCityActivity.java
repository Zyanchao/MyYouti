package com.youti.yonghu.activity;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.youti_geren.R;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.YoutiApplication;
import com.youti.utils.Utils;
import com.youti.view.MGridView;
import com.youti.view.QuickIndexBar;
import com.youti.view.QuickIndexBar.OnLetterChangeListener;
import com.youti.yonghu.adapter.CityAdapter;
import com.youti.yonghu.bean.City;

public class SelectCityActivity extends FragmentActivity {

	String[] ALLCITIES = new String[] { "上海", "北京", "天津", "重庆", "西安市", "铜川市",
			"宝鸡市", "咸阳市", "渭南市", "延安市", "汉中市", "榆林市", "安康市", "商洛市", "杭州市",
			"宁波市", "温州市", "嘉兴市", "湖州市", "绍兴市", "金华市", "衢州市", "舟山市", "台州市",
			"丽水市", "石家庄市", "唐山市", "秦皇岛市", "邯郸市", "邢台市", "保定市", "张家口市", "承德市",
			"沧州市", "廊坊市", "衡水市", "沈阳市", "大连市", "鞍山市", "抚顺市", "本溪市", "丹东市",
			"锦州市", "营口市", "阜新市", "辽阳市", "盘锦市", "铁岭市", "朝阳市", "葫芦岛市", "济南市",
			"青岛市", "淄博市", "枣庄市", "东营市", "烟台市", "潍坊市", "济宁市", "泰安市", "威海市",
			"日照市", "莱芜市", "临沂市", "德州市", "聊城市", "滨州市", "菏泽市", "广州市", "深圳市",
			"珠海市", "汕头市", "韶关市", "佛山市", "江门市", "湛江市", "茂名市", "肇庆市", "惠州市",
			"梅州市", "汕尾市", "河源市", "阳江市", "清远市", "东莞市", "中山市", "潮州市", "揭阳市",
			"云浮市", "武汉市", "黄石市", "十堰市", "荆州市", "宜昌市", "襄樊市", "鄂州市", "荆门市",
			"孝感市", "黄冈市", "咸宁市", "随州市", "南京市", "无锡市", "徐州市", "常州市", "苏州市",
			"南通市", "连云港市", "淮安市", "盐城市", "扬州市", "镇江市", "泰州市", "宿迁市", "成都市",
			"自贡市", "攀枝花市", "泸州市", "德阳市", "绵阳市", "广元市", "遂宁市", "内江市", "乐山市",
			"南充市", "眉山市", "宜宾市", "广安市", "达州市", "雅安市", "巴中市", "资阳市",
			"阿坝藏族羌族自治州", "甘孜藏族自治州", "凉山彝族自治州"

	};

	String[] HOTCITIES = new String[] { "西安", "上海", "北京", "广州", "深圳", "天津",
			"武汉", "南京", "成都", " 杭州", "重庆" };
	ArrayList<String> hot_List = new ArrayList<String>();
	MGridView gv;
	TextView tv_currentcity;
	EditText et_search;
	TextView tv_result;
	FrameLayout fl_content;
	private String text;
	YoutiApplication youtiApplication;
	ArrayList<String> allList=new ArrayList<String>();

	public final String mPageName ="SelectCityActivity";
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
		setContentView(R.layout.layout_selectcity);
		tv_currentcity=(TextView) findViewById(R.id.tv_currentcity);
		et_search=(EditText) findViewById(R.id.et_search);
		tv_result=(TextView) findViewById(R.id.tv_result);
		fl_content =(FrameLayout) findViewById(R.id.fl_content);
		youtiApplication=YoutiApplication.getInstance();
		tv_currentcity.setText(youtiApplication.myPreference.getCity());
		tv_result.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!text.equals("亲，暂无该地区服务")){
					
					tv_result.setVisibility(View.GONE);
					fl_content.setVisibility(View.VISIBLE);
					
				}
			}
		});
		
		View v = View.inflate(this, R.layout.hotcity_gridview, null);
		gv = (MGridView) v.findViewById(R.id.gv);

		for (int i = 0; i < HOTCITIES.length; i++) {
			hot_List.add(HOTCITIES[i]);
		}

		gv.setAdapter(new MyAdapter());
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Utils.showToast(getApplicationContext(), "小U暂不支持"+hot_List.get(position)+"地区服务");
				//tv_currentcity.setText(hot_List.get(position));
				finish();
			}
		});
		final ListView mListView = (ListView) findViewById(R.id.lv);

		final ArrayList<City> names = new ArrayList<City>();

		fillAndSort(names);
		if (mListView.getHeaderViewsCount() < 1) {
			mListView.addHeaderView(v, null, true);
		}

		mListView.setAdapter(new CityAdapter(this, names));
		/**
		 * 快速查询ListView的条目点击事件
		 */
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position >= 1) {
					Utils.showToast(getApplicationContext(),"小U暂不支持"+
							names.get(position - 1).getName()+"地区服务");
					//tv_currentcity.setText(names.get(position-1).getName());
					finish();
				}

			}
		});

		QuickIndexBar mQuickIndexBar = (QuickIndexBar) findViewById(R.id.quickIndex);
		mQuickIndexBar.setLetterChangeListener(new OnLetterChangeListener() {

			@Override
			public void OnLetterChange(String letter) {
				Utils.showToast(getApplicationContext(), letter);
				// 执行ListView的定位方法
				for (int i = 0; i < names.size(); i++) {
					City friend = names.get(i);
					String l = friend.getPinyin().charAt(0) + "";
					if (TextUtils.equals(letter, l)) {
						// 中断循环，快速定位
						mListView.setSelection(i+1);
						//使用下面的判断，并且上面的设置为setSelection(i)的结果就是点击A的时候，会显示热门城市。
						/*if(i>=1){
							mListView.setSelection(i+1);
						}*/
						//int mdistance =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,SelectCityActivity.this.getResources().getDisplayMetrics());
						//mListView.setSelectionFromTop(i, -mdistance);
						break;
					}
				}

			}
		});
		/**
		 * 搜索框文本变化监听器
		 */
		et_search.addTextChangedListener(new TextWatcher() {
	
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				text = et_search.getText().toString().trim();
				if(allList.contains(text)&&!TextUtils.isEmpty(text)){
					tv_result.setText(text);
				}else{
					tv_result.setText("亲，暂无该地区服务");
				}
				tv_result.setVisibility(View.VISIBLE);
				fl_content.setVisibility(View.GONE);
				
				
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
	/**
	 * 热门城市GridView的适配器
	 * @author Administrator
	 *
	 */
	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return hot_List.size();
		}

		@Override
		public Object getItem(int position) {
			return hot_List.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v;
			if (convertView == null) {
				v = View.inflate(getApplicationContext(),
						R.layout.item_gridview, null);
			} else {
				v = convertView;
			}
			TextView tv = (TextView) v.findViewById(R.id.tv);
			tv.setText(hot_List.get(position));
			return v;
		}

	}

	private void fillAndSort(ArrayList<City> names) {

		for (int i = 0; i < ALLCITIES.length; i++) {
			names.add(new City(ALLCITIES[i]));
			allList.add(ALLCITIES[i]);
		}

		Collections.sort(names);

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK&&tv_result.isShown()  ){
			tv_result.setVisibility(View.GONE);
			fl_content.setVisibility(View.VISIBLE);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
