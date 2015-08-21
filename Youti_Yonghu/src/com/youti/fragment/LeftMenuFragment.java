package com.youti.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.easemob.util.NetUtils;
import com.example.youti_geren.R;
import com.umeng.analytics.MobclickAgent;
import com.youti.utils.NetTips;
import com.youti.utils.Utils;
import com.youti.yonghu.activity.CoachListActivity;
import com.youti.yonghu.activity.CommunityListActivity;
import com.youti.yonghu.activity.CourseListActivity;
import com.youti.yonghu.activity.MainMainActivity;
import com.youti.yonghu.activity.VideoWaiteActivity;

public class LeftMenuFragment extends Fragment implements OnClickListener{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.mian_menu_left, null);
		courseBtn = (RelativeLayout)view.findViewById(R.id.ll_menu_kc);
		coachBtn = (RelativeLayout) view.findViewById(R.id.ll_menu_jl);
		vedioBtn = (RelativeLayout) view.findViewById(R.id.ll_menu_sp);
		communityBtn = (RelativeLayout) view.findViewById(R.id.ll_menu_sq);
		
		coachBtn.setOnClickListener(this);
		courseBtn.setOnClickListener(this);
		vedioBtn.setOnClickListener(this);
		communityBtn.setOnClickListener(this);
		return view;
	}

	public final String mPageName="LeftMenuFragment";
	  @Override
	  	public void onPause() {
				super.onPause();
				MobclickAgent.onPageEnd(mPageName);
			}

			@Override
			public void onResume() {
				super.onResume();
				MobclickAgent.onPageStart(mPageName);
			}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_menu_kc:
			if(selectIndex==0)return;
			//if(NetUtils.hasNetwork(arg0))
			Bundle b = new Bundle();
			b.putString("val", "全部");
			nextActivity(CourseListActivity.class,b);
			
			break;
		case R.id.ll_menu_jl:
			if(selectIndex==1)return;
			if (NetTips.isNetTips(getActivity())) {
				Bundle b1 = new Bundle();
				b1.putString("val", "全部");
				nextActivity(CoachListActivity.class,b1);
			}
			
			break;
		case R.id.ll_menu_sp:
			if(selectIndex==2)return;
			nextActivity(VideoWaiteActivity.class,null);
			break;
		case R.id.ll_menu_sq:
			if(selectIndex==3)return;
			nextActivity(CommunityListActivity.class,null);
			break;
		default:
			break;
		}
		
		//((MainMainActivity)getActivity()).getSlidingMenu().toggle();
	}
	
	private int selectIndex = -1;
	private RelativeLayout courseBtn;
	private RelativeLayout coachBtn;
	private RelativeLayout vedioBtn;
	private RelativeLayout communityBtn;
	public void setSelectButton(int index){
		coachBtn.setSelected(false);
		courseBtn.setSelected(false);
		vedioBtn.setSelected(false);
		communityBtn.setSelected(false);
		selectIndex = index;
		switch (index) {
		case 0:
			coachBtn.setSelected(true);
			break;
		case 1:
			courseBtn.setSelected(true);
			break;
		case 2:
			vedioBtn.setSelected(true);
			break;
		case 3:
			communityBtn.setSelected(true);
			break;
		default:
			break;
		}
	}
	
	private void nextActivity(Class<?> next, Bundle b) {
		Intent intent = new Intent();
		intent.setClass(getActivity(), next);
		if (b != null) {
			intent.putExtras(b);
		}
		getActivity().startActivity(intent);
	}
}
