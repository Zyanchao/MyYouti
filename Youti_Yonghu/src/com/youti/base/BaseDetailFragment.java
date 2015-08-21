package com.youti.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youti.appConfig.YoutiApplication;

public abstract class BaseDetailFragment extends Fragment implements
		android.view.View.OnClickListener {

	protected LayoutInflater mInflater;

	public YoutiApplication getApplication() {
		return (YoutiApplication) getActivity().getApplication();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.mInflater = inflater;
		View view = super.onCreateView(inflater, container, savedInstanceState);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	protected int getLayoutId() {
		return 0;
	}

	protected View inflateView(int resId) {
		return this.mInflater.inflate(resId, null);
	}

	public boolean onBackPressed() {
		return false;
	}

	/*
	 * protected void hideWaitDialog() { FragmentActivity activity =
	 * getActivity(); if (activity instanceof DialogControl) { ((DialogControl)
	 * activity).hideWaitDialog(); } }
	 * 
	 * protected WaitDialog showWaitDialog() { return
	 * showWaitDialog(R.string.loading); }
	 * 
	 * protected WaitDialog showWaitDialog(int resid) { FragmentActivity
	 * activity = getActivity(); if (activity instanceof DialogControl) { return
	 * ((DialogControl) activity).showWaitDialog(resid); } return null; }
	 * 
	 * protected WaitDialog showWaitDialog(String resid) { FragmentActivity
	 * activity = getActivity(); if (activity instanceof DialogControl) { return
	 * ((DialogControl) activity).showWaitDialog(resid); } return null; }
	 */

	public void initView(View view) {

	}

	public void initData() {

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
