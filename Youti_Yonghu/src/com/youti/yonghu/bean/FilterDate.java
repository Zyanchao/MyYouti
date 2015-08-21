package com.youti.yonghu.bean;

public class FilterDate {
	public FilterDate(String itemId, String tv_date, String tv_week,
			String wholdDate) {
		super();
		this.itemId = itemId;
		this.tv_date = tv_date;
		this.tv_week = tv_week;
		this.wholdDate = wholdDate;
	}
	
	public FilterDate() {
		super();
	}

	public String itemId;
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getTv_date() {
		return tv_date;
	}
	public void setTv_date(String tv_date) {
		this.tv_date = tv_date;
	}
	public String getTv_week() {
		return tv_week;
	}
	public void setTv_week(String tv_week) {
		this.tv_week = tv_week;
	}
	public String getWholdDate() {
		return wholdDate;
	}
	public void setWholdDate(String wholdDate) {
		this.wholdDate = wholdDate;
	}
	public String tv_date;
	public String tv_week;
	public String wholdDate;
}
