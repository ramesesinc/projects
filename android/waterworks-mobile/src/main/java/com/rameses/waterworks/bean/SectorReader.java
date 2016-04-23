package com.rameses.waterworks.bean;

public class SectorReader {
    
    private String objid, sectorid, title, assigneeid, assigneename;
    
    public SectorReader(String objid, String sectorid, String title, String assigneeid, String assigneename){
        this.objid = objid;
        this.sectorid = sectorid;
        this.title = title;
        this.assigneeid = assigneeid;
        this.assigneename = assigneename;
    }
    
    public String getObjid(){ return objid; }
    public String getSectorId(){ return sectorid; }
    public String getTitle(){ return title; }
    public String getAssigneeId(){ return assigneeid; }
    public String getAssigneeName(){ return assigneename; }
    
}
