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
     def payOption = "Full";  
    
     /*
     public void init() {
        boolean pass = false;
        def params = [:];
        params.onselect = { o->
            super.init();
            loadData( o.acctno );
            pass = true;
        }
        Modal.show( Inv.lookupOpener( "cashreceipt:market:lookup", params ) );
        if(!pass) throw new BreakException();
    }  
    */
   
    public def getLookupPayer() {
        MsgBox.alert('lookup Payer!');
    }
    
    def billingListModel = [
        fetchList: { o->
            return entity.billitems;
        }
    ] as BasicListModel;
    
    def loadData(acctno) {
        def info = cashReceiptSvc.getBilling([ refno: acctno ]);
        entity.putAll( info );
    }
    
    def loadBarcode() { 
        //loadData( barcodeid );
        super.init(); 
        return "default";
    }   

    def showPayOption() {
        def h = { o->
            def op = [refno: entity.acctno, option:o.payOption];  
            payOption = 'Full';
            if(o.date) {
                op.date = o.date;
                payOption = 'Date until ' + op.date;
            }
            else if(o.numdays) {
                op.numdays = o.numdays;
                payOption = 'No. of days ' + o.numdays;
            }
            else if(o.amount) {
                op.amount = o.amount;
                payOption = "";
            }
            def info = cashReceiptSvc.getBilling(op);
            entity.billitems = info.billitems;
            entity.items = info.items;
            entity.amount = info.amount;
            super.updateBalances();
            itemListModel.reload();
        };    
        return Inv.lookupOpener("cashreceipt:market:payoption", [handler:h]); 
    }
    
}

