package com.youti.yonghu.bean;

import java.util.List;

public class OnLineBean {
	public String code;
	public String info;
	public List<CoachItem> list;
	
	public class CoachItem{
		public String coach_tel;//电话
		public String coach_id;
		public String coach_name;//姓名
		public String head_img;//头像地址
		public String line;// offline 
		public String project_type;//项目
		public String server_city;//城市
		public int pro_cert;//是否认证
		public String number;//咨询数量
	}
}
