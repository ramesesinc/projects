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
    
    void afterCreate() {
        entity.state = 'DRAFT';
        entity.itemclass = 'AF';
        entity.items = [];
        entity.reqtype = invoker.properties.reqtype;
    }
    
    public boolean isEditAllowed() { 
        if( entity.state == 'CLOSED') return false;
        return super.isEditAllowed();
    }
    
    public def getUnitList() {
        return selectedItem.item.units;
    }
    
}    