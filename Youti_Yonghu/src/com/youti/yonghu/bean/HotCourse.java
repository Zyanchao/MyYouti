package com.youti.yonghu.bean;

import java.util.List;

/**
 * 热门课程
 */
public class HotCourse {

	private  String course_id;//课程id
	private  String img;//图片路径
	private  String start_time;//时间
	private  String price;//价格
	private  String praise_num;//点赞数
	private  String comment_num;//评论数
	private  List<UserEntity> user_heads;//点赞的用户头像
	
	public String getCourse_id() {
		return course_id;
	}
	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
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
