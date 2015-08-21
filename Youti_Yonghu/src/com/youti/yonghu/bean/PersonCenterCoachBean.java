package com.youti.yonghu.bean;

import java.util.List;

public class PersonCenterCoachBean {
	public String code;
	public String info;
	public List<PersonCenterCoach> list;
	
	public class PersonCenterCoach{
		public String coach_id;
		public String coach_img;
		public String pro_val;
		public String coach_name;
		public String add_time;
	}
	
}
