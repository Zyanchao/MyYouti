package com.youti.yonghu.bean;

import java.util.List;

public class VideoListBean {
	public String code;
	public String info;
	public List<VideoListItem> list;
	
	public class VideoListItem{
		
		public String video_id;
		public String img;
		public String title;
		public String isshow;
		public String totalstudy;
		public String comment_num;
		public String praise_num;//点赞个数
		public String is_zan;//用户是否点赞
		public int dataId; //给每条数据添加id，这样listview就可以局部刷新了
		public List<VideoZanItem> user_img; //放置头像的集合
		public String getPraise_num() {
			return praise_num;
		}
		public void setPraise_num(String praise_num) {
			this.praise_num = praise_num;
		}
		public String getIs_zan() {
			return is_zan;
		}
		public void setIs_zan(String is_zan) {
			this.is_zan = is_zan;
		}
		public int getDataId() {
			return dataId;
		}
		public void setDataId(int dataId) {
			this.dataId = dataId;
		}
		public List<VideoZanItem> getUser_img() {
			return user_img;
		}
		public void setUser_img(List<VideoZanItem> user_img) {
			this.user_img = user_img;
		}
		
	}
	
	public class VideoZanItem {
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
}
