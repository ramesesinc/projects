package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.* 

class CashReceiptPaymentNonCashListModel extends CrudSubListModel {
        
    public def getCustomFilter() {
        return ["refid=:id", [id:masterEntity.objid] ]; 
    }

    public def open() {
        if(!selectedItem) throw new Exception("Please select an item");
        return Inv.lookupOpener("cashreceipt:open", [entity: [ objid: selectedItem.receiptid] ] );
    }
} 