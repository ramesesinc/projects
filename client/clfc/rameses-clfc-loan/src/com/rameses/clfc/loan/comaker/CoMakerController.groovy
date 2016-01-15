package com.rameses.clfc.loan.comaker;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.borrower.BorrowerContext;

class CoMakerController 
{
    def loanapp, service, beforeSaveHandlers, callBackHandler, caller, dataChangeHandlers;
    
    def helper = MapObject.helper;
    def source;
    
    void create() {
        loanapp.borrower = [ 
            _isnew    : true, 
            residency : [:], 
            occupancy : [:] 
        ];
    }
    
    void open() {
        source = loanapp.borrower;
        loanapp.borrower = helper.clone(source); 

        def borrower = loanapp.borrower;
        if (borrower.spouse == null) borrower.spouse = [:];
        if (borrower.residency == null) borrower.residency = [:];
        if (borrower.occupancy == null) borrower.occupancy = [:];
        
        def spouse = borrower.spouse;
        if (spouse.residency == null) spouse.residency = [:];
        if (spouse.occupancy == null) spouse.occupancy = [:];
        
        def filetype = borrower._filetype;
        def relation = borrower.relation;
        try { 
            def data = service.openBorrower([objid: source.objid]); 
            if (data.residency == null) data.residency = [:];
            if (data.occupancy == null) data.occupancy = [:];
            if (data.spouse == null) data.spouse = [:];
            if (data.spouse.residency == null) data.spouse.residency = [:];
            if (data.spouse.occupancy == null) data.spouse.occupancy = [:];
            
            borrower.clear();
            borrower.putAll(data); 
        } catch(Throwable t) {
            println '[WARN] JointBorrowerController.open: error caused by '+t.message; 
        } 

        borrower._filetype = filetype; 
        borrower.relation = relation; 
        borrower.type = 'COMAKER';  
    }    
    
    def createOpenerParams() {
        def ctx = new BorrowerContext(caller, this, service, loanapp);
        ctx.beforeSaveHandlers = beforeSaveHandlers;
        ctx.dataChangeHandlers = dataChangeHandlers;
        return [borrowerContext: ctx];
    }
    
    def tabHandler = [
        getOpeners: {
            return InvokerUtil.lookupOpeners('loanapp-comaker:plugin', createOpenerParams());
        },
        getOpenerParams: {
            return createOpenerParams(); 
        },
        beforeSelect: {item,index-> 
            if (caller?.mode == 'read' || index == 0) return true; 
        
            return (loanapp.borrower?.objid != null); 
        }
    ] as TabbedPaneModel 
            
    def doOk() {
        validate();
        beforeSaveHandlers.each{k,v-> 
            if (v != null) v(); 
        }
        
        if (callBackHandler) {
            callBackHandler(loanapp.borrower);
        } else {
            source.putAll(loanapp.borrower);
            loanapp.borrower = source;
        }
        return "_close";
    }
    
    def doCancel() {
        if (caller.mode == 'edit') {
            if (!MsgBox.confirm("Changes will be discarded. Continue?")) return null;
            
            loanapp.borrower = source;
        }
        return "_close"
    }
    
    void validate() {
        def spouse = loanapp.borrower.spouse;
        if (spouse?.objid != null) {
            if (!spouse.residency?.type) 
                throw new Exception('Spouse Residency: Type is required.');
            if (!spouse.residency?.since) 
                throw new Exception('Spouse Residency: Since is required.');
            if (spouse.residency?.type == 'RENTED') {
                if (!spouse.residency?.renttype) 
                    throw new Exception('Spouse Residency: Rent Type is required.');
                if (!spouse.residency?.rentamount) 
                    throw new Exception('Spouse Residency: Rent Amount is required.');
            }
            if (!spouse.occupancy?.type) 
                throw new Exception('Spouse Lot Occupancy: Type is required.');            
            if (!spouse.occupancy?.since) 
                throw new Exception('Spouse Lot Occupancy: Since is required.');
            if (spouse.occupancy?.type == 'RENTED') {
                if (!spouse.occupancy?.renttype) 
                    throw new Exception('Spouse Lot Occupancy: Rent Type is required.');
                if (!spouse.occupancy?.rentamount) 
                    throw new Exception('Spouse Lot Occupancy: Rent Amount is required.');
            }
        }
    }
}
