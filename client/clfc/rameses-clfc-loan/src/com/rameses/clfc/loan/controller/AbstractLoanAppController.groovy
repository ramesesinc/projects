package com.rameses.clfc.loan.controller;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.util.*;

public abstract class AbstractLoanAppController
{
    @Binding
    def binding;
    
    def mode = 'create';
    def entity;
    def productTypes = [];
    def clientTypes = LoanUtil.clientTypes;
    def appTypes = LoanUtil.appTypes;
    
    def getLoanTypes() {
        return getService().getLoanTypes();
    }
    
    protected abstract def getService();
    
    @PropertyChangeListener
    def listener = [
        "entity.clienttype": {o->
            if(o != 'MARKETED') entity.marketedby = null
            binding.refresh('entity.marketedby');
        }
    ]

    def routeLookupHandler = InvokerUtil.lookupOpener('route:lookup', [:]);
    /*
    def customerLookupHandler = InvokerUtil.lookupOpener('customer:search', [
        onselect: {o-> 
            service.checkBorrowerForExistingLoan([borrowerid: o.objid]); 
            entity.borrower = o; 
            entity.borrower.address = o.address?.text; 
        }, 
        onempty: { 
            entity.borrower = [:]; 
        }
    ]);
    */
   
    def getCustomerLookupHandler() {
        def onselect = { o-> 
            service.checkBorrowerForExistingLoan([borrowerid: o.objid]); 
            entity.borrower = o; 
            entity.borrower.address = o.address?.text;             
        }
        def onempty = {
            entity.borrower = [:];
        }
        return Inv.lookupOpener('customer:search', [onselect: onselect, onempty: onempty]);
    }
    
    def save() {
        if (entity.clienttype == 'MARKETED') {
            if(!entity.marketedby) throw new Exception('Interviewed by is required.');
        }
        
        if (!MsgBox.confirm('Ensure that all information is correct. Continue?')) return;
        entity = service.create(entity); 
        mode = 'read';
        return 'successpage'; 
    }
        
    def cancel() {
        if (MsgBox.confirm('You are about to close this window. Continue?')) return '_close';
        return null; 
    }
    
    def close() {
        return '_close';
    }
    
    void init() {}
    
    def create() {
        mode = 'create';
        init();
        return 'default';
    } 
    
    def edit() {
        def opener = InvokerUtil.lookupOpener('loanapp:open', [entity: entity]);
        opener.caption = 'LOAN-'+entity.appno;
        opener.target = 'window';
        binding.fireNavigation(opener); 
        return '_close'; 
    }
}
