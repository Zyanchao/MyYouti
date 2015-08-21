package com.youti.yonghu.bean;

import java.util.List;

public class UserCenterBean {
	public String code;
	public String info;
	public UserInfo list;
	public class UserInfo{
		public UserDetail user_info;
		public String ton;
		public List<UserPhoto> user_photo;
	}
	
	public class UserDetail{
		public String head_img;
		public String sign;
		public String user_name;
		public String sex;//0表示女，1表示男
	}
	
	public class UserPhoto{
		public String photo_id;
		public String photo_url;
		
		public String getPhoto_id() {
			return photo_id;
		}
		public void setPhoto_id(String photo_id) {
			this.photo_id = photo_id;
		}
		public String getPhoto_url() {
			return photo_url;
		}
		public void setPhoto_url(String photo_url) {
			this.photo_url = photo_url;
		}
	}
}
