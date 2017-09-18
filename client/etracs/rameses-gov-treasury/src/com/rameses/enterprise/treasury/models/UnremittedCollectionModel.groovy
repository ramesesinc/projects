package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;

class UnremittedCollectionModel extends CrudListModel {

    @Script("User")
    def user;
    
    def df = new java.text.DecimalFormat("#,##0.00")
    
    def getCustomFilter() {
        return [ "collector.objid=:uid AND remittanceid IS NULL", [ uid: user.userid ] ];
    }
    
    def viewSum() {
        MsgBox.alert('view sum');
    }
    

    void fix() {
        if ( MsgBox.confirm('You are about to fix your accountable forms.\nDo you want to continue?') ) {
            svc.fixInventory([:]);  
            MsgBox.alert('Successfully processed.'); 
        } 
    } 

    def remit() { 
        boolean pass = false; 
        def remdate = null; 
        def datehandler = { o-> 
            remdate = o; 
            pass = true; 
        }
        Modal.show("date:prompt", [ handler: datehandler, title: "Enter the Remittance Date" ]);
        if ( !pass ) return null; 
        
        return Inv.lookupOpener( "remittance:create", [ remittancedate: remdate ]);
    }
}
 