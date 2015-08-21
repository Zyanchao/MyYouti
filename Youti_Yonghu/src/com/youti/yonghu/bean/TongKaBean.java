package com.youti.yonghu.bean;

import java.util.List;

public class TongKaBean {
	
	private List<Tongka> ton_lists ;
	private String desc;

	public void setTon_list(List<Tongka> ton_list){
	this.ton_lists = ton_list;
	}
	public List<Tongka> getTon_list(){
	return this.ton_lists;
	}
	public void setDesc(String desc){
	this.desc = desc;
	}
	public String getDesc(){
	return this.desc;
	}

	
	
	public class Tongka {
		
		private String title;

		private String price;

		private String expday;

		private String ton_id;

		public void setTitle(String title){
		this.title = title;
		}
		public String getTitle(){
		return this.title;
		}
		public void setPrice(String price){
		this.price = price;
		}
		public String getPrice(){
		return this.price;
		}
		public void setExpday(String expday){
		this.expday = expday;
		}
		public String getExpday(){
		return this.expday;
		}
		public void setTon_id(String ton_id){
		this.ton_id = ton_id;
		}
		public String getTon_id(){
		return this.ton_id;
		}
		
		
	}
	

}
