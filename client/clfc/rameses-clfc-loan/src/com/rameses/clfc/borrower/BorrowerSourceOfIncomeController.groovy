package com.rameses.clfc.borrower;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.util.*;

class BorrowerSourceOfIncomeController
{
    //feed by the caller
    def borrowerContext;
    
    @Binding
    def binding;
    def htmlbuilder = new BorrowerInfoHtmlBuilder();
    
    void init() {
        borrowerContext.addDataChangeHandler('otherincome', {
            otherIncomeHandler.reload(); 
        });
    }    
    
    def selectedOtherIncome;
    def otherIncomeHandler = [
        fetchList: {o->
            if( !borrowerContext.borrower.otherincomes ) borrowerContext.borrower.otherincomes = [];
            borrowerContext.borrower.otherincomes.each{ it._filetype = "otherincome"; }
            return borrowerContext.borrower.otherincomes;
        },
        onRemoveItem: {o->
            return removeOtherIncomeImpl(o);
        },
        getOpenerParams: {o->
            return [mode: borrowerContext.mode, caller:this]
        }
    ] as EditorListModel;
    
    def addOtherIncome() {
        def handler = {otherincome->
            otherincome.refid = borrowerContext.borrower?.objid;
            borrowerContext.borrower.otherincomes.add(otherincome);
            otherIncomeHandler.reload();
        }
        return InvokerUtil.lookupOpener("otherincome:create", [handler:handler]);
    }
    
    void removeOtherIncome() {
        removeOtherIncomeImpl(selectedOtherIncome);
    }
            
    boolean removeOtherIncomeImpl(o) {
        if (borrowerContext.mode == 'read') return false;
        if (MsgBox.confirm("You are about to remove this item. Continue?")) {
            borrowerContext.borrower.otherincomes.remove(o);
            return true;
        } else { 
            return false; 
        } 
    }
    
    def getHtmlview() {
        return htmlbuilder.buildOtherIncome(selectedOtherIncome);
    }
}