package com.rameses.clfc.borrower;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.util.*;

class BorrowerSavingsAcctController
{
    //feed by the caller
    def borrowerContext;
    
    @Binding
    def binding;
    def htmlbuilder = new BorrowerInfoHtmlBuilder();
            
    void init() {
        borrowerContext.addDataChangeHandler('savingacct', {
            savingsAcctHandler.reload(); 
        });
    }    
    
    def selectedSavingAcct;
    def savingsAcctHandler = [
        fetchList: {o->
            if( !borrowerContext.borrower.savingaccts ) borrowerContext.borrower.savingaccts = [];
            borrowerContext.borrower.savingaccts.each{ it._filetype = "saving"; }
            return borrowerContext.borrower.savingaccts;
        },
        onRemoveItem: {o->
            return removeSavingAcctImpl(o);
        },
        getOpenerParams: {o->
            return [mode: borrowerContext.mode, caller:this]
        }
    ] as EditorListModel; 
    
    def addSavingAcct() {
        def handler = {acct->
            acct.borrowerid = borrowerContext.borrower?.objid;
            borrowerContext.borrower.savingaccts.add(acct);
            savingsAcctHandler.reload();
        }
        return InvokerUtil.lookupOpener("saving:create", [handler:handler]);
    }    
    
    void removeSavingAcct() {
        removeSavingAcctImpl(selectedSavingAcct);
    }
            
    boolean removeSavingAcctImpl(o) {
        if (borrowerContext.mode == 'read') return false;
        if (MsgBox.confirm("You are about to remove this savings account. Continue?")) {
            borrowerContext.borrower.savingaccts.remove(o);
            return true;
        } else { 
            return false; 
        } 
    }
    
    def getHtmlview() {
        return htmlbuilder.buildBankAccount(selectedSavingAcct);
    }
}