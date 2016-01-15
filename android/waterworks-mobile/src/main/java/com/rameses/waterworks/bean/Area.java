package com.rameses.waterworks.bean;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Dino
 */
public class Area {
    
    private SimpleStringProperty objid, name, description;
    public SimpleBooleanProperty selected;
    
    public Area(String objid, String name, String description, boolean selected){
        this.objid = new SimpleStringProperty(objid);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.selected = new SimpleBooleanProperty(selected);
    }
    
    public String getObjid(){ return objid.get(); }
    public String getName(){ return name.get(); }
    public String getDescription(){ return description.get(); }
    public boolean isSelected(){ return selected.get(); }
    
    @Override
    public String toString(){
        return getName();
    }
    
}
