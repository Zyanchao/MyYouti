package com.youti.yonghu.bean;

import java.util.List;

public class CountBean {
	public String code;//0失败， 1成功， 400用户id为空
	public String info;
	public MyCount list;
	public class MyCount{
		public String money; //余额
		public List<YouHuiQuan> coupons; //优惠券
		public String status;//是否设置过密码
	}
	
	public class YouHuiQuan{
		public String id;
		public String expiration_time;//过期时间
		//public String status;//0未使用，1已使用，2已过期
		public String cou_title;//名称
		public String getCou_title() {
			return cou_title;
		}
		public void setCou_title(String cou_title) {
			this.cou_title = cou_title;
		}
		public String price;//金额
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getExpiration_time() {
			return expiration_time;
		}
		public void setExpiration_time(String expiration_time) {
			this.expiration_time = expiration_time;
		}
		/*public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}*/
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		
	}
}
