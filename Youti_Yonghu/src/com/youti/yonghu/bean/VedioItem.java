package com.youti.yonghu.bean;

public class VedioItem {
	public String id; 
	/**是否是推荐**/
	public  String isTj;
	/**多少人学过**/
	public String num;
	/**简单介绍**/
	public String des;
	/**所属用户**/
	public String userID;
	public String img;
	public VedioItem(String id, String isTj, String num, String des,
			String userID, String img) {
		super();
		this.id = id;
		this.isTj = isTj;
		this.num = num;
		this.des = des;
		this.userID = userID;
		this.img = img;
	} 
	
	
}
