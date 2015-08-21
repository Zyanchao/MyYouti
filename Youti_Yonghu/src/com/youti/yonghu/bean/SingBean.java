package com.youti.yonghu.bean;

import java.io.Serializable;

public class SingBean{

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private String user_id;	
	private String user_name;	
	private String head_img;	
	private String sign_time;
	
	public String getSign_time() {
		return sign_time;
	}
	public void setSign_time(String sign_time) {
		this.sign_time = sign_time;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getHead_img() {
		return head_img;
	}
	public void setHead_img(String head_img) {
		this.head_img = head_img;
	}
	
	
}
