package com.rameses.clfc.loan.controller;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class LoanCollateralController
{
    //feed by the caller
    def loanapp, caller, selectedMenu;
    
    @Service('LoanAppCollateralService') 
    def service;    
    
    def beforeSaveHandlers = [:];
    def collateral = [];
    
    void init() {
        selectedMenu.saveHandler = { save(); }  
        
        def data = service.open([objid: loanapp.objid]); 
        loanapp.clear();
        loanapp.putAll(data);
        collateral = loanapp.collateral;
    }

    def createOpenerParams() {
        return [
            beforeSaveHandlers: beforeSaveHandlers, 
            service: service, 
            loanappid: loanapp.objid,
            collateral: collateral,
            mode: caller.mode 
        ]; 
    }
    
    def tabHandler = [
        getOpeners: {
            return InvokerUtil.lookupOpeners('loanapp-collateral:plugin', createOpenerParams());
        },
        getOpenerParams: {
            return createOpenerParams(); 
        }
    ] as TabbedPaneModel 
    
    void save() {
        beforeSaveHandlers.each{k,v-> 
            if (v != null) v(); 
        }
        
        def data = [objid: loanapp.objid, collateral: collateral];
        service.update(data);
    }
}