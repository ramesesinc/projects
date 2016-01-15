package com.rameses.waterworks.bean;

/**
 *
 * @author Dino
 */
public class Reading {
    
    String objid, meterid, reading, consumption, amtdue, totaldue, state;
    
    public Reading(String objid, String meterid, String reading, String consumption, String amtdue, String totaldue, String state){
        this.objid = objid;
        this.meterid = meterid;
        this.reading = reading;
        this.consumption = consumption;
        this.amtdue = amtdue;
        this.totaldue = totaldue;
        this.state = state;
    }
    
    public String getObjid(){ return objid; };
    public String getMeterid(){ return meterid; };
    public String getReading(){ return reading; };
    public String getConsumption() { return consumption; };
    public String getAmtDue() { return amtdue; };
    public String getTotalDue() { return totaldue; };
    public String getState() { return state; };
    
}
