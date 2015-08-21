package com.youti.yonghu.bean;

import java.io.Serializable;
import java.util.List;

/**
* @ClassName: ActiveBean 
* @Description: TODO(首页 四小项 活动) 
* @author zychao 
* @date 2015-6-30 下午4:32:55 
*
 */
public class ActiveBean implements Serializable{
	
	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = -2871846355135162962L;
	private String id;
	private String val;
	private String start_time;
	private String[] img;
	

	private String week;
	private String address;
	private String price;
	private String  jd;	
	private String  wd;	
	private String  alr_time ;// 时长
	

	private String end_time;
	private String baoming_num;
	private String de_url;

	
	
	public String[] getImg() {
		return img;
	}

	public void setImg(String[] img) {
		this.img = img;
	}
	
	public String getAlr_time() {
		return alr_time;
	}

	public void setAlr_time(String alr_time) {
		this.alr_time = alr_time;
	}
	
	public String getDe_url() {
		return de_url;
	}

	public void setDe_url(String de_url) {
		this.de_url = de_url;
	}

	private List<SingBean>  user_list;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}


	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}



	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getJd() {
		return jd;
	}

	public void setJd(String jd) {
		this.jd = jd;
	}

	public String getWd() {
		return wd;
	}

	public void setWd(String wd) {
		this.wd = wd;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getBaoming_num() {
		return baoming_num;
	}

	public void setBaoming_num(String baoming_num) {
		this.baoming_num = baoming_num;
	}


	public List<SingBean> getUser_list() {
		return user_list;
	}

	public void setUser_list(List<SingBean> user_list) {
		this.user_list = user_list;
	}

	
	
	
	

	
	
	

}
