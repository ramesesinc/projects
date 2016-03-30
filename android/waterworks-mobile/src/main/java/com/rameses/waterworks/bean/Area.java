package com.rameses.waterworks.bean;

import javafx.beans.property.SimpleBooleanProperty;

/**
 *
 * @author Rameses
 */
public class Area {
    
    private String objid, title, assigneeid, zone, sector;
    public SimpleBooleanProperty selected;
    
    public Area(String objid, String title, String assigneeid, String zone, String sector, boolean selected){
        this.objid = objid;
        this.title = title;
        this.assigneeid = assigneeid;
        this.zone = zone;
        this.sector = sector;
        this.selected = new SimpleBooleanProperty(selected);
    }
    
    public String getObjid(){ return objid; }
    public String getTitle(){ return title; }
    public String getAssigneeId(){ return assigneeid; }
    public String getZone(){ return zone; }
    public String getSector(){ return sector; };
    public boolean isSelected(){ return selected.get(); }
    
    @Override
    public String toString(){
        return getTitle();
    }
    
}
