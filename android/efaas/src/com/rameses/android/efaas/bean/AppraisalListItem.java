package com.rameses.android.efaas.bean;

public class AppraisalListItem {
	
	private String objid, subclass, specificclass, actualuse, rate, areasqm;
	
	public AppraisalListItem(String objid, String subclass, String specificclass, String actualuse, String rate, String areasqm){
		this.objid = objid;
		this.subclass = subclass;
		this.specificclass = specificclass;
		this.actualuse = actualuse;
		this.rate = rate;
		this.areasqm = areasqm;
	}
	
	public String getObjid(){ return objid; }
	public String getSubClass(){ return subclass; }
	public String getSpecificClass(){ return specificclass; }
	public String getActualUse(){ return actualuse; }
	public String getRate(){ return rate; }
	public String getArea(){ return areasqm; }

}
