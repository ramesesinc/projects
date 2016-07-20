package com.rameses.android.efaas.bean;

public class FloorItem {
	
	private String objid, floorno, floorarea;
	
	public FloorItem(String objid, String floorno, String floorarea){
		this.objid = objid;
		this.floorno = floorno;
		this.floorarea = floorarea;
	}
	
	public String getObjid(){ return objid; }
	public String getFloorNo(){ return floorno; }
	public String getFloorArea(){ return floorarea; }

}
