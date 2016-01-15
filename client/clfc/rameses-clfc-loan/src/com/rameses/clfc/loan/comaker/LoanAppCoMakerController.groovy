package com.rameses.clfc.loan.comaker;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.util.*;

class LoanAppCoMakerController 
{
    //feed by the caller
    def loanapp, caller, selectedMenu;
    
    private def beforeSaveHandlers = [:];
    private def dataChangeHandlers = [:];
    
    @Service('ComakerService') 
    def service; 
    
    def borrowers = [];
    def selectedCoMaker;
    
    void init() {
        selectedMenu.saveHandler = { save(); }  
        def data = service.open([objid: loanapp.objid]);
        loanapp.clear();
        loanapp.putAll(data);
        borrowers = loanapp.comakers;
    }
        
    void save() {
        def data = [ objid: loanapp.objid, borrowers: borrowers ]
        service.update(data); 
        borrowers.each { 
            it.remove('_isnew'); 
        }        
    }

    def addCoMaker() {
        def params = createOpenerParams()
        params.callBackHandler = {comaker->
            comaker._isnew = true;
            borrowers.add(comaker);
            coMakerHandler.reload();
        };
        return InvokerUtil.lookupOpener("comaker:create", params)
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
    
    def coMakerHandler = [
        fetchList: {o->
            if( !borrowers ) borrowers = []
            borrowers.each{ it._filetype = "comaker" }
            return borrowers;
        },
        onRemoveItem: {o->
            return removeCoMakerImpl(o);
        },
        getOpenerParams: {o->
            def params = createOpenerParams()
            params.loanapp.borrower = o;
            return params;
        }
    ] as EditorListModel;
        
    void removeCoMaker() {
        removeCoMakerImpl(selectedCoMaker);
    }
            
    boolean removeCoMakerImpl(o) {
        if (caller.mode == 'read') return false;
        if (MsgBox.confirm("You are about to remove this co-maker. Continue?")) {
            borrowers.remove(o);
            return true;
        } else { 
            return false; 
        } 
    }
    
    def getHtmlview() {
        def htmlbuilder=new BorrowerInfoHtmlBuilder();
        return htmlbuilder.buildBorrower(selectedCoMaker);
    }
}
