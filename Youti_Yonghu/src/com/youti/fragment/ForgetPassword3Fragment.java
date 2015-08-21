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

public class ForgetPassword3Fragment extends Fragment {
	EditText newPaw,confirmPaw;
	Button finish;
	private String phoneNum;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v =View.inflate(getActivity(), R.layout.layout_forgetpassword3, null);
		newPaw =(EditText) v.findViewById(R.id.newpaw);
		confirmPaw=(EditText) v.findViewById(R.id.confirmpaw);
		finish=(Button) v.findViewById(R.id.finish);
		return  v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		phoneNum = ((YoutiApplication)(getActivity().getApplication())).myPreference.getTelNumber();
		finish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String pwd1=newPaw.getText().toString().replace(" ", "");
				String pwd2=confirmPaw.getText().toString().replace(" ", "");
				if(TextUtils.isEmpty(pwd1)||TextUtils.isEmpty(pwd2)){
					Utils.showToast(getActivity(), "密码不能为空");
					return;
				}
				if(pwd1.equals(pwd2)){
					Utils.showToast(getActivity(), "两次密码不一致");
					return;
				}
				
				requestData(pwd2);
			}
		});
	}

	protected void requestData(String pwd) {
		RequestParams params =new RequestParams();
		params.put("tel_phone",phoneNum);
		params.put("new_pwd",pwd);
		HttpUtils.post("http://112.126.72.250/ut_app/index.php?m=User&a=up_pwd", params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Gson gson =new Gson();
				InfoBean fromJson = gson.fromJson(arg2, InfoBean.class);
				if(fromJson.code.equals("400")){
					Utils.showToast(getActivity(), "手机号为空");
				}else if(fromJson.code.equals("401")){
					Utils.showToast(getActivity(), "新密码为空");
				}else if(fromJson.code.equals("402")){
					Utils.showToast(getActivity(), "账号不存在");
				}else if(fromJson.code.equals("1")){
					Utils.showToast(getActivity(), "修改成功");
					getActivity().finish();
				}else{
					Utils.showToast(getActivity(), "修改失败");
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Utils.showToast(getActivity(), "网络连接异常");
			}
		});
	}
}
