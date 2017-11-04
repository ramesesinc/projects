package com.rameses.android.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.rameses.util.MapProxy;
import com.rameses.util.ObjectDeserializer;

public class Account {
	
	private String objid;
	private String acctno;
	private String acctname;
	private String address;
	private String serialno;
	private String sectorid;
	private String sectorcode;
	private String lastreading;
	private String classificationid;
	private String barcode;
	private String batchid;
	private String month;
	private String monthText;
	private String year;
	private String period;
	private String duedate;
	private String discodate;
	private String rundate;
	private String items;
	private String info;
	private String stuboutid;
	private String assignee_objid;
	private String assignee_name;
	private int sortorder;
	
	private String consumption;
	private String amtdue;
	private String totaldue;
	private String presreading;
	private final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	
	public Account(Map params) {
		MapProxy proxy = new MapProxy(params);

		objid = proxy.getString("objid");
		acctno = proxy.getString("acctno");
		acctname = proxy.getString("acctname");
		address = proxy.getString("address");
		if (address == null) address = "";
		
		serialno = proxy.getString("serialno");
		sectorid = proxy.getString("sectorid");
		sectorcode = proxy.getString("sectorcode");
		lastreading = proxy.getString("lastreading");
		classificationid = proxy.getString("classificationid");
		barcode = proxy.getString("barcode");
		batchid = proxy.getString("batchid");
		month = proxy.getString("month");
		monthText = "";
		if (month != null) {
			monthText = MONTHS[Integer.parseInt(month) - 1];
		}
		
		year = proxy.getString("year");
		period = proxy.getString("period");
		duedate = proxy.getString("duedate");
		discodate = proxy.getString("discodate");
		rundate = proxy.getString("rundate");
		items = proxy.getString("items");
//		Log.i("[Account]", "items--> " + items);
		info = proxy.getString("info");
		stuboutid = proxy.getString("stuboutid");
		assignee_objid = proxy.getString("assignee_objid");
		assignee_name = proxy.getString("assignee_name");
		sortorder = proxy.getInteger("sortorder");
	}

    public String getObjid(){ return objid; }
    public String getAcctNo(){ return acctno; }
    public String getAcctName(){ return acctname; }
    public String getAddress(){ return address; }
    public String getSerialNo(){ return serialno; }
    public String getSectorId(){ return sectorid; }
    public String getLastReading(){ return lastreading; }
    public String getSectorCode(){ return sectorcode; }
    public String getClassificationId(){ return classificationid; }
    public String getBarCode(){ return barcode; }
    public String getBatchId(){ return batchid; }
    public String getMonth(){ return month; }
    public String getMonthText() { return monthText; } 
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

    public String getPresReading(){ return presreading; }
    public void setPresReading(int i){
        presreading = String.valueOf(i);
    }	

    public String getPrevBalance(){
        double val = 0.00;
        List<ItemAccount> items = getItemList();
        for(ItemAccount i : items){
            val += Double.parseDouble(i.getAmount());
        }
        return String.format("%.2f", val);
    }

    public List<ItemAccount> getItemList(){
        List<ItemAccount> itemList = new ArrayList<ItemAccount>();
        try {
            List<Map> list = (List) ObjectDeserializer.getInstance().read(items);
//            Log.i("[Account]", "String Items : " + list);
//            Log.i("[Account]", "Account List : " + list.toString());
            for(Map m : list) {
//            	Log.i("[Account]", "title: " + m.get("title") + " amount: " + m.get("amount"));
            	String account = "";
            	if (m.containsKey("particulars")) {
            		account = m.get("particulars").toString();
            	}
                
                double amount = 0.00;
                if (m.containsKey("amount")) {
                	amount = Double.parseDouble(m.get("amount").toString());
                }
                itemList.add(new ItemAccount(account,amount));
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return itemList;
    }
}
