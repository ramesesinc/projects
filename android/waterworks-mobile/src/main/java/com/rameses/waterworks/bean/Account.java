package com.rameses.waterworks.bean;

public class Account {
    
    private String 
            objid,
            acctno,
            acctname,
            address,
            mobileno,
            phoneno,
            email,
            serialno,
            areaid,
            balance,
            penalty,
            othercharge,
            lastreading,
            lasttxndate,
            areaname,
            classificationid,
            lastreadingyear,
            lastreadingmonth,
            lastreadingdate,
            barcode;
    
    private String
            consumption,
            amtdue,
            totaldue,
            presreading;
    
    private String
            assignee_objid,
            assignee_name;
    
    public Account(
            String objid,
            String acctno,
            String acctname,
            String address,
            String mobileno,
            String phoneno,
            String email,
            String serialno,
            String areaid,
            String balance,
            String penalty,
            String othercharge,
            String lastreading,
            String lasttxndate,
            String areaname,
            String classificationid,
            String lastreadingyear,
            String lastreadingmonth,
            String lastreadingdate,
            String barcode
    ){
        this.objid = objid;
        this.acctno = acctno;
        this.acctname = acctname;
        this.address = address;
        this.mobileno = mobileno;
        this.phoneno = phoneno;
        this.email = email;
        this.serialno = serialno;
        this.areaid = areaid;
        this.balance = balance;
        this.penalty = penalty;
        this.othercharge = othercharge;
        this.lastreading = lastreading;
        this.lasttxndate = lasttxndate;
        this.areaname = areaname;
        this.classificationid = classificationid;
        this.lastreadingyear = lastreadingyear;
        this.lastreadingmonth = lastreadingmonth;
        this.lastreadingdate = lastreadingdate;
        this.barcode = barcode;
    }
    
    public String getObjid(){ return objid; }
    public String getAcctNo(){ return acctno; }
    public String getAcctName(){ return acctname; }
    public String getAddress(){ return address; }
    public String getMobileNo(){ return mobileno; }
    public String getPhoneNo(){ return phoneno; }
    public String getEmail(){ return email; }
    public String getSerialNo(){ return serialno; }
    public String getAreaId(){ return areaid; }
    public String getBalance(){ return balance; }
    public String getPenalty(){ return penalty; }
    public String getOtherCharge(){ return othercharge; }
    public String getLastReading(){ return lastreading; }
    public String getPresReading(){ return presreading; }
    public String getLastTxnDate(){ return lasttxndate; }
    public String getAreaName(){ return areaname; }
    public String getClassificationId(){ return classificationid; }
    public String getLastReadingYear(){ return lastreadingyear; }
    public String getLastReadingMonth(){ return lastreadingmonth; }
    public String getLastReadingDate(){ return lastreadingdate; }
    public String getBarCode(){ return barcode; }
    
    public String getConsumption(){ return consumption; }
    public String getAmtDue(){ return amtdue; }
    public String getTotalDue(){ return totaldue; }
    
    public void setConsumption(String s){ consumption = s; }
    public void setAmtDue(String s){ amtdue = s; }
    public void setTotalDue(String s){ totaldue = s; }
    
    public void setPresReading(int i){
        presreading = String.valueOf(i);
    }
    
}
