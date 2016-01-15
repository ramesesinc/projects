package com.rameses.clfc.loan.controller;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.util.*;

class LoanAppDetailController 
{
    //feed by the caller
    def loanapp, caller, selectedMenu;
    
    @Service('LoanAppService') 
    def service;

    def clientTypes = LoanUtil.clientTypes; 
    def productTypes;   
    def routeLookupHandler = InvokerUtil.lookupOpener("route:lookup", [:]);    
    def borrowerLookupHandler
    
    def data = [:];

    void init() {
        selectedMenu.saveHandler = { save(); }
        selectedMenu.dataChangeHandler = { dataChange(); }
        data = service.open([objid: loanapp.objid]);
        loanapp.clear();
        loanapp.putAll(data);
        productTypes = service.getProductTypes(); 
        data.producttype = productTypes.find{ it.name == data.producttype?.name } 
        if (data.producttype == null) data.producttype = [:];
        def handler = {o->            
            if (o.objid == data.borrower.objid) 
                throw new Exception('Cannot select prinpical borrower as next to.');
            data.nextto = o;
        }
        
        borrowerLookupHandler = InvokerUtil.lookupOpener("loanappborrower:lookup", [
            'query.routecode': data.route.code, 
            'query.borrowerid': data.borrower.objid, 
            onselect: handler
        ]);
    }

    void dataChange() {
        data = loanapp;
    }
    
    void save() {
        if(data.clienttype == 'MARKETED' && !data.marketedby) 
            throw new Exception('Interviewed by is required.');
        
        if(loanapp.state == 'FOR_INSPECTION' && !data.route?.code) 
            throw new Exception("Route is required.")
        
        service.update(data);
    }   
}
