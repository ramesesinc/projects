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
            serialno,
            sectorid,
            sectorcode,
            lastreading,
            classificationid,
            barcode,
            batchid,
            month,
            year,
            period,
            duedate,
            discodate,
            rundate,
            items,
            info,
            stuboutid;
    
    private int sortorder;
    
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
            String serialno,
            String sectorid,
            String sectorcode,
            String lastreading,
            String classificationid,
            String barcode,
            String batchid,
            String month,
            String year,
            String period,
            String duedate,
            String discodate,
            String rundate,
            String items,
            String info,
            String stuboutid,
            int sortorder
    ){
        this.objid = objid;
        this.acctno = acctno;
        this.acctname = acctname;
        this.address = address;
        this.serialno = serialno;
        this.sectorid = sectorid;
        this.sectorcode = sectorcode;
        this.lastreading = lastreading;
        this.classificationid = classificationid;
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
        this.stuboutid = stuboutid;
        this.sortorder = sortorder;
    }
    
    public String getObjid(){ return objid; }
    public String getAcctNo(){ return acctno; }
    public String getAcctName(){ return acctname; }
    public String getAddress(){ return address; }
    public String getSerialNo(){ return serialno; }
    public String getSectorId(){ return sectorid; }
    public String getLastReading(){ return lastreading; }
    public String getPresReading(){ return presreading; }
    public String getSectorCode(){ return sectorcode; }
    public String getClassificationId(){ return classificationid; }
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
    public String getStuboutId(){ return stuboutid; }
    public int getSortOrder(){ return sortorder; }
    
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
