package com.rameses.gov.treasury.controllers;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
import java.rmi.server.*;

public abstract class RISController extends CRUDController {

    boolean showConfirmOnSave = true;
    String confirmSaveMsg = "You are about to save this entry. Please confirm.";
    String entityName = "ris";

    Map createEntity() {
        def entity = [:];
        entity.items = [];
        entity.objid = "RIS"+new UID();
        entity.txntype = getTxnType();    
        return entity;
    }

    void beforeSave( o ) {
        if( !o.items)
            throw new Exception("Please add at least one item");
    }

    def selectedItem;
    public abstract def getLookupItems();
    public abstract String getTxnType();

    def itemModel = [
        onAddItem: { o->
            entity.items << o;
        },
        onRemoveItem: {
            entity.items.remove(o);
            return true;
        },
        fetchList: { o->
            return entity.items;
        }
    ] as EditorListModel;


}