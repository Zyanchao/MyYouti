package com.youti.yonghu.bean;

import java.util.List;

public class OrderCommenetListBean {
	public String code;
	public String info;
	public List<OrderCommentList> list;
	
	public class OrderCommentList{
		public String agree_id;
		public String coach_id;
		public String coach_tel;
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
