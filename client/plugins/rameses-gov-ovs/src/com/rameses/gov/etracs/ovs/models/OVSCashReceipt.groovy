package com.rameses.gov.etracs.ovs.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.cashreceipt.*;
import com.rameses.util.*;

public class OVSCashReceipt extends BasicCashReceipt {
    
     @Service("OVSCashReceiptService")
     def cashreceiptSvc;
    
     //we specify this so print detail will appear.
     String entityName = "misc_cashreceipt";
     String title = "Ordinance Violation";
     
     def payerChanged(def o){
        def m = cashreceiptSvc.getUnpaidViolations([objid:o.objid]);
        entity.billitems = m.billitems;
        entity.items = m.items;
        entity.amount = m.items.sum{it.amount};
        itemListHandler.reload();
        updateBalances();
        return null;
     }
    
   
    def previewReceipt() {
        return Inv.lookupOpener( "cashreceipt:preview", [entity: entity] );
    }
    
    def itemListHandler = [
        fetchList : {o ->
            return entity.billitems;
        }
    ] as BasicListModel;
   
}