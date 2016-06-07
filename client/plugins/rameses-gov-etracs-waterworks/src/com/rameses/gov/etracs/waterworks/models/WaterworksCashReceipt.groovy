package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.models.*;
import com.rameses.util.*;

public class WaterworksCashReceipt extends BillingCashReceiptModel {
    
     @Service("WaterworksCashReceiptService")
     def cashReceiptSvc;
    
     String title = "Waterworks";
     
     public void init() {
        super.init();
        boolean pass = false;
        def params = [:];
        params.onselect = { o->
            loadInfo( o );
            pass = true;
        }
        Modal.show( Inv.lookupOpener( "cashreceipt:waterworks:lookup", params ) );
        if(!pass) throw new BreakException();
    }
    
    def loadBarcode() { 
        def info = cashReceiptSvc.getBilling([ refno: barcodeid ]);
        loadInfo( info );
        return "default";
    }      

}