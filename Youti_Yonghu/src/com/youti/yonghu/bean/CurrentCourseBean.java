package com.youti.yonghu.bean;

import java.util.List;

public class CurrentCourseBean {
	public String code;
	public String info;
	public List<CurrentCourseDetailBean> list;
	
	public class CurrentCourseDetailBean{
		public String course_id;
		public String de_img;
		public String status;
		public String title;
		public String price;
		public String order_num;
		public String begin_time;
		public String order_id;
		public String sta;
		public String comment;
	}
}
