package com.rameses.waterworks.bean;

/**
 *
 * @author Rameses
 */
public class StuboutAccount {
    
    String objid, parentid, acctid;
    int sortorder;
    
    public StuboutAccount(String objid, String parentid, String acctid, int sortorder){
        this.objid = objid;
        this.parentid = parentid;
        this.acctid = acctid;
        this.sortorder = sortorder;
    }
    
    public String getObjid(){ return objid; }
    public String getParentId(){ return parentid; }
    public String getAcctId(){ return acctid; }
    public int getSortOrder(){ return sortorder; }
    
}
