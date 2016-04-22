package com.rameses.waterworks.bean;

import javafx.beans.property.SimpleBooleanProperty;

public class Area {
    
    private String objid, title, assigneeid, sectorid;
    public SimpleBooleanProperty selected;
    
    public Area(String objid, String title, String assigneeid, String sectorid, boolean selected){
        this.objid = objid;
        this.title = title;
        this.assigneeid = assigneeid;
        this.sectorid = sectorid;
        this.selected = new SimpleBooleanProperty(selected);
    }
    
    public String getObjid(){ return objid; }
    public String getTitle(){ return title; }
    public String getAssigneeId(){ return assigneeid; }
    public String getSectorId(){ return sectorid; };
    public boolean isSelected(){ return selected.get(); }
    
    @Override
    public String toString(){
        return getTitle();
    }
    
}
