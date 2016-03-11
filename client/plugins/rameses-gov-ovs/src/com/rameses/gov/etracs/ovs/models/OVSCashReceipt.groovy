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
        entity.amount = 0;
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
        },
        afterColumnUpdate : {o,colname ->
            reloadListModel(status.index);
        }
    ] as EditorListModel;

    void reloadListModel(def index){
        entity.items = [];
        (0..(entity.billitems.size()-1)).each {
             def item = entity.billitems[it];
             if(it <= index){
                item.checked = true;
                item.amount = item.balance;
                entity.items << [item:item.item, amount: item.amount, remarks: item.remarks];
             }else{
                item.checked = false;
                item.amount = 0;
             }
             entity.amount = entity.items.sum{it.amount};
        }
        updateBalances();
    }
   
}