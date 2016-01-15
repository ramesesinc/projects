package com.rameses.clfc.borrower;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.util.*;

class BorrowerEmploymentController
{   
    //feed by the caller
    def borrowerContext;
    
    @Binding
    def binding;
    def htmlbuilder = new BorrowerInfoHtmlBuilder();
    
    void init() {
        borrowerContext.addDataChangeHandler('employment', {
            employmentHandler.reload(); 
        });
    }    
    
    def selectedEmployment;
    def employmentHandler = [
        fetchList: {o->
            if( !borrowerContext.borrower.employments ) borrowerContext.borrower.employments = [];
            borrowerContext.borrower.employments.each{ it._filetype = "employment" }
            return borrowerContext.borrower.employments;
        },
        onRemoveItem: {o->
            return removeItemImpl(o);
        },
        getOpenerParams: {o->
            return [mode: borrowerContext.mode, caller:this]
        }
    ] as EditorListModel; 
    
    def addEmployment() {
        def handler = {employment->
            employment.refid = borrowerContext.borrower?.objid;
            borrowerContext.borrower.employments.add(employment);
            employmentHandler.reload();
        }
        return InvokerUtil.lookupOpener("employment:create", [handler:handler])
    }    
    
    void removeEmployment() {
        removeEmploymentImpl(selectedEmployment);
    }
    
    boolean removeEmploymentImpl(o) {
        if (borrowerContext.mode == 'read') return false;
        if (MsgBox.confirm("You are about to remove this item. Continue?")) {
            borrowerContext.borrower.employments.remove(o);
            return true;
        } else { 
            return false; 
        } 
    }
    
    def getHtmlview() {
        return htmlbuilder.buildEmployment(selectedEmployment);
    }
}