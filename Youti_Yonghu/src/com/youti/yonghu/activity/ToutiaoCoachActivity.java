package com.youti.yonghu.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.ab.util.AbDialogUtil;
import com.ab.util.AbToastUtil;
import com.alibaba.fastjson.JSON;
import com.example.youti_geren.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.Constants;
import com.youti.appConfig.YoutiApplication;
import com.youti.base.BaseActivity;
import com.youti.utils.HttpUtils;
import com.youti.utils.IntentJumpUtils;
import com.youti.utils.NetTips;
import com.youti.view.CustomProgressDialog;
import com.youti.yonghu.adapter.CoachTouTiaoListAdapter;
import com.youti.yonghu.bean.CoachBean;
import com.youti.yonghu.bean.CoachTopBean;

/**
 *  购买 课程 
 * @author zyc
 * 2015-6-1
 */
public class ToutiaoCoachActivity extends BaseActivity implements OnClickListener{

	
	private ImageView ivBack;
	private ImageView ivBg;
	private ImageView ivhead;
	private TextView tvName,tvSign,tvVal,tvAddress,tvZan,tvTouGuize;
	private ListView mListView;
	private View viewFooter;
	private List<CoachBean> mCoachTopList = new ArrayList<CoachBean>();
	private CoachTopBean topBean;
	private CoachTouTiaoListAdapter mAdapter;
	CustomProgressDialog dialog;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coach_toutiao);
		initView();
		initListener();
		getTopCoachList();
	}

	
	public final String mPageName ="ToutiaoCoachActivity";
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

	private void initView() {
	
		dialog = new CustomProgressDialog(this, R.string.laoding_tips,R.anim.frame2);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		mListView = (ListView) findViewById(R.id.lv_toutiao);
		
		ivhead = (ImageView) findViewById(R.id.headportrait);
		tvName = (TextView) findViewById(R.id.tv_name);
		tvVal = (TextView) findViewById(R.id.tv_val);
		tvSign = (TextView) findViewById(R.id.tv_sign);
		tvAddress = (TextView) findViewById(R.id.tv_address_city);
		tvZan = (TextView) findViewById(R.id.tv_zan_count);
		
		viewFooter = mInflater.inflate(R.layout.coach_toutiao_footer, null);
		tvTouGuize = (TextView) viewFooter.findViewById(R.id.tv_tou_guize);
		
		mListView.addFooterView(viewFooter);
		//ivSerach.setVisibility(View.GONE);
	}
	

	private void initListener() {
		ivBack.setOnClickListener(this);
		mListView.setOnItemClickListener(new ItemtClickListener());
	}

	private class ItemtClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			   CoachBean bean  = new CoachBean();
				TextView tv = (TextView) view.findViewById(R.id.tv_name);
				bean = (CoachBean) tv.getTag();
				Bundle bundle = new Bundle();
				bundle.putString(Constants.KEY_ID, bean.getCoach_id());
				bundle.putString(Constants.KEY_CHAT_TEL, bean.getCoach_tel());
				bundle.putString(Constants.KEY_CHAT_USERNAME, bean.getCoach_name());
				bundle.putString(Constants.KEY_CHAT_AVATAR, bean.getHead_img());
				IntentJumpUtils.nextActivity(CoachDetailActivity.class, ToutiaoCoachActivity.this, bundle);

		}
	}
	
	
	
	/**
	* @Title: getTopCoachList 
	* @Description: TODO(获取 头条 教练 列表) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void getTopCoachList() {
		RequestParams params = new RequestParams(); //
		params.put("user_id",YoutiApplication.getInstance().myPreference.getUserId());
		params.put("number",50);
		HttpUtils.post(Constants.COACH_TOP_LIST, params,
				new JsonHttpResponseHandler() {
					public void onStart() {
						super.onStart();
						dialog.show();

						if (mCoachTopList.size() != 0) {
							mCoachTopList.clear();
						}
					}

					public void onFailure(int statusCode,
							org.apache.http.Header[] headers,
							java.lang.Throwable throwable,
							org.json.JSONObject errorResponse) {
						
					};

					public void onFinish() {
						dialog.dismiss();
					};

					public void onSuccess(int statusCode,
							org.apache.http.Header[] headers,
							org.json.JSONObject response) {
						try {
							dialog.dismiss();
							response.toString();
							String state = response.getString("code");
							if (state.equals("1")) {
								com.alibaba.fastjson.JSONObject object = JSON
										.parseObject(response.toString());
								com.alibaba.fastjson.JSONObject obj = object
										.getJSONObject("list");

								topBean = JSON.parseObject(obj.toString(), CoachTopBean.class);
								mCoachTopList = topBean.getList();
								tvTouGuize.setText(topBean.getTop_rule().toString());
								imageLoader.displayImage(Constants.PIC_CODE+mCoachTopList.get(0).getHead_img(), ivhead, options);
								tvName.setText(mCoachTopList.get(0).getCoach_name());
								tvVal.setText(mCoachTopList.get(0).getProject_type());
								tvAddress.setText(mCoachTopList.get(0).getServer_province());
								tvSign.setText(mCoachTopList.get(0).getSign());
								tvZan.setText(mCoachTopList.get(0).getPraise_num());
								mCoachTopList.remove(0);
								mAdapter = new CoachTouTiaoListAdapter(mContext, mCoachTopList);
								mListView.setAdapter(mAdapter);
								mAdapter.notifyDataSetChanged();
								
								
							} else {

								AbToastUtil.showToast(mContext, R.string.data_tips);
							}
						} catch (Exception e) {

						}
					};
				});
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;

		default:
			break;
		}
		
	}

}
