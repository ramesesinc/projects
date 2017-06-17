package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.cashreceipt.*
import com.rameses.util.*;
import com.rameses.common.*;
        
public abstract class AbstractSelectionCashReceiptModel extends AbstractCashReceipt {
    
    String entityName = "misc_cashreceipt";
    def barcodeid;

    def txnid;
    def status;
    
    public abstract def getPaymentInfo(def app);
    
    public String getDetails() {
        return "Details";
    }

    void afterLoadInfo() {;}
    
    void loadInfo(def p) {
        def info = getPaymentInfo( p );
        entity.putAll(info);
        entity.items.each {
            it.balance = it.amount;
            it.checked = true;
        }
        entity.billitems = [];
        entity.billitems.addAll( entity.items );
        entity.amount = entity.items.sum{it.amount};
        itemListModel.reload(); 
        afterLoadInfo();
    }
    
    void init() {
        def o = MsgBox.prompt("Enter Transaction No");
        if(!o) throw new BreakException();
        txnid = o;
        loadInfo([id:txnid, action:'open']);
    }
    
    void loadBarcode() {
        txnid = barcodeid;
        loadInfo( [id: txnid, action:'barcode'] );
    }   
    
    def getTotalAmount() {
        if(!entity.items) return 0;
        return NumberUtil.round( entity.items.sum{ it.amount } );  
    }   

    def itemListModel = [
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
                    entity.items << o;
                }
                else {
                    o.amount = 0;
                }
             }
             else{
                o.checked = false;
                o.amount = 0.0;
             }
        }
        if( !entity.items) 
            entity.amount = 0;
        else    
            entity.amount = entity.items.sum{it.amount};
        updateBalances();
    }
        
    
}       