package com.youti.yonghu.bean;

import java.util.List;

public class MyCoachYueKeBean {
	public String code;
	public String info;
	public List<YueKeBean> list;
	
	public class YueKeBean{
		public String agree_id;
		public String order_id;
		public String user_id;
		public String coach_name;
		public String coach_img;
		public String pro_val;
		public String long_time;
		public String start_time;
		public String end_time;
		public String status;
	}
}
