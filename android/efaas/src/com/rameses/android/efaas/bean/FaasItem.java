package com.rameses.android.efaas.bean;

public class FaasItem {
	
	private String objid, pin, name, tdno;
	
	public FaasItem(String objid, String pin, String tdno, String name){
		this.objid = objid;
		this.pin = pin;
		this.tdno = tdno;
		this.name = name;
	}
	
	public String getObjid(){ return objid; }
	public String getPin(){ return pin; }
	public String getName(){ return name; }
	public String getTdNo(){ return tdno; }

}
