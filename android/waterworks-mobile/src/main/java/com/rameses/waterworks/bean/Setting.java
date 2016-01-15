package com.rameses.waterworks.bean;

public class Setting {
    
    private String name, value;
    
    public Setting(
            String name,
            String value
    ){
        this.name = name;
        this.value = value;
    }
    
    public String getName(){ return name; }
    
    public void setName(String s){ name = s; }
    
    public String getValue(){ return value; }
    
    public void setValue(String s){ value = s; }
    
}
