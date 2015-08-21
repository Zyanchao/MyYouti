package com.youti.yonghu.bean;

import java.util.List;

public class ClubBean {
	
	private String  club_about;//(俱乐部介绍)
	private String club_address;//(俱乐部地址)
	private String backg_img;//(背景图片)
	private List<ClubPhoto> club_photo;
	
	

	public String getClub_about() {
		return club_about;
	}

	public void setClub_about(String club_about) {
		this.club_about = club_about;
	}

	public String getClub_address() {
		return club_address;
	}


	public void setClub_address(String club_address) {
		this.club_address = club_address;
	}


	public String getBackg_img() {
		return backg_img;
	}


	public void setBackg_img(String backg_img) {
		this.backg_img = backg_img;
	}


	public List<ClubPhoto> getClub_photo() {
		return club_photo;
	}


	public void setClub_photo(List<ClubPhoto> club_photo) {
		this.club_photo = club_photo;
	}


	
	
	
	public class ClubPhoto {
		private String 	 photo_id;//(图片id)
		private String 	 club_id;//(俱乐部id)
		private String 	 img_url;//(图片url)
		
		
		public String getPhoto_id() {
			return photo_id;
		}
		public void setPhoto_id(String photo_id) {
			this.photo_id = photo_id;
		}
		public String getClub_id() {
			return club_id;
		}
		public void setClub_id(String club_id) {
			this.club_id = club_id;
		}
		public String getImg_url() {
			return img_url;
		}
		public void setImg_url(String img_url) {
			this.img_url = img_url;
		}
		
	}
	

}
