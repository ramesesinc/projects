package com.rameses.android.efaas.bean;

public class HomeListItem {
	
	int id;
	String title;

	public HomeListItem(int id, String title) {
		this.id = id;
		this.title = title;
	}
	
	public int getId(){ return id; }
	public String getTitle(){ return title; }

}
