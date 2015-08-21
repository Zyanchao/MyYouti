package com.youti.yonghu.bean;

public class UserEntity {
	//用户id
		public String user_id;
		//用户头像url
		public String head_img;
		//用户昵称
		public String user_name;
		
		
		
		public String getHead_img() {
			return head_img;
		}
		public void setHead_img(String head_img) {
			this.head_img = head_img;
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
		
		
		public UserEntity(){
			
		}
	public UserEntity(String id, String portraitUrl, String name) {
		super();
		this.user_id = id;
		this.head_img = portraitUrl;
		this.user_name = name;
	}
	
	
	
}
