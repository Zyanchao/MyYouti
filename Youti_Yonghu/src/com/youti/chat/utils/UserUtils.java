package com.youti.chat.utils;

import android.content.Context;
import android.widget.ImageView;

import com.example.youti_geren.R;
import com.squareup.picasso.Picasso;
import com.youti.appConfig.YoutiApplication;
import com.youti.chat.domain.User;

public class UserUtils {
	
	public static void setAcatr(String username,String acatr){
		 User user = YoutiApplication.getInstance().getContactList().get(username);
	        if(user == null){
	            user = new User(username);
	        }
	            
	        if(user != null){
	            //demo没有这些数据，临时填充
	            user.setNick(username);
	            user.setAvatar(acatr);
	        }
	      getUserInfo(username);
	}
	
    /**
     * 根据username获取相应user，由于demo没有真实的用户数据，这里给的模拟的数据；
     * @param username
     * @return
     */
    public static User getUserInfo(String username){
        User user = YoutiApplication.getInstance().getContactList().get(username);
        if(user == null){
            user = new User(username);
        }
        return user;
    }
    
    
    /**
     * 设置用户头像
     * @param username
     */
    public static void setUserAvatar(Context context, String username,ImageView imageView){
        User user = getUserInfo(username);
        if(user != null){
        	if(user.getUsername().equals("xitongxiaoxi")||user.getUsername().equals("xitongtongzhi")){
        		Picasso.with(context).load(R.drawable.wdxx_mishu).into(imageView);
        	}else {
        		Picasso.with(context).load(user.getAvatar()).placeholder(R.drawable.default_avatar).into(imageView);
        	}
            
        }else{
            Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
        }
    }
    
}
