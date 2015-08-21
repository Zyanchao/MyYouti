package com.youti.yonghu.bean;

import java.util.List;

public class FavoriateVideoBean {
	public String code;
	public String info;
	public List<VideoItemBean> list;
	
	public class VideoItemBean{
		public String video_id;
		public String img;
		public String praise_num;
		public String study_num;
		
	}
}
