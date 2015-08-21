package com.youti.yonghu.bean;

import java.util.ArrayList;
import java.util.List;

public class PhotoDetail {
	public String code;
	public String info;
	public InfoItem list;
	
	public class InfoItem{
		public String is_zan;
		public String user_id;
		public String user_name;
		public ArrayList<String> json_img;
		public String user_img;
		public String praise_num;
		public String comment_num;
		public List<ZanItem> praise;
		public List<CommentItem> comment;
	}
	
	public class CommentItem{
		public String user_name;
		public String user_img;
		public String comment_content;
		public String comment_time;
	}
	public class ZanItem{
		public String coach_tel;
		public String user_id;
		public String user_type;
		public String getUser_type() {
			return user_type;
		}
		public void setUser_type(String user_type) {
			this.user_type = user_type;
		}
		public String getUser_id() {
			return user_id;
		}
		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
		public String getUser_img() {
			return user_img;
		}
		public void setUser_img(String user_img) {
			this.user_img = user_img;
		}
		public String user_img;
		
	}
}
