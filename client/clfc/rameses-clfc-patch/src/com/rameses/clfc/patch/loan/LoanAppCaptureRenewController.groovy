package com.rameses.clfc.patch.loan;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.util.*;
import java.rmi.server.UID;

class LoanAppCaptureRenewController
{
    @Caller
    def caller;
    
    @Service('LoanAppCaptureRenewService')
    def service;

    def entity = [:];
    def handler;
    
    def productTypes, loanTypes;
    def clientTypes = LoanUtil.clientTypes;     
    def routeLookupHandler = Inv.lookupOpener('route:lookup', [:]);
    
    void init() {
        entity = service.open([objid: entity.objid]);
        productTypes = service.getProductTypes();
        loanTypes = service.getLoanTypes();
        
        entity.loanamount = null;
        entity.purpose = null; 
        entity.appno = null; 
    }
    
    def getLoanTyp
    
    def doCancel() {
        return '_close'; 
    } 
    
    def doSave() {
        entity = service.renew(entity);
        MsgBox.alert("Application has been successfully submitted");
        def callback = {
            if (handler) handler();
            
            def opener = Inv.lookupOpener('loanapp-capture:open', [entity: entity]);
            Inv.invoke(opener);
        }
        EventQueue.invokeLater(callback); 
        return '_close'; 
    }
} 