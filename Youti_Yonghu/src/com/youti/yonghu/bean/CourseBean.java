package com.youti.yonghu.bean;

import java.io.Serializable;
import java.util.List;

public class CourseBean implements Serializable{
	/**
	 * 课程 类
	 */
	private static final long serialVersionUID = 1L;
	public String course_id ="";//教练 id
	public String img ="";//课程图片
	public String describe ="";//图片描述
	public String start_time ="";//
	public String price = "";//课程价格
	public String studentCount = "";//学员数
	public String distance = "";// 距离
	public String study_num;// 多少人学过
	public int dataId; //给每条数据添加id，这样listview就可以局部刷新了
	public String praise;//登录用户是否点赞
	
	private String title;//	标题 
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPrese() {
		return prese;
	}
	public void setPrese(String prese) {
		this.prese = prese;
	}
	private String prese;//介绍

	
	
	
	
	
	
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
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
	/*** 点赞数*/
	public String praise_num = "";
	/*** 评论数*/
	public String comment_num = "";
	/** * 最近参与评论的用户头像*/
	public List<UserEntity> user_heads;
	

	public String getStudy_num() {
		return study_num;
	}
	public void setStudy_num(String study_num) {
		this.study_num = study_num;
	}
	
	
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
	public String getStudentCount() {
		return studentCount;
	}
	public void setStudentCount(String studentCount) {
		this.studentCount = studentCount;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
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
