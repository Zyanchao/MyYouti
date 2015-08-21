package com.youti.yonghu.bean;

import java.io.Serializable;

/*
 *广告页 实体类
 */
public class Carousel implements Serializable{
	
	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 1L;
	String  advert_img;
	String img_url;
	public String getAdvert_img() {
		return advert_img;
	}
	public void setAdvert_img(String advert_img) {
		this.advert_img = advert_img;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	
	
}
