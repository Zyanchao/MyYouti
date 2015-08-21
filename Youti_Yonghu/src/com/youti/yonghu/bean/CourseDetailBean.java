package com.youti.yonghu.bean;

import java.util.List;

public class CourseDetailBean extends CourseBean{
	
	
	/**
	 * 课程 类
	 */
	private static final long serialVersionUID = 1L;
	
	private String teach_plain;// 教学计划
	private String waring;// 注意事项
	private String agree_people;//适合人群 
	private String people_number;// 人数
	private String start_time;//时间
	private String se_time;//时间
	
	private String address;// 地址
	private String car_way;// 乘车路线
	private String video;// 视频url
	private String[] de_img;// 图片url
	
	private String club_id;
	public String getClub_id() {
		return club_id;
	}
	public void setClub_id(String club_id) {
		this.club_id = club_id;
	}
	private String praise_num;// 点赞数
	private String comment_num;// 评论数
	private List<UserEntity> user_heads;//(点赞头像)user_id用户id user_img用户最新点赞头像url
	public List<CourseBean> other_course; //其他课程//(其他课程)course_id课程id   img课程图片 title标题 prese介绍
	private String praise;//是否点赞
	
	
	public String[] getDe_img() {
		return de_img;
	}
	public void setDe_img(String[] de_img) {
		this.de_img = de_img;
	}
	public String getSe_time() {
		return se_time;
	}
	public void setSe_time(String se_time) {
		this.se_time = se_time;
	}
	public String getPraise() {
		return praise;
	}
	public void setPraise(String praise) {
		this.praise = praise;
	}
	public String getTeach_plain() {
		return teach_plain;
	}
	public void setTeach_plain(String teach_plain) {
		this.teach_plain = teach_plain;
	}
	public String getWaring() {
		return waring;
	}
	public void setWaring(String waring) {
		this.waring = waring;
	}
	public String getAgree_people() {
		return agree_people;
	}
	public void setAgree_people(String agree_people) {
		this.agree_people = agree_people;
	}
	public String getPeople_number() {
		return people_number;
	}
	public void setPeople_number(String people_number) {
		this.people_number = people_number;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCar_way() {
		return car_way;
	}
	public void setCar_way(String car_way) {
		this.car_way = car_way;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
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
	public List<CourseBean> getOther_course() {
		return other_course;
	}
	public void setOther_course(List<CourseBean> other_course) {
		this.other_course = other_course;
	}
	
}
