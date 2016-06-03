package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.models.*;
import com.rameses.util.*;

public class MarketCashReceipt extends PaymentOrderCashReceiptModel {
    
     @Service("MarketCashReceiptService")
     def cashReceiptSvc;
    
     def payOption = [type:'FULL']; 
    
     String title = "Market";
     
     void init() {
        super.init();
        boolean pass = false;
        def params = [:];
        params.onselect = { o->
            loadInfo( o );
            pass = true;
        }
        Modal.show( Inv.lookupOpener( "cashreceipt:market:lookup", params ) );
        if(!pass) throw new BreakException();
    }
    
    def loadBarcode() { 
        def info = cashReceiptSvc.getBilling([ refno: barcodeid ]);
        entity = [formtype: "serial", formno:"51", txnmode: 'ONLINE'];
        entity.collectiontype = info.collectiontype;
        entity = service.init( entity );
        super.loadInfo( info );
        return "default";
    }      

}