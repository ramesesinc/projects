package com.rameses.android.efaas.bean;

public class ExaminationListItem {
	
	private String objid, findings, date;
	
	public ExaminationListItem(String objid, String findings, String date){
		this.objid = objid;
		this.findings = findings;
		this.date = date;
	}
	
	public String getObjid(){ return objid; }
	public String getFindings(){ return findings; }
	public String getDate(){ return date; }

}
