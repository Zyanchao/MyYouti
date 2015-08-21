package com.youti.yonghu.bean;

import java.io.Serializable;

public class OrderCoach implements Serializable{
	
	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 2380956314064528279L;
	private String  order_id;//订单编号
	private String  project_type;//项目类型
	private String  total_price;//总价
	private String  price;//单价
	private String  coach_name;//教练昵称
	private String  long_time;//时长
	private float   zkou;//1无折扣
	
	
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getCoach_name() {
		return coach_name;
	}
	public void setCoach_name(String coach_name) {
		this.coach_name = coach_name;
	}
	public String getProject_type() {
		return project_type;
	}
	public void setProject_type(String project_type) {
		this.project_type = project_type;
	}
	public String getTotal_price() {
		return total_price;
	}
	public void setTotal_price(String total_price) {
		this.total_price = total_price;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getLong_time() {
		return long_time;
	}
	public void setLong_time(String long_time) {
		this.long_time = long_time;
	}
	public float getZkou() {
		return zkou;
	}
	public void setZkou(float zkou) {
		this.zkou = zkou;
	}
	


}
