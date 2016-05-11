package com.rameses.waterworks.bean;

public class Zone {
    
    private String objid, sectorid, code, description, readerid, assigneeid;
    
    public Zone(String objid, String sectorid, String code, String description, String readerid, String assigneeid){
        this.objid = objid;
        this.sectorid = sectorid;
        this.code = code;
        this.description = description;
        this.readerid = readerid;
        this.assigneeid = assigneeid;
    }
    
    public String getObjid(){ return objid; }
    public String getSectorId(){ return sectorid; }
    public String getCode(){ return code; }
    public String getDescription(){ return description; }
    public String getReaderId(){ return readerid; }
    public String getAssigneeId(){ return assigneeid; }
    
}
