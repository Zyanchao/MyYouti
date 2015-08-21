package com.youti.yonghu.bean;

/**
 * 热门 教练 实体类
 */
public class HotCoach {

	private String coach_id;//教练id
	private String coach_img;//图片路径
	private String praise_num;//点赞数
	private String comment_num;//评论数
	private String json_img;//点赞的用户头像
	public String getCoach_id() {
		return coach_id;
	}
	public void setCoach_id(String coach_id) {
		this.coach_id = coach_id;
	}
	public String getCoach_img() {
		return coach_img;
	}
	public void setCoach_img(String coach_img) {
		this.coach_img = coach_img;
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
	public String getJson_img() {
		return json_img;
	}
	public void setJson_img(String json_img) {
		this.json_img = json_img;
	}
	
	
	
	
}
