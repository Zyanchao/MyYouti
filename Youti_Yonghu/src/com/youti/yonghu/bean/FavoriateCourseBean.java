package com.youti.yonghu.bean;

import java.util.List;

public class FavoriateCourseBean {
	public String code;
	public String info;
	public List<CourseItemBean> list;
	
	public class CourseItemBean{
		public String course_id;
		public String start_time;
		public String price;
		public String img;
		public String praise_num;
		public String study_num;
	}
}
