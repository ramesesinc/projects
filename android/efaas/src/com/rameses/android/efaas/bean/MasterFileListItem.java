package com.rameses.android.efaas.bean;

public class MasterFileListItem {
	
	private String title, desc;
	
	public MasterFileListItem(String title, String desc){
		this.title = title;
		this.desc = desc;
	}
	
	public String getTitle(){ return title; }
	public String getDescription(){ return desc; }

}
