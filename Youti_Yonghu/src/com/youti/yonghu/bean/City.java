package com.youti.yonghu.bean;

import com.youti.utils.PinyinUtils;


public class City implements Comparable<City>{

	private String name;
	private String pinyin;
	
	public City(String name) {
		super();
		this.name = name;
		// 获取拼音
		pinyin = PinyinUtils.getPinyin(name);
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	@Override
	public int compareTo(City another) {
		return pinyin.compareTo(another.getPinyin());
	}
	
}
