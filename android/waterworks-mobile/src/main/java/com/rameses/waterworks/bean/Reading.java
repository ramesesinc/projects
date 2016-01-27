package com.rameses.waterworks.bean;

/**
 *
 * @author Dino
 */
public class Reading {
    
    String objid, acctid, reading, consumption, amtdue, totaldue, state, readingdate;
    
    public Reading(String objid, String acctid, String reading, String consumption, String amtdue, String totaldue, String state, String readingdate){
        this.objid = objid;
        this.acctid = acctid;
        this.reading = reading;
        this.consumption = consumption;
        this.amtdue = amtdue;
        this.totaldue = totaldue;
        this.state = state;
        this.readingdate = readingdate;
    }
    
    public String getObjid(){ return objid; };
    public String getAcctId(){ return acctid; };
    public String getReading(){ return reading; };
    public String getConsumption() { return consumption; };
    public String getAmtDue() { return amtdue; };
    public String getTotalDue() { return totaldue; };
    public String getState() { return state; };
    public String getReadingDate() { return readingdate; };
    
}
