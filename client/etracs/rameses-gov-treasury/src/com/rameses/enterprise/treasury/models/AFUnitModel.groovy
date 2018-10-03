package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class AFUnitModel extends CrudFormModel  {

    def receiptPrintoutList;
    def detailPrintoutList;
    
    def loadHandlers(def typename) {
        try {
            return Inv.lookupOpener( typename ).collect {
                [name:it.properties.name, caption:it.caption]
            };
        }
        catch(e) {
            return [];
        }
    }
    
    void afterInit() {
        receiptPrintoutList = loadHandlers("cashreceipt:printout");
        detailPrintoutList = loadHandlers("cashreceiptdetail:printout");        
    }
    
    void afterCreate() {
        entity.interval = 1;
    }
    
    void beforeSave(def m) {
        if( m == "create") {
            entity.itemid = entity.af.objid;
            entity.objid = entity.af.objid + "-" + entity.unit;
        }
    }
    

}    