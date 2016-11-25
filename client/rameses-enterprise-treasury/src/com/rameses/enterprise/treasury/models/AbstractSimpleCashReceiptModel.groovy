package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.cashreceipt.*
import com.rameses.util.*;
        
public abstract class AbstractSimpleCashReceiptModel extends AbstractCashReceipt {
    
    String entityName = "misc_cashreceipt"
    def prefix;
    def barcodeid;

    def status;   
    def payOption;
    def selectedItem;

    public abstract def loadPaymentInfo(def app);
    
    public String getPayOptionOpener() {
        return null;
    }
    
    public String getDetails() {
        return "Details";
    }

    void init() {
        def o = MsgBox.prompt("Enter Transaction No");
        if(!o) throw new BreakException();
        def info = loadPaymentInfo( [id: o] );
        entity.putAll(info);
        reloadItems(); 
    }
    
    void loadBarcode() {
        def info = loadPaymentInfo( [id: barcodeid] );
        entity.putAll( info );
        reloadItems(); 
    }   
    
    def getTotalAmount() {
        return NumberUtil.round( entity.items.sum{ it.amount } );  
    }   
    
    def showPayOption() {
        if(getPayOptionOpener()==null) return null;
        def h = { o->
            payOption = o;
            reloadItems();
        }
        return Inv.lookupOpener( getPayOptionOpener(), [handler: h]);
    }
    
    void reloadItems() {
        if(!entity.items) throw new Exception("There must be at least 1 item to pay");
        entity.amount = NumberUtil.round( entity.items.sum{ it.amount } );  
        updateBalances();
        itemListModel.reload();
    }
    
    def itemListModel = [
        fetchList: { o->
            return entity.items;
        }
    ] as BasicListModel;
}       