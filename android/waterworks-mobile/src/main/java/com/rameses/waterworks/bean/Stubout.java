package com.rameses.waterworks.bean;

public class Stubout {
    
    String objid, code, description, zoneid, zonecode, zonedesc, sectorid, sectorcode, areaid, areatitle, assigneeid, assigneename;
    
    public Stubout(
            String objid,
            String code, 
            String description, 
            String zoneid,
            String zonecode,
            String zonedesc,
            String sectorid,
            String sectorcode,
            String areaid,
            String areatitle,
            String assigneeid,
            String assigneename
            ){
        this.objid = objid;
        this.code = code;
        this.description = description;
        this.zoneid = zoneid;
        this.zonecode = zonecode;
        this.zonedesc = zonedesc;
        this.sectorid = sectorid;
        this.sectorcode = sectorcode;
        this.areaid = areaid;
        this.areatitle = areatitle;
        this.assigneeid = assigneeid;
        this.assigneename = assigneename;
    }
    
    public String getObjid(){ return objid; }
    public String getCode(){ return code; }
    public String getDescription(){ return description; }
    public String getZoneId(){ return zoneid; }
    public String getZoneCode(){ return zonecode; }
    public String getZoneDesc(){ return zonedesc; }
    public String getSectorId(){ return sectorid; }
    public String getSectorCode(){ return sectorcode; }
    public String getAreaId(){ return areaid; }
    public String getAreaTitle(){ return areatitle; }
    public String getAssigneeId(){ return assigneeid; }
    public String getAssigneeName(){ return assigneename; }
    
}
