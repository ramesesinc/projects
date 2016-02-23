/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.waterworks.bean;

import javafx.beans.property.SimpleBooleanProperty;

/**
 *
 * @author Rameses
 */
public class ReadingGroup {
    
    private String objid, title, assigneeid, assigneename, duedate;
    public SimpleBooleanProperty selected;
    
    public ReadingGroup(String objid, String title, String assigneeid, String assigneename, String duedate, boolean selected){
        this.objid = objid;
        this.title = title;
        this.assigneeid = assigneeid;
        this.assigneename = assigneename;
        this.duedate = duedate;
        this.selected = new SimpleBooleanProperty(selected);
    }
    
    public String getObjid(){ return objid; }
    public String getTitle(){ return title; }
    public String getAssigneeId(){ return assigneeid; }
    public String getAssigneeName(){ return assigneename; }
    public String getDueDate(){ return duedate; }
    public boolean isSelected(){ return selected.get(); }
    
    @Override
    public String toString(){
        return getTitle();
    }
    
}
