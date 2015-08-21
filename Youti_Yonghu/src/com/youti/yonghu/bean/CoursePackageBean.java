package com.youti.yonghu.bean;

import java.util.List;

public class CoursePackageBean {
	public List<UserAddress>user_address;//(用户地址)
	public ClassWay class_way;
	public List<ClassPackage> youhui;
	
	

	public List<UserAddress> getUser_address() {
		return user_address;
	}

	public void setUser_address(List<UserAddress> user_address) {
		this.user_address = user_address;
	}



	public List<ClassPackage> getYouhui() {
		return youhui;
	}

	public void setYouhui(List<ClassPackage> youhui) {
		this.youhui = youhui;
	}

	
	public class UserAddress{
		private String address;

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}
	}
	
	
	public class ClassWay{
		String price_coach;//200",
		String price_stu;// "120",
		String price_discuss;// "300"
		public String getPrice_coach() {
			return price_coach;
		}
		public void setPrice_coach(String price_coach) {
			this.price_coach = price_coach;
		}
		public String getPrice_stu() {
			return price_stu;
		}
		public void setPrice_stu(String price_stu) {
			this.price_stu = price_stu;
		}
		public String getPrice_discuss() {
			return price_discuss;
		}
		public void setPrice_discuss(String price_discuss) {
			this.price_discuss = price_discuss;
		}
		
	}
	
	public class ClassPackage{
		
		public String youhui_id;//(主键id) 
		public String long_time;//(时长)
		public String youhui_discount;//(折扣)
		public String youhui_desc;//(标题)
		public String getYouhui_id() {
			return youhui_id;
		}
		public void setYouhui_id(String youhui_id) {
			this.youhui_id = youhui_id;
		}
		public String getLong_time() {
			return long_time;
		}
		public void setLong_time(String long_time) {
			this.long_time = long_time;
		}
		public String getYouhui_discount() {
			return youhui_discount;
		}
		public void setYouhui_discount(String youhui_discount) {
			this.youhui_discount = youhui_discount;
		}
		public String getYouhui_desc() {
			return youhui_desc;
		}
		public void setYouhui_desc(String youhui_desc) {
			this.youhui_desc = youhui_desc;
		}
		
	}
		
		
	
}
