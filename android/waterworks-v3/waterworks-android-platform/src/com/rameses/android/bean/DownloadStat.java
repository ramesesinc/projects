package com.rameses.android.bean;

public class DownloadStat {

	private String batchid;
	private String assigneeid;
	private int recordcount;
	private int indexno;
	
	public DownloadStat() {}
	
	public DownloadStat(String batchid, String assigneeid, int recordcount, int indexno) {
		this.batchid = batchid;
		this.assigneeid = assigneeid;
		this.recordcount = recordcount;
		this.indexno = indexno;
	}
	
	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}
	
	public void setAssigneeid(String assigneeid) {
		this.assigneeid = assigneeid;
	}
	
	public void setRecordcount(int recordcount) {
		this.recordcount = recordcount;
	}
	
	public void setIndexno(int indexno) {
		this.indexno = indexno;
	}
	
	public String getBatchid() {
		return batchid;
	}
	
	public String getAssigneeid() {
		return assigneeid;
	}
	
	public int getRecordcount() {
		return recordcount;
	}
	
	public int getIndexno() {
		return indexno;
	}
}
