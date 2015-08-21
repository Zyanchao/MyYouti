package com.youti.yonghu.bean;

import android.graphics.Bitmap;

public class ChatMsgEntity {
	private static final String TAG = ChatMsgEntity.class.getSimpleName();

	private Bitmap headBitmap;
	private String tx;
	private String name;
	private int id;
	private String chat;
	private String uri;
	private String path;
	private String url;
	private String thumb;
	private int refer;
	private String time;
	private Bitmap imgBitmap;

	
	public ChatMsgEntity() {
	}
	
	public Bitmap getHeadBitmap() {
		return headBitmap;
	}

	public void setHeadBitmap(Bitmap headBitmap) {
		this.headBitmap = headBitmap;
	}

	public String getTx() {
		return tx;
	}

	public void setTx(String tx) {
		this.tx = tx;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String dateTime) {
		this.time = dateTime;
	}

	public String getChat() {
		return chat;
	}

	public void setChat(String chat) {
		this.chat = chat;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Bitmap getImgBitmap() {
		return imgBitmap;
	}

	public void setImgBitmap(Bitmap imgBitmap) {
		this.imgBitmap = imgBitmap;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public int getRefer() {
		return refer;
	}

	public void setRefer(int refer) {
		this.refer = refer;
	}

}
