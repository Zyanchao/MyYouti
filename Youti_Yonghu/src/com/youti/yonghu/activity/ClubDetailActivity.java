package com.youti.yonghu.activity;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.ab.util.AbDialogUtil;
import com.ab.util.AbToastUtil;
import com.example.youti_geren.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.Constants;
import com.youti.base.BaseActivity;
import com.youti.fragment.ClubCourseListFragment;
import com.youti.fragment.ClubDetailIntroduceFragment;
import com.youti.utils.HttpUtils;
import com.youti.utils.NetTips;

/**
 * 俱乐部 其他课程 详情页
 * @author zyc
 * 2015-2-12
 */
public class ClubDetailActivity extends BaseActivity{

	private Context mContext;
	TextView tv_first;
	TextView tv_second;
	TextView tv1 ,tv2;
	FrameLayout fl_content;
	ArrayList<Fragment> fragmentLists ;
	private ImageView iv_back;
	int screenWidth;
	View indicate_line;
	private ImageView iv_bg;
	Button btZixun,btBuy;
	String id;
	/** * FragmentTabhost*/
	private FragmentTabHost mTabHost;

	/** * 布局填充器*/
	private LayoutInflater mLayoutInflater;

	/*** Fragment数组界面*/
	private Class mFragmentArray[] = { ClubCourseListFragment.class, ClubDetailIntroduceFragment.class};

	/*** 选修卡文字*/
	private String mTextArray[] = { "俱乐部课程", "俱乐部介绍"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.club_detail);
		id = getIntent().getExtras().getString(Constants.KEY_ID);
		mContext = this;
		iv_bg = (ImageView) findViewById(R.id.iv_club);
		getBgPic();
		initView();
		
		
	}
	
	/**
	 * 
	* @Title: getToutiao 
	* @Description: TODO(获取俱乐部背景图片) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void getBgPic() {
		
			if(!NetTips.isNetTips(mContext)){
				//读取缓存 信息
				if(cache.getAsString("clbu_img")!=null){
					String pic = cache.getAsString("clbu_img");
					imageLoader.displayImage(Constants.PIC_CODE+pic, iv_bg, options);
				}
				return;
			}
				
			
			RequestParams params = new RequestParams(); //
			params.put("club_id",id);
			HttpUtils.post(Constants.CLUB_BGPIC, params,
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
								response.toString();
								String state = response.getString("code");
								String info = response.getString("info");
								
								if (state.equals("1")) {
									
									String pic = response.getString("backg_img");
									cache.put("clbu_img", pic);
									imageLoader.displayImage(Constants.PIC_CODE+pic, iv_bg, options);
									AbDialogUtil.removeDialog(mContext);
									
								} else {
									AbToastUtil.showToast(mContext, info);
								}
							} catch (Exception e) {
							}
						};
				});
		}


	private void initView() {
		mLayoutInflater = LayoutInflater.from(this);
		
		// 找到TabHost
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		// 得到fragment的个数
		int count = mFragmentArray.length;
		for (int i = 0; i < count; i++) {
			// 给每个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(mTextArray[i]).setIndicator(getTabItemView(i));
			Bundle bundle = new Bundle(); 
			bundle.putString(Constants.KEY_ID, id);
			// 将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, mFragmentArray[i], bundle);
			// 设置Tab按钮的背景
			mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_detail_tab);
		}
		
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
	}
	
	
	/**
	 *
	 * 给每个Tab按钮设置图标和文字
	 */
	private View getTabItemView(int index) {
		View view = mLayoutInflater.inflate(R.layout.tab_item_view, null);
		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText(mTextArray[index]);
		return view;
	}
	
	
	 public final String mPageName="ClubDetailActivity";
	    
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
