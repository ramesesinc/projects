package com.rameses.android.efaas.bean;

public class UploadItem {
	
	private String objid, pin, name, tdno;
	private boolean checked = false;
	
	public UploadItem(String objid, String pin, String tdno, String name, boolean checked){
		this.objid = objid;
		this.pin = pin;
		this.tdno = tdno;
		this.name = name;
		this.checked = checked;
	}
	
	public String getObjid(){ return objid; }
	public String getPin(){ return pin; }
	public String getName(){ return name; }
	public String getTdNo(){ return tdno; }
	public boolean isChecked(){ return checked; } 
	
	public void setCheck(boolean b){
		checked = b;
	}

}
