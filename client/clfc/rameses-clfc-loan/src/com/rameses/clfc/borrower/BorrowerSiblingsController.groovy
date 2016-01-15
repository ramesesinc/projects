package com.rameses.clfc.loan.borrower;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.util.*;

class BorrowerSiblingsController
{
    //feed by the caller
    def borrowerContext;
    
    @Binding
    def binding;
    def htmlbuilder = new BorrowerInfoHtmlBuilder();
    
    void init() {
        borrowerContext.addDataChangeHandler('siblings', {
            siblingsHandler.reload(); 
        });
    }    
    
    def selectedSibling;
    def siblingsHandler = [
        fetchList: {o->
            if( !borrowerContext.borrower.siblings ) borrowerContext.borrower.siblings = [];
            borrowerContext.borrower.siblings.each{ it._filetype = "sibling"; }
            return borrowerContext.borrower.siblings;
        },
        onRemoveItem: {o->
            return removeSiblingImpl(o);
        },
        getOpenerParams: {o->
            return [mode: borrowerContext.mode, caller:this]
        }
    ] as EditorListModel;
    
    def addSibling() {
        def handler = {sibling->
            sibling.borrowerid = borrowerContext.borrower?.objid;
            borrowerContext.borrower.siblings.add(sibling);
            siblingsHandler.reload();
        }
        return InvokerUtil.lookupOpener("sibling:create", [handler:handler])
    }    
    
    void removeSibling() {
        removeSiblingImpl(selectedSibling);
    }
            
    boolean removeSiblingImpl(o) {
        if (borrowerContext.mode == 'read') return false;
        if (MsgBox.confirm("You are about to remove this sibling. Continue?")) {
            borrowerContext.borrower.siblings.remove(o);
            return true;
        } else { 
            return false; 
        } 
    }
    
    def getHtmlview() {
        return htmlbuilder.buildSibling(selectedSibling);
    }
}