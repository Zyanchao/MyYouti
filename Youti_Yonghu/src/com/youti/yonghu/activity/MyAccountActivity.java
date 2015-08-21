package com.youti.yonghu.activity;

import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.YoutiApplication;
import com.youti.base.BaseActivity;
import com.youti.utils.HttpUtils;
import com.youti.utils.IntentJumpUtils;
import com.youti.utils.Utils;
import com.youti.view.CustomProgressDialog;
import com.youti.view.TitleBar;
import com.youti.view.WaitDialog;
import com.youti.yonghu.bean.CountBean;
import com.youti.yonghu.bean.CountBean.YouHuiQuan;

/**
 * 
* @ClassName: MyAccountActivity 
* @Description: TODO(我的账户) 
* @author zychao 
* @date 2015-6-28 下午3:47:11 
*
 */
public class MyAccountActivity extends BaseActivity implements OnClickListener{
	
	ListView lv_youhuiquan;
	TextView tv_account,tv_edit_password,tv_chongzhi,tv_card_chongzhi;
	String userId;
	private CountBean cb;
	TitleBar titleBar;
	RelativeLayout rl_chongzhi;
	String value;
	List<YouHuiQuan> list;
	
	int  payCode;
	//int 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_myaccount);
		
		if(getIntent()!=null&&getIntent().getExtras()!=null){
			//value=getIntent().getExtras().getString("value");
			payCode=getIntent().getExtras().getInt("payCode");
		}
		initView();
		userId=((YoutiApplication)getApplication()).myPreference.getUserId();
		//requestData();
		
	}
	public final String mPageName = "MyAccountActivity";
	private Intent intent;
	private CustomProgressDialog waitDialog;
	@Override
	public void onResume() {
		super.onResume();
		requestData();
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
		lv_youhuiquan =(ListView) findViewById(R.id.lv_youhuiquan);
		titleBar=(TitleBar) findViewById(R.id.index_titlebar);
		titleBar.setSearchGone();
		titleBar.setTitleBarTitle("我的账户");
		titleBar.setShareGone(false);
		tv_account =(TextView) findViewById(R.id.tv_account);
		
		rl_chongzhi=(RelativeLayout) findViewById(R.id.rl_chongzhi);
		userId=((YoutiApplication)getApplication()).myPreference.getUserId();
		//requestData();
		
		tv_edit_password = (TextView) findViewById(R.id.tv_edit_password);
		tv_chongzhi = (TextView) findViewById(R.id.tv_chongzhi);
		tv_card_chongzhi = (TextView) findViewById(R.id.tv_card_chongzhi);
		//tv_edit_password.setOnClickListener(this);
		tv_chongzhi.setOnClickListener(this);
		tv_card_chongzhi.setOnClickListener(this);
		lv_youhuiquan.setOnItemClickListener(new MyOnItemClick());
		
		
		
	}
	
	private void requestData() {
		waitDialog = new CustomProgressDialog(MyAccountActivity.this, R.string.laoding_tips,R.anim.frame2);
		waitDialog.show();
		String urlString = "http://112.126.72.250/ut_app/index.php?m=User&a=my_account";
		RequestParams params =new RequestParams();
		params.put("user_id", userId);
		HttpUtils.post(urlString, params, new TextHttpResponseHandler() {
			
			

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				waitDialog.dismiss();
				Gson gson =new Gson();
				cb = gson.fromJson(arg2, CountBean.class);
				if(cb.code.equals("1")){
					//请求成功
					if(TextUtils.isEmpty(value)){
						tv_account.setText(cb.list.money);
						
					}else{
						tv_account.setText(value);
					}
					
					if("0".equals(cb.list.status)){
						tv_edit_password.setText("支付密码设置");
						tv_edit_password.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								intent = new Intent(MyAccountActivity.this,ModifyPasswordActivity.class);
								intent.putExtra("code", "1");
								startActivity(intent);
							}
						});
					}else{
						tv_edit_password.setText("支付密码修改");
						tv_edit_password.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								intent = new Intent(MyAccountActivity.this,ModifyPasswordActivity.class);
								intent.putExtra("code", "0");
								startActivity(intent);
							}
						});
					}
					list = cb.list.coupons;
					if(list==null){
						Utils.showToast(mContext, "您已没有可用优惠券！");
						return;
					}else if(list.size()==0){	
						Utils.showToast(mContext, "您已没有可用优惠券！");
						return;
						
					}else{
						lv_youhuiquan.setAdapter(new MyAdapter(list));
					}
				}else if(cb.code.equals("400")){
					Toast.makeText(MyAccountActivity.this, "用户id为空", 0).show();
				}else{
					Toast.makeText(MyAccountActivity.this, "访问服务器失败，请稍后再试", 0).show();
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				waitDialog.dismiss();
				Utils.showToast(mContext, "网络连接异常");
			}
		});
	}

	
	
	
	class MyOnItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent();
			intent.putExtra("youhuiAmount", Float.parseFloat(list.get(position).getPrice()));
			intent.putExtra("id", list.get(position).getId());
			MyAccountActivity.this.setResult(payCode, intent);
			finish();//此处一定要调用finish()方法
		}
	}
	
	
	class MyAdapter extends BaseAdapter{
		
		public List<YouHuiQuan> mList;
		public MyAdapter(List<YouHuiQuan> mList){
			this.mList=list;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v =View.inflate(MyAccountActivity.this, R.layout.item_youhuiquan, null);
			TextView hongbao =(TextView) v.findViewById(R.id.hongbao);
			TextView tv_date =(TextView) v.findViewById(R.id.tv_date);
			TextView my_youhuiquan=(TextView) v.findViewById(R.id.my_youhuiquan);
			ImageView iv =(ImageView) v.findViewById(R.id.iv);
			
			hongbao.setText(mList.get(position).cou_title);
			tv_date.setText(mList.get(position).expiration_time);
			my_youhuiquan.setText(mList.get(position).price);
			/*if(mList.get(position).status.equals("0")){	
				iv.setBackgroundResource(R.drawable.myzh_hb);
			}else{
				iv.setVisibility(View.GONE);
			}*/
			
			return v;
		}
		
	}

	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.tv_edit_password:
			
			break;

		case R.id.tv_chongzhi:
			Bundle bundle = new Bundle();
			
			String mo = cb.list.money;
			bundle.putString("amcount", mo);
			IntentJumpUtils.nextActivity(UpAccountActivity.class,this,bundle);
			break;
		case R.id.tv_card_chongzhi:
			intent =new Intent(MyAccountActivity.this,RechargeActivity.class);
			startActivity(intent);
		//	finish();
			break;
		default:
			break;
		}
		
	}
}
