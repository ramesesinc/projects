package com.rameses.waterworks.bean;

import javafx.beans.property.SimpleBooleanProperty;

public class Sector {
    
    private String objid, code, assigneeid;
    public SimpleBooleanProperty selected;
    
    public Sector(String objid, String code, boolean selected, String assigneeid){
        this.objid = objid;
        this.code = code;
        this.selected = new SimpleBooleanProperty(selected);
        this.assigneeid = assigneeid;
    }
    
    public String getObjid(){ return objid; }
    public String getCode(){ return code; }
    public boolean isSelected(){ return selected.get(); }
    public String getAssigneeId(){ return assigneeid; }
    
    @Override
    public String toString(){
        return getCode();
    }
    
}
