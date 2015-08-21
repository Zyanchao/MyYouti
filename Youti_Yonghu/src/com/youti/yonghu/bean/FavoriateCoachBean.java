package com.youti.yonghu.bean;

import java.util.List;

public class FavoriateCoachBean {
	public String code;
	public String info;
	public List<CoachItemBean> list;
	
	public class CoachItemBean{
		public String coach_id;
		public String coach_name;
		public String head_img;
		public String project_type;
		public String praise_num;
		public String price;
		public String online;
		public String study_num;
	}
}
