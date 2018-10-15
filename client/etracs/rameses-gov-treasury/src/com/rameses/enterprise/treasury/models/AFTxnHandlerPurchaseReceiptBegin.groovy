package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;
import com.rameses.rcp.framework.ValidatorException;


class AFTxnHandlerPurchaseReceiptBegin extends AFTxnHandler {

    def getTxnType() {
        return ((entity.txntype == "BEGIN_BALANCE") ? "BEGIN" : "PURCHASE");
    }
    
    def init() {
        if( mode =="create" ) {
            entity.items.each {
                it.txntype = txnType;
            } 
        }
        return super.init();
    }
    
    def itemListHandler = [
        fetchList : { o->
            return entity.items;
        },
        createItem: {
           return [txntype: txnType]; 
        }, 
        onAddItem: { o-> 
            entity.items << o;
        },
        onRemoveItem: { o->
            entity.items.remove(o);
        },
        onColumnUpdate: { o,colName->
            if ( colName == "item" ) {
                o.item.objid = o.item.itemid;
                o.unit = o.item.unit; 
                o.cost = o.item.saleprice; 
                computeLineTotal( o ); 
            }
            else if(colName=="qty") {
                if ( afrequest && o.qty > o.qtyrequested ) 
                    throw new Exception("Qty must be less than qty requested");
                computeLineTotal( o ); 
            } 
            else if(colName=="cost") {
                computeLineTotal( o ); 
            } 
        }
    ] as EditorListModel;
    
    private void computeLineTotal( o ) {
        o.linetotal = (o.qty ? o.qty : 0) * (o.cost ? o.cost : 0.0);
    }
    
    public def getInfo() {
        return TemplateProvider.instance.getResult( "com/rameses/enterprise/treasury/views/AFTxnViewPurchaseReceiptBegin.gtpl", [entity:entity] );
    }
    
    def addBatch( def o ) {
        def item = entity.items.find{ it.objid == o.aftxnitemid };
        return Inv.lookupOpener( "af_control:addbatch", [ 
            refitem:item, handler:{ vv-> reloadEntity(); } 
        ]);
    }
    
    void removeBatch(def o) {
        if( !MsgBox.confirm('You are about to remove the entered accountable forms. Proceed?') ) return;
        o.refid = entity.objid;
        o.txntype = entity.txntype;
        try {
            svc.removeBatch(o);
            reloadEntity();
        }
        catch(e) {
            MsgBox.err(e);
        }
    }

}    