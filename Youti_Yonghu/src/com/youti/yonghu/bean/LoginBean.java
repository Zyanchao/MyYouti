package com.youti.yonghu.bean;


public class LoginBean {
	public String info;
	public String code;
	public UserBean list;
	
	public class UserBean {
		public String user_id;
		public String money;
		public String user_name;
		public String sex;
		public String sign;
		public String tel_phone;
		public String status;//是否设置过支付密码
	}
}
