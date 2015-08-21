package com.youti.yonghu.bean;

public class CoachOrderDetailBean {
	public String code;
	public String info;
	public CoachOrderDetail list;
	public class CoachOrderDetail{
		public String order_id;//订单id
		public String coach_name;//教练姓名
		public String head_img;//头像
		public String pro_val;//项目类型
		public String price;//单价
		public String total_time;//总时长
		public String remnant;//剩余时长
		public String zkou;//折扣
		public String status;//状态
		public String class_way;//上课方式1教练上门 2用户上门 3协商地点
		public String add_time;//下单时间
		public String order_num;//订单编号
		public String coach_phone;//教练手机号
		public String refund_agree;//应付或者退款金额
		public String total_price;//总价
		public String fact_total_price;//实付
	}
}
