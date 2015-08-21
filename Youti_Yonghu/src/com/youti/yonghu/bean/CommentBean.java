package com.youti.yonghu.bean;

import java.io.Serializable;
import java.util.List;

public class CommentBean implements Serializable{

	
	private String praise_num;//点赞数
    private String comment_num;//评论数
	private List<UserEntity>  user_heads;
	private List<Comment>  praise;
	
	
	
	public String getPraise_num() {
		return praise_num;
	}
	public void setPraise_num(String praise_num) {
		this.praise_num = praise_num;
	}
	public String getComment_num() {
		return comment_num;
	}
	public void setComment_num(String comment_num) {
		this.comment_num = comment_num;
	}
	public List<UserEntity> getUser_heads() {
		return user_heads;
	}
	public void setUser_heads(List<UserEntity> user_heads) {
		this.user_heads = user_heads;
	}
	public List<Comment> getPraise() {
		return praise;
	}
	public void setPraise(List<Comment> praise) {
		this.praise = praise;
	}
	

	
	
}
