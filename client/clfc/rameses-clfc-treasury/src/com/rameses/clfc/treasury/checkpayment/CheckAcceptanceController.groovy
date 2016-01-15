package com.rameses.clfc.treasury.checkpayment;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*

class CheckAcceptanceController 
{
    @Caller
    def caller;

    def data = [:];
    def entity; 
    def handler;
    
    void init() {
    }
    
    def doCancel() {
        return '_close'; 
    }
    
    def doOk() {
        if (handler) handler(data); 
        return '_close'; 
    }
    
    def passbookLookup = Inv.lookupOpener('passbook:lookup', [:]); 
    
    def getPassbookInfo() {
        if (!data?.passbook) return ''; 
        
        return data.passbook.bank?.objid + ' ' + data.passbook.passbookno; 
    } 
}
