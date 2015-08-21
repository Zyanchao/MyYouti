package com.youti.yonghu.bean;

import java.util.List;

public class PersonCenterCourseBean {
	public String code;
	public String info;
	public List<PersonCenterCourse> list;
	
	public class PersonCenterCourse{
		public String course_id;
		public String price;
		public String de_img;
		public String study_num;
		public String time;
		public String jd;
		public String wd;
	}
}
