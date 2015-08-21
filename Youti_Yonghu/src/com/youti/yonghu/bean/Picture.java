package com.youti.yonghu.bean;

import java.util.List;

public class Picture {
	public String code;
	public String info;
	public List<PicItem> list;
		
	public class PicItem{
		public String user_id;
		public String user_name;
		public String content;
		public String social_id;
		public String user_img;
		public String json_img;
		public String is_zan;
		public int width;
		public int height;
		public String praise_num;
		public String comment_num;
		
	}
	
}
