package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.cashreceipt.*;
import com.rameses.util.*;

public class VehicleCashReceipt extends BasicCashReceipt {
    
    @Service("VehicleCashReceiptService")
    def cashReceiptSvc;
    
     //we specify this so print detail will appear.
     String entityName = "misc_cashreceipt";
     String title = "Vehicle Registration";
     def barcodeid;
    
     def payOption = [type:'FULL'];   
    
     void init() {
        super.init();
        
        /*
        boolean pass = false;
        def params = [:];
        params.onselect = { o->
            loadInfo( o );
            pass = true;
        }
        Modal.show( Inv.lookupOpener( "cashreceipt:vehicle:lookup", params ) );
        if(!pass) throw new BreakException();
        */
       def o = MsgBox.input("Enter application no.");
       if(!o) throw new BreakException();
       loadInfo([appno: o]);
    }
            
    void loadBarcode() {
        loadInfo( [appno: barcodeid] );
    }   
    
    void loadInfo( def e ) {
        e.payOption = payOption; 
        def m = cashReceiptSvc.getBilling( e ); 
        entity.putAll(m);
    } 
    
    def showPayOption() {
        def h = { o ->
            payOption = o;
            loadInfo( [objid: entity.applicationid] );
            binding.refresh();
            itemListModel.reload();
        }
        return Inv.lookupOpener( "cashreceipt:payoption:vehicle", [payOption: payOption, handler:h]);
    }
    
    def previewReceipt() {
        return Inv.lookupOpener( "cashreceipt:preview", [entity: entity] );
    }
    
}