package com.youti.yonghu.bean;

import java.util.List;

/**
 * 筛选条件
 * @author zyc
 */
public class FilterLists {


	private List<FilterPrice> price_list;
	private List<FilterVal> val_list;
	private List<FilterRegion> region_list;
	private List<FilterWeek> week;
	
	public List<FilterWeek> getWeek() {
		return week;
	}
	public void setWeek(List<FilterWeek> week) {
		this.week = week;
	}
	public List<FilterPrice> getPrice_list() {
		return price_list;
	}
	public void setPrice_list(List<FilterPrice> price_list) {
		this.price_list = price_list;
	}
	public List<FilterVal> getVal_list() {
		return val_list;
	}
	public void setVal_list(List<FilterVal> val_list) {
		this.val_list = val_list;
	}
	public List<FilterRegion> getRegion_list() {
		return region_list;
	}
	public void setRegion_list(List<FilterRegion> region_list) {
		this.region_list = region_list;
	}
	
	
}
