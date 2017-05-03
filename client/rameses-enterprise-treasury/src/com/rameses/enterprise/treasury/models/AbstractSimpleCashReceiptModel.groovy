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
    def txnid;
    
    def _payOptions;
    
    public abstract def getPaymentInfo(def app);
    public abstract String getContextName();
    
    public String getDetails() {
        return "Details";
    }

    public List getPayOptions() {
        if(_payOptions==null){
            def list;
            try {
                list = Inv.lookupOpeners("simple_cashreceipt_payoption_type", [context: this])
            }catch(e){;}
            try {
                def mlist = Inv.lookupOpeners(getContextName()+":cashreceipt_payoption_type", [context: this]);
                if(mlist) list += mlist;
            }catch(e){;}
            _payOptions = list;
        } 
        return _payOptions;
    }
    
    void afterLoadInfo() {;}
    
    void loadInfo(def p) {
        def info = getPaymentInfo( p );
        entity.putAll(info);
        reloadItems(); 
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
        return NumberUtil.round( entity.items.sum{ it.amount } );  
    }   
    
    def showPayOption() {
        if( getPayOptions()==null) return null;
        def m = [:];
        m.payOptions = getPayOptions();
        m.onselect = { o->
            loadInfo( [id: txnid, payment: o, action:'payoption'] );
        }
        return Inv.lookupOpener( "simple_cashreceipt_payoption", m);
    }
    
    def resetPayOption() {
        loadInfo( [id: txnid] );
    }
    
    public void validateBeforePost() {
        if( entity.balancedue !=  0 )
            throw new Exception("Amount must be equal to amount paid");
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