package com.youti.yonghu.bean;

import java.io.Serializable;

public class PicsBean implements Serializable{

	/**
	 * 评论 信息 中的图片 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 图片路径
	 */
	private String img_url;

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

}
