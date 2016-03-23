package com.rameses.waterworks.bean;

import com.rameses.util.ObjectDeserializer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            lastreading,
            lasttxndate,
            areaname,
            classificationid,
            lastreadingyear,
            lastreadingmonth,
            lastreadingdate,
            barcode,
            batchid,
            month,
            year,
            period,
            duedate,
            discodate,
            rundate,
            items,
            info;
    
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
            String lastreading,
            String lasttxndate,
            String areaname,
            String classificationid,
            String lastreadingyear,
            String lastreadingmonth,
            String lastreadingdate,
            String barcode,
            String batchid,
            String month,
            String year,
            String period,
            String duedate,
            String discodate,
            String rundate,
            String items,
            String info
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
        this.lastreading = lastreading;
        this.lasttxndate = lasttxndate;
        this.areaname = areaname;
        this.classificationid = classificationid;
        this.lastreadingyear = lastreadingyear;
        this.lastreadingmonth = lastreadingmonth;
        this.lastreadingdate = lastreadingdate;
        this.barcode = barcode;
        this.batchid = batchid;
        this.month = month;
        this.year = year;
        this.period = period;
        this.duedate = duedate;
        this.discodate = discodate;
        this.rundate = rundate;
        this.items = items;
        this.info = info;
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
    public String getLastReading(){ return lastreading; }
    public String getPresReading(){ return presreading; }
    public String getLastTxnDate(){ return lasttxndate; }
    public String getAreaName(){ return areaname; }
    public String getClassificationId(){ return classificationid; }
    public String getLastReadingYear(){ return lastreadingyear; }
    public String getLastReadingMonth(){ return lastreadingmonth; }
    public String getLastReadingDate(){ return lastreadingdate; }
    public String getBarCode(){ return barcode; }
    public String getBatchId(){ return batchid; }
    public String getMonth(){ return month; }
    public String getYear(){ return year; }
    public String getPeriod(){ return period; }
    public String getDueDate(){ return duedate; }
    public String getDiscoDate(){ return discodate; }
    public String getRunDate(){ return rundate; }
    public String getItems(){ return items; }
    public String getInfo(){ return info; }
    
    public String getConsumption(){ return consumption; }
    public String getAmtDue(){ return amtdue; }
    public String getTotalDue(){ return totaldue; }
    
    public void setConsumption(String s){ consumption = s; }
    public void setAmtDue(String s){ amtdue = s; }
    public void setTotalDue(String s){ totaldue = s; }
    
    public void setPresReading(int i){
        presreading = String.valueOf(i);
    }
    
    public List<ItemAccount> getItemList(){
        List<ItemAccount> itemList = new ArrayList<ItemAccount>();
        List<Map> list = (List) ObjectDeserializer.getInstance().read(items);
        System.out.println("String Items : " + items);
        System.out.println("Account List : " + list.toString());
        for(Map m : list){
            String account = m.get("account") != null ? m.get("account").toString() : "";
            double amount = m.get("amount") != null ? Double.parseDouble(m.get("amount").toString()) : 0.00;
            itemList.add(new ItemAccount(account,amount));
        }
        return itemList;
    }
    
}
