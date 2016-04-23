package com.rameses.waterworks.bean;

public class Stubout {
    
    private String objid, code, description, zoneid, barangayid, barangayname, assigneeid;
    
    public Stubout(String objid, String code, String description, String zoneid, String barangayid, String barangayname, String assigneeid){
        this.objid = objid;
        this.code = code;
        this.description = description;
        this.zoneid = zoneid;
        this.barangayid = barangayid;
        this.barangayname = barangayname;
        this.assigneeid = assigneeid;
    }
    
    public String getObjid(){ return objid; }
    public String getCode(){ return code; }
    public String getDescription(){ return description; }
    public String getZoneId(){ return zoneid; }
    public String getBarangayId(){ return barangayid; }
    public String getBarangayName(){ return barangayname; }
    public String getAssigneeId(){ return assigneeid; }
    
}
