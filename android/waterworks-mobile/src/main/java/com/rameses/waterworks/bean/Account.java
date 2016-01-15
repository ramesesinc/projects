package com.rameses.waterworks.bean;

public class Account {
    
    private String 
            objid,
            state,
            dtstarted,
            acctno,
            applicationid,
            name,
            address,
            cellphoneno,
            meterid,
            areaid,
            area,
            balance,
            lasttxndate,
            serialno,
            prevreading,
            presreading,
            assignee_objid,
            assignee_name;
    
    private String
            consumption,
            amtdue,
            totaldue;
    
    public Account(
           String objid,
           String state,
           String dtstarted,
           String acctno,
           String applicationid,
           String name,
           String address,
           String cellphoneno,
           String meterid,
           String areaid,
           String area,
           String balance,
           String lasttxndate,
           String serialno,
           String prevreading,
           String presreading,
           String assignee_objid,
           String assignee_name
    ){
        this.objid = objid;
        this.state = state;
        this.dtstarted = dtstarted;
        this.acctno = acctno;
        this.applicationid = applicationid;
        this.name = name;
        this.address = address;
        this.cellphoneno = cellphoneno;
        this.meterid = meterid;
        this.areaid = areaid;
        this.area = area;
        this.balance = balance;
        this.lasttxndate = lasttxndate;
        this.serialno = serialno;
        this.prevreading = prevreading;
        this.presreading = presreading;
        this.assignee_objid = assignee_objid;
        this.assignee_name = assignee_name;
    }
    
    public String getObjid(){ return objid; }
    public String getState(){ return state; }
    public String getDtstarted(){ return dtstarted; }
    public String getAcctno(){ return acctno; }
    public String getApplicationid(){ return applicationid; }
    public String getName(){ return name; }
    public String getAddress(){ return address; }
    public String getCellphoneno(){ return cellphoneno; }
    public String getMeterid(){ return meterid; }
    public String getAreaid(){ return areaid; }
    public String getArea(){ return area; }
    public String getBalance(){ return balance; }
    public String getLasttxndate(){ return lasttxndate; }
    public String getSerialno(){ return serialno; }
    public String getPrevReading(){ return prevreading; }
    public String getPresReading(){ return presreading; }
    public String getAssigneeObjid(){ return assignee_objid; }
    public String getAssigneeName(){ return assignee_name; }
    
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
