package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

public class SubitemModel extends EditorListModel {
    
    def persistenceService;
    def queryService;
    
    def entity;
    def deletedItems = [];
    
    def items = [];
    def itemSchema;
    
    def itemName;

    protected void onAddItem(Object item) {
        entity[(itemName)].add( item );
    }

    protected boolean beforeColumnUpdate(Object item, String columnName, Object newValue) {
        //check if this column is unique within the set.
        if( itemSchema[(columnName)].unique == "true") {
            items.findAll{  } 
            if(colName == "item") {
                if( bomItems.findAll{it.item.objid == newValue.objid}) {
                    throw new Exception("Item already exists");
                }
            }
            throw new Exception("Item already exists");
        }
        return true;
    }

    protected boolean onRemoveItem(Object item) {
        deletedItems << item;
        items << item;
    }

    public List fetchList(Map params) {
        //check first if items !=null
        if(items==null) {
            items = [];
            def m = [:];
            m.findBy = [];
            items = queryService.getList(m);
        }
        
    }

    
}