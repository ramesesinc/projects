package com.rameses.clfc.loan.otherlending;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.clfc.util.*;

class LoanAppOtherLendingController
{
    //feed by the caller
    def loanapp, caller, selectedMenu;
    
    @Binding
    def binding;
    
    @Service('LoanAppOtherLendingService') 
    def service; 
    
    def htmlbuilder = new OtherLendingHtmlBuilder();
    def otherlendings = [];
    def selectedOtherLending;
    
    void init() {
        selectedMenu.saveHandler = { save(); }  
        def data = service.open([objid: loanapp.objid]);
        loanapp.clear();
        loanapp.putAll(data);
        otherlendings = loanapp.otherlendings;
    }
        
    void save() {
        def data = [ objid: loanapp.objid, otherlendings: otherlendings ]
        service.update(data); 
    }

    def addOtherLending() {
        def handler = {otherlending->
            otherlending.parentid = loanapp.objid;
            otherlendings.add(otherlending);
            otherLendingHandler.reload();
        }
        return InvokerUtil.lookupOpener("otherlending:create", [handler:handler]);
    }
    
    def otherLendingHandler = [
        fetchList: {o->
            if( !otherlendings ) otherlendings = [];
            otherlendings.each{ it._filetype = "otherlending" }
            return otherlendings;
        },
        onRemoveItem: {o->
            return removeOtherLendingImpl(o);
        },
        getOpenerParams: {o->
            return [mode: caller.mode, caller:this];
        }
    ] as EditorListModel
            
    void removeOtherLending() {
        removeOtherLendingImpl(selectedOtherLending);
    }
    
    boolean removeOtherLendingImpl(o) {
        if(caller.mode == 'read') return false;
        if(MsgBox.confirm("You are about to remove this lending. Continue?")) {
            otherlendings.remove(o);
            return true;
        }
        return false;
    }
            
    def getHtmlview() {
        return htmlbuilder.buildOtherLending(selectedOtherLending);
    }
}