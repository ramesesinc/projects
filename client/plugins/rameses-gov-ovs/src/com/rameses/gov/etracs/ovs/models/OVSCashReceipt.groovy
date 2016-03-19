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
     def selectedItem;
     def status;
     
     def payerChanged(def o){
        entity.billitems = cashreceiptSvc.getUnpaidViolations([objid:o.objid]);
        entity.items = [];
        entity.amount = 0;
        itemListHandler.reload();
        updateBalances();
        return null;
     }
    
    def itemListHandler = [
        fetchList : {o ->
            return entity.billitems;
        },
        afterColumnUpdate : {o,colname ->
            reloadListModel(status.index);
        }
    ] as EditorListModel;

    void reloadListModel(def index){
        entity.items.clear();
        entity.billitems.eachWithIndex { o, idx->
             if(idx <= index){
                if( idx < index ) {
                    o.checked = true;
                }
                if(o.checked ) {
                    o.amount = o.balance;
                    entity.items << [item:o.item, amount: o.amount, remarks: o.remarks];
                }
                else {
                    o.amount = 0;
                }
             }
             else{
                o.checked = false;
                o.amount = 0;
             }
        }
        entity.amount = entity.items.sum{it.amount};
        updateBalances();
    }
   
}