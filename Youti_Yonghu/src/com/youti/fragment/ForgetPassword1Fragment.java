package com.youti.fragment;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.youti_geren.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.youti.appConfig.YoutiApplication;
import com.youti.utils.HttpUtils;
import com.youti.utils.Utils;
import com.youti.yonghu.bean.InfoBean;

public class ForgetPassword1Fragment extends Fragment {
	Button btn_next;
	EditText phonenumber;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v=View.inflate(getActivity(), R.layout.layout_forgetpassword1, null);
		btn_next=(Button) v.findViewById(R.id.btn_next);
		phonenumber=(EditText) v.findViewById(R.id.phonenumber);
		return  v;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		btn_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String phone = phonenumber.getText().toString().replace(" ", "");
				if(TextUtils.isEmpty(phone)||phone.length()!=11){
					Utils.showToast(getActivity(), "请核实手机号码，重新发送");
					return;
				}
				requestData(phone);
				
				
			}
		});
		
	}
	
	protected void requestData(final String phone) {
		RequestParams params =new RequestParams();
		params.put("tel_phone", phone);
		HttpUtils.post("http://112.126.72.250/ut_app/index.php?m=User&a=find_pwd", params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson =new Gson();
				InfoBean fromJson = gson.fromJson(arg2, InfoBean.class);
				if(fromJson.code.equals("403")){
					Utils.showToast(getActivity(), "密码错误");
				}else if(fromJson.code.equals("402")){
					Utils.showToast(getActivity(), "账号不存在");
				}else if(fromJson.code.equals("0")){
					Utils.showToast(getActivity(), "验证码发送失败");
				}else if(fromJson.code.equals("401")){
					Utils.showToast(getActivity(), "手机格式错误");
				}else {
					Utils.showToast(getActivity(), "验证码发送成功");
					((YoutiApplication)(getActivity().getApplication())).myPreference.setTelNumber(phone);
					//((ForgetPasswordActivity) getActivity()).switchPage(1);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Utils.showToast(getActivity(), "网络连接异常");
			}
		});
	}
}
