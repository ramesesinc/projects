package com.rameses.waterworks.bean;

import javafx.beans.property.SimpleBooleanProperty;

/**
 *
 * @author Rameses
 */
public class ReadingGroup {
    
    private String objid, title, assigneeid, duedate;
    public SimpleBooleanProperty selected;
    private Object stubout;
    
    public ReadingGroup(String objid, String title, String assigneeid, String duedate, boolean selected, Object stubout){
        this.objid = objid;
        this.title = title;
        this.assigneeid = assigneeid;
        this.duedate = duedate;
        this.selected = new SimpleBooleanProperty(selected);
        this.stubout = stubout;
    }
    
    public String getObjid(){ return objid; }
    public String getTitle(){ return title; }
    public String getAssigneeId(){ return assigneeid; }
    public String getDueDate(){ return duedate; }
    public boolean isSelected(){ return selected.get(); }
    public Object getStubout(){ return stubout; }
    
    @Override
    public String toString(){
        return getTitle();
    }
    
}
