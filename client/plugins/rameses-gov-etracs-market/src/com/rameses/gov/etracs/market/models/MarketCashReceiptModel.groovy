package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.models.*;
import com.rameses.util.*;

public class MarketCashReceiptModel extends AbstractSimpleCashReceiptModel {
    
     @Service("MarketCashReceiptService")
     def cashReceiptSvc;
    
     //we specify this so print detail will appear.
     String entityName = "misc_cashreceipt";
     String title = "Market Rental";
     
     public String getContextName() {
         return "market";
     }
     
     public def getPaymentInfo( def app ) {
         return cashReceiptSvc.getInfo( app );
     }
    
     
     def changeTodate() {
        def h = { o->
            loadInfo([id:txnid, billdate: o, action:'payoption']);
            binding.refresh();
        }
        return Inv.lookupOpener("market_specify_billdate", [handler: h, fromdate: entity.fromdate, todate: entity.todate ] );
     }
     
     void payPartial() {
        def amt = MsgBox.prompt('Enter partial amount');
        if(amt) {
            def partial = new BigDecimal(amt+"");
            def td = new java.text.SimpleDateFormat("yyyy-MM-dd");
            loadInfo([id:txnid, partial: partial, billdate:  td.format(entity.todate), action:'payoption']);
        }
     }   

     void filterAccounts() {
         throw new Exception("Not yet supported");
         //loadInfo([id:txnid, action:'payoption']);
     }

    void init() {
        def selection;
        def s = { o->
            selection = o;
            return null;
        };
        Modal.show( "market_account:lookup" , [onselect:s] );
        if(!selection) throw new BreakException();
        txnid = selection.objid;
        loadInfo([id:txnid, action:'open']);
    }
    
}

