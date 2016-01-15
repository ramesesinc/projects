package com.rameses.waterworks.bean;

public class Meter {
    
    private String objid, appid, serialno, brand, size, capacity;
    private int prevreading, currentreading, consumption;
    
    public Meter(
        String objid,
        String appid,
        String serialno,
        String brand,
        String size,
        String capacity,
        int prevreading,
        int currentreading,
        int consumption
    ){
        this.objid = objid;
        this.appid = appid;
        this.serialno = serialno;
        this.brand = brand;
        this.size = size;
        this.capacity = capacity;
        this.prevreading = prevreading;
        this.currentreading = currentreading;
        this.consumption = consumption;
    }
    
    public String getObjid(){ return objid; }
    public String getAppid(){ return appid; }
    public String getSerialno(){ return serialno; }
    public String getBrand(){ return brand; }
    public String getSize(){ return objid; }
    public String getCapacity(){ return capacity; }
    public int getPrevreading(){ return prevreading; }
    public int getCurrentreading(){ return currentreading; }
    public int getConsumption(){ return consumption; }
    
    public void setObjid(String s){ objid = s; }
    public void setAppid(String s){ appid = s; }
    public void setSerialno(String s){ serialno = s; }
    public void setBrand(String s){ brand = s; }
    public void setSize(String s){ size = s; }
    public void setCapacity(String s){ capacity = s; }
    public void setPrevreading(int i){ prevreading = i; }
    public void setCurrentreading(int i){ currentreading = i; }
    public void setConsumption(int i){ consumption = i; }
    
}
