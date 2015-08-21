package com.youti.yonghu.bean;

/**
 * 筛选条件
 * @author zyc
 */
public class FilterItem {

	private String typeId;//模块 id（课程，教练，视频，社区）
	private String itemName;// 筛选条件 (项目，距离...)
	private String itemId;
	
	
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	
	
}
