package com.youti.yonghu.bean;

import java.util.List;

public class MyCoachOrderBean {
	public String code;
	public String info;
	public List<MyCoachOrderDetailBean> list;
	public class  MyCoachOrderDetailBean{
		public String order_id;//订单id
		public String pro_val;//项目类型
		public String price;//单价
		public String total_time;//总时长
		public String zkou;//折扣 1无折扣
		public String ag_total_price;//总价
		public String coach_name;//教练姓名
		public String coach_id;//教练id
		public String remnant;//剩余时长
		public String head_img;//教练头像
		public String status;//订单状态  0代表未支付  1代表已支付  2已完结  3代表退款中  4已退款 5已取消

	}
}
