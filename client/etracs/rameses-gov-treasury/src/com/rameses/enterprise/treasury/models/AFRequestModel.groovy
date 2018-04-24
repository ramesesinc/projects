package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class AFRequestModel extends CrudFormModel {

    @Invoker
    def invoker;
    
    def selectedItem;
    
    String printFormName = "afris";
    
    def getPrintFormData() {
        def m = [:];
        m.putAll( entity );
        m.controlno = entity.reqno;
        return m;
    }
    
    void afterCreate() {
        entity.state = 'DRAFT';
        entity.reqtype = invoker.properties.reqtype;
        entity.itemclass = 'AF';
        entity.items = [];
    }
    
    public boolean isEditAllowed() { 
        if( entity.state == 'CLOSED') return false;
        return super.isEditAllowed();
    }
    
    public void afterColumnUpdate(String name, def o, String colName ) {
        if( colName == "item" )  {
            o.item.objid = o.item.itemid
            o.unit = o.item.unit;
        }        
    }
}    