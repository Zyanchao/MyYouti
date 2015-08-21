package com.youti.yonghu.bean;


/**
 * 
* @ClassName: Praise 
* @Description: TODO(评论详情 类) 
* @author zychao 
* @date 2015-6-9 下午2:24:35 
*
 */
public class Comment  {
	
	
	private String  addtime;//评论时间
	private String  content;//评论内容
	private String  user_name;//评论人姓名
	private String  head_img;//评论人头像
	
	public String [] comment_img;//评论中的图片
	
	public String getHead_img() {
		return head_img;
	}
	public void setHead_img(String head_img) {
		this.head_img = head_img;
	}
	
	
	public String[] getComment_img() {
		return comment_img;
	}
	public void setComment_img(String[] comment_img) {
		this.comment_img = comment_img;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	

}
