package com.rameses.android.efaas.bean;

public class HomeItem {
	
	int id;
	String title;

	public HomeItem(int id, String title) {
		this.id = id;
		this.title = title;
	}
	
	public int getId(){ return id; }
	public String getTitle(){ return title; }

}
