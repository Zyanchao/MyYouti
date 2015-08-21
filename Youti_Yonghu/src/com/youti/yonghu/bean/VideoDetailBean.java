package com.youti.yonghu.bean;

import java.util.List;

public class VideoDetailBean {
	public String code;
	public String info;
	public VideoDetailItem list;
	
	public class VideoDetailItem{
		public List<PraiseItem> praise;
		public String praise_num;
		public List<CommentItem> comment;
		public List<CloseCourse> course;
		
		
		
		public String video_id;
		public String video_url;
		public String title;
		public String course_introduce;
		public String coach_introduce;
		public String coach_img;
		public String coach_name;
		public List<VideoContent> videolist;
		
		//可能为多余的三个字段
		public String tuijian;
		public String type_id;
		public String course_title;
		
	}
	//最近距离课程
	public class CloseCourse{
		public String img;
		public String id;
		public String introduce;
		public String title;
	}
	//评论集合
	public class CommentItem{
		public String user_name;
		public String user_img;
		public String comment_content;
		public String comment_time;
		public String getUser_name() {
			return user_name;
		}
		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}
		public String getUser_img() {
			return user_img;
		}
		public void setUser_img(String user_img) {
			this.user_img = user_img;
		}
		public String getComment_content() {
			return comment_content;
		}
		public void setComment_content(String comment_content) {
			this.comment_content = comment_content;
		}
		public String getComment_time() {
			return comment_time;
		}
		public void setComment_time(String comment_time) {
			this.comment_time = comment_time;
		}
		
		
	}
	//点赞集合
	public class PraiseItem{
		public String user_id;
		public String user_img;
		public String getUser_id() {
			return user_id;
		}
		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
		public String getUser_img() {
			return user_img;
		}
		public void setUser_img(String user_img) {
			this.user_img = user_img;
		}
		
	}
	
	public class VideoContent{
		public String type_id;
		public String title;
		public String video_url;
		public String coach_img;
		public String video_size;
	}
}
