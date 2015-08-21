package com.youti.yonghu.bean;

import java.util.List;

public class CoachDetailBean extends CoachBean{
	
	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 1L;
	public String coach_result;
	public String video_url;
	public String content_dec;
	public String server;
	
	public String teach_age;
	public String pro_cert;
	public String ide_cert;
	public String de_img;
	public String head_img;
	public String []coach_exp;
	
	public String praise;//该用户是否点赞
	
	public String getPraise() {
		return praise;
	}
	public void setPraise(String praise) {
		this.praise = praise;
	}
	public void setCoach_exp(String[] coach_exp) {
		this.coach_exp = coach_exp;
	}
	public String teach_level;//"初级、中级",
	public List <PicsBean>coach_photo;//相册
	public List <PicsBean>teach_content_photo;//教学成果 相关图片
	public List<CourseBean> other_course; //其他课程
	
	public String getCoach_result() {
		return coach_result;
	}
	public void setCoach_result(String coach_result) {
		this.coach_result = coach_result;
	}
	public String getVideo_url() {
		return video_url;
	}
	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}
	public String getContent_dec() {
		return content_dec;
	}
	public void setContent_dec(String content_dec) {
		this.content_dec = content_dec;
	}
	
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String[] getCoach_exp() {
		return coach_exp;
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
	public String getTeach_age() {
		return teach_age;
	}
	public void setTeach_age(String teach_age) {
		this.teach_age = teach_age;
	}
	public String getPro_cert() {
		return pro_cert;
	}
	public void setPro_cert(String pro_cert) {
		this.pro_cert = pro_cert;
	}
	public String getIde_cert() {
		return ide_cert;
	}
	public void setIde_cert(String ide_cert) {
		this.ide_cert = ide_cert;
	}
	public String getDe_img() {
		return de_img;
	}
	public void setDe_img(String de_img) {
		this.de_img = de_img;
	}
	public String getHead_img() {
		return head_img;
	}
	public void setHead_img(String head_img) {
		this.head_img = head_img;
	}
	public String getTeach_level() {
		return teach_level;
	}
	public void setTeach_level(String teach_level) {
		this.teach_level = teach_level;
	}
	public List<PicsBean> getCoach_photo() {
		return coach_photo;
	}
	public void setCoach_photo(List<PicsBean> coach_photo) {
		this.coach_photo = coach_photo;
	}
	public List<PicsBean> getTeach_content_photo() {
		return teach_content_photo;
	}
	public void setTeach_content_photo(List<PicsBean> teach_content_photo) {
		this.teach_content_photo = teach_content_photo;
	}
	public List<CourseBean> getOther_course() {
		return other_course;
	}
	public void setOther_course(List<CourseBean> other_course) {
		this.other_course = other_course;
	}
	
 	
	
	
}
