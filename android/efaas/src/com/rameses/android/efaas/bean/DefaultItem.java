package com.rameses.android.efaas.bean;

public class DefaultItem {
	
	private String objid, name;
	
	public DefaultItem(String objid, String name){
		this.objid = objid;
		this.name = name;
	}
	
	public String getObjid(){ return objid; }
	public String getName(){ return name; }

}
