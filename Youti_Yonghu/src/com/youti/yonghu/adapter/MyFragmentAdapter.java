package com.youti.yonghu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentAdapter extends FragmentPagerAdapter{

	private List<Fragment> mList = new ArrayList<Fragment>();
	
	public MyFragmentAdapter(FragmentManager fm,List<Fragment> list) {
		super(fm);
		this.mList = list;
	}

	@Override
	public Fragment getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public int getCount() {
		return mList.size();
	}


}
