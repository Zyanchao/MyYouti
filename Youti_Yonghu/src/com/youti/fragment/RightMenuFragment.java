package com.youti.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMConversation.EMConversationType;
import com.example.youti_geren.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.youti.appConfig.YoutiApplication;
import com.youti.applib.controller.HXSDKHelper;
import com.youti.utils.Utils;
import com.youti.view.CircleImageView;
import com.youti.yonghu.activity.EditDataActivity;
import com.youti.yonghu.activity.LoginActivity;
import com.youti.yonghu.activity.MoreActivity;
import com.youti.yonghu.activity.MyAccountActivity;
import com.youti.yonghu.activity.MyCoachActivity;
import com.youti.yonghu.activity.MyCourseActivity;
import com.youti.yonghu.activity.MyFavoriateActivity;
import com.youti.yonghu.activity.MyMessageActivity;
import com.youti.yonghu.activity.PersonCenterTestActivity;
import com.youti.yonghu.download.DownloadManagerActivity;

public class RightMenuFragment extends Fragment implements OnClickListener,EMEventListener{
	boolean isLogin;
	Intent intent ;
	Context context;
	RelativeLayout rl_user_icon,rl_edit,rl_account,rl_course,rl_coarch,rl_message,rl_download,rl_favoriate,rl_more;
	CircleImageView user_icon_image;
	private TextView tv_message_tip;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		context=getActivity();
		View v =View.inflate(getActivity(), R.layout.main_menu_right, null);
		rl_user_icon=(RelativeLayout) v.findViewById(R.id.rl_user_icon);
		rl_edit=(RelativeLayout) v.findViewById(R.id.rl_edit);
		rl_account=(RelativeLayout) v.findViewById(R.id.rl_account);
		rl_course=(RelativeLayout) v.findViewById(R.id.rl_course);
		rl_coarch=(RelativeLayout) v.findViewById(R.id.rl_coarch);
		rl_message=(RelativeLayout) v.findViewById(R.id.rl_message);
		rl_download=(RelativeLayout) v.findViewById(R.id.rl_download);
		rl_favoriate=(RelativeLayout) v.findViewById(R.id.rl_favorite);
		rl_more=(RelativeLayout) v.findViewById(R.id.rl_more);
		user_icon_image=(CircleImageView) v.findViewById(R.id.user_icon_image);
		user_icon_image.setBackgroundResource(R.drawable.user_head);
		tv_message_tip = (TextView) v.findViewById(R.id.tv_message_tip);
		
		rl_user_icon.setOnClickListener(this);
		rl_edit.setOnClickListener(this);
		rl_account.setOnClickListener(this);
		rl_course.setOnClickListener(this);
		rl_coarch.setOnClickListener(this);
		rl_message.setOnClickListener(this);
		rl_download.setOnClickListener(this);
		rl_favoriate.setOnClickListener(this);
		rl_more.setOnClickListener(this);
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		
	}
	public final String mPageName="RightMenuFragment";
	private String uri;
	  @Override
	  	public void onPause() {
				super.onPause();
				MobclickAgent.onPageEnd(mPageName);
			}

			@Override
			public void onResume() {
				super.onResume();
				String url = YoutiApplication.getInstance().myPreference.getHeadImgPath();
				//user_icon_image.setBackgroundResource(R.drawable.sp_head);
				if(url.startsWith("http")){
						ImageLoader.getInstance().displayImage(url, user_icon_image);
					
				}else{
					if(url.equals("")){
						ImageLoader.getInstance().displayImage("", user_icon_image);
					}else{
						ImageLoader.getInstance().displayImage("http://112.126.72.250/ut_app"+url, user_icon_image);
					}
				}
				MobclickAgent.onPageStart(mPageName);
			}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.rl_user_icon:
			 isLogin=YoutiApplication.getInstance().myPreference.getHasLogin();
			if(isLogin){
				intent =new Intent(context,PersonCenterTestActivity.class);				
			}else{
				intent = new Intent(context,LoginActivity.class);
			}
			context.startActivity(intent);
			break;
		case R.id.rl_edit:
			isLogin=((YoutiApplication)(context.getApplicationContext())).myPreference.getHasLogin();
			if(isLogin){
				intent=new Intent(context,EditDataActivity.class);
			}else{
				intent = new Intent(context,LoginActivity.class);
			}
			context.startActivity(intent);
			break;
		case R.id.rl_account: 
			isLogin=((YoutiApplication)(context.getApplicationContext())).myPreference.getHasLogin();
			if(isLogin){
				intent=new Intent(context,MyAccountActivity.class);
			}else{
				intent = new Intent(context,LoginActivity.class);
			}
			context.startActivity(intent);
			break;
		case R.id.rl_course:
			isLogin=((YoutiApplication)(context.getApplicationContext())).myPreference.getHasLogin();
			if(isLogin){
				intent=new Intent(context,MyCourseActivity.class);
			}else{
				intent = new Intent(context,LoginActivity.class);
			}
			context.startActivity(intent);
			break;
		case R.id.rl_coarch:
			isLogin=((YoutiApplication)(context.getApplicationContext())).myPreference.getHasLogin();
			if(isLogin){
				intent=new Intent(context,MyCoachActivity.class);
			}else{
				intent = new Intent(context,LoginActivity.class);
			}
			context.startActivity(intent);
			break;
		case R.id.rl_message:
			isLogin=((YoutiApplication)(context.getApplicationContext())).myPreference.getHasLogin();
			if(isLogin){
				intent=new Intent(context,MyMessageActivity.class);
			}else{
				intent = new Intent(context,LoginActivity.class);
			}
			context.startActivity(intent);
			break;
		case R.id.rl_download:
			intent =new Intent(context,DownloadManagerActivity.class);
			context.startActivity(intent);
			break;
		case R.id.rl_favorite:
			isLogin=((YoutiApplication)(context.getApplicationContext())).myPreference.getHasLogin();
			if(isLogin){
				intent=new Intent(context,MyFavoriateActivity.class);
			}else{
				intent = new Intent(context,LoginActivity.class);
			}
			context.startActivity(intent);
			break;
		case R.id.rl_more:
			intent =new Intent(context,MoreActivity.class);
			context.startActivity(intent);
			break;
		}
	}
	
	@Override
	public void onEvent(EMNotifierEvent event) {
		switch (event.getEvent()) {
		case EventNewMessage: // 普通消息
			EMMessage message = (EMMessage) event.getData();
			// 提示新消息
			HXSDKHelper.getInstance().getNotifier().onNewMsg(message);

			tv_message_tip.setVisibility(View.VISIBLE);
			//refreshUI();
			break;
		}
	}
		
		/**
		 * 获取未读消息数
		 * 
		 * @return
		 *//*
		public int getUnreadMsgCountTotal() {
			int unreadMsgCountTotal = 0;
			int chatroomUnreadMsgCount = 0;
			unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
			for(EMConversation conversation:EMChatManager.getInstance().getAllConversations().values()){
				if(conversation.getType() == EMConversationType.ChatRoom)
				chatroomUnreadMsgCount=chatroomUnreadMsgCount+conversation.getUnreadMsgCount();
			}
			return unreadMsgCountTotal-chatroomUnreadMsgCount;
		}*/
}
