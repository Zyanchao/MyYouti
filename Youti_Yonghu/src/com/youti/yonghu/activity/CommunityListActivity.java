package com.youti.yonghu.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.youti_geren.R;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.YoutiApplication;
import com.youti.base.BaseActivity;
import com.youti.fragment.OnLineConsultFragment;
import com.youti.fragment.PopularPersonShowFragment;
import com.youti.utils.DistanceUtils;
import com.youti.utils.PopWindowUtil;
import com.youti.utils.StringUtil;
import com.youti.view.TitleBar;
import com.youti.yonghu.adapter.FilterItenAdapter;
import com.youti.yonghu.bean.FilterItem;

public class CommunityListActivity extends BaseActivity implements OnClickListener{
	TitleBar titleBar;
	TextView tv_first;
	TextView tv_second;
	TextView tv1 ,tv2,tv_issue,tv_issued;
	FrameLayout fl_content;
	ViewPager view_pager;
	ArrayList<Fragment> list =new ArrayList<Fragment>();
	int screenWidth;
	View indicate_line;
	View pview,popView;
	private PopupWindow popupWindow;
	PopWindowUtil popu;
	ListView mItemList;
	ImageView add;
	private PopupWindow popup;
	
	private int buttomHeight,screenHeight;
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_community);
		
		initView();
		initListener();
		initData();
		getFilterItem();
	
	}
	private void initData() {
		titleBar.setTitleBarTitle(getResources().getString(R.string.left_menu_sq));
		OnLineConsultFragment ocf=new OnLineConsultFragment();
		PopularPersonShowFragment ppsf =new PopularPersonShowFragment();
		
		list.add(ocf);
		list.add(ppsf);
		
		view_pager.setAdapter(new MyAdapter(getSupportFragmentManager()));
		
		
	}
	
	@Override
	protected void onDestroy() {		
		super.onDestroy();
		if(popup!=null){
			popup.dismiss();
		}
	}
	private void initListener() {
		tv_second.setOnClickListener(this);
		tv_first.setOnClickListener(this);
		tv_issue.setOnClickListener(this);
		tv_issued.setOnClickListener(this);
		titleBar.getAddIcon().setOnClickListener(this);
		
		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		ViewPropertyAnimator.animate(indicate_line).translationX(screenWidth/5).setDuration(0);
		
		view_pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				lightAndScaleTabTitle();
				int targetPosition=0;
				if(position==0){
					targetPosition = screenWidth/5 ;
					titleBar.setSearchGone(true);
					titleBar.setAddVisiable(false);
					titleBar.setShareGone(false);
					llFooter.setVisibility(View.GONE);
				}else{
					targetPosition = screenWidth/5+screenWidth/2 ;
					titleBar.setSearchGone(true);
					titleBar.setAddVisiable(true);
					titleBar.setShareGone(false);
					llFooter.setVisibility(View.GONE);
					
				}
				ViewPropertyAnimator.animate(indicate_line).translationX(targetPosition).setDuration(0);
			}
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				
				
			}
			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
		
		
	}
	private void initView() {
		titleBar =(TitleBar) findViewById(R.id.index_titlebar);
		titleBar.setSearchGone(false);
		titleBar.setAddVisiable(false);
		titleBar.setShareGone(false);
		tv_second=(TextView) findViewById(R.id.tv_second);
		tv_first=(TextView) findViewById(R.id.tv_first);
		view_pager=(ViewPager) findViewById(R.id.view_pager);
		fl_content= (FrameLayout) findViewById(R.id.fl_content);
		indicate_line =findViewById(R.id.indicate_line);
		
		
		llOne=(LinearLayout) findViewById(R.id.footer_one);
		llTwo=(LinearLayout) findViewById(R.id.footer_two);
		llThree=(LinearLayout) findViewById(R.id.footer_three);
		llFooter=(LinearLayout) findViewById(R.id.ll_footer);
		llFooter.setVisibility(View.VISIBLE);
		llFooter.setVisibility(View.GONE);
		titleBar.setSearchGone();
		
		
		ivOne = (ImageView)findViewById(R.id.im_one);
		ivTwo = (ImageView)findViewById(R.id.im_two);
		ivThree = (ImageView)findViewById(R.id.im_three);
		
		pview = mInflater.inflate(R.layout.pop_list, null);
		popView = pview.findViewById(R.id.mLayout);		
		mItemList = (ListView) pview.findViewById(R.id.lv_pop);
		
		llOne.setOnClickListener(this);
		llThree.setOnClickListener(this);
		llTwo.setOnClickListener(this);
		
		View view =View.inflate(this, R.layout.popup_issue, null);
		tv_issue=(TextView) view.findViewById(R.id.tv_issue);
		tv_issued=(TextView)view.findViewById(R.id.tv_issueed);
		popup = new PopupWindow(view,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		popup.setBackgroundDrawable(getResources().getDrawable(R.drawable.sq_addbox));
		popup.setTouchable(true);
		popup.setFocusable(true); 
		
		//popup.setOutsideTouchable(true);//设置外部能点击
		
		/*
		popup.setTouchInterceptor(new View.OnTouchListener() {  
	         @Override
			public boolean onTouch(View v, MotionEvent event) {  
	             *//****   如果点击了popupwindow的外部，popupwindow也会消失 ****//* 
	             if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {  
	            	 popup.dismiss();  
	                 return false;   
	             }  
	             return false;  
	         }  
		 });*/
	}
	
	
	/**
	 * 获取 底部 筛选条件
	 */
	private FilterItenAdapter fAdapter;
	private FilterItem filterItem;
	private List<FilterItem> filterItems = new ArrayList<FilterItem>();
	private void getFilterItem() {
		InputStream inputStream;
		 try {
			inputStream = CommunityListActivity.this.getAssets().open("data/filter_kc_item.josn");
			String json = StringUtil.readTextFile(inputStream);
			//String state = json.getString("state");
				com.alibaba.fastjson.JSONObject object = JSON
						.parseObject(json.toString());
				com.alibaba.fastjson.JSONArray jsonArray = object
						.getJSONArray("msg");
				filterItems = JSON.parseArray(jsonArray.toString(),FilterItem.class);
				fAdapter = new FilterItenAdapter(getApplicationContext(), filterItems);
				mItemList.setAdapter(fAdapter);

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	boolean isVisiable=false;
	
	
	
	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
			case R.id.tv_second:
				view_pager.setCurrentItem(1);				
				break;
			case R.id.tv_first:			
				view_pager.setCurrentItem(0);
				break;
			case R.id.add:
				/*//Toast.makeText(this, "1", 0).show();
				
				if(isVisiable){
					//popup.dismiss();
					Toast.makeText(this, "1", 0).show();
					isVisiable=false;
				
				}else{*/
				//	Toast.makeText(this, "2", 0).show();
				int [] location=new int[2];
				v.getLocationInWindow(location);
					//popup.showAtLocation(titleBar.getAddIcon(), Gravity.RIGHT, location[0]+20, location[1]);
					popup.showAsDropDown(titleBar.getAddIcon(),getWindowManager().getDefaultDisplay().getWidth()-200,50);
					isVisiable=true;
					break;
				//}
			case R.id.tv_issue:
				Toast.makeText(this, "1", 0).show();
				intent = new Intent(this,IssuePhotoActivity.class);
				startActivity(intent);
				break;
			case R.id.tv_issueed:
				hasLogin = ((YoutiApplication)getApplication()).myPreference.getHasLogin();
				if(hasLogin){
					intent =new Intent(this,MyIssuedPhotoActivity.class);
					startActivity(intent);					
				}else{
					intent =new Intent(this,LoginActivity.class);
					startActivity(intent);
				}
				break;
			case R.id.footer_one:
				
				llOne.setBackgroundResource(R.color.chosed_ll_buttom);
				llTwo.setBackgroundResource(R.color.white);
				llThree.setBackgroundResource(R.color.white);
				ivOne.setBackgroundResource(R.drawable.jl_arrow_h);
				ivTwo.setBackgroundResource(R.drawable.jl_arrow);
				ivThree.setBackgroundResource(R.drawable.jl_arrow);
				popu = new PopWindowUtil(abApplication, screenWidth*1/3, screenHeight*7/12,0,buttomHeight, pview, popView);
				mItemList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						PopWindowUtil.colsePop();
					}
				});
				break;

			case R.id.footer_two:
				llOne.setBackgroundResource(R.color.white);
				llTwo.setBackgroundResource(R.color.chosed_ll_buttom);
				llThree.setBackgroundResource(R.color.white);
				ivOne.setBackgroundResource(R.drawable.jl_arrow);
				ivTwo.setBackgroundResource(R.drawable.jl_arrow_h);
				ivThree.setBackgroundResource(R.drawable.jl_arrow);
				popu = new PopWindowUtil(abApplication, screenWidth*1/3, screenHeight*7/12,screenWidth*1/3,buttomHeight, pview, popView);
				break;
			case R.id.footer_three:
		
				llOne.setBackgroundResource(R.color.white);
				llTwo.setBackgroundResource(R.color.white);
				llThree.setBackgroundResource(R.color.chosed_ll_buttom);
				ivOne.setBackgroundResource(R.drawable.jl_arrow);
				ivTwo.setBackgroundResource(R.drawable.jl_arrow);
				ivThree.setBackgroundResource(R.drawable.jl_arrow_h);
				popu = new PopWindowUtil(abApplication, screenWidth*1/3, screenHeight*7/12,screenWidth*2/3,buttomHeight, pview, popView);
				break;
				
		}
	}
	
	
	
	
	class MyAdapter extends FragmentPagerAdapter{

		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			
			return list.get(arg0);
		}

		@Override
		public int getCount() {
			return list.size();
		}


		
	}
	
	private void lightAndScaleTabTitle(){
		int currentPage = view_pager.getCurrentItem();
		
		tv_first.setTextColor(currentPage==0?Color.parseColor("#6049a1")
				:Color.parseColor("#333333"));
		tv_second.setTextColor(currentPage==1?Color.parseColor("#6049a1")
				:Color.parseColor("#333333"));
		
		ViewPropertyAnimator.animate(tv_first).scaleX(currentPage==0?1f:0.8f).setDuration(200);
		ViewPropertyAnimator.animate(tv_first).scaleY(currentPage==0?1f:0.8f).setDuration(200);
		ViewPropertyAnimator.animate(tv_second).scaleX(currentPage==1?1f:0.8f).setDuration(200);
		ViewPropertyAnimator.animate(tv_second).scaleY(currentPage==1?1f:0.8f).setDuration(200);
	}
	
	private LinearLayout llFooter;
	private LinearLayout llOne;
	private LinearLayout llThree;
	private LinearLayout llTwo;
	
	private ImageView ivOne,ivTwo,ivThree;
	//是否登录
	private boolean hasLogin;
	private Intent intent;
	
	/**
	 * 计算 底部菜单 高度
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		
		ViewTreeObserver vto = llFooter.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				Rect rect = new Rect();
				getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);// /取得整个视图部分,注意，如果你要设置标题样
				
				int top = rect.top;// //状态栏的高度，所以rect.height,rect.width分别是系统的高度的宽度
				View v = getWindow().findViewById(Window.ID_ANDROID_CONTENT);// /获得根视图
				buttomHeight = llFooter.getHeight();
				screenHeight = DistanceUtils.getScreenHeight(abApplication);
				float ss = buttomHeight/screenHeight;
				Log.d("tag", ss+"");
			}
		});
	}

	
}
