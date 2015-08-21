package com.youti.yonghu.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class CardEntity implements Parcelable{
	private int id;
	private String name;
	private String time;
	private String mainBody;
//	private Bitmap head;
	private String head;
//	private List<Bitmap> sourceList;
	private List<String> sourceList;
	private int numForward;
	private int numInformation;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public void setTime(String time) {
		this.time = time;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public List<String> getSourceList() {
		return sourceList;
	}
	public void setSourceList(List<String> sourceList) {
		this.sourceList = sourceList;
	}
	public int getNumForward() {
		return numForward;
	}
	public void setNumForward(int numForward) {
		this.numForward = numForward;
	}
	public int getNumInformation() {
		return numInformation;
	}
	public void setNumInformation(int numInformation) {
		this.numInformation = numInformation;
	}
	public String getMainBody() {
		return mainBody;
	}
	public void setMainBody(String mainBody) {
		this.mainBody = mainBody;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(time);
		dest.writeString(head);
		dest.writeList(sourceList);
		dest.writeInt(numForward);
		dest.writeInt(numInformation);
		dest.writeString(mainBody);
		
	}
	
	
	public static final Parcelable.Creator<CardEntity> CREATOR = new Creator<CardEntity>() {
		public CardEntity createFromParcel(Parcel source) {  
			CardEntity entity = new CardEntity();
			entity.id = source.readInt();
			entity.name = source.readString();
			entity.time = source.readString();
			entity.head = source.readString();
			entity.sourceList = new ArrayList<String>();  
			source.readList(entity.sourceList, ClassLoader.getSystemClassLoader());
			entity.numForward = source.readInt();
			entity.numInformation = source.readInt();
			entity.mainBody = source.readString();
            return entity;  
        }  
  
        public CardEntity[] newArray(int size) {  
            return new CardEntity[size];  
        }  
	};
}
