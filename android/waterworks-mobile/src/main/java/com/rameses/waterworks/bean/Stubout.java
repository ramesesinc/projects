package com.rameses.waterworks.bean;

/**
 *
 * @author Rameses
 */
public class Stubout {
    
    String objid, title, description, areaid;
    
    public Stubout(String objid, String title, String description, String areaid){
        this.objid = objid;
        this.title = title;
        this.description = description;
        this.areaid = areaid;
    }
    
    public String getObjid(){ return objid; }
    public String getTitle(){ return title; }
    public String getDescription(){ return description; }
    public String getAreaId(){ return areaid; }
    
}
