package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.models.*;
import com.rameses.util.*;

public class MarketCashReceiptModel extends com.rameses.enterprise.treasury.cashreceipt.AbstractCashReceipt {
    
     @Service("MarketCashReceiptService")
     def cashReceiptSvc;
    
     def barcodeid;
     String entityName = "misc_cashreceipt"
     
     String title = "Market Rental";
     def payOption = [type: "Full"];  
     def selectedItem;
    
     def loadData(acctno) {
        def info = cashReceiptSvc.getInfo([ acctno: acctno ]);
        entity.putAll( info );
        reload();
    }
    
    def loadBarcode() { 
        loadData( barcodeid );
        super.init(); 
        return "default";
    }   
    
    def listModel = [
        fetchList: { o->
            return entity.items;
        }
    ] as BasicListModel;
    
    def applyPayment() {
        def h = { o->
            payOption = o;
            reload();
        };    
        return Inv.lookupOpener("billing:payoption", [handler:h]); 
    }
    
    void reload() {
        def m = [acctid: entity.acctid, payOption: payOption];
        def bill =  cashReceiptSvc.getBilling( m );
        entity.putAll(bill);
        updateBalances();
        if( binding !=null ) {
            listModel.reload();
            binding.refresh();
        }
        
    }
    

}

