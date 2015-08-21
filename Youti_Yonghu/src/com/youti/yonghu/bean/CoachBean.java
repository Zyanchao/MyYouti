package com.youti.yonghu.bean;

import java.io.Serializable;
import java.util.List;

public class CoachBean  implements Serializable {
	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 1L;
	public String coach_id ="";
	private String head_img;
	private String coach_tel;
	public String coach_name;
	public String project_type;
	public String teach_age;
	public String sign;//签名
	public String val;//项目
	public String server_province;
	public String server_city;
	public String price = "";
 	public String distance = "";
 	public  String top;//名次
 	
 	public int dataId;
 	
 	
 	public String getCoach_tel() {
		return coach_tel;
	}
	public void setCoach_tel(String coach_tel) {
		this.coach_tel = coach_tel;
	}
 	public int getDataId() {
		return dataId;
	}
	public void setDataId(int dataId) {
		this.dataId = dataId;
	}

 	
 	public String getPraise() {
		return praise;
	}
	public void setPraise(String praise) {
		this.praise = praise;
	}
	/**
 	 * 点赞数
 	 */

 	public String praise;//是否赞过1(赞过)0(没赞过)
 	
	/** * 点赞数 */

 	public String praise_num = "";
 	/*** 评论数*/
 	public String comment_num = "";

 	/**
 	 * 最近参与点赞的用户头像
 	 */

 	/*** 最近参与评论的用户头像*/

 	public List<UserEntity> user_heads;
 	
 	
 	
 	public String getHead_img() {
		return head_img;
	}
	public void setHead_img(String head_img) {
		this.head_img = head_img;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getServer_province() {
		return server_province;
	}
	public void setServer_province(String server_province) {
		this.server_province = server_province;
	}
	public String getServer_city() {
		return server_city;
	}
	public void setServer_city(String server_city) {
		this.server_city = server_city;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getTop() {
		return top;
	}
	public void setTop(String top) {
		this.top = top;
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
	public String getTeach_age() {
		return teach_age;
	}
	public void setTeach_age(String teach_age) {
		this.teach_age = teach_age;
	}
	
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getCoach_id() {
		return coach_id;
	}
	public void setCoach_id(String coach_id) {
		this.coach_id = coach_id;
	}
	public String getPraise_num() {
		return praise_num;
	}
	public void setPraise_num(String praise_num) {
		this.praise_num = praise_num;
	}
	public String getComment_num() {
		return comment_num;
	}
	public void setComment_num(String comment_num) {
		this.comment_num = comment_num;
	}
	public List<UserEntity> getUser_heads() {
		return user_heads;
	}
	public void setUser_heads(List<UserEntity> user_heads) {
		this.user_heads = user_heads;
	}
	
}
