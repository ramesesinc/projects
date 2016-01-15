package com.rameses.clfc.loan.jointborrower;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.util.*;

class LoanAppJointBorrowerController 
{
    //feed by the caller
    def loanapp, caller, selectedMenu;
    
    private def beforeSaveHandlers = [:];
    private def dataChangeHandlers = [:];
    
    @Service('JointBorrowerService') 
    def service; 
    
    def borrowers = [];
    def selectedJointBorrower;
    
    void init() {
        selectedMenu.saveHandler = { save(); }  
        def data = service.open([objid: loanapp.objid]);
        loanapp.clear();
        loanapp.putAll(data);
        borrowers = loanapp.jointborrowers;
    }
        
    void save() {
        def data = [ objid: loanapp.objid, borrowers: borrowers ]
        service.update(data); 
        borrowers.each { 
            it.remove('_isnew'); 
        }
    }

    def addJointBorrower() {
        def params = createOpenerParams()
        params.callBackHandler = {joint->
            joint._isnew = true;
            borrowers.add(joint);
            jointBorrowerHandler.reload();
        };
        return InvokerUtil.lookupOpener("jointborrower:create", params)
    }
    
    def createOpenerParams() {
        return [
            loanapp: [:],
            caller: caller, 
            service: service, 
            beforeSaveHandlers: beforeSaveHandlers,
            dataChangeHandlers: dataChangeHandlers
        ]
    }
    
    def jointBorrowerHandler = [
        fetchList: {o->
            if( !borrowers ) borrowers = []
            borrowers.each{ it._filetype = "jointborrower" }
            return borrowers;
        },
        onRemoveItem: {o->
            return removeJointBorrowerImpl(o);
        },
        getOpenerParams: {o->
            def params = createOpenerParams()
            params.loanapp.borrower = o;
            return params
        }
    ] as EditorListModel;
        
    void removeJointBorrower() {
        removeJointBorrowerImpl(selectedJointBorrower);
    }
            
    boolean removeJointBorrowerImpl(o) {
        if (caller.mode == 'read') return false;
        if (MsgBox.confirm("You are about to remove this joint borrower. Continue?")) {
            borrowers.remove(o);
            return true;
        } else { 
            return false; 
        } 
    } 
    
    def getHtmlview() {
        def htmlbuilder=new BorrowerInfoHtmlBuilder();
        return htmlbuilder.buildBorrower(selectedJointBorrower)
    }
}
