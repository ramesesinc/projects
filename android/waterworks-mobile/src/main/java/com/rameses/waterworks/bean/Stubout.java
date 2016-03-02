package com.rameses.waterworks.bean;

/**
 *
 * @author Rameses
 */
public class Stubout {
    
    String objid, title, description, readinggroupid;
    Object accounts;
    
    public Stubout(String objid, String title, String description, String readinggroupid, Object accounts){
        this.objid = objid;
        this.title = title;
        this.description = description;
        this.readinggroupid = readinggroupid;
        this.accounts = accounts;
    }
    
    public String getObjid(){ return objid; }
    public String getTitle(){ return title; }
    public String getDescription(){ return description; }
    public String getReadingGroupId(){ return readinggroupid; }
    public Object getAccounts(){ return accounts; }
    
}
