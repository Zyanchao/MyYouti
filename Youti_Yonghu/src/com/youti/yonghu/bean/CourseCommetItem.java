package com.youti.yonghu.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class CourseCommetItem implements Serializable{
	
	
	/**
	 * 课程详情 评论 
	 */
	private static final long serialVersionUID = 1L;
	public String id ="";
	public String courseId ="";//课程 id
	public String coachId ="";// 教练 id
	
	public String Id ="";// 教练 id
	public ArrayList<PicsBean> picList ;//评论 图片列表
	public String date = "";//日期
	public String desc;//评论内容
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCoachId() {
		return coachId;
	}
	public void setCoachId(String coachId) {
		this.coachId = coachId;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
